package com.loadtrend.app.mobile.action;

import java.util.Iterator;
import java.util.TreeSet;

import loadtrend.mobile.PhoneBook;

import com.loadtrend.app.mobile.dialog.NewSMSDialog;

public class ShowPhoneSMSDialogAction extends WorkbenchWindowAction {
    public void run()
    {
        Iterator it = super.structuredSelection.iterator();
        
        NewSMSDialog dialog = new NewSMSDialog( super.window.getShell() );
        dialog.create();
        
        // Get rid of the duplicate items.
        TreeSet set = new TreeSet();
        while ( it.hasNext() )
        {
            PhoneBook phoneBook = (PhoneBook) it.next();
            set.add( phoneBook.getTeleNum() );
        }
        
        // create the string for txtReceiver
        String txtReceiver = "";
        it = set.iterator();
        while ( it.hasNext() )
        {
            txtReceiver = txtReceiver + it.next() + ";";
        }
        txtReceiver = txtReceiver.substring( 0, txtReceiver.length() - 1 );
        
        // set value to control in dialog
        dialog.getTxtReceiver().setText( txtReceiver );
        dialog.getTxtContent().setText( "" );
        dialog.getTxtContent().setFocus();
        
        dialog.open();
    }
}
