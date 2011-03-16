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
    def addUserInfo(self, newUserInfo, ownerTaobaoId, max_acceptable_price):
        # Don't buy the item from yourself.
        if ownerTaobaoId == newUserInfo.taobaoId:
            return
        
        if newUserInfo.buyer_payment > max_acceptable_price:
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
            # Failed buy and the last status time more than 1 days will allow to continue.
            elif userInfo.status == UserInfo.Status_Failed_Buy:
                if (datetime.now() - userInfo.last_status_time).days <= 1:
                    return
            elif userInfo.status == UserInfo.Status_RETRY:
                pass
            else:
                return
            database.updateUser(userInfo, active = 0)
        database.saveUser(newUserInfo)
            
    def getUnhandledUserInfoList(self):
        return database.getUnhandledUserInfoList()
    
    def getActiveUserByTaobaoId(self, taobaoId):
        return database.getActiveUserByTaobaoId(taobaoId)
    
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
    
    userInfoManager = UserInfoManager()
    
    # jiawzhang TODO: remove this later.
    debug_num = 0
    
    def __init__(self, keyword, username, password, alipayPassword, max_acceptable_price, seller_payment, message_to_seller):
        QObject.__init__(self)
        # jiawzhang XXX: if your signal is sent from sub-thread, set Qt.BlockingQueuedConnection will make sure:
        # after emit signal, the sub-thread will be blocked until the corresponding slot is completed.
        # If the signal is sent from the same thread, the default behavior is already similar like above.
        self.connect(self, SIGNAL('asynClickOn'), self.clickOn, Qt.BlockingQueuedConnection)
        self.connect(self, SIGNAL('asynLocateCheckCodeInput'), self.locateCheckCodeInput, Qt.BlockingQueuedConnection)
        self.connect(self, SIGNAL('new_webview'), self.new_webview, Qt.BlockingQueuedConnection)
        self.connect(self, SIGNAL('stop_webview'), self.stop_webview, Qt.BlockingQueuedConnection)
        self.keyword = keyword
        self.username = username
        self.password = password
        self.alipayPassword = alipayPassword
        self.max_acceptable_price = max_acceptable_price
        self.seller_payment = seller_payment
        self.message_to_seller = message_to_seller
        # jiawzhang TODO: this number should be configurable later.
        self.queue = Queue(5)
        
    def clickOn(self, element):
        clickOnString = None
        # <a> has a different implementation javascript for simulating clicking on it.
        if element.tagName() == 'A':
            clickOnString = "var evObj = document.createEvent('MouseEvents');evObj.initEvent( 'click', true, true );this.dispatchEvent(evObj);"
        else:
            clickOnString = 'this.click();'
        return element.evaluateJavaScript(clickOnString)
    
    def locateCheckCodeInput(self, view, element):
        view.tabWidget.setCurrentIndex(view.tabWidget.indexOf(view))
        return element.evaluateJavaScript('this.focus();')
    
    def __setValueOn(self, element, value):
        "Support most 'input' tag  with 'textarea' tag either, since the value could be Chinese, make sure it passed in as unicode based."
        # We can also use element.setAttribute('value', value) instead, but this clause fail to work in some cases, it's safer to use javascript based set-value function.
        # When invoking string1.format(string2), make sure both string1 & string2 are unicode based like below.
        if element.tagName() == 'TEXTAREA':
            # textarea element should be focus first, then set innerHTML.
            return element.evaluateJavaScript(u'this.focus();this.innerHTML="{0}";'.format(value))
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
        self.clickOn(searchButton)
    
    def search(self, frame):
        # See whether the current user login or not.
        p_login_info = frame.findFirstElement('p.login-info')
        isLogin = p_login_info.findFirst('a')
        if not isLogin.hasClass('user-nick'):
            self.clickOn(isLogin)
            return
                
        # check wang wang online option, check it if it's not checked.
        wwonline = frame.findFirstElement('input#filterServiceWWOnline')
        # Sort items by price asc
        priceSortButton = frame.findFirstElement('ul#J_FilterToolbar a.by-price')
        if not self.__isCheckedOn(wwonline):
            self.clickOn(wwonline)
            confirmButton = frame.findFirstElement('button#J_SubmitBtn')
            self.clickOn(confirmButton)
            return
        elif re.match(r'.*?sort=price-asc.*', priceSortButton.attribute('href', '')):
            self.clickOn(priceSortButton)
            return
        else:
            # We can't have frame.findXXXX functions in sub-thread, so I promote the code snip on parsing frame here.
            items = frame.findFirstElement('ul.list-view').findAll('li.list-item').toList()
            for index, item in enumerate(items):
                itemLink = unicode(item.findFirst('h3.summary a').attribute('href', ''))
                buyer_payment = float(item.findFirst('ul.attribute li.price em').toPlainText())
                if buyer_payment > self.max_acceptable_price:
                    # jiawzhang TODO: terminate all flows here.
                    pass
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
                        AutoAction.userInfoManager.addUserInfo(item, self.autoAction.username, self.autoAction.max_acceptable_price)
                    
                    # handle new userInfos from search page here.
                    self.autoAction.handleUserInfoList()
                    
                    # click on next page on search page for capturing new taobao items.
                    if not self.nextPage.isNull():
                        print 'call next page here.'
                        self.autoAction.emit(SIGNAL('asynClickOn'), self.nextPage)
                    else:
                        # jiawzhang TODO: add something to prompt it stopped here.
                        print 'all the search pages are handled.'
            
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
                # jiawzhang TODO: for all the legacy unhandled items, maybe set them retry directly.
                if (datetime.now() - self.userInfo.last_status_time).days > 1:
                    if self.userInfo.status == UserInfo.Status_Confirmed_Buy:
                        AutoAction.userInfoManager.setUserInfoStatus(self.userInfo, UserInfo.Status_Failed_Buy)
                    else:
                        AutoAction.userInfoManager.setUserInfoStatus(self.userInfo, UserInfo.Status_RETRY)
                    return
                    
                # jiawzhang TODO: wangwang message version:
                # if status == Not_Processing:
                # send query message to taobaoId in wangwang.
                # after sending queries, change status to processing.
                # set not to buy and will buy status here based on wangwang response message.
                    
                # Instance purchase version:
                if self.userInfo.status == UserInfo.Status_Not_Processing or self.userInfo.status == UserInfo.Status_Processing:
                    AutoAction.userInfoManager.setUserInfoStatus(self.userInfo, UserInfo.Status_Will_Buy)
                    
                link = None
                if self.userInfo.status == UserInfo.Status_Will_Buy:
                    link = self.userInfo.itemLink
                elif self.userInfo.status == UserInfo.Status_Confirmed_Buy:
                    link = self.userInfo.alipayLink
                        
                if link:
                    view = self.autoAction.queue.get()
                    
                    view.condition.acquire()
                    try:
                        view.userInfo = self.userInfo
                        self.autoAction.emit(SIGNAL('new_webview'), view, link)
                            
                        # waiting here, until view.userInfo is not None, means the whole buy flow completed.
                        # Any following flows which will be terminated should:
                        # 1. Set user status
                        # 2. Invoke self.terminateCurrentFlow(frame) to make sure the corresponding worker thread can jump out of the while below and pick up the next request.
                        waitingInterval = 0
                        while view.userInfo != None:
                            view.condition.wait(2)
                            waitingInterval = waitingInterval + 2
                            if waitingInterval % 30 == 0:
                                # jiawzhang TODO: set time out here, the number should be configurable, force stop the view and try again, this will trigger WebView.load_finished
                                # Test the situation If buy now page is blocked because of security picture verification.
                                url = view.url().toString()
                                if (re.search(r'^http://www\.taobao\.com', url) or re.search(r'^http://s\.taobao\.com/search\?q=', url) or
                                    re.search(r'^https://login\.taobao\.com/member/login\.jhtml', url) or re.search(r'^http://item\.taobao\.com/item\.htm\?id=', url)):
                                    print 'try stopping home, search, login, item page.'
                                    self.autoAction.emit(SIGNAL('stop_webview'), view)
                                print 'This url takes long time more than 30s: ' + url
                    finally:
                        view.condition.release()
                            
                    # push back the view for reusing.
                    self.autoAction.queue.put(view)
                    
                    # debug info here.
                    AutoAction.debug_num = AutoAction.debug_num + 1
                    print 'item completed: ' + str(AutoAction.debug_num) + ' ' + self.userInfo.taobaoId + ' ' + str(self.userInfo.buyer_payment) + ' ' + str(self.userInfo.last_status_time)
                        
        userInfoList = AutoAction.userInfoManager.getUnhandledUserInfoList()
        if userInfoList:
            for userInfo in userInfoList:
                AutoAction.channel.putRequest(MyRequest(self, userInfo))
            AutoAction.channel.startConsumer(self.queue.maxsize, True, False)
            AutoAction.channel.waitingForConsumerExist()
            
    def new_webview(self, view, link):
        view.load(QUrl(link))
        
    def stop_webview(self, view):
        view.stop()
    
    def login(self, frame):
        # Uncheck the safeLoginCheckbox to simplify the process, otherwise, I have to care activex seurity inputbox on the page.
        safeLoginCheckbox = frame.findFirstElement('input#J_SafeLoginCheck')
        if (self.__isCheckedOn(safeLoginCheckbox)):
            # if safeLoginChecked, uncheck it first.
            self.clickOn(safeLoginCheckbox)
                
        username = frame.findFirstElement('input#TPL_username_1')
        password = frame.findFirstElement('span#J_StandardPwd input.login-text')
        self.__setValueOn(username, self.username)
        self.__setValueOn(password, self.password)
        loginButton = frame.findFirstElement('button.J_Submit')
        self.clickOn(loginButton)
        
    def item(self, frame, userInfo):
        # test whether there is a username/password div pop up after clicking on buy now link.
        username = frame.findFirstElement('input#TPL_username_1')
        if not username.isNull():
            self.login(frame)
            return
        buynow = frame.findFirstElement('a#J_LinkBuy')
        if buynow.isNull():
            # Set status to failed buy if the item is offline.
            AutoAction.userInfoManager.setUserInfoStatus(userInfo, UserInfo.Status_Failed_Buy)
            self.terminateCurrentFlow(frame)
        else:
            # jiawzhang TODO: change this to 61 seconds when on production.
            self.asyncall(5, buynow)
    
    def buy(self, frame, userInfo):
        shippingFirstOption = frame.findFirstElement('table#trade-info tbody tr#J_Post input#shipping1')
        if not shippingFirstOption.isNull():
            self.clickOn(shippingFirstOption)
        message_box = frame.findFirstElement('textarea#J_msgtosaler')
        self.__setValueOn(message_box, self.message_to_seller)
        
        # Verify buyer_payment here with actual price including shipping fee here.
        actualPriceString = unicode(frame.findFirstElement('span.actual-price strong#J_ActualFee').toPlainText())
        print 'Actual Price String: ' + actualPriceString + ' for ' + userInfo.taobaoId
        actualPrice = -10000.0 if actualPriceString == '' else float(actualPriceString)
        if (userInfo.buyer_payment == actualPrice):
            confirmButton = frame.findFirstElement('input#performSubmit')
            checkCodeInput = frame.findFirstElement('input#J_checkCodeInput')
            
            # Add this multiple thread so that I make sure only one alipay page will be handled at a time.
            class AlipayRequest(thread_util.Request):
                def __init__(self, autoAction, confirmButton, checkCodeInput, frame):
                    self.autoAction = autoAction
                    self.confirmButton = confirmButton
                    self.checkCodeInput = checkCodeInput
                    self.frame = frame
                def doAction(self):
                    print 'Begin Alipay Request ...'
                    view = self.frame.page().view()
                    view.condition.acquire()
                    try:
                        self.autoAction.emit(SIGNAL('asynClickOn'), self.confirmButton)
                        if not self.checkCodeInput.isNull():
                            self.autoAction.emit(SIGNAL('asynLocateCheckCodeInput'), view, self.checkCodeInput)
                        view.condition.wait()
                    finally:
                        view.condition.release()
                    print 'End Alipay Request ...'
            AutoAction.alipayChannel.putRequest(AlipayRequest(self, confirmButton, checkCodeInput, frame))
            
        else:
            # Set status to retry if the price of item is changed.
            AutoAction.userInfoManager.setUserInfoStatus(userInfo, UserInfo.Status_RETRY)
            self.terminateCurrentFlow(frame)
        
    def alipay(self, frame, userInfo):
        # Set status to confirmed buy and save the alipay link.
        # jiawzhang XXX: The string to be saved to sqlite must be unicode first, otherwise, error happens.
        AutoAction.userInfoManager.setUserInfoStatus(userInfo, UserInfo.Status_Confirmed_Buy, unicode(frame.url().toString()))
        
        confirmButton = frame.findFirstElement('input.J_ForAliControl')
        if not confirmButton.isNull():
            ahkpython.sendAlipayPassword(self.alipayPassword)
            # jiawzhang XXX: Should Have a Async Click On Confirm Button for Security ActiveX Inputbox
            # Other wise, you will always get button clicked first, then fill the password into the Security ActiveX Inputbox, which lead to issues.
            self.asyncall(2, confirmButton)
        else:
            # Set status to fail to buy if there is no comfirmed button for alipay page.
            AutoAction.userInfoManager.setUserInfoStatus(userInfo, UserInfo.Status_Failed_Buy)
            self.terminateCurrentFlow(frame)
    
    def asyncall(self, seconds, clickableElement):
        class AsynCall(threading.Thread):
            def __init__(self, autoAction, seconds, clickableElement):
                threading.Thread.__init__(self)
                self.autoAction = autoAction
                self.seconds = seconds
                self.clickableElement = clickableElement
            def run(self):
                time.sleep(self.seconds)
                self.autoAction.emit(SIGNAL('asynClickOn'), self.clickableElement)
        asyncCall = AsynCall(self, seconds, clickableElement)
        asyncCall.setDaemon(True)
        asyncCall.start()
        
    def pay_success(self, frame, userInfo):
        # Set status to completed buy.
        AutoAction.userInfoManager.setUserInfoStatus(userInfo, UserInfo.Status_Succeed_Buy)
        self.terminateCurrentFlow(frame)
            
    def pay_fail(self, frame, userInfo):
        # Set status to fail to buy.
        AutoAction.userInfoManager.setUserInfoStatus(userInfo, UserInfo.Status_Failed_Buy)
        self.terminateCurrentFlow(frame)
    
    def process_incomplete(self, frame, userInfo):
        # Set status to retry.
        AutoAction.userInfoManager.setUserInfoStatus(userInfo, UserInfo.Status_RETRY)
        self.terminateCurrentFlow(frame)
    
    def terminateCurrentFlow(self, frame):
        "Invoking on this method will make sure the worker thread pick up next request or to be stopped if no requests."
        view = frame.page().view()
        view.condition.acquire()
        try:
            view.userInfo = None
            view.condition.notifyAll()
        finally:
            view.condition.release()
        
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
            print 'find unexpected url: ' + url
            # self.process_incomplete(frame, userInfo)
        
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
        self.condition = threading.Condition()
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
        
