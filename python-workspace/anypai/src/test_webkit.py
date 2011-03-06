#!/usr/bin/python
# encoding: utf-8

import os
import sys

from PyQt4.QtGui import *
from PyQt4.QtCore import *
from PyQt4.QtWebKit import *
from PyQt4.QtNetwork import *

StoragePath = './storage/'

class CookieJar(QNetworkCookieJar):
    
    CookiePath = StoragePath + 'cookies'
    
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

import thread_util
from Queue import Queue

import platform
if platform.system() == 'Windows':
    import ahkpython

import database
from database import UserInfo

class UserInfoManager:
    def __init__(self, ownerTaobaoId, max_acceptable_price):
        self.ownerTaobaoId = ownerTaobaoId
        self.max_acceptable_price = max_acceptable_price

    def addUserInfo(self, newUserInfo):
        # Don't buy the item from yourself.
        if self.ownerTaobaoId == newUserInfo.taobaoId:
            return
        
        if newUserInfo.buyer_payment > self.max_acceptable_price:
            return
        
        # If the userInfo exists, below is the logic which allow to continue. Otherwise just return, don't add userInfo anymore.
        userInfo = database.getActiveUserByTaobaoId(newUserInfo.taobaoId)
        if userInfo:
            # Succeed buy/Confirmed order and the last status time more than 31 days will allow to continue.
            if (userInfo.status == UserInfo.Status_Succeed_Buy or userInfo.status == UserInfo.Status_Confirmed_Payment or
                userInfo.status == UserInfo.Status_Refunded_Payment):
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
            elif userInfo.status == UserInfo.Status_RETRY:
                pass
            else:
                return
            database.updateUser(userInfo, active = 0)
        database.saveUser(newUserInfo)
            
    def getUnhandledUserInfoList(self):
        return database.getUnhandledUserInfoList()
    
    def setUserInfoStatus(self, userInfo, status, alipayLink = None):
        if (status == UserInfo.Status_Confirmed_Buy) and alipayLink:
            pass
        else:
            alipayLink = None
        database.updateUser(userInfo, status = status, alipayLink = alipayLink, last_status_time = datetime.now())

class AutoAction(QObject):
    
    channel = thread_util.Channel()
    
    alipayChannel = thread_util.Channel()
    # allow only one alipay page at a time.
    alipayChannel.startConsumer(1)
    
    # jiawzhang TODO: remove this later.
    debug_num = 0
    
    def __init__(self, keyword, username, password, alipayPassword, max_acceptable_price, seller_payment, message_to_seller):
        QObject.__init__(self)
        # jiawzhang XXX: if your signal is sent from sub-thread, set Qt.BlockingQueuedConnection will make sure:
        # after emit signal, the sub-thread will be blocked until the corresponding slot is completed.
        # If the signal is sent from the same thread, the default behavior is already similar like above.
        self.connect(self, SIGNAL('asynClickOn'), self.__clickOn, Qt.BlockingQueuedConnection)
        self.connect(self, SIGNAL('new_webview'), self.__new_webview, Qt.BlockingQueuedConnection)
        self.connect(self, SIGNAL('stop_webview'), self.__stop_webview, Qt.BlockingQueuedConnection)
        self.keyword = keyword
        self.username = username
        self.password = password
        self.alipayPassword = alipayPassword
        self.max_acceptable_price = max_acceptable_price
        self.seller_payment = seller_payment
        self.message_to_seller = message_to_seller
        self.userInfoManager = UserInfoManager(username, max_acceptable_price)
        # jiawzhang TODO: this number should be configurable later.
        self.queue = Queue(5)
        
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
    
    def home(self, frame):
        # When visiting the home, begin to create view and put them to the queue, sub-threads will leverage them later.
        for i in range(self.queue.maxsize):
            view = WebView(frame.page().view().tabWidget, self)
            self.queue.put(view)
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
            class AsynHandler(threading.Thread):
                def __init__(self, autoAction, items, nextPage):
                    threading.Thread.__init__(self)
                    self.autoAction = autoAction
                    self.items = items
                    self.nextPage = nextPage
                def run(self):
                    # handle legacy userInfos here.
                    self.autoAction.handleUserInfoList()
                    
                    for item in self.items:
                        self.autoAction.userInfoManager.addUserInfo(item)
                    
                    # handle new userInfos from search page here.
                    self.autoAction.handleUserInfoList()
                    
                    # click on next page on search page for capturing new taobao items.
                    if not self.nextPage.isNull():
                        print 'call next page here.'
                        self.autoAction.emit(SIGNAL('asynClickOn'), self.nextPage)
                    else:
                        # jiawzhang TODO: add something to prompt it stopped here.
                        print 'all the search pages are handled.'
            
            # We can't have frame.findXXXX functions in sub-thread, so I promote the code snip on parsing frame here.
            items = frame.findFirstElement('ul.list-view').findAll('li.list-item').toList()
            for index, item in enumerate(items):
                itemLink = unicode(item.findFirst('h3.summary a').attribute('href', ''))
                buyer_payment = float(item.findFirst('ul.attribute li.price em').toPlainText())
                shipping_fee = float(item.findFirst('ul.attribute li.price span.shipping').toPlainText()[3:])
                buyer_payment = buyer_payment + shipping_fee
                taobaoId = unicode(item.findFirst('p.seller a').toPlainText())
                # wangwangLink is not present always, reload the page if it fail to get wangwangLink.
                wangwangLink = unicode(item.findFirst('a.ww-inline').attribute('href', ''))
                # jiawzhang XXX: uncomment the three line below, since if wangwangLink always '', the performance is poor, we don't need wangwangLink at this moment.
