/*
 * Created on 2005-6-22
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.mobile.ericsson;

import java.io.UnsupportedEncodingException;

import loadtrend.mobile.Mobile;
import loadtrend.mobile.MobileException;
import loadtrend.mobile.Port;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Ericsson extends Mobile
{
    public Ericsson( Port port ) throws MobileException
    {
        super( port );
    }
    
    public Ericsson( Port port, String type ) throws MobileException
    {
        super( port, type );
    }
    
    protected void phoneBookEncodeFormat() throws MobileException
    {
        super.setMobileEncodeFormat( Mobile.ENCODE_UTF8_FORMAT );
    }
    
    protected String phoneBookNameDecoder( String name ) throws MobileException
    {
        try
        {
            byte[] bytes = name.getBytes( "ISO-8859-1" );
            return new String( bytes, "UTF-8" );
        }
        catch ( UnsupportedEncodingException uee )
        {
            throw new MobileException( uee.getMessage() );
        }
    }
    
    /**
     * This method could be over-write by sub class of Mobile
     * @throws MobileException
     */
    protected String phoneBookNameEncoder( String name ) throws MobileException
    {
        try
        {
            byte[] bytes = name.getBytes( "UTF-8" );
            return new String( bytes, "ISO-8859-1" );
        }
        catch ( UnsupportedEncodingException uee )
        {
            throw new MobileException( uee.getMessage() );
        }
    }
}
