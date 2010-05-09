package com.loadtrend.web.mobile.test.dao;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.loadtrend.web.mobile.dao.model.Item;
import com.loadtrend.web.mobile.dao.model.Product;

public class ItemHibernateDAOTest extends HibernateDAOTest {
    
    public void testAddRemoveItemToProduct() {
        Item item = new Item();
        item.setAuthor("jiawei");
        item.setName("√¿≈Æ∞°");
        item.setWeekpaytimes(new Integer(0));
        item.setPaytimes(new Integer(0));
        Product product = (Product) productDAO.getProductsByName("sex").iterator().next();
        item.setProduct(product);
        item.setUploader("13916939847");
        item.setUploadtime(new Date());
        item.setUrl("http://www.google.com/sex.gif");
        item.setItemType("0");
        
        super.itemDAO.saveItem(item);
        // super.itemDAO.removeItem(item.getId());
    }
    
    public void testRemoveItemHavingOrders() {
    	Item item = super.itemDAO.getItem("657", false);
    	super.itemDAO.removeItem(item.getId());
    }
    
    public void testItemPage() {
    	// Get picture item type uploaded by 13916939847
    	int pageSize = 10;
    	List items = super.itemDAO.getItemsByUploader("13916939847", "0", 1, pageSize);
    	int totalSize = super.itemDAO.getTotalSize("13916939847", "0");
    	Iterator it = items.iterator();
    	while (it.hasNext()) {
    		Item item = (Item) it.next();
    		System.out.println(item.getName());
    	}
    	System.out.println(totalSize);
    	System.out.println((totalSize-1)/pageSize + 1);
    }
    
    public void testGetTopWeekPayTimesUploaders() {
    	List list = super.itemDAO.getTopWeekPayTimesUploaders(10);
        System.out.println(list);
    }
    
    public void testGetValidItemsByProduct() {
        Product product = productDAO.getProduct("4", false);
        List list = super.itemDAO.getValidItemsByProduct(product);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Item item = (Item) it.next();
            System.out.println(item.getId());
        }
    }
}
