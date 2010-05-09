package com.loadtrend.app.mobile.netaction;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import com.loadtrend.app.mobile.editors.PictureEditor;
import com.loadtrend.app.mobile.editors.ResourceEditor;

public class GetValidItemsBySearchAction extends NetAction {

    private String key = null;
    
    private List items = null;

    public GetValidItemsBySearchAction(ResourceEditor editor, String key) {
        super.part = editor;
        super.window = super.part.getSite().getWorkbenchWindow();
        this.key = key;
    }

    protected void netExecute() throws Exception {
        super.showRealTimeInfo("��������ͼ����ϸ��Ϣ");
        this.items = super.jMobileClientManager.getValidItemsBySearch(this.key, super.part instanceof PictureEditor ? "0" : "1");
    }

    protected void performUIAfterNetExecute() {
    	ResourceEditor resourceEditor = (ResourceEditor) super.part;
        
        if (this.items.size() == 0) {
        	MessageBox box = new MessageBox(super.window.getShell(), SWT.OK);
        	box.setMessage("û���ҵ����ͼ��");
        	box.open();
        } else {
            resourceEditor.showResourceFromSearch(items);
        }
    }

}
