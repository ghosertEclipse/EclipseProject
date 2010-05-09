package com.loadtrend.app.mobile.loadtool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Iterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.HyperlinkSettings;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.EditorPart;

import com.loadtrend.app.mobile.loadtool.action.UploadLocalFileAction;
import com.loadtrend.web.mobile.dao.model.Product;

public abstract class ResourceEditor extends EditorPart {
    
    public static final String EDITOR_ID = "com.loadtrend.app.mobile.loadtool.ResourceEditor"; 
    
    protected int RES_NUM_PERPAGE = 0;
    
    protected Color blueColor = null;
    
    protected Color activeLinkColor = null;
    
    protected Color bgColor = null;
    
    protected Color deepBgColor = null;
    
    protected Color orangeColor = null;
    
    protected Color whiteColor = null;
    
    protected Color blackColor = null;
    
    protected Form form = null;
    
    protected FormText formText = null;
    
    protected FormText uploadFormText = null;
    
    private FormText linkFormText = null;
    
    protected FormToolkit kit = null;
    
    protected Composite linkComposite = null;
    
    protected Product product = null;
    
    protected ImageHyperlink prePage = null;
    
    protected ImageHyperlink nextPage = null;
    
    protected ImageHyperlink[] linkPages = null;
    
    protected int couplePages = 15;
    
    protected ImageHyperlink prePages = null;
    
    protected ImageHyperlink nextPages = null;
    
    protected IWorkbenchWindow window = null;
    
    protected FileDialog fileDialog = null;
    
    protected ResourceExplorerView view = null;
    
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
//      ����Editor����������ʾ���ơ���Ҫ����������plugin.xml�е�name����
        setPartName(input.getName());
//      ����Editor��������ͼ�ꡣ��Ҫ������Զ�ʹ��һ��Ĭ�ϵ�ͼ��
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
        
        this.window = this.getSite().getWorkbenchWindow();
        
