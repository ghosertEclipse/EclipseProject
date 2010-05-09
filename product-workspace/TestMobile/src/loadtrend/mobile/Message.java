/*
 * Created on 2005-3-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.mobile;

import java.io.Serializable;

/**
 * @author Jiawei_Zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Message implements Serializable, Cloneable, Comparable
{
    public static final String CLASS_NAME = "Message";
    
    public static final int UNREAD_STATUS  = 0;
    public static final int READ_STATUS    = 1;
    public static final int UNSENT_STATUS  = 2;
    public static final int SENT_STATUS    = 3;
    public static final int ALL_STATUS     = 4;
    
    public static final int FIVE_MINUTES_VALIDTIME = 0x00;
    public static final int ONE_HOUR_VALIDTIME     = 0x0B;
    public static final int TWELVE_HOURS_VALIDTIME = 0x8F;
    public static final int ONE_DAY_VALIDTIME      = 0xA7;
    public static final int ONE_WEEK_VALIDTIME     = 0xC4;
    public static final int MAX_VALIDTIME          = 0xFF;
    
    private int index                      = -1;              // 
    private int status                     = -1;              // 消息状态：0-未读，1-已读，2-未发，3-已发
    private String messageCenterNum        = "";              // 服务中心号码
    private String teleNum                 = "";              // 目标方号码/发送方号码
    private String receiveTime             = "";              // 服务时间戳(status=0,1时需要填充）
    private boolean deliveryStatusReport   = false;           // 状态报告(status=2,3时需要填充）
    private int deliveryValidTime          = MAX_VALIDTIME;   // 有效期(status=2,3时需要填充）
    private boolean deliveryHandFree       = false;           // 免提(status=2,3时需要填充）
    private String content                 = "";              // 一百六十英文/七十汉字的短消息内容
    private String reserveSign             = "";              // 连锁短消息标志
    private String pdu                     = "";              // raw pdu for the message
    
    public Message()
    { 
    }
    
    /**
     * Implement Cloneable Interface
     */
    public Object clone()
    {
        try
        {
            return super.clone();
        }
        catch ( CloneNotSupportedException cnse )
        {
            return null;
        }
    }
    
    /**
     * Implement Comparable Interface
     * For using TreeSet to store this object, the order will be followed by this method ( default order by index ),
     * or you will be failure to store this object if you don't implement comparable interface
     */
	public int compareTo( Object object )
	{
		Message message = (Message) object;
		if ( this.index < message.getIndex() ) return -1;
		if ( this.index > message.getIndex() ) return 1;
        return 0;
	}

    /**
     * Rewrite the equals method for using LinkedHashSet.
     */
    public boolean equals( Object obj )
    {
        // Step 1
        if ( this == obj ) return true;
        
        // Step 2
        if ( obj == null ) return false;
        
        // Step 3
        if ( this.getClass() != obj.getClass() ) return false;
        
        // Step 4
        Message message = (Message) obj;
        
        if ( this.receiveTime.equals( message.getReceiveTime() ) && 
             this.status == message.getStatus() && 
             this.teleNum.equals( message.getTeleNum() ) &&
             this.content.equals( message.getContent() ) )
        {
                  return true;
        }
        
        return false;
    }

    /**
     * Rewrite hashCode if rewrite the equals( Object ) method.
     */
    public int hashCode()
    {
        int result = 17;
        
        result = result * 37 + this.receiveTime.hashCode();
        result = result * 37 + this.teleNum.hashCode();
        result = result * 37 + this.content.hashCode();
        result = result * 37 + this.status;
        
        return result;
    }

    /**
     * @return Returns the index.
     */
    public int getIndex()
    {
        return index;
    }
    
    /**
     * No need to invoke by client programmer
     * @param index The index to set.
     */
    public void setIndex(int index)
    {
        this.index = index;
    }
    
    /**
     * @return Returns the messageCenterNum.
     */
    public String getMessageCenterNum()
    {
        return messageCenterNum;
    }
    
    /**
     * @param messageCenterNum The messageCenterNum to set.
     */
    public void setMessageCenterNum(String messageCenterNum)
    {
        this.messageCenterNum = messageCenterNum;
    }
    
    /**
     * @return Returns the content.
     */
    public String getContent()
    {
        return content;
    }
    
    /**
     * @param content The content to set.
     */
    public void setContent(String content)
    {
        this.content = content;
    }
    
    /**
     * @return Returns the deliveryStatusReport.
     */
    public boolean getDeliveryStatusReport()
    {
        return deliveryStatusReport;
    }
    
    /**
     * @param deliveryStatusReport The deliveryStatusReport to set.
     */
    public void setDeliveryStatusReport(boolean deliveryStatusReport)
    {
        this.deliveryStatusReport = deliveryStatusReport;
    }
    
    /**
     * @return Returns the deliveryHandFree.
     */
    public boolean getDeliveryHandFree()
    {
        return deliveryHandFree;
    }
    
    /**
     * @param deliveryHandFree The deliveryHandFree to set.
     */
    public void setDeliveryHandFree(boolean deliveryHandFree)
    {
        this.deliveryHandFree = deliveryHandFree;
    }
    
    /**
     * @return Returns the deliveryValidTime.
     */
    public int getDeliveryValidTime()
    {
        return deliveryValidTime;
    }
    
    /**
     * @param deliveryValidTime The deliveryValidTime to set.
     *        <li>Message.FIVE_MINUTES_VALIDTIME = 0x00;
     *        <li>Message.ONE_HOUR_VALIDTIME     = 0x0B;
     *        <li>Message.TWELVE_HOURS_VALIDTIME = 0x8F;
     *        <li>Message.ONE_DAY_VALIDTIME      = 0xA7;
     *        <li>Message.ONE_WEEK_VALIDTIME     = 0xC4;
     *        <li>Message.MAX_VALIDTIME          = 0xFF;
     */
    public void setDeliveryValidTime(int deliveryValidTime)
    {
        this.deliveryValidTime = deliveryValidTime;
    }
    
    /**
     * @return Returns the receiveTime.
     */
    public String getReceiveTime()
    {
        return receiveTime;
    }
    
    /**
     * No need to invoke by client programmer
     * @param receiveTime The receiveTime to set.
     */
    public void setReceiveTime(String receiveTime)
    {
        this.receiveTime = receiveTime;
    }
    
    /**
     * @return Returns the reserveSign.
     */
    public String getReserveSign()
    {
        return reserveSign;
    }
    
    /**
     * No need to invoke by client programmer
     * @param reserveSign The reserveSign to set.
     */
    public void setReserveSign(String reserveSign)
    {
        this.reserveSign = reserveSign;
    }
    
    /**
     * @return Returns the status.
     */
    public int getStatus()
    {
        return status;
    }
    
    /**
     * No need to invoke by client programmer
     * @param status The status to set.
     */
    public void setStatus(int status)
    {
        this.status = status;
    }
    
    /**
     * @return Returns the teleNum.
     */
    public String getTeleNum()
    {
        return teleNum;
    }
    
    /**
     * @param teleNum The teleNum to set.
     */
    public void setTeleNum(String teleNum)
    {
        this.teleNum = teleNum;
    }
    
    /**
     * @return String raw pdu for the message
     */
    public String getPdu()
    {
        return pdu;
    }
    
    /**
     * No need to invoke by client programmer
     * @param String raw pdu for the message
     */
    public void setPdu( String pdu )
    {
        this.pdu = pdu;
    }
}
