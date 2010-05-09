package com.loadtrend.app.mobile.dialog;

import java.io.IOException;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;

import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;

public class AboutDialog extends MobileDialog
{
    public AboutDialog( Shell parentShell )
    {
        super( parentShell );
    }
    protected int[] configureShellWidthHeight()
    {
        int[] widthHeight = new int[]{ 350, 200 };
        return widthHeight;
    }

    protected void configShellTextImage( Shell shell )
    {
        // Initialize shell text.
        shell.setText( Messages.getString( MessagesConstant.ABOUT_DIALOG_TITLE ) );
    }
    
    protected void createButtonsForButtonBar(Composite parent)
    {
        // create OK buttons by default
        createButton( parent, 
                     IDialogConstants.OK_ID, 
                     Messages.getString( MessagesConstant.MOBILE_DIALOG_OKBUTTON_TEXT ),
                     true );
    }
    
    protected Control createDialogArea( Composite parent )
    {
        Composite comp = (Composite) super.createDialogArea( parent );
        this.setBackGroundWhite( comp );
        
        FormLayout formLayout = new FormLayout();
        formLayout.marginTop = 5;
        formLayout.marginLeft = 20;
        formLayout.spacing = 0;
        comp.setLayout( formLayout );
        
        FormData formData = new FormData();
        
        // The first row: product name label
        formData = new FormData();
        formData.left = new FormAttachment( 0, 0 );
        formData.right = new FormAttachment( 0, 350 );
        formData.top = new FormAttachment( 0, 0 );
        Label lbProductName = new Label( comp, SWT.LEFT );
        lbProductName.setLayoutData( formData );
        lbProductName.setText( Messages.getString( MessagesConstant.ABOUT_DIALOG_LBPRODUCTNAME_TEXT ) );
        this.setBackGroundWhite( lbProductName );
        
        // The first row: product name label
        formData = new FormData();
        formData.left = new FormAttachment( 0, 0 );
        formData.right = new FormAttachment( 0, 350 );
        formData.top = new FormAttachment( lbProductName, 10 );
        Label lbVersion = new Label( comp, SWT.LEFT );
        lbVersion.setLayoutData( formData );
        lbVersion.setText( Messages.getString( MessagesConstant.ABOUT_DIALOG_LBVERSION_TEXT ) );
        this.setBackGroundWhite( lbVersion );
        
        // The first row: product name label
        formData = new FormData();
        formData.left = new FormAttachment( 0, 0 );
        formData.right = new FormAttachment( 0, 350 );
        formData.top = new FormAttachment( lbVersion );
        Label lbBuildID = new Label( comp, SWT.LEFT );
        lbBuildID.setLayoutData( formData );
        lbBuildID.setText( Messages.getString( MessagesConstant.ABOUT_DIALOG_LBBUILDID_TEXT ) );
        this.setBackGroundWhite( lbBuildID );
        
        // The first row: product name label
        formData = new FormData();
        formData.left = new FormAttachment( 0, 0 );
        formData.right = new FormAttachment( 0, 350 );
        formData.top = new FormAttachment( lbBuildID, 10 );
        Link lkAuthor = new Link( comp, SWT.NONE );
        lkAuthor.setLayoutData( formData );
        lkAuthor.setText( Messages.getString( MessagesConstant.ABOUT_DIALOG_LKAUTHOR_TEXT ) );
        lkAuthor.addSelectionListener( new LinkSelectionAdapter() );
        this.setBackGroundWhite( lkAuthor );
        
        formData = new FormData();
        formData.left = new FormAttachment( 0, 0 );
        formData.right = new FormAttachment( 0, 350 );
        formData.top = new FormAttachment( lkAuthor );
        Link lkHomepage = new Link( comp, SWT.NONE );
        lkHomepage.setLayoutData( formData );
        lkHomepage.setText( Messages.getString( MessagesConstant.ABOUT_DIALOG_LKHOMEPAGE_TEXT ) );
        lkHomepage.addSelectionListener( new LinkSelectionAdapter() );
        this.setBackGroundWhite( lkHomepage );
        
        // The first row: product name label
        formData = new FormData();
        formData.left = new FormAttachment( 0, 0 );
        formData.right = new FormAttachment( 0, 350 );
        formData.top = new FormAttachment( lkHomepage, 10 );
        Label lbCopyRight = new Label( comp, SWT.LEFT );
        lbCopyRight.setLayoutData( formData );
        lbCopyRight.setText( Messages.getString( MessagesConstant.ABOUT_DIALOG_LBCOPYRIGHT_TEXT ) );
        this.setBackGroundWhite( lbCopyRight );
        
        return comp;
    }
    
    
    private void setBackGroundWhite( Control control )
    {
        control.setBackground( Display.getCurrent().getSystemColor( SWT.COLOR_WHITE ) );
    }
    
    private class LinkSelectionAdapter extends SelectionAdapter
    {
        public void widgetSelected( SelectionEvent e )
        {
            try
            {
                Runtime.getRuntime().exec( "cmd.exe /c start " + e.text );
            }
            catch ( IOException e1 )
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }
}
