package com.loadtrend.app.mobile.views.smsArrival;

import org.dom4j.Element;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;

public class SMSArrivalViewRightSection implements ISelectionListener {

    private FormToolkit kit = null;
    
    private Form form = null;
    
    private Section rightSection = null;
    
    private GridLayout rightSectionClientGridLayout = null;
    
    public SMSArrivalViewRightSection(IWorkbenchWindow window, FormToolkit kit, Form form) {
        this.kit = kit;
        this.form = form;
        window.getSelectionService().addSelectionListener(this);
    }
    
    public void createRightSection() {
        // Right section define./////////////////////////////////////////////////////////////////////////
        rightSection = kit.createSection(form.getBody(), Section.TITLE_BAR | Section.DESCRIPTION | Section.TWISTIE | Section.EXPANDED);
        rightSection.setText(Messages.getString(MessagesConstant.SMSArrivalView_RIGHTSECTION_TEXT));
        // rightSection.setDescription("right section description");
        rightSection.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        // define section client.
        rightSectionClientGridLayout = new GridLayout(3, false);
        rightSectionClientGridLayout .verticalSpacing = 10;
        rightSectionClientGridLayout.marginTop = 5;
    }
    
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        
        // Produce the new child controls for the right section client.
        if (!(part instanceof SMSArrivalView)) return;
        IStructuredSelection structuredSelection = (IStructuredSelection) selection;        if (!(structuredSelection.getFirstElement() instanceof Element)) return;
        Element element = (Element) structuredSelection.getFirstElement();

        
        // Dispose the old right section client and produce the new one.
        if (rightSection.getClient() != null ) rightSection.getClient().dispose();
        Composite rightSectionClient = kit.createComposite(rightSection);
        rightSectionClient.setLayout(rightSectionClientGridLayout);
        rightSection.setClient(rightSectionClient);
        kit.paintBordersFor(rightSection);               
        
        // Create the different right section client with different element.
        RightSectionClientFactory factory = RightSectionClientFactory.getInstance();
        factory.createRightSectionClient(element, rightSectionClient, kit);

        // Flush the layout to refresh the composite.
        rightSectionClient.layout(true) ;
        rightSection.layout(true);
        rightSection.getParent().layout(true);
      
        // The nice interface.
        kit.paintBordersFor(rightSectionClient);
    }
}
