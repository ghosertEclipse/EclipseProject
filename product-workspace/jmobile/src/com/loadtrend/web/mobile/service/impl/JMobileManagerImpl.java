package com.loadtrend.web.mobile.service.impl;

import java.util.Collection;
import java.util.List;

import com.loadtrend.web.mobile.dao.ItemDAO;
import com.loadtrend.web.mobile.dao.OrderDAO;
import com.loadtrend.web.mobile.dao.ProductDAO;
import com.loadtrend.web.mobile.dao.UserRecorderDAO;
import com.loadtrend.web.mobile.dao.model.Item;
import com.loadtrend.web.mobile.dao.model.Order;
import com.loadtrend.web.mobile.dao.model.Product;
import com.loadtrend.web.mobile.dao.model.UserRecorder;
import com.loadtrend.web.mobile.service.JMobileManager;

public class JMobileManagerImpl implements JMobileManager {
    private ItemDAO itemDAO = null;

    private ProductDAO productDAO = null;

    private OrderDAO orderDAO = null;
    
    private UserRecorderDAO userRecorderDAO = null;

    public void setItemDAO(ItemDAO itemDAO) {
        this.itemDAO = itemDAO;
    }
    public List getValidItemsByProduct(Product product) {
        return itemDAO.getValidItemsByProduct(product);
    }
    
    public List getValidItemsBySearch(String key, String itemType) {
    	return itemDAO.getValidItemsBySearch(key, itemType);
    }

    public List getItemsByUploader(String uploader, String itemType, int pageNumber, int pageSize) {
    	return itemDAO.getItemsByUploader(uploader, itemType, pageNumber, pageSize);
    }
    
    public int getTotalSize(String uploader, String itemType) {
    	return itemDAO.getTotalSize(uploader, itemType);
    }

    public void setOrderDAO(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public Item getItem(String id, boolean initialize) {
        return itemDAO.getItem(id, initialize);
    }

    public Product getProduct(String id, boolean initialize) {
        return productDAO.getProduct(id, initialize);
    }

    public Product getRootProduct(boolean initialize) {
        return productDAO.getRootProduct(initialize);
    }

    public void removeItem(String id) {
        itemDAO.removeItem(id);
    }
    
    public void removeItems(Collection items) {
        itemDAO.removeItems(items);
    }
    
    public void saveItems(Collection items) {
        itemDAO.saveItems(items);
    }
    
    public void saveUniqueItems(Product product, Collection items) {
    	itemDAO.saveUniqueItems(product, items);
    }
    
    public void removeProduct(String id) {
        productDAO.removeProduct(id);
    }

    public void saveItem(Item item) {
        itemDAO.saveItem(item);
    }

    public void saveProduct(Product product) {
        productDAO.saveProduct(product);
    }

	/* (non-Javadoc)
	 * @see com.loadtrend.web.mobile.service.JMobileManager#getTopWeekPayTimesUploaders(int)
	 */
	public List getTopWeekPayTimesUploaders(int topNumber) {
		return itemDAO.getTopWeekPayTimesUploaders(topNumber);
	}

    public Order getOrder(String id) {
        return orderDAO.getOrder(id);
    }

    public void removeOrder(String id) {
        orderDAO.removeOrder(id);
    }

    public void saveOrder(Order order) {
        orderDAO.saveOrder(order);
    }
    
    public void setUserRecorderDAO(UserRecorderDAO userRecorderDAO) {
		this.userRecorderDAO = userRecorderDAO;
	}
    
	public void saveUserRecorder(UserRecorder userRecorder) {
        userRecorderDAO.saveUserRecorder(userRecorder);
    }
}
