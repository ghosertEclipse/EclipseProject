package com.loadtrend.app.mobile.dialog;

import loadtrend.mobile.Mobile;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.loadtrend.app.mobile.MobilePlugin;
import com.loadtrend.app.mobile.data.ImageConstant;
import com.loadtrend.app.mobile.data.ImageLoader;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.data.Preferences;
import com.loadtrend.app.mobile.data.PreferencesConstant;
import com.loadtrend.app.mobile.mobileaction.SavePBAction;
import com.loadtrend.app.mobile.util.SpecialEntity;

public class NewPBDialog extends MobileDialog
{
    private static final int SAVE_ID = IDialogConstants.NO_TO_ALL_ID + 10;
    
    private static final int EXIT_ID = IDialogConstants.NO_TO_ALL_ID + 11;
    
    private Label lbInputStatus = null;
    
    private Text txtName = null;
    
    private Text txtTeleNum = null;
    
    private Button rdMobile = null;
    
    private Button rdSimCard = null;
    
    private Button rdLocalMachine = null;
    
    private Button saveButton = null;
    
    public NewPBDialog( Shell parentShell )
    {
        super( parentShell );
    }
    
    protected void configShellTextImage( Shell shell )
    {
        // Initialize shell text.
        shell.setText( Messages.getString( MessagesConstant.NEWPB_DIALOG_TITLE ) );
        
        // Set title image
        ImageLoader loader = ImageLoader.getInstance();
        shell.setImage( loader.getImage( ImageConstant.SHOW_NEWPBDIALOG_ACTION_IMAGE ) );
    }

    protected int[] configureShellWidthHeight()
    {
        int[] widthHeight = new int[]{ 350, 200 };
        return widthHeight;
    }
    
    protected void createButtonsForButtonBar( Composite parent )
    {
        createButton( parent, SAVE_ID, Messages.getString( MessagesConstant.NEWPB_DIALOG_SAVEBUTTON_TEXT ), false );
        createButton( parent, EXIT_ID, Messages.getString( MessagesConstant.NEWPB_DIALOG_EXITBUTTON_TEXT ), false );
        saveButton = getButton( SAVE_ID );
    }

    protected void buttonPressed( int buttonId )
    {   
        if ( buttonId == SAVE_ID )
        {
            this.setValuesToPreferences();
            new SavePBAction(MobilePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow()).run();
        }
        super.close();
        return;
    }

