# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'E:\VimProject\pyqt\qidian\src\res\bookdlg.ui'
#
# Created: Tue Feb 17 20:55:06 2009
#      by: PyQt4 UI code generator 4.4.4
#
# WARNING! All changes made in this file will be lost!

from PyQt4 import QtCore, QtGui

class Ui_BookDialog(object):
    def setupUi(self, BookDialog):
        BookDialog.setObjectName("BookDialog")
        BookDialog.resize(297, 147)
        font = QtGui.QFont()
        font.setFamily("Arial")
        font.setPointSize(9)
        BookDialog.setFont(font)
        self.verticalLayout = QtGui.QVBoxLayout(BookDialog)
        self.verticalLayout.setContentsMargins(-1, 18, -1, -1)
        self.verticalLayout.setObjectName("verticalLayout")
        self.gridLayout = QtGui.QGridLayout()
        self.gridLayout.setObjectName("gridLayout")
        self.label = QtGui.QLabel(BookDialog)
        self.label.setObjectName("label")
        self.gridLayout.addWidget(self.label, 0, 0, 1, 1)
        self.bookNameLineEdit = QtGui.QLineEdit(BookDialog)
        self.bookNameLineEdit.setObjectName("bookNameLineEdit")
        self.gridLayout.addWidget(self.bookNameLineEdit, 0, 1, 1, 1)
        self.label_2 = QtGui.QLabel(BookDialog)
        self.label_2.setObjectName("label_2")
        self.gridLayout.addWidget(self.label_2, 1, 0, 1, 1)
        self.bookIdLineEdit = QtGui.QLineEdit(BookDialog)
        self.bookIdLineEdit.setEchoMode(QtGui.QLineEdit.Normal)
        self.bookIdLineEdit.setObjectName("bookIdLineEdit")
        self.gridLayout.addWidget(self.bookIdLineEdit, 1, 1, 1, 1)
        self.validationLabel = QtGui.QLabel(BookDialog)
        self.validationLabel.setAlignment(QtCore.Qt.AlignCenter)
        self.validationLabel.setObjectName("validationLabel")
        self.gridLayout.addWidget(self.validationLabel, 2, 0, 1, 2)
        self.line = QtGui.QFrame(BookDialog)
        self.line.setFrameShape(QtGui.QFrame.HLine)
        self.line.setFrameShadow(QtGui.QFrame.Sunken)
        self.line.setObjectName("line")
        self.gridLayout.addWidget(self.line, 3, 0, 1, 2)
        self.buttonBox = QtGui.QDialogButtonBox(BookDialog)
        self.buttonBox.setOrientation(QtCore.Qt.Horizontal)
        self.buttonBox.setStandardButtons(QtGui.QDialogButtonBox.Cancel|QtGui.QDialogButtonBox.Ok)
        self.buttonBox.setObjectName("buttonBox")
        self.gridLayout.addWidget(self.buttonBox, 4, 0, 1, 2)
        self.verticalLayout.addLayout(self.gridLayout)

        self.retranslateUi(BookDialog)
        QtCore.QObject.connect(self.buttonBox, QtCore.SIGNAL("accepted()"), BookDialog.accept)
        QtCore.QObject.connect(self.buttonBox, QtCore.SIGNAL("rejected()"), BookDialog.reject)
        QtCore.QMetaObject.connectSlotsByName(BookDialog)

    def retranslateUi(self, BookDialog):
        BookDialog.setWindowTitle(QtGui.QApplication.translate("BookDialog", "图书对话框", None, QtGui.QApplication.UnicodeUTF8))
        self.label.setText(QtGui.QApplication.translate("BookDialog", "图书名：", None, QtGui.QApplication.UnicodeUTF8))
        self.label_2.setText(QtGui.QApplication.translate("BookDialog", "图书ID：", None, QtGui.QApplication.UnicodeUTF8))

