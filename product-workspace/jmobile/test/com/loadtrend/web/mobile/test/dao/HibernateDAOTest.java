package com.loadtrend.web.mobile.test.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.loadtrend.web.mobile.dao.ItemDAO;
import com.loadtrend.web.mobile.dao.OrderDAO;
import com.loadtrend.web.mobile.dao.ProductDAO;

import junit.framework.TestCase;

public class HibernateDAOTest extends TestCase {
    
    protected ApplicationContext applicationContext = null;
    
    protected ProductDAO productDAO = null;
    
    protected ItemDAO itemDAO = null;
    
    protected OrderDAO orderDAO = null;
    
    public HibernateDAOTest() {
        applicationContext = new FileSystemXmlApplicationContext("/war/WEB-INF/applicationContext-hibernate.xml");
        productDAO = (ProductDAO) applicationContext.getBean("productDAO");
        itemDAO = (ItemDAO) applicationContext.getBean("itemDAO");
        orderDAO = (OrderDAO) applicationContext.getBean("orderDAO");
    }
}
