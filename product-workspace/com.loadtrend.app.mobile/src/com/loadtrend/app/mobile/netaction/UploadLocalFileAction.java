package com.loadtrend.app.mobile.netaction;

import java.io.IOException;

import org.eclipse.ui.IWorkbenchWindow;

import com.loadtrend.app.mobile.mobileaction.QuickSendSMSToDownloadMediaAction;

public class UploadLocalFileAction extends NetAction {
    
    private String filename = null;
    
    private byte[] bytes  = null;
    
    private String itemid = null;
    
    private boolean isSendToFriend = false;
    
    private String friendNumber = null;
    
    /**
     * Upload the file.
     * @param window
     * @param filename
     * @param bytes
     * @param isSendToFriend
     * @param friendNumber
     */
    public UploadLocalFileAction(IWorkbenchWindow window, String filename, byte[] bytes, boolean isSendToFriend, String friendNumber) {
        super.window = window;
        this.filename = filename;
        this.bytes = bytes;
        this.isSendToFriend = isSendToFriend;
        this.friendNumber = friendNumber;
    }

    protected void netExecute() throws IOException {
        super.showRealTimeInfo("正在上传文件：" + filename);
        this.itemid = super.jMobileClientManager.uploadLocalFile(filename, bytes).getId();
    }

    protected void performUIAfterNetExecute() {
        new QuickSendSMSToDownloadMediaAction(super.window, "57572194", isSendToFriend ? friendNumber : null, itemid, filename).run();
    }
}
