/*
 * Created on 2005-7-4
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.mobile;

import java.util.MissingResourceException;
import java.util.Properties;

import loadtrend.mobile.util.PropertiesReader;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MobileConfig
{
	private static final String CONFIG_PROPERTIES_PREFIX = "CONFIG_";
	private static final String DEFAULT_MOBILE_TYPE = "OTHER";

    private Properties properties = null;
    private String type = null;
    
    /**
     * Instance MobileConfig by invoking the default mobile type for no parameter constructor.
     */
	public MobileConfig()
	{
	    this( DEFAULT_MOBILE_TYPE );
	}
	
    /**
     * Instance MobileConfig by invoking the specified mobile type. If the mobile type does not exist,
     * specified mobile type with DEFAULT_MOBILE_TYPE.
     * @param type The key in Mobile_Type.properties.
     */
	public MobileConfig( String type )
	{
		PropertiesReader reader = PropertiesReader.getInstance();
        
        try
        {
            properties = reader.read( CONFIG_PROPERTIES_PREFIX + type );
            this.type = type;
        }
        catch ( MissingResourceException mre )
        {
            properties = reader.read( CONFIG_PROPERTIES_PREFIX + DEFAULT_MOBILE_TYPE );
            this.type = DEFAULT_MOBILE_TYPE;
        }
	}
	
	public int getMaxLenOfPBTextInSM()
	{
	    String key = "PBTEXT_MAXLEN_SM";
	    return getIntValue( key );
	}
	
	public int getMaxLenOfPBNumberInSM()
	{
	    String key = "PBNUMBER_MAXLEN_SM";
	    return getIntValue( key );
	}
	public int getMaxLenOfUnicodePBTextInSM()
	{
	    String key = "UNICODEPBTEXT_MAXLEN_SM";
	    return getIntValue( key );
	}
	public int getMaxLenOfPBTextInME()
	{
	    String key = "PBTEXT_MAXLEN_ME";
	    return getIntValue( key );
	}
	public int getMaxLenOfPBNumberInME()
	{
	    String key = "PBNUMBER_MAXLEN_ME";
	    return getIntValue( key );
	}
	public int getMaxLenOfUnicodePBTextInME()
	{
	    String key = "UNICODEPBTEXT_MAXLEN_ME";
	    return getIntValue( key );
	}
	
	public String getNewMessageArrivalEnableATCommand()
	{
	    String key = "NEWMESSAGEARRIVAL_ENABLE_ATCOMMAND";
	    return getStringValue( key );
	}
	
	public String getNewMessageArrivalDisableATCommand()
	{
	    String key = "NEWMESSAGEARRIVAL_DISABLE_ATCOMMAND";
	    return getStringValue( key );
	}
	
	public boolean canListDialledPhoneBooks()
	{
	    String key = "LISTDIALLEDPHONEBOOKS";
	    return getBooleanValue( key );    
	}
	
	public boolean canSupportPhoneBookInME()
	{
	    String key = "SUPPORTPHONEBOOK_ME";
	    return getBooleanValue( key );
	}
	
	private boolean getBooleanValue( String key )
	{
	    String value = getStringValue( key );
	    boolean booleanValue = Boolean.getBoolean( value );
	    return booleanValue;
	}
	
	private int getIntValue( String key )
	{
	    String value = getStringValue( key );
		int intValue = Integer.parseInt( value );
		return intValue;
	}
	
	private String getStringValue( String key )
	{
		String value = properties.getProperty( key );
		if ( value == null )
		{
            throw new IllegalArgumentException( "No Key: " + key + " is found in properties file: " + CONFIG_PROPERTIES_PREFIX + type );
		}
		return value;
	}

}
