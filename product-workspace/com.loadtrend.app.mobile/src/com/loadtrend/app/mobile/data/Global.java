package com.loadtrend.app.mobile.data;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.ui.IWorkbenchWindow;

import com.loadtrend.app.mobile.MobilePlugin;
import com.loadtrend.app.mobile.dialog.PreferencesDialog;
import com.loadtrend.app.trees.TreeObject;
import com.loadtrend.app.trees.TreeParent;

import loadtrend.mobile.Message;
import loadtrend.mobile.Mobile;
import loadtrend.mobile.MobileException;
import loadtrend.mobile.MobileFactory;
import loadtrend.mobile.PhoneBook;
import loadtrend.mobile.Port;
import loadtrend.mobile.SerialPort;

public class Global
{
	private static Mobile mobile = null;
	
    private static Locale locale = null;
    
    private static TreeParent smsInvisibleRoot = null;
    
    private static TreeParent pbInvisibleRoot = null;
    
    public static TreeParent getPBInvisibleRoot()
    {
        return pbInvisibleRoot;
    }

    public static void setPBInvisibleRoot( TreeParent pbInvisibleRoot )
    {
        Global.pbInvisibleRoot = pbInvisibleRoot;
    }

    public static TreeParent getSMSInvisibleRoot()
    {
        return smsInvisibleRoot;
    }

    public static void setSMSInvisibleRoot( TreeParent smsInvisibleRoot )
    {
        Global.smsInvisibleRoot = smsInvisibleRoot;
    }

    public static Locale getLocale()
    {
        if ( locale == null )
        {
            return Global.getNewLocale();
        }
        return locale;
    }
    
    public static Locale getNewLocale()
    {
        String value = Preferences.getValue( PreferencesConstant.LANGUAGE );
        String[] values = value.split( "," );
        
        if ( values.length == 3 )
        {
            locale = new Locale( values[1], values[2] );
        }
        else if ( values.length == 2 )
        {
            locale = new Locale( values[1] );
        }
        else
        {
            locale = Locale.ENGLISH;
        }
        Global.locale = locale;
        return locale;
    }
    
    /**
     * Get mobile instance if exists or create mobile instance.
     * @return
     */
	public static Mobile getOrCreateMobile()
	{
		if ( mobile == null )
		{
			return Global.createMobile();
		}
		return mobile;
	}
    
    /**
     * Create mobile instance.
     * @return
     */
	public static Mobile createMobile()
	{
		try
		{
			MobileFactory mf = MobileFactory.getInstance();
			Port port = SerialPort.getInstance();
			String mobileType = Preferences.getValue( PreferencesConstant.MOBILE_TYPE );
			String mobileNumber = Preferences.getValue( PreferencesConstant.MOBILE_NUMBER );
			if ( mobileType.equals( "" ) || mobileNumber == null || mobileNumber.equals(""))
			{
                IWorkbenchWindow window = MobilePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
				int code = new PreferencesDialog(window.getShell(), null).open();
				if ( code == IDialogConstants.CANCEL_ID )
					return null;
			}
			mobileType = Preferences.getValue( PreferencesConstant.MOBILE_TYPE );
			Mobile mobile = mf.createMobile( port, mobileType );
			Global.mobile = mobile;
		}
		catch ( MobileException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mobile;
	}
	
    public static Mobile getMobile() {
        return mobile;
    }
	
    public static Collection getLocalPBCollection()
    {
        TreeObject localMachine = Global.getPBInvisibleRoot().getChildren( Messages.getString( MessagesConstant.LOCALMACHINE_TEXT ) );
        TreeObject pbBox = ( (TreeParent) localMachine ).getChildren( Messages.getString( MessagesConstant.PBBOX_TEXT ) );
        TreeObject pbDraft = ( (TreeParent) localMachine ).getChildren( Messages.getString( MessagesConstant.DRAFT_TEXT ) );
        Collection pbCollection = new LinkedHashSet();
        pbCollection.addAll( pbBox.getCollection() );
        pbCollection.addAll( pbDraft.getCollection() );
        return pbCollection;
    }
    
    public static String getNameOrNumber( Message message )
    {
        if ( message == null ) return "";
        
        Iterator it = Global.getLocalPBCollection().iterator();
        while ( it.hasNext() )
        {
            PhoneBook phoneBook = (PhoneBook) it.next();
            if ( phoneBook.getTeleNum().equals( message.getTeleNum() ) )
            {
                return phoneBook.getName();
            }
        }
        return message.getTeleNum();
    }
}
