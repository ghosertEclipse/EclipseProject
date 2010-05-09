package com.loadtrend.web.mobile.dao.hibernate;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.loadtrend.web.mobile.dao.OrderDAO;
import com.loadtrend.web.mobile.dao.model.Order;

public class OrderHibernateDAO extends HibernateDaoSupport implements OrderDAO {

    public Order getOrder(String id) {
        Order order = (Order) super.getHibernateTemplate().get(Order.class, id);
        return order;
    }

    public void removeOrder(String id) {
        super.getHibernateTemplate().delete(this.getOrder(id));
    }

    public void saveOrder(Order order) {
        super.getHibernateTemplate().saveOrUpdate(order);
    }
}
