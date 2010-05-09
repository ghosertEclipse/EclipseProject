package com.loadtrend.app.mobile.views.smsArrival;

import org.dom4j.Element;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.loadtrend.app.mobile.control.FormEntry;
import com.loadtrend.app.mobile.control.IFormEntryAdapter;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.data.Preferences;
import com.loadtrend.app.mobile.data.PreferencesConstant;
import com.loadtrend.app.mobile.util.MobileXMLUtil;

public class RightSectionClientFileElement extends RightSectionClient {

    protected SelectionAdapter createContentByElement(final Element element,
            final Composite rightSectionClient, FormToolkit kit) {
        // Create FormEntry with text control.
        String labelName = Messages.getString(MessagesConstant.SMSArrivalView_FILEDESCRIPTION);
        final FormEntry fileDescription = new FormEntry(rightSectionClient, kit, labelName, null, false, FormEntry.TEXT_TYPE);
        fileDescription.setValue(element.attributeValue(MobileXMLUtil.VALUE));
        
        labelName = Messages.getString(MessagesConstant.SMSArrivalView_QUERYCODE);
        final FormEntry queryCode = new FormEntry(rightSectionClient, kit, labelName, null, false, FormEntry.TEXT_TYPE);
        queryCode.setValue(element.attributeValue(MobileXMLUtil.CODE));
        
        labelName = Messages.getString(MessagesConstant.SMSArrivalView_PATH);
        String browser = Messages.getString(MessagesConstant.SMSArrivalView_BROWSER);
        final FormEntry path = new FormEntry(rightSectionClient, kit, labelName, browser, false, FormEntry.TEXT_TYPE);
        path.setFormEntryListener(new IFormEntryAdapter() {
            public void browseButtonSelected(FormEntry entry) {
                String pathname = new FileDialog(rightSectionClient.getShell(), SWT.OPEN).open();
                if (pathname != null) entry.setValue(pathname);
            }
        });
        path.setValue(element.attributeValue(MobileXMLUtil.PATH));
        
        IFormEntryAdapter adapter = new IFormEntryAdapter() {
            public void textValueChanged(FormEntry entry) {
                if ((fileDescription.getValue() == null || fileDescription.getValue().trim().equals(""))) {
                    lbInputStatus.setText(fileDescription.getLabelText() + Messages.getString(MessagesConstant.SMSArrivalView_TEXTBOX_MANDATORY));
                    btConfirm.setEnabled(false);
                    return;
                } 
                if ((queryCode.getValue() == null || queryCode.getValue().trim().equals(""))) {
                    lbInputStatus.setText(queryCode.getLabelText() + Messages.getString(MessagesConstant.SMSArrivalView_TEXTBOX_MANDATORY));
                    btConfirm.setEnabled(false);
                    return;
                } 
                if ((path.getValue() == null || path.getValue().trim().equals(""))) {
                    lbInputStatus.setText(path.getLabelText() + Messages.getString(MessagesConstant.SMSArrivalView_TEXTBOX_MANDATORY));
                    btConfirm.setEnabled(false);
                    return;
                } 
                lbInputStatus.setText("");
                btConfirm.setEnabled(true);
            }
        };
        fileDescription.setFormEntryListener(adapter);
        queryCode.setFormEntryListener(adapter);
        path.setFormEntryListener(adapter);
        
        // settings description.
        String queryCodeString = element.getParent().attributeValue(MobileXMLUtil.CODE) + " " + queryCode.getValue();
        String programString = element.getParent().attributeValue(MobileXMLUtil.COMMAND);
        String fileString = path.getValue();
        String mobileNumber = Preferences.getValue(PreferencesConstant.MOBILE_NUMBER);
        mobileNumber = mobileNumber == null ? "" : mobileNumber;
        final String queryReplyString = Messages.getString(MessagesConstant.SMSArrivalView_EXECUTEFILE_STRING,
                                                           queryCodeString,
                                                           programString,
                                                           fileString,
                                                           mobileNumber);
        final Label lbQueryReply = super.createlbInputStatus(kit, rightSectionClient, queryReplyString, 2);
        return new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                MobileXMLUtil.modifyElement(element, MobileXMLUtil.VALUE, fileDescription.getValue());
                MobileXMLUtil.modifyElement(element, MobileXMLUtil.CODE, queryCode.getValue());
                MobileXMLUtil.modifyElement(element, MobileXMLUtil.PATH, path.getValue());
                if (!lbQueryReply.isDisposed()) {
                    lbQueryReply.setText(queryReplyString);
                }
            }
        };
    }

}
