package com.loadtrend.app.mobile.editors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.EditorPart;

import com.loadtrend.app.mobile.MobilePlugin;
import com.loadtrend.app.mobile.netaction.GetValidItemsBySearchAction;
import com.loadtrend.app.mobile.netaction.UploadLocalFileAction;

public abstract class ResourceEditor extends EditorPart {
    
    public static final String EDITOR_ID = "com.loadtrend.app.mobile.editors"; 
    
    protected int RES_NUM_PERPAGE = 0;
    
    protected Color blueColor = null;
    
    protected Color activeLinkColor = null;
    
    protected Color bgColor = null;
    
    protected Color deepBgColor = null;
    
    protected Color orangeColor = null;
    
    protected Color whiteColor = null;
    
    protected Color blackColor = null;
    
    protected Color greenColor = null;
    
    protected Form form = null;
    
    protected FormText formText = null;
    
    private FormText linkFormText = null;
    
    protected FormToolkit kit = null;
    
    protected Composite linkComposite = null;
    
    protected List items = null;
    
    protected ImageHyperlink prePage = null;
    
    protected ImageHyperlink nextPage = null;
    
    protected ImageHyperlink[] linkPages = null;
    
    protected int couplePages = 15;
    
    protected ImageHyperlink prePages = null;
    
    protected ImageHyperlink nextPages = null;
    
    protected IWorkbenchWindow window = null;
    
    protected FileDialog fileDialog = null;
    
    private Image folderImage = MobilePlugin.getImageDescriptor("icons/fldr_obj.gif").createImage();
    
    private Image searchImage = MobilePlugin.getImageDescriptor("icons/sms_searchsms.gif").createImage();
    
    private Image moneyImage = MobilePlugin.getImageDescriptor("icons/money.gif").createImage();
    
    protected Button friendCheckBox = null;
    
    protected Text friendNumber = null;
    
    protected Text searchTextbox = null;
    
    public void doSave( IProgressMonitor monitor )
    {
    }

    public void doSaveAs()
    {
    }

    public void init( IEditorSite site, IEditorInput input ) throws PartInitException
    {
        setSite( site );
        setInput( input );
//      设置Editor标题栏的显示名称。不要，则名称用plugin.xml中的name属性
        setPartName(input.getName());
//      设置Editor标题栏的图标。不要，则会自动使用一个默认的图标
//      setTitleImage(input.getImageDescriptor().createImage());
    }

    public boolean isDirty()
    {
        return false;
    }

    public boolean isSaveAsAllowed()
    {
        return false;
    }

    public void createPartControl( Composite parent )
    {
        Display display = super.getSite().getShell().getDisplay();
        this.kit = new FormToolkit(display);
        
        this.form = kit.createForm(parent);
        TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 1;
        layout.makeColumnsEqualWidth = false;
        layout.leftMargin = 15;
        layout.rightMargin = 15;
        form.getBody().setLayout(layout);
        
        // Build FormColor.
        FormColors formColors = new FormColors(display);
        this.blueColor = formColors.getColor(FormColors.TITLE);
        this.activeLinkColor = formColors.createColor("activeLinkColor", 0xcc, 0x00, 0x00);
        this.bgColor = formColors.createColor("bgColor", 0xf7, 0xfb, 0xf7);
        this.deepBgColor = formColors.createColor("deepBgColor", 0xff, 0xf3, 0xde);
        this.orangeColor = formColors.createColor("orangeColor", 0xff, 0x99, 0x00);
        this.whiteColor = formColors.createColor("whiteColor", 0xff, 0xff, 0xff);
        this.blackColor = formColors.createColor("blackColor", 0x00, 0x00, 0x00);
        this.greenColor = formColors.createColor("greenColor", 0x00, 0x80, 0x00);
        
        this.window = this.getSite().getWorkbenchWindow();
        
        fileDialog = new FileDialog(this.getSite().getShell(), SWT.OPEN);
    }
    
    public void showResourceFromProduct(List items) {
        
    	this.items = items;
        
        // build send to friend and upload composite.
        this.buildFriendUploadComposite();
        
        // build link composite.
        this.buildLinkComposite(1);
        
        // The first time to show image composite.
        this.buildFormTextComposite(1);
    }
    
   public void showResourceFromSearch(List items) {
        
    	this.items = items;
        
        // build send to friend and upload composite.
        // this.buildFriendUploadComposite();
        
        // build link composite.
        this.buildLinkComposite(1);
        
        // The first time to show image composite.
        this.buildFormTextComposite(1);
    }
    
