#!/usr/bin/python
# encoding: utf-8

import sys
from PyQt4.QtGui import *
from PyQt4.QtCore import *
from PyQt4.QtWebKit import *

class MainContainer(QMainWindow):
    def __init__(self, parent = None):
        QMainWindow.__init__(self, parent)
        
        # Enable plugins here will make activex turn on, it's important to taobao security activex control
        # jiawzhang TODO: may be disable QWebSettings.AutoLoadImages for speeding up page loading later ?
        QWebSettings.globalSettings().setAttribute(QWebSettings.PluginsEnabled, True)
        
        self.view = QWebView(self)
        self.setCentralWidget(self.view)
        
        self.page = self.view.page()
        self.frame = self.page.currentFrame()
        
        # view.load(QUrl("http://www.google.com"))
        # view.load(QUrl("http://item.taobao.com/item.htm?id=9190349629"))
        self.view.load(QUrl("http://www.taobao.com"))
        print 'jiawei'
        
        # view.connect(view, SIGNAL('loadFinished(bool)'), load_finished)
        # view.connect(view, SIGNAL('loadProgress(int)'), load_progress)
    
    def createWindow(self, webWindowType):
        print 'new window is creating'
        return QWebView()
        
    def load_finished(self, status):
        # element = self.frame.findFirstElement("input.lst")
        self.frame.evaluateJavaScript("q = document.getElementsByName('q')[0]; q.value = 'jiawei'; b = document.getElementsByName('btnG')[0]; b.click();")
        # element.setAttribute("value", "jiawzhang")
        # print element.attribute("class")
    
    def load_progress(self, progress):
        print progress

if __name__ == '__main__':
    app = QApplication(sys.argv)
    main = MainContainer()
    main.show()
    app.exec_()
    