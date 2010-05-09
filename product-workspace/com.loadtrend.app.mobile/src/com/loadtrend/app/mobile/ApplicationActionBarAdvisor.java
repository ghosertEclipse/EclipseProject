package com.loadtrend.app.mobile;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import com.loadtrend.app.mobile.action.ChangeLanguageAction;
import com.loadtrend.app.mobile.action.ContactAuthorAction;
import com.loadtrend.app.mobile.action.ShowAboutDialogAction;
import com.loadtrend.app.mobile.action.ShowPreferencesDialogAction;
import com.loadtrend.app.mobile.action.ShowToolBarTextAction;
import com.loadtrend.app.mobile.action.UpdateAction;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.mobileaction.CloseMobilePortAction;
import com.loadtrend.app.mobile.mobileaction.MobileAction;
import com.loadtrend.app.mobile.mobileaction.OpenMobilePortAction;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor
{
	private IWorkbenchAction quitAction = null;
	
	private MobileAction openMobilePortAction = null;
	
	private MobileAction closeMobilePortAction = null;
	
	private Action showToolBarTextAction = null;
    
    private Action changeLanguageAction = null;
	
	private Action showPreferencesDialogAction = null;
    
    private Action contactAuthorAction = null;

    private Action showAboutDialogAction = null;
    
    private Action updateAction = null;
    
	public ApplicationActionBarAdvisor( IActionBarConfigurer configurer )
	{
		super( configurer );
	}
    
	protected void makeActions( IWorkbenchWindow window )
	{
        // System action
        quitAction = ActionFactory.QUIT.create( window );
        
        // Below is the code to create action
        showToolBarTextAction = new ShowToolBarTextAction(this.getActionBarConfigurer().getCoolBarManager());
        changeLanguageAction = new ChangeLanguageAction(window);
        showPreferencesDialogAction = new ShowPreferencesDialogAction(window);
        contactAuthorAction = new ContactAuthorAction();
        showAboutDialogAction = new ShowAboutDialogAction(window);
        updateAction = new UpdateAction(window);
        
        // Below is the code to create mobile action
        openMobilePortAction = new OpenMobilePortAction(window);
        closeMobilePortAction = new CloseMobilePortAction(window);
        
        // Initial the action state
		openMobilePortAction.setEnabled( true );
		closeMobilePortAction.setEnabled( false );
        
	}
	
	public void fillTrayItem(IMenuManager trayItem) {
		trayItem.add(openMobilePortAction);
		trayItem.add(closeMobilePortAction);
		trayItem.add(showPreferencesDialogAction);
	    trayItem.add(contactAuthorAction);
	    trayItem.add(updateAction);
	    trayItem.add(showAboutDialogAction);
		trayItem.add(quitAction);
	}

	protected void fillMenuBar( IMenuManager menuBar )
	{
		IMenuManager mobileMenu = new MenuManager( Messages.getString( MessagesConstant.MOBILE_MENU ) );
        mobileMenu.add( openMobilePortAction );
        mobileMenu.add( closeMobilePortAction );
        mobileMenu.add( new Separator() );
        mobileMenu.add( quitAction );
		
		IMenuManager viewMenu = new MenuManager( Messages.getString( MessagesConstant.VIEW_MENU ) );
		viewMenu.add( showToolBarTextAction );
        viewMenu.add( changeLanguageAction );
        
		IMenuManager windowMenu = new MenuManager( Messages.getString( MessagesConstant.WINDOW_MENU ) );
		windowMenu.add( showPreferencesDialogAction );
        
        IMenuManager helpMenu = new MenuManager( Messages.getString( MessagesConstant.HELP_MENU ) );
        helpMenu.add( updateAction );
        helpMenu.add( contactAuthorAction );
        helpMenu.add( showAboutDialogAction );
        
		menuBar.add( mobileMenu );
		menuBar.add( new Separator(IWorkbenchActionConstants.MB_ADDITIONS) );
		menuBar.add( viewMenu );
		menuBar.add( windowMenu );
        menuBar.add( helpMenu );
	}

	protected void fillCoolBar( ICoolBarManager coolBar )
	{
		IToolBarManager mobilePortToolBar = new ToolBarManager( SWT.FLAT | SWT.RIGHT );
		mobilePortToolBar.add( createActionContributionItem( openMobilePortAction ) );
		mobilePortToolBar.add( createActionContributionItem( closeMobilePortAction ) );
		IToolBarManager preferencesToolBar = new ToolBarManager( SWT.FLAT | SWT.RIGHT );
		preferencesToolBar.add( createActionContributionItem( showPreferencesDialogAction ) );
        
		coolBar.add( mobilePortToolBar );
		coolBar.add( preferencesToolBar );
        coolBar.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private ActionContributionItem createActionContributionItem( Action action )
	{
		ActionContributionItem aci = new ActionContributionItem( action );
		aci.setMode( ActionContributionItem.MODE_FORCE_TEXT );
		return aci;
	}
}