#                if (wangwangLink == ''):
#                    frame.page().action(QWebPage.Reload).trigger()
#                    return
                wangwangLink = unicode(QUrl.fromPercentEncoding(wangwangLink))
                items[index] = UserInfo(taobaoId, itemLink, wangwangLink, buyer_payment, self.seller_payment)
            nextPage = frame.findFirstElement('div.page-bottom a.page-next')
            
            # jiawzhang TODO: make sure we clear all the sub-threads we made when we exit app or press stop button on the GUI.
            asyncHandler = AsynHandler(self, items, nextPage)
            asyncHandler.setDaemon(True)
            asyncHandler.start()
            return

    def handleUserInfoList(self):
        # jiawzhang XXX: for channel.putRequest in a loop, make sure you always pass in variable like userInfo by constructor,
        # Otherwise, you may get duplicated userInfo in your Request class, not sure whether this is a pyqt bug.
        class MyRequest(thread_util.Request):
            def __init__(self, autoAction, userInfo):
                self.userInfo = userInfo
                self.autoAction = autoAction
            def doAction(self):
                if (datetime.now() - self.userInfo.last_status_time).days > 1:
                    if self.userInfo.status == UserInfo.Status_Confirmed_Buy:
                        self.autoAction.userInfoManager.setUserInfoStatus(self.userInfo, UserInfo.Status_Failed_Buy)
                    else:
                        self.autoAction.userInfoManager.setUserInfoStatus(self.userInfo, UserInfo.Status_RETRY)
                    return
                    
                # jiawzhang TODO: wangwang message version:
                # if status == Not_Processing:
                # send query message to taobaoId in wangwang.
                # after sending queries, change status to processing.
                # set not to buy and will buy status here based on wangwang response message.
                    
                # Instance purchase version:
                if self.userInfo.status == UserInfo.Status_Not_Processing or self.userInfo.status == UserInfo.Status_Processing:
                    self.autoAction.userInfoManager.setUserInfoStatus(self.userInfo, UserInfo.Status_Will_Buy)
                    
                link = None
                if self.userInfo.status == UserInfo.Status_Will_Buy:
                    link = self.userInfo.itemLink
                elif self.userInfo.status == UserInfo.Status_Confirmed_Buy:
                    link = self.userInfo.alipayLink
                        
                if link:
                    view = self.autoAction.queue.get()
                    view.userInfo = self.userInfo
                    self.autoAction.emit(SIGNAL('new_webview'), view, link)
                        
                    # waiting here, until view.userInfo is not None, means the whole buy flow completed.
                    # Any following flows which will be terminated should:
                    # 1. Set user status
                    # 2. Invoke self.__terminateCurrentFlow(frame) to make sure the corresponding worker thread can jump out of the while below and pick up the next request.
                    waitingInterval = 0
                    while view.userInfo != None:
                        time.sleep(2)
                        waitingInterval = waitingInterval + 2
                        if waitingInterval > 30:
                            # jiawzhang TODO: set time out here, the number should be configurable, force stop the view and try again, this will trigger WebView.load_finished
                            # Test the situation If buy now page is blocked because of security picture verification.
                            print 'try stopping current page.'
                            self.autoAction.emit(SIGNAL('stop_webview'), view)
                            waitingInterval = 0
                            
                    # push back the view for reusing.
                    self.autoAction.queue.put(view)
                    
                    # debug info here.
                    AutoAction.debug_num = AutoAction.debug_num + 1
                    print 'item completed: ' + str(AutoAction.debug_num) + ' ' + self.userInfo.taobaoId + ' ' + str(self.userInfo.buyer_payment) + ' ' + str(self.userInfo.last_status_time)
                        
        userInfoList = self.userInfoManager.getUnhandledUserInfoList()
        if userInfoList:
            for userInfo in userInfoList:
                AutoAction.channel.putRequest(MyRequest(self, userInfo))
            AutoAction.channel.startConsumer(self.queue.maxsize, True, False)
            AutoAction.channel.waitingForConsumerExist()
            
    def __new_webview(self, view, link):
        view.load(QUrl(link))
        
    def __stop_webview(self, view):
        view.stop()
    
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
        # test whether there is a username/password div pop up after clicking on buy now link.
        username = frame.findFirstElement('input#TPL_username_1')
        if not username.isNull():
            self.login(frame)
            return
        buynow = frame.findFirstElement('a#J_LinkBuy')
        if buynow.isNull():
            # Set status to failed buy if the item is offline.
            self.userInfoManager.setUserInfoStatus(userInfo, UserInfo.Status_Failed_Buy)
            self.__terminateCurrentFlow(frame)
        else:
            # jiawzhang TODO: change this to 61 seconds when on production.
            self.__asyncall(61, buynow)
    
    def buy(self, frame, userInfo):
        shippingFirstOption = frame.findFirstElement('table#trade-info tbody tr#J_Post input#shipping1')
        if not shippingFirstOption.isNull():
            self.__clickOn(shippingFirstOption)
        message_box = frame.findFirstElement('textarea#J_msgtosaler')
        self.__setValueOn(message_box, self.message_to_seller)
        
        # Verify buyer_payment here with actual price including shipping fee here.
        actualPrice = float(frame.findFirstElement('span.actual-price strong#J_ActualFee').toPlainText())
        if (userInfo.buyer_payment == actualPrice):
            confirmButton = frame.findFirstElement('input#performSubmit')
            
            # Add this multiple thread so that I make sure only one alipay page will be handled at a time.
            class AlipayRequest(thread_util.Request):
                def __init__(self, autoAction, confirmButton, frame):
                    self.autoAction = autoAction
                    self.confirmButton = confirmButton
                    self.frame = frame
                def doAction(self):
                    self.autoAction.emit(SIGNAL('asynClickOn'), self.confirmButton)
                    view = self.frame.page().view()
                    while view.userInfo != None:
                        time.sleep(2)
            AutoAction.alipayChannel.putRequest(AlipayRequest(self, confirmButton, frame))
            
        else:
            # Set status to retry if the price of item is changed.
            self.userInfoManager.setUserInfoStatus(userInfo, UserInfo.Status_RETRY)
            self.__terminateCurrentFlow(frame)
        
    def alipay(self, frame, userInfo):
        # Set status to confirmed buy and save the alipay link.
        self.userInfoManager.setUserInfoStatus(userInfo, UserInfo.Status_Confirmed_Buy, frame.url().toString())
        
        confirmButton = frame.findFirstElement('input.J_ForAliControl')
        if not confirmButton.isNull():
            ahkpython.sendAlipayPassword(self.alipayPassword)
            # jiawzhang XXX: Should Have a Async Click On Confirm Button for Security ActiveX Inputbox
            # Other wise, you will always get button clicked first, then fill the password into the Security ActiveX Inputbox, which lead to issues.
            self.__asyncall(2, confirmButton)
        else:
            # Set status to fail to buy if there is no comfirmed button for alipay page.
            self.userInfoManager.setUserInfoStatus(userInfo, UserInfo.Status_Failed_Buy)
            self.__terminateCurrentFlow(frame)
    
    def __asyncall(self, seconds, clickableElement):
        autoAction = self
        class AsynCall(threading.Thread):
            def run(self):
                time.sleep(seconds)
                autoAction.emit(SIGNAL('asynClickOn'), clickableElement)
        asyncCall = AsynCall()
        asyncCall.setDaemon(True)
        asyncCall.start()
        
    def pay_success(self, frame, userInfo):
        # Set status to completed buy.
        self.userInfoManager.setUserInfoStatus(userInfo, UserInfo.Status_Succeed_Buy)
        self.__terminateCurrentFlow(frame)
            
    def pay_fail(self, frame, userInfo):
        # Set status to fail to buy.
        self.userInfoManager.setUserInfoStatus(userInfo, UserInfo.Status_Failed_Buy)
        self.__terminateCurrentFlow(frame)
    
    def process_incomplete(self, frame, userInfo):
        # Set status to retry.
        self.userInfoManager.setUserInfoStatus(userInfo, UserInfo.Status_RETRY)
        self.__terminateCurrentFlow(frame)
    
    def __terminateCurrentFlow(self, frame):
        "Invoking on this method will make sure the worker thread pick up next request or to be stopped if no requests."
        frame.page().view().userInfo = None
        
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
            self.process_incomplete(frame, userInfo)
        
