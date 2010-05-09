package com.loadtrend.app.mobile.action;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchWindowPulldownDelegate2;

import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.data.MobileAppConstant;
import com.loadtrend.app.mobile.editors.SMSEditor;
import com.loadtrend.app.mobile.util.GenerateSMSReport;

public class GenerateSMSReportAction extends WorkbenchWindowAction implements IWorkbenchWindowPulldownDelegate2
{
    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchWindowPulldownDelegate#getMenu(org.eclipse.swt.widgets.Control)
     */
    public Menu getMenu(Control parent) {
        Menu generateSMSReportMenu = new Menu( parent );
        return this.createMenu(generateSMSReportMenu);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchWindowPulldownDelegate2#getMenu(org.eclipse.swt.widgets.Menu)
     */
    public Menu getMenu(Menu parent) {
        Menu generateSMSReportMenu = new Menu( parent );
        return this.createMenu(generateSMSReportMenu);
    }
    
    protected Menu createMenu(Menu generateSMSReportMenu) {
        MenuItem item = new MenuItem( generateSMSReportMenu, SWT.PUSH );
        item.setText( Messages.getString( MessagesConstant.SMSREPORT_TXT_TYPE ) );
        item.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                generate();
            }
        } );
        return generateSMSReportMenu; 
    }

    protected void generate() {
        FileDialog fileDialog = new FileDialog( window.getShell(), SWT.SAVE );
        fileDialog.setFilterPath( MobileAppConstant.JAVA_ROOT_PATH );
        fileDialog.setFilterExtensions( new String[]{ ".txt" } );
        fileDialog.setFileName( ((SMSEditor)part).getTreeObject().getTreeName() );
        
        if ( fileDialog.open() != null )
        {
            String fileName = fileDialog.getFilterPath() + File.separatorChar + fileDialog.getFileName();
            GenerateSMSReport.generateTXT( fileName, structuredSelection.toList() );
        } 
    }
}
