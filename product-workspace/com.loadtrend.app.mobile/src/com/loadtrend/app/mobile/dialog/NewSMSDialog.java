package com.loadtrend.app.mobile.dialog;

import java.util.ArrayList;
import java.util.Iterator;

import loadtrend.mobile.Message;
import loadtrend.mobile.MessageProcessor;
import loadtrend.mobile.Mobile;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchWindow;

import com.loadtrend.app.mobile.MobilePlugin;
import com.loadtrend.app.mobile.data.Global;
import com.loadtrend.app.mobile.data.ImageConstant;
import com.loadtrend.app.mobile.data.ImageLoader;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.data.MobileAppConstant;
import com.loadtrend.app.mobile.data.Preferences;
import com.loadtrend.app.mobile.data.PreferencesConstant;
import com.loadtrend.app.mobile.mobileaction.MobileAction;
import com.loadtrend.app.mobile.mobileaction.SaveSMSAction;
import com.loadtrend.app.mobile.mobileaction.SendSMSAction;
import com.loadtrend.app.mobile.mobileaction.SendSaveSMSAction;

public class NewSMSDialog extends MobileDialog
{
	private static final int SEND_ID = IDialogConstants.NO_TO_ALL_ID + 1;
	
	private static final int SAVE_ID = IDialogConstants.NO_TO_ALL_ID + 2;
	
	private static final int SENDSAVE_ID = IDialogConstants.NO_TO_ALL_ID + 3;
	
	private static final int EXIT_ID = IDialogConstants.NO_TO_ALL_ID + 4;
	
	private Text txtReceiver = null;
	
	private Label lbSMSStatus = null;
	
	private StyledText txtContent = null;
	
	private Button chkStatusReport = null;
	
	private Button chkHandFree = null;
	
	private Combo comboValidTime = null;
	
	private Menu popMenu = null;
	
	private MobileAction  action = null;
	
    private LocalPBListShell localPBListShell = null;
    
    public NewSMSDialog( Shell parentShell )
    {
        super( parentShell );
    }
    
    protected void configShellTextImage( Shell shell )
	{
    	// Initialize shell text.
        shell.setText( Messages.getString( MessagesConstant.NEWSMS_DIALOG_TITLE ) );
        
        // Set title image
        ImageLoader loader = ImageLoader.getInstance();
        shell.setImage( loader.getImage( ImageConstant.SHOW_NEWSMSDIALOG_ACTION_IMAGE ) );
	}

	protected int[] configureShellWidthHeight()
	{
		int[] widthHeight = new int[]{ 420, 300 };
		return widthHeight;
	}
	
	protected void createButtonsForButtonBar( Composite parent )
	{
		createButton( parent, SEND_ID, Messages.getString( MessagesConstant.NEWSMS_DIALOG_SENDBUTTON_TEXT ), true );
		createButton( parent, SAVE_ID, Messages.getString( MessagesConstant.NEWSMS_DIALOG_SAVEBUTTON_TEXT ), false );
		createButton( parent, SENDSAVE_ID, Messages.getString( MessagesConstant.NEWSMS_DIALOG_SENDSAVEBUTTON_TEXT ), false );
		createButton( parent, EXIT_ID, Messages.getString( MessagesConstant.NEWSMS_DIALOG_EXITBUTTON_TEXT ), false );	
		
		ImageLoader loader = ImageLoader.getInstance();
    	MenuManager menuManager = new MenuManager( "#popMenu" );
    	menuManager.add( new Action( Messages.getString( MessagesConstant.MOBILE_TEXT ), loader.getImageDescriptor( ImageConstant.MOBILE_IMAGE ) )
		{
			public void run()
			{
				Preferences.setValue( PreferencesConstant.SMS_MEMO_TYPE, Mobile.SMS_ME_MEMO );
				action.run();
				NewSMSDialog.this.close();
			}
		} );
		menuManager.add( new Action( Messages.getString( MessagesConstant.SIMCARD_TEXT ), loader.getImageDescriptor( ImageConstant.SIMCARD_IMAGE ) )
		{
			public void run()
			{
				Preferences.setValue( PreferencesConstant.SMS_MEMO_TYPE, Mobile.SMS_SM_MEMO );
				action.run();
				NewSMSDialog.this.close();
			}
		} );
		menuManager.add( new Action( Messages.getString( MessagesConstant.LOCALMACHINE_TEXT ), loader.getImageDescriptor( ImageConstant.LOCAL_MACHINE_IMAGE ) )
		{
			public void run()
			{
				// add corresponding local machine handle.
                Preferences.setValue( PreferencesConstant.SMS_MEMO_TYPE, "" );
                action.run();
				NewSMSDialog.this.close();
			}
		} );
		popMenu = menuManager.createContextMenu( getShell() );
		getShell().setMenu( popMenu );
	}

