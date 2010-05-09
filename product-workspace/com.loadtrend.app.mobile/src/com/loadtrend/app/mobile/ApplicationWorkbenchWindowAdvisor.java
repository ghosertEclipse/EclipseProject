package com.loadtrend.app.mobile;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.internal.util.PrefUtil;

import com.loadtrend.app.mobile.data.ImageConstant;
import com.loadtrend.app.mobile.data.ImageLoader;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.netaction.UserRecorderAction;
import com.loadtrend.app.mobile.perspectives.MusicPerspective;
import com.loadtrend.app.mobile.perspectives.PhoneBookPerspective;
import com.loadtrend.app.mobile.perspectives.PicturePerspective;
import com.loadtrend.app.mobile.perspectives.SMSArrivalPerspective;
import com.loadtrend.app.mobile.perspectives.ShortMessagePerspective;
import com.loadtrend.app.mobile.util.UpdateJobChangeListener;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
	
	private TrayItem trayItem = null;
	
	private Image trayImage = null;
	
	private ApplicationActionBarAdvisor actionBarAdvisor = null;

	public ApplicationWorkbenchWindowAdvisor( IWorkbenchWindowConfigurer configurer )
	{
		super( configurer );
	}

	public ActionBarAdvisor createActionBarAdvisor( IActionBarConfigurer configurer )
	{
		actionBarAdvisor = new ApplicationActionBarAdvisor(configurer);
		return actionBarAdvisor;
	}

	/**
	 * Configurate the profile of the application and store all the necessary instance to the Global class.
	 */
	public void preWindowOpen()
	{
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize( new Point( 1024, 768 ) );
        
        //
        configurer.setShowMenuBar( true );
		configurer.setShowPerspectiveBar( true );
		IPreferenceStore apiStore = PrefUtil.getAPIPreferenceStore();
		// apiStore.setValue(
		// IWorkbenchPreferenceConstants.INITIAL_FAST_VIEW_BAR_LOCATION,
		// IWorkbenchPreferenceConstants.LEFT );
		// apiStore.setValue(
		// IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, false );
		apiStore.setValue( IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR,
                           IWorkbenchPreferenceConstants.TOP_LEFT );
		
        configurer.setShowCoolBar( true );
		configurer.setShowStatusLine( true );
		configurer.setShowFastViewBars( true );
		configurer.setTitle( Messages.getString( MessagesConstant.MAIN_WINDOW_TITLE ) ); //$NON-NLS-1$
		
		// Add for update
		configurer.setShowProgressIndicator(true);
	}
    
    public void postWindowRestore()
    {
    }

    public void postWindowCreate()
    {        
        IWorkbenchPage page = getWindowConfigurer().getWindow().getActivePage();
        if ( page != null )
        {
            // IPerspectiveDescriptor activePersp = page.getPerspective();
            IPerspectiveRegistry reg = getWindowConfigurer().getWindow().getWorkbench().getPerspectiveRegistry();
            IPerspectiveDescriptor phonePersp = reg.findPerspectiveWithId( PhoneBookPerspective.PERSPECTIVE_ID ); //$NON-NLS-1$
            IPerspectiveDescriptor picturePersp = reg.findPerspectiveWithId( PicturePerspective.PERSPECTIVE_ID ); //$NON-NLS-1$
            IPerspectiveDescriptor musicPersp = reg.findPerspectiveWithId( MusicPerspective.PERSPECTIVE_ID ); //$NON-NLS-1$
            IPerspectiveDescriptor smsPersp = reg.findPerspectiveWithId( ShortMessagePerspective.PERSPECTIVE_ID ); //$NON-NLS-1$
            IPerspectiveDescriptor smsArrivalPersp = reg.findPerspectiveWithId( SMSArrivalPerspective.PERSPECTIVE_ID ); //$NON-NLS-1$
            page.setPerspective( phonePersp );
            page.setPerspective( musicPersp );
            page.setPerspective( picturePersp );
            page.setPerspective( smsPersp );
            page.setPerspective( smsArrivalPersp );
            // page.setPerspective( activePersp );
        }
    }
    

	/* (non-Javadoc)
	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#postWindowOpen()
	 */
	public void postWindowOpen() {
		
		// Log the user recorder
		new UserRecorderAction().run();
		
        // Setup the new job and listener and schedule the job
		UpdateJobChangeListener.getInstance(super.getWindowConfigurer().getWindow()).start(true);
		
		// Add system popup menu.
		trayItem = initTaskItem(super.getWindowConfigurer().getWindow());
		  if (trayItem != null) {
		    hookPopupMenu(super.getWindowConfigurer().getWindow());
		    hookMinimize(super.getWindowConfigurer().getWindow());
		  }
	}

	private void hookMinimize(final IWorkbenchWindow window) {
//		  window.getShell().addShellListener(new ShellAdapter() {
//		    public void shellIconified(ShellEvent e) {
//		      window.getShell().setVisible(false);
//		    }
//		  });
	  trayItem.addListener(SWT.DefaultSelection, new Listener() {
	    public void handleEvent(Event event) {
	      Shell shell = window.getShell();
	      if (!shell.isVisible()) {
	        shell.setVisible(true);
	      }
          window.getShell().setMinimized(false);
	    }
	  });
	}

	private void hookPopupMenu(final IWorkbenchWindow window) {
	  trayItem.addListener(SWT.MenuDetect, new Listener() {
	    public void handleEvent(Event event) {
	      MenuManager trayMenu = new MenuManager();
	      Menu menu = trayMenu.createContextMenu(window.getShell());
	      actionBarAdvisor.fillTrayItem(trayMenu);
	      menu.setVisible(true);
	    }
	  });
	}

	private TrayItem initTaskItem(IWorkbenchWindow window) {
	  final Tray tray = window.getShell().getDisplay().getSystemTray();
	  if (tray == null)
	    return null;
	  TrayItem trayItem = new TrayItem(tray, SWT.NONE);
	  trayImage = ImageLoader.getInstance().getImage(ImageConstant.MOBILE_IMAGE);
	  trayItem.setImage(trayImage);
	  trayItem.setToolTipText(Messages.getString(MessagesConstant.MAIN_WINDOW_TITLE));
	  return trayItem;
	}

	public void dispose() {
	  if (trayImage != null) {
	    trayImage.dispose();
	    trayItem.dispose();
	  }
	}
}
