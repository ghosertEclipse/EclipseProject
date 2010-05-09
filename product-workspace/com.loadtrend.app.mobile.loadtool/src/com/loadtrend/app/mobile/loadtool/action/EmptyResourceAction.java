package com.loadtrend.app.mobile.loadtool.action;

import com.loadtrend.app.mobile.loadtool.ResourceExplorerView;
import com.loadtrend.web.mobile.dao.model.Product;

public class EmptyResourceAction extends NetAction {

	protected void netExecute() {
        Product product = (Product) this.structuredSelection.getFirstElement();
        super.jMobileManager.removeItems(product.getItems());
        product.getItems().clear();
        
        // Refresh treeViewer.
        ((ResourceExplorerView)super.view).refreshTreeViewerByRootProduct();
        ((ResourceExplorerView)super.view).showResourceEditor(product);
	}

}
