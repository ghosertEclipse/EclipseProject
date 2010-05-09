package com.loadtrend.app.mobile.action;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import com.loadtrend.app.mobile.views.PBSearchView;

public class OpenPBSearchViewAction extends WorkbenchWindowAction
{
    public void run()
    {
        IWorkbenchPage page = super.window.getActivePage();
        IViewPart view = page.findView( PBSearchView.VIEW_ID );
        if ( view == null )
        {
            try
            {
                page.showView( PBSearchView.VIEW_ID );
            }
            catch ( PartInitException e )
            {
                e.printStackTrace();
            }
        }
        page.bringToTop( view );
    }
}
