package com.loadtrend.app.mobile.mobileaction;

import java.util.Collection;

import loadtrend.mobile.Message;
import loadtrend.mobile.Mobile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.editors.SMSEditor;
import com.loadtrend.app.mobile.util.SpecialEntity;
import com.loadtrend.app.trees.TreeObject;

public class DeleteSMSAction extends MobileAction
{
	protected Collection collection = null;
	
    protected TreeObject to = null;
    
	public boolean performUIBeforeNonUI()
	{
        if ( !confirmToDelete() ) return false;
		
        collection = super.structuredSelection.toList();
        
        this.to = ((SMSEditor) part).getTreeObject();
        
        if ( to.getParent().getName().startsWith( Messages.getString( MessagesConstant.LOCALMACHINE_TEXT ) ) )
        {
            // handle the local machine
            this.performUIAfterNonUI();
            return false;
        }
        
		return true;
	}

	public void performNonUI()
	{
		Mobile mobile = super.mobile;
		
		try
		{
            // Set SMS memory to be deleted SMS.
            String deviceName = to.getParent().getName();
            String deviceParam = SpecialEntity.getSMSMemoParam( deviceName );
            synchronized (mobile)
            {
                mobile.setSMSMemoGetSMSNum( deviceParam );
                
                Message[] messages = (Message[]) collection.toArray( new Message[0] );
                int length = messages.length;
                for ( int i = 0; i < length; i++ )
                {
                    super.showRealTimeInfo( Messages.getString( MessagesConstant.DELETE_SMS_ACTION_INFO, 
                                                                super.getShortenContent( messages[i] ),
                                                                new Integer( i+1 ),
                                                                new Integer( length ) ) );
                    
                    mobile.deleteMessage( messages[i] );
                }
            }
		}
		catch ( Exception e )
		{
			super.saveError( Messages.getString( MessagesConstant.DELETESMS_FAILURE_MESSAGEBOX_TITLE ), null, e );
		}
	}

	public void performUIAfterNonUI()
	{
        if ( !super.hasError() )
        {
            to.getCollection().removeAll( collection );

            // Notify the observer to refresh interface.
            to.setCollection( to.getCollection() );
        }
	}
    
    protected boolean confirmToDelete()
    {
        MessageBox mb = new MessageBox( super.window.getShell(), SWT.YES | SWT.NO | SWT.ICON_WARNING );
        mb.setText( Messages.getString( MessagesConstant.CONFIRM_DELETESMS_MESSAGEBOX_TITLE ) );
        mb.setMessage( Messages.getString( MessagesConstant.CONFIRM_DELETESMS_MESSAGEBOX_BODY ) );
        int resultCode = mb.open();
        if ( resultCode == SWT.NO ) return false;
        return true;
    }
}
