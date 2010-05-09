/*
 * Created on 2005-7-4
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.mobile.util;

import java.util.Enumeration;
import java.util.Properties;
import java.util.PropertyResourceBundle;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PropertiesReader
{
	private static PropertiesReader reader = null;

	private PropertiesReader()
	{
	}

	public static PropertiesReader getInstance()
	{
		if ( reader == null )
		{
			reader = new PropertiesReader();
		}
		return reader;
	}

	public Properties read( String fileName )
	{
        PropertyResourceBundle esProperties = null;
        Properties properties               = null;
        Enumeration keys                    = null;
        String keyTemp                      = null;

        // Create object of PropertyResourceBundle for the filename

        esProperties =
        ( PropertyResourceBundle )
             PropertyResourceBundle.getBundle( fileName );

        properties = new Properties();

        // Call getKeys method of PropertyResourceBundle to get all keys
        keys = esProperties.getKeys();

        // Using the key name, get corresponding value and populate
        // properties object
        while( keys.hasMoreElements() )
        {
            keyTemp = (String) keys.nextElement();
            properties.setProperty( keyTemp,
                                    esProperties.getString( keyTemp ) );
            keyTemp = null;
        }

        return properties;
    }
}
