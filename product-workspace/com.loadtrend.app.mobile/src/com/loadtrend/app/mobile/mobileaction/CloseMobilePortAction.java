package com.loadtrend.app.mobile.mobileaction;

import loadtrend.mobile.Mobile;

import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;

import com.loadtrend.app.mobile.data.Global;
import com.loadtrend.app.mobile.data.ImageConstant;
import com.loadtrend.app.mobile.data.ImageLoader;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.mobileActionListener.MobileActionStatusChangedListener;
import com.loadtrend.app.mobile.views.PBExplorerView;
import com.loadtrend.app.mobile.views.SMSExplorerView;
import com.loadtrend.app.trees.TreeObject;
import com.loadtrend.app.trees.TreeParent;

public class CloseMobilePortAction extends MobileAction implements MobileActionStatusChangedListener
{	
	/* (non-Javadoc)
     * @see com.loadtrend.app.mobile.mobileActionListener.MobileActionStatusChangedListener#changeStatus(loadtrend.mobile.Mobile)
     * Enable or disable the OPEN_MOBILEPORT_ACTION by mobile.isConnecting.
     */
    public void changeStatus(Mobile mobile) {
        super.setEnabled(mobile.isConnecting());
    }

    public CloseMobilePortAction(IWorkbenchWindow window)
	{
		super( Messages.getString( MessagesConstant.CLOSE_MOBILEPORT_ACTION_TEXT ), AS_PUSH_BUTTON );
		setToolTipText( Messages.getString( MessagesConstant.CLOSE_MOBILEPORT_ACTION_TOOLTIPTEXT ) );
		setAccelerator( SWT.CTRL | 'D' );
		ImageLoader imageLoader = ImageLoader.getInstance();
		setImageDescriptor( imageLoader.getImageDescriptor( ImageConstant.CLOSE_MOBILEPORT_ACTION_IMAGE ) );
		setDisabledImageDescriptor( imageLoader.getImageDescriptor( ImageConstant.CLOSE_MOBILEPORT_ACTION_DIMAGE ) );
        super.addMobileActionStatusChangedListener(this);
        super.window = window;
	}

	/* (non-Javadoc)
	 * @see com.loadtrend.app.mobile.mobileaction.MobileAction#performNonUI()
	 */
	public void performNonUI()
	{
		Mobile mobile = super.mobile;
		if ( mobile == null ) return;
		
		try
		{
			mobile.closeMobilePort();
            super.notifyMobileActionStatusChangedListener();
		}
		catch ( Exception me )
		{
			// You can't invoke SWT control in NonUI
			super.saveError( Messages.getString( MessagesConstant.CLOSE_MOBILEPORT_FAILURE_MESSAGEBOX_TITLE ),
					Messages.getString( MessagesConstant.CLOSE_MOBILEPORT_FAILURE_MESSAGEBOX_BODY ), me );
			return;
		}
	}

	/* (non-Javadoc)
	 * @see com.loadtrend.app.mobile.mobileaction.MobileAction#performUIAfterNonUI()
	 */
	public void performUIAfterNonUI()
	{
        // Close the mobile & simcard editor part and set mobile & simcard SMSCollection of TreeObject null value.
        IWorkbenchPage page = super.window.getActivePage();
        
		TreeParent invisibleRoot = Global.getSMSInvisibleRoot();
        this.closeEditorWithInvisibleRoot( page, invisibleRoot );
        
        invisibleRoot = Global.getPBInvisibleRoot();
        this.closeEditorWithInvisibleRoot( page, invisibleRoot );
        
        SMSExplorerView smsExplorerView = (SMSExplorerView) page.findView( SMSExplorerView.VIEW_ID );
        if ( smsExplorerView != null )
        {
            smsExplorerView.getTreeViewer().refresh();
        }
        
        PBExplorerView pbExplorerView = (PBExplorerView) page.findView( PBExplorerView.VIEW_ID );
        if ( pbExplorerView != null )
        {
            pbExplorerView.getTreeViewer().refresh();
        }
	}

	/* (non-Javadoc)
	 * @see com.loadtrend.app.mobile.mobileaction.MobileAction#performUIBeforeNonUI()
	 */
	public boolean performUIBeforeNonUI()
	{
		return true;
	}
	
    private void closeEditorWithInvisibleRoot( IWorkbenchPage page, TreeParent invisibleRoot )
    {
        TreeObject[] to = invisibleRoot.getChildren();
        for ( int i = 0; i < to.length; i++ )
        {
            if ( to[i].getName().startsWith( Messages.getString( MessagesConstant.MOBILE_TEXT ) ) )
            {
                this.closeMobileSimCardEditor( page, to[i] );
            }
            
            if ( to[i].getName().startsWith( Messages.getString( MessagesConstant.SIMCARD_TEXT ) ) )
            {
                this.closeMobileSimCardEditor( page, to[i] );
            }
        }
    }
    
	private void closeMobileSimCardEditor( IWorkbenchPage page, TreeObject to )
    {
        TreeParent tp = (TreeParent) to;
        for ( int i=0; i < tp.getChildren().length; i++ )
        {
            tp.getChildren()[i].setCollection( null );
            page.closeEditor( page.findEditor( tp.getChildren()[i].getEditorInput() ), false );
        }
    }
}
