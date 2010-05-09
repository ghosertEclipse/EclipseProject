package com.loadtrend.app.mobile.action;

import com.loadtrend.app.mobile.data.Preferences;
import com.loadtrend.app.mobile.data.PreferencesConstant;
import com.loadtrend.app.mobile.dialog.NewPBDialog;

public class ShowNewPBDialogAction extends WorkbenchWindowAction {
    
    public void run()
    {
        NewPBDialog dialog = new NewPBDialog( super.window.getShell() );
        dialog.create();
        
        // Set phonebook index to Preferences
        Preferences.setValue( PreferencesConstant.PB_SAVE_INDEX, String.valueOf( -1 ) );
        
        dialog.open();
    }
}
