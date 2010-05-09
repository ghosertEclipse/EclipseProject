package com.loadtrend.app.mobile.mobileaction;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

import loadtrend.mobile.Message;
import loadtrend.mobile.MessageProcessor;
import loadtrend.mobile.Mobile;
import loadtrend.mobile.MobileException;

import com.loadtrend.app.mobile.data.Global;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.data.Preferences;
import com.loadtrend.app.mobile.data.PreferencesConstant;
import com.loadtrend.app.mobile.editors.SMSEditor;
import com.loadtrend.app.mobile.util.SpecialEntity;
import com.loadtrend.app.trees.TreeObject;
import com.loadtrend.app.trees.TreeParent;

public class SendSaveSMSAction extends MobileAction
{
	protected String smsMemoType = null;
	
	private String[] targetNumbers = null;
    
    private Message[] messages = null;
	
	private ArrayList messageSaved = null;
	
    public SendSaveSMSAction(IWorkbenchWindow window) {
        super.window = window;
    }
    
	public boolean performUIBeforeNonUI()
	{
        // Get the value to be set to Message from preferences.properties
        String txtReceiver = Preferences.getValue( PreferencesConstant.TXT_RECEIVER );
        String txtContent = Preferences.getValue( PreferencesConstant.TXT_CONTENT );
        
        boolean isHandFree = false;
		if ( Preferences.getValue( PreferencesConstant.HAND_FREE ).equals( "true" ) )
		{
			isHandFree = true;
		}
        
        boolean isStatusReport = false;
		if ( Preferences.getValue( PreferencesConstant.STATUS_REPORT ).equals( "true" ) )
		{
			isStatusReport = true;
		}
        
        int validTime = Integer.parseInt( Preferences.getValue( PreferencesConstant.VALID_TIME ) );
		
        // Get target numbers from single line string
		ArrayList list = new ArrayList();
        String errorNumber = "";
		String[] numbers = txtReceiver.split( ";" );
		for ( int i = 0; i < numbers.length; i++ )
		{
			if ( !numbers[i].equals( "" ) && ( numbers[i].matches( "[+][0-9]+" ) ) || ( numbers[i].matches( "[0-9]+" ) ) )
			{
				list.add( numbers[i] );
			}
            else
            {
                // ERROR_TELENUMBER_PLUSPREFIX error happens
                if ( !numbers[i].equals( "" ) )
                {
                    errorNumber = errorNumber + numbers[i] + ";";
                }
            }
		}
        if ( !errorNumber.equals( "" ) )
        {
            // Get rid of ';' and show the errorNumber
            errorNumber = errorNumber.substring( 0, errorNumber.length() - 1 );
            MessageDialog.openError( super.window.getShell(), 
                                     Messages.getString( MessagesConstant.SENDSAVESMS_FALURE_MESSAGEBOX_TITLE ),
                                     Messages.getString( MessagesConstant.ERROR_TELENUMBER_PLUSPREFIX ) + 
                                     " " + errorNumber );
            return false;
        }
		targetNumbers = (String[]) list.toArray( new String[]{} );
		
        // Create and set the value to Message which to be sent
		Message message = new Message();
		message.setContent( txtContent );
		message.setDeliveryHandFree( isHandFree );
		message.setDeliveryStatusReport( isStatusReport );
		message.setDeliveryValidTime( validTime );
		
        messages = MessageProcessor.splitMessageByContent( message );
        
		return true;
	}

	public void performNonUI()
	{
		try
		{
            this.sendSMS();
            
            smsMemoType = Preferences.getValue( PreferencesConstant.SMS_MEMO_TYPE );
            if ( smsMemoType.equals( "" ) )
            {
                this.localSaveSMS();
            }
            else
            {
                this.saveSMS();
            }
		}
		catch ( Exception e )
		{
			super.saveError( Messages.getString( MessagesConstant.SENDSAVESMS_FALURE_MESSAGEBOX_TITLE ), null, e );
		}
	}

