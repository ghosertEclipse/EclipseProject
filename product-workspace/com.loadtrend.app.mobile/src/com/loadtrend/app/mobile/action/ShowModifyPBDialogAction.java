package com.loadtrend.app.mobile.action;

import loadtrend.mobile.Mobile;
import loadtrend.mobile.PhoneBook;

import com.loadtrend.app.mobile.data.Preferences;
import com.loadtrend.app.mobile.data.PreferencesConstant;
import com.loadtrend.app.mobile.dialog.NewPBDialog;
import com.loadtrend.app.mobile.editors.PBEditor;
import com.loadtrend.app.mobile.util.SpecialEntity;

public class ShowModifyPBDialogAction extends WorkbenchWindowAction
{
    private String pbMemoDesc = null;
    
    public void run()
    {
        // Get the phonebook selected to be modified.
        PhoneBook phoneBook = (PhoneBook) super.structuredSelection.getFirstElement();
        
        // Get the phonebook memo type
        pbMemoDesc = ((PBEditor) super.part).getTreeObject().getParent().getName();
        String pbMemoType = SpecialEntity.getPBMemoParam( pbMemoDesc );
        
        // Set the values to dialog.
        NewPBDialog dialog = new NewPBDialog( super.window.getShell() );
        dialog.create();
        
        // Set phonebook memo type to dialog radio.
        if ( pbMemoType.equals( Mobile.PB_ME_MEMO ) )
        {
            dialog.getRdMobile().setSelection( true );
        }
        else if ( pbMemoType.equals( Mobile.PB_SM_MEMO ) )
        {
            dialog.getRdSimCard().setSelection( true );
        }
        else
        {
            dialog.getRdLocalMachine().setSelection( true );
        }
        
        // Disable the dialog radio for phonebook memo type when modifying the phonebook
        dialog.getRdLocalMachine().setEnabled( false );
        dialog.getRdMobile().setEnabled( false );
        dialog.getRdSimCard().setEnabled( false );
        
        // Set the phonebook value to the dialog interface.
        dialog.getTxtName().setText( phoneBook.getName() );
        dialog.getTxtTeleNum().setText( phoneBook.getTeleNum() );
        dialog.getTxtTeleNum().setFocus();
        dialog.getTxtTeleNum().setSelection( 0, phoneBook.getTeleNum().length() );
        
        // Set phonebook index to Preferences
        Preferences.setValue( PreferencesConstant.PB_SAVE_INDEX, String.valueOf( phoneBook.getIndex() ) );
        
        dialog.open();
    }
}
