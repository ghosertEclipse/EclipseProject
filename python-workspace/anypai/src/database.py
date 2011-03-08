# encoding: utf-8

import sqlite3

from datetime import datetime
from datetime import timedelta

from PyQt4.QtCore import QUrl

db_location = './storage/database'

class UserInfo:
    # set UserInfo retry will make that UserInfo re-process if our search mechanism pickup the same user once again.
    Status_RETRY = 0
    # initial status.
    Status_Not_Processing = 1
    # status after sending wangwang message, not implemented, jiawzhang TODO.
    Status_Processing = 2
    # status after getting NO response from wangwang, not implemented, jiawzhang TODO.
    Status_NotTo_Buy = 3
    # status after prepare to buy item.
    Status_Will_Buy = 4
    # status after confirming to buy the item.
    Status_Confirmed_Buy = 5
    # status after failing to payment.
    Status_Failed_Buy = 6
    # status after succeeding to payment.
    Status_Succeed_Buy = 7
    # status after confirming payment.
    Status_Confirmed_Payment = 8
    # status after refunding payment.
    Status_Refunded_Payment = 9
    # jiawzhang XXX: convert any string in this class to unicode, otherwise, it reports error when storing to sqlite3.
    def __init__(self, taobaoId = None, itemLink = None, wangwangLink = None, buyer_payment = None, seller_payment = None):
        """convert any string in this class to unicode before passing them into constructor, otherwise, it reports error when storing to sqlite3."""
        self.id = None
        self.taobaoId = taobaoId
        self.itemLink = itemLink
        self.wangwangLink = wangwangLink
        self.buyer_payment = buyer_payment
        self.seller_payment = seller_payment
        self.status = UserInfo.Status_Not_Processing
        self.last_status_time = datetime.now()
        self.alipayLink = None
        self.active = 1
    def __str__(self):
        return u'{0} {1} {2} {3} {4} {5} {6} {7} {8} {9}'.format(self.id, self.taobaoId, self.itemLink, self.wangwangLink, self.buyer_payment, self.seller_payment,
                                                         self.status, self.last_status_time, self.alipayLink, self.active)

def createTable():
    conn = sqlite3.connect(db_location, detect_types=sqlite3.PARSE_DECLTYPES|sqlite3.PARSE_COLNAMES)
    c = conn.cursor()

    # Create table
    c.execute("""
    create table user_info (id integer primary key, taobaoId text, itemLink text, wangwangLink text, buyer_payment real, seller_payment real,
    status integer, last_status_time timestamp, alipayLink text, active integer)
    """)
    
    # Save (commit) the changes
    conn.commit()
    
    # We can also close the cursor if we are done with it
    c.close()
    conn.close()

def saveUser(userInfo):
    conn = sqlite3.connect(db_location, detect_types=sqlite3.PARSE_DECLTYPES|sqlite3.PARSE_COLNAMES)
    with conn:
        conn.execute("""
        insert into user_info(taobaoId, itemLink, wangwangLink, buyer_payment, seller_payment, status, last_status_time, active) values
        (?, ?, ?, ?, ?, ?, ?, ?)
        """, (userInfo.taobaoId, userInfo.itemLink, userInfo.wangwangLink, userInfo.buyer_payment, userInfo.seller_payment, userInfo.status,
              userInfo.last_status_time, userInfo.active))
    conn.close()

def updateUser(userInfo, **dict):
    if not dict:
        return
    conn = sqlite3.connect(db_location, detect_types=sqlite3.PARSE_DECLTYPES|sqlite3.PARSE_COLNAMES)
    updateSql = ''
    params = []
    for key, value in dict.iteritems():
        updateSql = updateSql + key + ' = ?,'
        params.append(value)
        setattr(userInfo, key, value)
    updateSql = updateSql[0 : len(updateSql) - 1] + ' where id = ?'
    params.append(userInfo.id)
        
    with conn:
        conn.execute("update user_info set " + updateSql, params)
    conn.close()
    
