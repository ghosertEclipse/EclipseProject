# ecoding: utf-8

import time
import threading
from datetime import datetime

from PyQt4.QtCore import *
from PyQt4.QtGui import QMessageBox
from PyQt4.QtWebKit import QWebPage

import database
from database import UserInfo
import thread_util

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
            if (userInfo.status == UserInfo.Status_Succeed_Buy or userInfo.status == UserInfo.Status_Confirmed_Payment):
                if (datetime.now() - userInfo.last_status_time).days < 31:
                    return
            # NotTo buy and the last status time more than 6 days will allow to continue.
            elif userInfo.status == UserInfo.Status_NotTo_Buy:
                if (datetime.now() - userInfo.last_status_time).days < 6:
                    return
            # Failed buy and the last status time more than 1 days will allow to continue.
            elif userInfo.status == UserInfo.Status_Failed_Buy:
                if (datetime.now() - userInfo.last_status_time).days < 1:
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
    
    def setUserInfoStatus(self, userInfo, status):
        database.updateUser(userInfo, status = status, last_status_time = datetime.now())

class AutoAction(QObject):
    
    userInfoManager = UserInfoManager()
    
    def __init__(self):
        QObject.__init__(self)
        # jiawzhang XXX: if your signal is sent from sub-thread, set Qt.BlockingQueuedConnection will make sure:
        # after emit signal, the sub-thread will be blocked until the corresponding slot is completed.
        # If the signal is sent from the same thread, the default behavior is already similar like above.
        self.connect(self, SIGNAL('asynClickOn'), self.clickOn, Qt.BlockingQueuedConnection)
        self.connect(self, SIGNAL('asynLocateCheckCodeInput'), self.locateCheckCodeInput, Qt.BlockingQueuedConnection)
        self.connect(self, SIGNAL('new_webview'), self.new_webview, Qt.BlockingQueuedConnection)
        self.connect(self, SIGNAL('stop_webview'), self.stop_webview, Qt.BlockingQueuedConnection)
        self.connect(self, SIGNAL('reload'), self.reload, Qt.BlockingQueuedConnection)
        self.connect(self, SIGNAL('messageFromSubThread'), self.messageFromSubThread, Qt.BlockingQueuedConnection)
        self.asyncHandler = None
        self.isTerminated = False
        
    def messageFromSubThread(self, title, content):
        QMessageBox.information(None, title, content, QMessageBox.Ok)
    
    def reload(self, frame):
        frame.page().action(QWebPage.Reload).trigger()
    
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
    
    def setValueOn(self, element, value):
        "Support most 'input' tag  with 'textarea' tag either, since the value could be Chinese, make sure it passed in as unicode based."
        # We can also use element.setAttribute('value', value) instead, but this clause fail to work in some cases, it's safer to use javascript based set-value function.
        # When invoking string1.format(string2), make sure both string1 & string2 are unicode based like below.
        if element.tagName() == 'TEXTAREA':
            # textarea element should be focus first, then set innerHTML.
            return element.evaluateJavaScript(u'this.focus();this.innerHTML="{0}";'.format(value))
        else:
            return element.evaluateJavaScript(u'this.value="{0}";'.format(value))

    def isCheckedOn(self, element):
        "Check whether 'input' tag with 'checkbox' type is checked or not."
        checked = element.evaluateJavaScript('this.checked;').toString()
        if checked == 'true':
            return True
        else:
            return False
    
    def subtmitForm(self, element):
        "Submit the form directly."
        return element.evaluateJavaScript('this.submit();')
    
    def keyupOn(self, element):
        "This is major for input box."
        keyupOnString = "var evObj = document.createEvent('UIEvents');evObj.initEvent( 'keyup', true, true );this.dispatchEvent(evObj);"
        return element.evaluateJavaScript(keyupOnString)
    
    def selectDropdownList(self, element, index):
        return element.evaluateJavaScript("this.selectedIndex = {0}".format(index))
    
    def new_webview(self, view, link):
        view.load(QUrl(link))
        
    def stop_webview(self, view):
        view.stop()
    
    def asynClickOn(self, seconds, clickableElement):
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
        
    def login(self, frame):
        # Uncheck the safeLoginCheckbox to simplify the process, otherwise, I have to care activex seurity inputbox on the page.
        safeLoginCheckbox = frame.findFirstElement('input#J_SafeLoginCheck')
        if (self.isCheckedOn(safeLoginCheckbox)):
            # if safeLoginChecked, uncheck it first.
            self.clickOn(safeLoginCheckbox)
                
        username = frame.findFirstElement('input#TPL_username_1')
        password = frame.findFirstElement('span#J_StandardPwd input.login-text')
        self.setValueOn(username, self.username)
        self.setValueOn(password, self.password)
        loginButton = frame.findFirstElement('button.J_Submit')
        self.clickOn(loginButton)
        
    def terminateCurrentFlow(self, frame):
        "Invoking on this method will make sure the worker thread pick up next request or to be stopped if no requests."
        view = frame.page().view()
        self.__terminateCurrentFlow(view)
            
    def __terminateCurrentFlow(self, view):
        view.condition.acquire()
        try:
            view.userInfo = None
            view.condition.notifyAll()
        finally:
            view.condition.release()
        
    def termintateAll(self, tabWidget):
        self.isTerminated = True
        if self.asyncHandler:
            self.asyncHandler.kill()
        while tabWidget.count() >= 2:
            view = tabWidget.widget(1)
            view.stop()
            self.__terminateCurrentFlow(view)
            view.close()
            tabWidget.removeTab(1)
            