package com.loadtrend.app.mobile.loadtool.action;

import org.eclipse.jface.dialogs.IDialogConstants;

import com.loadtrend.app.mobile.loadtool.ResourceExplorerView;
import com.loadtrend.app.mobile.loadtool.dialog.ProductDialog;
import com.loadtrend.web.mobile.dao.model.Product;

public class AddProductAction extends NetAction {
    
	/* (non-Javadoc)
	 * @see com.loadtrend.app.mobile.action.NetAction#netExecute(org.eclipse.jface.action.IAction, com.loadtrend.web.mobile.service.JMobileManager)
	 */
	protected void netExecute() {
        Product product = (Product) this.structuredSelection.getFirstElement();
        ProductDialog productDialog = new ProductDialog(super.window.getShell());
        if (productDialog.open() == IDialogConstants.OK_ID) {
	        Product newProduct = new Product();
	        newProduct.setName(productDialog.getName());
	        newProduct.setParent(product);
	        super.jMobileManager.saveProduct(newProduct);
            ((ResourceExplorerView) super.view).refreshTreeViewerByRootProduct();
        }
        
	}
}
