package com.loadtrend.app.mobile.editors;

import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

import com.loadtrend.app.mobile.MobilePlugin;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.mobileaction.QuickSendSMSToDownloadMediaAction;
import com.loadtrend.web.mobile.dao.model.Item;

public class MusicEditor extends ResourceEditor {

    public static final String EDITOR_ID = "com.loadtrend.app.mobile.editors.MusicEditor"; 
    
    private Image ringToneImage = MobilePlugin.getImageDescriptor("icons/ringtone.gif").createImage();
    
    private Image playImage = MobilePlugin.getImageDescriptor("icons/play.bmp").createImage();
    
    private Browser browser = null;
    
    public MusicEditor() {
        super.RES_NUM_PERPAGE = 18;
    }
    
    public void createPartControl( Composite parent ) {
    	super.createPartControl(parent);
        super.fileDialog.setFilterExtensions(new String[] {"*.mmf;*.wav;*.amr;*.mid;*.mp3;*.imy;*.wma", "*.bmp;*.gif;*.ico;*.jpg;*.png"});
        super.fileDialog.setFilterNames(new String[] {"音乐格式(*.mmf;*.wav;*.amr;*.mid;*.mp3;*.imy;*.wma)", "图片格式(*.bmp;*.gif;*.ico;*.jpg;*.png)"});
    }
    
    protected Composite buildItemComposite(FormText formText, Iterator it) {
        Composite composite = kit.createComposite(formText);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        composite.setLayout(layout);
        
        // Create the mobile music composite.
        MobileMusic mobileMusic = new MobileMusic(composite,
                                                  new Color[] {super.orangeColor, super.deepBgColor, super.activeLinkColor, super.orangeColor},
                                                  new String[] {"编号", "铃声名字", "试听", "歌手/作者", "周下载数"},
                                                  true);
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
        data.minimumHeight = 15;
        mobileMusic.setLayoutData(data);
        for (int i = 0; i < RES_NUM_PERPAGE && it.hasNext(); i++) {
            final Item item = (Item) it.next();
            Color bgColor = super.bgColor;
            mobileMusic = new MobileMusic(composite,
                                          new Color[] {super.blueColor, bgColor, super.activeLinkColor, super.orangeColor},
                                          new String[] {item.getId(),
                                                        item.getName(),
                                                        item.getUrl(),
                                                        item.getAuthor() == null || item.getAuthor().trim().equals("") ? "未知" : item.getAuthor(),
                                                        item.getPaytimes().toString()
                                                        },
                                          false);
            data = new GridData(SWT.FILL, SWT.FILL, true, true);
            data.minimumHeight = 25;
            mobileMusic.setLayoutData(data);
        } 
        
        // Create browser.
        browser = new Browser(composite, SWT.NONE);
        data = new GridData(SWT.FILL, SWT.FILL, true, true);
        browser.setLayoutData(data);
        Menu menu = new Menu(composite.getShell(), SWT.POP_UP);
        browser.setMenu(menu);
        
        return composite;
    }
    
    private class MobileMusic extends Composite {
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
	            Label name = new Label(this, SWT.NONE);
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
	            final ImageHyperlink name = new ImageHyperlink(this, SWT.NONE);
	            data = new FormData();
	            data.left = new FormAttachment(id);
	            data.right = new FormAttachment(55);
                data.top = new FormAttachment(0);
                data.bottom = new FormAttachment(100);
	            name.setLayoutData(data);
	            name.setForeground(colors[2]);
	            name.setBackground(colors[1]);
	            name.setText(strings[1]);
                name.setImage(ringToneImage);
                name.addMouseTrackListener(new MouseTrackListener() {
                    public void mouseEnter(MouseEvent e) {
                        name.setForeground(colors[3]);
                    }
                    public void mouseExit(MouseEvent e) {
                        name.setForeground(colors[2]);
                    }
                    public void mouseHover(MouseEvent e) {
                    }
                });
                name.addHyperlinkListener(new HyperlinkAdapter() {
                    public void linkActivated(HyperlinkEvent e) {
                       new QuickSendSMSToDownloadMediaAction(MusicEditor.super.getSite().getWorkbenchWindow(),
                               "57572194",
                               friendCheckBox.getSelection() ? friendNumber.getText() : null,
                               strings[0],
                               strings[1]).run();
                       MusicEditor.this.playMusic(strings[0], strings[2]);
                    }
                });
            }
            
            if (isTitle) {
	            Label listen = new Label(this, SWT.NONE);
                data = new FormData();
	            data.left = new FormAttachment(55);
	            data.right = new FormAttachment(70);
                data.top = new FormAttachment(0);
                data.bottom = new FormAttachment(100);
	            listen.setLayoutData(data);
	            listen.setForeground(colors[0]);
	            listen.setBackground(colors[1]);
	            listen.setText(strings[2]);
            } else {
	            final ImageHyperlink listen = new ImageHyperlink(this, SWT.NONE);
                data = new FormData();
	            data.left = new FormAttachment(55);
	            data.right = new FormAttachment(70);
                data.top = new FormAttachment(0);
                data.bottom = new FormAttachment(100);
	            listen.setLayoutData(data);
	            listen.setForeground(colors[0]);
	            listen.setBackground(colors[1]);
                listen.setImage(playImage);
                listen.addHyperlinkListener(new HyperlinkAdapter() {
                    public void linkActivated(HyperlinkEvent e) {
                    	MusicEditor.this.playMusic(strings[0], strings[2]);
                    }
                });
            }
            
            final ImageHyperlink author = new ImageHyperlink(this, SWT.NONE);
            data = new FormData();
            data.left = new FormAttachment(70);
            data.right = new FormAttachment(85);
            data.top = new FormAttachment(0);
            data.bottom = new FormAttachment(100);
            author.setLayoutData(data);
            author.setForeground(colors[0]);
            author.setBackground(colors[1]);
            author.setText(strings[3]);
            author.addMouseTrackListener(new MouseTrackListener() {
                public void mouseEnter(MouseEvent e) {
                	author.setForeground(colors[3]);
                }
                public void mouseExit(MouseEvent e) {
                	author.setForeground(colors[0]);
                }
                public void mouseHover(MouseEvent e) {
                }
            });
            author.addHyperlinkListener(new HyperlinkAdapter() {
                public void linkActivated(HyperlinkEvent e) {
                	MusicEditor.super.searchKey(e.getLabel().trim());
                }
            });
            
            Label downloads = new Label(this, SWT.NONE);
            data = new FormData();
            data.left = new FormAttachment(author);
            data.right = new FormAttachment(100);
            data.top = new FormAttachment(0);
            data.bottom = new FormAttachment(100);
            downloads.setLayoutData(data);
            downloads.setForeground(colors[0]);
            downloads.setBackground(colors[1]);
            downloads.setText(strings[4]);
        }
    }
    
    private void playMusic(String id, String url) {
        String playSoundEmbedHtml = Messages.getString(MessagesConstant.MusicEditor_PLAYMUSIC_EMBED_HTML, id, url);
        String playSoundHtml = Messages.getString(MessagesConstant.MusicEditor_PLAYMUSIC_HTML, playSoundEmbedHtml);
        browser.setText(playSoundHtml);
    }

    public void dispose() {
        super.dispose();
        this.ringToneImage.dispose();
        this.playImage.dispose();
    }
    
    
}