/*
 * Created on 2005-6-19
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.mobile;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Processor
{
    /**
     * Get the values between startString and endString from portValues, then cut the processed data for further processing
     * eg. values -> "\r\n+CMGL: 1,2,,92\r\n"; getStringFromTokens( values, " ", "," ) -> "1"; values -> ",2,,92\r\n";
     * This method affect on this.portValues
     * @param portValues
     * @param startString 
     * @param endString
     * @return String "" if there is no results found
     */
    public static String getStringFromTokens( StringBuffer portValues, String startString, String endString )
    {
        int i = portValues.indexOf( startString );
        int j = portValues.indexOf( endString, i + startString.length() );
        if ( i == -1 || j == -1 )
        {
            portValues = null;
            return "";
        }
        String string = portValues.substring( i + startString.length(), j );
        portValues.delete( 0, j );
        return string;
    }
    
    /**
     * Get the values between startString and endString from portValues, then cut the startString & endString data for further processing
     * eg. values -> "\r\n+CMGL: 1,2,,92\r\n"; getStringFromTokens( values, " ", "," ) -> "1"; values -> "\r\n+CMGL:2,,92\r\n";
     * This method affect on this.portValues
     * @param portValues
     * @param startString 
     * @param endString
     * @return String "" if there is no results found
     */
    public static String getRidOfString( StringBuffer portValues, String startString, String endString )
    {
        int i = portValues.indexOf( startString );
        int j = portValues.indexOf( endString, i + startString.length() );
        if ( i == -1 || j == -1 )
        {
            return "";
        }
        String string = portValues.substring( i + startString.length(), j );
        portValues.delete(i, j + endString.length());
        return string;
    }
    
    /**
     * Get the values between startString and endString from portValues, then remain the startString & endString data for further processing
     * eg. values -> "\r\n+CMGL: 1,2,,92\r\n"; getStringFromTokens( values, " ", "," ) -> "1"; values -> "\r\n+CMGL: 1,2,,92\r\n";
     * This method affect on this.portValues
     * @param portValues
     * @param startString 
     * @param endString
     * @return String "" if there is no results found
     */
    public static String getRemainOfString( StringBuffer portValues, String startString, String endString )
    {
        int i = portValues.indexOf( startString );
        int j = portValues.indexOf( endString, i + startString.length() );
        if ( i == -1 || j == -1 )
        {
            return "";
        }
        String string = portValues.substring( i + startString.length(), j );
        return string;
    }
    
	// 7-BIT
	// H翻译成1001000，i翻译成1101001，显然H的二进制编码不足八位
	// 那么就将i的最后一位补足到H的前面。那么就成了11001000（C8）
	// i剩下六位110100，前面再补两个0，变成00110100（34），于是“Hi”
	// 就变成了两个八进制数 C8 34
	// "Hello!"
    // 1001000 1100101 1101100 1101100 1101111 0100001
    // 11001000 00110010 10011011 11111101 1110 1
	// "C8329BFD0E01"
    public static byte[] gsmEncode7bit( String string )
    {
        int length = string.length();
        byte[] bytes = string.getBytes();
        byte[] byteBuffer = new byte[length];
        int resultLength = ( length / 8 ) * 7 + ( length % 8 );
        byte[] resultBytes = new byte[resultLength];
        int c = 0x00;

        // reserve the data that should be added to the former byte
        for ( int i = 0; i < length; i++ )
        {
            byteBuffer[i] = (byte) ( bytes[i] & (int) ( Math.pow( 2, ( i % 8 ) ) - 1 ) );
        }

        // add the reserved data to the former byte, except the first one of eighth
        int i = 0, j = 0;
        for ( ; i < length; i++ )
        {
            if ( ( i % 8 ) == 0 ) continue;
            c = byteBuffer[i] << ( 8 - i % 8 );
            bytes[i - 1] = (byte) ( bytes[i - 1] >> ( ( i - 1 ) % 8 ) );
            resultBytes[j] = (byte) ( c | bytes[i - 1] );
            j++;
        }
        if ( ( length % 8 ) != 0 )
        {
            resultBytes[j] = (byte) ( bytes[length - 1] >> ( j % 7 ) );
        }

        return resultBytes;
    }

    public static String gsmDecode7bit( byte[] bytes )
    {
        int length = bytes.length;
        int resultLength = ( length / 7 ) * 8 + ( length % 7 );
        
        // 7 bytes means 7 strings or 8 strings, we should know the exact number
        if ( length > 0 && resultLength % 8 == 0 )
        {
            // means 7 string if the last byte is 0x00 or 0x01
            if ( bytes[length - 1] == 0x00 || bytes[length - 1] == 0x01 )
            {
                resultLength = resultLength - 1;
            }
        }
        byte[] byteBuffer = new byte[resultLength];
        byte[] resultBytes = new byte[resultLength];
        int c = 0x00;

        // get the data you reserved on each bytes
        for ( int i = 0, j = 0; i < resultLength; i++ )
        {
            if ( i % 8 == 0 )
            {
                byteBuffer[i] = 0x00;
                continue;
            }
            c = ( bytes[j] < 0 ) ? ( bytes[j] + 256 ) : bytes[j];
            byteBuffer[i] = (byte) ( c >> ( 7 - j % 7 ) );
            j++;
        }

        // act the reserved data on bytes to decode data
        for ( int i = 0, j = 0; i < resultLength; i++, j++ )
        {
            c = ( bytes[j] < 0 ) ? (bytes[j] + 256) : bytes[j];
            c = c & (int) ( Math.pow( 2, ( 7 - j % 7 ) ) - 1 );
            bytes[j] = (byte) ( c << ( j % 7 ) );
            resultBytes[i] = (byte) ( bytes[j] | byteBuffer[i] );
            if ( ( j + 1 ) % 7 == 0 )
            {
                i++;
                if ( i >= resultLength ) break;
                resultBytes[i] = byteBuffer[i];
            }
        }

        return new String( resultBytes );
    }
	
    // convert StringBuffer "0891683108200105F0..." to bytes[] {0x08, 0x91, 0x68, 0x31, ...}
    public static byte[] gsmStringToBytes( StringBuffer pdu )
    {
        char nextChar = 0x00;
        int length = pdu.length() / 2;
        byte[] bytes = new byte[length];
        
        for ( int i = 0; i < length; i++ )
        {
            nextChar = getNextHeader( pdu );
            bytes[i] = (byte) nextChar;
        }
        return bytes;
    }
    
	// convert bytes[] {0x08, 0x91, 0x68, 0x31, ...} to StringBuffer "0891683108200105F0..."
    public static StringBuffer gsmBytesToString( byte[] bytes )
	{
	    int length = bytes.length;
	    StringBuffer pdu = new StringBuffer();
		for ( int i = 0; i < length; i++ )
		{
			if ( bytes[i] < 0 )
			{
				pdu.append( Integer.toHexString( bytes[i] + 256 ) );
			}
			if ( bytes[i] >= 0 && bytes[i] <= 0x0F )
			{
				pdu.append( "0" + Integer.toHexString( bytes[i] ) );
			}
			if ( bytes[i] > 0x0F )
			{
				pdu.append( Integer.toHexString( bytes[i] ) );
			}
		}
		return new StringBuffer( pdu.toString().toUpperCase() );
	}
	
	public static StringBuffer gsmBytesToString( byte b )
	{
	    byte[] bytes = new byte[1];
	    bytes[0] = b;
	    return gsmBytesToString( bytes );
	}
	
    // convert "0891683108200105F0..." to "91683108200105F0..."
    // and return the first two number "08" with the char form 0x80
    public static char getNextHeader( StringBuffer pdu )
    {
        if ( pdu.length() < 2 ) return 0x00;
        
        char header = (char) Short.valueOf( pdu.substring( 0, 2 ), 16 ).shortValue();
        pdu.delete( 0, 2 );
        return header;
    }
    
    // cut and return the string with specified length from specified StringBuffer
    public static String getNextData( StringBuffer pdu, int length )
    {
        if ( pdu.length() < length ) return "";
        
        String data = pdu.substring( 0, length );
        pdu.delete( 0, length );
        return data;
    }
}
