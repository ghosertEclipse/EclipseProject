package com.loadtrend.app.mobile.action;

import loadtrend.mobile.Message;

import com.loadtrend.app.mobile.dialog.NewSMSDialog;

public class ShowResendSMSDialogAction extends WorkbenchWindowAction
{
    public void run()
    {
        Message message = (Message) super.structuredSelection.getFirstElement();
        
        NewSMSDialog dialog = new NewSMSDialog( super.window.getShell() );
        dialog.create();
        
        dialog.getTxtReceiver().setText( message.getTeleNum() );
        dialog.getTxtContent().setText( message.getContent() );
        if ( !message.getTeleNum().equals( "" ) )
        {
            dialog.getTxtContent().setFocus();
            dialog.getTxtContent().setSelection( message.getContent().length() );
        }
        dialog.open();
    }
}
