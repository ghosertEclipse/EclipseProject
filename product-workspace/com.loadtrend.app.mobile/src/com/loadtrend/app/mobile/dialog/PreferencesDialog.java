package com.loadtrend.app.mobile.dialog;

import java.util.ArrayList;
import java.util.Iterator;

import loadtrend.mobile.Mobile;
import loadtrend.mobile.MobileFactory;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.loadtrend.app.mobile.action.AutoDetectMobilePortAction;
import com.loadtrend.app.mobile.data.Global;
import com.loadtrend.app.mobile.data.ImageConstant;
import com.loadtrend.app.mobile.data.ImageLoader;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.data.MobileAppConstant;
import com.loadtrend.app.mobile.data.Preferences;
import com.loadtrend.app.mobile.data.PreferencesConstant;

public class PreferencesDialog extends MobileDialog
{    
    private Combo manuCombo = null;
    
    private Combo typeCombo = null;
    
    private Combo countryCombo = null;
    
    private static final String DEFAULT_MOBILE_TYPE = "Other";
    
    private Mobile mobile = null;
    
    private Text mobileNumber = null;
    
    public PreferencesDialog(Shell parentShell, Mobile mobile)
    {
        super( parentShell );
        this.mobile = mobile;
    }
    
    protected void configShellTextImage( Shell shell )
	{
    	// Initialize shell text.
        shell.setText( Messages.getString( MessagesConstant.PREFERENCES_DIALOG_TITLE ) );
        
        // Set title image
        ImageLoader loader = ImageLoader.getInstance();
        shell.setImage( loader.getImage( ImageConstant.SHOW_PREFERENCESDIALOG_ACTION_IMAGE ) );
	}

	protected int[] configureShellWidthHeight()
	{
		int[] widthHeight = new int[]{ 320, 345 };
		return widthHeight;
	}

	protected Control createDialogArea( Composite parent )
    {
        Composite comp = (Composite) super.createDialogArea( parent );
        
        GridLayout layout = (GridLayout) comp.getLayout();
        layout.numColumns = 1;
        
        createSelectMobileGroup( comp );
        
        createSelectCountryGroup( comp );
        
        createConnectionGroup( comp );
        
        return comp;
    }
	
    protected void createButtonsForButtonBar(Composite parent) {
        // create OK button by default
        createButton(parent, IDialogConstants.OK_ID, Messages.getString( MessagesConstant.MOBILE_DIALOG_OKBUTTON_TEXT ), true);
    }

    protected void buttonPressed( int buttonId )
    {
        if ( buttonId == IDialogConstants.OK_ID )
        {
        	// Check mobile number validation.
			if (!mobileNumber.getText().matches("[0-9]+")) {
				MessageBox box = new MessageBox(this.getShell(), SWT.OK | SWT.ICON_INFORMATION);
				box.setText(Messages.getString(MessagesConstant.PREFERENCES_DIALOG_CHECK_MOBILE));
				box.setMessage(Messages.getString(MessagesConstant.PREFERENCES_DIALOG_NUMBER_ONLY));
				box.open();
				mobileNumber.setFocus();
				return;
			}
			
			if (mobileNumber.getText().length() != 11) {
				MessageBox box = new MessageBox(this.getShell(), SWT.OK | SWT.ICON_INFORMATION);
				box.setText(Messages.getString(MessagesConstant.PREFERENCES_DIALOG_CHECK_MOBILE));
				box.setMessage(Messages.getString(MessagesConstant.PREFERENCES_DIALOG_ELEVEN_ONLY));
				box.open();
				mobileNumber.setFocus();
				return;
			}
			Preferences.setValue(PreferencesConstant.MOBILE_NUMBER, mobileNumber.getText());
			
			// set mobile type, manu and country
        	String mobileType = null;
        	if ( typeCombo.getText().equals( DEFAULT_MOBILE_TYPE ) )
        	{
        		mobileType = manuCombo.getText().toUpperCase();
        	}
        	else
        	{
        		mobileType = typeCombo.getText().toUpperCase();
        	}
		    Preferences.setValue( PreferencesConstant.MOBILE_TYPE, mobileType );
		    
		    String countryIndex = String.valueOf( countryCombo.getSelectionIndex() );
		    Preferences.setValue( PreferencesConstant.COUNTRY_NAME, countryIndex );
        }
        else
        {
        }
    	super.buttonPressed( buttonId );
    }

