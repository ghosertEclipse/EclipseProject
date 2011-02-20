#!/usr/bin/python
# encoding: utf-8

import os
import sys

from PyQt4.QtGui import *
from PyQt4.QtCore import *
from PyQt4.QtWebKit import *
from PyQt4.QtNetwork import *

import ahkpython

class CookieJar(QNetworkCookieJar):
    
    CookiePath = '/home/jiawzhang/Templates/cookies'
    
    def __init__(self, parent = None):
        QNetworkCookieJar.__init__(self, parent)
        self.loadAllCookies()
    
    def saveAllCookies(self):
        allCookies = QNetworkCookieJar.allCookies(self)
        with open(CookieJar.CookiePath, 'w') as f:
            lines = ''
            for cookie in allCookies:
                lines = lines + cookie.toRawForm() + '\r\n'
            f.writelines(lines)
    
    def loadAllCookies(self):
        if os.path.exists(CookieJar.CookiePath):
            with open(CookieJar.CookiePath, 'r') as f:
                lines = ''
                for line in f:
                    lines = lines + line
                allCookies = QNetworkCookie.parseCookies(lines)
                QNetworkCookieJar.setAllCookies(self, allCookies)
                
import re
import time
import threading
from datetime import datetime

class UserInfo:
    Status_Not_Processing = 1
    Status_Processing = 2
    Status_NotTo_Buy = 3
    Status_Will_Buy = 4
    Status_Confirmed_Buy = 5
    Status_Failed_Buy = 6
    Status_Succeed_Buy = 7
    Status_Confirmed_Order = 8
    def __init__(self, taobaoId, itemLink, wangwangLink, buyer_payment, max_acceptable_price, seller_payment):
        self.taobaoId = taobaoId
        self.itemLink = itemLink
        self.wangwangLink = wangwangLink
        self.buyer_payment = buyer_payment
        self.max_acceptable_price = max_acceptable_price
        self.seller_payment = seller_payment
        self.status = UserInfo.Status_Not_Processing
        self.last_status_time = datetime.now()
        self.alipayLink = None

class UserInfoManager:
    userMap = {}
    def addUserInfo(self, userInfo):
        # If the userInfo exists in map, below is the logic which allow to continue. Otherwise just return, don't add userInfo anymore.
        if UserInfoManager.userMap.has_key(userInfo.taobaoId):
            # Succeed buy/Confirmed order and the last status time more than 31 days will allow to continue.
            if userInfo.status == UserInfo.Status_Succeed_Buy or userInfo.status == UserInfo.Status_Confirmed_Order:
                if (datetime.now() - userInfo.last_status_time).days <= 31:
                    return
            # NotTo buy and the last status time more than 6 days will allow to continue.
            elif userInfo.status == UserInfo.Status_NotTo_Buy:
                if (datetime.now() - userInfo.last_status_time).days <= 6:
                    return
            # Failed buy and the last status time more than 3 days will allow to continue.
            elif userInfo.status == UserInfo.Status_Failed_Buy:
                if (datetime.now() - userInfo.last_status_time).days <= 3:
                    return
            else:
                return
                    
        if userInfo.buyer_payment <= userInfo.max_acceptable_price:
            UserInfoManager.userMap[userInfo.taobaoId] = userInfo
            
    def getUserInfoMap(self):
        return UserInfoManager.userMap
    
    def setUserInfoStatus(self, userInfo, status, alipayLink = None):
        userInfo.status = status
        if (status == UserInfo.Status_Confirmed_Buy) and alipayLink:
            userInfo.alipayLink = alipayLink
        userInfo.last_status_time = datetime.now()

