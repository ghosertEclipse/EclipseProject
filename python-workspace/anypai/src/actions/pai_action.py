# encoding: utf-8

import re
import time
from Queue import Queue
from datetime import datetime

from PyQt4.QtCore import *
from PyQt4.QtGui import QMessageBox
from PyQt4.QtWebKit import *

import thread_util
from database import UserInfo
from actions import AutoAction
from webview import WebView
from properties import MaxPaiThreadNum
from properties import WaitingSecondsOnItemPage

import platform
if platform.system() == 'Windows':
    import ahkpython

class PaiAction(AutoAction):
    
    def __init__(self, keyword, username, password, alipayPassword, max_acceptable_price, seller_payment, message_to_seller):
        AutoAction.__init__(self)
        self.keyword = keyword
        self.username = username
        self.password = password
        self.alipayPassword = alipayPassword
        self.max_acceptable_price = max_acceptable_price
        self.seller_payment = seller_payment
        self.message_to_seller = message_to_seller
        self.queue = Queue(MaxPaiThreadNum)
        self.channel = thread_util.Channel()
        self.alipayChannel = thread_util.Channel()
        # allow only one alipay page at a time.
        self.alipayChannel.startConsumer(1)
        
    def home(self, frame):
        # When visiting the home, begin to create view and put them to the queue, sub-threads will leverage them later.
        for i in range(self.queue.maxsize):
            view = WebView(frame.page().view().tabWidget, self)
            self.queue.put(view)
        searchBox = frame.findFirstElement('input#q')
        self.setValueOn(searchBox, self.keyword)
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
        if not self.isCheckedOn(wwonline):
            self.clickOn(wwonline)
            confirmButton = frame.findFirstElement('button#J_SubmitBtn')
            self.clickOn(confirmButton)
            return
        elif re.match(r'.*?sort=price-asc.*', priceSortButton.attribute('href', '')):
            self.clickOn(priceSortButton)
            return
        else:
            isMaxAcceptablePriceAccessed = False
            # We can't have frame.findXXXX functions in sub-thread, so I promote the code snip on parsing frame here.
            items = frame.findFirstElement('ul.list-view').findAll('li.list-item').toList()
            for index, item in enumerate(items):
                itemLink = unicode(item.findFirst('h3.summary a').attribute('href', ''))
                buyer_payment = float(item.findFirst('ul.attribute li.price em').toPlainText())
                if buyer_payment > self.max_acceptable_price:
                    isMaxAcceptablePriceAccessed = True
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
            
            class AsynHandler(thread_util.KThread):
                def __init__(self, autoAction, items, isMaxAcceptablePriceAccessed, nextPage):
                    thread_util.KThread.__init__(self)
                    self.autoAction = autoAction
                    self.items = items
                    self.nextPage = nextPage
                    self.isMaxAcceptablePriceAccessed = isMaxAcceptablePriceAccessed
                def run(self):
                    # handle legacy userInfos here.
                    self.autoAction.handleUserInfoList()
                    
                    for item in self.items:
                        AutoAction.userInfoManager.addUserInfo(item, self.autoAction.username, self.autoAction.max_acceptable_price)
                    
                    # handle new userInfos from search page here.
                    self.autoAction.handleUserInfoList()
                    
                    # click on next page on search page for capturing new taobao items.
                    if not self.nextPage.isNull() and self.isMaxAcceptablePriceAccessed == False:
                        print 'call next page here.'
                        self.autoAction.emit(SIGNAL('asynClickOn'), self.nextPage)
                    else:
                        self.autoAction.emit(SIGNAL('messageFromSubThread'), u'拍货完成', u'所有符合条件的宝贝都已经拍下，请稍候再拍或换帐号继续拍。')
                        self.autoAction.emit(SIGNAL('terminateAll'))
            
            self.asyncHandler = AsynHandler(self, items, isMaxAcceptablePriceAccessed, nextPage)
            self.asyncHandler.setDaemon(True)
            self.asyncHandler.start()
            return

    def handleUserInfoList(self):
        # jiawzhang XXX: for channel.putRequest in a loop, make sure you always pass in variable like userInfo by constructor,
        # Otherwise, you may get duplicated userInfo in your Request class, not sure whether this is a pyqt bug.
        class MyRequest(thread_util.Request):
            def __init__(self, autoAction, userInfo):
                self.userInfo = userInfo
                self.autoAction = autoAction
            def doAction(self):
                if self.userInfo.status == UserInfo.Status_Confirmed_Buy:
                    AutoAction.userInfoManager.setUserInfoStatus(self.userInfo, UserInfo.Status_Failed_Buy)
                    return
                    
                if (datetime.now() - self.userInfo.last_status_time).seconds > 3600:
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
                    
                link = self.userInfo.itemLink
                
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
                    print 'item completed: ' + self.userInfo.taobaoId + ' ' + str(self.userInfo.buyer_payment) + ' ' + str(self.userInfo.last_status_time)
                        
        userInfoList = AutoAction.userInfoManager.getUnhandledUserInfoList()
        if userInfoList:
            for userInfo in userInfoList:
                self.channel.putRequest(MyRequest(self, userInfo))
            self.channel.startConsumer(self.queue.maxsize, True, False)
            self.channel.waitingForConsumerExist()
            
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
            self.asynClickOn(WaitingSecondsOnItemPage, buynow)
    
    def buy(self, frame, userInfo):
        shippingFirstOption = frame.findFirstElement('table#trade-info tbody tr#J_Post input#shipping1')
        if not shippingFirstOption.isNull():
            self.clickOn(shippingFirstOption)
        message_box = frame.findFirstElement('textarea#J_msgtosaler')
        self.setValueOn(message_box, self.message_to_seller)
        
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
            self.alipayChannel.putRequest(AlipayRequest(self, confirmButton, checkCodeInput, frame))
            
        else:
            # Set status to retry if the price of item is changed.
            AutoAction.userInfoManager.setUserInfoStatus(userInfo, UserInfo.Status_RETRY)
            self.terminateCurrentFlow(frame)
        
    def alipay(self, frame, userInfo):
        # Set status to confirmed buy.
        # jiawzhang XXX: The string to be saved to sqlite must be unicode first, otherwise, error happens.
        AutoAction.userInfoManager.setUserInfoStatus(userInfo, UserInfo.Status_Confirmed_Buy)
        
        confirmButton = frame.findFirstElement('input.J_ForAliControl')
        if not confirmButton.isNull():
            ahkpython.sendAlipayPassword(self.alipayPassword)
            # jiawzhang XXX: Should Have a Async Click On Confirm Button for Security ActiveX Inputbox
            # Other wise, you will always get button clicked first, then fill the password into the Security ActiveX Inputbox, which lead to issues.
            self.asynClickOn(2, confirmButton)
        else:
            # Set status to fail to buy if there is no comfirmed button for alipay page.
            AutoAction.userInfoManager.setUserInfoStatus(userInfo, UserInfo.Status_Failed_Buy)
            QMessageBox.information(None, u'支付失败', u'您的支付宝余额可能不足，即将停止拍货，请充值后重新拍货。', QMessageBox.Ok)
            self.emit(SIGNAL('terminateAll'))
    
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
    
    def perform(self, frame, url, userInfo):
        if self.isTerminated:
            return
        if (re.search(r'^http://www\.taobao\.com', url)):
            self.home(frame)
        elif (re.search(r'^http://s\.taobao\.com/search\?q=', url)):
            self.search(frame)
        elif (re.search(r'^https://login\.taobao\.com/member/login\.jhtml', url)):
            self.login(frame)
        elif (re.search(r'^http://item\.taobao\.com/item\.htm\?id=', url)):
            self.item(frame, userInfo)
            frame.page().settings().setAttribute(QWebSettings.AutoLoadImages, True)
        elif (re.search(r'^http://buy\.taobao\.com/auction/buy_now\.jhtml', url)):
            frame.page().settings().setAttribute(QWebSettings.AutoLoadImages, False)
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
        
    def termintateAll(self, tabWidget):
        self.channel.stopConsumer()
        self.alipayChannel.stopConsumer()
        AutoAction.termintateAll(self, tabWidget)
