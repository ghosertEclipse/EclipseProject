package com.loadtrend.app.mobile.util;

import loadtrend.mobile.Message;
import loadtrend.mobile.MessageProcessor;
import loadtrend.mobile.Mobile;
import loadtrend.mobile.MobileConfig;

import com.loadtrend.app.mobile.data.Global;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;

public class SpecialEntity
{
	public static String encode( String originalString )
	{
		originalString = originalString.replaceAll( "&", "&amp;" );
		originalString = originalString.replaceAll( "\"", "&quot;");
		originalString = originalString.replaceAll( "<", "&lt;");
		originalString = originalString.replaceAll( ">", "&gt;");
		originalString = originalString.replaceAll( "\r\n", "<br></br>" );
        originalString = originalString.replaceAll( "\r", "<br></br>" );
        originalString = originalString.replaceAll( "\n", "<br></br>" );

		return originalString;
	}
    
    /**
     * Format the Date & Time from SMS time format: 051231120059 to 05-12-31 12:00:59
     * @param string
     * @return
     */
    public static String formatDateTime( String string )
    {
        String resultString = "";
        
        for ( int i = 0; string.length() >= 2; i++ )
        {
            if ( i == 6 ) break;
            
            resultString += string.substring( 0, 2 );
            string = string.substring( 2 );
            switch ( i )
            {
                case 0:
                    resultString += "-";
                    break;
                case 1:
                    resultString += "-";
                    break;
                case 2:
                    resultString += " ";
                    break;
                case 3:
                    resultString += ":";
                    break;
                case 4:
                    resultString += ":";
                    break;
            }
        }
        return resultString;
    }
    
    /**
     * Get SMS Status Description(Internationalized) by pass the SMS status param.
     * @param smsStatusParam
     *        <li>Message.UNREAD_STATUS</li>
     *        <li>Message.READ_STATUS</li>
     *        <li>Message.UNSENT_STATUS</li>
     *        <li>Message.SENT_STATUS</li>
     * @return
     */
    public static String getSMSStatusDesc( int smsStatusParam )
    {
        String smsStatusDesc = "";
        
        switch ( smsStatusParam )
        {
            case Message.UNREAD_STATUS:
                smsStatusDesc = Messages.getString( MessagesConstant.UNREAD_STATUS );
                break;
            case Message.READ_STATUS:
                smsStatusDesc = Messages.getString( MessagesConstant.READ_STATUS );
                break;
            case Message.UNSENT_STATUS:
                smsStatusDesc = Messages.getString( MessagesConstant.UNSENT_STATUS );
                break;
            case Message.SENT_STATUS:
                smsStatusDesc = Messages.getString( MessagesConstant.SENT_STATUS );
                break;
        }
        
        return smsStatusDesc;
    }
    
    /**
     * Get SMS memo param by pass the SMS memo description.
     * @param smsMemoDesc
     * @return <li>Mobile.SMS_ME_MEMO</li>
     *         <li>Mobile.SMS_SM_MEMO</li>
     */
    public static String getSMSMemoParam( String smsMemoDesc )
    {
        String smsMemoParam = "";

        if ( smsMemoDesc.startsWith( Messages.getString( MessagesConstant.MOBILE_TEXT ) ) )
        {
            smsMemoParam = Mobile.SMS_ME_MEMO;
        }
        else if ( smsMemoDesc.startsWith( Messages.getString( MessagesConstant.SIMCARD_TEXT ) ) )
        {
            smsMemoParam = Mobile.SMS_SM_MEMO;
        }
        
        return smsMemoParam;
    }
    
    /**
     * Get PB memo param by pass the PB memo description.
     * @param pbMemoDesc
     * @return <li>Mobile.PB_ME_MEMO</li>
     *         <li>Mobile.PB_SM_MEMO</li>
     */
    public static String getPBMemoParam( String pbMemoDesc )
    {
        String smsMemoParam = "";

        if ( pbMemoDesc.startsWith( Messages.getString( MessagesConstant.MOBILE_TEXT ) ) )
        {
            smsMemoParam = Mobile.PB_ME_MEMO;
        }
        else if ( pbMemoDesc.startsWith( Messages.getString( MessagesConstant.SIMCARD_TEXT ) ) )
        {
            smsMemoParam = Mobile.PB_SM_MEMO;
        }
        
        return smsMemoParam;
    }
    
    /**
     * Get SMS memo description by pass the SMS memo param.
     * @param smsMemoParam
     *        <li>Mobile.SMS_ME_MEMO</li>
     *        <li>Mobile.SMS_SM_MEMO</li>
     * @return
     */
    public static String getSMSMemoDesc( String smsMemoParam )
    {
        String smsMemoDesc = "";
        
        if ( smsMemoParam.equals( Mobile.SMS_ME_MEMO ) )
        {
            smsMemoDesc = Messages.getString( MessagesConstant.MOBILE_TEXT );
        }
        else if ( smsMemoParam.equals( Mobile.SMS_SM_MEMO ) )
        {
            smsMemoDesc = Messages.getString( MessagesConstant.SIMCARD_TEXT );
        }
        else
        {
            smsMemoDesc = Messages.getString( MessagesConstant.LOCALMACHINE_TEXT );
        }
        
        return smsMemoDesc;
    }
    
