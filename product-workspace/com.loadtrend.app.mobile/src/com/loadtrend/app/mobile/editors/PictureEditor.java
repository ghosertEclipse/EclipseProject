package com.loadtrend.app.mobile.editors;

import java.text.MessageFormat;
import java.util.Iterator;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormText;

import com.loadtrend.app.mobile.control.MobileImage;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.mobileaction.QuickSendSMSToDownloadMediaAction;
import com.loadtrend.web.mobile.dao.model.Item;

public class PictureEditor extends ResourceEditor {
    
    public static final String EDITOR_ID = "com.loadtrend.app.mobile.editors.PictureEditor"; 
    
    private MessageFormat messageFormat = new MessageFormat("编号:{0} {1} \n周下载数:{2}");
    
    public PictureEditor() {
        super.RES_NUM_PERPAGE = 15;
    }
    
    public void createPartControl( Composite parent ) {
    	super.createPartControl(parent);
        super.fileDialog.setFilterExtensions(new String[] {"*.bmp;*.gif;*.ico;*.jpg;*.png", "*.mmf;*.wav;*.amr;*.mid;*.mp3;*.imy;*.wma" });
        super.fileDialog.setFilterNames(new String[] {"图片格式(*.bmp;*.gif;*.ico;*.jpg;*.png)", "音乐格式(*.mmf;*.wav;*.amr;*.mid;*.mp3;*.imy;*.wma)"});
    }
    
    protected Composite buildItemComposite(FormText formText, Iterator it) {
        Composite composite = kit.createComposite(formText);
        RowLayout layout = new RowLayout();
        layout.spacing = 10;
        composite.setLayout(layout);
        
        for (int i = 0; i < RES_NUM_PERPAGE && it.hasNext(); i++) {
            final Item item = (Item) it.next();
	        MobileImage mobileImage = new MobileImage(composite, 128, 128);
	        mobileImage.setLayoutData(new RowData(mobileImage.getMobileImageWidth(), mobileImage.getMobileImageHeight()));
	        mobileImage.setBackground(blueColor);
	        String title = messageFormat.format(new String[]{item.getId(), item.getName(), item.getPaytimes().toString()});
	        mobileImage.paintImageAsyn(item.getUrl(), true, title, Messages.getString(MessagesConstant.PictureEditor_CLICKPROMPT),
                    new MouseAdapter() {
                        public void mouseDown(MouseEvent e) {
                            new QuickSendSMSToDownloadMediaAction(PictureEditor.super.getSite().getWorkbenchWindow(),
                                                            "57572194",
                                                            friendCheckBox.getSelection() ? friendNumber.getText() : null,
                                                            item.getId(),
                                                            item.getName()).run();
                        }
                    });
        } 
        return composite;
    }
}