class VerifyAction(AutoAction):
    def __init__(self, username, password, alipayPassword, userPayMap):
        QObject.__init__(self)
        self.connect(self, SIGNAL('asynClickOn'), self.clickOn, Qt.BlockingQueuedConnection)
        self.connect(self, SIGNAL('new_webview'), self.new_webview, Qt.BlockingQueuedConnection)
        self.connect(self, SIGNAL('stop_webview'), self.stop_webview, Qt.BlockingQueuedConnection)
        self.connect(self, SIGNAL('reload'), self.__reload, Qt.BlockingQueuedConnection)
        self.username = username
        self.password = password
        self.alipayPassword = alipayPassword
        self.userPayMap = userPayMap
        self.view = None
        
    def myTaobao(self, frame):
        # See whether the current user login or not.
        p_login_info = frame.findFirstElement('p.login-info')
        isLogin = p_login_info.findFirst('a')
        if not isLogin.hasClass('user-nick'):
            self.clickOn(isLogin)
            return
        infoCenter = frame.findAllElements('div.infocenter div.section ul li')
        # 待付款 infoCenter[0]
        # 待确认收货 infoCenter[1]
        # 待评价 infoCenter[2]
        confirmPayUrl = infoCenter[1].findFirst('a')
        if confirmPayUrl.isNull():
            # jiawzhang TODO: finish verify flow.
            print 'no item to be confirmed, finish verify flow'
            return
        self.view = WebView(frame.page().view().tabWidget, self)
        self.clickOn(confirmPayUrl)
    
    def listBoughtItems(self, frame):
        confirmUrlMap = {}
        items = frame.findAllElements('table#J_BoughtTable tbody').toList()
        for item in items:
            # taobao deal time: 2011-03-08 21:39
            dealTime = unicode(item.findFirst('span.deal-time').toPlainText())
            ymdhm = re.match(r'.*?(\d+)-(\d+)-(\d+) (\d+):(\d+).*?', dealTime).groups()
            dealTime = datetime(int(ymdhm[0]), int(ymdhm[1]), int(ymdhm[2]), int(ymdhm[3]), int(ymdhm[4]))
            taobaoId = unicode(item.findFirst('span.seller a').attribute('title', ''))
            if not self.userPayMap.has_key(taobaoId):
                continue
            seller_paytime = self.userPayMap[taobaoId]
            if seller_paytime < dealTime:
                continue
            confirmUrl = unicode(item.findFirst('td.operate a').attribute('href', ''))
            confirmUrlMap[taobaoId] = confirmUrl
            
        if not confirmUrlMap:
            print 'All the items are confirmed.'
            return
        
        class AsynHandler(threading.Thread):
            def __init__(self, autoAction, confirmUrlMap, frame):
                threading.Thread.__init__(self)
                self.autoAction = autoAction
                self.confirmUrlMap = confirmUrlMap
                self.frame = frame
            def run(self):
                view = self.autoAction.view
                for taobaoId, confirmUrl in confirmUrlMap.iteritems():
                    print 'Begin Verify Request ...'
                    view.condition.acquire()
                    try:
                        view.userInfo = AutoAction.userInfoManager.getActiveUserByTaobaoId(taobaoId)
                        self.autoAction.emit(SIGNAL('new_webview'), view, confirmUrl)
                        while view.userInfo != None:
                            view.condition.wait(2)
                    finally:
                        view.condition.release()
                    print 'End Verify Request ...'
                self.autoAction.emit(SIGNAL('reload'), frame)
            
        asyncHandler = AsynHandler(self, confirmUrlMap, frame)
        asyncHandler.setDaemon(True)
        asyncHandler.start()
        return
    
    def __reload(self, frame):
        frame.page().action(QWebPage.Reload).trigger()
    
    def confirmGoods(self, frame):
        ahkpython.sendAlipayPassword(self.alipayPassword)
        frame = frame.childFrames()[0]
        confirmButton = frame.findFirstElement('div.submit-line input.btn')
        # Below maybe the right way to confirm a js popup window.
        # 1. Start the confirm operation thread first, then press the confirm button to trigger the js popup.
        class PreJsConfirm(threading.Thread):
            def run(self):
                time.sleep(5)
                ahkpython.confirmJavaScript()
        self.asyncall(3, confirmButton)
        pjc = PreJsConfirm()
        pjc.setDaemon(True)
        pjc.start()
    
    def comment(self, frame):
        commentButton = frame.findFirstElement('a.long-btn')
        self.clickOn(commentButton)
        
    def rate(self, frame):
        okButton = frame.findFirstElement('td.ok input')
        self.clickOn(okButton)
        shopRatings = frame.findAllElements('div.shop-rating').toList()
        for shopRating in shopRatings:
            fiveStars = shopRating.findFirst('a.five-stars')
            self.clickOn(fiveStars)
        confirmButton = frame.findFirstElement('div.rate-submit button.btn')
        self.clickOn(confirmButton)
    
    def rateSuccess(self, frame, userInfo):
        # Set status to completed buy.
        AutoAction.userInfoManager.setUserInfoStatus(userInfo, UserInfo.Status_Confirmed_Payment)
        self.terminateCurrentFlow(frame)
        
    def perform(self, frame, url, userInfo):
        if (re.search(r'^http://i\.taobao\.com/', url)):
            self.myTaobao(frame)
        elif (re.search(r'^https://login\.taobao\.com/member/login\.jhtml', url)):
            self.login(frame)
        elif (re.search(r'^http://trade\.taobao\.com/trade/itemlist/listBoughtItems\.htm', url)):
            self.listBoughtItems(frame)
        elif (re.search(r'^http://trade\.taobao\.com/trade/itemlist/list_bought_items\.htm', url)):
            self.listBoughtItems(frame)
        elif (re.search(r'^http://trade\.taobao\.com/trade/confirm_goods\.htm', url)):
            self.confirmGoods(frame)
        elif (re.search(r'^http://trade\.taobao\.com/trade/trade_success\.htm', url)):
            self.comment(frame)
        elif (re.search(r'^http://rate\.taobao\.com/remark_seller\.jhtml\?$', url) or re.search(r'^http://rate\.taobao\.com/remarkSeller\.jhtml\?$', url)):
            self.rateSuccess(frame, userInfo)
        elif (re.search(r'^http://rate\.taobao\.com/remark_seller\.jhtml', url) or re.search(r'^http://rate\.taobao\.com/remarkSeller\.jhtml', url)):
            self.rate(frame)
        else:
            print 'find unexpected url: ' + url
            # self.process_incomplete(frame, userInfo)
            
