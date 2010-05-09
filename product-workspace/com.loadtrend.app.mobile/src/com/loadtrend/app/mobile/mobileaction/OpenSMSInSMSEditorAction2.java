package com.loadtrend.app.mobile.mobileaction;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;

import com.loadtrend.app.mobile.views.SMSExplorerView;

public class OpenSMSInSMSEditorAction2 extends OpenSMSInSMSEditorAction {
    
    public OpenSMSInSMSEditorAction2(IWorkbenchWindow window, SMSExplorerView view) {
        super.window = window;
        super.view = view;
    }
    
    public void run(IStructuredSelection structuredSelection) {
    	super.structuredSelection = structuredSelection;
    	super.run();
    }
}
