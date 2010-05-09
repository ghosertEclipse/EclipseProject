package com.loadtrend.app.mobile.mobileaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import loadtrend.mobile.Message;
import loadtrend.mobile.Mobile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindowPulldownDelegate2;
import org.eclipse.ui.PartInitException;

import com.loadtrend.app.mobile.data.Global;
import com.loadtrend.app.mobile.data.ImageConstant;
import com.loadtrend.app.mobile.data.ImageLoader;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.editors.SMSEditor;
import com.loadtrend.app.mobile.util.SpecialEntity;
import com.loadtrend.app.trees.TreeObject;
import com.loadtrend.app.trees.TreeParent;

public class ExportSMSAction extends MobileAction implements IWorkbenchWindowPulldownDelegate2
{
    protected String smsMemoType = null;
    
    private Collection smsCollection = null;
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchWindowPulldownDelegate#getMenu(org.eclipse.swt.widgets.Control)
     */
    public Menu getMenu(Control parent) {
        Menu exportSMSMenu = new Menu( parent );
        return this.createMenu(exportSMSMenu);
    }
    
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchWindowPulldownDelegate2#getMenu(org.eclipse.swt.widgets.Menu)
     */
    public Menu getMenu(Menu parent) {
        Menu exportSMSMenu = new Menu( parent );
        return this.createMenu(exportSMSMenu);
    }
    
    private Menu createMenu(Menu exportSMSMenu) {
        ImageLoader loader = ImageLoader.getInstance();
        
        // Click Mobile item
        MenuItem mobileItem = new MenuItem( exportSMSMenu, SWT.PUSH );
        mobileItem.setText( Messages.getString( MessagesConstant.MOBILE_TEXT ) );
        mobileItem.setImage( loader.getImage( ImageConstant.MOBILE_IMAGE ) );
        mobileItem.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                smsMemoType = Mobile.SMS_ME_MEMO;
                ExportSMSAction.this.run();
            }
        } );
        
        // Click Sim Card item
        MenuItem simCardItem = new MenuItem( exportSMSMenu, SWT.PUSH );
        simCardItem.setText( Messages.getString( MessagesConstant.SIMCARD_TEXT ) );
        simCardItem.setImage( loader.getImage( ImageConstant.SIMCARD_IMAGE ) );
        simCardItem.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                smsMemoType = Mobile.SMS_SM_MEMO;
                ExportSMSAction.this.run();
            }
        } );

        return exportSMSMenu; 
    }


    public void performNonUI()
    {
        Mobile mobile = super.mobile;
        
        int pieces = smsCollection.size();
        
        try
        {
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
                Message[] messages = (Message[]) smsCollection.toArray( new Message[]{} );
                smsCollection.clear();
                for ( int i = 0, n = 1; i < messages.length; i++ )
                {
                    Message cloneMessage = (Message) messages[i].clone();
                    super.showRealTimeInfo( Messages.getString( MessagesConstant.SAVE_SMS_ACTION_INFO, 
                                                                getShortenContent( cloneMessage ),
                                                                cloneMessage.getTeleNum(),
                                                                new Integer( n++ ),
                                                                new Integer( pieces ) ) );
                    mobile.saveStandardMessage( cloneMessage );
                    smsCollection.add( cloneMessage );
                }
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
            TreeObject inbox = tp.getChildren( Messages.getString( MessagesConstant.INBOX_TEXT ) );
            TreeObject outbox = tp.getChildren( Messages.getString( MessagesConstant.OUTBOX_TEXT ) );
            
            // Split the sms collection with inbox sms and outbox sms.
            ArrayList inboxList = new ArrayList();
            ArrayList outboxList = new ArrayList();
            
            Iterator it = smsCollection.iterator();
            while ( it.hasNext() )
            {
                Message message = (Message) it.next();
                int status = message.getStatus();
                if ( status == Message.UNREAD_STATUS || status == Message.READ_STATUS )
                {
                    inboxList.add( message );
                }
                else
                {
                    outboxList.add( message );
                }
            }
            
            // Set or add saved message, then open the editor part to show it.
            if ( inbox.getCollection() != null )
            {
                // Notify the observer to refresh interface.
                inbox.getCollection().addAll( inboxList );
                inbox.setCollection( inbox.getCollection() );
                outbox.getCollection().addAll( outboxList );
                outbox.setCollection( outbox.getCollection() );
                if ( !smsMemoType.equals( "" ) )
                {
                    // Load data to smsBox if the data is saved from simcard or mobile
                    TreeObject localMachine = Global.getSMSInvisibleRoot().getChildren( Messages.getString( MessagesConstant.LOCALMACHINE_TEXT ) );
                    TreeObject smsBox = ( (TreeParent) localMachine ).getChildren( Messages.getString( MessagesConstant.SMSBOX_TEXT ) );
                    smsBox.getCollection().addAll( smsCollection );
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
        
        smsMemoType = null;
    }

    public boolean performUIBeforeNonUI()
    {
        if ( smsMemoType == null ) return false;
        ArrayList list = new ArrayList();
        list.addAll( super.structuredSelection.toList() );
        smsCollection = list;
        return true;
    }
}
