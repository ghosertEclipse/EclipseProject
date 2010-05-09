package com.loadtrend.app.mobile.mobileaction;

import java.text.MessageFormat;

import loadtrend.mobile.Message;
import loadtrend.mobile.Mobile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;

import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.util.SpecialEntity;

public class QuickSendSMSToDownloadMediaAction extends MobileAction {

    private String targetNumber = null;
    
    private String content = null;
    
    private String description = null;
    
    private String friendNumber = null;
    
    /**
     * Send a SMS to download picture or ringtone to your mobile or friend's mobile.
     * @param window
     * @param targetNumber long code.
     * @param friendNumber friend's mobile number, friendNumber.trim() will be handled, null is acceptable.
     * @param content sms content.
     * @param description picture or ringtone description.
     */
    public QuickSendSMSToDownloadMediaAction(IWorkbenchWindow window, String targetNumber, String friendNumber, String content, String description) {
        super.window = window;
        // popup mobile process prompt when mobile is processing other request.
        super.mobileProcessingPrompt = true;
        this.targetNumber = targetNumber;
        this.content = content;
        this.description = description;
        this.friendNumber = friendNumber;
    }
    
    public boolean performUIBeforeNonUI() {
        return true;
    }

    public void performNonUI()
    {
        Mobile mobile = super.mobile;
        Message message = new Message();
        message.setTeleNum( targetNumber );
        message.setContent( friendNumber == null ? content : content + " " + friendNumber.trim());
        
        try {
            content = content.length() < 15 ? content : content.substring( 0, 15 );
            content = SpecialEntity.encode( content );
            super.showRealTimeInfo( Messages.getString( MessagesConstant.ResourceEditor_DOWNLOADPROMPT, description));
            mobile.sendMessage( message );
        } catch ( Exception e ) {
            super.saveError( Messages.getString( MessagesConstant.QUICKSEND_SMS_FAILURE_MESSAGEBOX_TITLE ), null, e );
        }
    }

    public void performUIAfterNonUI() {
    }

    protected void showErrorInNonUI() {
        if (super.exception instanceof IllegalStateException) {
            MessageBox messageBox = new MessageBox(window.getShell(), SWT.OK | SWT.ICON_INFORMATION);
            if (this.friendNumber == null) {
	            messageBox.setText("������ʾ");
	            messageBox.setMessage(MessageFormat.format("�����ֻ�δͨ�����ڣ�������������ͱ����������Ч���ӣ��޷���ɴ��䡣\r\n\r\n��������Դ������Ϊ {0} �������ֻ����Ͷ��� {0} �� 57572194 �� ��{1}�� ֱ�������������ֻ���", new String[] {this.content, this.description}));
            } else {
	            messageBox.setText("������ʾ");
	            messageBox.setMessage(MessageFormat.format("�����ֻ�û�кͱ����������Ч����,���Ͷ��ţ���š��ո񡿺����ֻ����룢����57572194��������͡�\r\n\r\n��������Դ������Ϊ {0} �������ֻ����Ͷ��� {1} �� 57572194 �� ��{2}�� ֱ�����͸����ĺ��ѡ�", new String[] {this.content, this.content + " " + this.friendNumber.trim(), this.description}));
            }
            messageBox.open();
        } else {
            super.showErrorInNonUI();
        }
    }
    
}
