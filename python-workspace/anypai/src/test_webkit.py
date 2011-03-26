#!/usr/bin/python
# encoding: utf-8

import os
import sys

from PyQt4.QtGui import *
from PyQt4.QtCore import *
from PyQt4.QtWebKit import *

import re
from datetime import datetime

import database
from webview import WebView
from actions.pai_action import PaiAction
from actions.verify_action import VerifyAction
from properties import StoragePath

# jiawzhang TODO:
# 1. Rate with word-comments.

class MainPanel(QWidget):
    def __init__(self, tabWidget = None):
        QWidget.__init__(self, tabWidget)
        self.tabWidget = tabWidget
        
        hLayout = QHBoxLayout()
        self.btBeginPai = QPushButton(u'开始拍货')
        self.btPayVerify = QPushButton(u'开始验证')
        hLayout.addWidget(self.btBeginPai)
        hLayout.addWidget(self.btPayVerify)
        
        self.textEdit = QTextEdit()
        
        vLayout = QVBoxLayout()
        vLayout.addLayout(hLayout)
        vLayout.addWidget(self.textEdit)
        
        self.setLayout(vLayout)
        
        self.connect(self.btBeginPai, SIGNAL('clicked()'), self.beginPai)
        self.connect(self.btPayVerify, SIGNAL('clicked()'), self.payVerify)
        
        self.autoAction = None
    
    def beginPai(self):
        # jiawzhang TODO: If running tons of view.load, I saw "Segmentation fault" and then PyQt exits, not sure Windows has the same issue, google it 'Segmentation fault Qt webkit'!
        if self.btBeginPai.text() == u'开始拍货':
            # Make sure fill in unicode characters.
            self.autoAction = PaiAction(u'捷易通充值平台加款卡10元自动转帐', u'ghosert', u'011849', u'011849', 9.90, 10.00, u'捷易通ID: ghosert')
            self.connect(self.autoAction, SIGNAL('terminateAll'), self.btBeginPai.click)
            view = WebView(self.tabWidget, self.autoAction)
            view.load(QUrl("http://www.taobao.com/"))
            # view.load(QUrl("http://item.taobao.com/item.htm?id=9248227645"))
            self.btBeginPai.setText(u'停止拍货')
            self.btPayVerify.setDisabled(True)
        else:
            self.autoAction.termintateAll(self.tabWidget)
            self.btBeginPai.setText(u'开始拍货')
            self.btPayVerify.setDisabled(False)
        
    def payVerify(self):
        # 捷易通查单网址：
        # http://dx.jieyitong.net/system/index.asp
        if self.btPayVerify.text() == u'开始验证':
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
                taobaoId = itemList[6].split(' ')[0]
                # 2011-3-9 3:57:00
                ymdhms = re.match(r'(\d+)-(\d+)-(\d+) (\d+):(\d+):(\d+)', itemList[4]).groups()
                seller_paytime = datetime(int(ymdhms[0]), int(ymdhms[1]), int(ymdhms[2]), int(ymdhms[3]), int(ymdhms[4]), int(ymdhms[5]))
                userPayMap[taobaoId] = seller_paytime
            self.autoAction = VerifyAction(u'ghosert', u'011849', u'011849', userPayMap)
            view = WebView(self.tabWidget, self.autoAction)
            view.load(QUrl("http://i.taobao.com/"))
            self.btPayVerify.setText(u'停止验证')
            self.btBeginPai.setDisabled(True)
        else:
            self.autoAction.termintateAll(self.tabWidget)
            self.btPayVerify.setText(u'开始验证')
            self.btBeginPai.setDisabled(False)

class MainContainer(QMainWindow):
    def __init__(self, parent = None):
        QMainWindow.__init__(self, parent)
        
        self.tabWidget = QTabWidget(self)
        self.tabWidget.setStyleSheet('QTabBar::tab {width: 100px;}')
        self.tabWidget.setTabsClosable(False)
        self.setCentralWidget(self.tabWidget)
        
        mainPanel = MainPanel(self.tabWidget)
        self.tabWidget.addTab(mainPanel, 'Main Panel')
        
        self.resize(942, 563)
    
    def closeEvent(self, event):
        self.tabWidget.clear()

if __name__ == '__main__':
    app = QApplication(sys.argv)
    main = MainContainer()
    
    # Enable plugins here will make activex turn on, it's important to taobao security activex control
    QWebSettings.globalSettings().setAttribute(QWebSettings.PluginsEnabled, True)
    QWebSettings.globalSettings().setAttribute(QWebSettings.AutoLoadImages, False)
    QWebSettings.globalSettings().enablePersistentStorage(StoragePath)
    
    # Uncomment thhis line for test purpose only.
    # database.db_location = './storage/database_test'
    
    # create table if there is no table.
    if not os.path.exists(database.db_location):
        database.createTable()
        
    main.setWindowTitle(u'淘宝随意拍')
    main.show()
    app.exec_()
    
