/*
 * Created on 2005-7-4
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.mobile;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;

import loadtrend.mobile.util.PropertiesReader;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MobileFactory
{
	private static final String MOBILE_TYPE_PROPERTIES = "Mobile_Type";
	
	private static final String DEFAULT_MOBILE_TYPE = "OTHER";

    private Properties properties = null;
    
    private static MobileFactory factory = null;
	
	private MobileFactory()
	{
		PropertiesReader reader = PropertiesReader.getInstance();
        properties = reader.read( MOBILE_TYPE_PROPERTIES );
	}
	
	public static MobileFactory getInstance()
	{
		if ( factory == null )
		{
			factory = new MobileFactory();
		}
		return factory;
	}

	public Mobile createMobile( Port port ) throws MobileException
	{
		return createMobile( port, DEFAULT_MOBILE_TYPE );
	}

    /**
     * Create mobile instance with the parameter Port, type
     * @param port Port name.
     * @param type The Key in Mobile_Type.properties file
     * @return Mobile
     * @throws MobileException
     */
	public Mobile createMobile( Port port, String type ) throws MobileException
	{
		Mobile mobile = null;
		String fullClassName = null;

		fullClassName  = properties.getProperty( type );

		if ( fullClassName == null )
		{
		    throw new IllegalArgumentException( "Type: " + type + " is not found." );
		}

		try
		{
		    Class c = Class.forName( fullClassName );
		    Class[] pTypes = new Class[] { Port.class, String.class };
		    Constructor constructor = c.getConstructor(pTypes);
		    Object[] args = new Object[] { port, type };
		    mobile = (Mobile) constructor.newInstance( args );
		    return mobile;
        }
		catch( ClassNotFoundException cnfe )
		{
		    throw new MobileException( cnfe );
		}
		catch ( NoSuchMethodException nsme )
		{
		    throw new MobileException( nsme );
		}
		catch ( InvocationTargetException ite )
		{
		    throw new MobileException( ite );
		}
		catch ( InstantiationException ie )
		{
		    throw new MobileException( ie );
		}
		catch ( IllegalAccessException iae )
		{
		    throw new MobileException( iae );
		}
	}

	public ArrayList listTypes()
	{
		ArrayList list = new ArrayList();

		Enumeration enu = properties.propertyNames();
		
		list = Collections.list( enu );
		
		Collections.sort( list );
		
		int index = list.indexOf( DEFAULT_MOBILE_TYPE );
		list.remove( index );
		list.add( DEFAULT_MOBILE_TYPE );

		return list;
	}

}
