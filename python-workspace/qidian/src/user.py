# coding: utf-8

import pickle

from PyQt4.QtGui import *
from PyQt4.QtCore import *

from res.ui_userdlg import Ui_UserDialog
from common import *
import rule

class UserDialog(QDialog, Ui_UserDialog):
    def __init__(self, parent = None):
        QDialog.__init__(self, parent)
        Ui_UserDialog.setupUi(self, self)
        self.btOk = self.buttonBox.button(QDialogButtonBox.Ok)
        self.btOk.setText(u'确定')
        btCancel = self.buttonBox.button(QDialogButtonBox.Cancel)
        btCancel.setText(u'取消')
        self.connect(self.userLineEdit, SIGNAL("textChanged(QString)"), self.validation)
        self.connect(self.passwordLineEdit, SIGNAL("textChanged(QString)"), self.validation)
        self.__existingUsers = []
        self.validation()

    def getUserPasswd(self):
        return (self.userLineEdit.text(), self.passwordLineEdit.text())

    def setUserPasswd(self, user, passwd):
        self.userLineEdit.setText(user)
        self.passwordLineEdit.setText(passwd)

    def validation(self):
        if self.userLineEdit.text() in self.__existingUsers:
            self.validationLabel.setText(u'用户已存在')
            self.btOk.setDisabled(True)
            return

        if not (len(self.userLineEdit.text()) and len(self.passwordLineEdit.text())):
            self.validationLabel.setText(u'用户名密码不可为空')
            self.btOk.setDisabled(True)
            return

        self.validationLabel.setText('')
        self.btOk.setDisabled(False)

    def setValidationList(self, existingUsers):
        self.__existingUsers = existingUsers