def getActiveUserByTaobaoId(taobaoId):
    users = getObjects(UserInfo, 'select * from user_info where taobaoId = ? and active = 1', taobaoId)
    if users:
        return users[0]
    else:
        return None
    
def getUnhandledUserInfoList():
    "Get all the unhandled userInfos to be handled."
    users = getObjects(UserInfo, 'select * from user_info where status in (?, ?, ?, ?) and active = 1',
                       UserInfo.Status_Not_Processing, UserInfo.Status_Processing, UserInfo.Status_Will_Buy, UserInfo.Status_Confirmed_Buy)
    return users

def getHandledUserInfoList():
    "Get all the handled userInfos."
    users = getObjects(UserInfo, 'select * from user_info where status in (?, ?, ?, ?, ?, ?)',
                       UserInfo.Status_RETRY, UserInfo.Status_NotTo_Buy, UserInfo.Status_Failed_Buy,
                       UserInfo.Status_Succeed_Buy, UserInfo.Status_Confirmed_Payment, UserInfo.Status_Refunded_Payment)
    return users
    
def getObjects(class_type, select_sql, *params):
    "A general function for get objects directly from db table. detect_types here is necessary for datetime default adapter from python, see python sqlite3 doc."
    conn = sqlite3.connect(db_location, detect_types=sqlite3.PARSE_DECLTYPES|sqlite3.PARSE_COLNAMES)
    conn.row_factory = sqlite3.Row
    cursor = conn.cursor()
    
    objects = []
    if params:
        cursor.execute(select_sql, params)
    else:
        cursor.execute(select_sql)
    for row in cursor:
        object = class_type()
        for key in row.keys():
            setattr(object, key, row[key])
        objects.append(object)
        
    cursor.close()
    conn.close()
    
    return objects
    
def __getUsersMonthly():
    "Get the users within 31 days, an sample function for date time type."
    users = getObjects(UserInfo, 'select * from user_info where last_status_time >= ?', datetime.now() - timedelta(31))
    return users

def __test():
    db_location = './storage/database_test'
    import os
    if os.path.exists(db_location):
        os.remove(db_location)
    createTable()
    # jiawzhang XXX: The string to be saved to sqlite must be unicode first, otherwise, error happens.
    userInfo = UserInfo(u'代理梦想家80后', u'http://item.taobao.com/item.htm?id=9248227645',
    unicode(QUrl.fromPercentEncoding(u'http://www.taobao.com/webww/?ver=1&&touid=cntaobao%E4%BB%A3%E7%90%86%E6%A2%A6%E6%83%B3%E5%AE%B680%E5%90%8E&siteid=cntaobao&status=1&portalId=&gid=9190349629&itemsId=')),
    0.80, 1.00)
    saveUser(userInfo)
    saveUser(userInfo)
    userInfo.last_status_time = datetime.now() - timedelta(31)
    saveUser(userInfo)
    
    'get all users'
    users = getObjects(UserInfo, 'select * from user_info')
    userInfo = users[0]
    for user in users:
        print user
        
        
    print 'update active = 0 and then get usersMonthly:'
    updateUser(userInfo, active = 0)
    print 'userInfo.active: ' + str(userInfo.active)
    
    
    print '__getUsersMonthly'
    users = __getUsersMonthly()
    for user in users:
        print user
    
    print 'getActiveByTaobaoId'
    userInfo = getActiveUserByTaobaoId(u'代理梦想家80后')
    print userInfo
    
    print 'getUnhandledUserInfoList'
    userInfos = getUnhandledUserInfoList()
    for userInfo in userInfos:
        print userInfo
    
def __queryProduction():
    # users = getObjects(UserInfo, 'select * from user_info where id = 7 and active = 1')
    # updateUser(users[0], status = UserInfo.Status_Succeed_Buy)
    print 'getUnhandledUserInfoList'
    userInfos = getUnhandledUserInfoList()
    for userInfo in userInfos:
        print userInfo
    print 'getHandledUserInfoList'
    userInfos = getHandledUserInfoList()
    for userInfo in userInfos:
        print userInfo
        
if __name__ == '__main__':
    # __test()
    __queryProduction()
    