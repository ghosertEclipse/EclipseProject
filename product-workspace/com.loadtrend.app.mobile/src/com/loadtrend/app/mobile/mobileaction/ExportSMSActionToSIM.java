package com.loadtrend.app.mobile.mobileaction;

import loadtrend.mobile.Mobile;

public class ExportSMSActionToSIM extends ExportSMSAction {
    public void run() {
        super.smsMemoType = Mobile.SMS_SM_MEMO;
        super.run();
    }
}
