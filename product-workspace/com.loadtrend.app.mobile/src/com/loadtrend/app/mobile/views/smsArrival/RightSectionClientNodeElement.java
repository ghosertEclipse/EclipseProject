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

public class RightSectionClientNodeElement extends RightSectionClient {

    public SelectionAdapter createContentByElement(final Element element, Composite rightSectionClient, FormToolkit kit) {
       final boolean hasChildNode = element.elements().size() > 0;
        // Create FormEntry with text control.
       String labelName = Messages.getString(MessagesConstant.SMSArrivalView_REPLYCONTENT);
       final FormEntry replyContent = new FormEntry(rightSectionClient, kit, labelName, null, false, FormEntry.TEXT_TYPE);
       replyContent.setValue(element.attributeValue(MobileXMLUtil.VALUE));
       
       labelName = Messages.getString(MessagesConstant.SMSArrivalView_QUERYCODE);
       final FormEntry queryCode = new FormEntry(rightSectionClient, kit, labelName, null, false, FormEntry.TEXT_TYPE);
       queryCode.setValue(element.attributeValue(MobileXMLUtil.CODE));
       
       IFormEntryAdapter adapter = new IFormEntryAdapter() {
           public void textValueChanged(FormEntry entry) {
               if (replyContent.getValue() == null || replyContent.getValue().trim().equals("")) {
                   lbInputStatus.setText(Messages.getString(MessagesConstant.SMSArrivalView_LBINPUTSTATUS_VALUEMANDATORY_TEXT));
                   btConfirm.setEnabled(false);
                   return;
               }
               if ((queryCode.getValue() == null || queryCode.getValue().trim().equals("")) && hasChildNode) {
                   lbInputStatus.setText(Messages
                                    .getString(MessagesConstant.SMSArrivalView_LBINPUTSTATUS_CODEMANDATORY_TEXT));
                    btConfirm.setEnabled(false);
                    return;
                }
                lbInputStatus.setText("");
                btConfirm.setEnabled(true);
            }
        };
        replyContent.setFormEntryListener(adapter);
        queryCode.setFormEntryListener(adapter);

        // Query reply string description.
        String queryReplyString = getQueryReplyString(element);
        final Label lbQueryReply = super.createlbInputStatus(kit, rightSectionClient,
                queryReplyString, 2);

        return new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                MobileXMLUtil.modifyElement(element, MobileXMLUtil.VALUE, replyContent.getValue());
                MobileXMLUtil.modifyElement(element, MobileXMLUtil.CODE, queryCode.getValue());
                if (queryCode.getValue() == null || queryCode.getValue().trim().equals("")) {
                    MobileXMLUtil.modifyElement(element, MobileXMLUtil.ADDUNABLE, MessagesConstant.WARNING_CODE_MANDATORY_FORADDNODE);
                } else {
                    MobileXMLUtil.modifyElement(element, MobileXMLUtil.ADDUNABLE, null);
                }
                if (!lbQueryReply.isDisposed()) {
                lbQueryReply.setText(RightSectionClientNodeElement.this
                                .getQueryReplyString(element));
                }
            }
        };
    }
    
    private String getQueryReplyString(Element element) {
        String queryString = this.getQueryString(element, 0);
        String replyString = element.attributeValue(MobileXMLUtil.VALUE);
        String mobileNumber = Preferences.getValue(PreferencesConstant.MOBILE_NUMBER);
        mobileNumber = mobileNumber == null ? "" : mobileNumber;
        String queryRepyString = Messages.getString(MessagesConstant.SMSArrivalView_QUERYREPLY_STRING, queryString, replyString, mobileNumber);        
        return queryRepyString;
    }
    
    private String getQueryString(Element element, int i) {
        String queryReplyString = "";
        if (!element.getParent().isRootElement()) {
            String valueOrCode = (i==0 ? "" : element.attributeValue(MobileXMLUtil.CODE) + " ");
            queryReplyString = getQueryString(element.getParent(), ++i) + valueOrCode;
            return queryReplyString;
        }
        return "";
    }
}
