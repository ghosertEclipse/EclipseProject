package com.loadtrend.app.mobile.netaction;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import com.loadtrend.app.mobile.editors.ResourceEditor;
import com.loadtrend.app.mobile.views.media.ResourceExplorerView;
import com.loadtrend.web.mobile.dao.model.Product;

public class GetValidItemsByProductAction extends NetAction {
    
    private Product product = null;
    
    private List items = null;

    public GetValidItemsByProductAction(ResourceExplorerView view, Product product) {
        super.view = view;
        super.window = view.getSite().getWorkbenchWindow();
        this.product = product;
    }

    protected void netExecute() throws Exception {
        super.showRealTimeInfo("正在获取图铃详细信息");
        this.items = super.jMobileClientManager.getValidItemsByProduct(this.product);
    }

    protected void performUIAfterNetExecute() {
        ResourceExplorerView resourceExplorerView = (ResourceExplorerView) super.view;
        TreeViewer treeViewer = resourceExplorerView.getTreeViewer();
        IEditorInput resourceEditorInput = resourceExplorerView.getResourceEditorInput();
        String editor_id = resourceExplorerView.getEDITOR_ID();
        
        if (this.items.size() == 0) {
            boolean expandedState = !treeViewer.getExpandedState(product);
            treeViewer.setExpandedState( product, expandedState );
        } else {
            IWorkbenchPage page = window.getActivePage();
            IEditorPart editorPart = page.findEditor(resourceEditorInput);
            if ( editorPart != null ) {
                page.closeEditor(editorPart, false );
            }
            try {
                editorPart = page.openEditor(resourceEditorInput, editor_id);
                ResourceEditor resourceEditor = (ResourceEditor) editorPart;
                resourceEditor.showResourceFromProduct(items);
            }
            catch ( PartInitException e ) {
                MessageDialog.openError(window.getShell(),
                                        "Open resource editor error.",
                                        e.getMessage());
            } 
        }
    }

}
