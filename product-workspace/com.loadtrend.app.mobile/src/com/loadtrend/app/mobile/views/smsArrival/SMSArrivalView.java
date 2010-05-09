package com.loadtrend.app.mobile.views.smsArrival;

import org.dom4j.Element;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.mobileaction.StartSMSArrivalListenerAction;
import com.loadtrend.app.mobile.util.MobileXMLModifyListener;
import com.loadtrend.app.mobile.util.MobileXMLUtil;

public class SMSArrivalView extends ViewPart {

    public static final String VIEW_ID = "com.loadtrend.app.mobile.views.smsArrival.SMSArrivalView";
    
    private TreeViewer treeViewer = null;
    
    private Button checkbox = null;
    
    private Color white = null;
    
    private Color red = null;
    
    public void createPartControl( Composite parent )
    {
        this.setPartName( Messages.getString( MessagesConstant.SMSArrivalView_PARTNAME ) );

        white = parent.getDisplay().getSystemColor(SWT.COLOR_WHITE);
        red = parent.getDisplay().getSystemColor(SWT.COLOR_DARK_RED);
        
        final FormToolkit kit = new FormToolkit( parent.getDisplay() );
        final Form form = kit.createForm( parent );
        GridLayout gridLayout = new GridLayout(2, true);
        gridLayout.horizontalSpacing = 20;
        gridLayout.marginTop = 5;
        form.getBody().setLayout(gridLayout);
        
        // Left section define.
        Section leftSection = kit.createSection(form.getBody(), Section.TITLE_BAR | Section.DESCRIPTION | Section.TWISTIE | Section.EXPANDED);
        leftSection.setText(Messages.getString(MessagesConstant.SMSArrivalView_LEFTSECTION_TEXT));
        // leftSection.setDescription("left section description");
        leftSection.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        // define section client.
        Composite leftSectionClient = kit.createComposite(leftSection);
        gridLayout = new GridLayout(1, false);
        gridLayout.verticalSpacing = 10;
        gridLayout.marginTop = 5;
        leftSectionClient.setLayout(gridLayout);
        
        // define checkbox.
        this.checkbox = kit.createButton(leftSectionClient, Messages.getString(MessagesConstant.SMSArrivalView_ENABLESMSARRIVAL_LISTENER), SWT.CHECK);
        checkbox.setForeground(red);
        checkbox.setBackground(white);
        checkbox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Define tree viewer.
        Tree tree = kit.createTree(leftSectionClient, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
        treeViewer = new TreeViewer(tree); 
        
        // For the action set desgin in the plugin.xml can be affected by the treeViewer.
        getSite().setSelectionProvider(treeViewer);
        treeViewer.setAutoExpandLevel( TreeViewer.ALL_LEVELS );
        treeViewer.setContentProvider( new SMSArrivalViewContentProvider() );
        treeViewer.setLabelProvider( new SMSArrivalViewLabelProvider() );
        treeViewer.setInput(MobileXMLUtil.getRootElement());
        treeViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        
        // Refresh the SMSArrivalView when the mobile xml modified.
        MobileXMLUtil.addMobileXMLModifyListener(new MobileXMLModifyListener() {
            public void mobileXMLModified(Element element) {
                SMSArrivalView.this.refresh(element);
            }
        });
        
        hookContextMenu();
        
        leftSection.setClient(leftSectionClient);
        kit.paintBordersFor(leftSection);
        
        
        // Create the Right Section for SMSArrivalView
        new SMSArrivalViewRightSection(getSite().getWorkbenchWindow(), kit, form).createRightSection();
        
        // Get monitorText from SMS Arrival View Log viewpart
        SMSArrivalViewLog smsArrivalViewLog = null;
        try {
             smsArrivalViewLog = (SMSArrivalViewLog) this.getSite().getPage().showView(SMSArrivalViewLog.VIEW_ID);
        } catch (PartInitException e1) {
            e1.printStackTrace();
        }
        SMSArrivalViewListener listener = smsArrivalViewLog.getListener();

        // Start or Stop SMS Arrival Listener.
        final StartSMSArrivalListenerAction startSMSArrivalListenerAction = new StartSMSArrivalListenerAction(super.getSite().getWorkbenchWindow(), checkbox, listener);
        checkbox.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                startSMSArrivalListenerAction.run();
                checkbox.setSelection(startSMSArrivalListenerAction.isStarted());
            }
        });
    }        

    
    private void hookContextMenu()
    {
        // Create the context menu and register it with the Workbench.
        MenuManager menuMgr = new MenuManager( "#PopupMenu" );
        Menu menu = menuMgr.createContextMenu( treeViewer.getControl() );
        treeViewer.getControl().setMenu( menu );
        menuMgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        // Design for apply popupMenus extenstion defined in plugin.xml
        getSite().registerContextMenu( menuMgr, treeViewer );
    }
    
    public void setFocus()
    {
    }
    
    public void dispose()
    {
        super.dispose();
        white.dispose();
        red.dispose();
    }
    
    private void refresh(Element element) {
        treeViewer.setInput(MobileXMLUtil.getRootElement());
        treeViewer.setSelection(new StructuredSelection(element));
    }


    public Button getCheckbox() {
        return checkbox;
    }
}
