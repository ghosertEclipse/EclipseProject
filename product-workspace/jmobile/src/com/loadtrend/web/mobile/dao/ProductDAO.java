package com.loadtrend.web.mobile.dao;

import java.util.List;

import com.loadtrend.web.mobile.dao.model.Product;

public interface ProductDAO {
    public Product getProduct(String id, boolean initialize);
    public Product getRootProduct(boolean initialize);
    public List getProductsByName(String name);
    public void saveProduct(Product product);
    public void removeProduct(String id);
    public void removeProductsByName(String name);
}
