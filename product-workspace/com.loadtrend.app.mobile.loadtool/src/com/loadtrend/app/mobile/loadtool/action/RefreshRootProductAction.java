package com.loadtrend.app.mobile.loadtool.action;

import java.util.Iterator;

import com.loadtrend.app.mobile.loadtool.MusicExplorerView;
import com.loadtrend.app.mobile.loadtool.PictureExplorerView;
import com.loadtrend.app.mobile.loadtool.ResourceExplorerView;
import com.loadtrend.web.mobile.dao.model.Product;

public class RefreshRootProductAction extends NetAction {
    
    private ResourceExplorerView view = null;
    
    public RefreshRootProductAction(ResourceExplorerView view) {
        this.view = view;
        super.window = view.getSite().getWorkbenchWindow();
    }
    
	protected void netExecute() {
        Product product = super.jMobileManager.getRootProduct(true);
        if (view instanceof PictureExplorerView) {
            Iterator it = product.getChildren().iterator();
            while (it.hasNext()) {
                Product picture = (Product) it.next();
                if (picture.getId().equals("2")) {
                    product.getChildren().clear();
                    product.getChildren().add(picture);
                    break;
                }
            }
            view.refresh(product);
        }
        if (view instanceof MusicExplorerView) {
            Iterator it = product.getChildren().iterator();
            while (it.hasNext()) {
                Product music = (Product) it.next();
                if (music.getId().equals("3")) {
                    product.getChildren().clear();
                    product.getChildren().add(music);
                    break;
                }
            }
            view.refresh(product);
        }
	}
}
