package com.loadtrend.app.mobile.views.smsArrival;

import org.dom4j.Element;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.loadtrend.app.mobile.control.FormEntry;
import com.loadtrend.app.mobile.control.IFormEntryAdapter;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.util.MobileXMLUtil;

public class RightSectionClientSettingsElement extends RightSectionClient {
    public SelectionAdapter createContentByElement(final Element element, Composite rightSectionClient, FormToolkit kit) {
        // Create FormEntry with errorprompt combo control.
        String labelName = Messages.getString(MessagesConstant.SMSArrivalView_ERRORPROMPT);
        final FormEntry errorPrompt = new FormEntry(rightSectionClient, kit, labelName, null, false, FormEntry.COMBO_TYPE);
        final String yes = Messages.getString(MessagesConstant.SMSArrivalView_COMBO_YES);
        String no = Messages.getString(MessagesConstant.SMSArrivalView_COMBO_NO);
        errorPrompt.setComboValues(new String[]{yes, no});
        if (element.attributeValue(MobileXMLUtil.ERRORPROMPT).equalsIgnoreCase("true")) {
            errorPrompt.setValue(yes);
        } else {
            errorPrompt.setValue(no);
        }
        // Create FormEntry with prompt text control.
        labelName = Messages.getString(MessagesConstant.SMSArrivalView_PROMPT);
        final FormEntry prompt = new FormEntry(rightSectionClient, kit, labelName, null, false, FormEntry.TEXT_TYPE);
        prompt.setValue(element.attributeValue(MobileXMLUtil.PROMPT));
        // Create FormEntry with prompt text control.
        labelName = Messages.getString(MessagesConstant.SMSArrivalView_CONTINUEPROMPT);
        final FormEntry continuePrompt = new FormEntry(rightSectionClient, kit, labelName, null, false, FormEntry.TEXT_TYPE);
        continuePrompt.setValue(element.attributeValue(MobileXMLUtil.CONTINUEPROMPT));
        // Create FormEntry with prompt text control.
        labelName = Messages.getString(MessagesConstant.SMSArrivalView_CONTINUECOMMAND);
        final FormEntry continueCommand = new FormEntry(rightSectionClient, kit, labelName, null, false, FormEntry.TEXT_TYPE);
        continueCommand.setValue(element.attributeValue(MobileXMLUtil.CONTINUECOMMAND));
        // Create FormEntry with prompt text control.
        labelName = Messages.getString(MessagesConstant.SMSArrivalView_NUMBEROFTHREAD);
        final FormEntry numberOfThread = new FormEntry(rightSectionClient, kit, labelName, null, false, FormEntry.TEXT_TYPE);
        numberOfThread.setValue(element.attributeValue(MobileXMLUtil.NUMBEROFTHREAD));
        // Create FormEntry with prompt text control.
        labelName = Messages.getString(MessagesConstant.SMSArrivalView_RESTARTTHREADCOMMAND);
        final FormEntry restartThreadCommand = new FormEntry(rightSectionClient, kit, labelName, null, false, FormEntry.TEXT_TYPE);
        restartThreadCommand.setValue(element.attributeValue(MobileXMLUtil.RESTARTTHREADCOMMAND));
        // Create FormEntry with prompt text control.
        labelName = Messages.getString(MessagesConstant.SMSArrivalView_RESTARTALLTHREADCOMMAND);
        final FormEntry restartAllThreadCommand = new FormEntry(rightSectionClient, kit, labelName, null, false, FormEntry.TEXT_TYPE);
        restartAllThreadCommand.setValue(element.attributeValue(MobileXMLUtil.RESTARTALLTHREADCOMMAND));
        // Create FormEntry with prompt text control.
        labelName = Messages.getString(MessagesConstant.SMSArrivalView_DELETEAFTERARRIVAL);
        final FormEntry deleteAfterArrival = new FormEntry(rightSectionClient, kit, labelName, null, false, FormEntry.COMBO_TYPE);
        deleteAfterArrival.setComboValues(new String[]{yes, no});
        if (element.attributeValue(MobileXMLUtil.DELETEAFTERARRIVAL).equalsIgnoreCase("true")) {
            deleteAfterArrival.setValue(yes);
        } else {
            deleteAfterArrival.setValue(no);
        }
        
        IFormEntryAdapter adapter = new IFormEntryAdapter() {
            public void textValueChanged(FormEntry entry) {
                if (prompt.getValue() == null || prompt.getValue().trim().equals("")) {
                    lbInputStatus.setText(prompt.getLabelText() + Messages.getString(MessagesConstant.SMSArrivalView_TEXTBOX_MANDATORY));
                    btConfirm.setEnabled(false);
                    return;
                } 
                if (continueCommand.getValue() == null || continueCommand.getValue().trim().equals("")) {
                    lbInputStatus.setText(continueCommand.getLabelText() + Messages.getString(MessagesConstant.SMSArrivalView_TEXTBOX_MANDATORY));
                    btConfirm.setEnabled(false);
                    return;
                } 
                if (continuePrompt.getValue() == null || continuePrompt.getValue().trim().equals("")) {
                    lbInputStatus.setText(continuePrompt.getLabelText() + Messages.getString(MessagesConstant.SMSArrivalView_TEXTBOX_MANDATORY));
                    btConfirm.setEnabled(false);
                    return;
                } 
                if (restartThreadCommand.getValue() == null || restartThreadCommand.getValue().trim().equals("")) {
                    lbInputStatus.setText(restartThreadCommand.getLabelText() + Messages.getString(MessagesConstant.SMSArrivalView_TEXTBOX_MANDATORY));
                    btConfirm.setEnabled(false);
                    return;
                } 
                if (restartAllThreadCommand.getValue() == null || restartAllThreadCommand.getValue().trim().equals("")) {
                    lbInputStatus.setText(restartAllThreadCommand.getLabelText() + Messages.getString(MessagesConstant.SMSArrivalView_TEXTBOX_MANDATORY));
                    btConfirm.setEnabled(false);
                    return;
                } 

                if (numberOfThread.getValue() == null || !numberOfThread.getValue().matches("[0-9]+")) {
                    lbInputStatus.setText(numberOfThread.getLabelText() + Messages.getString(MessagesConstant.SMSArrivalView_TEXTBOX_NUMBER_MANDATORY));
                    btConfirm.setEnabled(false);
                    return;
                } 
                lbInputStatus.setText("");
                btConfirm.setEnabled(true);
            }
        };
        prompt.setFormEntryListener(adapter);
        continueCommand.setFormEntryListener(adapter);
        continuePrompt.setFormEntryListener(adapter);
        numberOfThread.setFormEntryListener(adapter);
        restartThreadCommand.setFormEntryListener(adapter);
        restartAllThreadCommand.setFormEntryListener(adapter);
        
        // Create adapter for btConfirm button.
        return new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                String errorPromptValue = "false";
                if (errorPrompt.getCombo().getText().equalsIgnoreCase(yes)) errorPromptValue = "true";
                String deleteAfterArrivalValue = "false";
                if (deleteAfterArrival.getCombo().getText().equalsIgnoreCase(yes)) deleteAfterArrivalValue = "true";
                MobileXMLUtil.modifyElement(element, MobileXMLUtil.ERRORPROMPT, errorPromptValue);
                MobileXMLUtil.modifyElement(element, MobileXMLUtil.PROMPT, prompt.getValue());
                MobileXMLUtil.modifyElement(element, MobileXMLUtil.CONTINUECOMMAND, continueCommand.getValue());
                MobileXMLUtil.modifyElement(element, MobileXMLUtil.CONTINUEPROMPT, continuePrompt.getValue());
                MobileXMLUtil.modifyElement(element, MobileXMLUtil.NUMBEROFTHREAD, numberOfThread.getValue());
                MobileXMLUtil.modifyElement(element, MobileXMLUtil.RESTARTTHREADCOMMAND, restartThreadCommand.getValue());
                MobileXMLUtil.modifyElement(element, MobileXMLUtil.RESTARTALLTHREADCOMMAND, restartAllThreadCommand.getValue());
                MobileXMLUtil.modifyElement(element, MobileXMLUtil.DELETEAFTERARRIVAL, deleteAfterArrivalValue);
            }
        };
    }
}
