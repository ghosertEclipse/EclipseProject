package com.loadtrend.app.mobile.action;

import java.io.IOException;

import org.eclipse.jface.action.Action;

import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;

public class ContactAuthorAction extends Action
{
    public ContactAuthorAction()
    {
        super( Messages.getString( MessagesConstant.CONTACT_AUTHOR_ACTION_TEXT ), AS_PUSH_BUTTON );
        // super.setAccelerator( SWT.CTRL | 'T' );
        super.setToolTipText( Messages.getString( MessagesConstant.CONTACT_AUTHOR_ACTION_TOOLTIPTEXT ) );
    }
    
    public void run()
    {
        try
        {
            Runtime.getRuntime().exec( "cmd.exe /c start mailto:ghoster_e@yahoo.com" );
        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
