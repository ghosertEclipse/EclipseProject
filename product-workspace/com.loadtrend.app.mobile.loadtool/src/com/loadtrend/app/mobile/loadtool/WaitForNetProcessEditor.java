package com.loadtrend.app.mobile.loadtool;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class WaitForNetProcessEditor extends EditorPart {

    public static final String EDITOR_ID = "com.loadtrend.app.mobile.loadtool.WaitForNetProcessEditor"; 

	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
        setSite( site );
        setInput( input );
//      ����Editor����������ʾ���ơ���Ҫ����������plugin.xml�е�name����
        setPartName(input.getName());
//      ����Editor��������ͼ�ꡣ��Ҫ������Զ�ʹ��һ��Ĭ�ϵ�ͼ��
//      setTitleImage(input.getImageDescriptor().createImage());
	}

	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	public void createPartControl(Composite parent) {
		Label label = new Label(parent, SWT.NORMAL);
		label.setText("waiting for net process.......");
	}

	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