	protected void buttonPressed( int buttonId )
	{
		Preferences.setValue( PreferencesConstant.TXT_RECEIVER, txtReceiver.getText() );
		Preferences.setValue( PreferencesConstant.TXT_CONTENT, txtContent.getText() );
		
		if ( buttonId == EXIT_ID )
		{
			super.close();
			return;
		}
		
		if ( !checkParameter( buttonId ) ) return;
		
        IWorkbenchWindow window = MobilePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
        
		if ( buttonId == SEND_ID )
		{
            action = new SendSMSAction(window);
		}
		if ( buttonId == SAVE_ID )
		{
            action = new SaveSMSAction(window);
		}
		if ( buttonId == SENDSAVE_ID )
		{
            action = new SendSaveSMSAction(window);
		}
		
		if ( buttonId == SEND_ID )
		{
			action.run();
			super.close();
		}
		else
		{
			popMenu.setVisible( true );
			return;
		}
	}

	protected Control createDialogArea( Composite parent )
    {
    	Composite comp = (Composite) super.createDialogArea( parent );
    	FormLayout formLayout = new FormLayout();
    	formLayout.marginTop = 10;
    	formLayout.marginLeft = 10;
    	formLayout.spacing = 5;
    	comp.setLayout( formLayout );
    	
    	// The first row: lable, text, button
    	FormData formData = new FormData();
    	formData.left = new FormAttachment( 0, 0 );
    	formData.top = new FormAttachment( 0, 0 );
    	Label lbReceiver = new Label( comp, SWT.SHADOW_OUT );
    	lbReceiver.setText( Messages.getString( MessagesConstant.NEWSMS_DIALOG_LBRECEIVER_TEXT ) );
    	lbReceiver.setLayoutData( formData );
    	
    	formData = new FormData();
    	formData.left = new FormAttachment( lbReceiver );
    	formData.right = new FormAttachment( 0, 370 );
    	formData.top = new FormAttachment( 0, 0 );
    	txtReceiver = new Text( comp, SWT.BORDER );
    	txtReceiver.setLayoutData( formData );
    	fillTxtReceiver();
    	
    	formData = new FormData();
    	formData.left = new FormAttachment( txtReceiver, -1 );
    	formData.top = new FormAttachment( 0, 0 );
    	Button btSelectReceivers = new Button( comp, SWT.ARROW | SWT.RIGHT );
    	btSelectReceivers.setLayoutData( formData );
        btSelectReceivers.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                if ( localPBListShell == null || localPBListShell.isDisposed() )
                {
                    localPBListShell = new LocalPBListShell( NewSMSDialog.this.getShell(), 
                                                             NewSMSDialog.this.txtReceiver );
                    localPBListShell.create();
                }
            }
        });
    	
    	// SMS status label
    	formData = new FormData();
    	formData.left = new FormAttachment( 0, 0 );
    	formData.top = new FormAttachment( lbReceiver, 10 );
    	lbSMSStatus = new Label( comp, SWT.LEFT );
    	lbSMSStatus.setLayoutData( formData );
    	lbSMSStatus.setText( Messages.getString( MessagesConstant.NEWSMS_DIALOG_LBSMSSTATUS_DEFAULT_TEXT ) );
    	
    	// SMS text content
    	formData = new FormData();
    	formData.left = new FormAttachment( 0, 0 );
    	formData.right = new FormAttachment( 0, 390 );
    	formData.top = new FormAttachment( lbSMSStatus );
    	formData.bottom = new FormAttachment( 0, 170 );
    	txtContent = new StyledText( comp, SWT.BORDER | SWT.MULTI | SWT.WRAP );
    	txtContent.setLayoutData( formData );
    	txtContent.addModifyListener( new TextContentModifyListener() );
    	fillTxtContent();
    	
    	// Status Report checkbox
    	formData = new FormData();
    	formData.left = new FormAttachment( 0, 0 );
    	formData.top = new FormAttachment( txtContent );
    	chkStatusReport = new Button( comp, SWT.CHECK );
    	chkStatusReport.setText( Messages.getString( MessagesConstant.NEWSMS_DIALOG_CHKSTATUSREPORT_TEXT ) );
    	chkStatusReport.setLayoutData( formData );
    	fillChkStatusReport();
    	chkStatusReport.addSelectionListener( new SelectionAdapter()
		{
			public void widgetSelected( SelectionEvent e )
			{
				String value = String.valueOf( chkStatusReport.getSelection() );
				Preferences.setValue( PreferencesConstant.STATUS_REPORT, value  );
			}
		} );
    	
        // Hand Free checkbox
    	formData = new FormData();
    	formData.left = new FormAttachment( chkStatusReport );
    	formData.top = new FormAttachment( txtContent );
    	chkHandFree = new Button( comp, SWT.CHECK );
    	chkHandFree.setText( Messages.getString( MessagesConstant.NEWSMS_DIALOG_CHKHANDFREE_TEXT ) );
    	chkHandFree.setLayoutData( formData );
    	fillChkHandFree();
    	chkHandFree.addSelectionListener( new SelectionAdapter()
		{
			public void widgetSelected( SelectionEvent e )
			{
				String value = String.valueOf( chkHandFree.getSelection() );
				Preferences.setValue( PreferencesConstant.HAND_FREE, value );
			}
		} );
    	
    	// ValidTime Combo and its label
    	formData = new FormData();
    	formData.right = new FormAttachment( 0, 390 );
    	formData.top = new FormAttachment( txtContent );
    	comboValidTime = new Combo( comp, SWT.READ_ONLY );
    	comboValidTime.setVisibleItemCount( 6 );
    	comboValidTime.setLayoutData( formData );
    	fillComboValidTime();
    	comboValidTime.addSelectionListener( new SelectionAdapter()
		{
			public void widgetSelected( SelectionEvent e )
			{
				int selectedIndex = comboValidTime.getSelectionIndex();
				int value = 0;
				switch ( selectedIndex )
				{
					case 0:
						value = Message.FIVE_MINUTES_VALIDTIME;
						break;
					case 1:
						value = Message.ONE_HOUR_VALIDTIME;
						break;
					case 2:
						value = Message.TWELVE_HOURS_VALIDTIME;
						break;
					case 3:
						value = Message.ONE_DAY_VALIDTIME;
						break;
					case 4:
						value = Message.ONE_WEEK_VALIDTIME;
						break;
					case 5:
						value = Message.MAX_VALIDTIME;
						break;
				}
				Preferences.setValue( PreferencesConstant.VALID_TIME, "" + value );
			}
		} );
    	
    	formData = new FormData();
    	formData.right = new FormAttachment( comboValidTime );
    	formData.top = new FormAttachment( txtContent, 2 );
    	Label lbValidTime = new Label( comp, SWT.LEFT );
    	lbValidTime.setText( Messages.getString( MessagesConstant.NEWSMS_DIALOG_LBVALIDTIME_TEXT ) );
    	lbValidTime.setLayoutData( formData );
    	
    	// Seperator line
    	formData = new FormData();
    	formData.left = new FormAttachment( 0, 0 );
    	formData.right = new FormAttachment( 0, 390 );
    	formData.top = new FormAttachment( chkHandFree, 10 );
    	Label lbSeperator = new Label( comp, SWT.SEPARATOR | SWT.HORIZONTAL );
    	lbSeperator.setLayoutData( formData );
        
        // show localPBListShell Follow With ParentShell
        this.getShell().addControlListener( new ControlListener()
        {
            public void controlResized( ControlEvent e )
            {
            }
        
            public void controlMoved( ControlEvent e )
            {
                if ( localPBListShell != null && !localPBListShell.isDisposed() )
                {
                    localPBListShell.showFollowWithParentShell();
                }
            }
        });
        
    	return comp;
    }
	
	private void fillTxtReceiver()
	{
		String txtReceiver = Preferences.getValue( PreferencesConstant.TXT_RECEIVER );
		this.txtReceiver.setText( txtReceiver );
	}
	
	private void fillTxtContent()
	{
		String txtContent = Preferences.getValue( PreferencesConstant.TXT_CONTENT );
		this.txtContent.setText( txtContent );
	}
	
	private void fillChkStatusReport()
	{
		boolean isStatusReport = false;
		String statusReport = Preferences.getValue( PreferencesConstant.STATUS_REPORT );
		if ( statusReport.equals( "true" ) )
		{
			isStatusReport = true;
		}
		chkStatusReport.setSelection( isStatusReport );
	}
	
	private void fillChkHandFree()
	{
		boolean isHandFree = false;
		String handFree = Preferences.getValue( PreferencesConstant.HAND_FREE );
		if ( handFree.equals( "true" ) )
		{
			isHandFree = true;
		}
		chkHandFree.setSelection( isHandFree );
	}
	
    private void fillComboValidTime()
    {
        // Load the value list from validtime properties file
        ArrayList list = Messages.getListFromPropertiesByNaturalOrder( MobileAppConstant.VAILDTIME_PROPERTIES, Global.getLocale() );
    	Iterator it = list.iterator();
        while ( it.hasNext() )
        {
            comboValidTime.add( (String) it.next() );
        }
        
    	String value = Preferences.getValue( PreferencesConstant.VALID_TIME );
    	int itemIndex = 0;
    	if ( !value.equals( "" ) )
    	{
    		switch ( Integer.parseInt( value ) )
			{
				case Message.FIVE_MINUTES_VALIDTIME:
					itemIndex = 0;
					break;
				case Message.ONE_HOUR_VALIDTIME:
					itemIndex = 1;
					break;
				case Message.TWELVE_HOURS_VALIDTIME:
					itemIndex = 2;
					break;
				case Message.ONE_DAY_VALIDTIME:
					itemIndex = 3;
					break;
				case Message.ONE_WEEK_VALIDTIME:
					itemIndex = 4;
					break;
				case Message.MAX_VALIDTIME:
					itemIndex = 5;
					break;
			}
    	}
    	comboValidTime.setText( comboValidTime.getItem( itemIndex ) );
    }
    
    private class TextContentModifyListener implements ModifyListener
    {
		public void modifyText( ModifyEvent e )
		{
			String content = txtContent.getText();
			
			int wordCount = content.length();
			int maxLenOfOneSMS = 160;
			String txtEnglishOrNot = Messages.getString( MessagesConstant.NEWSMS_DIALOG_LBSMSSTATUS_ENG );
			if ( MessageProcessor.isContentUnicode( content ) )
			{
				maxLenOfOneSMS = 70;
				txtEnglishOrNot = Messages.getString( MessagesConstant.NEWSMS_DIALOG_LBSMSSTATUS_NONENG );
			}
			
			Message message = new Message();
			message.setContent( content );
			Message[] messages = MessageProcessor.splitMessageByContent( message );
			int pieces = messages.length;
			
			for ( int i = 0; i < pieces; i++ )
			{
				Color foreground = null;
				int start = i * maxLenOfOneSMS;
				int length = ( i == pieces - 1 ? content.length() - i * maxLenOfOneSMS : maxLenOfOneSMS );
				if ( i % 2 == 0)
				{
					foreground = Display.getCurrent().getSystemColor( SWT.COLOR_DARK_MAGENTA );
				}
				else
				{
					foreground = Display.getCurrent().getSystemColor( SWT.COLOR_DARK_GREEN );
				}
				StyleRange styleRange = new StyleRange( start, length, foreground, null, SWT.NORMAL );
				txtContent.setStyleRange( styleRange );
			}
	    	
	    	lbSMSStatus.setText( Messages.getString( MessagesConstant.NEWSMS_DIALOG_LBSMSSTATUS_TEXT,
	    			                                 new Integer( wordCount ),
	    			                                 new Integer( maxLenOfOneSMS ),
	    			                                 txtEnglishOrNot,
	    			                                 new Integer( pieces )
	    			                                 ) );
	    	lbSMSStatus.pack();
		}
    }
    
    private boolean checkParameter( int buttonId )
    {
		if ( !txtReceiver.getText().matches( "[0-9+;]*" ) )
		{
			MessageDialog.openError( getShell(),
					                 Messages.getString( MessagesConstant.NEWSMS_DIALOG_ERROR_TITLE ),
					                 Messages.getString( MessagesConstant.ERROR_TELENUMBER_INVALID )
					                 );
			txtReceiver.setFocus();
			txtReceiver.selectAll();
			return false;
		}
		
		if ( txtReceiver.getText().equals( "" ) && buttonId != SAVE_ID )
		{
			MessageDialog.openError( getShell(),
					                 Messages.getString( MessagesConstant.NEWSMS_DIALOG_ERROR_TITLE ),
					                 Messages.getString( MessagesConstant.ERROR_TELENUMBER_MANDATORY )
					                 );
			txtReceiver.setFocus();
			return false;
		}
		
		if ( txtReceiver.getText().matches( "[+;]+" ) )
		{
			MessageDialog.openError( getShell(),
					                 Messages.getString( MessagesConstant.NEWSMS_DIALOG_ERROR_TITLE ),
					                 Messages.getString( MessagesConstant.ERROR_TELENUMBER_NONUMBER )
					                 );
			txtReceiver.setFocus();
			return false;
		}
		
		if ( txtContent.getText().equals( "" ) )
		{
			MessageDialog.openError( getShell(),
					                 Messages.getString( MessagesConstant.NEWSMS_DIALOG_ERROR_TITLE ),
					                 Messages.getString( MessagesConstant.ERROR_SMSCONTENT_MANDATORY )
					                 );
			txtContent.setFocus();
			return false;
		}
		return true;
    }

    public StyledText getTxtContent()
    {
        return txtContent;
    }

    public Text getTxtReceiver()
    {
        return txtReceiver;
    }
}