    protected Control createDialogArea( Composite parent )
    {
        Composite comp = (Composite) super.createDialogArea( parent );
        FormLayout formLayout = new FormLayout();
        formLayout.marginTop = 20;
        formLayout.marginLeft = 20;
        formLayout.spacing = 5;
        comp.setLayout( formLayout );
        
        FormData formData = new FormData();
        
        // The first row: name label
        formData = new FormData();
        formData.left = new FormAttachment( 0, 0 );
        formData.right = new FormAttachment( 0, 50 );
        formData.top = new FormAttachment( 0, 0 );
        Label lbName = new Label( comp, SWT.LEFT );
        lbName.setLayoutData( formData );
        lbName.setText( Messages.getString( MessagesConstant.NEWPB_DIALOG_LBNAME_TEXT ) );
        
        // The first row: name text
        formData = new FormData();
        formData.left = new FormAttachment( lbName );
        formData.right = new FormAttachment( 0, 200 );
        formData.top = new FormAttachment( 0, 0 );
        txtName = new Text( comp, SWT.BORDER );
        txtName.setLayoutData( formData );
        txtName.addModifyListener( new ModifyListener()
        {
            public void modifyText( ModifyEvent e )
            {
                checkNameNumberTextBox();
            }
        });
        
        // The second row: number label
        formData = new FormData();
        formData.left = new FormAttachment( 0, 0 );
        formData.right = new FormAttachment( 0, 50 );
        formData.top = new FormAttachment( lbName, 15 );
        Label lbTeleNum = new Label( comp, SWT.LEFT );
        lbTeleNum.setLayoutData( formData );
        lbTeleNum.setText( Messages.getString( MessagesConstant.NEWPB_DIALOG_LBNUMBER_TEXT ) );
        
        // The second row: number text
        formData = new FormData();
        formData.left = new FormAttachment( lbTeleNum );
        formData.right = new FormAttachment( 0, 200 );
        formData.top = new FormAttachment( txtName, 10 );
        txtTeleNum = new Text( comp, SWT.BORDER );
        txtTeleNum.setLayoutData( formData );
        txtTeleNum.addModifyListener( new ModifyListener()
        {
            public void modifyText( ModifyEvent e )
            {
                checkNameNumberTextBox();
            }
        });
        
        // Mobile, SimCard, Machine radios        
        formData = new FormData();
        formData.left = new FormAttachment( txtTeleNum, 15 );
        formData.top = new FormAttachment( 0, 0 );
        rdMobile = new Button( comp, SWT.RADIO );
        rdMobile.setLayoutData( formData );
        rdMobile.setText( Messages.getString( MessagesConstant.MOBILE_TEXT ) );
        rdMobile.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                checkNameNumberTextBox();
            }
        });
        
        formData = new FormData();
        formData.left = new FormAttachment( txtTeleNum, 15 );
        formData.top = new FormAttachment( rdMobile );
        rdSimCard = new Button( comp, SWT.RADIO );
        rdSimCard.setLayoutData( formData );
        rdSimCard.setText( Messages.getString( MessagesConstant.SIMCARD_TEXT ) );
        rdSimCard.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                checkNameNumberTextBox();
            }
        });
        
        formData = new FormData();
        formData.left = new FormAttachment( txtTeleNum, 15 );
        formData.top = new FormAttachment( rdSimCard );
        rdLocalMachine = new Button( comp, SWT.RADIO );
        rdLocalMachine.setLayoutData( formData );
        rdLocalMachine.setText( Messages.getString( MessagesConstant.LOCALMACHINE_TEXT ) );
        
        // Input status label
        formData = new FormData();
        formData.left = new FormAttachment( 0, 0 );
        formData.right = new FormAttachment( 0, 350 );
        formData.top = new FormAttachment( lbTeleNum, 20 );
        lbInputStatus = new Label( comp, SWT.LEFT );
        lbInputStatus.setLayoutData( formData );
        
        // Seperator line
        formData = new FormData();
        formData.left = new FormAttachment( 0, 0 );
        formData.right = new FormAttachment( 0, 300 );
        formData.top = new FormAttachment( lbInputStatus, 5 );
        Label lbSeperator = new Label( comp, SWT.SEPARATOR | SWT.HORIZONTAL );
        lbSeperator.setLayoutData( formData );
        
        return comp;
    }
    
    private void setValuesToPreferences()
    {
        if ( rdMobile.getSelection() )
        {
            Preferences.setValue( PreferencesConstant.PB_MEMO_TYPE, Mobile.PB_ME_MEMO );
        }
        else if ( rdSimCard.getSelection() )
        {
            Preferences.setValue( PreferencesConstant.PB_MEMO_TYPE, Mobile.PB_SM_MEMO );
        }
        else
        {
            Preferences.setValue( PreferencesConstant.PB_MEMO_TYPE, "" );
        }
        
        Preferences.setValue( PreferencesConstant.TXT_NAME, txtName.getText() );
        
        Preferences.setValue( PreferencesConstant.TXT_NUMBER, txtTeleNum.getText() );
    }
    
    private void checkNameNumberTextBox()
    {
        String pbMemoParam = "";
        
        if ( rdMobile.getSelection() )
        {
            pbMemoParam = Mobile.PB_ME_MEMO;
        }
        if ( rdSimCard.getSelection() )
        {
            pbMemoParam = Mobile.PB_SM_MEMO;
        }
        
        String errorDescription = SpecialEntity.checkPhoneBookNameNumber( txtName.getText(), 
                                                                          txtTeleNum.getText(), 
                                                                          pbMemoParam );
        
        if ( errorDescription.equals( "" ) )
        {
            lbInputStatus.setText( "" );
            saveButton.setEnabled( true );
        }
        else
        {
            lbInputStatus.setText( errorDescription );
            saveButton.setEnabled( false );
        }
    }

    public Button getRdLocalMachine()
    {
        return rdLocalMachine;
    }

    public Button getRdMobile()
    {
        return rdMobile;
    }

    public Button getRdSimCard()
    {
        return rdSimCard;
    }

    public Text getTxtName()
    {
        return txtName;
    }

    public Text getTxtTeleNum()
    {
        return txtTeleNum;
    }
}
