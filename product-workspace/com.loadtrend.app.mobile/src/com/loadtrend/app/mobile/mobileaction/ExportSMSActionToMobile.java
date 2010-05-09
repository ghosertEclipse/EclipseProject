package com.loadtrend.app.mobile.mobileaction;

import loadtrend.mobile.Mobile;

public class ExportSMSActionToMobile extends ExportSMSAction {
    public void run() {
        super.smsMemoType = Mobile.SMS_ME_MEMO;
        super.run();
    }
}
