/*
 * Created on 2005-3-3
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.mobile;

/**
 * @author Zhang Jiawei
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */

public class MobileInfo
{
    private String nameOfManu    = "";

    private String nameOfTele    = "";

    private String verOfSW       = "";

    private String valueOfIMEI   = "";

    // Change in per cent
    private String batteryCharge = "";

    // From -113dBm to -51dBm or more
    private String signalQuality = "";

    private String ownerTeleNum  = "";
    
    

    /**
     * @return Returns the batteryCharge.
     */
    public String getBatteryCharge()
    {
        return batteryCharge;
    }
    /**
     * @param batteryCharge The batteryCharge to set.
     */
    public void setBatteryCharge( String batteryCharge )
    {
        this.batteryCharge = batteryCharge;
    }
    /**
     * @return Returns the nameOfManu.
     */
    public String getNameOfManu()
    {
        return nameOfManu;
    }
    /**
     * @param nameOfManu The nameOfManu to set.
     */
    public void setNameOfManu( String nameOfManu )
    {
        this.nameOfManu = nameOfManu;
    }
    /**
     * @return Returns the nameOfTele.
     */
    public String getNameOfTele()
    {
        return nameOfTele;
    }
    /**
     * @param nameOfTele The nameOfTele to set.
     */
    public void setNameOfTele( String nameOfTele )
    {
        this.nameOfTele = nameOfTele;
    }
    /**
     * @return Returns the ownerTeleNum.
     */
    public String getOwnerTeleNum()
    {
        return ownerTeleNum;
    }
    /**
     * @param ownerTeleNum The ownerTeleNum to set.
     */
    public void setOwnerTeleNum( String ownerTeleNum )
    {
        this.ownerTeleNum = ownerTeleNum;
    }
    /**
     * @return Returns the signalQuality.
     */
    public String getSignalQuality()
    {
        return signalQuality;
    }
    /**
     * @param signalQuality The signalQuality to set.
     */
    public void setSignalQuality( String signalQuality )
    {
        this.signalQuality = signalQuality;
    }
    /**
     * @return Returns the valueOfIMEI.
     */
    public String getValueOfIMEI()
    {
        return valueOfIMEI;
    }
    /**
     * @param valueOfIMEI The valueOfIMEI to set.
     */
    public void setValueOfIMEI( String valueOfIMEI )
    {
        this.valueOfIMEI = valueOfIMEI;
    }
    /**
     * @return Returns the verOfSW.
     */
    public String getVerOfSW()
    {
        return verOfSW;
    }
    /**
     * @param verOfSW The verOfSW to set.
     */
    public void setVerOfSW( String verOfSW )
    {
        this.verOfSW = verOfSW;
    }
}
