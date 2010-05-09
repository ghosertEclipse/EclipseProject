/*
 * Created on 2005-3-17
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.mobile;

import java.io.UnsupportedEncodingException;

/**
 * This class is used to parse PDU-text informaiton, and convert it to
 * class Message, but not including "index" and "status"
 * @author Zhang Jiawei
 */
public class MessageProcessor extends Processor
{
    public static final String CLASS_NAME        = "MessageProcessor";
    
    private static final String ISO_10646_UCS_2   = "iso-10646-ucs-2";
    
    // type of Tel Num
    private static char TYPE_OF_NUM               = 0x00;
    private static final int INTERNATIONAL_NUM    = 0x91;
    private static final int NATIONAL_NUM         = 0xA1;
    
    // TP-MTI（TP-Message-Type-Indicator）
    private static char TP_MTI  = 0x00;
    private static final int READ_TYPE_MESSAGE    = 0x00;
    private static final int SENT_TYPE_MESSAGE    = 0x01;
    private static final int SPEC_TYPE_MESSAGE    = 0x02;
    
    // 用户信息编码方式 Data Coding Scheme
    // TP-Data-Coding-Scheme
    private static char TP_DCS                    = 0x00;
    private static final int GSM_7BIT             = 0x00;
    private static final int GSM_8BIT             = 0x04;
    private static final int GSM_UCS2             = 0x08;
    // hand free ( used only when encoding pdu )
    private static final int GSM_HANDFREE_NO      = 0x00;
    private static final int GSM_HANDFREE_YES     = 0x10;
    
    // TP-User-Data-Header-Indicator
    private static char TP_UDHI = 0x00;
    private static final int USER_DATA_HEADER_NO  = 0x00;
    private static final int USER_DATA_HEADER_YES = 0x40;

    // TP-ST
    // TP-Status-Report-Request ( used only when encoding pdu )
    // TP-Protocol-Identifier ( I don't know how to use it as far )
    private static char TP_ST                     = 0x00;
    private static char TP_SPR_YSE                = 0x31;
    private static char TP_SPR_NO                 = 0x11; 
    private static char TP_PID                    = 0x00;
    
    /**
     * Split a long content message to several small content messages
     * If the length of the message is more than 160 none unicode characters or 70 unicode characters
     * @param message the other parameters for the message should have been set before invoking this method.
     * @return Message Array
     */
    public static Message[] splitMessageByContent( Message message )
    {
        Message[] messages = null;
        String content = message.getContent();
        int count = 0;
        int limitedCharacter = 0;
        
        if ( !isContentUnicode( content ) )
        {
            count = (int) Math.ceil( (double) content.length() / 160 );
            limitedCharacter = 160;
        }
        else
        {
            count = (int) Math.ceil( (double) content.length() / 70 );
            limitedCharacter = 70;
        }
        
        if ( count == 0 )
        {
            // The content is ""
            messages = new Message[1];
            messages[0] = (Message) message.clone();
            messages[0].setContent( "" );
        }
        else
        {
            String[] contents = new String[count];
            messages = new Message[count];
            int i = 0;
            for ( ; i < count - 1; i++ )
            {
                messages[i] = (Message) message.clone();
                contents[i] = content.substring( i * limitedCharacter, ( i + 1 ) * limitedCharacter );
                messages[i].setContent( contents[i] );
            }
            messages[i] = (Message) message.clone();
            contents[i] = content.substring( i * limitedCharacter, content.length() );
            messages[i].setContent( contents[i] );
        }
        return messages;
    }
    
    
    
    /**
     * Return the sms count by computing the content.
     * @param content
     * @return
     */
    public static int getMessageCountByContent(String content) {
        if ( !isContentUnicode( content ) )
        {
            return (int) Math.ceil( (double) content.length() / 160 );
        }
        else
        {
            return (int) Math.ceil( (double) content.length() / 70 );
        }
    }
    
    /**
     * Validate the content length of message
     * @param message
     * @return false if the content of your SMS more than 70 unicode characters or 160 non-unicode characters.
     */
    public static boolean messageLengthValidate( Message message )
    {
        String content = message.getContent();
        // Whether the length of Message.content more than the limited
        if ( !isContentUnicode( content ) )
        {
            // Not more than 160 none unicode
            if ( content.length() > 160 ) return false;
        }
        else
        {
            // Not more than 70 unicode
            if ( content.length() > 70 ) return false;
        }
        return true;
    }
    
    /**
     * Return the result whether the content is unicode or not.
     * @param content
     * @return true if the content is unicode, or false
     */
    public static boolean isContentUnicode( String content )
    {
    	if ( content.getBytes().length == content.length() )
    	{
    		return false;
    	}
    	return true;
    }
    
