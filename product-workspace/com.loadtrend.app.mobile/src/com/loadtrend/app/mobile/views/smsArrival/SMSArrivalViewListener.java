package com.loadtrend.app.mobile.views.smsArrival;

import loadtrend.mobile.Message;

public interface SMSArrivalViewListener {
    /**
     * Refresh the monitor text.
     * @param message the new message arrived.
     * @param receiveSMS the SMS type: received or sent for true or false.
     */
    public abstract void refreshMonitorText(Message message, boolean receiveSMS) throws Exception;
    
    /**
     * Pop up the prompt window.
     * @param message the new message arrivaed.
     */
    public abstract void popupWindow(Message message);
    
    public abstract void startOrStopMonitor(boolean isStarting, boolean isStartOrStop);
    
    public abstract void deleteMessageLog(Message message);
}
