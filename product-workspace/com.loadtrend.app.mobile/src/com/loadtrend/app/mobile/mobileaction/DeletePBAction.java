package com.loadtrend.app.mobile.mobileaction;

import java.util.Collection;

import loadtrend.mobile.Mobile;
import loadtrend.mobile.PhoneBook;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.editors.PBEditor;
import com.loadtrend.app.mobile.util.SpecialEntity;
import com.loadtrend.app.trees.TreeObject;

public class DeletePBAction extends MobileAction
{
    protected Collection collection = null;
    
    protected TreeObject to = null;
    
    public boolean performUIBeforeNonUI()
    {
        if ( !confirmToDelete() ) return false;
        
        // Get selected items to be deleted.
        collection = super.structuredSelection.toList();
        
        this.to = ((PBEditor) part).getTreeObject();
        
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
            // Set phonebook memory to be deleted phonebook.
            String deviceName = to.getParent().getName();
            String deviceParam = SpecialEntity.getSMSMemoParam( deviceName );
            synchronized ( mobile )
            {
                mobile.setPhoneBookMemo( deviceParam );
                
                PhoneBook[] phoenbooks = (PhoneBook[]) collection.toArray( new PhoneBook[0] );
                int length = phoenbooks.length;
                for ( int i = 0; i < length; i++ )
                {
                    super.showRealTimeInfo( Messages.getString( MessagesConstant.DELETE_PB_ACTION_INFO, 
                                                                super.getShortenContent( phoenbooks[i] ),
                                                                new Integer( i+1 ),
                                                                new Integer( length ) ) );
                    
                    mobile.deletePhoneBook( phoenbooks[i] );
                }
            }
        }
        catch ( Exception e )
        {
            super.saveError( Messages.getString( MessagesConstant.DELETEPB_FAILURE_MESSAGEBOX_TITLE ), null, e );
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
        mb.setText( Messages.getString( MessagesConstant.CONFIRM_DELETEPB_MESSAGEBOX_TITLE ) );
        mb.setMessage( Messages.getString( MessagesConstant.CONFIRM_DELETEPB_MESSAGEBOX_BODY ) );
        int resultCode = mb.open();
        if ( resultCode == SWT.NO ) return false;
        return true;
    }
}