    /**
     * For reading message, to get "messageCenterNum", "teleNum", "receiveTime"(read_type_message),
     * "deliveryValidTime"(sent_type_message), "content", "reserveSign".
     * @param pdus
     * @return Message
     */
    public static Message gsmDecodePdu( String pdus ) throws UnsupportedEncodingException
    {
        Message message = new Message();
        
        StringBuffer pdu = new StringBuffer( pdus );
        char header = 0x00;
        String data = "";
        
        // Get the messageCenterNum
        header = getNextHeader( pdu );
        if ( header != 0x00 )
        {
            TYPE_OF_NUM = getNextHeader( pdu );
            data = getNextData( pdu, ( header - 1 ) * 2 );
            if ( TYPE_OF_NUM == INTERNATIONAL_NUM )
            {
                message.setMessageCenterNum( "+" + gsmDecodeNumbers( data ) );
            }
            else
            {
                message.setMessageCenterNum( gsmDecodeNumbers( data ) );
            }
        }
        
        // TP-MTI（TP-Message-Type-Indicator）
        // TP-User-Data-Header-Indicator
        TP_ST = getNextHeader( pdu );
        TP_MTI = (char) ( TP_ST & 0x03 );
        TP_UDHI = (char) ( TP_ST & 0x40 );
        
        // handler the sent_type_message and spec_type_message
        if ( TP_MTI == SENT_TYPE_MESSAGE || TP_MTI == SPEC_TYPE_MESSAGE )
        {
            getNextHeader( pdu );
        }

        // Get the TeleNum
        header = getNextHeader( pdu );
        if ( ( header & 0x01 ) == 0x01 ) header++;
        TYPE_OF_NUM = getNextHeader( pdu );
        data = getNextData( pdu, header );
        if ( TYPE_OF_NUM == INTERNATIONAL_NUM )
        {
            message.setTeleNum( "+" + gsmDecodeNumbers( data ) );
        }
        else
        {
            message.setTeleNum( gsmDecodeNumbers( data ) );
        }

        
        if ( TP_MTI == READ_TYPE_MESSAGE || TP_MTI == SENT_TYPE_MESSAGE )
        {
            // Get TP-PID (TP-Protocol-Identifier)
            TP_PID = getNextHeader( pdu );
            // Get TP-DCS (Data Coding Scheme)
            TP_DCS = (char) ( getNextHeader( pdu ) & 0x0C );
        }
        
        
        if ( TP_MTI == READ_TYPE_MESSAGE )
        {
            // Get receiveTime
            data = getNextData( pdu, 14 );
            message.setReceiveTime( gsmDecodeNumbers( data ) );
        }
        if ( TP_MTI == SENT_TYPE_MESSAGE )
        {
            // Get DeliveryValidTime
            header = getNextHeader( pdu );
            message.setDeliveryValidTime( header );
        }
        if ( TP_MTI == SPEC_TYPE_MESSAGE )
        {
            // Get receiveTime
            data = getNextData( pdu, 14 );
            message.setReceiveTime( gsmDecodeNumbers( data ) );
            // Get receiveTime
            data = getNextData( pdu, 14 );
            message.setContent( gsmDecodeNumbers( data ) );
            return message;
        }
        
        // Get User Data Length
        getNextHeader( pdu );
        
        // Get reserveSign
        if ( TP_UDHI == USER_DATA_HEADER_YES )
        {
            data = getNextData( pdu, 12 );
            message.setReserveSign( data );
        }
        
        byte[] userDataBytes = gsmStringToBytes( pdu );
        
        // decode text
        if ( TP_DCS == GSM_7BIT )
        {
            message.setContent( gsmDecode7bit( userDataBytes ) );
        }
        if ( TP_DCS == GSM_UCS2 )
        {
            message.setContent( new String( userDataBytes, ISO_10646_UCS_2 ) );
        }
        if ( TP_DCS == GSM_8BIT )
        {
            message.setContent( new String( userDataBytes, 0, userDataBytes.length ) );
        }
        
        return message;
    }
    