    private void showError(final String title, final String message) {
        this.window.getShell().getDisplay().syncExec(new Runnable() {
            public void run() {
                MessageBox messageBox = new MessageBox(window.getShell(), SWT.ICON_ERROR | SWT.OK);
                messageBox.setText(title);
                messageBox.setMessage(message);
                messageBox.open();
            }
        });
    }
    
    private void showWarning(final String title, final String message) {
        this.window.getShell().getDisplay().syncExec(new Runnable() {
            public void run() {
                MessageBox messageBox = new MessageBox(window.getShell(), SWT.ICON_WARNING | SWT.OK);
                messageBox.setText(title);
                messageBox.setMessage(message);
                messageBox.open();
            }
        });
    }
    
    private byte[] checkFileSizeAndGet(String filename, int size) {
        if (size > 1024 * 500) {
            this.showWarning("上传文件检查警告", "上传图铃不能大于500KB");
            return null;
        }
        FileInputStream stream = null;
        try {
            byte[] bytes = new byte[size];
            stream = new FileInputStream(filename);
            stream.read(bytes, 0, size);
            stream.close();
            return bytes;
        } catch (FileNotFoundException fe) {
            this.showError("file not found.", fe.getMessage());
        } catch (IOException ioe) {
            this.showError("file read error.", ioe.getMessage());
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    
    private void buildFriendUploadComposite() {
        Composite composite = kit.createComposite(this.form.getBody());
        composite.setLayoutData(new TableWrapData());
        TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 1;
        layout.leftMargin = 0;
        layout.topMargin = 0;
        layout.bottomMargin = 0;
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        composite.setLayout(layout);
        // build friend composite.
        Composite friendComposite = kit.createComposite(composite);
        TableWrapData data = new TableWrapData();
        friendComposite.setLayoutData(data);
        layout = new TableWrapLayout();
        layout.numColumns = 4;
        layout.bottomMargin = 0;
        friendComposite.setLayout(layout);
        Label prompt = kit.createLabel(friendComposite, "点击图铃或发短信:编号至57572194下载,1元/条");
        prompt.setForeground(this.greenColor);
        this.friendCheckBox = kit.createButton(friendComposite, "赠送图铃给好友手机号码:", SWT.CHECK);
        friendCheckBox.setForeground(this.greenColor);
        this.friendNumber = kit.createText(friendComposite, "");
        Label label = kit.createLabel(friendComposite, "(点击或浏览本地图铃完成赠送)");
        label.setForeground(this.greenColor);
        friendCheckBox.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
            }
            public void widgetSelected(SelectionEvent e) {
                if (friendCheckBox.getSelection()) {
                    friendNumber.setFocus();
                }
            }
        });
        this.friendNumber.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                friendCheckBox.setSelection(friendNumber.getText().length() > 0);
            }
        });
        kit.paintBordersFor(friendComposite);
        
        // build upload composite
        Composite uploadComposite = kit.createComposite(composite);
        data = new TableWrapData();
        uploadComposite.setLayoutData(data);
        layout = new TableWrapLayout();
        layout.numColumns = 4;
        layout.bottomMargin = 0;
        uploadComposite.setLayout(layout);
        
        
        // search text box and hyperlink.
        this.searchTextbox = kit.createText(uploadComposite, "");
        this.searchTextbox.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				// enter key code pressed.
				if (e.keyCode == 13) {
    	            String key = searchTextbox.getText().trim();
					ResourceEditor.this.searchKey(key);
				}
			}
			public void keyReleased(KeyEvent e) {
			}
        });
        final ImageHyperlink searchHyperlink = kit.createImageHyperlink(uploadComposite, SWT.NORMAL);
        searchHyperlink.setImage(searchImage);
        searchHyperlink.setText("点击图铃大搜索");
        searchHyperlink.setUnderlined(false);
        searchHyperlink.setForeground(this.greenColor);
        searchHyperlink.setBackground(this.whiteColor);
        searchHyperlink.addHyperlinkListener(new HyperlinkAdapter() {
            public void linkActivated(HyperlinkEvent e) {
    	        String key = searchTextbox.getText().trim();
				ResourceEditor.this.searchKey(key);
            }
        });
        searchHyperlink.addMouseTrackListener(new MouseTrackListener() {
            public void mouseEnter(MouseEvent e) {
            	searchHyperlink.setForeground(ResourceEditor.this.orangeColor);
            }
            public void mouseExit(MouseEvent e) {
            	searchHyperlink.setForeground(ResourceEditor.this.greenColor);
            }
            public void mouseHover(MouseEvent e) {
            }
        });
        
        // upload hyperlink
        final ImageHyperlink hyperlink = kit.createImageHyperlink(uploadComposite, SWT.NORMAL);
        hyperlink.setImage(folderImage);
        hyperlink.setText("将本地图铃下载至手机或赠送给好友");
        hyperlink.setUnderlined(false);
        hyperlink.setForeground(this.greenColor);
        hyperlink.setBackground(this.whiteColor);
        hyperlink.addHyperlinkListener(new HyperlinkAdapter() {
            public void linkActivated(HyperlinkEvent e) {
                // some small error if no this thread.
                new Thread() {
                    public void run() {
                        String filename = fileDialog.open();
                        if (filename != null) {
                            final File file = new File(filename);
                            int size = (int) file.length();
                            final byte[] bytes = ResourceEditor.this.checkFileSizeAndGet(filename, size);
                            if (bytes != null) {
                                ResourceEditor.this.window.getShell().getDisplay().syncExec(new Runnable() {
                                    public void run() {
                                        new UploadLocalFileAction(ResourceEditor.this.window, file.getName(), bytes, friendCheckBox.getSelection(), friendNumber.getText()).run(null);
                                    }
                                });
                            }
                        }
                    }
                }.start();
            }
        });
        hyperlink.addMouseTrackListener(new MouseTrackListener() {
            public void mouseEnter(MouseEvent e) {
                hyperlink.setForeground(ResourceEditor.this.orangeColor);
            }
            public void mouseExit(MouseEvent e) {
                hyperlink.setForeground(ResourceEditor.this.greenColor);
            }
            public void mouseHover(MouseEvent e) {
            }
        });
        // build browser
        final ImageHyperlink bowserHyperlink = kit.createImageHyperlink(uploadComposite, SWT.NORMAL);
        bowserHyperlink.setImage(moneyImage);
        bowserHyperlink.setText("免费上传我的图铃资源, 赢取下载现金奖励");
        bowserHyperlink.setUnderlined(false);
        bowserHyperlink.setForeground(this.greenColor);
        bowserHyperlink.setBackground(this.whiteColor);
        final IEditorInput browserEditorInput = new AppEditorInput("browser", null, "browser");
        bowserHyperlink.addHyperlinkListener(new HyperlinkAdapter() {
            public void linkActivated(HyperlinkEvent hyperlinkEvent) {
                IWorkbenchPage page = window.getActivePage();
                try {
                    page.openEditor(browserEditorInput, BrowserEditor.EDITOR_ID);
                }
                catch ( PartInitException e ) {
                    ResourceEditor.this.showError("Open browser editor error", e.getMessage());
                } 
            }
        });
        bowserHyperlink.addMouseTrackListener(new MouseTrackListener() {
            public void mouseEnter(MouseEvent e) {
            	bowserHyperlink.setForeground(ResourceEditor.this.orangeColor);
            }
            public void mouseExit(MouseEvent e) {
            	bowserHyperlink.setForeground(ResourceEditor.this.greenColor);
            }
            public void mouseHover(MouseEvent e) {
            }
        });
        
        kit.paintBordersFor(uploadComposite);
    }
    
    protected void searchKey(String key) {
    	if (key.equals("")) {
    		MessageBox box = new MessageBox(ResourceEditor.this.getSite().getShell(), SWT.OK);
    		box.setMessage("请输入关键字");
    		box.open();
    		searchTextbox.setFocus();
    	} else {
    		new GetValidItemsBySearchAction(ResourceEditor.this, key).run();
    	}
    }
    
    private void buildLinkComposite(int page) {
        final int pages = (this.items.size() - 1) / RES_NUM_PERPAGE + 1;
        
        if (this.linkFormText != null && !this.linkFormText.isDisposed()) this.linkFormText.dispose();
        // Build FormText.
        this.linkFormText = kit.createFormText(form.getBody(), false);
        this.linkFormText.setLayoutData(new TableWrapData());
        this.linkFormText.setText("<form><p><control href=\"composite\" fill=\"true\"/></p></form>", true, true);
        
        // Build linkComposite.
        this.linkComposite = kit.createComposite(this.linkFormText);
        
        RowLayout layout = new RowLayout();
        layout.marginBottom = 0;
        layout.spacing = 5;
        this.linkComposite.setLayout(layout);
        
        // First page.
        int groupCouplePages = (page - 1) / couplePages;
        
        this.buildLinks("[首页]", 1, null);
        
        this.prePage = this.buildLinks("[上一页]", page == 1 ? 1 : page - 1, null);
        
        int initialPrePages = groupCouplePages > 0 ? (groupCouplePages - 1) * couplePages + 1 : 1;
        
        this.prePages = this.buildLinks(MessageFormat.format("[上{0}页]", new String[] {String.valueOf(couplePages)}),
        		                        initialPrePages, null);
        
        // Show the number.
        this.linkPages = new ImageHyperlink[couplePages];
        for (int i = 0, j = groupCouplePages * couplePages; i < couplePages && j < pages; i++, j++) {
        	if (j + 1 == page) {
                this.linkPages[i] = this.buildLinks(String.valueOf(j + 1), j + 1, null, true);
        	} else {
                this.linkPages[i] = this.buildLinks(String.valueOf(j + 1), j + 1, null);
        	}
        }
        
        int initialNextPages = groupCouplePages == (pages - 1) / couplePages ?
        		               (groupCouplePages * couplePages) + 1 : (groupCouplePages + 1) * couplePages + 1;
        
        this.nextPages = this.buildLinks(MessageFormat.format("[下{0}页]", new String[] {String.valueOf(couplePages)}),
        		                         initialNextPages, null);
        
        this.nextPage = this.buildLinks("[下一页]", page == pages ? pages : page + 1, null);
        
        this.buildLinks("[未页]", pages, null);
        
        this.buildLinks(MessageFormat.format("[{0}页,{1}条]",
        		                             new String[] {String.valueOf(pages), String.valueOf(this.items.size())}),
        		        1, null);
        
        this.linkFormText.setControl("composite", this.linkComposite);
    }
    
    private ImageHyperlink buildLinks(String text, int href, Image image) {
    	return this.buildLinks(text, href, image, false);
    }
    
    private ImageHyperlink buildLinks(String text, final int href, Image image, boolean isCurrentLink) {
        final ImageHyperlink hyperlink = new ImageHyperlink(this.linkComposite, SWT.NONE);
        hyperlink.setLayoutData(new RowData(5, 5));
        hyperlink.setText(text);
        hyperlink.setHref(String.valueOf(href));
        hyperlink.setImage(image);
        hyperlink.setUnderlined(false);
        hyperlink.addHyperlinkListener(new HyperlinkAdapter() {
            public void linkActivated(HyperlinkEvent e) {
            	ResourceEditor.this.buildLinkComposite(href);
                ResourceEditor.this.buildFormTextComposite(href);
            }
        });
        hyperlink.setBackground(this.whiteColor);
        if (!isCurrentLink) {
	        hyperlink.setForeground(this.activeLinkColor);
	        hyperlink.addMouseTrackListener(new MouseTrackListener() {
	            public void mouseEnter(MouseEvent e) {
	                hyperlink.setForeground(ResourceEditor.this.orangeColor);
	            }
	            public void mouseExit(MouseEvent e) {
	                hyperlink.setForeground(ResourceEditor.this.activeLinkColor);
	            }
	            public void mouseHover(MouseEvent e) {
	            }
	        });
        } else {
	        hyperlink.setForeground(this.orangeColor);
	        hyperlink.addMouseTrackListener(new MouseTrackListener() {
	            public void mouseEnter(MouseEvent e) {
	                hyperlink.setForeground(ResourceEditor.this.orangeColor);
	            }
	            public void mouseExit(MouseEvent e) {
	                hyperlink.setForeground(ResourceEditor.this.orangeColor);
	            }
	            public void mouseHover(MouseEvent e) {
	            }
	        });
        }
        return hyperlink;
    }
    
    // Build MobileImages from Product.
    private void buildFormTextComposite(int page) {
        if (this.formText != null && !this.formText.isDisposed()) this.formText.dispose();
        // Build FormText.
        this.formText = kit.createFormText(form.getBody(), false);
        this.formText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.FILL_GRAB));
        formText.setText("<form><p><control href=\"composite\" fill=\"true\"/></p></form>", true, true);
        
        int beginItem = (page - 1) * RES_NUM_PERPAGE + 1;
        
        Iterator it = this.items.iterator();
        for (int i = 1; i < beginItem; i++) {
            it.next();
        }
        
        Composite composite = this.buildItemComposite(this.formText, it);
        
        this.formText.setControl("composite", composite);
        this.form.getBody().layout(true);
    }
    
    protected abstract Composite buildItemComposite(FormText formText, Iterator it);
    
    public void dispose() {
        super.dispose();
        this.blueColor.dispose();
        this.activeLinkColor.dispose();
        this.bgColor.dispose();
        this.deepBgColor.dispose();
        this.orangeColor.dispose();
        this.whiteColor.dispose();
        this.blackColor.dispose();
        this.greenColor.dispose();
        this.folderImage.dispose();
        this.searchImage.dispose();
        this.moneyImage.dispose();
    }

    public void setFocus()
    {
    }
}
