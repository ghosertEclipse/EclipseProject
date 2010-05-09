package com.loadtrend.app.mobile.loadtool.action;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import com.loadtrend.app.mobile.loadtool.ResourceExplorerView;
import com.loadtrend.web.mobile.dao.model.Product;

public class DeleteProductAction extends NetAction {

	protected void netExecute() {
        MessageBox messageBox = new MessageBox(super.window.getShell(), SWT.OK | SWT.CANCEL);
        messageBox.setMessage("真的删除分类吗？");
        int resultCode = messageBox.open();
        if (resultCode == SWT.CANCEL) return;
        
        Product product = (Product) this.structuredSelection.getFirstElement();
        super.jMobileManager.removeProduct(product.getId());
        product.getItems().clear();
        ((ResourceExplorerView) super.view).refreshTreeViewerByRootProduct();
        ((ResourceExplorerView)super.view).showResourceEditor(product);
	}
    
    

}
