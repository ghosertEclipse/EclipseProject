package com.loadtrend.app.mobile.action;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;

import com.loadtrend.app.mobile.data.Global;
import com.loadtrend.app.mobile.data.ImageConstant;
import com.loadtrend.app.mobile.data.ImageLoader;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.dialog.PreferencesDialog;

public class ShowPreferencesDialogAction extends Action
{
    private IWorkbenchWindow window = null;
	public ShowPreferencesDialogAction(IWorkbenchWindow window)
	{
		super( Messages.getString( MessagesConstant.SHOW_PREFERENCESDIALOG_ACTION_TEXT ), AS_PUSH_BUTTON );
		super.setAccelerator( SWT.CTRL | 'D' );
		super.setToolTipText( Messages.getString( MessagesConstant.SHOW_PREFERENCESDIALOG_ACTION_TOOLTIPTEXT ) );
		ImageLoader loader = ImageLoader.getInstance();
		super.setImageDescriptor( loader.getImageDescriptor( ImageConstant.SHOW_PREFERENCESDIALOG_ACTION_IMAGE ) );
		super.setDisabledImageDescriptor( loader.getImageDescriptor( ImageConstant.SHOW_PREFERENCESDIALOG_ACTION_DIMAGE ) );
        this.window = window;
	}
	
	public void run()
	{
		new PreferencesDialog(this.window.getShell(), Global.getMobile()).open();
	}
}
