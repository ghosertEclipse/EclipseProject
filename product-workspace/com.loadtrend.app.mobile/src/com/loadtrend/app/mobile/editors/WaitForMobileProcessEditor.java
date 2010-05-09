package com.loadtrend.app.mobile.editors;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.EditorPart;

import com.loadtrend.app.mobile.MobilePlugin;
import com.loadtrend.app.mobile.control.MobileImage;
import com.loadtrend.app.mobile.data.ImageConstant;
import com.loadtrend.app.mobile.data.ImageLoader;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;

public class WaitForMobileProcessEditor extends EditorPart
{
	public static final String EDITOR_ID = "com.loadtrend.app.mobile.editors.WaitForMobileProcessEditor"; 
	
    protected String bodyHtml = null;
    
    protected String picKey = "pic";
    
    private ImageDescriptor imageDescriptor = ImageLoader.getInstance().getImageDescriptor(ImageConstant.LONG_PROCESS_IMAGE);
    
    private URL imageUrl = MobilePlugin.getDefault().find(new Path("/icons/longProcess.gif"));
    
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
//		设置Editor标题栏的显示名称。不要，则名称用plugin.xml中的name属性
		setPartName(input.getName());
//		设置Editor标题栏的图标。不要，则会自动使用一个默认的图标
//		setTitleImage(input.getImageDescriptor().createImage());
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
        FormToolkit kit = new FormToolkit( parent.getDisplay() );
        ScrolledForm scrolledForm = kit.createScrolledForm( parent );
        
        TableWrapLayout layout = new TableWrapLayout();
        scrolledForm.getBody().setLayout( layout );
        scrolledForm.setAlwaysShowScrollBars( false );
        layout.numColumns = 1;
        layout.bottomMargin = 0;
        layout.topMargin = 100;
        layout.leftMargin = 150;
        layout.rightMargin = 0;
        layout.verticalSpacing = 10;
        
        final FormText formText = kit.createFormText( scrolledForm.getBody(), false );
        formText.setLayoutData(new TableWrapData());
        
        createBodyHtml( "" );
        formText.setText(bodyHtml, true, true);
        formText.setBackground( parent.getDisplay().getSystemColor( SWT.COLOR_WHITE ) );
        formText.setWhitespaceNormalized( false );

        ImageData imageData = this.imageDescriptor.getImageData();
        
        Composite composite = kit.createComposite(formText);
        RowLayout rowlayout = new RowLayout();
        rowlayout.marginTop = 0;
        composite.setLayout(rowlayout);
        
        // load gif
        MobileImage mobileImage = new MobileImage(composite, imageData.width, imageData.height);
        mobileImage.setLayoutData(new RowData(imageData.width, imageData.height));
        mobileImage.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        
        InputStream inputStream = null;
        try {
            inputStream = this.imageUrl.openStream();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mobileImage.paintImageAsyn(inputStream, null, null, null);
        formText.setControl(picKey, composite);
        
        IWorkbenchWindow window = getEditorSite().getWorkbenchWindow();
        final Display display = window.getShell().getDisplay();
        // startAnimation within 100 ms.
        display.asyncExec(new Runnable() {
            public void run() {
                if ( formText.isDisposed() ) return;
                formText.setText( bodyHtml, true, true );
                formText.redraw();
                display.timerExec(100, this );
            }
        });
	}
    
	public void setFocus()
	{
	}
    
    /**
     * Define the content format to be shown.
     * @param content
     */
    public void createBodyHtml( String content )
    {
        bodyHtml = Messages.getString( MessagesConstant.WAITFOR_MOBILEPROCESS_EDITOR_BODY_HTML, picKey, content );
    }
}
