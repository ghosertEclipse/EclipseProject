/*
 * Created on 2005-3-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.mobile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;

import javax.comm.CommPortIdentifier;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.UnsupportedCommOperationException;

/**
 * @author Jiawei_Zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SerialPort extends Port
{
    private static final int OPEN_PORT_OVERTIME = 2000;
    private static final int SERIAL_PORT_BAUDRATE = 9600;
    private static final int READ_BUFFER_SIZE = 20;
    
    private javax.comm.SerialPort jSerialPort = null;
    private OutputStream outputStream = null;
    private InputStream inputStream = null;

    private static SerialPort serialPort = null;
    
    private SerialPort()
    {
    }
    
    /**
     * Singleton pattern design
     */
    public static SerialPort getInstance()
    {
        if ( serialPort == null )
        {
            serialPort = new SerialPort();
        }
        return serialPort;
    }
    
    /**
     * Using this method firstly before you open the third-part
     * program to build bluetooth COM port connection with your
     * bluetooth device.
     * @param portName the name of the port: "COM1", "COM5"
     * @return void if successful, or throws PortException
     * @throws PortException
     */
    public void openPort( String portName ) throws PortException
    {
        try
        {
            CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier( portName );
            if ( portId.getPortType() == CommPortIdentifier.PORT_SERIAL )
            {
                try
                {
                    jSerialPort = (javax.comm.SerialPort) portId.open( this.getClass().getName(), OPEN_PORT_OVERTIME );
                    outputStream = jSerialPort.getOutputStream();
                    inputStream = jSerialPort.getInputStream();
                    jSerialPort.notifyOnDataAvailable( true );
                    jSerialPort.setSerialPortParams( SERIAL_PORT_BAUDRATE,
                                                     javax.comm.SerialPort.DATABITS_8,
                                                     javax.comm.SerialPort.STOPBITS_1,
                                                     javax.comm.SerialPort.PARITY_NONE
                                                   );
                }
                catch (  PortInUseException piue )
                {
                    throw new PortException( piue.getMessage() );
                }
                catch ( IOException ioe )
                {
                    throw new PortException( ioe.getMessage() );
                }
                catch ( UnsupportedCommOperationException ucoe )
                {
                    throw new PortException( "Port currently opened is not supported communication operation" );
                }
            }
        }
        catch ( NoSuchPortException nspe )
        {
            throw new PortException( "Port currently opened does not exist" );
        }
        catch ( Exception e )
        {
            throw new PortException( "Unknown port exception" );
        }
        return;
    } 

    /**
     * Close the port that opened by method openPort()
     */
    public void closePort()
    {
        if ( jSerialPort != null )
        {
            jSerialPort.close();
        }
    }
    

    /**
     * Read the port that opened by method openPort()
     * @return the content that read from port, "" if no data
     * @throws PortException
     */
    public StringBuffer readPort() throws PortException
    {
        byte[] readByteBuffer = new byte[READ_BUFFER_SIZE];
        StringBuffer readStringBuffer = new StringBuffer( "" );
        
        try
        {
            while ( inputStream.available() > 0 )
            {
                int num = inputStream.read( readByteBuffer );
                // "ISO-8859-1" designed for Ericsson.phoneBookNameDecoder method
                readStringBuffer.append( new String( readByteBuffer, 0, num, "ISO-8859-1" ) );
            }
        }
        catch ( IOException ioe )
        {
            throw new PortException( ioe.getMessage() );
        }
        catch ( Exception e )
        {
            throw new PortException( e.getMessage() );
        }
        return readStringBuffer;
    }
    
    /**
     * Read the port that opened by method openPort()
     * @param checkDataOverTime spcified milliseconds in which there is no data come from port,
     *        forces to stop reading port and return the result.
     * @return the content that read from port, "" if no data
     * @throws PortExcepiton
     */
    public StringBuffer readPort( int checkDataOverTime ) throws PortException
    {
        String[] stringBreakSign = new String[0];
        return readPort( stringBreakSign, checkDataOverTime );
    }
    
    /**
     * Read the port that opened by method openPort()
     * @param stringBreakSign spcified sign array, once one of them equals to the data come from port,
     *        forces to stop reading port and return the result.
     * @param checkDataOverTime spcified milliseconds in which there is no data come from port,
     *        forces to stop reading port and return the result.
     * @return the content that read from port, "" if no data
     * @throws PortException
     */
    public StringBuffer readPort( String[] stringBreakSign, int checkDataOverTime ) throws PortException
    {
        byte[] readByteBuffer = new byte[READ_BUFFER_SIZE];
        StringBuffer readStringBuffer = new StringBuffer( "" );
        
        long startTime = System.currentTimeMillis();
        boolean breakSign = true;
        int stringBreakSignNum = stringBreakSign.length;
        
        try
        {
            while ( true )
            {
                // avoid suspending
                Thread.sleep( 10 );
                if ( System.currentTimeMillis() - startTime > checkDataOverTime )
                {
                    if ( breakSign == false )
                    {
                        breakSign = true;
                        startTime = System.currentTimeMillis();
                    }
                    else
                    {
                        break;
                    }
                }
                while ( inputStream.available() > 0 )
                {
                    int num = inputStream.read( readByteBuffer );
                    //System.out.println( num );
                    // "ISO-8859-1" designed for Ericsson.phoneBookNameDecoder method
                    readStringBuffer.append( new String( readByteBuffer, 0, num, "ISO-8859-1" ) );
                    //System.out.print( new String(readBuffer) );
                    for ( int i = 0; i < stringBreakSignNum; i++ )
                    {
                        if ( readStringBuffer.lastIndexOf( stringBreakSign[i] ) != -1 )
                        {
                            return readStringBuffer;
                        }
                    }
                    breakSign = false;
                }
            }
        }
        catch ( IOException ioe )
        {
            throw new PortException( ioe.getMessage() );
        }
        catch ( Exception e )
        {
            throw new PortException( e.getMessage() );
        }
        return readStringBuffer;
    }
    
    public void writePort( String content ) throws PortException
    {
        try
        {
            outputStream.write( content.getBytes( "ISO-8859-1" ) );
            outputStream.flush();
        }
        catch ( IOException ioe )
        {
            throw new PortException( ioe.getMessage() );
        }
    }
    
    /**
     * Auto detected the port, there will be no port opened after run the method
     * @return the list of available serial port name on the PC
     */
    public ArrayList monitorPort()
    {
        Set set = new TreeSet();
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        CommPortIdentifier portId = null;
        
        while ( portList.hasMoreElements() )
        {
            portId = (CommPortIdentifier) portList.nextElement();
            if ( portId.getPortType() == CommPortIdentifier.PORT_SERIAL )
            {
            	if (portId.getName().length() > 3) {
                    set.add( Integer.valueOf(portId.getName().substring(3)));
            	}
            } 
        }
        
        ArrayList list = new ArrayList();
        Integer[] ports = (Integer[]) set.toArray(new Integer[]{});
        for (int i = 0; i < ports.length; i++ ) {
        	list.add("COM" + ports[i].toString());
        }
        return list;
    }
}
