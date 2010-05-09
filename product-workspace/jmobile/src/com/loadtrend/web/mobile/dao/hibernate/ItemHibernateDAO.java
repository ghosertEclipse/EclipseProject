package com.loadtrend.web.mobile.dao.hibernate;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.loadtrend.web.mobile.dao.ItemDAO;
import com.loadtrend.web.mobile.dao.model.Item;
import com.loadtrend.web.mobile.dao.model.Product;

public class ItemHibernateDAO extends HibernateDaoSupport implements ItemDAO {

    public Item getItem(String id, boolean initialize) {
        Item item = (Item) super.getHibernateTemplate().get(Item.class, id);
        if (initialize && item != null) {
            Hibernate.initialize(item.getOrders());
        }
        return item;
    }

    public void removeItem(String id) {
        super.getHibernateTemplate().delete(this.getItem(id, false));
    }

    public void saveItem(Item item) {
        super.getHibernateTemplate().saveOrUpdate(item);
    }

    public void removeItems(Collection items) {
        super.getHibernateTemplate().deleteAll(items);
    }

    public void saveItems(Collection items) {
        super.getHibernateTemplate().saveOrUpdateAll(items);
    }
    
    public void saveUniqueItems(Product product, Collection items) {
    	List list = super.getHibernateTemplate().find("from Item i where i.product=?", product);
    	HashSet set = new LinkedHashSet();
    	set.addAll(list);
    	set.addAll(items);
    	this.saveItems(set);
    }
    
    public List getValidItemsByProduct(Product product) {
        Session session = super.getSession();
        Query query = session.createQuery("from Item i where i.isValid=? and i.product=? order by i.weekpaytimes desc, i.id desc");
        query.setString(0, "1");
        query.setEntity(1, product);
    	return query.list();
    }
    
    public List getValidItemsBySearch(String key, String itemType) {
        Session session = super.getSession();
        Query query = session.createQuery("from Item i where i.isValid=? and i.itemType=? and (i.name like ? or i.author like ?) order by i.weekpaytimes desc, i.id desc");
        query.setString(0, "1");
        query.setString(1, itemType);
        query.setString(2, "%" + key + "%");
        query.setString(3, "%" + key + "%");
    	return query.list();
    }

    public List getItemsByUploader(String uploader, String itemType, int pageNumber, int pageSize) {
    	Session session = super.getSession();
    	Query query = session.createQuery("from Item where uploader=? and itemType=? order by weekpaytimes desc, id desc");
    	query.setString(0, uploader);
    	query.setString(1, itemType);
    	query.setFirstResult((pageNumber -1) * pageSize);
    	query.setMaxResults(pageSize);
    	return query.list();
    }
    
    public int getTotalSize(String uploader, String itemType) {
    	List list = super.getHibernateTemplate().find("select count(id) from Item where uploader=? and itemType=?",
    			                                      new String[] {uploader, itemType});
    	Integer totalPage = (Integer) list.iterator().next();
    	return totalPage.intValue();
    }

	public List getTopWeekPayTimesUploaders(int topNumber) {
    	Session session = super.getSession();
    	Query query = session.createQuery("select uploader, sum(weekpaytimes) from Item where uploader is not null " +
    			                          "group by uploader order by 2 desc, id desc");
    	query.setFirstResult(0);
    	query.setMaxResults(topNumber);
    	return query.list();
	}
    
}
