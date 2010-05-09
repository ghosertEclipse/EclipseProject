package com.loadtrend.app.mobile.mobileaction;

import java.util.Collection;

import loadtrend.mobile.Mobile;
import loadtrend.mobile.MobileException;
import loadtrend.mobile.PhoneBook;
import loadtrend.mobile.PhoneBookProcessor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

import com.loadtrend.app.mobile.data.Global;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.data.Preferences;
import com.loadtrend.app.mobile.data.PreferencesConstant;
import com.loadtrend.app.mobile.editors.PBEditor;
import com.loadtrend.app.mobile.util.SpecialEntity;
import com.loadtrend.app.trees.TreeObject;
import com.loadtrend.app.trees.TreeParent;

public class SavePBAction extends MobileAction
{
    private String pbMemoType = null;
    
    private PhoneBook phoneBook = null;
    
    private TreeObject to = null;
    
    private int saveIndex = 0;
    
    public SavePBAction(IWorkbenchWindow window) {
        super.window = window;
    }
    
    public boolean performUIBeforeNonUI()
    {        
        // Get the value to be set to Phonebook from preferences.properties
        String txtName = Preferences.getValue( PreferencesConstant.TXT_NAME );
        String txtNumber = Preferences.getValue( PreferencesConstant.TXT_NUMBER );
        saveIndex = Integer.parseInt( Preferences.getValue( PreferencesConstant.PB_SAVE_INDEX ) );
        
        // Create and set the value to PhoneBook which to be saved.
        phoneBook = new PhoneBook();
        phoneBook.setName( txtName );
        phoneBook.setTeleNum( txtNumber );
        phoneBook.setIndex( saveIndex );
        
        // Get the mobile TreeObject or simcard TreeObject which is decided by user select.
        pbMemoType = Preferences.getValue( PreferencesConstant.PB_MEMO_TYPE );
        TreeParent tp = Global.getPBInvisibleRoot();
        TreeObject toParent = tp.getChildren( SpecialEntity.getPBMemoDesc( pbMemoType ) );
        tp = (TreeParent) toParent;

        if ( pbMemoType.equals( "" ) )
        {
            to = tp.getChildren( Messages.getString( MessagesConstant.DRAFT_TEXT ) );
            this.performUIAfterNonUI();
            return false;
        }
        else
        {
            // Check whether the phonebook has been list
            to = tp.getChildren( Messages.getString( MessagesConstant.PBBOX_TEXT ) );
            if ( to.getCollection() == null )
            {
                MessageBox mb = new MessageBox( super.window.getShell(), SWT.ICON_WARNING | SWT.OK );
                mb.setText( Messages.getString( MessagesConstant.NEWPB_DIALOG_WARNING_TITLE ) );
                mb.setMessage( Messages.getString( MessagesConstant.WARNING_SAVEPB_REFRESHLIST_FIRST ) );
                mb.open();
                return false;
            }

            return true;
        }
    }

    public void performNonUI()
    {
        try
        {
            this.savePB();
        }
        catch ( Exception e )
        {
            super.saveError( Messages.getString( MessagesConstant.SAVEPB_FALURE_MESSAGEBOX_TITLE ), null, e );
        }
    }

    public void performUIAfterNonUI()
    {
        if ( !super.hasError() )
        {
            IWorkbenchPage page = super.window.getActivePage();
            
            // Set or add saved phonebook, then open the editor part to show it.
            if ( to.getCollection() != null )
            {
                // Notify the observer to refresh interface.
                to.getCollection().add( phoneBook );
                to.setCollection( to.getCollection() );
                if ( !pbMemoType.equals( "" ) )
                {
                    // Load data to pbBox if the data is saved from simcard or mobile
                    TreeObject localMachine = Global.getPBInvisibleRoot().getChildren( Messages.getString( MessagesConstant.LOCALMACHINE_TEXT ) );
                    TreeObject pbBox = ( (TreeParent) localMachine ).getChildren( Messages.getString( MessagesConstant.PBBOX_TEXT ) );
                    pbBox.getCollection().add( phoneBook );
                    pbBox.setCollection( pbBox.getCollection() );
                }
                
                // Open the editor if it does not exist.
                IEditorPart editorPart = page.findEditor( to.getEditorInput() );
                if ( editorPart == null )
                {
                    try
                    {
                        editorPart = page.openEditor( to.getEditorInput(), PBEditor.EDITOR_ID );
                        ( (PBEditor) editorPart ).setTreeObject( to );
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
    
    private void savePB() throws MobileException
    {        
        Mobile mobile = super.mobile;
        
        int pieces = 1;
        
        int phoneBookSpace = mobile.getPhoneBookSpace( pbMemoType );
        
        Collection collection = to.getCollection();
        
        // Check the phonebook space before save the phonebooks.
        int capability = phoneBookSpace - pieces - collection.size();
        if ( capability < 0 )
        {
            throw new IllegalArgumentException( Messages.getString( MessagesConstant.ERROR_SAVEPB_CAPABILITY_FULL, 
                                                                    new Integer( pieces ), 
                                                                    new Integer( pieces + capability ) ) );
        }   
        
        // Save phonebooks
        int[] index = new int[pieces];
        
        if ( phoneBook.getIndex() == -1 )
        {
            // save & add phonebook
            PhoneBookProcessor.getPhoneBookIndexes( index, phoneBookSpace, collection );
        }
        else
        {
            // modify phonebook
            index = new int[] { phoneBook.getIndex() };
        }
        
        for ( int i = 0, n = 1; i < index.length; i++ )
        {
            super.showRealTimeInfo( Messages.getString( MessagesConstant.SAVE_PB_ACTION_INFO, 
                                                        getShortenContent( phoneBook ), 
                                                        phoneBook.getTeleNum(), 
                                                        new Integer( n++ ), 
                                                        new Integer( pieces ) ) );
            mobile.savePhoneBook( index[i], phoneBook );
        }
        
        // if I modify phonebook, I should remove it from collection firstly.
        if ( saveIndex != -1 )
        {
            collection.remove( phoneBook );
        }
    }
}
