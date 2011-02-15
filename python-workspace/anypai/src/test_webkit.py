#!/usr/bin/python
# encoding: utf-8

import os
import sys

from PyQt4.QtGui import *
from PyQt4.QtCore import *
from PyQt4.QtWebKit import *
from PyQt4.QtNetwork import *

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

class AutoAction:
    
    def __clickOn(self, element):
        clickOnString = None
        if element.tagName() == 'A': # <a> has a different implementation javascript for simulating clicking on it.
            clickOnString = "var evObj = document.createEvent('MouseEvents');evObj.initEvent( 'click', true, true );this.dispatchEvent(evObj);"
        else:
            clickOnString = 'this.click();'
        return element.evaluateJavaScript(clickOnString)
    
    def __keyupOn(self, element):
        "This is major for input box."
        keyupOnString = "var evObj = document.createEvent('UIEvents');evObj.initEvent( 'keyup', true, true );this.dispatchEvent(evObj);"
        return element.evaluateJavaScript(keyupOnString)
        # element.evaluateJavaScript(keyupOnString)
        # element.evaluateJavaScript('alert(this.value);')
    
    def __init__(self, keyword, username, password):
        self.keyword = keyword
        self.username = username
        self.password = password
    
    def setKeyword(self, keyword):
        self.keyword = keyword
    
    def setUserInfo(self, username, password):
        self.username = username
        self.password = password
    
    def home(self, frame):
        searchBox = frame.findFirstElement('input#q')
        if not searchBox: return False
        searchBox.setAttribute('value', self.keyword)
        searchButton = frame.findFirstElement('button.tsearch-submit')
        if not searchButton: return False
        searchButton.evaluateJavaScript('this.click()')
        return True
    
    def search(self, frame):
        # See whether the current user login or not.
        p_login_info = frame.findFirstElement('p.login-info')
        isLogin = p_login_info.findFirst('a')
        if not isLogin.hasClass('user-nick'):
            self.__clickOn(isLogin)
            return True
                
        # check wang wang online option.
        wwonline = frame.findFirstElement('input#filterServiceWWOnline')
        if wwonline.attribute('checked', 'not_found_value') == 'not_found_value':
            wwonline.evaluateJavaScript('this.click()')
            confirmButton = frame.findFirstElement('button#J_SubmitBtn')
            confirmButton.evaluateJavaScript('this.click()')
            return True
        else:
            return True
    
    def login(self, frame):
        username = frame.findFirstElement('input#TPL_username_1')
        password = frame.findFirstElement('span#J_StandardPwd input.login-text')
        username.setAttribute('value', self.username)
        self.__keyupOn(username) # This step is necessary, since taobao is using 'keyup' js code to login when typing on user name input box.
        password.setAttribute('value', self.password)
        loginButton = frame.findFirstElement('button.J_Submit')
        loginButton.evaluateJavaScript('this.click()')
        return True
    
    def perform(self, frame, url):
        if (re.search(r'^http://www\.taobao\.com', url)):
            return self.home(frame)
        elif (re.search(r'^http://s\.taobao\.com/search\?q=', url)):
            return self.search(frame)
        elif (re.search(r'^https://login\.taobao\.com/member/login\.jhtml', url)):
            return self.login(frame)
        else:
            return False
        
class WebView(QWebView):
    
    cookieJar = CookieJar()
    autoAction = AutoAction(u'捷易通加款1元', 'ghosert', '011849')
    
    def __init__(self, tabWidget = None):
        QWebView.__init__(self, tabWidget)
        networkAccessManager = self.page().networkAccessManager()
        networkAccessManager.setCookieJar(self.__class__.cookieJar)
        tabWidget.setCurrentIndex(tabWidget.addTab(self, 'loading'))
        tabWidget.connect(self, SIGNAL('loadStarted()'), self.load_started)
        tabWidget.connect(self, SIGNAL('loadFinished(bool)'), self.load_finished)
        tabWidget.connect(self, SIGNAL('loadProgress(int)'), self.load_progress)
        self.tabWidget = tabWidget
        self.frame = self.page().currentFrame()
        
    def createWindow(self, webWindowType):
        return WebView(self.tabWidget)
    
    def load_started(self):
        pass
        
    def load_finished(self, status):
        self.__class__.cookieJar.saveAllCookies()
        self.tabWidget.setTabText(self.tabWidget.indexOf(self.sender()), self.sender().title())
        self.__class__.autoAction.perform(self.frame, self.url().toString())
    
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
        

if __name__ == '__main__':
    app = QApplication(sys.argv)
    main = MainContainer()
    
    # Enable plugins here will make activex turn on, it's important to taobao security activex control
    QWebSettings.globalSettings().setAttribute(QWebSettings.PluginsEnabled, True)
    # QWebSettings.globalSettings().setAttribute(QWebSettings.AutoLoadImages, False)
    QWebSettings.globalSettings().enablePersistentStorage("/home/jiawzhang/Templates")
        
    main.show()
    app.exec_()
    