package com.loadtrend.app.mobile.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;

import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.dialog.AboutDialog;

public class ShowAboutDialogAction extends Action
{
    private IWorkbenchWindow window = null;
    
    public ShowAboutDialogAction(IWorkbenchWindow window)
    {
        super( Messages.getString( MessagesConstant.SHOW_ABOUTDIALOG_ACTION_TEXT ), AS_PUSH_BUTTON );
        // super.setAccelerator( SWT.CTRL | 'T' );
        super.setToolTipText( Messages.getString( MessagesConstant.SHOW_ABOUTDIALOG_ACTION_TOOLTIPTEXT ) );
        this.window = window;
    }
    
    public void run()
    {
        AboutDialog aboutDialog = new AboutDialog(this.window.getShell());
        aboutDialog.open();
    }
}
