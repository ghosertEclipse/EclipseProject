/*
 * Created on 2005-3-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.mobile;

import java.util.ArrayList;

/**
 * @author Jiawei_Zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class Port {
    
    /**
     * @param portName the name of the port: "COM1", "COM5"
     * @return void
     * @throws PortException
     */
    public abstract void openPort( String portName ) throws PortException;
    
    /**
     * Close the port that opened by method openPort()
     */
    public abstract void closePort();
    
    /**
     * Read the port that opened by method openPort()
     * @return the content that read from port, "" if no data
     * @throws PortException
     */
    public abstract StringBuffer readPort() throws PortException;
    
    /**
     * Read the port that opened by method openPort()
     * @param checkDataOverTime spcified milliseconds in which there is no data come from port,
     *        forces to stop reading port and return the result.
     * @return the content that read from port, "" if no data
     * @throws PortException
     */
    public abstract StringBuffer readPort( int checkDataOverTime ) throws PortException;
    
    /**
     * Read the port that opened by method openPort()
     * @param stringBreakSign spcified sign array, once one of them equals to the data come from port,
     *        forces to stop reading port and return the result.
     * @param checkDataOverTime spcified milliseconds in which there is no data come from port,
     *        forces to stop reading port and return the result.
     * @return the content that read from port, "" if no data
     * @throws PortException
     */
    public abstract StringBuffer readPort( String[] stringBreakSign, int checkDataOverTime ) throws PortException;
    
    /**
     * Write data to port if no response.
     * @param content The content that you want to write to the port that opened by the method openPort()
     * @throws PortException
     */
    public abstract void writePort( String content ) throws PortException;
    
    /**
     * Auto detected the port, there will be no port opened after run the method
     * @return the list of available serial port name on the PC
     */
    public abstract ArrayList monitorPort();
}
