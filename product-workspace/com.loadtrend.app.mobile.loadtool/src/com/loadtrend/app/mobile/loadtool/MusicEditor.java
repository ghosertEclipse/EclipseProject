package com.loadtrend.app.mobile.loadtool;

import java.text.MessageFormat;
import java.util.Iterator;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

import com.loadtrend.api.win32.ShellExecuteEx;
import com.loadtrend.app.mobile.loadtool.action.ChangeItemValidStatusAction;
import com.loadtrend.app.mobile.loadtool.action.DeleteItemAction;
import com.loadtrend.app.mobile.loadtool.action.ModifyItemAction;
import com.loadtrend.web.mobile.dao.model.Item;

public class MusicEditor extends ResourceEditor {

    public static final String EDITOR_ID = "com.loadtrend.app.mobile.loadtool.MusicEditor"; 
    
    private Image ringToneImage = LoadtoolPlugin.getImageDescriptor("icons/ringtone.gif").createImage();
    
    private Image playImage = LoadtoolPlugin.getImageDescriptor("icons/play.bmp").createImage();
    
    private static final String MEDIA_PLAYER_PATH = "C:\\Program Files\\Windows Media Player\\wmplayer.exe";
    
    public MusicEditor() {
        super.RES_NUM_PERPAGE = 18;
    }
    
    protected Composite buildItemComposite(FormText formText, Iterator it, int page) {
        Composite composite = kit.createComposite(formText);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        composite.setLayout(layout);
        
        MobileMusic mobileMusic = new MobileMusic(composite,
                                                  new Color[] {super.orangeColor, super.deepBgColor, super.activeLinkColor, super.orangeColor},
                                                  new String[] {"编号", "铃声名字", "歌手/作者", "下载次数", "试听"},
                                                  true);
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
        data.minimumHeight = 15;
        mobileMusic.setLayoutData(data);
        for (int i = 0; i < RES_NUM_PERPAGE && it.hasNext(); i++) {
            final Item item = (Item) it.next();
            Color bgColor = item.getIsValid().equals("1") ? super.bgColor : super.deepBgColor;
            mobileMusic = new MobileMusic(composite,
                                          new Color[] {super.blueColor, bgColor, super.activeLinkColor, super.orangeColor},
                                          new String[] {item.getId(),
                                                        item.getName(),
                                                        item.getAuthor() == null || item.getAuthor().trim().equals("") ? "未知" : item.getAuthor(),
                                                        item.getPaytimes().toString(),
                                                        item.getUrl()},
                                          false);
            data = new GridData(SWT.FILL, SWT.FILL, true, true);
            data.minimumHeight = 25;
            mobileMusic.setLayoutData(data);
            
			// Create the context menu and register it with the Workbench.
			MenuManager menuMgr = new MenuManager( "#PopupMenu" );
			Menu menu = menuMgr.createContextMenu( mobileMusic.getLinkName() );
			mobileMusic.getLinkName().setMenu( menu );
	        menuMgr.add(new ChangeItemValidStatusAction(this, super.view, item, page));
	        menuMgr.add(new ModifyItemAction(this, super.view, item, page));
	        menuMgr.add(new DeleteItemAction(this, super.view, item, page));
        } 
        return composite;
    }
    
