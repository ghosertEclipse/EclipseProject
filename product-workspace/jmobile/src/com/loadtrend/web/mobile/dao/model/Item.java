package com.loadtrend.web.mobile.dao.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Item implements Serializable {
	private String id = null;

	private Product product = null;

	private String name = null;

	private String author = null;

	private String url = null;
    
    private String uploader = null;
    
    private Integer weekpaytimes = null;
    
    private Integer paytimes = null;
    
    private String isValid = null;
    
    private Date uploadtime = null;
    
    private String itemType = null;

	private Set orders = new HashSet();

	/**
	 * @return Returns the author.
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author
	 *            The author to set.
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the orders.
	 */
	public Set getOrders() {
		return orders;
	}

	/**
	 * @param orders
	 *            The orders to set.
	 */
	public void setOrders(Set orders) {
		this.orders = orders;
	}

	/**
	 * @return Returns the product.
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * @param product
	 *            The product to set.
	 */
	public void setProduct(Product product) {
		this.product = product;
	}

	/**
	 * @return Returns the url.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            The url to set.
	 */
	public void setUrl(String url) {
		this.url = url;
	}

    public Integer getPaytimes() {
        return paytimes;
    }

    public void setPaytimes(Integer paytimes) {
        this.paytimes = paytimes;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public Date getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(Date uploadtime) {
        this.uploadtime = uploadtime;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public Integer getWeekpaytimes() {
        return weekpaytimes;
    }

    public void setWeekpaytimes(Integer weekpaytimes) {
        this.weekpaytimes = weekpaytimes;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    /**
     * Rewrite the equals method for using LinkedHashSet.
     */
    public boolean equals( Object obj )
    {
        // Step 1
        if ( this == obj ) return true;
        
        // Step 2
        if ( obj == null ) return false;
        
        // Step 3
        if ( this.getClass() != obj.getClass() ) return false;
        
        // Step 4
        Item item = (Item) obj;
        
        if (this.name.equals(item.getName()) && this.url.equals(item.getUrl())) {
            return true;
        }
        return false;
    }

    /**
     * Rewrite hashCode if rewrite the equals( Object ) method.
     */
    public int hashCode() {
        int result = 17;
        result = result * 37 + (this.name == null ? 0 : this.name.hashCode());
        result = result * 37 + (this.url == null ? 0 : this.url.hashCode());
        return result;
    }
}
