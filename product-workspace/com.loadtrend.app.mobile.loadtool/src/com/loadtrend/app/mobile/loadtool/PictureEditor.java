package com.loadtrend.app.mobile.loadtool;

import java.text.MessageFormat;
import java.util.Iterator;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.forms.widgets.FormText;

import com.loadtrend.app.mobile.loadtool.action.ChangeItemValidStatusAction;
import com.loadtrend.app.mobile.loadtool.action.DeleteItemAction;
import com.loadtrend.app.mobile.loadtool.action.ModifyItemAction;
import com.loadtrend.app.mobile.loadtool.control.MobileImage;
import com.loadtrend.web.mobile.dao.model.Item;

public class PictureEditor extends ResourceEditor {
    
    public static final String EDITOR_ID = "com.loadtrend.app.mobile.loadtool.PictureEditor"; 
    
    private MessageFormat messageFormat = new MessageFormat("编号:{0} {1} \n下载次数:{2}");
    
    public PictureEditor() {
        super.RES_NUM_PERPAGE = 15;
    }
    
    protected Composite buildItemComposite(FormText formText, Iterator it, int page) {
        Composite composite = kit.createComposite(formText);
        RowLayout layout = new RowLayout();
        layout.spacing = 15;
        composite.setLayout(layout);
        
        for (int i = 0; i < RES_NUM_PERPAGE && it.hasNext(); i++) {
            final Item item = (Item) it.next();
	        MobileImage mobileImage = new MobileImage(composite, 128, 128);
	        mobileImage.setLayoutData(new RowData(mobileImage.getMobileImageWidth(), mobileImage.getMobileImageHeight()));
	        if (item.getIsValid().equals("1")) {
	            mobileImage.setBackground(blueColor);
	        } else {
	            mobileImage.setBackground(activeLinkColor);
	        }
	        String title = messageFormat.format(new String[]{item.getId(), item.getName(), item.getPaytimes().toString()});
	        mobileImage.paintImageAsyn(item.getUrl(), true, title, "Click to show id.",
	                                   new MouseAdapter() {
	                                       public void mouseDown(MouseEvent e) {
	                                    	   if (e.button == 1) {
	                                               MessageBox messageBox = new MessageBox(PictureEditor.this.getSite().getShell(), SWT.OK | SWT.ICON_INFORMATION);
	                                               messageBox.setText("图片下载提示");
	                                               messageBox.setMessage(MessageFormat.format("请直接发送短信 {0} 到 57572194 将 {1} 下载至手机", new String[] {item.getId(), item.getName()}));
	                                               messageBox.open();
	                                    	   }
	                                       }
	                                   });
			// Create the context menu and register it with the Workbench.
			MenuManager menuMgr = new MenuManager( "#PopupMenu" );
			Menu menu = menuMgr.createContextMenu( mobileImage.getImageCanvas() );
			mobileImage.getImageCanvas().setMenu( menu );
	        menuMgr.add(new ChangeItemValidStatusAction(this, super.view, item, page));
	        menuMgr.add(new ModifyItemAction(this, super.view, item, page));
	        menuMgr.add(new DeleteItemAction(this, super.view, item, page));
        } 
        return composite;
    }
}
