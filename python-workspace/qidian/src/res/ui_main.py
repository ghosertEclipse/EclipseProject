# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'E:\VimProject\pyqt\qidian\src\res\main.ui'
#
# Created: Tue Feb 17 20:55:06 2009
#      by: PyQt4 UI code generator 4.4.4
#
# WARNING! All changes made in this file will be lost!

from PyQt4 import QtCore, QtGui

class Ui_MainDialog(object):
    def setupUi(self, MainDialog, UserTableWidget, BookListWidget, VoteTreeWidget):
        MainDialog.setObjectName("MainDialog")
        MainDialog.resize(942, 563)
        font = QtGui.QFont()
        font.setFamily("Arial")
        font.setPointSize(9)
        MainDialog.setFont(font)
        self.verticalLayout = QtGui.QVBoxLayout(MainDialog)
        self.verticalLayout.setObjectName("verticalLayout")
        self.gridLayout = QtGui.QGridLayout()
        self.gridLayout.setObjectName("gridLayout")
        self.groupBox = QtGui.QGroupBox(MainDialog)
        self.groupBox.setObjectName("groupBox")
        self.userTableWidget = UserTableWidget(self.groupBox)
        self.userTableWidget.setGeometry(QtCore.QRect(10, 20, 441, 201))
        font = QtGui.QFont()
        font.setFamily("Arial")
        font.setPointSize(9)
        self.userTableWidget.setFont(font)
        self.userTableWidget.setAutoFillBackground(False)
        self.userTableWidget.setMidLineWidth(0)
        self.userTableWidget.setHorizontalScrollBarPolicy(QtCore.Qt.ScrollBarAsNeeded)
        self.userTableWidget.setEditTriggers(QtGui.QAbstractItemView.NoEditTriggers)
        self.userTableWidget.setDragEnabled(True)
        self.userTableWidget.setDragDropMode(QtGui.QAbstractItemView.DragOnly)
        self.userTableWidget.setSelectionMode(QtGui.QAbstractItemView.SingleSelection)
        self.userTableWidget.setSelectionBehavior(QtGui.QAbstractItemView.SelectRows)
        self.userTableWidget.setRowCount(0)
        self.userTableWidget.setObjectName("userTableWidget")
        self.userTableWidget.setColumnCount(5)
        self.userTableWidget.setRowCount(0)
        item = QtGui.QTableWidgetItem()
        self.userTableWidget.setHorizontalHeaderItem(0, item)
        item = QtGui.QTableWidgetItem()
        self.userTableWidget.setHorizontalHeaderItem(1, item)
        item = QtGui.QTableWidgetItem()
        self.userTableWidget.setHorizontalHeaderItem(2, item)
        item = QtGui.QTableWidgetItem()
        self.userTableWidget.setHorizontalHeaderItem(3, item)
        item = QtGui.QTableWidgetItem()
        self.userTableWidget.setHorizontalHeaderItem(4, item)
        self.btRefreshUser = QtGui.QPushButton(self.groupBox)
        self.btRefreshUser.setGeometry(QtCore.QRect(140, 230, 141, 23))
        self.btRefreshUser.setObjectName("btRefreshUser")
        self.gridLayout.addWidget(self.groupBox, 0, 0, 1, 1)
        self.groupBox_2 = QtGui.QGroupBox(MainDialog)
        self.groupBox_2.setObjectName("groupBox_2")
        self.bookListWidget = BookListWidget(self.groupBox_2)
        self.bookListWidget.setGeometry(QtCore.QRect(10, 20, 441, 192))
        font = QtGui.QFont()
        font.setFamily("Arial")
        font.setPointSize(9)
        self.bookListWidget.setFont(font)
        self.bookListWidget.setAcceptDrops(True)
        self.bookListWidget.setViewMode(QtGui.QListView.IconMode)
        self.bookListWidget.setObjectName("bookListWidget")
        self.label = QtGui.QLabel(self.groupBox_2)
        self.label.setGeometry(QtCore.QRect(0, 220, 441, 21))
        self.label.setAlignment(QtCore.Qt.AlignCenter)
        self.label.setObjectName("label")
        self.gridLayout.addWidget(self.groupBox_2, 1, 0, 1, 1)
        self.groupBox_3 = QtGui.QGroupBox(MainDialog)
        self.groupBox_3.setObjectName("groupBox_3")
        self.voteTreeWidget = VoteTreeWidget(self.groupBox_3)
        self.voteTreeWidget.setGeometry(QtCore.QRect(10, 20, 431, 471))
        font = QtGui.QFont()
        font.setFamily("Arial")
        font.setPointSize(9)
        self.voteTreeWidget.setFont(font)
        self.voteTreeWidget.setAllColumnsShowFocus(False)
        self.voteTreeWidget.setHeaderHidden(False)
        self.voteTreeWidget.setObjectName("voteTreeWidget")
        self.btStartVote = QtGui.QPushButton(self.groupBox_3)
        self.btStartVote.setGeometry(QtCore.QRect(150, 500, 151, 23))
        self.btStartVote.setObjectName("btStartVote")
        self.gridLayout.addWidget(self.groupBox_3, 0, 1, 2, 1)
        self.verticalLayout.addLayout(self.gridLayout)

        self.retranslateUi(MainDialog)
        QtCore.QMetaObject.connectSlotsByName(MainDialog)
        MainDialog.setTabOrder(self.userTableWidget, self.btRefreshUser)
        MainDialog.setTabOrder(self.btRefreshUser, self.bookListWidget)
        MainDialog.setTabOrder(self.bookListWidget, self.voteTreeWidget)
        MainDialog.setTabOrder(self.voteTreeWidget, self.btStartVote)

    def retranslateUi(self, MainDialog):
        MainDialog.setWindowTitle(QtGui.QApplication.translate("MainDialog", "疯狂起点投票机", None, QtGui.QApplication.UnicodeUTF8))
        self.groupBox.setTitle(QtGui.QApplication.translate("MainDialog", "起点用户列表(右键点击下表添加用户)", None, QtGui.QApplication.UnicodeUTF8))
        self.userTableWidget.horizontalHeaderItem(0).setText(QtGui.QApplication.translate("MainDialog", "用户名", None, QtGui.QApplication.UnicodeUTF8))
        self.userTableWidget.horizontalHeaderItem(1).setText(QtGui.QApplication.translate("MainDialog", "用户类型", None, QtGui.QApplication.UnicodeUTF8))
        self.userTableWidget.horizontalHeaderItem(2).setText(QtGui.QApplication.translate("MainDialog", "大号/小号", None, QtGui.QApplication.UnicodeUTF8))
        self.userTableWidget.horizontalHeaderItem(3).setText(QtGui.QApplication.translate("MainDialog", "日推荐票", None, QtGui.QApplication.UnicodeUTF8))
        self.userTableWidget.horizontalHeaderItem(4).setText(QtGui.QApplication.translate("MainDialog", "日广告票", None, QtGui.QApplication.UnicodeUTF8))
        self.btRefreshUser.setText(QtGui.QApplication.translate("MainDialog", "刷新用户列表", None, QtGui.QApplication.UnicodeUTF8))
        self.groupBox_2.setTitle(QtGui.QApplication.translate("MainDialog", "起点图书列表(右键点击下表添加图书)", None, QtGui.QApplication.UnicodeUTF8))
        self.label.setText(QtGui.QApplication.translate("MainDialog", "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">\n"
"<html><head><meta name=\"qrichtext\" content=\"1\" /><style type=\"text/css\">\n"
"p, li { white-space: pre-wrap; }\n"
"</style></head><body style=\" font-family:\'Arial\'; font-size:9pt; font-weight:400; font-style:normal;\">\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" color:#0147d5;\">拖曳用户列表中的用户至图书列表中的某本图书进行投票，同一本书可重复投票</span></p></body></html>", None, QtGui.QApplication.UnicodeUTF8))
        self.groupBox_3.setTitle(QtGui.QApplication.translate("MainDialog", "投票列表(右键点击下表删除投票)", None, QtGui.QApplication.UnicodeUTF8))
        self.voteTreeWidget.headerItem().setText(0, QtGui.QApplication.translate("MainDialog", "书名/用户名", None, QtGui.QApplication.UnicodeUTF8))
        self.voteTreeWidget.headerItem().setText(1, QtGui.QApplication.translate("MainDialog", "用户类型", None, QtGui.QApplication.UnicodeUTF8))
        self.voteTreeWidget.headerItem().setText(2, QtGui.QApplication.translate("MainDialog", "大号/小号", None, QtGui.QApplication.UnicodeUTF8))
        self.voteTreeWidget.headerItem().setText(3, QtGui.QApplication.translate("MainDialog", "推荐投票数", None, QtGui.QApplication.UnicodeUTF8))
        self.voteTreeWidget.headerItem().setText(4, QtGui.QApplication.translate("MainDialog", "广告投票数", None, QtGui.QApplication.UnicodeUTF8))
        self.btStartVote.setText(QtGui.QApplication.translate("MainDialog", "开始投票", None, QtGui.QApplication.UnicodeUTF8))

