select * from user_info where alipayLink is not null;

select * from user_info where active = 1;

select * from user_info where status = 5;

select * from user_info order by last_status_time desc;

select datetime('now');
select datetime('now') < '2011-01-01 00:00:00';

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
    # status after succeeding to payment.
    Status_Succeed_Buy = 7
    # status after confirming payment.
    Status_Confirmed_Payment = 8
    # status after refunding payment.
    Status_Refunded_Payment = 9
*/
