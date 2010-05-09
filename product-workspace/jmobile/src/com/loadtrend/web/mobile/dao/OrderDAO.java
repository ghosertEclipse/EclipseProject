package com.loadtrend.web.mobile.dao;

import com.loadtrend.web.mobile.dao.model.Order;

public interface OrderDAO {
    public Order getOrder(String id);
    public void saveOrder(Order order);
    public void removeOrder(String id);
}
