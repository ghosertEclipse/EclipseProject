package com.loadtrend.app.mobile.netaction;

import com.loadtrend.app.mobile.data.Preferences;
import com.loadtrend.app.mobile.data.PreferencesConstant;

public class UserRecorderAction extends NetAction {
	
	protected void netExecute() throws Exception {
	}

	protected void performUIAfterNetExecute() {
	}
	
	public void run() {
		new Thread() {
			public void run() {
				try {
				    String mobileNumber = Preferences.getValue(PreferencesConstant.MOBILE_NUMBER);
				    UserRecorderAction.super.jMobileClientManager.saveUserRecorder(mobileNumber);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}.start();
	}
}
