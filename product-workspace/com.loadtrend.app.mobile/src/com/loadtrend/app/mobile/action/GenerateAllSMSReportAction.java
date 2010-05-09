package com.loadtrend.app.mobile.action;

import java.io.File;
import java.util.Collection;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;

import com.loadtrend.app.mobile.data.ImageConstant;
import com.loadtrend.app.mobile.data.ImageLoader;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.data.MobileAppConstant;
import com.loadtrend.app.mobile.util.GenerateSMSReport;
import com.loadtrend.app.trees.TreeObject;
import com.loadtrend.app.trees.TreeParent;

public class GenerateAllSMSReportAction extends Action implements ISelectionListener, ActionFactory.IWorkbenchAction {
    
    private IWorkbenchWindow window = null;
    
    protected TreeObject to = null;
    
    protected Collection smsCollection = null;
    
    protected Menu createMenu(Menu generateSMSReportMenu) {
        MenuItem item = new MenuItem( generateSMSReportMenu, SWT.PUSH );
        item.setText( Messages.getString( MessagesConstant.SMSREPORT_TXT_TYPE ) );
        item.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                FileDialog fileDialog = new FileDialog( window.getShell(), SWT.SAVE );
                fileDialog.setFilterPath( MobileAppConstant.JAVA_ROOT_PATH );
                fileDialog.setFilterExtensions( new String[]{ ".txt" } );
                fileDialog.setFileName( to.getTreeName() );
                
                if ( fileDialog.open() != null )
                {
                    String fileName = fileDialog.getFilterPath() + File.separatorChar + fileDialog.getFileName();
                    GenerateSMSReport.generateTXT( fileName, smsCollection );
                }

            }
        } );
        return generateSMSReportMenu; 
    }
    
    public GenerateAllSMSReportAction(IWorkbenchWindow window)
    {
        super( Messages.getString( MessagesConstant.GENERATE_ALLSMS_REPORT_ACTION_TEXT ), AS_DROP_DOWN_MENU );
        super.setToolTipText( Messages.getString( MessagesConstant.GENERATE_ALLSMS_REPORT_ACTION_TOOLTIPTEXT ) );
        ImageLoader loader = ImageLoader.getInstance();
        super.setImageDescriptor(loader.getImageDescriptor(ImageConstant.GENERATE_SMSREPORT_ACTION_IMAGE));
        super.setDisabledImageDescriptor(loader.getImageDescriptor(ImageConstant.GENERATE_SMSREPORT_ACTION_DIMAGE));
        
        this.window = window;
        window.getSelectionService().addSelectionListener(this);
        super.setMenuCreator(new IMenuCreator() {
            // To fill with the other menu with the current menu.
            public Menu getMenu(Menu parent)
            {
                Menu generateSMSReportMenu = new Menu(parent);
                return GenerateAllSMSReportAction.this.createMenu(generateSMSReportMenu);
            }
            // To fill with the control ( normally the ActionBars ) with the current menu.
            public Menu getMenu(Control parent)
            {
                Menu generateSMSReportMenu = new Menu( parent );
                return GenerateAllSMSReportAction.this.createMenu(generateSMSReportMenu);
            }
            public void dispose()
            {
            }
        }
        );
    }
    
    public void dispose() {
        this.window.getSelectionService().removeSelectionListener(this);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        // TODO Auto-generated method stub
        IStructuredSelection structuredSelection = (IStructuredSelection) selection;
        if (!(structuredSelection.getFirstElement() instanceof TreeObject)) return;
        if (structuredSelection.size() == 1 && !(structuredSelection.getFirstElement() instanceof TreeParent)) {
            to = (TreeObject) structuredSelection.getFirstElement();
            smsCollection = to.getCollection();
            if (smsCollection != null && smsCollection.size() > 1) {
                super.setEnabled(true);
                return;
            }
        }
        super.setEnabled(false);
    }
}
