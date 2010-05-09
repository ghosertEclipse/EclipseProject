package com.loadtrend.app.mobile.action;

import com.loadtrend.app.mobile.dialog.NewSMSDialog;

public class ShowNewSMSDialogAction extends WorkbenchWindowAction
{
	public void run()
	{
        NewSMSDialog dialog = new NewSMSDialog( super.window.getShell() );
        dialog.create();
        if ( !dialog.getTxtReceiver().getText().equals( "" ) )
        {
            dialog.getTxtContent().setFocus();
            dialog.getTxtContent().setSelection( dialog.getTxtContent().getText().length() );
        }
        dialog.open();
	}
}
