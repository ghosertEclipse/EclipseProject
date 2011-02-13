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
    def __init__(self, mainContainer = None):
        QWebView.__init__(self, mainContainer.tabWidget)
        self.mainContainer = mainContainer
        
    def createWindow(self, webWindowType):
        return self.mainContainer.createWebView()

class MainContainer(QMainWindow):
    def __init__(self, parent = None):
        QMainWindow.__init__(self, parent)
        
        self.tabWidget = QTabWidget(self)
        self.tabWidget.setTabsClosable(True)
        self.connect(self.tabWidget, SIGNAL('tabCloseRequested(int)'), self.tabWidget.removeTab)
        self.setCentralWidget(self.tabWidget)
        
        self.cookieJar = CookieJar()
        self.cookieJar.loadAllCookies()
        view = self.createWebView()
        view.load(QUrl("http://www.taobao.com"))
        
    def createWebView(self):
        view = WebView(self)
        networkAccessManager = view.page().networkAccessManager()
        networkAccessManager.setCookieJar(self.cookieJar)
        
        self.tabWidget.setCurrentIndex(self.tabWidget.addTab(view, 'loading'))
        self.connect(view, SIGNAL('loadFinished(bool)'), self.load_finished)
        self.connect(view, SIGNAL('loadProgress(int)'), self.load_progress)
        return view
    
    def load_finished(self, status):
        self.cookieJar.saveAllCookies()
        self.tabWidget.setTabText(self.tabWidget.currentIndex(), self.sender().title())
        # element = self.frame.findFirstElement("input.lst")
        # self.frame.evaluateJavaScript("q = document.getElementsByName('q')[0]; q.value = 'jiawei'; b = document.getElementsByName('btnG')[0]; b.click();")
        # element.setAttribute("value", "jiawzhang")
        # print element.attribute("class")
    
    def load_progress(self, progress):
        self.tabWidget.setTabText(self.tabWidget.currentIndex(), str(progress) + "%")
        

if __name__ == '__main__':
    app = QApplication(sys.argv)
    main = MainContainer()
    
    # Enable plugins here will make activex turn on, it's important to taobao security activex control
    QWebSettings.globalSettings().setAttribute(QWebSettings.PluginsEnabled, True)
    # QWebSettings.globalSettings().setAttribute(QWebSettings.AutoLoadImages, False)
    QWebSettings.globalSettings().enablePersistentStorage("/home/jiawzhang/Templates")
        
    main.show()
    app.exec_()
    