class MainPanel(QWidget):
    def __init__(self, tabWidget = None):
        QWidget.__init__(self, tabWidget)
        self.tabWidget = tabWidget
        
        hLayout = QHBoxLayout()
        btBeginPai = QPushButton(u'开始拍货')
        btPayVerify = QPushButton(u'开始验证')
        hLayout.addWidget(btBeginPai)
        hLayout.addWidget(btPayVerify)
        
        self.textEdit = QTextEdit()
        
        vLayout = QVBoxLayout()
        vLayout.addLayout(hLayout)
        vLayout.addWidget(self.textEdit)
        
        self.setLayout(vLayout)
        
        self.connect(btBeginPai, SIGNAL('clicked()'), self.beginPai)
        self.connect(btPayVerify, SIGNAL('clicked()'), self.payVerify)
    
    def beginPai(self):
        # jiawzhang TODO: If running tons of view.load, I saw "Segmentation fault" and then PyQt exits, not sure Windows has the same issue, google it 'Segmentation fault Qt webkit'!
        # Make sure fill in unicode characters.
        autoAction = AutoAction(u'捷易通充值平台加款卡10元自动转帐', u'ghosert', u'011849', u'011849', 9.90, 10.00, u'捷易通ID: ghosert')
        view = WebView(self.tabWidget, autoAction)
        view.load(QUrl("http://www.taobao.com/"))
        # view.load(QUrl("http://item.taobao.com/item.htm?id=9248227645"))
        
    def payVerify(self):
        # 捷易通查单网址：
        # http://dx.jieyitong.net/system/index.asp
        seller_payment = 10.00
        jytPaymentString = unicode(self.textEdit.toPlainText())
        payList = jytPaymentString.split('\n')
        tdLength = 7
        if len(payList) < tdLength:
            return
        payList = payList[1 : len(payList) - 1]
        userPayMap = {}
        rows = len(payList) / tdLength
        for row in range(rows):
            itemList = payList[tdLength * row : tdLength * (row + 1)]
            if not re.match(r'\d', itemList[0]):
                continue
            if itemList[1] != u'资金转入':
                continue
            if float(itemList[2]) < seller_payment:
                continue
            taobaoId = itemList[6]
            # 2011-3-9 3:57:00
            ymdhms = re.match(r'(\d+)-(\d+)-(\d+) (\d+):(\d+):(\d+)', itemList[4]).groups()
            seller_paytime = datetime(int(ymdhms[0]), int(ymdhms[1]), int(ymdhms[2]), int(ymdhms[3]), int(ymdhms[4]), int(ymdhms[5]))
            userPayMap[taobaoId] = seller_paytime
        autoAction = VerifyAction(u'ghosert', u'011849', u'011849', userPayMap)
        view = WebView(self.tabWidget, autoAction)
        view.load(QUrl("http://i.taobao.com/"))

class MainContainer(QMainWindow):
    def __init__(self, parent = None):
        QMainWindow.__init__(self, parent)
        
        self.tabWidget = QTabWidget(self)
        self.tabWidget.setStyleSheet('QTabBar::tab {width: 100px;}')
        self.tabWidget.setTabsClosable(False)
        self.connect(self.tabWidget, SIGNAL('tabCloseRequested(int)'), self.closeTab)
        self.setCentralWidget(self.tabWidget)
        
        mainPanel = MainPanel(self.tabWidget)
        self.tabWidget.addTab(mainPanel, 'Main Panel')
        
        self.resize(942, 563)
    
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
    
    # Uncomment thhis line for test purpose only.
    # database.db_location = './storage/database_test'
    
    # create table if there is no table.
    if not os.path.exists(database.db_location):
        database.createTable()
        
    main.setWindowTitle(u'淘宝随意拍')
    main.show()
    app.exec_()
    