class AutoAction(QObject):
    
    userInfoManager = UserInfoManager()
    
    def __clickOn(self, element):
        clickOnString = None
        # <a> has a different implementation javascript for simulating clicking on it.
        if element.tagName() == 'A':
            clickOnString = "var evObj = document.createEvent('MouseEvents');evObj.initEvent( 'click', true, true );this.dispatchEvent(evObj);"
        else:
            clickOnString = 'this.click();'
        return element.evaluateJavaScript(clickOnString)
    
    def __setValueOn(self, element, value):
        "Support most 'input' tag  with 'textarea' tag either, since the value could be Chinese, make sure it passed in as unicode based."
        # We can also use element.setAttribute('value', value) instead, but this clause fail to work in some cases, it's safer to use javascript based set-value function.
        # When invoking string1.format(string2), make sure both string1 & string2 are unicode based like below.
        if element.tagName() == 'TEXTAREA':
            # textarea element should be focus first, then set innerHTML.
            element.setFocus()
            return element.evaluateJavaScript(u'this.innerHTML="{0}";'.format(value))
        else:
            return element.evaluateJavaScript(u'this.value="{0}";'.format(value))

    def __isCheckedOn(self, element):
        "Check whether 'input' tag with 'checkbox' type is checked or not."
        checked = element.evaluateJavaScript('this.checked;').toString()
        if checked == 'true':
            return True
        else:
            return False
    
    def __submitForm(self, element):
        "Submit the form directly."
        return element.evaluateJavaScript('this.submit();')
    
    def __keyupOn(self, element):
        "This is major for input box."
        keyupOnString = "var evObj = document.createEvent('UIEvents');evObj.initEvent( 'keyup', true, true );this.dispatchEvent(evObj);"
        return element.evaluateJavaScript(keyupOnString)
    
    def __init__(self, keyword, username, password, alipayPassword, max_acceptable_price, seller_payment, message_to_seller):
        QObject.__init__(self)
        self.connect(self, SIGNAL('asynClickOn'), self.__clickOn)
        self.keyword = keyword
        self.username = username
        self.password = password
        self.alipayPassword = alipayPassword
        self.max_acceptable_price = max_acceptable_price
        self.seller_payment = seller_payment
        self.message_to_seller = message_to_seller
    
    def home(self, frame):
        searchBox = frame.findFirstElement('input#q')
        self.__setValueOn(searchBox, self.keyword)
        searchButton = frame.findFirstElement('button.tsearch-submit')
        self.__clickOn(searchButton)
    
    def search(self, frame):
        # See whether the current user login or not.
        p_login_info = frame.findFirstElement('p.login-info')
        isLogin = p_login_info.findFirst('a')
        if not isLogin.hasClass('user-nick'):
            self.__clickOn(isLogin)
            return
                
        # check wang wang online option, check it if it's not checked.
        wwonline = frame.findFirstElement('input#filterServiceWWOnline')
        if not self.__isCheckedOn(wwonline):
            self.__clickOn(wwonline)
            confirmButton = frame.findFirstElement('button#J_SubmitBtn')
            self.__clickOn(confirmButton)
            return
        else:
            userInfo = UserInfo(u'代理梦想家80后', u'http://item.taobao.com/item.htm?id=9248227645',
            QUrl.fromPercentEncoding(u'http://www.taobao.com/webww/?ver=1&&touid=cntaobao%E4%BB%A3%E7%90%86%E6%A2%A6%E6%83%B3%E5%AE%B680%E5%90%8E&siteid=cntaobao&status=1&portalId=&gid=9190349629&itemsId='),
            0.80, 0.90, 1.00)
            AutoAction.userInfoManager.addUserInfo(userInfo)
            
            # jiawzhang TODO: should handle the legacy userInfos first, then search new users from search page.
            
            userInfoMap = AutoAction.userInfoManager.getUserInfoMap()
            
            # for taobaoId, userInfo in userInfoMap.iteritems():
                # UserInfo.Status_Processing
                # UserInfo.Status_Will_Buy # make sure change WillBuy to Not Processing if the last status time is more than 1 day from since.
                # UserInfo.Status_Confirmed_Buy # caculate the time and decided whether to go alipay, if yes, load userInfo.alipayLink
            for taobaoId, userInfo in userInfoMap.iteritems():
                if userInfo.status == UserInfo.Status_Not_Processing:
                    # jiawzhang TODO: send query message to taobaoId in wangwang.
                    # jiawzhang TODO: after sending queries, change status to processing
                    pass
                # jiawzhang TODO: set not to buy and will buy status here.
                # jiawzhang TODO: will buy flow:
                tabWidget = frame.page().view().tabWidget
                view = WebView(tabWidget, userInfo)
                view.load(QUrl(userInfo.itemLink))
            
            return
    
    def login(self, frame):

        # Uncheck the safeLoginCheckbox to simplify the process, otherwise, I have to care activex seurity inputbox on the page.
        safeLoginCheckbox = frame.findFirstElement('input#J_SafeLoginCheck')
        if (self.__isCheckedOn(safeLoginCheckbox)):
            # if safeLoginChecked, uncheck it first.
            self.__clickOn(safeLoginCheckbox)
                
        username = frame.findFirstElement('input#TPL_username_1')
        password = frame.findFirstElement('span#J_StandardPwd input.login-text')
        self.__setValueOn(username, self.username)
        self.__setValueOn(password, self.password)
        loginButton = frame.findFirstElement('button.J_Submit')
        self.__clickOn(loginButton)
        
    def item(self, frame, userInfo):
        # jiawzhang TODO: change it to 61 on production.
        time.sleep(5)
        # test whether there is a username/password div pop up after clicking on buy now link.
        username = frame.findFirstElement('input#TPL_username_1')
        if not username.isNull():
            self.login(frame)
            return
        buynow = frame.findFirstElement('a#J_LinkBuy')
        if buynow.isNull():
            # Set status to failed buy if the item is offline.
            AutoAction.userInfoManager.setUserInfoStatus(userInfo, UserInfo.Status_Failed_Buy)
        else:
            self.__clickOn(buynow)
    
    def buy(self, frame, userInfo):
        message_box = frame.findFirstElement('textarea#J_msgtosaler')
        self.__setValueOn(message_box, self.message_to_seller)
        confirmButton = frame.findFirstElement('input#performSubmit')
        self.__clickOn(confirmButton)
        
    def alipay(self, frame, userInfo):
        # Set status to confirmed buy and save the alipay link.
        AutoAction.userInfoManager.setUserInfoStatus(userInfo, UserInfo.Status_Confirmed_Buy, frame.url().toString())
        
        confirmButton = frame.findFirstElement('input.J_ForAliControl')
        if not confirmButton.isNull():
            ahkpython.sendAlipayPassword(self.alipayPassword)
            # jiawzhang XXX: Should Have a Async Click On Confirm Button for Security ActiveX Inputbox
            # Other wise, you will always get button clicked first, then fill the password into the Security ActiveX Inputbox, which lead to issues.
            autoAction = self
            class AsynCall(threading.Thread):
                def run(self):
                    time.sleep(2)
                    autoAction.emit(SIGNAL('asynClickOn'), confirmButton)
            AsynCall().start()
        else:
            # Set status to fail to buy if there is no comfirmed button for alipay page.
            AutoAction.userInfoManager.setUserInfoStatus(userInfo, UserInfo.Status_Failed_Buy)
    
    def pay_success(self, frame, userInfo):
        # Set status to completed buy.
        AutoAction.userInfoManager.setUserInfoStatus(userInfo, UserInfo.Status_Succeed_Buy)
        print 'jiawzhang TODO: close current tab.'
            
    def pay_fail(self, frame, userInfo):
        # Set status to fail to buy.
        AutoAction.userInfoManager.setUserInfoStatus(userInfo, UserInfo.Status_Failed_Buy)
        print 'jiawzhang TODO: close current tab.'
        
    def perform(self, frame, url, userInfo):
        if (re.search(r'^http://www\.taobao\.com', url)):
            self.home(frame)
        elif (re.search(r'^http://s\.taobao\.com/search\?q=', url)):
            self.search(frame)
        elif (re.search(r'^https://login\.taobao\.com/member/login\.jhtml', url)):
            self.login(frame)
        elif (re.search(r'^http://item\.taobao\.com/item\.htm\?id=', url)):
            self.item(frame, userInfo)
        elif (re.search(r'^http://buy\.taobao\.com/auction/buy_now\.jhtml', url)):
            self.buy(frame, userInfo)
        elif (re.search(r'^https://cashier\.alipay\.com/standard/payment/cashier\.htm', url)):
            self.alipay(frame, userInfo)
        elif (re.search(r'^http://trade\.taobao\.com/trade/pay_success\.htm', url)):
            self.pay_success(frame, userInfo)
        elif (re.search(r'^https://cashier\.alipay\.com/home/error\.htm', url)):
            self.pay_fail(frame, userInfo)
        else:
            return
        
