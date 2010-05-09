# coding: utf-8

import pickle

from PyQt4.QtGui import *
from PyQt4.QtCore import *

from res.ui_votedlg import Ui_VoteDialog
from common import *
import rule

class VoteDialog(QDialog, Ui_VoteDialog):
    def __init__(self, parent = None):
        QDialog.__init__(self, parent)
        Ui_VoteDialog.setupUi(self, self)
        self.connect(self.btCancel, SIGNAL("clicked()"), self.accept)

class VoteTreeWidget(QTreeWidget):
    """ Reimplement context menu event here.
    """
    def __init__(self, parent=None):
        QTreeWidget.__init__(self, parent)

    def initialize(self):
        "This method should be invoked by main.py first."

        self.setColumnWidth(0, 120)
        self.setColumnWidth(1, 65)
        self.setColumnWidth(2, 90)
        self.setColumnWidth(3, 70)
        self.setColumnWidth(4, 65)

        # add actions to voteTreeWidget
        # contextMenuEvent below only work for Qt::DefaultContextMenu So the line below should be commented.
        # self.setContextMenuPolicy(Qt.ActionsContextMenu)
        self.deleteBookVoteAction = createAction(self, u'删除图书投票关系', self.deleteBookVote, removeIcon)
        self.deleteUserVoteAction = createAction(self, u'删除用户投票关系', self.deleteUserVote, removeIcon)

        # set appdata, appdata comes from common.py
        self.appdata = appdata

        # update vote tree
        self.__updateVoteTree(self.appdata.getVoteData())

    def contextMenuEvent(self, event):

        if self.currentItem() == None:
            return

        menu = QMenu('ContextMenu')
        # parent book item
        if self.currentItem().childCount() > 0:
            menu.addAction(self.deleteBookVoteAction)
        # child user item
        else:
            menu.addAction(self.deleteBookVoteAction)
            menu.addAction(self.deleteUserVoteAction)
        menu.exec_(event.globalPos())

    def deleteBookVote(self):
        # parent book item
        if self.currentItem().childCount() > 0:
            item = self.currentItem()
        # child user item
        else:
            item = self.currentItem().parent()

        bookName = item.text(0)
        bookId = item.data(0, Qt.UserRole).toString()
        userInfoMap = {}
        for index in range(item.childCount()):
            userItem = item.child(index)
            userName = userItem.text(0)
            recVoteNum = userItem.text(3)
            advVoteNum = userItem.text(4)
            userInfoMap[userName] = (recVoteNum, advVoteNum)

        # Remove the bookItem with its userItems.
        self.removeItemWidget(item, 0)

        # Remove the relation in data module.
        voteData = self.appdata.getVoteData()
        voteData.removeBook(bookName, bookId)
        with open('data', 'w') as file:
            pickle.dump(self.appdata, file)

        # the signal including userInfoMap: {userName:(recVoteNum, advVoteNum)}
        self.emit(SIGNAL('bookVoteDeleted'), userInfoMap)

    def deleteUserVote(self):
        bookItem = self.currentItem().parent()
        bookName = bookItem.text(0)
        bookId = bookItem.data(0, Qt.UserRole).toString()
        userInfoMap = {}
        userItem = self.currentItem()
        userName = userItem.text(0)
        recVoteNum = userItem.text(3)
        advVoteNum = userItem.text(4)
        userInfoMap[userName] = (recVoteNum, advVoteNum)

        # Remove the userItem/bookItem in the bookItem.
        bookItem.removeChild(userItem)
        if bookItem.childCount() == 0:
            self.removeItemWidget(bookItem, 0)

        # Remove the relation in data module.
        voteData = self.appdata.getVoteData()
        voteData.removeUser(bookName, bookId, userInfoMap.keys()[0])
        with open('data', 'w') as file:
            pickle.dump(self.appdata, file)

        # the signal including userInfoMap: {userName:(recVoteNum, advVoteNum)}
        self.emit(SIGNAL('userVoteDeleted'), userInfoMap)

    def __updateVoteTree(self, voteData):
        self.clear()
        voteDataMap = voteData.getData()
        for index, book in enumerate(sorted(voteDataMap.iterkeys())):
            # get book and users info.
            bookName, bookId = book
            users = voteDataMap[book]

            # create bookItem
            bookItem = self.__addBookItemToWidget(index, bookName, bookId)

            # create user child tree item.
            for index2, user in enumerate(sorted(users)):
                userName, passwd = user
                self.__addUserItemToBookItem(bookItem, index2, userName, passwd, '', '', '', '')
        self.expandAll()

    def __addBookItemToWidget(self, index, bookName, bookId):
        # create book tree item.
        stringlist = QStringList()
        stringlist.append(bookName)
        bookItem = QTreeWidgetItem(stringlist)
        bookItem.setIcon(0, bookIcon)
        # set bookId to the item.
        bookItem.setData(0, Qt.UserRole, QVariant(bookId))
        self.insertTopLevelItem(index, bookItem)
        return bookItem

    def __addUserItemToBookItem(self, bookItem, index, userName, passwd, userType, userBigSmall, realRecVoteNum, realAdvVoteNum):
        stringlist = QStringList()
        stringlist.append(userName)
        stringlist.append(userType)
        stringlist.append(userBigSmall)
        stringlist.append(realRecVoteNum)
        stringlist.append(realAdvVoteNum)
        userItem = QTreeWidgetItem(stringlist)
        userItem.setIcon(0, userIcon)
        # set passwd to the item.
        userItem.setData(0, Qt.UserRole, QVariant(passwd))
        bookItem.insertChild(index, userItem)

    def addVote(self, bookName, bookId, userName, passwd, userType, userBigSmall, recVoteNum, advVoteNum):
        "This slot will be invoked once the 'userDropped' signal is emitted from bookListWidget."

        matchlist = self.findItems(bookName, Qt.MatchExactly, 0)
        exist_userType = None
        exist_userName = None
        if len(matchlist) > 0:
            bookItem = self.findItems(bookName, Qt.MatchExactly, 0)[0]
            exist_userType = bookItem.child(0).text(1)
            exist_userName = bookItem.child(0).text(0)
        result = rule.validationForAddVote(bookName, bookId, userName, userType, exist_userName, exist_userType, recVoteNum, advVoteNum)
        if result:
            QMessageBox.information(self, u'提示:', result, QMessageBox.Ok)
            return

        realRecVoteNum, realAdvVoteNum, remainRecVoteNum, remainAdvVoteNum = rule.getRealRemainVoteNum(userBigSmall, recVoteNum, advVoteNum)

        # emit the remain vote num signal to update userTableWidget
        self.emit(SIGNAL('voteCompleted'), userName, remainRecVoteNum, remainAdvVoteNum)
        
        voteData = self.appdata.getVoteData()
        voteData.addData(bookName, bookId, userName, passwd)
        with open('data', 'w') as file:
            pickle.dump(self.appdata, file)

        self.__addNewRowOnVoteTree(bookName, bookId, userName, passwd, userType, userBigSmall, realRecVoteNum, realAdvVoteNum)

    def __addNewRowOnVoteTree(self, bookName, bookId, userName, passwd, userType, userBigSmall, realRecVoteNum, realAdvVoteNum):
        index = -1
        for index in range(self.topLevelItemCount()):
            bookItem = self.topLevelItem(index)
            # Found a matched bookItem, add user to it.
            if bookName == bookItem.text(0):
                for index2 in range(bookItem.childCount()):
                    if userName < bookItem.child(index2).text(0):
                        break
                else:
                    index2 = index2 + 1
                # add user to book item.
                self.__addUserItemToBookItem(bookItem, index2, userName, passwd, userType, userBigSmall, realRecVoteNum, realAdvVoteNum)
                return
            if bookName < bookItem.text(0):
                break
        else:
            # The current bookName is the maximum one. set index++ for ready to add the new maximun bookItem to the widget.
            index = index + 1

        # There is no exisiting bookItem which has the same bookName.
        # We do need to create a new bookItem and add user now.
        bookItem = self.__addBookItemToWidget(index, bookName, bookId)
        self.__addUserItemToBookItem(bookItem, 0, userName, passwd, userType, userBigSmall, realRecVoteNum, realAdvVoteNum)
        self.expandAll()
        return
    
    def getUserBookMap(self):
        """Create the user-book voting relations. {(userName, pwd):[(bookName, bookId, recVoteNum, advVoteNum)]}
           This method will be called before starting vote.
        """
        userBookMap = {}
        for index in range(self.topLevelItemCount()):
            bookItem = self.topLevelItem(index)
            bookName = bookItem.text(0)
            bookId = bookItem.data(0, Qt.UserRole).toString()
            for index2 in range(bookItem.childCount()):
                userItem = bookItem.child(index2)
                userName = userItem.text(0)
                pwd = userItem.data(0, Qt.UserRole).toString()
                recVoteNum = userItem.text(3)
                advVoteNum = userItem.text(4)

                # No valid vote number due to refresh user table failed or user has already been deleted in user table widget.
                if recVoteNum == '' :
                    continue

                # Due to both '0' here, no need to vote, so skip it.
                if recVoteNum == '0' and advVoteNum == '0':
                    continue

                if userBookMap.has_key((unicode(userName), unicode(pwd))):
                    userBookMap[(unicode(userName), unicode(pwd))].append((unicode(bookName), unicode(bookId), unicode(recVoteNum), unicode(advVoteNum)))
                else:
                    userBookMap[(unicode(userName), unicode(pwd))] = [(unicode(bookName), unicode(bookId), unicode(recVoteNum), unicode(advVoteNum))]
        return userBookMap
    
    
    def refreshVoteTreeAfterSingleUserVoting(self, userName, bookName, recVoteNum, advVoteNum):
        """Refresh the vote number of vote tree widget.
           This method will be called after one user finish voting.
        """
        bookItem = self.findItems(bookName, Qt.MatchExactly, 0)[0]
        for index2 in range(bookItem.childCount()):
            if userName == bookItem.child(index2).text(0):
                currentRecVoteNum = bookItem.child(index2).text(3)
                currentAdvVoteNum = bookItem.child(index2).text(4)
                realRecVoteNum = int(currentRecVoteNum) - int(recVoteNum)
                realAdvVoteNum = int(currentAdvVoteNum) - int(advVoteNum)
                if realRecVoteNum >= 0:
                    bookItem.child(index2).setText(3, str(realRecVoteNum))
                if realAdvVoteNum >= 0:
                    bookItem.child(index2).setText(4, str(realAdvVoteNum))
                    
    def updateUserItemOnBookItem(self, bookItem, index, userInfoForBookItem):
        userType, userBigSmall, realRecVoteNum, realAdvVoteNum = userInfoForBookItem
        bookItem.child(index).setText(1, userType)
        bookItem.child(index).setText(2, userBigSmall)
        bookItem.child(index).setText(3, realRecVoteNum)
        bookItem.child(index).setText(4, realAdvVoteNum)
