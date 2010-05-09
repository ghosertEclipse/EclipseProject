package com.loadtrend.app.mobile.mobileaction;

import loadtrend.mobile.Mobile;

public class ExportPBActionToSIM extends ExportPBAction {
    public void run() {
        super.pbMemoType = Mobile.PB_SM_MEMO;
        super.run();
    }
}
