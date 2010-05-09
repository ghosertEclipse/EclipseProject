package com.loadtrend.app.mobile.loadtool.action;

import com.loadtrend.app.mobile.loadtool.ResourceEditor;
import com.loadtrend.app.mobile.loadtool.ResourceExplorerView;
import com.loadtrend.web.mobile.dao.model.Item;

public class ChangeItemValidStatusAction extends NetAction {

	private Item item = null;
	
	private ResourceEditor editor = null;
	
	private ResourceExplorerView view = null;
	
	private int page = 0;
	
	public ChangeItemValidStatusAction(ResourceEditor editor, ResourceExplorerView view, Item item, int page) {
		super("±ä¸ü×´Ì¬");
		super.window = editor.getSite().getWorkbenchWindow();
		this.item = item;
		this.editor = editor;
		this.view = view;
		this.page = page;
	}

	protected void netExecute() {
		if (this.item.getIsValid().equals("1")) {
			this.item.setIsValid("0");
		} else {
			this.item.setIsValid("1");
		}
        super.jMobileManager.saveItem(item);
        
        this.view.refreshTreeViewerByRootProduct();
        editor.showResourceFromProduct(view, item.getProduct(), page);
	}
}
