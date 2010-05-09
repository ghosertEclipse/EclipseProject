package com.loadtrend.app.mobile.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.swt.SWT;

import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;

/**
 * @author Zhang Jiawei
 * 
 */
public class ShowToolBarTextAction extends Action
{
    private ICoolBarManager coolBarManager = null;
    
	public ShowToolBarTextAction(ICoolBarManager coolBarManager)
	{
		super( Messages.getString( MessagesConstant.SHOW_TOOLBARTEXT_ACTION_TEXT ), AS_CHECK_BOX );
		super.setAccelerator( SWT.CTRL | 'T' );
		super.setToolTipText( Messages.getString( MessagesConstant.SHOW_TOOLBARTEXT_ACTION_TOOLTIPTEXT ) );
		super.setChecked( true );
        this.coolBarManager = coolBarManager;
	}

	public void run()
	{
		int mode = 0;

		if ( super.isChecked() )
		{
			mode = ActionContributionItem.MODE_FORCE_TEXT;
		}
		
		// Get IContributionItem which can be converted to ToolBarContributionItem then get IToolBarManager from it.
		IContributionItem[] ici = coolBarManager.getItems();

		// Get all the ActionContributionItem, and set the mode as per parameter.
		for ( int i = 0; i < ici.length; i++ )
		{
			ToolBarContributionItem tbci = (ToolBarContributionItem) ici[i];

			IToolBarManager toolBarManager = tbci.getToolBarManager();

			IContributionItem[] aci = toolBarManager.getItems();

			for ( int j = 0; j < aci.length; j++ )
			{
				( (ActionContributionItem) aci[j] ).setMode( mode );
			}
		}
		
		coolBarManager.update( true );
	}

}