    /**
     * Get PB memo description by pass the PB memo param.
     * @param pbMemoParam
     *        <li>Mobile.PB_ME_MEMO</li>
     *        <li>Mobile.PB_SM_MEMO</li>
     * @return
     */
    public static String getPBMemoDesc( String pbMemoParam )
    {
        String pbMemoDesc = "";
        
        if ( pbMemoParam.equals( Mobile.PB_ME_MEMO ) )
        {
            pbMemoDesc = Messages.getString( MessagesConstant.MOBILE_TEXT );
        }
        else if ( pbMemoParam.equals( Mobile.PB_SM_MEMO ) )
        {
            pbMemoDesc = Messages.getString( MessagesConstant.SIMCARD_TEXT );
        }
        else
        {
            pbMemoDesc = Messages.getString( MessagesConstant.LOCALMACHINE_TEXT );
        }
        
        return pbMemoDesc;
    }
    
    /**
     * Check the phonebook name and number.
     * @param name The name should be checked.
     * @param number The number should be checked.
     * @param pbMemoParam
     *        <li>Mobile.PB_ME_MEMO</li>
     *        <li>Mobile.PB_SM_MEMO</li>
     * @return The error description if error occurs or "" will be return.
     */
    public static String checkPhoneBookNameNumber( String name, String number, String pbMemoParam )
    {
        MobileConfig mobileConfig = Global.getOrCreateMobile().getMobileConfig();
        
        boolean isUnicode = MessageProcessor.isContentUnicode( name );
        int maxLenOfText = 0;
        String memoType = "";
        String txtEnglishOrNot = Messages.getString( MessagesConstant.NEWPB_DIALOG_LBINPUTSTATUS_ENG );
        int maxLenOfNumber = 0;
        
        if ( pbMemoParam.equals( Mobile.PB_ME_MEMO ) )
        {
            if ( isUnicode )
            {
                maxLenOfText = mobileConfig.getMaxLenOfUnicodePBTextInME();
                txtEnglishOrNot = Messages.getString( MessagesConstant.NEWPB_DIALOG_LBINPUTSTATUS_NONENG );
            }
            else
            {
                maxLenOfText = mobileConfig.getMaxLenOfPBTextInME();
            }
            maxLenOfNumber = mobileConfig.getMaxLenOfPBNumberInME();
            memoType = Messages.getString( MessagesConstant.MOBILE_TEXT );
        }
        
        if ( pbMemoParam.equals( Mobile.PB_SM_MEMO ) )
        {
            if ( isUnicode )
            {
                maxLenOfText = mobileConfig.getMaxLenOfUnicodePBTextInSM();
                txtEnglishOrNot = Messages.getString( MessagesConstant.NEWPB_DIALOG_LBINPUTSTATUS_NONENG );
            }
            else
            {
                maxLenOfText = mobileConfig.getMaxLenOfPBTextInSM();
            }
            maxLenOfNumber = mobileConfig.getMaxLenOfPBNumberInSM();
            memoType = Messages.getString( MessagesConstant.SIMCARD_TEXT );
        }
        
        if ( maxLenOfText != 0 && name.length() > maxLenOfText )
        {
            return Messages.getString( MessagesConstant.NEWPB_DIALOG_LBINPUTSTATUS_NAMETOOLONG_TEXT, 
                                       memoType, 
                                       txtEnglishOrNot, 
                                       new Integer( maxLenOfText ) );
        }
        else if ( name.length() == 0 )
        {
            return Messages.getString( MessagesConstant.NEWPB_DIALOG_LBINPUTSTATUS_NAMEMANDATORY_TEXT );
        }
        else if ( maxLenOfNumber != 0 && number.length() > maxLenOfNumber )
        {
            return Messages.getString( MessagesConstant.NEWPB_DIALOG_LBINPUTSTATUS_NUMBERTOOLONG_TEXT, 
                                                       memoType, 
                                                       new Integer( maxLenOfNumber ) );
        }
        else if ( number.length() == 0 )
        {
            return Messages.getString( MessagesConstant.NEWPB_DIALOG_LBINPUTSTATUS_NUMBERMANDATORY_TEXT );
        }
        else if ( !number.matches( "[0-9*+#]*" ) )
        {
            return Messages.getString( MessagesConstant.NEWPB_DIALOG_LBINPUTSTATUS_NUMBERVALIDATION_TEXT );
        }
        else
        {
            return "";
        }
    }
}
