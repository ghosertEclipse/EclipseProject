# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'E:\VimProject\pyqt\qidian\src\res\userdlg.ui'
#
# Created: Tue Feb 17 20:55:07 2009
#      by: PyQt4 UI code generator 4.4.4
#
# WARNING! All changes made in this file will be lost!

from PyQt4 import QtCore, QtGui

class Ui_UserDialog(object):
    def setupUi(self, UserDialog):
        UserDialog.setObjectName("UserDialog")
        UserDialog.resize(297, 147)
        font = QtGui.QFont()
        font.setFamily("Arial")
        font.setPointSize(9)
        UserDialog.setFont(font)
        self.verticalLayout = QtGui.QVBoxLayout(UserDialog)
        self.verticalLayout.setContentsMargins(-1, 18, -1, -1)
        self.verticalLayout.setObjectName("verticalLayout")
        self.gridLayout = QtGui.QGridLayout()
        self.gridLayout.setObjectName("gridLayout")
        self.label = QtGui.QLabel(UserDialog)
        self.label.setObjectName("label")
        self.gridLayout.addWidget(self.label, 0, 0, 1, 1)
        self.userLineEdit = QtGui.QLineEdit(UserDialog)
        self.userLineEdit.setObjectName("userLineEdit")
        self.gridLayout.addWidget(self.userLineEdit, 0, 1, 1, 1)
        self.label_2 = QtGui.QLabel(UserDialog)
        self.label_2.setObjectName("label_2")
        self.gridLayout.addWidget(self.label_2, 1, 0, 1, 1)
        self.passwordLineEdit = QtGui.QLineEdit(UserDialog)
        self.passwordLineEdit.setObjectName("passwordLineEdit")
        self.gridLayout.addWidget(self.passwordLineEdit, 1, 1, 1, 1)
        self.validationLabel = QtGui.QLabel(UserDialog)
        self.validationLabel.setAlignment(QtCore.Qt.AlignCenter)
        self.validationLabel.setObjectName("validationLabel")
        self.gridLayout.addWidget(self.validationLabel, 2, 0, 1, 2)
        self.line = QtGui.QFrame(UserDialog)
        self.line.setFrameShape(QtGui.QFrame.HLine)
        self.line.setFrameShadow(QtGui.QFrame.Sunken)
        self.line.setObjectName("line")
        self.gridLayout.addWidget(self.line, 3, 0, 1, 2)
        self.buttonBox = QtGui.QDialogButtonBox(UserDialog)
        self.buttonBox.setOrientation(QtCore.Qt.Horizontal)
        self.buttonBox.setStandardButtons(QtGui.QDialogButtonBox.Cancel|QtGui.QDialogButtonBox.Ok)
        self.buttonBox.setObjectName("buttonBox")
        self.gridLayout.addWidget(self.buttonBox, 4, 0, 1, 2)
        self.verticalLayout.addLayout(self.gridLayout)

        self.retranslateUi(UserDialog)
        QtCore.QObject.connect(self.buttonBox, QtCore.SIGNAL("accepted()"), UserDialog.accept)
        QtCore.QObject.connect(self.buttonBox, QtCore.SIGNAL("rejected()"), UserDialog.reject)
        QtCore.QMetaObject.connectSlotsByName(UserDialog)

    def retranslateUi(self, UserDialog):
        UserDialog.setWindowTitle(QtGui.QApplication.translate("UserDialog", "用户对话框", None, QtGui.QApplication.UnicodeUTF8))
        self.label.setText(QtGui.QApplication.translate("UserDialog", "用户名：", None, QtGui.QApplication.UnicodeUTF8))
        self.label_2.setText(QtGui.QApplication.translate("UserDialog", "密码：", None, QtGui.QApplication.UnicodeUTF8))