        fileDialog = new FileDialog(this.getSite().getShell(), SWT.OPEN);
        fileDialog.setFilterExtensions(new String[] {"*.bmp;*.gif;*.ico;*.jpg;*.png", "*.mmf;*.wav;*.amr;*.mid;*.mp3;*.imy;*.wma" });
        fileDialog.setFilterNames(new String[] {"ͼƬ��ʽ(*.bmp;*.gif;*.ico;*.jpg;*.png)", "���ָ�ʽ(*.mmf;*.wav;*.amr;*.mid;*.mp3;*.imy;*.wma)"});
    }
    
    public void showResourceFromProduct(ResourceExplorerView view, Product product, int page) {
        
        this.product = product;
        
        this.view = view;
        
        // build upload form text.
        this.buildUploadFormText();
        
        // build link composite.
        this.buildLinkComposite(page);
        
        // The first time to show image composite.
        this.buildFormTextComposite(page);
    }
    
    private void buildUploadFormText() {
        // build upload file and send mo area.
        if (this.uploadFormText != null && !this.uploadFormText.isDisposed()) this.uploadFormText.dispose();
        uploadFormText = kit.createFormText(this.form.getBody(), true);
        uploadFormText.setLayoutData(new TableWrapData());

        HyperlinkSettings hyperlinkSettings = new HyperlinkSettings(super.getSite().getShell().getDisplay());
        hyperlinkSettings.setForeground(this.activeLinkColor);
        hyperlinkSettings.setActiveForeground(this.activeLinkColor);
        hyperlinkSettings.setHyperlinkUnderlineMode(HyperlinkSettings.UNDERLINE_NEVER);
        uploadFormText.setHyperlinkSettings(hyperlinkSettings);
        uploadFormText.setText("<form><p><span color=\"system\" font=\"system\">ͼ������˵��:</span><br/>" +
                "<span color=\"system\" font=\"system\">1. ȷ���ֻ��Ѿ��ͱ����������,��ֱ�ӵ��ͼ��,���������Է��Ͷ��� ͼ���� �� 57572194 ��������,</span><span color=\"activeColor\">��:���� 12 �� 57572194��</span><br/>" + 
                "<span color=\"system\" font=\"system\">2. �����Ҫ</span><span color=\"activeColor\">����ͼ�������</span><span color=\"system\" font=\"system\">, �����ֻ����� ���[�ո�]�����ֻ��� �� 57572194 </span><span color=\"activeColor\">��:���� 12 1397862823 �� 57572194��</span><br/>" + 
                "<span color=\"system\" font=\"system\">3. ������ͼ���������ֻ�</span><a href=\"browse\">������˴����������</a>��<span color=\"system\" font=\"system\">����ͼ��ɹ�������ȡ1Ԫ, ���ɹ����շѡ�</span><br/>" +
                "<span color=\"system\" font=\"system\">4. ����ϴ��ҵ�ͼ����Դ, Ӯȡ�����ֽ���, ����Խ��, ����Խ��, �ϲ��ⶥ��</span><a href=\"upload\">������˴����鿴���鲢�ϴ�</a></p></form>", true, true);
        uploadFormText.setColor("system", blueColor);
        uploadFormText.setColor("activeColor", activeLinkColor);
        
        final IEditorInput browserEditorInput = new AppEditorInput("browser", null, "browser");
        uploadFormText.addHyperlinkListener(new HyperlinkAdapter() {
            public void linkActivated(HyperlinkEvent event) {
                if (event.getHref().equals("upload")) {
                    IWorkbenchPage page = window.getActivePage();
                    try {
                        page.openEditor(browserEditorInput, BrowserEditor.EDITOR_ID);
                    }
                    catch ( PartInitException e ) {
                        ResourceEditor.this.showError("Open browser editor error", e.getMessage());
                    } 
                }
                if (event.getHref().equals("browse")) {
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
                                            new UploadLocalFileAction(ResourceEditor.this, view, file.getName(), bytes).run(null);
                                        }
                                    });
                                }
                            }
                        }
                    }.start();
                }
            }
        });
        
        this.form.getBody().layout(true);
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
            this.showWarning("�ϴ��ļ���龯��", "�ϴ�ͼ�岻�ܴ���500KB");
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
    
    private void buildLinkComposite(int page) {
        final int pages = (product.getItems().size() - 1) / RES_NUM_PERPAGE + 1;
        
        if (this.linkFormText != null && !this.linkFormText.isDisposed()) this.linkFormText.dispose();
        // Build FormText.
        this.linkFormText = kit.createFormText(form.getBody(), false);
        this.linkFormText.setLayoutData(new TableWrapData());
        this.linkFormText.setText("<form><p><control href=\"composite\" fill=\"true\"/></p></form>", true, true);
        
        // Build linkComposite.
        this.linkComposite = kit.createComposite(this.linkFormText);
        
        RowLayout layout = new RowLayout();
        layout.marginTop = 10;
        layout.spacing = 5;
        this.linkComposite.setLayout(layout);
        
        // First page.
        int groupCouplePages = (page - 1) / couplePages;
        
        this.buildLinks("[��ҳ]", 1, null);
        
        this.prePage = this.buildLinks("[��һҳ]", page == 1 ? 1 : page - 1, null);
        
        int initialPrePages = groupCouplePages > 0 ? (groupCouplePages - 1) * couplePages + 1 : 1;
        
        this.prePages = this.buildLinks(MessageFormat.format("[��{0}ҳ]", new String[] {String.valueOf(couplePages)}),
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
        
        this.nextPages = this.buildLinks(MessageFormat.format("[��{0}ҳ]", new String[] {String.valueOf(couplePages)}),
     		                             initialNextPages, null);
        
        this.nextPage = this.buildLinks("[��һҳ]", page == pages ? pages : page + 1, null);
        
        this.buildLinks("[δҳ]", pages, null);
        
        this.buildLinks(MessageFormat.format("[{0}ҳ,{1}��]",
        		                             new String[] {String.valueOf(pages), String.valueOf(product.getItems().size())}),
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
        this.formText.setLayoutData(new TableWrapData());
        formText.setText("<form><p><control href=\"composite\" fill=\"true\"/></p></form>", true, true);
        
        int beginItem = (page - 1) * RES_NUM_PERPAGE + 1;
        
        Iterator it = this.product.getItems().iterator();
        for (int i = 1; i < beginItem; i++) {
            it.next();
        }
        
        Composite composite = this.buildItemComposite(this.formText, it, page);
        
        this.formText.setControl("composite", composite);
        
        this.form.getBody().layout(true);
    }
    
    protected abstract Composite buildItemComposite(FormText formText, Iterator it, int page);
    
    public void dispose() {
        super.dispose();
        this.blueColor.dispose();
        this.activeLinkColor.dispose();
        this.bgColor.dispose();
        this.deepBgColor.dispose();
        this.orangeColor.dispose();
        this.whiteColor.dispose();
        this.blackColor.dispose();
    }

    public void setFocus()
    {
    }
}
