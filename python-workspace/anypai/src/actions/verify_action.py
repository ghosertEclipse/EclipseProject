# encoding: utf-8

import re
import threading
import time
from datetime import datetime

from PyQt4.QtCore import *

import thread_util
from actions import AutoAction
from webview import WebView
from database import UserInfo

import platform
if platform.system() == 'Windows':
    import ahkpython

class VerifyAction(AutoAction):
    def __init__(self, username, password, alipayPassword, userPayMap):
        AutoAction.__init__(self)
        self.username = username
        self.password = password
        self.alipayPassword = alipayPassword
        self.userPayMap = userPayMap
        self.view = None
        self.items = []
        
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
            print 'no item to be confirmed, finish verify flow'
            return
        self.view = WebView(frame.page().view().tabWidget, self)
        self.clickOn(confirmPayUrl)
    
    def listBoughtItems(self, frame):
        confirmUrlMap = {}
        self.items.extend(frame.findAllElements('table#J_BoughtTable tbody').toList())
        nextPage = frame.findFirstElement('li.next-page a')
        if not nextPage.isNull():
            self.clickOn(nextPage)
            return
        for item in self.items:
            # taobao deal time: 2011-03-08 21:39
            dealTime = unicode(item.findFirst('span.deal-time').toPlainText())
            ymdhm = re.match(r'.*?(\d+)-(\d+)-(\d+) (\d+):(\d+).*?', dealTime).groups()
            dealTime = datetime(int(ymdhm[0]), int(ymdhm[1]), int(ymdhm[2]), int(ymdhm[3]), int(ymdhm[4]))
            taobaoId = unicode(item.findFirst('span.seller a').attribute('title', ''))
            seller_paytime = self.userPayMap.get(taobaoId)
            if seller_paytime != None and seller_paytime >= dealTime:
                confirmUrl = unicode(item.findFirst('td.operate a').attribute('href', ''))
                confirmUrlMap[taobaoId] = confirmUrl
            elif (datetime.now() - dealTime).days >= 1:
                desc = unicode(item.findFirst('td.after-service a').toPlainText())
                if desc == u'申请退款':
                    confirmUrl = unicode(item.findFirst('td.after-service a').attribute('href', ''))
                    confirmUrlMap[taobaoId] = confirmUrl
            
        if not confirmUrlMap:
            print 'All the items are confirmed.'
            return
        
        class AsynHandler(thread_util.KThread):
            def __init__(self, autoAction, confirmUrlMap, frame):
                thread_util.KThread.__init__(self)
                self.autoAction = autoAction
                self.confirmUrlMap = confirmUrlMap
                self.frame = frame
            def run(self):
                view = self.autoAction.view
                for taobaoId, confirmUrl in confirmUrlMap.iteritems():
                    print 'Begin Verify Request ...'
                    print confirmUrl
                    view.condition.acquire()
                    try:
                        print u'taobaoId:' + taobaoId
                        if re.match(r'.*?refund.*?', confirmUrl):
                            print 'refund flow begin ...'
                            self.autoAction.emit(SIGNAL('new_webview'), view, confirmUrl)
                            view.condition.wait()
                        else:
                            userInfo = AutoAction.userInfoManager.getActiveUserByTaobaoId(taobaoId)
                            if userInfo:
                                print userInfo.status
                                if userInfo.status != UserInfo.Status_Confirmed_Payment:
                                    view.userInfo = userInfo
                                    self.autoAction.emit(SIGNAL('new_webview'), view, confirmUrl)
                                    while view.userInfo != None:
                                        view.condition.wait(2)
                                else:
                                    title = u'提示:'
                                    content = u'本月内已有针对卖家 ' + userInfo.taobaoId + u' 的打款记录，不再放款。系统有bug，请人为干预，申请退款或手动确认该交易'
                                    self.autoAction.emit(SIGNAL('messageFromSubThread'), title, content)
                    finally:
                        view.condition.release()
                    print 'End Verify Request ...'
                print 'All the items are confirmed.'
            
        self.asyncHandler = AsynHandler(self, confirmUrlMap, frame)
        self.asyncHandler.setDaemon(True)
        self.asyncHandler.start()
        return
    
    def confirmGoods(self, frame):
        ahkpython.sendAlipayPassword(self.alipayPassword)
        frame = frame.childFrames()[0]
        confirmButton = frame.findFirstElement('div.submit-line input.btn')
        # Below maybe the right way to confirm a js popup window.
        # 1. Start the confirm operation thread first, then press the confirm button to trigger the js popup.
        class PreJsConfirm(threading.Thread):
            def run(self):
                time.sleep(4)
                ahkpython.confirmJavaScript()
        self.asynClickOn(2, confirmButton)
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
    
    def refund(self, frame):
        self.clickOn(frame.findFirstElement('input#good-unreceived'))
        self.selectDropdownList(frame.findFirstElement('select#J_RefundReason2'), 1)
        self.setValueOn(frame.findFirstElement('textarea#J_RefundInfo'), u'卖家没货')
        ahkpython.sendAlipayPassword(self.alipayPassword)
        
        frame = frame.childFrames()[0]
        confirmButton = frame.findFirstElement('div.submit-line input')
        class PreJsConfirm(threading.Thread):
            def run(self):
                time.sleep(4)
                ahkpython.confirmJavaScript()
        self.asynClickOn(2, confirmButton)
        pjc = PreJsConfirm()
        pjc.setDaemon(True)
        pjc.start()
    
    def refundSuccess(self, frame):
        self.terminateCurrentFlow(frame)
        
    def perform(self, frame, url, userInfo):
        if self.isTerminated:
            return
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
        elif (re.search(r'^http://trade\.taobao\.com/refund.*?\d+$', url)):
            self.refund(frame)
        elif (re.search(r'^http://trade\.taobao\.com/refund.*?\d+#$', url)):
            self.refundSuccess(frame)
        else:
            print 'find unexpected url: ' + url
            # self.process_incomplete(frame, userInfo)
