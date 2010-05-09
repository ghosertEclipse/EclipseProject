package com.loadtrend.app.mobile.mobileaction;

import org.eclipse.ui.IWorkbenchWindow;

import loadtrend.mobile.Message;
import loadtrend.mobile.Mobile;

import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.util.SpecialEntity;
import com.loadtrend.app.mobile.views.SMSSendView;

public class QuickSendSMSAction extends MobileAction
{
	private String targetNumber = null;
	
	private String content = null;
    
    private SMSSendView sendView = null;
	
	public QuickSendSMSAction(IWorkbenchWindow window, SMSSendView sendView, String targetNumber, String content) {
        super.window = window;
        this.sendView = sendView;
        this.targetNumber = targetNumber;
        this.content = content;
	}
	
	public boolean performUIBeforeNonUI()
	{
        this.sendView.changeStatus(false);
		return true;
	}

	public void performNonUI()
	{
		Mobile mobile = super.mobile;
		Message message = new Message();
		message.setTeleNum( targetNumber );
		message.setContent( content );
		
		try
		{
            content = content.length() < 15 ? content : content.substring( 0, 15 );
            content = SpecialEntity.encode( content );
            super.showRealTimeInfo( Messages.getString( MessagesConstant.QUICKSEND_SMS_ACTION_INFO, content, targetNumber ) );
			mobile.sendMessage( message );
		}
		catch ( Exception e )
		{
			super.saveError( Messages.getString( MessagesConstant.QUICKSEND_SMS_FAILURE_MESSAGEBOX_TITLE ), null, e );
		}
	}

	public void performUIAfterNonUI() {
        this.sendView.changeStatus(true);
	}

}
