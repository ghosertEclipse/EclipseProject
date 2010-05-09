import os, pickle

from PyQt4.QtGui import QAction, QFont, QIcon
from PyQt4.QtCore import SIGNAL

import res.qrc_resources

def createAction(self, text, slot, icon=None, signal='triggered()'):
    action = QAction(text, self)
    action.setFont(defaultFont)
    if icon:
        action.setIcon(icon)
    self.connect(action, SIGNAL(signal), slot)
    return action

class UserData:

    def __init__(self, user, passwd):
        self.__user = user
        self.__passwd = passwd

    def getUserPasswd(self):
        return (self.__user, self.__passwd)

    def setUserPasswd(self, user, passwd):
        self.__user = user
        self.__passwd = passwd

class BookData:
    def __init__(self, bookName, bookId):
        self.__bookName = bookName
        self.__bookId = bookId

    def getBookNameId(self):
        return (self.__bookName, self.__bookId)

    def setBookNameId(self, bookName, bookId):
        self.__bookName = bookName
        self.__bookId = bookId

class VoteData:
    def __init__(self):
        "self.__map means {(bookName, bookId):[(userName, passwd), ...]}"
        self.__map = {}

    def addData(self, bookName, bookId, userName, passwd):
        if self.__map.has_key((bookName, bookId)):
            self.__map[(bookName, bookId)].append((userName, passwd))
        else:
            self.__map[(bookName, bookId)] = [(userName, passwd)]

    def removeBook(self, bookName, bookId):
        "Return the user list belong to the removed book."
        if self.__map.has_key((bookName, bookId)):
            return self.__map.pop((bookName, bookId))
        else:
            return []

    def removeUser(self, bookName, bookId, userName):
        "Return the user belong to the given book."
        if self.__map.has_key((bookName, bookId)):
            users = self.__map[(bookName, bookId)]
            for user in users:
                if userName in user:
                    break
            else:
                return []
            users.remove(user)
            if not users:
                self.__map.pop((bookName, bookId))
            return user
        else:
            return []

    def getData(self):
        return self.__map

class ApplicationData:
    def __init__(self, userDatas, bookDatas, voteData):
        self.__userDatas = userDatas
        self.__bookDatas = bookDatas
        self.__voteData = voteData

    def getUserDatas(self):
        return self.__userDatas

    def getBookDatas(self):
        return self.__bookDatas

    def getVoteData(self):
        return self.__voteData

# load fonts
defaultFont = QFont()
defaultFont.setFamily('Arial')
defaultFont.setPointSize(9)

# load icons
bookIcon = QIcon(":/book.ico")
userIcon = QIcon(":/user.ico")
addIcon = QIcon(":/add.gif")
editIcon = QIcon(":/edit.gif")
removeIcon = QIcon(":/remove.gif")
appIcon = QIcon(":/app.ico")

# load ApplicationData
appdata = None
if os.path.isfile('data'):
    with open('data') as file:
        appdata = pickle.load(file)
else:
    appdata = ApplicationData([], [], VoteData())