    private class MobileMusic extends Composite {
    	private ImageHyperlink linkName = null;
    	private Label name = null;
        public MobileMusic (Composite parent, final Color[] colors, final String[] strings, boolean isTitle) {
            super (parent, SWT.NONE);
            this.setForeground(colors[0]);
            this.setBackground(colors[1]);
            
            FormLayout layout = new FormLayout();
            this.setLayout(layout);
            
            Label id = new Label(this, SWT.NONE);
            FormData data = new FormData();
            data.left = new FormAttachment(0);
            data.right = new FormAttachment(15);
            data.top = new FormAttachment(0);
            data.bottom = new FormAttachment(100);
            id.setLayoutData(data);
            id.setForeground(colors[0]);
            id.setBackground(colors[1]);
            id.setText(strings[0]);
            
            if (isTitle) {
	            name = new Label(this, SWT.NONE);
	            data = new FormData();
	            data.left = new FormAttachment(id);
	            data.right = new FormAttachment(55);
                data.top = new FormAttachment(0);
                data.bottom = new FormAttachment(100);
	            name.setLayoutData(data);
	            name.setForeground(colors[0]);
	            name.setBackground(colors[1]);
	            name.setText(strings[1]);
            } else {
            	linkName = new ImageHyperlink(this, SWT.NONE);
	            data = new FormData();
	            data.left = new FormAttachment(id);
	            data.right = new FormAttachment(55);
                data.top = new FormAttachment(0);
                data.bottom = new FormAttachment(100);
	            linkName.setLayoutData(data);
	            linkName.setForeground(colors[2]);
	            linkName.setBackground(colors[1]);
	            linkName.setText(strings[1]);
	            linkName.setImage(ringToneImage);
	            linkName.addMouseTrackListener(new MouseTrackListener() {
                    public void mouseEnter(MouseEvent e) {
                    	linkName.setForeground(colors[3]);
                    }
                    public void mouseExit(MouseEvent e) {
                    	linkName.setForeground(colors[2]);
                    }
                    public void mouseHover(MouseEvent e) {
                    }
                });
	            linkName.addHyperlinkListener(new HyperlinkAdapter() {
                    public void linkActivated(HyperlinkEvent e) {
                       MessageBox messageBox = new MessageBox(MusicEditor.this.getSite().getShell(), SWT.OK | SWT.ICON_INFORMATION);
                       messageBox.setText("铃声下载提示");
                       messageBox.setMessage(MessageFormat.format("请直接发送短信 {0} 到 57572194 将 {1} 下载至手机", new String[] {strings[0], strings[1]}));
                       messageBox.open();
                    }
                });
            }
            
            Label author = new Label(this, SWT.NONE);
            data = new FormData();
            data.left = new FormAttachment(isTitle ? (Control) name : linkName);
            data.right = new FormAttachment(70);
            data.top = new FormAttachment(0);
            data.bottom = new FormAttachment(100);
            author.setLayoutData(data);
            author.setForeground(colors[0]);
            author.setBackground(colors[1]);
            author.setText(strings[2]);
            
            Label downloads = new Label(this, SWT.NONE);
            data = new FormData();
            data.left = new FormAttachment(author);
            data.right = new FormAttachment(85);
            data.top = new FormAttachment(0);
            data.bottom = new FormAttachment(100);
            downloads.setLayoutData(data);
            downloads.setForeground(colors[0]);
            downloads.setBackground(colors[1]);
            downloads.setText(strings[3]);
            
            if (isTitle) {
	            Label listen = new Label(this, SWT.NONE);
                data = new FormData();
	            data.left = new FormAttachment(downloads);
	            data.right = new FormAttachment(100);
                data.top = new FormAttachment(0);
                data.bottom = new FormAttachment(100);
	            listen.setLayoutData(data);
	            listen.setForeground(colors[0]);
	            listen.setBackground(colors[1]);
	            listen.setText(strings[4]);
            } else {
	            final ImageHyperlink listen = new ImageHyperlink(this, SWT.NONE);
                data = new FormData();
	            data.left = new FormAttachment(downloads);
	            data.right = new FormAttachment(100);
                data.top = new FormAttachment(0);
                data.bottom = new FormAttachment(100);
	            listen.setLayoutData(data);
	            listen.setForeground(colors[0]);
	            listen.setBackground(colors[1]);
                listen.setImage(playImage);
                listen.addHyperlinkListener(new HyperlinkAdapter() {
                    public void linkActivated(HyperlinkEvent e) {
                        StringBuffer resultCodeErrorInfo = new StringBuffer();
                        ShellExecuteEx.execute(MEDIA_PLAYER_PATH , strings[4], ShellExecuteEx.NORMAL, resultCodeErrorInfo);
                        System.out.println(resultCodeErrorInfo);
                    }
                });
            }
        }
        
		public ImageHyperlink getLinkName() {
			return linkName;
		}
        
    }

    public void dispose() {
        super.dispose();
        this.ringToneImage.dispose();
        this.playImage.dispose();
    }
    
    
}