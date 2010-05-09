package com.loadtrend.web.mobile.dao;

import java.util.Collection;
import java.util.List;

import com.loadtrend.web.mobile.dao.model.Item;
import com.loadtrend.web.mobile.dao.model.Product;

public interface ItemDAO {
    public Item getItem(String id, boolean initialize);
    public void saveItem(Item item);
    public void removeItem(String id);
    public void removeItems(Collection items);
    public void saveItems(Collection items);
    public void saveUniqueItems(Product product, Collection items);
    public List getValidItemsByProduct(Product product);
    public List getValidItemsBySearch(String key, String itemType);
    public List getItemsByUploader(String uploader, String itemType, int pageNumber, int pageSize);
    public int getTotalSize(String uploader, String itemType);
    public List getTopWeekPayTimesUploaders(int topNumber);
}
