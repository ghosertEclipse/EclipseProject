package com.loadtrend.app.mobile.views;

import loadtrend.mobile.Message;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.mobileaction.QuickSendSMSAction;

public class SMSSendView extends ViewPart implements ISelectionListener
{
	public static final String VIEW_ID = "com.loadtrend.app.mobile.views.SMSSendView";
	
	private Text targetNumber = null;
	
	private Text content = null;
	
	private Button send = null;
	
	public void createPartControl( Composite parent )
	{
        this.setPartName( Messages.getString( MessagesConstant.SMSSendView_PARTNAME ) );
        
		FormLayout layout = new FormLayout();
		layout.spacing = 5;
		parent.setLayout( layout );

		FormData data = null;
		
		data = new FormData();
		data.left = new FormAttachment( 0, 5 );
		data.top = new FormAttachment( 0, 3 );
		Label sendTo = new Label( parent, SWT.LEFT );
		sendTo.setText( Messages.getString( MessagesConstant.SMSSENDVIEW_LBSENDTO_TEXT ) );
		sendTo.setLayoutData( data );
		
		data = new FormData();
		data.left = new FormAttachment( sendTo );
		data.top = new FormAttachment( 0, 0 );
		data.right = new FormAttachment( 0, 150 );
		data.bottom = new FormAttachment( 100 );
		targetNumber = new Text( parent, SWT.BORDER );
		targetNumber.setLayoutData( data );
		
		data = new FormData();
		data.left = new FormAttachment( targetNumber );
		data.top = new FormAttachment( 0, 3 );
		Label withContent = new Label( parent, SWT.LEFT );
		withContent.setText( Messages.getString( MessagesConstant.SMSSENDVIEW_LBCONTENT_TEXT ) );
		withContent.setLayoutData( data );
		
		data = new FormData();
		data.left = new FormAttachment( withContent );
		data.top = new FormAttachment( 0, 0 );
		data.right = new FormAttachment( 85 );
		data.bottom = new FormAttachment( 100 );
		content = new Text( parent, SWT.BORDER );
		content.setLayoutData( data );
		
		data = new FormData();
		data.left = new FormAttachment( content );
		data.top = new FormAttachment( 0, 0 );
		data.right = new FormAttachment( 100 );
		data.bottom = new FormAttachment( 100 );
		send = new Button( parent, SWT.PUSH );
		send.setLayoutData( data );
		send.setText( Messages.getString( MessagesConstant.SMSSENDVIEW_SENDBUTTON_TEXT ) );
		
		send.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				if ( !checkParameter() ) return;
                new QuickSendSMSAction(SMSSendView.this.getSite().getWorkbenchWindow(), SMSSendView.this,
                        targetNumber.getText(), content.getText()).run();
            }
		});
        
        this.getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
	}
    
    public void changeStatus(boolean status) {
        targetNumber.setEnabled(status);
        content.setEnabled(status);
        send.setEnabled(status);
    }
	
	private boolean checkParameter()
	{
		if ( !targetNumber.getText().matches( "[0-9+]*" ) )
		{
			MessageDialog.openError( getViewSite().getShell(), 
                                     Messages.getString( MessagesConstant.QUICKSEND_SMS_FAILURE_MESSAGEBOX_TITLE ), 
                                     Messages.getString( MessagesConstant.ERROR_TELENUMBER_INVALID ) );
			targetNumber.setFocus();
			targetNumber.selectAll();
			return false;
		}
		
		if ( targetNumber.getText().equals( "" ) )
		{
			MessageDialog.openError( getViewSite().getShell(), 
                                     Messages.getString( MessagesConstant.QUICKSEND_SMS_FAILURE_MESSAGEBOX_TITLE ), 
                                     Messages.getString( MessagesConstant.ERROR_TELENUMBER_MANDATORY ) );
			targetNumber.setFocus();
			return false;
		}
		
		if ( !targetNumber.getText().matches( "[+][0-9]+" ) && !targetNumber.getText().matches( "[0-9]+" ))
		{
			MessageDialog.openError( getViewSite().getShell(), 
                                     Messages.getString( MessagesConstant.QUICKSEND_SMS_FAILURE_MESSAGEBOX_TITLE ), 
                                     Messages.getString( MessagesConstant.ERROR_TELENUMBER_PLUSPREFIX ) );
			targetNumber.setFocus();
			targetNumber.selectAll();
			return false;
		}
		
		if ( content.getText().equals( "" ) )
		{
			MessageDialog.openError( getViewSite().getShell(), 
                                     Messages.getString( MessagesConstant.QUICKSEND_SMS_FAILURE_MESSAGEBOX_TITLE ), 
                                     Messages.getString( MessagesConstant.ERROR_SMSCONTENT_MANDATORY ) );
			content.setFocus();
			return false;
		}
		return true;
	}
	
	public void setFocus()
	{
		if ( targetNumber.getText().equals( "" ) )
		{
			targetNumber.setFocus();
		}
		else
		{
			content.setFocus();
		}
	}
    
    public void dispose() {
        super.dispose();
        this.getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(this);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        IStructuredSelection structuredSelection = (IStructuredSelection) selection;
        Object object = structuredSelection.getFirstElement();
        
        if (object == null && this.targetNumber.isEnabled()) this.targetNumber.setText("");
        
        if (!(object instanceof Message)) return;
        
        Message message = (Message) object;
        if (this.targetNumber.isEnabled()) {
            this.targetNumber.setText( message.getTeleNum() );
        }
    }
	
}
