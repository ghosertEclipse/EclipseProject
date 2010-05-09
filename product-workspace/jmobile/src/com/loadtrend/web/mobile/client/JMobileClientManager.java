package com.loadtrend.web.mobile.client;

import java.io.IOException;
import java.util.List;

import com.loadtrend.web.mobile.dao.model.Item;
import com.loadtrend.web.mobile.dao.model.Product;

public interface JMobileClientManager {
    public Item uploadLocalFile(String filename, byte[] bytes) throws IOException;
    public Product getRootProduct(boolean initialize);
    public Product getProduct(String id, boolean initialize);
    public List getValidItemsByProduct(Product product);
    public List getValidItemsBySearch(String key, String itemType);
    public boolean isRegistedUser(String mobileNumber);
    public void saveUserRecorder(String mobile);
}
