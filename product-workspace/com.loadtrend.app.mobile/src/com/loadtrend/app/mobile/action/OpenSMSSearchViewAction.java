package com.loadtrend.app.mobile.action;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import com.loadtrend.app.mobile.views.SMSSearchView;

public class OpenSMSSearchViewAction extends WorkbenchWindowAction
{
	public void run()
	{
		IWorkbenchPage page = super.window.getActivePage();
		IViewPart view = page.findView( SMSSearchView.VIEW_ID );
		if ( view == null )
		{
			try
			{
				page.showView( SMSSearchView.VIEW_ID );
			}
			catch ( PartInitException e )
			{
				e.printStackTrace();
			}
		}
		page.bringToTop( view );
	}
}