    private void createConnectionGroup( Composite comp )
    {
        // Connection Group
        Group selectConnectionGroup = new Group( comp, SWT.SHADOW_ETCHED_IN );
        selectConnectionGroup.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        selectConnectionGroup.setText( Messages.getString( MessagesConstant.PREFERENCES_DIALOG_CONNECTIONGROUP_TEXT ) );

        selectConnectionGroup.setLayout( new GridLayout( 1, false ) );
        
        // Create description label and auto-detect button
        Label description = new Label( selectConnectionGroup, SWT.LEFT | SWT.WRAP );
        description.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        description.setText( Messages.getString( MessagesConstant.PREFERENCES_DIALOG_DESCRIPTION ) );
        
        Button autoDetectButton = new Button( selectConnectionGroup, SWT.PUSH );
        autoDetectButton.setText( Messages.getString( MessagesConstant.PREFERENCES_DIALOG_AUTODETECTBUTTON_TEXT ) );
        autoDetectButton.setLayoutData( new GridData( SWT.END, SWT.NONE, true, true  ) );
        
        autoDetectButton.addSelectionListener( new SelectionAdapter()
		{
			public void widgetSelected( SelectionEvent e )
			{
				if (mobile != null && mobile.isConnecting()) {
					MessageBox mb = new MessageBox( getShell(), SWT.OK | SWT.ICON_INFORMATION );
					mb.setText( Messages.getString( MessagesConstant.PREFERENCES_DIALOG_CONNECTION_CREATED_MESSAGEBOX_TITLE ) );
					mb.setMessage( Messages.getString( MessagesConstant.PREFERENCES_DIALOG_CONNECTION_CREATED_MESSAGEBOX_BODY ) );
					mb.open();
				}
				else {
                    new AutoDetectMobilePortAction(PreferencesDialog.this.getShell()).run();
				}
			}
		} );
        
        selectConnectionGroup.pack();
    }
    
    private void createSelectCountryGroup( Composite comp )
    {
        // Select Country Group
        Group selectCountryGroup = new Group( comp, SWT.SHADOW_ETCHED_IN );
        selectCountryGroup.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        selectCountryGroup.setText( Messages.getString( MessagesConstant.PREFERENCES_DIALOG_COUNTRYGROUP_TEXT ) );

        selectCountryGroup.setLayout( new GridLayout( 1, false ) );
        
        // Create country combo
        countryCombo = new Combo( selectCountryGroup, SWT.READ_ONLY );
        countryCombo.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        countryCombo.setVisibleItemCount( 15 );
        fillCountryCombo();
        
        selectCountryGroup.pack();
    }
    
    private void fillCountryCombo()
    {
        ArrayList list = Messages.getListFromPropertiesByNaturalOrder( MobileAppConstant.IDC_PROPERTIES, 
                                                                       Global.getLocale() );
    	Iterator it = list.iterator();
    	while ( it.hasNext() )
    	{
    		countryCombo.add( (String) it.next() );
    	}
    	
    	String countryIndex = Preferences.getValue( PreferencesConstant.COUNTRY_NAME );
        int selectionIndex = 0;
    	if ( !countryIndex.equals( "" ) )
    	{
    		selectionIndex = Integer.parseInt( countryIndex );
    	}
    	countryCombo.select( selectionIndex );
    }
    
