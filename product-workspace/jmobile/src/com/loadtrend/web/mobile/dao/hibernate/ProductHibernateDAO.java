package com.loadtrend.web.mobile.dao.hibernate;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.loadtrend.web.mobile.dao.ProductDAO;
import com.loadtrend.web.mobile.dao.model.Product;

public class ProductHibernateDAO extends HibernateDaoSupport implements ProductDAO {

    public Product getProduct(String id, boolean initialize) {
        Product product = (Product) super.getHibernateTemplate().get(Product.class, id);
        this.initialize(product, initialize);
        return product;
    }
    
    public List getProductsByName(String name) {
        return super.getHibernateTemplate().find("from Product p where p.name=?", name);
    }

    public void removeProduct(String id) {
        super.getHibernateTemplate().delete(this.getProduct(id, false));
    }
    
    public void removeProductsByName(String name) {
        super.getHibernateTemplate().deleteAll(this.getProductsByName(name));
    }

    public void saveProduct(Product product) {
        super.getHibernateTemplate().saveOrUpdate(product);
    }

    public Product getRootProduct(boolean initialize) {
        Product product = (Product) super.getHibernateTemplate().find("from Product p where p.parent=null").iterator().next();
        this.initialize(product, initialize);
        return product;
    }
    
    public void initialize(Product product, boolean initialize) {
    	if (product == null) return;
        if (initialize) Hibernate.initialize(product.getItems());
        Hibernate.initialize(product.getChildren());
        Iterator it = product.getChildren().iterator();
        while (it.hasNext()) {
            Product childProduct = (Product) it.next();
            this.initialize(childProduct, initialize);
        }
    }
}
