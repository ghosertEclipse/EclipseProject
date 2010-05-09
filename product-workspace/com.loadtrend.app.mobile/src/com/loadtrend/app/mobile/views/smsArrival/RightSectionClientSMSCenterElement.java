package com.loadtrend.app.mobile.views.smsArrival;

import org.dom4j.Element;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.loadtrend.app.mobile.control.FormEntry;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.util.MobileXMLUtil;

public class RightSectionClientSMSCenterElement extends RightSectionClient {
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
        // Create adapter for btConfirm button.
        return new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                String enableValue = "false";
                if (enable.getCombo().getText().equalsIgnoreCase(yes)) enableValue = "true";
                MobileXMLUtil.modifyElement(element, MobileXMLUtil.ENABLE, enableValue);
            }
        };
    }
}