    private void createSelectMobileGroup( Composite comp )
    {
        // Select Mobile Group
        Group selectMobileGroup = new Group( comp, SWT.SHADOW_ETCHED_IN );
        selectMobileGroup.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        selectMobileGroup.setText( Messages.getString( MessagesConstant.PREFERENCES_DIALOG_MOBILEGROUP_TEXT ) );

        selectMobileGroup.setLayout( new GridLayout( 2, false ) );
        
        // Create manufactory label and combo
        Label manuLabel = new Label( selectMobileGroup, SWT.LEFT );
        manuLabel.setLayoutData( new GridData() );
        manuLabel.setText( Messages.getString( MessagesConstant.PREFERENCES_DIALOG_MANULABEL_TEXT ) );
        manuCombo = new Combo( selectMobileGroup, SWT.READ_ONLY );
        manuCombo.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        
        // Create type label and combo
        Label typeLabel = new Label( selectMobileGroup, SWT.LEFT );
        typeLabel.setLayoutData( new GridData() );
        typeLabel.setText( Messages.getString( MessagesConstant.PREFERENCES_DIALOG_TYPELABEL_TEXT ) );
        typeCombo = new Combo( selectMobileGroup, SWT.READ_ONLY );
        typeCombo.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        
        fillManuTypeCombo();
		
		// Refesh the fillTypeCombo when manuCombo changed
        manuCombo.addSelectionListener( new SelectionAdapter()
		{
			public void widgetSelected( SelectionEvent e )
			{
				String manu = manuCombo.getText();
				typeCombo.removeAll();
				fillTypeComboByManu( manu );
			}
		} );
        
        // Create input mobile label and textbox
        Label inputMobile = new Label(selectMobileGroup, SWT.LEFT);
        inputMobile.setLayoutData(new GridData());
        inputMobile.setText(Messages.getString(MessagesConstant.PREFERENCES_DIALOG_MOBILE_INPUT));
        mobileNumber = new Text(selectMobileGroup, SWT.BORDER);
        mobileNumber.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
    	String number = Preferences.getValue( PreferencesConstant.MOBILE_NUMBER );
    	if (number == null || number.equals("")) {
            mobileNumber.setFocus();
    	} else {
    		mobileNumber.setText(number);
    	}
        
        selectMobileGroup.pack();
    }
    
    private void fillManuTypeCombo()
	{
		MobileFactory mf = MobileFactory.getInstance();
		ArrayList list = mf.listTypes();
		for ( int i = 0; i < list.size(); i++ )
		{
			String item = (String) list.get( i );
			if ( item.indexOf( "_" ) == -1 )
			{
				item = item.substring( 0, 1 ) + item.substring( 1 ).toLowerCase();
				manuCombo.add( item );
			}
		}

		// Set the manuComboText and typeComboText
    	String mobileType = Preferences.getValue( PreferencesConstant.MOBILE_TYPE );
    	String manuComboText = null;
    	String typeComboText = null;
    	if ( mobileType.equals( "" ) )
    	{
    		manuComboText = DEFAULT_MOBILE_TYPE;
    		typeComboText = DEFAULT_MOBILE_TYPE;
    	}
    	else
    	{
    		// Get the string before "_" and INITCAP the result. eg. SIEMENS_S57 to Siemens
    		if ( mobileType.indexOf( "_" ) != -1 )
    		{
    			manuComboText = mobileType.substring( 0, mobileType.indexOf( "_" ) );
    		    manuComboText = manuComboText.substring( 0, 1 ) + manuComboText.substring( 1 ).toLowerCase();
    			typeComboText = manuComboText + mobileType.substring( mobileType.indexOf( "_" ) );
    		}
    		else
    		{
    			manuComboText = mobileType;
    		    manuComboText = manuComboText.substring( 0, 1 ) + manuComboText.substring( 1 ).toLowerCase();
    			typeComboText = DEFAULT_MOBILE_TYPE;
    		}
    	}
    	
    	fillTypeComboByManu( manuComboText );
    	manuCombo.setText( manuComboText );
    	typeCombo.setText( typeComboText );
	}
    
    private void fillTypeComboByManu( String manu )
	{
		MobileFactory mf = MobileFactory.getInstance();
		ArrayList list = mf.listTypes();
		for ( int i = 0; i < list.size(); i++ )
		{
			String item = (String) list.get( i );
			if ( item.indexOf( manu.toUpperCase() + "_" ) != -1 )
			{
				item = manu + item.substring( item.indexOf( "_" ) );
				typeCombo.add( item );
			}
		}
		typeCombo.add( DEFAULT_MOBILE_TYPE );
		typeCombo.setText( typeCombo.getItem( 0 ) );
	}
}
