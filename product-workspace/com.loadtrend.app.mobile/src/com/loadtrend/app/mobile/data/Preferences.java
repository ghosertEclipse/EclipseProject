package com.loadtrend.app.mobile.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

public class Preferences
{
	private static String fileName = MobileAppConstant.JAVA_ROOT_PATH + MobileAppConstant.CURRENT_PREFERENCE_PROPERTIES;
	private static Properties properties = loadProperties();

	private Preferences()
	{
	}
	
	private static Properties loadProperties()
	{
		Properties properties = new Properties();
		try
		{
            File file = new File( fileName );
            if ( file.exists() )
            {
                properties.load( new FileInputStream( fileName ) );
            }
            else
            {
                ResourceBundle bundle = ResourceBundle.getBundle( MobileAppConstant.PREFERENCE_PROPERTIES );
                Enumeration enumeration = bundle.getKeys();
                while ( enumeration.hasMoreElements() )
                {
                    String key = (String )enumeration.nextElement();
                    properties.setProperty( key, bundle.getString( key ) );
                }
            }
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
		return properties;
	}

	/**
	 * Get the preference value with specified key.
	 * @param key
	 * @return preference value
	 */
	public static String getValue( String key )
	{
		return properties.getProperty( key );
	}
	
	/**
	 * Set preference value with specified key, also the pair key-value will be store to corresponding properties file
	 * @param key preference key
	 * @param value preference value
	 */
	public static void setValue( String key, String value )
	{
		properties.setProperty( key, value );
		storeToProperties();
	}
	
	private static void storeToProperties()
	{
		try
		{
			properties.store( new FileOutputStream( fileName ), null );
		}
		catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
