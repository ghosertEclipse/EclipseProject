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
import com.loadtrend.app.mobile.editors.PBEditor;
import com.loadtrend.app.mobile.util.GeneratePBReport;

public class GeneratePBReportAction extends WorkbenchWindowAction implements IWorkbenchWindowPulldownDelegate2
{
    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchWindowPulldownDelegate2#getMenu(org.eclipse.swt.widgets.Menu)
     */
    public Menu getMenu(Menu parent) {
        Menu generatePBReportMenu = new Menu( parent );
        return this.configurateMenu( generatePBReportMenu );
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchWindowPulldownDelegate#getMenu(org.eclipse.swt.widgets.Control)
     */
    public Menu getMenu(Control parent) {
        Menu generatePBReportMenu = new Menu( parent );
        return this.configurateMenu( generatePBReportMenu );
    }
    
    private Menu configurateMenu( Menu menu )
    {
        MenuItem item = new MenuItem( menu, SWT.PUSH );
        item.setText( Messages.getString( MessagesConstant.PBREPORT_TXT_TYPE ) );
        item.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                generate();
            }
        } );

        return menu;
    }
    
    protected void generate() {
        FileDialog fileDialog = new FileDialog( window.getShell(), SWT.SAVE );
        fileDialog.setFilterPath( MobileAppConstant.JAVA_ROOT_PATH );
        fileDialog.setFilterExtensions( new String[]{ ".txt" } );
        fileDialog.setFileName( ((PBEditor)part).getTreeObject().getTreeName() );
        
        if ( fileDialog.open() != null )
        {
            String fileName = fileDialog.getFilterPath() + File.separatorChar + fileDialog.getFileName();
            GeneratePBReport.generateTXT( fileName, structuredSelection.toList() );
        } 
    }
}
