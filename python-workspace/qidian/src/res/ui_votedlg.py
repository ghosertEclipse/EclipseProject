# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'E:\VimProject\pyqt\qidian\src\res\votedlg.ui'
#
# Created: Tue Feb 17 20:55:07 2009
#      by: PyQt4 UI code generator 4.4.4
#
# WARNING! All changes made in this file will be lost!

from PyQt4 import QtCore, QtGui

class Ui_VoteDialog(object):
    def setupUi(self, VoteDialog):
        VoteDialog.setObjectName("VoteDialog")
        VoteDialog.resize(396, 253)
        self.verticalLayout = QtGui.QVBoxLayout(VoteDialog)
        self.verticalLayout.setObjectName("verticalLayout")
        self.splitter = QtGui.QSplitter(VoteDialog)
        self.splitter.setOrientation(QtCore.Qt.Vertical)
        self.splitter.setObjectName("splitter")
        self.label_4 = QtGui.QLabel(self.splitter)
        self.label_4.setObjectName("label_4")
        self.label = QtGui.QLabel(self.splitter)
        self.label.setWordWrap(True)
        self.label.setObjectName("label")
        self.line = QtGui.QFrame(self.splitter)
        self.line.setFrameShape(QtGui.QFrame.HLine)
        self.line.setFrameShadow(QtGui.QFrame.Sunken)
        self.line.setObjectName("line")
        self.label_2 = QtGui.QLabel(self.splitter)
        self.label_2.setObjectName("label_2")
        self.lbVoteStatus = QtGui.QLabel(self.splitter)
        self.lbVoteStatus.setWordWrap(True)
        self.lbVoteStatus.setObjectName("lbVoteStatus")
        self.line_2 = QtGui.QFrame(self.splitter)
        self.line_2.setFrameShape(QtGui.QFrame.HLine)
        self.line_2.setFrameShadow(QtGui.QFrame.Sunken)
        self.line_2.setObjectName("line_2")
        self.btCancel = QtGui.QPushButton(self.splitter)
        self.btCancel.setObjectName("btCancel")
        self.verticalLayout.addWidget(self.splitter)

        self.retranslateUi(VoteDialog)
        QtCore.QMetaObject.connectSlotsByName(VoteDialog)

    def retranslateUi(self, VoteDialog):
        VoteDialog.setWindowTitle(QtGui.QApplication.translate("VoteDialog", "投票对话框", None, QtGui.QApplication.UnicodeUTF8))
        self.label_4.setText(QtGui.QApplication.translate("VoteDialog", "投票说明：", None, QtGui.QApplication.UnicodeUTF8))
        self.label.setText(QtGui.QApplication.translate("VoteDialog", "投票系统正访问起点进行自动投票。如果您发现投票状态长时间没有响应（超过5分钟），请点击“取消自动投票”按钮，并重新投票。", None, QtGui.QApplication.UnicodeUTF8))
        self.label_2.setText(QtGui.QApplication.translate("VoteDialog", "投票状态：", None, QtGui.QApplication.UnicodeUTF8))
        self.lbVoteStatus.setText(QtGui.QApplication.translate("VoteDialog", "正在登录起点网站进行投票。。。", None, QtGui.QApplication.UnicodeUTF8))
        self.btCancel.setText(QtGui.QApplication.translate("VoteDialog", "取消自动投票", None, QtGui.QApplication.UnicodeUTF8))