class WebView(QWebView):
    
    cookieJar = CookieJar()
    # Make sure fill in unicode characters.
    autoAction = AutoAction(u'捷易通加款1元', u'ghosert', u'011849', u'011849', 0.90, 1.00, u'捷易通ID: ghosert')
    
    def __init__(self, tabWidget = None, userInfo = None):
        QWebView.__init__(self, tabWidget)
        networkAccessManager = self.page().networkAccessManager()
        networkAccessManager.setCookieJar(self.__class__.cookieJar)
        tabWidget.setCurrentIndex(tabWidget.addTab(self, 'loading'))
        tabWidget.connect(self, SIGNAL('loadStarted()'), self.load_started)
        tabWidget.connect(self, SIGNAL('loadFinished(bool)'), self.load_finished)
        tabWidget.connect(self, SIGNAL('loadProgress(int)'), self.load_progress)
        self.tabWidget = tabWidget
        self.frame = self.page().currentFrame()
        self.userInfo = userInfo
    
    def createWindow(self, webWindowType):
        return WebView(self.tabWidget)
    
    def load_started(self):
        pass
        
    def load_finished(self, status):
        self.__class__.cookieJar.saveAllCookies()
        self.tabWidget.setTabText(self.tabWidget.indexOf(self.sender()), self.sender().title())
        self.__class__.autoAction.perform(self.frame, self.url().toString(), self.userInfo)
    
    def load_progress(self, progress):
        self.tabWidget.setTabText(self.tabWidget.indexOf(self.sender()), str(progress) + "%")
        
class MainContainer(QMainWindow):
    def __init__(self, parent = None):
        QMainWindow.__init__(self, parent)
        
        self.tabWidget = QTabWidget(self)
        self.tabWidget.setTabsClosable(True)
        self.connect(self.tabWidget, SIGNAL('tabCloseRequested(int)'), self.tabWidget.removeTab)
        self.setCentralWidget(self.tabWidget)
        
        view = WebView(self.tabWidget)
        view.load(QUrl("http://www.taobao.com/"))
        # view.load(QUrl("http://item.taobao.com/item.htm?id=9248227645"))

if __name__ == '__main__':
    app = QApplication(sys.argv)
    main = MainContainer()
    
    # Enable plugins here will make activex turn on, it's important to taobao security activex control
    QWebSettings.globalSettings().setAttribute(QWebSettings.PluginsEnabled, True)
    # QWebSettings.globalSettings().setAttribute(QWebSettings.AutoLoadImages, False)
    QWebSettings.globalSettings().enablePersistentStorage("/home/jiawzhang/Templates")
        
    main.setWindowTitle(u'淘宝随意拍')
    main.show()
    app.exec_()
    
