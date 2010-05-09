/*
 * Created on 2005-6-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.mobile.siemens;

import loadtrend.mobile.Mobile;
import loadtrend.mobile.MobileException;
import loadtrend.mobile.Port;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Siemens extends Mobile
{
    public Siemens( Port port ) throws MobileException
    {
        super( port );
    }
    
    public Siemens( Port port, String type ) throws MobileException
    {
        super( port, type );
    }
    
}
