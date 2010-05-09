package com.loadtrend.app.mobile.mobileaction;

import org.eclipse.ui.IWorkbenchWindow;

import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.data.Preferences;
import com.loadtrend.app.mobile.data.PreferencesConstant;

public class SaveSMSAction extends SendSaveSMSAction
{
    public SaveSMSAction(IWorkbenchWindow window) {
        super(window);
    }
    
	public boolean performUIBeforeNonUI()
    {
        smsMemoType = Preferences.getValue( PreferencesConstant.SMS_MEMO_TYPE );
        if ( smsMemoType.equals( "" ) )
        {
            super.performUIBeforeNonUI();
            super.localSaveSMS();
            super.performUIAfterNonUI();
            return false;
        }
        else
        {
            return super.performUIBeforeNonUI();
        }
    }

    public void performNonUI()
	{
		try
		{
            super.saveSMS();
		}
		catch ( Exception e )
		{
			super.saveError( Messages.getString( MessagesConstant.SAVESMS_FALURE_MESSAGEBOX_TITLE ), null, e );
		}
	}
}