	public void performUIAfterNonUI()
	{
		if ( !super.hasError() )
		{
            IWorkbenchPage page = super.window.getActivePage();
            
            // Get the mobile TreeObject or simcard TreeObject which is decided by user select.
			TreeParent tp = Global.getSMSInvisibleRoot();
			TreeObject to = null;
            to = tp.getChildren( SpecialEntity.getSMSMemoDesc( smsMemoType ) );
			
            // Get the outbox TreeObject, the short message will be saved to outbox anyway.
			tp = ( TreeParent ) to;
            
            if ( smsMemoType.equals( "" ) )
            {
                to = tp.getChildren( Messages.getString( MessagesConstant.DRAFT_TEXT ) );
            }
            else
            {
                to = tp.getChildren( Messages.getString( MessagesConstant.OUTBOX_TEXT ) );
            }
			
            // Set or add saved message, then open the editor part to show it.
			if ( to.getCollection() != null )
            {
                // Notify the observer to refresh interface.
                to.getCollection().addAll( messageSaved );
                to.setCollection( to.getCollection() );
                if ( !smsMemoType.equals( "" ) )
                {
                    // Load data to smsBox if the data is saved from simcard or mobile
                    TreeObject localMachine = Global.getSMSInvisibleRoot().getChildren( Messages.getString( MessagesConstant.LOCALMACHINE_TEXT ) );
                    TreeObject smsBox = ( (TreeParent) localMachine ).getChildren( Messages.getString( MessagesConstant.SMSBOX_TEXT ) );
                    smsBox.getCollection().addAll( messageSaved );
                    smsBox.setCollection( smsBox.getCollection() );
                }
                
                // Open the editor if it does not exist.
                IEditorPart editorPart = page.findEditor( to.getEditorInput() );
                if ( editorPart == null )
                {
                    try
                    {
                        editorPart = page.openEditor( to.getEditorInput(), SMSEditor.EDITOR_ID );
                        ( (SMSEditor) editorPart ).setTreeObject( to );
                    }
                    catch ( PartInitException e )
                    {
                        e.printStackTrace();
                    }
                }
                page.bringToTop( editorPart );
            }
		}
	}
    
    protected void sendSMS() throws MobileException
    {
        Mobile mobile = super.mobile;
        int numOfTeleNum = targetNumbers.length;
        int numOfContent = messages.length;
        int pieces = numOfTeleNum * numOfContent;
        
        // Send short messages
        for ( int i = 0, n = 1; i < numOfTeleNum; i++ )
        {
            for ( int j = 0; j < numOfContent; j++ )
            {
                messages[j].setTeleNum( targetNumbers[i] );
                super.showRealTimeInfo( Messages.getString( MessagesConstant.SEND_SMS_ACTION_INFO, 
                                                            getShortenContent( messages[j] ),
                                                            targetNumbers[i],
                                                            new Integer( n++ ),
                                                            new Integer( pieces ) ) );
                mobile.sendStandardMessage( messages[j] );
            }
        }
    }
    
    protected void saveSMS() throws MobileException
    {        
        Mobile mobile = super.mobile;
        int numOfTeleNum = targetNumbers.length;
        int numOfContent = messages.length;
        
        if ( numOfTeleNum == 0 )
        {
            numOfTeleNum = 1;
            targetNumbers = new String[] { "" };
        }
        
        int pieces = numOfTeleNum * numOfContent;
        
        synchronized (mobile)
        {
            // Check the sms space before save the messages.
            int capability = mobile.checkSmsSpace( pieces, smsMemoType );
            if ( capability < 0 )
            {
                throw new IllegalArgumentException( Messages.getString( MessagesConstant.ERROR_SAVESMS_CAPABILITY_FULL, 
                                                                        new Integer( pieces ), 
                                                                        new Integer( pieces + capability ) ) );
            }
            
            // Save short messages
            messageSaved = new ArrayList();
            for ( int i = 0, n = 1; i < numOfTeleNum; i++ )
            {
                for ( int j = 0; j < numOfContent; j++ )
                {
                    Message cloneMessage = (Message) messages[j].clone();
                    cloneMessage.setTeleNum( targetNumbers[i] );
                    super.showRealTimeInfo( Messages.getString( MessagesConstant.SAVE_SMS_ACTION_INFO, 
                                                                getShortenContent( cloneMessage ),
                                                                targetNumbers[i],
                                                                new Integer( n++ ),
                                                                new Integer( pieces ) ) );
                    mobile.saveStandardMessage( cloneMessage );
                    messageSaved.add( cloneMessage );
                }
            }
        }
    }
    
    protected void localSaveSMS()
    {
        int numOfTeleNum = targetNumbers.length;
        int numOfContent = messages.length;
        
        if ( numOfTeleNum == 0 )
        {
            numOfTeleNum = 1;
            targetNumbers = new String[] { "" };
        }
        
        // Save short messages
        messageSaved = new ArrayList();
        for ( int i = 0; i < numOfTeleNum; i++ )
        {
            for ( int j = 0; j < numOfContent; j++ )
            {
                Message cloneMessage = (Message) messages[j].clone();
                cloneMessage.setTeleNum( targetNumbers[i] );
                messageSaved.add( cloneMessage );
            }
        }
    }
}