class WebView(QWebView):
    
    cookieJar = CookieJar()
    
    def __init__(self, tabWidget, autoAction):
        QWebView.__init__(self, tabWidget)
        networkAccessManager = self.page().networkAccessManager()
        networkAccessManager.setCookieJar(self.__class__.cookieJar)
        tabWidget.addTab(self, 'loading')
        # index = tabWidget.addTab(self, 'loading')
        # tabWidget.setCurrentIndex(index)
        tabWidget.connect(self, SIGNAL('loadStarted()'), self.load_started)
        tabWidget.connect(self, SIGNAL('loadFinished(bool)'), self.load_finished)
        tabWidget.connect(self, SIGNAL('loadProgress(int)'), self.load_progress)
        self.tabWidget = tabWidget
        self.frame = self.page().currentFrame()
        self.userInfo = None
        self.autoAction = autoAction
    
    def createWindow(self, webWindowType):
        return WebView(self.tabWidget, self.autoAction)
    
    def load_started(self):
        pass
        
    def load_finished(self, status):
        self.__class__.cookieJar.saveAllCookies()
        self.tabWidget.setTabText(self.tabWidget.indexOf(self.sender()), self.sender().title())
        self.autoAction.perform(self.frame, self.url().toString(), self.userInfo)
    
    def load_progress(self, progress):
        self.tabWidget.setTabText(self.tabWidget.indexOf(self.sender()), str(progress) + "%")
        
