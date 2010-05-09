package com.loadtrend.app.mobile.mobileaction;

import org.eclipse.ui.IWorkbenchWindow;

import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;

public class SendSMSAction extends SendSaveSMSAction
{	
    public SendSMSAction(IWorkbenchWindow window) {
        super(window);
    }
    
	public void performNonUI()
	{
		try
		{
            super.sendSMS();
		}
		catch ( Exception e )
		{
			super.saveError( Messages.getString( MessagesConstant.SENDSMS_FALURE_MESSAGEBOX_TITLE ), null, e );
		}
	}
	
	public void performUIAfterNonUI()
	{
	}
}
