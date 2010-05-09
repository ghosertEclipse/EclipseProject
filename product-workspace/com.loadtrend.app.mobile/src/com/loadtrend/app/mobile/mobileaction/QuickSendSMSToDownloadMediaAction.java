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
	            messageBox.setText("下载提示");
	            messageBox.setMessage(MessageFormat.format("您的手机未通过串口，红外或者蓝牙和本软件建立有效连接，无法完成传输。\r\n\r\n待传输资源分配编号为 {0} 请拿起手机发送短信 {0} 到 57572194 将 【{1}】 直接下载至您的手机。", new String[] {this.content, this.description}));
            } else {
	            messageBox.setText("赠送提示");
	            messageBox.setMessage(MessageFormat.format("您的手机没有和本软件建立有效连接,发送短信＂编号【空格】好友手机号码＂到＂57572194＂完成赠送。\r\n\r\n待赠送资源分配编号为 {0} 请拿起手机发送短信 {1} 到 57572194 将 【{2}】 直接赠送给您的好友。", new String[] {this.content, this.content + " " + this.friendNumber.trim(), this.description}));
            }
            messageBox.open();
        } else {
            super.showErrorInNonUI();
        }
    }
    
}
