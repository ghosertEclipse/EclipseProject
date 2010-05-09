package com.loadtrend.web.mobile.test.dao;

import java.util.Iterator;

import com.loadtrend.web.mobile.dao.model.Product;

public class ProductHibernateDAOTest extends HibernateDAOTest {
    
    public void testAddChildToProduct() {
        
        // Remove root product.
        Product rootProduct = productDAO.getRootProduct(false);
        productDAO.removeProduct(rootProduct.getId());
        
        // Set root product.
        rootProduct = new Product();
        rootProduct.setName("root");
        productDAO.saveProduct(rootProduct);
        
        // Set child product of root product.
        Product pictProduct = new Product();
        pictProduct.setName("picture");
        pictProduct.setParent(rootProduct);
        productDAO.saveProduct(pictProduct);
        
        Product musicProduct = new Product();
        musicProduct.setName("music");
        musicProduct.setParent(rootProduct);
        productDAO.saveProduct(musicProduct);
        
        // Set child child product of picture, music.
        Product sexProcut = new Product();
        sexProcut.setName("sex");
        sexProcut.setParent(pictProduct);
        productDAO.saveProduct(sexProcut);
        
        Product starProcut = new Product();
        starProcut.setName("star");
        starProcut.setParent(musicProduct);
        productDAO.saveProduct(starProcut);
        
        // Print root product.
        rootProduct.getChildren().add(pictProduct);
        rootProduct.getChildren().add(musicProduct);
        pictProduct.getChildren().add(sexProcut);
        musicProduct.getChildren().add(starProcut);
        this.printRootProduct(rootProduct, 0);
    }
    
    private void printRootProduct(Product rootProduct, int i) {
        System.out.println(this.getSpace(i) + rootProduct.getName());
        Iterator it = rootProduct.getChildren().iterator();
        while (it.hasNext()) {
            Product product = (Product) it.next();
            this.printRootProduct(product, i + 1);
        }
    }
    
    private String getSpace(int i) {
        String string = "";
        for (int j = 0; j < i; j++) {
            string = string + " ";
        }
        return string;
    }
}
