package com.loadtrend.app.mobile.mobileaction;

import loadtrend.mobile.Mobile;

public class ExportPBActionToMobile extends ExportPBAction {
    public void run() {
        super.pbMemoType = Mobile.PB_ME_MEMO;
        super.run();
    }
}
