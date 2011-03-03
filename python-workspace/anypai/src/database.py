# encoding: utf-8

import sqlite3

from datetime import datetime

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
    def __init__(self, taobaoId, itemLink, wangwangLink, buyer_payment, seller_payment):
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

def create_table():
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

def save_user(userInfo):
    conn = sqlite3.connect(db_location, detect_types=sqlite3.PARSE_DECLTYPES|sqlite3.PARSE_COLNAMES)
    with conn:
        conn.execute("""
        insert into user_info(taobaoId, itemLink, wangwangLink, buyer_payment, seller_payment, status, last_status_time, active) values
        (?, ?, ?, ?, ?, ?, ?, ?)
        """, (userInfo.taobaoId, userInfo.itemLink, userInfo.wangwangLink, userInfo.buyer_payment, userInfo.seller_payment, userInfo.status,
              userInfo.last_status_time, userInfo.active))
    conn.close()

def get_user():
    conn = sqlite3.connect(db_location, detect_types=sqlite3.PARSE_DECLTYPES|sqlite3.PARSE_COLNAMES)
    c = conn.cursor()
    c.execute("select * from user_info")
    for row in c:
        print row
    c.close()
    conn.close()
    
if __name__ == '__main__':
    db_location = './storage/database_test'
    import os
    os.system('rm {0}'.format(db_location))
    create_table()
    userInfo = UserInfo(u'代理梦想家80后', u'http://item.taobao.com/item.htm?id=9248227645',
    unicode(QUrl.fromPercentEncoding(u'http://www.taobao.com/webww/?ver=1&&touid=cntaobao%E4%BB%A3%E7%90%86%E6%A2%A6%E6%83%B3%E5%AE%B680%E5%90%8E&siteid=cntaobao&status=1&portalId=&gid=9190349629&itemsId=')),
    0.80, 1.00)
    save_user(userInfo)
    save_user(userInfo)
    save_user(userInfo)
    get_user()
    
