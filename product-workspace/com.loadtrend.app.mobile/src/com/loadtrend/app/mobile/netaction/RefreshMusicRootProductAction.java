package com.loadtrend.app.mobile.netaction;

import com.loadtrend.app.mobile.views.media.MusicExplorerView;
import com.loadtrend.web.mobile.dao.model.Product;

public class RefreshMusicRootProductAction extends NetAction {

    private Product product = null;

    public RefreshMusicRootProductAction(MusicExplorerView view) {
        super.view = view;
        super.window = view.getSite().getWorkbenchWindow();
    }

    protected void netExecute() throws Exception {
        super.showRealTimeInfo("正在获取炫铃分类信息");
        Product root = new Product();
        product = super.jMobileClientManager.getProduct("3", false);
        product.setParent(root);
    }

    protected void performUIAfterNetExecute() {
        ((MusicExplorerView) (super.view)).refresh(product);
    }

}
