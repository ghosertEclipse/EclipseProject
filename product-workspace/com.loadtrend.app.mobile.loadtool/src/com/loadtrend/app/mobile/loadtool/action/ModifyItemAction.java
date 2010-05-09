package com.loadtrend.app.mobile.loadtool.action;

import java.util.Iterator;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import com.loadtrend.app.mobile.loadtool.ResourceEditor;
import com.loadtrend.app.mobile.loadtool.ResourceExplorerView;
import com.loadtrend.app.mobile.loadtool.dialog.ItemDialog;
import com.loadtrend.web.mobile.dao.model.Item;
import com.loadtrend.web.mobile.dao.model.Product;

public class ModifyItemAction extends NetAction {
	
	private Item item = null;
	
	private ResourceEditor editor = null;
	
	private ResourceExplorerView view = null;
	
	private int page = 0;
	
	public ModifyItemAction(ResourceEditor editor, ResourceExplorerView view, Item item, int page) {
		super("修改条目");
		super.window = editor.getSite().getWorkbenchWindow();
		this.item = item;
		this.editor = editor;
		this.view = view;
		this.page = page;
	}

	protected void netExecute() {
        ItemDialog itemDialog = new ItemDialog(super.window.getShell());
        itemDialog.create();
        itemDialog.setAuthor(item.getAuthor() == null ? "" : item.getAuthor());
        itemDialog.setName(item.getName() == null ? "" : item.getName());
        itemDialog.setPaytimes(item.getPaytimes().toString());
        itemDialog.setProductId(item.getProduct().getId());
        itemDialog.setUploader(item.getUploader() == null ? "" : item.getUploader());
        itemDialog.setUploadTime(item.getUploadtime().toString());
        itemDialog.setUrl(item.getUrl() == null ? "" : item.getUrl());
        itemDialog.setValid(item.getIsValid().equals("1"));
        itemDialog.setWeekpaytimes(item.getWeekpaytimes().toString());
        if (itemDialog.open() == IDialogConstants.OK_ID) {
        	item.setAuthor(itemDialog.getAuthor().equals("") ? null : itemDialog.getAuthor());
        	item.setIsValid(itemDialog.isValid() ? "1" : "0");
        	item.setName(itemDialog.getName());
        	item.setPaytimes(new Integer(itemDialog.getPaytimes()));
       	    Product moveToProduct = this.searchProduct(view.getRootProduct(), itemDialog.getProductId());
       	    if (moveToProduct == null) {
       	    	MessageBox box = new MessageBox(window.getShell(), SWT.ICON_ERROR | SWT.OK);
       	    	box.setMessage("指定的分类不存在!");
       	    	box.open();
       	    	return;
       	    }
       	    Product currentProduct = item.getProduct();
        	item.setProduct(moveToProduct);
        	item.setUploader(itemDialog.getUploader().equals("") ? null : itemDialog.getUploader());
        	item.setUrl(itemDialog.getUrl());
        	item.setWeekpaytimes(new Integer(itemDialog.getWeekpaytimes()));
        	
	        super.jMobileManager.saveItem(item);
            currentProduct = super.jMobileManager.getProduct(currentProduct.getId(), true);
            this.view.refreshTreeViewerByRootProduct();
            editor.showResourceFromProduct(view, currentProduct, page);
        }
	}
	
	private Product searchProduct(Product rootProduct, String id) {
		Iterator it = rootProduct.getChildren().iterator();
		while (it.hasNext()) {
			Product product = (Product) it.next();
			product = this.searchProduct(product, id);
			if (product != null) return product;
		}
		return rootProduct.getId().equals(id) ? rootProduct : null;
	}
}