class UserTableWidget(QTableWidget):
    """ Reimplement a drag-drop customized QTableWidget.
    """
    def __init__(self, parent=None):
        QTableWidget.__init__(self, parent)

    def initialize(self):
        "This method should be invoked by main.py first."
        self.setColumnWidth(0, 100)
        self.setColumnWidth(1, 80)
        self.setColumnWidth(2, 90)
        self.setColumnWidth(3, 65)
        self.setColumnWidth(4, 65)

        # add actions to userTableWidget
        self.sortItems(0, Qt.AscendingOrder)
        self.setContextMenuPolicy(Qt.ActionsContextMenu)
        addUserAction = createAction(self, u'新增用户', self.addUser, addIcon)
        editUserAction = createAction(self, u'修改用户', self.editUser, editIcon)
        deleteUserAction = createAction(self, u'删除用户', self.deleteUser, removeIcon)
        self.addAction(addUserAction)
        self.addAction(editUserAction)
        self.addAction(deleteUserAction)

        # self.setAcceptDrops(True)
        self.setDragEnabled(True)

        # set appdata, appdata comes from common.py
        self.appdata = appdata

        # update user table
        self.__updateUserTable(self.appdata.getUserDatas())

    def startDrag(self, dropActions):
        currentRow = self.currentRow()

        # load user infos to be passed.
        userName = self.item(currentRow, 0).text()
        passwd = self.item(currentRow, 0).data(Qt.UserRole).toString()
        icon = self.item(currentRow, 0).icon()
        userType = self.item(currentRow, 1).text()
        userBigSmall = self.item(currentRow, 2).text()
        recVoteNum = self.item(currentRow, 3).text()
        advVoteNum = self.item(currentRow, 4).text()

        data = QByteArray()
        stream = QDataStream(data, QIODevice.WriteOnly)
        stream << userName << passwd << userType << userBigSmall << recVoteNum << advVoteNum
        mimeData = QMimeData()
        mimeData.setData("application/x-userInfo", data)
        drag = QDrag(self)
        drag.setMimeData(mimeData)
        pixmap = icon.pixmap(24, 24)
        drag.setHotSpot(QPoint(12, 12))
        drag.setPixmap(pixmap)
        # drag.start()
        if drag.start(Qt.CopyAction) == Qt.CopyAction:
            # dropEvent has a chance to set operation to Qt.MoveAction or Qt.CopyAction,
            # so that here we have chance to do different thing according to move or copy,
            # for example if it is move action here, we maybe need a takeItem() call here.
            pass

    def addUser(self):
        userDialog = UserDialog(self)
        userDialog.setValidationList([userData2.getUserPasswd()[0] for userData2 in self.appdata.getUserDatas()])
        if userDialog.exec_():
            userName, passwd = userDialog.getUserPasswd()

            # update and save ApplicationData
            self.appdata.getUserDatas().append(UserData(userName, passwd))
            with open('data', 'w') as file:
                pickle.dump(self.appdata, file)

            rowNum = len(self.appdata.getUserDatas()) - 1
            self.__addNewRowOnUserTable(rowNum, userName, passwd)

    def editUser(self):
        currentRow = self.currentRow()
        if currentRow == -1:
            return
        user = self.item(currentRow, 0).text()
        for userData in self.appdata.getUserDatas():
            if user == userData.getUserPasswd()[0]:
                passwd = userData.getUserPasswd()[1]
                break
        userDialog = UserDialog(self)
        userDialog.setValidationList([userData2.getUserPasswd()[0] for userData2 in self.appdata.getUserDatas() if userData2.getUserPasswd()[0] != user])
        userDialog.setUserPasswd(user, passwd)

        if userDialog.exec_():
            user, passwd = userDialog.getUserPasswd()

            # update and save ApplicationData
            userData.setUserPasswd(user, passwd)
            with open('data', 'w') as file:
                pickle.dump(self.appdata, file)

            self.item(currentRow, 0).setText(user)
            self.item(currentRow, 0).setData(Qt.UserRole, QVariant(passwd))

    def deleteUser(self):
        currentRow = self.currentRow()
        if currentRow == -1:
            return

        user = self.item(currentRow, 0).text()
        for userData in self.appdata.getUserDatas():
            if user == userData.getUserPasswd()[0]:
                break

        # update and save ApplicationData
        self.appdata.getUserDatas().remove(userData)
        with open('data', 'w') as file:
            pickle.dump(self.appdata, file)

        self.removeRow(currentRow)

    def __updateUserTable(self, userDatas):
        self.clearContents()
        for index, userData in enumerate(userDatas):
            userName, passwd = userData.getUserPasswd()
            self.__addNewRowOnUserTable(index, userName, passwd)

    def __addNewRowOnUserTable(self, rowNum, userName, passwd):
        # add a new empty row first
        self.insertRow(rowNum)
        item = QTableWidgetItem(userName)
        item.setIcon(userIcon)
        # set passwd to the first item.
        item.setData(Qt.UserRole, QVariant(passwd))
        self.setItem(rowNum, 0, item)
        self.setItem(rowNum, 1, QTableWidgetItem(''))
        self.setItem(rowNum, 2, QTableWidgetItem(''))
        self.setItem(rowNum, 3, QTableWidgetItem(''))
        self.setItem(rowNum, 4, QTableWidgetItem(''))

    def updateVoteNumInUserTableForReturn(self, userInfoMap):
        "Return the vote numbers to the user table after removal action happens in voteTreeWidget."

        for userName in userInfoMap.iterkeys():
            # find matched userItem
            userItems = self.findItems(userName, Qt.MatchExactly)
            for userItem in userItems:
                if self.column(userItem) == 0:
                    break
            else:
                continue
            row = self.row(userItem)

            returnRecVoteNum, returnAdvVoteNum = userInfoMap[userName]
            if returnRecVoteNum == '':
                continue
            recItem = self.item(row, 3)
            advItem = self.item(row, 4)
            if recItem.text() == '' or recItem.text() == '-1':
                continue
            recItem.setText(str(int(recItem.text()) + int(returnRecVoteNum)))
            advItem.setText(str(int(advItem.text()) + int(returnAdvVoteNum)))

    def updateVoteNumInUserTable(self, userName, remainRecVoteNum, remainAdvVoteNum):
        "This slot will be invoked once the 'voteCompleted' signal is emitted from voteTreeWidget."

        currentRow = self.currentRow()
        self.item(currentRow, 3).setText(remainRecVoteNum)
        self.item(currentRow, 4).setText(remainAdvVoteNum)
    
    def refreshUserTableAfterConnectingQidian(self, ticketList):
        "Refresh the user table widget."
        for row, (userName, userType, userBigSmall, recVoteNum, advVoteNum) in enumerate(ticketList):
            item = self.item(row, 1)
            item.setText(userType)
            item = self.item(row, 2)
            item.setText(userBigSmall)
            item = self.item(row, 3)
            item.setText(recVoteNum)
            item = self.item(row, 4)
            item.setText(advVoteNum)
    
    def updateUserTableByUserName(self, userName):
        # find matched userItem
        userItems = self.findItems(userName, Qt.MatchExactly)
        for userItem in userItems:
            if self.column(userItem) == 0:
                break
        else:
            # (1) match failed, so we skipped to update vote tree widget.
            return None
        row = self.row(userItem)

        # userType, userBigSmall, recVoteNum, advVoteNum
        userType = self.item(row, 1).text()
        userBigSmall = self.item(row, 2).text()
        recVoteNum = self.item(row, 3).text()
        advVoteNum = self.item(row, 4).text()

        # (2) login failed, so we skipped to update vote tree widget.
        if int(recVoteNum) == -1:
            return None
                
        realRecVoteNum, realAdvVoteNum, remainRecVoteNum, remainAdvVoteNum = rule.getRealRemainVoteNum(userBigSmall, recVoteNum, advVoteNum)

        self.item(row, 3).setText(remainRecVoteNum)
        self.item(row, 4).setText(remainAdvVoteNum)
        
        return (userType, userBigSmall, realRecVoteNum, realAdvVoteNum)

