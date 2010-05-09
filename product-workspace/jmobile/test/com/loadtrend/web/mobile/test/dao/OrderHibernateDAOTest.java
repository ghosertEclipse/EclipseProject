package com.loadtrend.web.mobile.test.dao;

import java.util.Date;

import com.loadtrend.web.mobile.dao.model.Order;

public class OrderHibernateDAOTest extends HibernateDAOTest {
    
    public void testSaveOrder () {
        Order order = new Order();
        order.setLinkid("linkid");
        order.setOptime(new Date());
        order.setParam("param");
        order.setPayer("payer");
        order.setPaySuccess("0");
        order.setProductcode("productcode");
        order.setPushSuccess("0");
        order.setReceiver("receive");
        order.setSender("sender");
        super.orderDAO.saveOrder(order);
    }
}
