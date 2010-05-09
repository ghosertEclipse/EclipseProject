package com.loadtrend.app.mobile.loadtool;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class BrowserEditor extends EditorPart {
    
    public static final String EDITOR_ID = "com.loadtrend.app.mobile.loadtool.BrowserEditor"; 

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

    public void createPartControl(Composite parent) {
        // TODO Auto-generated method stub
        Browser browser = new Browser(parent, SWT.NONE);
        browser.setUrl("http://221.137.247.59/jmobile/uploadFile.do");
    }

    public void setFocus() {
        // TODO Auto-generated method stub

    }

}
