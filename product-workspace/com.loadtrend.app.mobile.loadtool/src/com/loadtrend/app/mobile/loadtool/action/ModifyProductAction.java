package com.loadtrend.app.mobile.loadtool.action;

import org.eclipse.jface.dialogs.IDialogConstants;

import com.loadtrend.app.mobile.loadtool.ResourceExplorerView;
import com.loadtrend.app.mobile.loadtool.dialog.ProductDialog;
import com.loadtrend.web.mobile.dao.model.Product;

public class ModifyProductAction extends NetAction {

	protected void netExecute() {
        Product product = (Product) this.structuredSelection.getFirstElement();
        ProductDialog productDialog = new ProductDialog(super.window.getShell());
        productDialog.create();
        productDialog.setName(product.getName());
        if (productDialog.open() == IDialogConstants.OK_ID) {
            product.setName(productDialog.getName());
	        super.jMobileManager.saveProduct(product);
            ((ResourceExplorerView) super.view).refreshTreeViewerByRootProduct();
        }

	}
}
