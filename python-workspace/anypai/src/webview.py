#!/usr/bin/python
# encoding: utf-8

import os
import threading

from PyQt4.QtGui import *
from PyQt4.QtCore import *
from PyQt4.QtWebKit import *
from PyQt4.QtNetwork import *

from properties import StoragePath

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
                

class WebView(QWebView):
    
    cookieJar = CookieJar()
    
    def __init__(self, tabWidget, autoAction):
        QWebView.__init__(self, tabWidget)
        networkAccessManager = self.page().networkAccessManager()
        networkAccessManager.setCookieJar(self.__class__.cookieJar)
        tabWidget.addTab(self, 'loading')
        # index = tabWidget.addTab(self, 'loading')
        # tabWidget.setCurrentIndex(index)
        # jiawzhang TODO: see whether chagne Qt.connectiontype will resolve the ramdon crash issue.
        tabWidget.connect(self, SIGNAL('loadStarted()'), self.load_started, Qt.QueuedConnection)
        tabWidget.connect(self, SIGNAL('loadFinished(bool)'), self.load_finished, Qt.QueuedConnection)
        tabWidget.connect(self, SIGNAL('loadProgress(int)'), self.load_progress, Qt.QueuedConnection)
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
        # jiawzhang TODO: See whether this will resolve the item page is blocked issue.
        # self.__class__.cookieJar.saveAllCookies()
        self.tabWidget.setTabText(self.tabWidget.indexOf(self.sender()), self.sender().title())
        self.autoAction.perform(self.frame, self.url().toString(), self.userInfo)
    
    def load_progress(self, progress):
        self.tabWidget.setTabText(self.tabWidget.indexOf(self.sender()), str(progress) + "%")
        
