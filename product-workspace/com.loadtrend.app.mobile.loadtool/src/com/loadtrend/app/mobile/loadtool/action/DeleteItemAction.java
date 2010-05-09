package com.loadtrend.app.mobile.loadtool.action;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import com.loadtrend.app.mobile.loadtool.ResourceEditor;
import com.loadtrend.app.mobile.loadtool.ResourceExplorerView;
import com.loadtrend.web.mobile.dao.model.Item;

public class DeleteItemAction extends NetAction {
	
	private Item item = null;
	
	private ResourceEditor editor = null;
	
	private ResourceExplorerView view = null;
	
	private int page = 0;
	
	public DeleteItemAction(ResourceEditor editor, ResourceExplorerView view, Item item, int page) {
		super("删除条目");
		super.window = editor.getSite().getWorkbenchWindow();
		this.item = item;
		this.editor = editor;
		this.view = view;
		this.page = page;
	}

	protected void netExecute() {
        MessageBox messageBox = new MessageBox(super.window.getShell(), SWT.OK | SWT.CANCEL);
        messageBox.setMessage("真的删除该条目吗？");
        int resultCode = messageBox.open();
        if (resultCode == SWT.CANCEL) return;
        
        super.jMobileManager.removeItem(item.getId());
        item.getProduct().getItems().remove(item);
        
        this.view.refreshTreeViewerByRootProduct();
        editor.showResourceFromProduct(view, item.getProduct(), page);
	}
}
