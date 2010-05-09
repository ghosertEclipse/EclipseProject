package com.loadtrend.app.test.soundrecorder.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.ui.forms.widgets.Form;

public class RecordingRadioUrlItem {
	
	private RecordingSection section = null;
	
	private Browser browser = null;
	
	private static final String imageLocation = System.getProperty("user.dir") + "/images/internet_radio.jpg";
	
	public RecordingRadioUrlItem(RecordingSection section) {
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
	
	public void initBrowser() {
		this.browser.setText(this.getContent("Prepare to show the radio url which is filled in scheduled recording.",
				                             imageLocation));
	}
	
	public void setUrl(final String url) {
		this.browser.setText(this.getContent("Loading..." + url, imageLocation));
				                             
		this.browser.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) {
			}
			public void completed(ProgressEvent event) {
		        browser.setUrl(url);
		        browser.removeProgressListener(this);
			}
		});
	}
	
	private String getContent(String description, String imageLocation) {
		return "<html><head><body><table height=\"300\"><tr><td width=\"30%\"><IMG height=\"300\" SRC=\"" +
               imageLocation + "\"></td><td><font face=\"verdana\" size=\"1\">" +
               description + "</font></td></tr></table></body></head></html>";
	}
}
