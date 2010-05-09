package com.loadtrend.web.mobile.test.service;

import java.util.Iterator;

import com.loadtrend.web.mobile.dao.model.Item;
import com.loadtrend.web.mobile.dao.model.Product;

public class JMobileMangerTest extends ServiceTest {
    
    public void testGetRootProduct() {
        Product product = super.jMobileManager.getRootProduct(true);
        this.printProduct(product);
        assertEquals("root", product.getName());
    }
    
    public void testGetItem() {
        Item item = super.jMobileManager.getItem("1", false);
        System.out.println(item.getUrl());
        item = super.jMobileManager.getItem("sss", false);
        assertNull(item);
    }
    
    public void printProduct(Product product) {
        System.out.println(product.getName());
        Iterator it = product.getChildren().iterator();
        while (it.hasNext()) {
            Product childProduct = (Product) it.next();
            this.printProduct(childProduct);
        }
    }
    
    public void testGetProduct() {
        Product product = super.jMobileManager.getProduct("2",true);
        this.printProduct(product);
        assertEquals("picture", product.getName());
    }
    
}