class MainContainer(QMainWindow):
    def __init__(self, parent = None):
        QMainWindow.__init__(self, parent)
        
        self.tabWidget = QTabWidget(self)
        self.tabWidget.setTabsClosable(True)
        self.connect(self.tabWidget, SIGNAL('tabCloseRequested(int)'), self.closeTab)
        self.setCentralWidget(self.tabWidget)
        
        # jiawzhang TODO: If running tons of view.load, I saw "Segmentation fault" and then PyQt exits, not sure Windows has the same issue, google it 'Segmentation fault Qt webkit'!
        
        # Make sure fill in unicode characters.
        autoAction = AutoAction(u'捷易通充值平台加款卡1元自动转帐', u'ghosert', u'011849', u'011849', 0.90, 1.00, u'捷易通ID: ghosert')
    
        view = WebView(self.tabWidget, autoAction)
        view.load(QUrl("http://www.taobao.com/"))
        # view.load(QUrl("http://item.taobao.com/item.htm?id=9248227645"))
    
    def closeTab(self, tabIndex):
        view = self.tabWidget.widget(tabIndex)
        view.close()
        self.tabWidget.removeTab(tabIndex)
    
    def closeEvent(self, event):
        self.tabWidget.clear()

if __name__ == '__main__':
    app = QApplication(sys.argv)
    main = MainContainer()
    
    # Enable plugins here will make activex turn on, it's important to taobao security activex control
    QWebSettings.globalSettings().setAttribute(QWebSettings.PluginsEnabled, True)
    # QWebSettings.globalSettings().setAttribute(QWebSettings.AutoLoadImages, False)
    QWebSettings.globalSettings().enablePersistentStorage(StoragePath)
    
    # create table if there is no table.
    if not os.path.exists(database.db_location):
        database.createTable()
        
    main.setWindowTitle(u'淘宝随意拍')
    main.show()
    app.exec_()
    
