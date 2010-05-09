# coding: utf-8

import re, pickle

from PyQt4.QtGui import *
from PyQt4.QtCore import *

from res.ui_bookdlg import Ui_BookDialog
from common import *

class BookDialog(QDialog, Ui_BookDialog):
    def __init__(self, parent = None):
        QDialog.__init__(self, parent)
        Ui_BookDialog.setupUi(self, self)
        self.btOk = self.buttonBox.button(QDialogButtonBox.Ok)
        self.btOk.setText(u'确定')
        self.btOk.setDisabled(True)
        btCancel = self.buttonBox.button(QDialogButtonBox.Cancel)
        btCancel.setText(u'取消')
        self.connect(self.bookNameLineEdit, SIGNAL("textChanged(QString)"), self.validation)
        self.connect(self.bookIdLineEdit, SIGNAL("textChanged(QString)"), self.validation)
        self.__existingBookNames = []
        self.__existingBookIds = []
        self.validation()

    def getBookNameId(self):
        return (self.bookNameLineEdit.text(), self.bookIdLineEdit.text())

    def setBookNameId(self, bookName, bookId):
        self.bookNameLineEdit.setText(bookName)
        self.bookIdLineEdit.setText(bookId)

    def validation(self):
        if self.bookNameLineEdit.text() in self.__existingBookNames:
            self.validationLabel.setText(u'图书名已存在')
            self.btOk.setDisabled(True)
            return

        if self.bookIdLineEdit.text() in self.__existingBookIds:
            self.validationLabel.setText(u'图书ID已存在')
            self.btOk.setDisabled(True)
            return

        if not(len(self.bookNameLineEdit.text()) and len(self.bookIdLineEdit.text())):
            self.validationLabel.setText(u'图书名, ID不可为空')
            self.btOk.setDisabled(True)
            return

        if not re.search(r'^\d+$', self.bookIdLineEdit.text()):
            self.validationLabel.setText(u'图书ID必须是数字')
            self.btOk.setDisabled(True)
            return

        self.validationLabel.setText('')
        self.btOk.setDisabled(False)

    def setValidationList(self, existingBookNames, existingBookIds):
        self.__existingBookNames = existingBookNames
        self.__existingBookIds = existingBookIds

class BookListWidget(QListWidget):
    """ Reimplement a drag-drop customized QTableWidget.
    """

    def __init__(self, parent=None):
        QListWidget.__init__(self, parent)
        
    def dragEnterEvent(self, event):
        if event.mimeData().hasFormat("application/x-userInfo"):
            currentRow = self.indexAt(event.pos()).row()
            if currentRow != -1:
                event.accept()
            else:
                event.ignore()
        else:
            event.ignore()

    def dragMoveEvent(self, event):
        if event.mimeData().hasFormat("application/x-userInfo"):
            currentRow = self.indexAt(event.pos()).row()
            if currentRow != -1:
                event.accept()
            else:
                event.ignore()
        else:
            event.ignore()

    def dropEvent(self, event):
        if event.mimeData().hasFormat("application/x-userInfo"):
            data = event.mimeData().data("application/x-userInfo")
            stream = QDataStream(data, QIODevice.ReadOnly)
            userName = QString()
            passwd = QString()
            userType = QString()
            userBigSmall = QString()
            recVoteNum = QString()
            advVoteNum = QString()
            # we have user info right here.
            stream >> userName >> passwd >> userType >> userBigSmall >> recVoteNum >> advVoteNum

            currentRow = self.indexAt(event.pos()).row()

            # we have book info right here.
            bookName = self.item(currentRow).text()
            bookId = self.item(currentRow).data(Qt.UserRole).toString()

            event.setDropAction(Qt.CopyAction)
            event.accept()
            self.emit(SIGNAL('userDropped'), bookName, bookId, userName, passwd, userType, userBigSmall, recVoteNum, advVoteNum)
        else:
            event.ignore()

    def initialize(self):
        "This method should be invoked by main.py first."

        # add actions to bookListWidget
        self.setContextMenuPolicy(Qt.ActionsContextMenu)
        addBookAction = createAction(self, u'新增图书', self.addBook, addIcon)
        editBookAction = createAction(self, u'修改图书', self.editBook, editIcon)
        deleteBookAction = createAction(self, u'删除图书', self.deleteBook, removeIcon)
        self.addAction(addBookAction)
        self.addAction(editBookAction)
        self.addAction(deleteBookAction)

        self.setAcceptDrops(True)
        # self.setDragEnabled(True)

        # set appdata, appdata comes from common.py
        self.appdata = appdata

        # update book list
        self.__updateBookList(self.appdata.getBookDatas())

    def __updateBookList(self, bookDatas):
        self.clear()
        for bookData in bookDatas:
            bookName, bookId = bookData.getBookNameId()
            item = QListWidgetItem(bookName)
            item.setIcon(bookIcon)
            # set bookId to the item.
            item.setData(Qt.UserRole, QVariant(bookId))
            self.addItem(item)

    def addBook(self):
        bookDialog = BookDialog(self)
        bookDialog.setValidationList([bookData2.getBookNameId()[0] for bookData2 in self.appdata.getBookDatas()],
                                     [bookData2.getBookNameId()[1] for bookData2 in self.appdata.getBookDatas()])
        if bookDialog.exec_():
            bookName, bookId = bookDialog.getBookNameId()

            # update and save ApplicationData
            bookData = BookData(bookName, bookId)
            self.appdata.getBookDatas().append(bookData)
            with open('data', 'w') as file:
                pickle.dump(self.appdata, file)

            # update book list
            item = QListWidgetItem(bookName)
            item.setIcon(bookIcon)
            # set bookId to the item.
            item.setData(Qt.UserRole, QVariant(bookId))
            self.addItem(item)

    def editBook(self):
        currentItem = self.currentItem()
        if currentItem == None:
            return
        bookName = currentItem.text()
        for bookData in self.appdata.getBookDatas():
            if bookName == bookData.getBookNameId()[0]:
                bookId = bookData.getBookNameId()[1]
                break
        bookDialog = BookDialog(self)
        bookDialog.setValidationList([bookData2.getBookNameId()[0] for bookData2 in self.appdata.getBookDatas() if bookData2.getBookNameId()[0] != bookName],
                                     [bookData2.getBookNameId()[1] for bookData2 in self.appdata.getBookDatas() if bookData2.getBookNameId()[1] != bookId])
        bookDialog.setBookNameId(bookName, bookId)

        if bookDialog.exec_():
            bookName, bookId = bookDialog.getBookNameId()

            # update and save ApplicationData
            bookData.setBookNameId(bookName, bookId)
            with open('data', 'w') as file:
                pickle.dump(self.appdata, file)

            # update book list
            currentItem.setText(bookName)
            # set bookId to the item.
            currentItem.setData(Qt.UserRole, QVariant(bookId))

    def deleteBook(self):
        currentItem = self.currentItem()
        if currentItem == None:
            return
        bookName = currentItem.text()
        for bookData in self.appdata.getBookDatas():
            if bookName == bookData.getBookNameId()[0]:
                break

        # update and save ApplicationData
        self.appdata.getBookDatas().remove(bookData)
        with open('data', 'w') as file:
            pickle.dump(self.appdata, file)

        # update book list
        self.takeItem(self.row(currentItem))

