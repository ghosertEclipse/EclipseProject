#!/usr/bin/python
# encoding: utf-8

import sys
from PyQt4.QtGui import *
from PyQt4.QtCore import *
from PyQt4.QtWebKit import *

def load_finished(status):
    element = frame.findFirstElement("input.lst")
    frame.evaluateJavaScript("q = document.getElementsByName('q')[0]; q.value = 'jiawei'; b = document.getElementsByName('btnG')[0]; b.click();")
    # element.setAttribute("value", "jiawzhang")
    # print element.attribute("class")

def load_progress(progress):
    print progress

app = QApplication(sys.argv)

# Enable plugins here will make activex turn on, it's important to taobao security activex control
# jiawzhang TODO: may be disable QWebSettings.AutoLoadImages for speeding up page loading later ?
QWebSettings.globalSettings().setAttribute(QWebSettings.PluginsEnabled, True)

view = QWebView()
page = view.page()
frame = page.currentFrame()

# view.load(QUrl("http://www.google.com"))
# view.load(QUrl("http://item.taobao.com/item.htm?id=9190349629"))
view.load(QUrl("http://www.taobao.com"))

# view.connect(view, SIGNAL('loadFinished(bool)'), load_finished)
# view.connect(view, SIGNAL('loadProgress(int)'), load_progress)


view.show()

app.exec_()

