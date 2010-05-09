package com.loadtrend.app.mobile.views.smsArrival;

import org.dom4j.Element;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;

public abstract class RightSectionClient {
            protected Button btConfirm = null;
    
    protected Button btCancel = null;
    
    protected Label lbInputStatus = null;
    
    public void build(Element element, Composite rightSectionClient, FormToolkit kit) {
        
        // Create the different content with the different element.
        SelectionAdapter adapter = this.createContentByElement(element, rightSectionClient, kit);
        
        // Create a spacer that take 2 columns.
        lbInputStatus = this.createlbInputStatus(kit, rightSectionClient, "", 3);
        
        btConfirm = kit.createButton(rightSectionClient, Messages.getString(MessagesConstant.SMSArrivalView_NODESETTING_CONFIRM), SWT.PUSH);
        btConfirm.addSelectionListener(adapter);
        btCancel = kit.createButton(rightSectionClient, Messages.getString(MessagesConstant.SMSArrivalView_NODESETTING_CANCEL), SWT.PUSH);
    }
    
    /**
     * Create the different content with the different element.
     * @param element
     * @param rightSectionClient
     * @param kit
     * @return SelectionAdapter
     */
    protected abstract SelectionAdapter createContentByElement(Element element, Composite rightSectionClient, FormToolkit kit);
    
    protected Label createlbInputStatus(FormToolkit toolkit, Composite parent, String text, int span) {
        Label label = toolkit.createLabel(parent, text); //$NON-NLS-1$
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = span;
        label.setLayoutData(gd);
        label.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
        return label;
    }
}
