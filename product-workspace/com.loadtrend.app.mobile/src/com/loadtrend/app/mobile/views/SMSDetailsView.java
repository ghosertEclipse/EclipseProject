package com.loadtrend.app.mobile.views;

import loadtrend.mobile.Message;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;

import com.loadtrend.app.mobile.data.Global;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.util.SpecialEntity;

public class SMSDetailsView extends ViewPart implements ISelectionListener
{
	public static final String VIEW_ID = "com.loadtrend.app.mobile.views.SMSDetailsView";
	
	private FormText title = null;
	
	private FormText body = null;
	
	private FormToolkit kit = null;
	
	private ScrolledForm scrolledForm = null;
	
	public void createPartControl( Composite parent )
	{
        this.setPartName( Messages.getString( MessagesConstant.SMSDETAILSVIEW_PARTNAME ) );
		this.createMainControl( parent );
        this.getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
	}
	
	private void createMainControl( Composite parent )
	{
		kit = new FormToolkit( parent.getDisplay() );
		scrolledForm = kit.createScrolledForm( parent );
		
		TableWrapLayout layout = new TableWrapLayout();
		scrolledForm.getBody().setLayout( layout );
		scrolledForm.setAlwaysShowScrollBars( false );
		layout.numColumns = 1;
		layout.bottomMargin = 0;
		layout.topMargin = 0;
		layout.leftMargin = 0;
		layout.rightMargin = 0;
		layout.verticalSpacing = 10;
		
		title = kit.createFormText( scrolledForm.getBody(), true );
		TableWrapData td = new TableWrapData();
		td.colspan = 1;
		td.grabHorizontal = true;
		title.setLayoutData( td );
		title.setBackground( parent.getDisplay().getSystemColor( SWT.COLOR_WIDGET_BACKGROUND ) );
		title.setWhitespaceNormalized( false );
		
		body = kit.createFormText( scrolledForm.getBody(), true );
		td = new TableWrapData();
		td.colspan = 1;
		td.grabHorizontal = true;
		body.setLayoutData( td );
		body.setBackground( parent.getDisplay().getSystemColor( SWT.COLOR_WHITE ) );
		body.setWhitespaceNormalized( false );
		// body.setForeground( new Color( parent.getDisplay(), 0x33, 0x99, 0xff ) );
	}
	
    /**
     * Fill the SMS Details View with Message class.
     * @param message clear all in SMS Details View if message equals null.
     */
	private void fillFormTextContent( Message message, String nameOrNumber )
	{
		if ( message == null )
        {
            title.setText( "", false, true );
            body.setText( "", false, true );
        }
        else
        {
            String name = "--";
            if ( !message.getTeleNum().equals( nameOrNumber ) )
            {
                name = nameOrNumber;
            }
            String thtml = Messages.getString( MessagesConstant.SMSDETAILSVIEW_TITLE_HTML, 
                    SpecialEntity.encode( message.getTeleNum() ),
                    SpecialEntity.encode( name ),
                    SpecialEntity.formatDateTime( message.getReceiveTime() ),
                    SpecialEntity.getSMSStatusDesc( message.getStatus() ) );

            title.setText( thtml, true, true );

            String bhtml = Messages.getString( MessagesConstant.SMSDETAILSVIEW_BODY_HTML, 
                    SpecialEntity.encode( message.getContent() ) );
            body.setText( bhtml, true, true );

            scrolledForm.reflow( true );
        }
	}

	public void setFocus()
	{
	}
	
	public void dispose()
	{
		super.dispose();
		kit.dispose();
        this.getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(this);
	}

    /* (non-Javadoc)
     * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        IStructuredSelection structuredSelection = (IStructuredSelection) selection;
        Object object = structuredSelection.getFirstElement();
        
        if (object == null) this.fillFormTextContent(null, "");
        
        if (!(object instanceof Message)) return;
        
        Message message = (Message) object;
        this.fillFormTextContent(message, Global.getNameOrNumber(message));
    }
}
