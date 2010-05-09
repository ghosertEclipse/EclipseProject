/*
 * Created on 2005-6-1
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.mobile.test;

import java.util.ArrayList;

import junit.framework.TestCase;
import loadtrend.mobile.Port;
import loadtrend.mobile.PortException;
import loadtrend.mobile.SerialPort;
import loadtrend.mobile.util.BlockOperation;
import loadtrend.mobile.util.BlockOperationUtil;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SerialPortTester extends TestCase
{
    private Port port = null;
    
    private static final int timeout = 5000;
    
    public static void main( String[] args )
    {
        junit.textui.TestRunner.run( SerialPortTester.class );
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        port = SerialPort.getInstance();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }
    
    public void testMonitorPort()
    {
        ArrayList list = port.monitorPort();
        System.out.println( list );
    }
    
    // This test case is used to solved the writing block problem:
    // when writing data to unknown COM, such as some virtual bluetooth COM,
    // the thread will block on port.writePort method.
    public void testWritingBlockProblem() throws PortException, InterruptedException
    {
        String[] stringBreakSign = new String[] { "\r\nOK\r\n", "\r\nERROR\r\n" };
        
        port.openPort( "COM10" );
        
        final String atCommand = "AT+CMGC=?\r";
        // String atCommand = "AT+CPBR=%s\r".replaceAll( "%s", "9" );
        // String atCommand = "AT+CCLK?\r";
        
        boolean isSuccessWriting = BlockOperationUtil.start(new BlockOperation() {
			public void run() throws PortException {
				port.writePort(atCommand);
			}
        }, timeout);
        if (isSuccessWriting) {
            StringBuffer portValue = port.readPort( stringBreakSign, 1000 );
            System.out.println( portValue );
        } else {
            System.out.println("Writing blocked.");
        }
        
        port.closePort();
    }
    
//    public void testATCommand() throws PortException
//    {
//        String[] stringBreakSign = new String[] { "\r\nOK\r\n", "\r\nERROR\r\n" };
//        
//        port.openPort( "COM1" );
//        
//        String atCommand = "AT+CMGC=?\r";
//        // String atCommand = "AT+CCLK?\r";
//        
//        port.writePort( atCommand );
//        StringBuffer portValue = port.readPort( stringBreakSign, 10000 );
//        
//        System.out.println( portValue );
//        
//        port.closePort();
//        
//    }
    
//    public void testReadMessageATCommand() throws PortException
//    {
//          String[] stringBreakSign = new String[] { "\r\nOK\r\n", "\r\nERROR\r\n" };
//          
//          port.openPort( "COM9" );
//          
//          String atCommand = "AT+CPMS=%s,%s\r".replaceAll( "%s", Mobile.SMS_ME_MEMO );
//          port.writePort( atCommand );
//          StringBuffer portValue = port.readPort( stringBreakSign, 10000 );
//          System.out.println( portValue );
//          
//          atCommand = "AT+CMGL=4\r";
//          port.writePort( atCommand );
//          portValue = port.readPort( stringBreakSign, 10000 );
//          System.out.println( portValue );
//          
//          port.closePort();
//    }
}
