package com.loadtrend.app.mobile.mobileaction;

import java.util.Collection;

import loadtrend.mobile.Message;
import loadtrend.mobile.Mobile;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.editors.SMSEditor;
import com.loadtrend.app.mobile.util.SpecialEntity;
import com.loadtrend.app.mobile.views.SMSExplorerView;
import com.loadtrend.app.trees.TreeObject;
import com.loadtrend.app.trees.TreeParent;

public class OpenSMSInSMSEditorAction extends MobileAction
{	
	private IWorkbenchPage page = null;
	
	private TreeObject to = null;
	
    protected TreeParent invisibleRoot = null;
    
    private TreeObject smsBox = null;
    
	/* (non-Javadoc)
	 * @see com.loadtrend.app.mobile.mobileaction.MobileAction#performNonUI()
	 */
	public void performNonUI()
	{
        if ( to.getCollection() != null )  return;
        retrieveMessage();
	}

	/* (non-Javadoc)
	 * @see com.loadtrend.app.mobile.mobileaction.MobileAction#performUIAfterNonUI()
	 */
	public void performUIAfterNonUI()
	{
		if ( super.hasError() ) return;
		try
		{
            SMSEditor editorPart = (SMSEditor) page.openEditor( to.getEditorInput(), SMSEditor.EDITOR_ID );
            editorPart.setTreeObject( to );
		}
		catch ( PartInitException e )
		{
			MessageDialog.openError( super.window.getShell(), 
                                     Messages.getString( MessagesConstant.OPEN_SMSEDITOR_FAILURE_MESSAGEBOX_TITLE ),
                                     e.getMessage() );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.loadtrend.app.mobile.mobileaction.MobileAction#performUIBeforeNonUI()
	 */
	public boolean performUIBeforeNonUI()
	{
		page = super.window.getActivePage();
        
		to = (TreeObject) super.structuredSelection.getFirstElement();

		if ( to.getParent().getName().startsWith( Messages.getString( MessagesConstant.LOCALMACHINE_TEXT ) ) )
		{
			// handle the local machine
            this.performUIAfterNonUI();
			return false;
		}
		
		IEditorInput editorInput = to.getEditorInput();
		
		IEditorPart editorPart = page.findEditor( editorInput );
		if ( editorPart == null )
		{
            invisibleRoot = ((SMSExplorerView) super.view).getInvisibleRoot();
			return true;
		}
		else
		{
			page.bringToTop( editorPart );
			editorPart.setFocus();
			return false;
		}
	}
    
    protected void retrieveMessage()
    {
        TreeObject localMachine = invisibleRoot.getChildren( Messages.getString( MessagesConstant.LOCALMACHINE_TEXT ) );
        smsBox = ( (TreeParent) localMachine ).getChildren( Messages.getString( MessagesConstant.SMSBOX_TEXT ) );
        
        TreeObject[] mobileSimCard = invisibleRoot.getChildren();
        
        for ( int i = 0; i < mobileSimCard.length; i++ )
        {
            TreeParent tp = (TreeParent) mobileSimCard[i];
            
            // Only read the message from Mobile and SimCard
            if ( !tp.getName().startsWith( Messages.getString( MessagesConstant.MOBILE_TEXT ) ) &&
                 !tp.getName().startsWith( Messages.getString( MessagesConstant.SIMCARD_TEXT ) ) )
            {
                continue;
            }
            
            TreeObject[] inboxOutbox = tp.getChildren();
            
            for ( int j = 0; j < inboxOutbox.length; j++ )
            {
                try
                {
                    this.retrieveMessageByTreeObject( inboxOutbox[j] );
                }
                catch ( Exception e )
                {
                    // You can't invoke SWT control in NonUI
                    super.saveError( Messages.getString( MessagesConstant.OPEN_SMSINSMSEDITOR_FAILURE_MESSAGEBOX_TITLE ), null, e );
                    return;
                }
            }
        }
    }
    
    private void retrieveMessageByTreeObject( TreeObject to ) throws Exception
    {
        Mobile mobile = super.mobile;
        if ( mobile == null )
            return;

        String deviceName = to.getParent().getName();
        String messageType = to.getName();
        String deviceParam = SpecialEntity.getSMSMemoParam( deviceName );
        deviceName = deviceName + "-" + messageType;
        
        Collection collection = null;
        
        super.showRealTimeInfo( Messages.getString( MessagesConstant.OPEN_SMSINSMSEDITOR_ACTION_INFO, deviceName ) );
        
        if ( messageType.startsWith( Messages.getString( MessagesConstant.INBOX_TEXT ) ) )
        {
            
            collection = mobile.listMessages( Message.READ_STATUS, deviceParam );
            collection.addAll( mobile.listMessages( Message.UNREAD_STATUS, deviceParam ) );
        }
        else
        {
            collection = mobile.listMessages( Message.SENT_STATUS, deviceParam );
            collection.addAll( mobile.listMessages( Message.UNSENT_STATUS, deviceParam ) );
        }
        
        to.setCollection( collection );
        smsBox.getCollection().addAll( collection );
        smsBox.setCollection( smsBox.getCollection() );
    }
}
