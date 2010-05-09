package com.loadtrend.app.mobile.action;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;

import com.loadtrend.app.mobile.data.Global;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MobileAppConstant;
import com.loadtrend.app.mobile.data.Preferences;
import com.loadtrend.app.mobile.data.PreferencesConstant;

public class ChangeLanguageAction extends Action
{
	private ArrayList list = null;
	
    public ChangeLanguageAction(final IWorkbenchWindow window)
    {
        super( "Languages", AS_DROP_DOWN_MENU );
        super.setAccelerator( SWT.CTRL | 'L' );
        super.setToolTipText( "Choose your native language." );
        super.setMenuCreator( new IMenuCreator()
        {
            public Menu getMenu( Menu parent )
            {
                Menu languageSubMenu = new Menu( parent );

                list = Messages.getListFromPropertiesByNaturalOrder( MobileAppConstant.LANGUAGES_PROPERITES );
                
                String language = Global.getLocale().getLanguage();
                String country = Global.getLocale().getCountry();
                
                final MenuItem[] items = new MenuItem[ list.size() ];
                for ( int i = 0; i < list.size(); i++ )
                {
                    String value = (String) list.get( i );
                    String[] values = value.split( "," );
                    
                    if ( values.length != 2 && values.length != 3 ) continue;
                    
                    items[i] = new MenuItem( languageSubMenu, SWT.RADIO );
                    items[i].setText( values[0] );
                    
                    // set selection true
                    if ( values.length == 2 )
                    {
                    	if ( language.equals( values[1] ) && country.equals( "" ) ) items[i].setSelection( true );
                    }
                    else if ( values.length == 3 )
                    {
                    	if ( language.equals( values[1] ) && country.equals( values[2]) ) items[i].setSelection( true );
                    }
                }
                items[0].addSelectionListener( new SelectionAdapter()
                {
                    public void widgetSelected( SelectionEvent e )
                    {
                        for ( int j = 0; j < items.length; j++ )
                        {
                            if ( items[j].getSelection() )
                            {
                                Preferences.setValue( PreferencesConstant.LANGUAGE, (String) list.get( j ) );
                                ChangeLanguageAction.this.storeLocaleToIni( (String) list.get( j ) );
                                
                                MessageBox mb = new MessageBox( window.getShell(), SWT.YES | SWT.NO );
                                mb.setText( "Languages" );
                                mb.setMessage( "Your setting will be applied next time when the application startup," +
                                               "Do you want to close the application now?" );
                                if ( mb.open() == SWT.YES )
                                {
                                    window.getWorkbench().close();
                                }
                                break;
                            }
                        }
                    }
                } );
                return languageSubMenu;
            }
        
            public Menu getMenu( Control parent )
            {
                return null;
            }
        
            public void dispose()
            {
            }
        });
    }

    private void storeLocaleToIni( String localeString )
    {
        // Get the languange_COUNTRY from localeString
        String[] values = localeString.split( "," );
        
        String languageCountry = "";
        
        // set selection true
        if ( values.length == 2 )
        {
            languageCountry = values[1];
        }
        else if ( values.length == 3 )
        {
            languageCountry = values[1] + "_" + values[2];
        }
        
        // add -nl language_COUNTRY parameter to application ini file
        File file = new File( MobileAppConstant.JAVA_ROOT_PATH + MobileAppConstant.APP_CONFIG_INI );
        if ( file.exists() )
        {
            try
            {
                String string = "-nl\r\n";
                string = string + languageCountry + "\r\n";
                string = string + "-perspective\r\n";
                string = string + "com.loadtrend.app.mobile.perspectives.ShortMessagePerspective\r\n";
                
                
                FileWriter writer = new FileWriter( file );
                writer.write( string );
                writer.close();
            }
            catch ( IOException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
//    Translate interface text without need to restart application.
//    private void changeInterface()
//    {
//        Messages.refreshByLocale( Global.getNewLocale() );
//        IMenuManager menuBar = Global.getWindowConfigurer().getActionBarConfigurer().getMenuManager();
//        menuBar.removeAll();
//        ApplicationActionBarAdvisor advisor = new ApplicationActionBarAdvisor( Global.getWindowConfigurer().getActionBarConfigurer() );
//        advisor.createActions( Global.getWindow() );
//        advisor.fillMenuBar( menuBar );
//        menuBar.updateAll( true );
//    }
    
    public void run()
    {
    }
}
