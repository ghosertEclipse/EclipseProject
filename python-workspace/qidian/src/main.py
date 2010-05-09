# -*- coding: utf-8 -*-

import sys

from PyQt4.QtGui import *
from PyQt4.QtCore import *
from res.ui_main import Ui_MainDialog

import qidian
import kthreading

# start app here.
app = QApplication(sys.argv)

from common import *
from user import *
from book import *
from vote import *

class MainDialog(QDialog, Ui_MainDialog):
    def __init__(self, parent = None):
        QDialog.__init__(self, parent)
        Ui_MainDialog.setupUi(self, self, UserTableWidget, BookListWidget, VoteTreeWidget)

        # set main dialog icon
        QDialog.setWindowIcon(self, appIcon)

        self.userTableWidget.initialize()

        self.bookListWidget.initialize()

        self.voteTreeWidget.initialize()

        # add slots to voteTreeWidget, because of the self-defined signal here, bookListWidget and voteTreeWidget don't have to know each other.
        self.connect(self.bookListWidget, SIGNAL('userDropped'), self.voteTreeWidget.addVote)

        # add slots to update userTableWidget
        # the signal including userInfoMap: {userName:(recVoteNum, advVoteNum)}
        self.connect(self.voteTreeWidget, SIGNAL('bookVoteDeleted'), self.userTableWidget.updateVoteNumInUserTableForReturn)
        self.connect(self.voteTreeWidget, SIGNAL('userVoteDeleted'), self.userTableWidget.updateVoteNumInUserTableForReturn)
        self.connect(self.voteTreeWidget, SIGNAL('voteCompleted'), self.userTableWidget.updateVoteNumInUserTable)

        # click on 'btRefreshUser' button to connect qidian to refresh the userTableWidget
        self.connect(self.btRefreshUser, SIGNAL('clicked()'), self.refreshUserTable)

        # click on 'btStartVote' button to connect qidian to start voting.
        self.connect(self.btStartVote, SIGNAL('clicked()'), self.startVote)

    def startVote(self):
        userBookMap = self.voteTreeWidget.getUserBookMap()

        qidianInstance = qidian.Qidian()
        def qidianAction():
            qidianInstance.voteTickets(userBookMap)
            qidianInstance.emit(SIGNAL('voteTicketsFinished'))

        voteDialog = VoteDialog(self)
        nonUIThread = kthreading.KThread(target = qidianAction)
        nonUIThread.setDaemon(True)
        self.connect(qidianInstance, SIGNAL('qidianStatus'), voteDialog.lbVoteStatus.setText)
        self.connect(qidianInstance, SIGNAL('voteTicketsFinished'), voteDialog.accept)
        self.connect(qidianInstance, SIGNAL('qidianVoteOk'), self.voteTreeWidget.refreshVoteTreeAfterSingleUserVoting)

        nonUIThread.start()
        if voteDialog.exec_():
            nonUIThread.kill()
            return

    def refreshUserTable(self):
        userPwdList = []
        for row in range(self.userTableWidget.rowCount()):
            item = self.userTableWidget.item(row, 0)
            # Convert QString to Unicode for 'qidian' module and then add it to list.
            userPwdList.append((unicode(item.text()), unicode(item.data(Qt.UserRole).toString())))
                    
        qidianInstance = qidian.Qidian()
        def qidianAction():
            #ticketList = qidianInstance.getMockTicketList(userPwdList)
            ticketList = qidianInstance.getTicketList(userPwdList)
            qidianInstance.emit(SIGNAL('getTicketListFinished'), voteDialog, ticketList)

        voteDialog = VoteDialog(self)
        nonUIThread= kthreading.KThread(target = qidianAction)
        nonUIThread.setDaemon(True)
        self.connect(qidianInstance, SIGNAL('qidianStatus'), voteDialog.lbVoteStatus.setText)
        self.connect(qidianInstance, SIGNAL('getTicketListFinished'), self.__refreshUserTableUI)
        nonUIThread.start()
        if voteDialog.exec_():
            nonUIThread.kill()
            return

    def __refreshUserTableUI(self, voteDialog, ticketList):
        # Close the voteDialog
        voteDialog.accept()
        
        self.userTableWidget.refreshUserTableAfterConnectingQidian(ticketList)

        # Refresh the vote number of vote tree widget and user table widget.
        for index in range(self.voteTreeWidget.topLevelItemCount()):
            bookItem = self.voteTreeWidget.topLevelItem(index)
            for index2 in range(bookItem.childCount()):
                userName = bookItem.child(index2).text(0)
                
                userInfoForBookItem = self.userTableWidget.updateUserTableByUserName(userName)
                if  not userInfoForBookItem:
                    continue
                
                self.voteTreeWidget.updateUserItemOnBookItem(bookItem, index2, userInfoForBookItem)
                
main = MainDialog()
main.show()
app.exec_()
