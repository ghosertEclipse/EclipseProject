package com.loadtrend.app.mobile.data;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TreeSet;

public class Messages
{
	private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle( MobileAppConstant.MESSAGE_PROPERTIES, Global.getLocale() );

	private Messages()
	{
	}
    
    public static void refreshByLocale( Locale locale )
    {
        RESOURCE_BUNDLE = ResourceBundle.getBundle( MobileAppConstant.MESSAGE_PROPERTIES, locale );
    }
    
	public static String getString( String key )
	{
		try
		{
			return RESOURCE_BUNDLE.getString( key );
		}
		catch ( MissingResourceException e )
		{
			return '!' + key + '!';
		}
	}
	
	public static String getString( String key, Object obj0 )
	{
		MessageFormat format = createMessageFormat( key );
		return format.format( new Object[]{ obj0 } );
	}
	
	public static String getString( String key, Object obj0, Object obj1 )
	{
		MessageFormat format = createMessageFormat( key );
		return format.format( new Object[]{ obj0, obj1 } );
	}
	
	public static String getString( String key, Object obj0, Object obj1, Object obj2 )
	{
		MessageFormat format = createMessageFormat( key );
		return format.format( new Object[]{ obj0, obj1, obj2 } );
	}
	
	public static String getString( String key, Object obj0, Object obj1, Object obj2, Object obj3 )
	{
		MessageFormat format = createMessageFormat( key );
		return format.format( new Object[]{ obj0, obj1, obj2, obj3 } );
	}
    
    public static String getString( String key, Object[] objs )
    {
        MessageFormat format = createMessageFormat( key );
        return format.format( objs );
    }
	
	private static MessageFormat createMessageFormat( String key )
	{
		String pattern = getString( key );
		return new MessageFormat( pattern );
	}
    
    
    public static ArrayList getListFromPropertiesByNaturalOrder( String baseName )
    {
        return getListFromPropertiesByNaturalOrder( baseName, Locale.getDefault() );
    }
    
    public static ArrayList getListFromPropertiesByNaturalOrder( String baseName, Locale locale )
    {
        ResourceBundle bundle = ResourceBundle.getBundle( baseName, locale );
        Enumeration enumeration = bundle.getKeys();
        TreeSet ts = new TreeSet( new Comparator()
        {
            public int compare( Object o1, Object o2 )
            {
                return Integer.parseInt( (String) o1 ) - Integer.parseInt( (String) o2 );
            }
        });
        while ( enumeration.hasMoreElements() )
        {
            ts.add( enumeration.nextElement() );
        }
        
        ArrayList list = new ArrayList();
        Iterator it = ts.iterator();
        while ( it.hasNext() )
        {
            String key = (String) it.next();
            list.add( bundle.getString( key ) );
        }
        return list;
    }
    
}
