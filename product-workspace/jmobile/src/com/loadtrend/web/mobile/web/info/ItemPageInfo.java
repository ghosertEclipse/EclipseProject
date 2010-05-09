package com.loadtrend.web.mobile.web.info;

import java.util.List;

public class ItemPageInfo {
	private int totalPage = 0;
	private List items = null;
    private int totalSize = 0;
	
	public ItemPageInfo(int totalSize, int totalPage, List items) {
        this.totalSize = totalSize;
		this.totalPage = totalPage;
		this.items = items;
	}

	/**
	 * @return Returns the items.
	 */
	public List getItems() {
		return items;
	}

	/**
	 * @return Returns the totalPage.
	 */
	public int getTotalPage() {
		return totalPage;
	}

    public int getTotalSize() {
        return totalSize;
    }
}
