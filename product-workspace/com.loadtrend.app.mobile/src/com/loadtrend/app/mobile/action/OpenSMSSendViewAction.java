package com.loadtrend.app.mobile.action;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import com.loadtrend.app.mobile.views.SMSSendView;

public class OpenSMSSendViewAction extends WorkbenchWindowAction
{
    
	public void run()
	{
		IWorkbenchPage page = super.window.getActivePage();
		IViewPart view = page.findView( SMSSendView.VIEW_ID );
		if ( view == null )
		{
			try
			{
				page.showView( SMSSendView.VIEW_ID );
			}
			catch ( PartInitException e )
			{
				e.printStackTrace();
			}
		}
		page.bringToTop( view );
	}
}
