package com.loadtrend.app.mobile;

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.loadtrend.app.mobile.perspectives.ShortMessagePerspective;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor
{
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor( IWorkbenchWindowConfigurer configurer )
	{
		return new ApplicationWorkbenchWindowAdvisor( configurer );
	}

	public String getInitialWindowPerspectiveId()
	{
		return ShortMessagePerspective.PERSPECTIVE_ID;
	}
	
	public void initialize( IWorkbenchConfigurer configurer )
	{
		super.initialize( configurer );
		configurer.setSaveAndRestore( true );
	}
}
