package com.loadtrend.app.mobile.mobileaction;

import loadtrend.mobile.StartOrStopMessageArrivalListener;

import org.eclipse.swt.widgets.Button;
import org.eclipse.ui.IWorkbenchWindow;

import com.loadtrend.app.mobile.arrivalaction.NewMessageArrivalListener;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.util.MobileXMLUtil;
import com.loadtrend.app.mobile.views.smsArrival.SMSArrivalViewListener;


public class StartSMSArrivalListenerAction extends MobileAction {

    private NewMessageArrivalListener messageArrivalListener = null;
    
    private StartOrStopMessageArrivalListener startOrStopMessageArrivalListener = null;
    
    private SMSArrivalViewListener viewListener = null;
    
    private Button checkBox = null;
    
    private boolean isStart = false;
    
    private boolean isStarted = false;
    
    public void performNonUI() {
        try {
            if (isStart) {
                super.mobile.addMessageArrivalListener(messageArrivalListener);
                super.mobile.startMessageArrivalWorkThread(MobileXMLUtil.getInitMessageArrivalWorkThreadCount(), startOrStopMessageArrivalListener);
            } else {
                super.mobile.stopMessageArrivalWorkThread();
                super.mobile.removeAllMessageArrivalListener();
            }
        } catch ( Exception e ) {
            super.saveError(Messages.getString(MessagesConstant.START_NEWMESSAGE_ARRIVAL_LISTENER_FALURE_MESSAGEBOX_TITLE), null, e);
        }
    }

    public void performUIAfterNonUI() {
    }

    public boolean performUIBeforeNonUI() {
        this.isStart = this.checkBox.getSelection();
        if (isStart) {
            // starting...
            viewListener.startOrStopMonitor(true, true);
        }
        return true;
    }
    
    public StartSMSArrivalListenerAction(final IWorkbenchWindow window, final Button checkBox, SMSArrivalViewListener listener) {
        super.window = window;
        // popup mobile process prompt when mobile is processing other request.
        super.mobileProcessingPrompt = true;
        this.checkBox = checkBox;
        this.messageArrivalListener = new NewMessageArrivalListener(listener);
        this.viewListener = listener;
        this.startOrStopMessageArrivalListener = new StartOrStopMessageArrivalListener() {
            public void startMessageArrival() {
                viewListener.startOrStopMonitor(false, true);
                window.getShell().getDisplay().syncExec(new Runnable() {
                    public void run() {
                        checkBox.setSelection(true);
                        isStart = true;
                    }
                });
            }
            public void stopMessageArrival() {
                viewListener.startOrStopMonitor(false, false);
                window.getShell().getDisplay().syncExec(new Runnable() {
                    public void run() {
                        checkBox.setSelection(false);
                        isStart = false;
                    }
                });
            }
        };
    }

    public boolean isStarted() {
        return isStarted;
    }
}
