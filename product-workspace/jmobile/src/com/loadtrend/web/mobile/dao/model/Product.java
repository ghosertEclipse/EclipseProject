package com.loadtrend.web.mobile.dao.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Product implements Serializable {
	private String id = null;

	private String name = null;

	private Set items = new HashSet();
    
    private Product parent = null;
    
    private Set children = new HashSet();
    
    public Product getParent() {
        return parent;
    }

    public void setParent(Product parent) {
        this.parent = parent;
    }

    public Set getChildren() {
        return children;
    }

    public void setChildren(Set children) {
        this.children = children;
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
	 * @return Returns the items.
	 */
	public Set getItems() {
		return items;
	}

	/**
	 * @param items
	 *            The items to set.
	 */
	public void setItems(Set items) {
		this.items = items;
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

}
