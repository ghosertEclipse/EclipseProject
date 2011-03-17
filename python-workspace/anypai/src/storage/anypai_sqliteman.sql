select * from user_info where alipayLink is not null;

select * from user_info where active = 1;

select * from user_info where status = 5;

select * from user_info order by last_status_time desc;

select datetime('now', 'localtime', '-31 days');
select datetime('now') < '2011-01-01 00:00:00';

# all the items which are purchased successfully, while status = 6 may be refunded, status = 7 is confirmed and rated items
select * from user_info where status >= 7;

# all the items with valid status.
select * from user_info where active = 1;

# if there is any results from this sql, means system issue, there should be no item could be purchased twice in 31 days.
select taobaoId, count(1) from user_info where status >= 7 and last_status_time >= datetime('now', 'localtime', '-31 days') group by taobaoId having count(1) > 1;


/*
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
    # status after succeeding to payment. if the item keeps 7 always, means this item is refunded.
    Status_Succeed_Buy = 7
    # status after confirming payment.
    Status_Confirmed_Payment = 8
*/