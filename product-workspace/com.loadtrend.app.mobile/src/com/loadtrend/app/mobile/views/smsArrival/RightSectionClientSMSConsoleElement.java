package com.loadtrend.app.mobile.views.smsArrival;

import org.dom4j.Element;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.loadtrend.app.mobile.control.FormEntry;
import com.loadtrend.app.mobile.control.IFormEntryAdapter;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.data.Preferences;
import com.loadtrend.app.mobile.data.PreferencesConstant;
import com.loadtrend.app.mobile.util.MobileXMLUtil;

public class RightSectionClientSMSConsoleElement extends RightSectionClient {
    public SelectionAdapter createContentByElement(final Element element, Composite rightSectionClient, FormToolkit kit) {
        
        final String yes = Messages.getString(MessagesConstant.SMSArrivalView_COMBO_YES);
        String no = Messages.getString(MessagesConstant.SMSArrivalView_COMBO_NO);

        // Create FormEntry with enable combo control.
        String labelName = Messages.getString(MessagesConstant.SMSArrivalView_ENABLE);
        final FormEntry enable = new FormEntry(rightSectionClient, kit, labelName, null, false, FormEntry.COMBO_TYPE);
        enable.setComboValues(new String[]{yes, no});
        if (element.attributeValue(MobileXMLUtil.ENABLE).equalsIgnoreCase("true")) {
            enable.setValue(yes);
        } else {
            enable.setValue(no);
        }
        // Create FormEntry with enable combo control.
        labelName = Messages.getString(MessagesConstant.SMSArrivalView_RESULTRETURNABLE);
        final FormEntry resultReturnable = new FormEntry(rightSectionClient, kit, labelName, null, false, FormEntry.COMBO_TYPE);
        resultReturnable.setComboValues(new String[]{yes, no});
        if (element.attributeValue(MobileXMLUtil.RESULTRETURNABLE).equalsIgnoreCase("true")) {
            resultReturnable.setValue(yes);
        } else {
            resultReturnable.setValue(no);
        }
        // Create FormEntry with prompt text control.
        labelName = Messages.getString(MessagesConstant.SMSArrivalView_CONSOLECOMMAND);
        final FormEntry consoleCommand = new FormEntry(rightSectionClient, kit, labelName, null, false, FormEntry.TEXT_TYPE);
        consoleCommand.setValue(element.attributeValue(MobileXMLUtil.CONSOLECOMMAND));
        
        IFormEntryAdapter adapter = new IFormEntryAdapter() {
            public void textValueChanged(FormEntry entry) {
                if (consoleCommand.getValue() == null || consoleCommand.getValue().trim().equals("")) {
                    lbInputStatus.setText(consoleCommand.getLabelText() + Messages.getString(MessagesConstant.SMSArrivalView_TEXTBOX_MANDATORY));
                    btConfirm.setEnabled(false);
                    return;
                }
                lbInputStatus.setText("");
                btConfirm.setEnabled(true);
            }
        };
        consoleCommand.setFormEntryListener(adapter);
        
        // settings description
        String mobileNumber = Preferences.getValue(PreferencesConstant.MOBILE_NUMBER);
        mobileNumber = mobileNumber == null ? "" : mobileNumber;
        final String queryReplyString = Messages.getString(MessagesConstant.SMSArrivalView_EXECUTECMD_STRING, consoleCommand.getValue(), mobileNumber);
        final Label lbQueryReply = super.createlbInputStatus(kit, rightSectionClient, queryReplyString, 2);
        
        // Create adapter for btConfirm button.
        return new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                String enableValue = "false";
                if (enable.getCombo().getText().equalsIgnoreCase(yes)) enableValue = "true";
                String resultReturnableValue = "false";
                if (resultReturnable.getCombo().getText().equalsIgnoreCase(yes)) resultReturnableValue = "true";
                MobileXMLUtil.modifyElement(element, MobileXMLUtil.ENABLE, enableValue);
                MobileXMLUtil.modifyElement(element, MobileXMLUtil.RESULTRETURNABLE, resultReturnableValue);
                MobileXMLUtil.modifyElement(element, MobileXMLUtil.CONSOLECOMMAND, consoleCommand.getValue());
                if (!lbQueryReply.isDisposed()) {
                    lbQueryReply.setText(queryReplyString);
                }
            }
        };
    }
}
