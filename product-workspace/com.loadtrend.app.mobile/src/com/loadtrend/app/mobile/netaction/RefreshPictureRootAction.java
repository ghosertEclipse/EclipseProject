package com.loadtrend.app.mobile.netaction;

import com.loadtrend.app.mobile.views.media.PictureExplorerView;
import com.loadtrend.web.mobile.dao.model.Product;

public class RefreshPictureRootAction extends NetAction {
    
    private Product product = null;

    public RefreshPictureRootAction(PictureExplorerView view) {
        super.view = view;
        super.window = view.getSite().getWorkbenchWindow();
    }

    protected void netExecute() throws Exception {
        super.showRealTimeInfo("正在获取彩图分类信息");
        Product root = new Product();
        product = super.jMobileClientManager.getProduct("2", false);
        product.setParent(root);
    }

    protected void performUIAfterNetExecute() {
        ((PictureExplorerView) (super.view)).refresh(product);
    }
}
