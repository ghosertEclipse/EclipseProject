package com.loadtrend.app.mobile.loadtool.action;

import java.io.IOException;
import java.text.MessageFormat;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import com.loadtrend.app.mobile.loadtool.ResourceEditor;
import com.loadtrend.app.mobile.loadtool.ResourceExplorerView;
import com.loadtrend.web.mobile.dao.model.Item;
import com.loadtrend.web.mobile.dao.model.Product;

public class UploadLocalFileAction extends NetAction {
    
	private ResourceEditor editor = null;
	
	private ResourceExplorerView view = null;
	
    private String filename = null;
    
    private byte[] bytes  = null;
    
    public UploadLocalFileAction(ResourceEditor editor, ResourceExplorerView view, String filename, byte[] bytes) {
    	this.editor = editor;
    	this.view = view;
        super.window = editor.getSite().getWorkbenchWindow();
        this.filename = filename;
        this.bytes = bytes;
    }

    protected void netExecute() {
        try {
            Item item = super.jMobileClientManager.uploadLocalFile(filename, bytes);
            Product product = super.jMobileManager.getProduct(item.getProduct().getId(), true);
            this.view.refreshTreeViewerByRootProduct();
            editor.showResourceFromProduct(view, product, 1);
            MessageBox messageBox = new MessageBox(super.window.getShell(), SWT.ICON_INFORMATION | SWT.OK);
            messageBox.setText("上传成功");
            messageBox.setMessage(MessageFormat.format("成功上传{0}, 分配编号为{1}, 请直接发送短信 {2} 到 57572194 下载至手机。",new String[] {filename, item.getId(), item.getId()}));
            messageBox.open();
        } catch (IOException ioe) {
	        MessageDialog.openError(window.getShell(), "upload file to the remote server failed.", ioe.getMessage());
        }
    }

}
