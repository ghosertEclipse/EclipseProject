package com.loadtrend.app.test.soundrecorder.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.ui.forms.widgets.Form;
	
public class RecordingAboutItem {
	
	private RecordingSection section = null;
	
	private Browser browser = null;
	
	private static final String imageLocation = System.getProperty("user.dir") + "/images/internet_radio.jpg";
	
	private static final String purchaseImage = System.getProperty("user.dir") + "/images/purchase.gif";
	
	public RecordingAboutItem(RecordingSection section) {
		this.section = section;
	}
	
	public CTabItem createItem() {
        // recording schedule radio url item
		CTabItem radioUrlItem = new CTabItem(section.tabFolder, SWT.NONE);
		
		Form radioUrlItemForm = section.kit.createForm(section.tabFolder);
		radioUrlItem.setControl(radioUrlItemForm);
		radioUrlItemForm.getBody().setLayout(new FillLayout());
		
		this.browser = new Browser(radioUrlItemForm.getBody(), SWT.NONE);
		
		this.initBrowser();
		
		return radioUrlItem;
	}
	
	private void initBrowser() {
		this.browser.setText(this.getContent(imageLocation, purchaseImage));
	}
	
	private String getContent(String imageLocation, String purchaseImage) {
		return "<html><head><body><table height=\"300\" cellspacing=\"0\">" +
		"<tr><td width=\"30%\"><IMG height=\"300\" SRC=\"" + imageLocation + "\"></td>" +
		"<td><table height=\"160\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">" +
		
//		"<tr height=\"20\"><td><font face=\"verdana\" size=\"1\">You are using the trial version which is limited to 60 seconds and save-disabled after editing sound. <br><br>Press the button below to get the full version.</font></td></tr>" +
//		"<tr height=\"40\" align=\"center\" valign=\"bottom\"><td><br><a href=\"http://www.recordsound.net/buynow.htm\" target=\"_blank\"><IMG SRC=\"" + purchaseImage + "\" BORDER=\"0\"></a></td></tr>" +

		"<tr height=\"20\"><td><font face=\"verdana\" size=\"1\"><br>Version Info:  4.56</font></td></tr>" +
		"<tr height=\"20\"><td><font face=\"verdana\" size=\"1\"><br>Support Email: <a href=\"mailto:ghosert@gmail.com\">ghosert@gmail.com</a></font></td></tr>" +
		"<tr height=\"20\"><td><font face=\"verdana\" size=\"1\"><br>Official Site: <a href=\"http://www.recordsound.net\" target=\"_blank\">http://www.recordsound.net</a></font></td></tr>" +
		"<tr height=\"20\"><td><font face=\"verdana\" size=\"1\"><br>2001-2007 Windows Audio Recorder designed by arbiter, all rights reserved.</font></td></tr>" +
		"</table></td></tr>" +
		"</table></body></head></html>";
	}
}
