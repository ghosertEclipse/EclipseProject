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

public class RightSectionClientControlElement extends RightSectionClient {

    public SelectionAdapter createContentByElement(final Element element, final Composite rightSectionClient, FormToolkit kit) {
        // Create FormEntry with text control.
        String labelName = Messages.getString(MessagesConstant.SMSArrivalView_FUNCTIONDESCRIPTION);
        final FormEntry functionDescription = new FormEntry(rightSectionClient, kit, labelName, null, false, FormEntry.TEXT_TYPE);
        functionDescription.setValue(element.attributeValue(MobileXMLUtil.VALUE));
        
        labelName = Messages.getString(MessagesConstant.SMSArrivalView_QUERYCODE);
        final FormEntry queryCode = new FormEntry(rightSectionClient, kit, labelName, null, false, FormEntry.TEXT_TYPE);
        queryCode.setValue(element.attributeValue(MobileXMLUtil.CODE));
        
        labelName = Messages.getString(MessagesConstant.SMSArrivalView_PROGRAM_FILE_PATH);
        String browser = Messages.getString(MessagesConstant.SMSArrivalView_BROWSER);
        final FormEntry commandLine = new FormEntry(rightSectionClient, kit, labelName, browser, false, FormEntry.TEXT_TYPE);
        commandLine.setFormEntryListener(new IFormEntryAdapter() {
            public void browseButtonSelected(FormEntry entry) {
                String pathname = new FileDialog(rightSectionClient.getShell(), SWT.OPEN).open();
                if (pathname != null) entry.setValue(pathname);
            }
        });
        commandLine.setValue(element.attributeValue(MobileXMLUtil.COMMAND));
        
        String editable = element.attributeValue(MobileXMLUtil.EDITABLE);
        if (editable != null && editable.equalsIgnoreCase("false")) {
            commandLine.setEditable(false);
        }
        
        IFormEntryAdapter adapter = new IFormEntryAdapter() {
            public void textValueChanged(FormEntry entry) {
                if ((functionDescription.getValue() == null || functionDescription.getValue().trim().equals(""))) {
                    lbInputStatus.setText(functionDescription.getLabelText() + Messages.getString(MessagesConstant.SMSArrivalView_TEXTBOX_MANDATORY));
                    btConfirm.setEnabled(false);
                    return;
                } 
                if ((queryCode.getValue() == null || queryCode.getValue().trim().equals(""))) {
                    lbInputStatus.setText(queryCode.getLabelText() + Messages.getString(MessagesConstant.SMSArrivalView_TEXTBOX_MANDATORY));
                    btConfirm.setEnabled(false);
                    return;
                } 
                if ((commandLine.getValue() == null || commandLine.getValue().trim().equals(""))) {
                    lbInputStatus.setText(commandLine.getLabelText() + Messages.getString(MessagesConstant.SMSArrivalView_TEXTBOX_MANDATORY));
                    btConfirm.setEnabled(false);
                    return;
                } 
                lbInputStatus.setText("");
                btConfirm.setEnabled(true);
            }
        };
        
        functionDescription.setFormEntryListener(adapter);
        queryCode.setFormEntryListener(adapter);
        commandLine.setFormEntryListener(adapter);
        
        // Query reply string description.
        String mobileNumber = Preferences.getValue(PreferencesConstant.MOBILE_NUMBER);
        mobileNumber = mobileNumber == null ? "" : mobileNumber;
        final String queryReplyString = Messages.getString(MessagesConstant.SMSArrivalView_EXECUTEPROGRAM_STRING, queryCode.getValue(), commandLine.getValue(), mobileNumber);
        final Label lbQueryReply = super.createlbInputStatus(kit, rightSectionClient, queryReplyString, 2);
        return new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                MobileXMLUtil.modifyElement(element, MobileXMLUtil.VALUE, functionDescription.getValue());
                MobileXMLUtil.modifyElement(element, MobileXMLUtil.CODE, queryCode.getValue());
                MobileXMLUtil.modifyElement(element, MobileXMLUtil.COMMAND, commandLine.getValue());
                if (!lbQueryReply.isDisposed()) {
                    lbQueryReply.setText(queryReplyString);
                }
            }
        };
    }

}
