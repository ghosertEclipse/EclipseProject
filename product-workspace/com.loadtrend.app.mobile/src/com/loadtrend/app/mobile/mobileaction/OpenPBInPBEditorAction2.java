package com.loadtrend.app.mobile.mobileaction;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;

import com.loadtrend.app.mobile.views.PBExplorerView;

public class OpenPBInPBEditorAction2 extends OpenPBInPBEditorAction {
    
    public OpenPBInPBEditorAction2(IWorkbenchWindow window, PBExplorerView view) {
        super.window = window;
        super.view = view;
    }
    
    public void run(IStructuredSelection structuredSelection) {
    	super.structuredSelection = structuredSelection;
    	super.run();
    }
}