    /**
     * For sending and saving message.
     * @param message
     * @return pdu string
     */
    public static String gsmEncodePdu( Message message ) throws UnsupportedEncodingException
    {
        // unsent
        // 08 91683108200105F0 11 00 00 80              00 08 FF 08 62C94E0199996E2F
        StringBuffer pdus = new StringBuffer();
        int length = 0;
        
        // Encode message center number
        String messageCenterNum = message.getMessageCenterNum();
        length = messageCenterNum.length();
        
        if ( length == 0 )
        {
            pdus.append( gsmBytesToString( (byte) length ) );
        }
        else
        {
            if ( messageCenterNum.substring( 0, 1 ).equals( "+" ) )
            {
                // wipe off "+" from length
                length = length - 1;
            }
            if ( ( length & 0x01 ) == 0x01 ) length++;
            length = length / 2 + 1;
            pdus.append( gsmBytesToString( (byte) length ) );
            
            if ( messageCenterNum.substring( 0, 1 ).equals( "+" ) )
            {
                // Encode number as International number and delete "+" from telephone number
                messageCenterNum = messageCenterNum.substring( 1 );
                pdus.append( gsmBytesToString( (byte) INTERNATIONAL_NUM ) );
            }
            else
            {
                // Encode number as national number
                pdus.append( gsmBytesToString( (byte) NATIONAL_NUM ) );
            }
        }
        pdus.append( gsmEncodeNumbers( messageCenterNum ) );
        
        // Encode TP_ST
        if ( message.getDeliveryStatusReport() )
        {
            pdus.append( gsmBytesToString( (byte) TP_SPR_YSE ) );
        }
        else
        {
            pdus.append( gsmBytesToString( (byte) TP_SPR_NO ) );
        }
        
        // Encode TP_MR
        pdus.append( "00" );
        
        // Encode target telephone number
        String targetNum = message.getTeleNum();
        length = targetNum.length();
        if ( !targetNum.equals( "" ) && targetNum.substring( 0, 1 ).equals( "+" ) )
        {
            length = length - 1;
        }
        pdus.append( gsmBytesToString( (byte) length ) );
        if ( !targetNum.equals( "" ) && targetNum.substring( 0, 1 ).equals( "+" ) )
        {
            targetNum = targetNum.substring( 1 );
            pdus.append( gsmBytesToString( (byte) INTERNATIONAL_NUM ) );
        }
        else
        {
            pdus.append( gsmBytesToString( (byte) NATIONAL_NUM ) );
        }
        pdus.append( gsmEncodeNumbers( targetNum ) );
        
        // Encode TP_PID
        pdus.append( "00" );
        
        // Encode TP_DCS
        String content = message.getContent();
        if ( content.getBytes().length == content.length() )
        {
            // none unicode - 7bit encoding
            if ( message.getDeliveryHandFree() )
            {
                pdus.append( gsmBytesToString( (byte) ( GSM_7BIT | GSM_HANDFREE_YES ) ) );
            }
            else
            {
                pdus.append( gsmBytesToString( (byte) ( GSM_7BIT | GSM_HANDFREE_NO ) ) );
            }
        }
        else
        {
            // unicode - ucs2 encoding
            if ( message.getDeliveryHandFree() )
            {
                pdus.append( gsmBytesToString( (byte) ( GSM_UCS2 | GSM_HANDFREE_YES ) ) );
            }
            else
            {
                pdus.append( gsmBytesToString( (byte) ( GSM_UCS2 | GSM_HANDFREE_NO ) ) );
            }
        }
        
        // Encode delivery valid time
        pdus.append( gsmBytesToString( (byte) message.getDeliveryValidTime() ) );
        
        // Encode Data Coding Scheme
        byte[] userDataBytes = null;
        if ( !isContentUnicode( content ) )
        {
            // none unicode - 7bit encoding
            userDataBytes = gsmEncode7bit( content );
            pdus.append( gsmBytesToString( (byte) content.length() ) );
            pdus.append( gsmBytesToString( userDataBytes ) );
        }
        else
        {
            // unicode - ucs2 encoding
            userDataBytes = content.getBytes( ISO_10646_UCS_2 );
            pdus.append( gsmBytesToString( (byte) ( content.length() * 2 ) ) );
            pdus.append( gsmBytesToString( userDataBytes ) );
        }
        
        return pdus.toString();
    }
    
    // convert 683108200105F0 to 8613800210500
    private static String gsmDecodeNumbers( String data )
    {
        if ( data.length() == 0 ) return "";
        
        String TelNum = "";
        int length = data.length();
        for ( int i = 0; i < length; i = i + 2 )
        {
            TelNum = TelNum + data.charAt( i + 1 ) + data.charAt( i );
        }
        if ( TelNum.substring( length - 1 ).equalsIgnoreCase( "F" ) )
        {
            TelNum = TelNum.substring( 0, length - 1 );
        }
        return TelNum;
    }
    
    // convert 8613800210500 to 683108200105F0
    private static String gsmEncodeNumbers( String data )
    {
        if ( data.length() == 0 ) return "";
        
        String TelNum = "";
        int length = data.length();
        
        if ( ( length & 0x01 ) == 0x01 )
        {
            length = length + 1;
            data = data + "F";
        }
        for ( int i = 0; i < length; i = i + 2 )
        {
            TelNum = TelNum + data.charAt( i + 1 ) + data.charAt( i );
        }
        return TelNum;
    }

}
