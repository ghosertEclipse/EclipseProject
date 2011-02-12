#!/usr/bin/python
# encoding: utf-8

import sys
from PyQt4.QtGui import *
from PyQt4.QtCore import *
from PyQt4.QtWebKit import *

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
        self.setCentralWidget(self.tabWidget)
        
        view = self.createWebView()
        view.load(QUrl("http://www.taobao.com"))
        
    def createWebView(self):
        view = WebView(self)
        self.tabWidget.setCurrentIndex(self.tabWidget.addTab(view, 'loading'))
        self.tabWidget.connect(view, SIGNAL('loadFinished(bool)'), self.load_finished)
        self.tabWidget.connect(view, SIGNAL('loadProgress(int)'), self.load_progress)
        return view
    
    def load_finished(self, status):
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
    # jiawzhang TODO: may be disable QWebSettings.AutoLoadImages for speeding up page loading later ?
    QWebSettings.globalSettings().setAttribute(QWebSettings.PluginsEnabled, True)
        
    main.show()
    app.exec_()
    