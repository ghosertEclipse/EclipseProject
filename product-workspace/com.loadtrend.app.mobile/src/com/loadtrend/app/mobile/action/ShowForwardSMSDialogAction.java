package com.loadtrend.app.mobile.action;

import loadtrend.mobile.Message;

import com.loadtrend.app.mobile.dialog.NewSMSDialog;

public class ShowForwardSMSDialogAction extends WorkbenchWindowAction
{
    public void run()
    {
        Message message = (Message) super.structuredSelection.getFirstElement();
        
        NewSMSDialog dialog = new NewSMSDialog( super.window.getShell() );
        dialog.create();
        
        dialog.getTxtReceiver().setText( "" );
        dialog.getTxtContent().setText( message.getContent() );
        
        dialog.open();
    }
}
