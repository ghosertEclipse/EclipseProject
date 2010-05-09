/*
 * Created on 2005-3-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.mobile;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import loadtrend.mobile.util.BlockOperation;
import loadtrend.mobile.util.BlockOperationUtil;

/**
 * @author Jiawei_Zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Mobile
{
    public static final String CLASS_NAME = "Mobile";
    
    public static final String AT_COMMAND_OK = "\r\nOK\r\n";
    public static final String AT_COMMAND_ERROR = "\r\nERROR\r\n";
    public static final String MESSAGE_PREPARED = "\r\n> ";
    
    // SIM storage for SMS
    // Mobile Equipment storage for SMS
    // Any of the storages associated with ME
    public static final String SMS_SM_MEMO = "\"SM\"";
    public static final String SMS_ME_MEMO = "\"ME\"";
    public static final String SMS_MT_MEMO = "\"MT\"";
    
    // storage type for phonebook
    public static final String PB_SM_MEMO  = "\"SM\"";
    public static final String PB_ME_MEMO  = "\"ME\"";
    public static final String PB_DC_MEMO  = "\"DC\"";
    public static final String PB_MC_MEMO  = "\"MC\"";
    public static final String PB_RC_MEMO  = "\"RC\"";
    
    // Mobile encode format
    public static final String ENCODE_GSM_FORMAT = "\"GSM\"";
    public static final String ENCODE_UCS2_FORMAT = "\"UCS2\"";
    public static final String ENCODE_UTF8_FORMAT = "\"UTF-8\"";
    public static final String ENCODE_HEX_FORMAT = "\"HEX\"";
    
    // Telephone number type
    private static final String INTERNATIONAL_NUM  = "145";
    private static final String NATIONAL_NUM       = "129";
    
    private int smsSpaceOfSM = 0;
    private int smsSpaceOfME = 0;
    
    private Port port = null;
    private MobileConfig mobileConfig = null;
    private static final String DEFAULT_MOBILE_TYPE = "OTHER";
    
    // listen port thread
    private ListenPortThread listenPortThread = null;
    
    // The Channel class to put and take the port values.
    private Channel channel = null;
    
    // Message Arrival Work Thread
    private MessageArrivalWorkThread[] messageArrivalWorkThreads = null;
    
    private List messageArrivalListeners = new ArrayList();
    
    private StartOrStopMessageArrivalListener startOrStopMessageArrivalListener = null;
    
    private boolean isConnecting = false;
    
    public Mobile( Port port )
    {
        this( port, DEFAULT_MOBILE_TYPE );
    }
    
    public Mobile( Port port, String type )
    {
        this.port = port;
        this.mobileConfig = new MobileConfig( type );
    }
    
    /**
     * Get MobileConfig
     * @return
     */
    public MobileConfig getMobileConfig()
    {
        return mobileConfig;
    }
    
    /**
     * Open the specified mobile port
     * @param portName "COM1", "COM2", "COM3" etc.
     * @return void
     * @throws MobileException
     */
    public synchronized void openMobilePort( String portName ) throws MobileException
    {
        try
        {
        	boolean isMobilePort = this.isMobilePort(portName);
        	if (!isMobilePort) throw new MobileException("Not Mobile Port.");
            port.openPort( portName );
            if ( listenPortThread == null || listenPortThread.isShutdown() == true ) {
                channel = new Channel();
                listenPortThread = new ListenPortThread( port, channel );
                listenPortThread.start();
            }
            this.isConnecting = true;
            return;
        }
        catch ( PortException pe ) {
            throw new MobileException( pe.getMessage() );
        }
        catch ( MobileException me ) {
        	if (listenPortThread != null && listenPortThread.isShutdown() == false) {
	            listenPortThread.shutdown();
	            listenPortThread = null;
        	}
            port.closePort();
            throw me;
        }
    }
    
    /**
     * Close the port that opened by OpenMobilePort method
     */
    public synchronized void closeMobilePort() throws MobileException
    {
        this.removeAllMessageArrivalListener();
        this.stopMessageArrivalWorkThread();
        if (listenPortThread != null && listenPortThread.isShutdown() == false) {
            listenPortThread.shutdown();
        }
        if ( port != null ) {
            port.closePort();
        }
        this.isConnecting = false;
    }
    
    /**
     * Search and find the available mobile port, make sure that the port returned by this method need to be opened
     * with the openMobilePort method once again.
     * @return portName or null if failed to get available port
     */
    public synchronized String monitorMobilePort()
    {
        ArrayList list = port.monitorPort();
        Iterator it = list.iterator();

        while ( it.hasNext() ) {
            String portName = (String) it.next();
        	boolean isMobilePort = this.isMobilePort(portName);
        	if (isMobilePort) return portName;
        	continue;
        }
        return null;
    }
    
    public synchronized boolean isMobilePort(final String portName) {
    	try {
    		// open port blocking handler
    		boolean isSuccessOperation = BlockOperationUtil.start(new BlockOperation() {
				public void run() throws PortException {
	                port.openPort( portName );
				}
    		}, 5000);
    		if (!isSuccessOperation) return false;
    		// write port blocking handler
    		isSuccessOperation = BlockOperationUtil.start(new BlockOperation() {
				public void run() throws PortException {
					port.writePort("AT+CMGC=?\r");
				}
    		}, 5000);
			if (!isSuccessOperation) {
				port.closePort();
				return false;
			}
	        StringBuffer portValues = port.readPort( new String[]{ AT_COMMAND_OK, AT_COMMAND_ERROR }, 1000 );
	        port.closePort();
	        if ( portValues.lastIndexOf( AT_COMMAND_OK ) == -1 ) {
	        	return false;
	        }
	        return true;
    	} catch (PortException e) {
		} catch (InterruptedException e) {
		}
		return false;
    }
    
    /**
     * Specifies the SMS memory type and get the number of the SMS.
     * This method will affect readMessage, listMessage, saveMessage and deleteMessage method.
     * 
     * @param smsMemoType
     *            <p>Mobile.SMS_SM_MEMO Mobile.SMS_ME_MEMO
     * @return the number of the SMS in the memo that specified by this method
     * @throws MobileException
     */
    public synchronized int setSMSMemoGetSMSNum( String smsMemoType ) throws MobileException
    {
        String atCommand = "AT+CPMS=%s,%s\r".replaceAll( "%s", smsMemoType );
        
        StringBuffer portValues = this.receiveATCommandOKResult( atCommand );
        String smsNum = Processor.getStringFromTokens( portValues, " ", "," );
        String smsSpace = Processor.getStringFromTokens( portValues, ",", "," );
        if ( smsMemoType.equals( Mobile.SMS_SM_MEMO ) )
        {
            this.smsSpaceOfSM = Integer.parseInt( smsSpace );
        }
        else
        {
            this.smsSpaceOfME = Integer.parseInt( smsSpace );
        }
        return Integer.parseInt( smsNum );
    }
    
    /**
     * Get total SMS space by specified SMS memory type
     * @param smsMemoType
     *            <p>Mobile.SMS_SM_MEMO Mobile.SMS_ME_MEMO
     * @return total SMS space number
     * @throws MobileException
     *         <p> IllegalArgumentException parameter passed error
     */
    public int getSmsSpace( String smsMemoType ) throws MobileException
    {
        if ( smsMemoType.equals( Mobile.SMS_SM_MEMO ) )
        {
            if ( this.smsSpaceOfSM == 0 ) this.setSMSMemoGetSMSNum( smsMemoType );
            return this.smsSpaceOfSM;
        }
        if ( smsMemoType.equals( Mobile.SMS_ME_MEMO ) )
        {
            if ( this.smsSpaceOfME == 0 ) this.setSMSMemoGetSMSNum( smsMemoType );
            return this.smsSpaceOfME;
        }
        throw new IllegalArgumentException( "Parameter passed error" );
    }
    
    /**
     * Check whether the space is enough to save more SMS in the specified SMS memory type
     * Actually, invoke this method will set the SMS memory type at the same time
     * @param num number of the SMS you want to save to mobile
     * @param smsMemoType the SMS memory type you want to save
     *        <p>Mobile.SMS_SM_MEMO Mobile.SMS_ME_MEMO
     * @return the capability of the space
     * @throws MobileException
     */
    public int checkSmsSpace( int num, String smsMemoType ) throws MobileException
    {
        int used = this.setSMSMemoGetSMSNum( smsMemoType );
        int total = this.getSmsSpace( smsMemoType );
        return total - num - used;
    }
    
    /**
     * Lists all the Messages in the mobile and read message data from mobile
     * @return messageData TreeSet, default order by index
     * @throws MobileException
     * @see listMessages( int messageType )
     */
    public TreeSet listAllMessages(String smsMemoType) throws MobileException
    {
        return listMessages(Message.ALL_STATUS, smsMemoType);
    }
     
    /**
     * Lists the Messages that its status are specified and read message data from mobile
     * @param messageType
     *        Message.UNREAD_STATUS
              Message.READ_STATUS
              Message.UNSENT_STATUS
              Message.SENT_STATUS
              Message.ALL_STATUS
     * @param smsMemoType
     *            <p>Mobile.SMS_SM_MEMO Mobile.SMS_ME_MEMO
     * @return messageData TreeSet, default order by index
     */
    public synchronized TreeSet listMessages(int messageType, String smsMemoType) throws MobileException {
        this.setSMSMemoGetSMSNum(smsMemoType);
        return listMessages(messageType);
    }
    
    private TreeSet listMessages( int messageType ) throws MobileException
    {
        String atCommand = "AT+CMGL=%s\r".replaceAll( "%s", String.valueOf( messageType ) );
        
        StringBuffer portValues = this.receiveATCommandOKResult( atCommand );
        
        String indexSign = null;
        int index = 0;
        int status = 0;
        String pdus = null;
        Message message = null;
        TreeSet ts = new TreeSet();
        
        while ( true )
        {
            indexSign = Processor.getStringFromTokens( portValues, " ", "," );
            if ( indexSign.equals( "" ) )
                break;
            index = Integer.parseInt( indexSign );
            status = Integer.parseInt( Processor.getStringFromTokens( portValues, ",", "," ) );
            pdus = Processor.getStringFromTokens( portValues, "\r\n", "+" );
            if ( !pdus.equals( "" ) )
            {
                if ( pdus.lastIndexOf( "\r\n" ) != -1 ) pdus = pdus.substring( 0, pdus.length() - 2 );
            }
            else
            {
                pdus = Processor.getStringFromTokens( portValues, "\r\n", "\r\n" );
            }
            try
            {
                message = MessageProcessor.gsmDecodePdu( pdus );
            }
            catch ( UnsupportedEncodingException uee )
            {
                throw new MobileException( uee.getMessage() );
            }
            message.setIndex( index );
            message.setStatus( status );
            message.setPdu( pdus );
            ts.add( message );
        }
        return ts;
    }
    
    /**
     * Read the sms with the specified index and sms memo type.
     * @param index
     * @param smsMemoType
     *            <p>Mobile.SMS_SM_MEMO Mobile.SMS_ME_MEMO
     * @return Message
     * @throws MobileException
     */
    public synchronized Message readMessage( int index, String smsMemoType) throws MobileException
    {
        this.setSMSMemoGetSMSNum(smsMemoType);
        return readMessage(index);
    }
    
    
    public Message readMessage( int index ) throws MobileException {
        
        String atCommand = "AT+CMGR=%s\r".replaceAll( "%s", String.valueOf( index ) );
        
        StringBuffer portValues = this.receiveATCommandOKResult( atCommand );
        
        int status = 0;
        String pdus = null;
        Message message = null;
        status = Integer.parseInt( Processor.getStringFromTokens( portValues, " ", "," ) );
        pdus = Processor.getStringFromTokens( portValues, "\r\n", "\r\n" );
        try
        {
            message = MessageProcessor.gsmDecodePdu( pdus );
        }
        catch ( UnsupportedEncodingException uee )
        {
            throw new MobileException( uee.getMessage() );
        }
        message.setIndex( index );
        message.setStatus( status );
        message.setPdu( pdus );
        return message;
        
    }
    
    /**
     * **********************************Send Message**********************************************************
     */
    
    /**
     * Send the specified content to the specified telephone number, and if the content is very long (more than the specified sms count: iCount),
     * the method will send the iCount messages to the target number, and in the last message(the iCountTH message),
     * the continue prompt specified will be added.
     * @param continuePrompt the format should be "....{0}....", then the sign {0} will be replaced with the count of the remained messages.
     * @return String[]
     * <li>String[0] the content has been sent.
     * <li>String[1] the remained content is not sent and will be sent next time. "" if there is no remained content.
     */
    public String[] sendMessage(String teleNum, String content, String continuePrompt, int iCount) throws MobileException {
        StringBuffer contentRemain = new StringBuffer();
        Message newMessage = new Message();
        newMessage.setContent(content);
        newMessage.setTeleNum(teleNum);
        Message[] messages = MessageProcessor.splitMessageByContent(newMessage);
        int iRemainSMSCount = messages.length - iCount;
        if (iRemainSMSCount > 0) {
            int messageContentLimited = 0;
            String messageContent = messages[iCount - 1].getContent();
            if (MessageProcessor.isContentUnicode(messageContent) ||
                MessageProcessor.isContentUnicode(continuePrompt)) {
                messageContentLimited  = 70;
            } else {
                messageContentLimited  = 160;
            }
            
            int lastIndex = messageContentLimited - continuePrompt.length();
            // compute the remain sms content and store it to the continueSMSContentMap.
            contentRemain.append(messageContent.substring(lastIndex));
            for (int i = 0; i < iRemainSMSCount; i++){
                contentRemain.append(messages[iCount + i].getContent());
            }
            // recompute the replace the {0} in the continue prompt.
            iRemainSMSCount = MessageProcessor.getMessageCountByContent(contentRemain.toString());
            continuePrompt = MessageFormat.format(continuePrompt, new Object[]{String.valueOf(iRemainSMSCount)});
            // add the continue prompt to last sms content.
            String contentTobeSend = messageContent.substring(0, lastIndex) + continuePrompt;
            messages[iCount - 1].setContent(contentTobeSend);
        }
        
        // Get the messages to be send.
        int iToBeSentMessages = messages.length <= iCount ? messages.length : iCount;
        Message[] toBeSentMessages = new Message[iToBeSentMessages];
        System.arraycopy(messages, 0, toBeSentMessages, 0, iToBeSentMessages);
        
        // Send them.
        this.sendMessage(toBeSentMessages);
        
        // Return result String[]
        String[] strings = new String[2];
        strings[0] = "";
        for (int i = 0; i < toBeSentMessages.length; i++) {
            strings[0] = strings[0] + toBeSentMessages[i].getContent();
        }
        strings[1] = contentRemain.toString();
        return strings;
    }
    
    /**
     * Send SMS Collection to target mobile number, the length of content is not limited, the function will
     * split each long message to several standard messages
     * 
     * @see sendMessage( Message[] message )
     */
    public void sendMessage( Collection collection ) throws MobileException
    {
        Message[] messages = (Message[]) collection.toArray( new Message[0] );
        sendMessage( messages );
        return;
    }
    
    /**
     * Send one SMS to target mobile numbers, the length of content is not limited, the function will
     * split each long message to several standard messages
     * 
     * @see sendStandardMessage( Message message )
     */
    public void sendMessage( Message message, String[] teleNum ) throws MobileException
    {
        int numOfTeleNum = teleNum.length;
        Message[] messages = MessageProcessor.splitMessageByContent( message );
        int numOfContent = messages.length;
        for ( int i = 0; i < numOfTeleNum; i++ )
        {
            for ( int j = 0; j < numOfContent; j++ )
            {
                messages[j].setTeleNum( teleNum[i] );
                sendStandardMessage( messages[j] );
            }
        }
    }
    
    /**
     * Send SMS array to target mobile number, the length of content is not limited, the function will
     * split each long message to several standard messages
     * 
     * @see sendMessage( Message message )
     */
    public void sendMessage( Message[] messages ) throws MobileException
    {
        int count = messages.length;
        for ( int i = 0; i < count; i++ )
        {
            sendMessage( messages[i] );
        }
        return;
    }
    
    /**
     * Send SMS to target mobile number, the length of content is not limited, the function will
     * split long message to several standard messages
     * 
     * @see sendStandardMessage( Message message )
     */
    public void sendMessage( Message message ) throws MobileException
    {
        Message[] messages = null;

        if ( MessageProcessor.messageLengthValidate( message ) )
        {
            sendStandardMessage( message );
        }
        else
        {
            messages = MessageProcessor.splitMessageByContent( message );
            sendStandardMessage( messages );
        }
        return;
    }

    /**
     * Send standard SMS array to target mobile number, the length of content must be less than 
     * 160 none unicode or 70 unicode characters
     * 
     * @see sendStandardMessage( Message message )
     */
    public void sendStandardMessage( Message[] messages ) throws MobileException
    {
        int count = messages.length;
        for ( int i = 0; i < count; i++ )
        {
            sendStandardMessage( messages[i] );
        }
        return;
    }
    
    /**
     * Send standard SMS to target mobile telephone, the length of content must be less than 
     * 160 none unicode or 70 unicode characters
     * 
     * @param message
     *            you should set these fields before sending it.
     *            <li>Message.messageCenterNum - +86139... ( optional )
     *            <li>Message.teleNum - the format is: +86139.... or 139... (
     *            required )
     *            <li>Message.deliveryStatusReport - designed for siemens (
     *            optional )
     *            <li>Message.ONE_HOUR_VALIDTIME - 5 minutes, 1 hour, 12 hours,
     *            1 day, 1 week, MAX ( optional )
     *            <li>Message.deliveryHandFree - optional
     *            <li>Message.content - not more than 160 none unicode or 70
     *            unicode characters ( optional )
     * @return void
     * @throws IllegalArgumentException if the content of SMS more than 70 unicode unicode or 160 non-nunicode
     *         or if there is no target telephone number is specified in Message parameter
     */
    public synchronized void sendStandardMessage( Message message ) throws MobileException
    {     
        if ( !MessageProcessor.messageLengthValidate( message ) )
        {
            throw new IllegalArgumentException( "The content of SMS more than 70 unicode characters or 160 non-unicode characters." );
        }
        
        String teleNum = message.getTeleNum();

        // Whether the target telephone number is not set
        if ( teleNum.equals( "" ) ) throw new IllegalArgumentException( "No target telephone number is specified." );
        
        // Convert message to pdu
        String pdus = null;
        try
        {
            pdus = MessageProcessor.gsmEncodePdu( message );
        }
        catch ( UnsupportedEncodingException uee )
        {
            throw new MobileException( uee.getMessage() );
        }
        
        int pduLength = pdus.length();
        
        // add ctrl-z to pdus as the last character
        byte[] bytes = new byte[1];
        bytes[0] = 0x1A;
        pdus = pdus + new String( bytes );
        
        int smscLength = Integer.parseInt( pdus.substring( 0, 2 ), 16);
        smscLength = smscLength + 1;
        
        String atCommand = "AT+CMGS=%s\r".replaceAll( "%s", String.valueOf( pduLength / 2 - smscLength ) );
        
        this.receiveMessagePreparedResult( atCommand );
        
        this.receiveATCommandOKResult( pdus );
        
        return;
    }
    
    /************************************Save Message***********************************************************/
    
    /**
     * Save Collection contain Message object to target mobile number, the length of content is not limited, the function will
     * split long message to several standard messages, and the method will return them as results
     * <p>If message status is not set, the message result will be saved as unsent message
     * <p>After the method saved the message successfully, the index will be set to message result
     * <p>This method is used to save new messages or save existing messages, usually it will be used
     * to save local messages to mobile
     * @param Collection collection can be any type messages
     * @param smsMemoType
     *            <p>Mobile.SMS_SM_MEMO Mobile.SMS_ME_MEMO
     * @see saveMessage( Message message )
     * @see checkSmsSpace( int num, String smsMemoType )
     * @return Message[] the size of Message[] result is euqal to or more than the size of Message[] parameter
     */
    public Message[] saveMessage( Collection collection, String smsMemoType ) throws MobileException
    {
        Message[] messages = (Message[]) collection.toArray( new Message[0] );
        return saveMessage( messages, smsMemoType );
    }
    
    /**
     * Spilt one SMS to several messages with different mobile number, and then save them
     * the length of content is not limited, the function will split long message to several standard messages
     * <p>If message status is not set, the message result will be saved as unsent message
     * <p>After the method saved the message successfully, the index will be set to message result
     * <p>This method is used to save one new message with several target numbers to mobile, existing message should
     * not be allowed to pass to this method
     * @param Message message should be new message
     * @param smsMemoType
     *            <p>Mobile.SMS_SM_MEMO Mobile.SMS_ME_MEMO
     * @see saveStandardMessage( Message message )
     * @see checkSmsSpace( int num, String smsMemoType )
     * @return Message[] the size of Message[] result is euqal to or more than the size of Message[] parameter 
     */
    public synchronized Message[] saveMessage( Message message, String[] teleNum, String smsMemoType ) throws MobileException
    {
        this.setSMSMemoGetSMSNum(smsMemoType);
        ArrayList list = new ArrayList();
        int numOfTeleNum = teleNum.length;
        Message[] messages = MessageProcessor.splitMessageByContent( message );
        int numOfContent = messages.length;
        for ( int i = 0; i < numOfTeleNum; i++ )
        {
            for ( int j = 0; j < numOfContent; j++ )
            {
                Message cloneMessage = (Message) messages[j].clone();
                cloneMessage.setTeleNum( teleNum[i] );
                saveStandardMessage( cloneMessage );
                list.add( cloneMessage );
            }
        }
        return (Message[]) list.toArray( new Message[0] );
    }
    
    /**
     * Save SMS array to target mobile number, the length of content is not limited, the function will
     * split long message to several standard messages, and the method will return them as results
     * <p>If message status is not set, the message result will be saved as unsent message
     * <p>After the method saved the message successfully, the index will be set to message result
     * <p>This method is used to save new messages or save existing messages, usually it will be used
     * to save local messages to mobile
     * @param Message messages can be any type messages
     * @param smsMemoType
     *            <p>Mobile.SMS_SM_MEMO Mobile.SMS_ME_MEMO
     * @see saveMessage( Message message )
     * @see checkSmsSpace( int num, String smsMemoType )
     * @return Message[] the size of Message[] result is euqal to or more than the size of Message[] parameter
     */
    public synchronized Message[] saveMessage( Message[] messages, String smsMemoType ) throws MobileException
    {
        this.setSMSMemoGetSMSNum(smsMemoType);
        return this.saveMessage(messages);
    }
    
    private Message[] saveMessage( Message[] messages ) throws MobileException
    {
        int row = messages.length;
        Message[][] message = new Message[row][];
        int[] colum = new int[row];
        int totalSize = 0;
        for ( int i = 0; i < row; i++ )
        {
            message[i] = saveMessage( messages[i] );
            colum[i] = message[i].length;
            totalSize = totalSize +colum[i];
        }
        
        Message[] mg = new Message[totalSize];
        int k = 0;
        for ( int i = 0; i < row; i++ )
        {
            for ( int j = 0; j < colum[i]; j++ )
            {
                mg[k++] = message[i][j];
            }
        }
        return mg;
    }
    
    /**
     * Save SMS to target mobile number, the length of content is not limited, the function will
     * split long message to several standard messages, and the method will return them as results
     * <p>If message status is not set, the message result will be saved as unsent message
     * <p>After the method saved the message successfully, the index will be set to message result
     * <p>This method is used to save new message or save existing message
     * @param Message message can be any type message
     * @param smsMemoType
     *            <p>Mobile.SMS_SM_MEMO Mobile.SMS_ME_MEMO
     * @see saveStandardMessage( Message message )
     * @see checkSmsSpace( int num, String smsMemoType )
     * @return Message[]
     */
    public synchronized Message[] saveMessage(Message message, String smsMemoType) throws MobileException
    {
        this.setSMSMemoGetSMSNum(smsMemoType);
        return this.saveMessage(message);
    }

    private Message[] saveMessage(Message message) throws MobileException
    {
        if ( MessageProcessor.messageLengthValidate( message ) )
        {
            saveStandardMessage( message );
            return new Message[]{ message };
        }
        else
        {
            Message[] messages = MessageProcessor.splitMessageByContent( message );
            saveStandardMessage( messages );
            return messages;
        }
    }
    
    /**
     * Save standard SMS array to target mobile telephone, the length of content must be less than 
     * 160 none unicode or 70 unicode characters
     * <p>If message status is not set, the message parameter will be saved as unsent message
     * <p>After the method saved the message successfully, the index will be set to message parameter
     * <p>This method is used to save new messages or save existing messages
     * @param Message messages can be any type messages
     * @param smsMemoType
     *            <p>Mobile.SMS_SM_MEMO Mobile.SMS_ME_MEMO
     * @see saveStandardMessage( Message message )
     * @see checkSmsSpace( int num, String smsMemoType )
     */
    public synchronized void saveStandardMessage( Message[] messages, String smsMemoType ) throws MobileException
    {
        this.setSMSMemoGetSMSNum(smsMemoType);
        this.saveStandardMessage(messages);
    }
    
    private void saveStandardMessage( Message[] messages ) throws MobileException
    {
        int count = messages.length;
        for ( int i = 0; i < count; i++ )
        {
            saveStandardMessage( messages[i] );
        }
        return;
    }
    
    /**
     * Save standard SMS to target mobile telephone, the length of content must be less than 
     * 160 none unicode or 70 unicode characters
     * <p>If message status is not set, the message parameter will be saved as unsent message
     * <p>After the method saved the message successfully, the index will be set to message parameter
     * <p>This method is used to save new message or save existing message
     * @see checkSmsSpace( int num, String smsMemoType )
     * @param message
     *            you should set these fields before sending it.
     *            <li>Message.status - Message.UNSENT_STATUS will be as default value if this parameter is not set )
     *            <li>Message.messageCenterNum - +86139... ( optional )
     *            <li>Message.teleNum - the format is: +86139.... or 139... (
     *            optional )
     *            <li>Message.deliveryStatusReport - designed for siemens (
     *            optional )
     *            <li>Message.ONE_HOUR_VALIDTIME - 5 minutes, 1 hour, 12 hours,
     *            1 day, 1 week, MAX ( optional )
     *            <li>Message.deliveryHandFree - optional
     *            <li>Message.content - not more than 160 none unicode or 70
     *            unicode characters ( optional )
     * @param smsMemoType
     *            <p>Mobile.SMS_SM_MEMO Mobile.SMS_ME_MEMO
     * @return void
     * @throws IllegalArgumentException if the content of SMS more than 70 unicode unicode or 160 non-nunicode
     *         or if there is no target telephone number is specified in Message parameter
     */
    public synchronized void saveStandardMessage(Message message, String smsMemoType) throws MobileException {
        this.setSMSMemoGetSMSNum(smsMemoType);
        this.saveStandardMessage(message);
    }
    
    /**
     * You should synchronized this method with checkSmsSpace method and setSMSMemoGetSMSNum method.
     * @see saveStandardMessage(Message, String)
     */
    public synchronized void saveStandardMessage( Message message ) throws MobileException
    {
        if ( !MessageProcessor.messageLengthValidate( message ) )
        {
            throw new IllegalArgumentException( "The content of SMS more than 70 unicode characters or 160 non-unicode characters." );
        }
        
        String status = null;
        String pdus = null;
        
        // Get message status that want to be set
        if ( message.getStatus() == -1 )
        {
            status = String.valueOf( Message.UNSENT_STATUS );
            
            // Convert message to pdu
            try
            {
                pdus = MessageProcessor.gsmEncodePdu( message );
            }
            catch( UnsupportedEncodingException uee )
            {
                throw new MobileException( uee.getMessage() );
            }
        }
        else
        {
            status = String.valueOf( message.getStatus() );
            pdus = message.getPdu();
        }
                

        int pduLength = pdus.length();
        
        // add ctrl-z to pdus as the last character
        byte[] bytes = new byte[1];
        bytes[0] = 0x1A;
        pdus = pdus + new String( bytes );
        
        int smscLength = Integer.parseInt( pdus.substring( 0, 2 ), 16);
        smscLength = smscLength + 1;
        
        // create at command
        String atCommand = "AT+CMGW=%s,%t\r".replaceAll( "%s", String.valueOf( pduLength / 2 - smscLength ) );
        atCommand = atCommand.replaceAll( "%t", status );
        
        this.receiveMessagePreparedResult( atCommand );
        
        // Get index from port value
        StringBuffer portValues = this.receiveATCommandOKResult( pdus );
        int index = Integer.parseInt( Processor.getStringFromTokens( portValues, " ", "\r\n" ) );
        
        if ( index <= 0 ) throw new MobileException( "Save message failure or storage is full" );
        
        message.setIndex( index );
        
        if ( message.getStatus() == -1 )
        {
            message.setStatus( Message.UNSENT_STATUS );
            message.setPdu( pdus );
        }
        
        return;
    }
    
    /************************************Delete Message***********************************************************/
    
    /**
     * Delete the message with specified index
     * @param index
     * @throws MobileException if the index does not exist
     */
    public synchronized void deleteMessage( int index, String smsMemoType ) throws MobileException
    {
        this.setSMSMemoGetSMSNum(smsMemoType);
        this.deleteMessage(index);
    }
    
    /**
     * You should synchronized this method with setSMSMemoGetSMSNum method.
     * @see deleteMessage(Message, String)
     */
    public void deleteMessage( int index ) throws MobileException
    {
        if ( index < 1 ) throw new IllegalArgumentException( "The index of Message should be more than 0." );
        String atCommand = "AT+CMGD=%s\r".replaceAll( "%s", String.valueOf( index ) );
        this.receiveATCommandOKResult( atCommand );
        return;
    }
    
    /**
     * Delete the messge specified
     * @param message
     * @throws MobileException
     */
    public synchronized void deleteMessage( Message message, String smsMemoType ) throws MobileException
    {
        this.setSMSMemoGetSMSNum(smsMemoType);
        deleteMessage( message.getIndex() );
    }
    
    /**
     * You should synchronized this method with setSMSMemoGetSMSNum method.
     * @see deleteMessage(Message, String)
     */
    public void deleteMessage( Message message ) throws MobileException
    {
        deleteMessage( message.getIndex() );
    }
    
    /**
     * Delete the messge array specified
     * @param messages
     * @throws MobileException
     */
    public synchronized void deleteMessage( Message[] messages, String smsMemoType ) throws MobileException
    {
        this.setSMSMemoGetSMSNum(smsMemoType);
        int length = messages.length;
        for ( int i = 0; i < length; i++ )
        {
            deleteMessage( messages[i] );
        }
    }
    
    /**
     * Delete the message collection contains message specified
     * @param collection
     * @throws MobileException
     */
    public void deleteMessage( Collection collection, String smsMemoType ) throws MobileException
    {
        Message[] messages = (Message[]) collection.toArray( new Message[0] );
        deleteMessage( messages, smsMemoType );
    }
    
    /**
     * Get SMS center number
     * @return center number if exists, or ""
     */
    public String getSMSCNum() throws MobileException
    {
        String num = "";
        String type = "";
        String atCommand = "AT+CSCA?\r";
        setMobileEncodeFormat( Mobile.ENCODE_GSM_FORMAT );
        StringBuffer portValues = this.receiveATCommandOKResult( atCommand );
        num = Processor.getStringFromTokens( portValues, "\"", "\"" );
        type = Processor.getStringFromTokens( portValues, ",", "\r\n" );
        if ( !num.equals( "" ) )
        {
            if ( !num.substring( 0, 1 ).equals( "+" ) && type.equals( INTERNATIONAL_NUM ) )
            {
                num = "+" + num;
            }
            return num;
        }
        return "";
    }
    
    /**
     * Get mobile information
     * @return MobileInfo
     */
    public MobileInfo getMoibleInfo()
    {
        MobileInfo mi = new MobileInfo();
        String[] atCommands = new String[]{ "AT+CGMI\r", "AT+CGMM\r", "AT+CGMR\r", "AT+CGSN\r", "AT+CBC\r", "AT+CSQ\r", "AT+CNUM\r" };

        StringBuffer[] portValues = null;
        
        portValues = receiveATCommandOKResult( atCommands );
        
        int i = 0;
        String nameOfManu    = Processor.getStringFromTokens( portValues[i++], "\r\n", "\r\n" );
        
        String nameOfTele    = Processor.getStringFromTokens( portValues[i++], "\r\n", "\r\n" );

        String verOfSW       = Processor.getStringFromTokens( portValues[i++], "\r\n", "\r\n" );

        String valueOfIMEI   = Processor.getStringFromTokens( portValues[i++], "\r\n", "\r\n" );

        String batteryCharge = Processor.getStringFromTokens( portValues[i++], ",", "\r\n" ) + "%";

        int sq = Integer.parseInt( Processor.getStringFromTokens( portValues[i++], " ", "," ) );
        sq = sq * 2 - 113;
        String signalQuality = String.valueOf( sq ) + "dBm";

        String ownerTeleNum  = Processor.getStringFromTokens( portValues[i++], ",\"", "\"," );
        
        mi.setNameOfManu( nameOfManu );
        mi.setNameOfTele( nameOfTele );
        mi.setVerOfSW( verOfSW );
        mi.setValueOfIMEI( valueOfIMEI );
        mi.setBatteryCharge( batteryCharge );
        mi.setSignalQuality( signalQuality );
        mi.setOwnerTeleNum( ownerTeleNum );
        
        return mi;
    }
    
    /**
     * The format is "05/06/10,21:21:50"
     * @return String shows the time on Mobile
     */
    public String getMobileTime() throws MobileException
    {
        String atCommand = "AT+CCLK?\r";
        StringBuffer portValues = this.receiveATCommandOKResult( atCommand );
        String mobileTime = Processor.getStringFromTokens( portValues, "\"", "\"" );
        return mobileTime.substring( 0, 17 );
    }
    
    /**
     * The format is "05/06/10,21:21:50"
     * @param mobileTime
     * @return void
     * @throws MobileException
     */
    public void setMobileTime( String mobileTime) throws MobileException
    {
        StringBuffer portValues = this.receiveATCommandOKResult( "AT+CCLK?\r" );
        String timeZone = Processor.getStringFromTokens( portValues, "\"", "\"" ).substring( 17 );

        String atCommand = "AT+CCLK=\"%s\"\r".replaceAll( "%s", mobileTime + timeZone );
        this.receiveATCommandOKResult( atCommand );
        return;
    }
    
    /******************************************PhoneBook Handler*****************************************/
    
    /**
     * Set mobile encode format
     * 
     * @param encodeFormat
     *            <li>Mobile.ENCODE_GSM_FORMAT
     *            <li>Mobile.ENCODE_UCS2_FORMAT
     *            <li>Mobile.ENCODE_UTF8_FORMAT
     *            <li>Mobile.ENCODE_HEX_FORMAT
     * @return void
     */
    public void setMobileEncodeFormat( String encodeFormat ) throws MobileException
    {
        String atCommand = "AT+CSCS=%s\r".replaceAll( "%s", encodeFormat );
        this.receiveATCommandOKResult( atCommand );
        return;
    }
    
    /**
     * Specifies the PhoneBook memory type.
     * 
     * @param phoneBookMemoType
     *            <li>Mobile.PB_SM_MEMO
     *            <li>Mobile.PB_ME_MEMO
     *            <li>Mobile.PB_DC_MEMO
     *            <li>Mobile.PB_MC_MEMO
     *            <li>Mobile.PB_RC_MEMO
     * @return void if no MobileException throws
     * @throws MobileException if the at command can not be recognized
     */
    public void setPhoneBookMemo( String phoneBookMemoType ) throws MobileException
    {
        String atCommand = "AT+CPBS=%s\r".replaceAll( "%s", phoneBookMemoType );
        this.receiveATCommandOKResult( atCommand );
        return;
    }
    
    /**
     * Get phoneBookSpace with specified phonebook memo type.
     * And specifies the PhoneBook memory type.
     * @param phoneBookMemoType
     *            <li>Mobile.PB_SM_MEMO
     *            <li>Mobile.PB_ME_MEMO
     *            <li>Mobile.PB_DC_MEMO
     *            <li>Mobile.PB_MC_MEMO
     *            <li>Mobile.PB_RC_MEMO
     * @return int phoneBookSpace
     * @throws MobileException
     */
    public synchronized int getPhoneBookSpace( String phoneBookMemoType ) throws MobileException
    {
        this.setPhoneBookMemo( phoneBookMemoType );
        String atCommand = "AT+CPBR=?\r";
        StringBuffer portValues = this.receiveATCommandOKResult( atCommand );
        int phoneBookSpace = Integer.parseInt( Processor.getStringFromTokens(
                portValues, "-", ")" ) );
        return phoneBookSpace;
    }
    
    /**
     * List phonebooks in mobile
     */
    public TreeSet listPhoneBooksInME() throws MobileException
    {
        return listPhoneBooks( Mobile.PB_ME_MEMO );
    }
    
    /**
     * List phonebooks in SIM card
     */
    public TreeSet listPhoneBooksInSM() throws MobileException
    {
        return listPhoneBooks( Mobile.PB_SM_MEMO );
    }
    
    /**
     * List phonebooks have been received
     */
    public TreeSet listReceivedPhoneBooks() throws MobileException
    {
        return listPhoneBooks( Mobile.PB_RC_MEMO );
    }
    
    /**
     * List phonebooks have been missed
     */
    public TreeSet listMissedPhoneBooks() throws MobileException
    {
        return listPhoneBooks( Mobile.PB_MC_MEMO );
    }
    
    /**
     * List phonebooks have been dialled
     */
    public TreeSet listDialledPhoneBooks() throws MobileException
    {
        return listPhoneBooks( Mobile.PB_DC_MEMO );
    }
    
    /**
     * List all phonebooks with specified phonebook memo type.
     * Used to list all the phonebooks in SM or ME normally( if Mobile supports ).
     * @param phoneBookMemoType
     *            <li>Mobile.PB_SM_MEMO
     *            <li>Mobile.PB_ME_MEMO
     *            <li>Mobile.PB_DC_MEMO
     *            <li>Mobile.PB_MC_MEMO
     *            <li>Mobile.PB_RC_MEMO
     * @see listPhoneBooks( int, String )
     */
    public synchronized TreeSet listPhoneBooks(  String phoneBookMemoType ) throws MobileException
    {
        int  phoneBookSpace = this.getPhoneBookSpace( phoneBookMemoType );
        return listPhoneBooks( phoneBookSpace, phoneBookMemoType );
    }
    
    /**
     * List phonebooks with specified index with specified phonebook memo type.
     * @param index 1 to index of phonebook will be listed
     * @param phoneBookMemoType
     *            <li>Mobile.PB_SM_MEMO
     *            <li>Mobile.PB_ME_MEMO
     *            <li>Mobile.PB_DC_MEMO
     *            <li>Mobile.PB_MC_MEMO
     *            <li>Mobile.PB_RC_MEMO
     * @see listPhoneBooks( int, int, String )
     */
    public TreeSet listPhoneBooks( int index, String phoneBookMemoType ) throws MobileException
    {
        return listPhoneBooks( 1, index, phoneBookMemoType );
    }
    
    /**
     * List phonebooks with specified start index and end index with specified phonebook memo type.
     * @param startIndex start index of phonebook will be listed
     * @param endIndex end index of phonebook will be listed
     * @param phoneBookMemoType
     *            <li>Mobile.PB_SM_MEMO
     *            <li>Mobile.PB_ME_MEMO
     *            <li>Mobile.PB_DC_MEMO
     *            <li>Mobile.PB_MC_MEMO
     *            <li>Mobile.PB_RC_MEMO
     * @return TreeSet contains phonebooks
     * @throws MobileException
     */
    public synchronized TreeSet listPhoneBooks( int startIndex, int endIndex, String phoneBookMemoType ) throws MobileException
    {
        int  phoneBookSpace = this.getPhoneBookSpace( phoneBookMemoType );
        if ( startIndex < 1 ) throw new IllegalArgumentException( "startIndex should be more than 0." );
        if ( endIndex > phoneBookSpace ) throw new IllegalArgumentException( "endIndex should be less than phonebook space." );
        
        this.phoneBookEncodeFormat();
        String atCommand = "AT+CPBR=%s,%t\r".replaceAll( "%s", String.valueOf( startIndex ) );
        atCommand = atCommand.replaceAll( "%t", String.valueOf( endIndex ) );
        StringBuffer portValues = this.receiveATCommandOKResult( atCommand );
        TreeSet ts = new TreeSet();
        PhoneBook phoneBook = null;
        String index = "";
        String teleNum = "";
        String name = "";
        String time = "";

        while ( true )
        {
            // Get index, teleNum, name and time
            index = Processor.getStringFromTokens( portValues, " ", "," );
            if ( index.equals( "" ) )
                break;
            teleNum = Processor.getStringFromTokens( portValues, "\"", "\"" );
            name = Processor.getStringFromTokens( portValues, ",\"", "\"" );
            if ( !portValues.substring( 1, 3 ).equals( "\r\n" ) )
            {
                time = Processor.getStringFromTokens( portValues, "\",\"", "\"" );
            }
            
            // Decode teleNum and name if necessary
            teleNum = this.phoneBookTeleNumDecoder( teleNum );
            name = this.phoneBookNameDecoder( name );
            
            phoneBook = new PhoneBook();
            phoneBook.setIndex( Integer.parseInt( index ) );
            phoneBook.setTeleNum( teleNum );
            phoneBook.setName( name );
            phoneBook.setTime( time );
            ts.add( phoneBook );
        }
        return ts;
    }
    
    /*************************************phonebook encode & decode*************************/
    
    /**
     * This method could be over-write by sub class of Mobile
     * @throws MobileException
     */
    protected void phoneBookEncodeFormat() throws MobileException
    {
        this.setMobileEncodeFormat( Mobile.ENCODE_UCS2_FORMAT );
    }
    
    /**
     * This method could be over-write by sub class of Mobile, for listing phone book text
     * @throws MobileException
     */
    protected String phoneBookNameDecoder( String name ) throws MobileException
    {
        try
        {
            byte[] bytes = Processor.gsmStringToBytes( new StringBuffer( name ) );
            return new String( bytes, "iso-10646-ucs-2" );
        }
        catch ( UnsupportedEncodingException uee )
        {
            throw new MobileException( uee.getMessage() );
        }
    }
    
    /**
     * This method could be over-write by sub class of Mobile,  for listing phone book number
     * @throws MobileException
     */
    protected String phoneBookTeleNumDecoder( String teleNum ) throws MobileException
    {
        return teleNum;
    }
    
    /**
     * This method could be over-write by sub class of Mobile, for saving phone book name
     * @throws MobileException
     */
    protected String phoneBookNameEncoder( String name ) throws MobileException
    {
        try
        {
            byte[] bytes = name.getBytes( "iso-10646-ucs-2" );
            return Processor.gsmBytesToString( bytes ).toString();
        }
        catch ( UnsupportedEncodingException uee )
        {
            throw new MobileException( uee.getMessage() );
        }
    }
    
    /**
     * This method could be over-write by sub class of Mobile, for saving phone book number
     * @throws MobileException
     */
    protected String phoneBookTeleNumEncoder( String teleNum ) throws MobileException
    {
        return teleNum;
    }
    
    /*******************************************Save phonebook handler***************************************/
        
    /**
     * Check phone book text length
     * @param phoneBook
     * @param phoneBookMemoType
     *        <li>Mobile.PB_SM_MEMO
     *        <li>Mobile.PB_ME_MEMO
     * @return true or false
     */
    public boolean checkPhoneBookTextLength( PhoneBook phoneBook, String phoneBookMemoType )
    {
        String name = phoneBook.getName();
        int lenOfPBText = name.length();
        
        int maxLenOfPBText = 0;
        if ( name.getBytes().length == name.length() )
        {
            if ( phoneBookMemoType.equals( Mobile.PB_SM_MEMO ) )
            {
                maxLenOfPBText = mobileConfig.getMaxLenOfPBTextInSM();
            }
            else
            {
                maxLenOfPBText = mobileConfig.getMaxLenOfPBTextInME();
            }
        }
        else
        {
            if ( phoneBookMemoType.equals( Mobile.PB_SM_MEMO ) )
            {
                maxLenOfPBText = mobileConfig.getMaxLenOfUnicodePBTextInSM();
            }
            else
            {
                maxLenOfPBText = mobileConfig.getMaxLenOfUnicodePBTextInME();
            }
        }

        if ( lenOfPBText > maxLenOfPBText ) return false;
        return true;
    }
    
    /**
     * Check phone book number length
     * @param phoneBook
     * @param phoneBookMemoType
     *        <li>Mobile.PB_SM_MEMO
     *        <li>Mobile.PB_ME_MEMO
     * @return true or false
     */
    public boolean checkPhoneBookNumberLength( PhoneBook phoneBook, String phoneBookMemoType )
    {
        String teleNum = phoneBook.getTeleNum();
        int lenOfPBNumber = teleNum.length();
        int maxLenOfPBNumber = 0;
        
        if ( phoneBookMemoType.equals( Mobile.PB_SM_MEMO ) )
        {
           maxLenOfPBNumber = mobileConfig.getMaxLenOfPBNumberInSM();
        }
        else
        {
           maxLenOfPBNumber = mobileConfig.getMaxLenOfPBNumberInME();
        }
        
        if ( lenOfPBNumber > maxLenOfPBNumber ) return false;
        return true;
    }
    
    /**
     * @see checkPhoneBookValidation( PhoneBook[] phoneBook, String phoneBookMemoType )
     */
    public void checkPhoneBookValidation( PhoneBook phoneBook, String phoneBookMemoType ) throws MobileException
    {
        checkPhoneBookValidation( new PhoneBook[]{ phoneBook }, phoneBookMemoType );
    }
    
    /**
     * Check the phonebook parameter before invoke savePhoneBook method
     * @param phoneBook the phoneBook will be validated
     * @param phoneBookMemoType
     *        <li>Mobile.PB_SM_MEMO
     *        <li>Mobile.PB_ME_MEMO
     */
    public void checkPhoneBookValidation( PhoneBook[] phoneBook, String phoneBookMemoType ) throws MobileException
    {
        for ( int i = 0; i < phoneBook.length; i++ )
        {
            if ( phoneBook[i].getName().equals( "" ) )
            {
                throw new MobileException( "Phone book text should be specified." );
            }
            if ( phoneBook[i].getTeleNum().equals( "" ) )
            {
                throw new MobileException( "Phone book number should be specified." );
            }
            if ( !PhoneBookProcessor.checkTeleNumValidation( phoneBook[i].getTeleNum() ) )
            {
                throw new MobileException( "Telephone number can include \"*\", \"+\", \"#\" and numeric character only." );
            }
            if ( !this.checkPhoneBookNumberLength( phoneBook[i], phoneBookMemoType ) )
            {
                throw new MobileException( "The length of phone book number \"" + phoneBook[i].getTeleNum() + "\" is too long." );
            }
            if ( !this.checkPhoneBookTextLength( phoneBook[i], phoneBookMemoType ) )
            {
                throw new MobileException( "The length of phone book text \"" + phoneBook[i].getName() + "\" is too long." );
            }
        }
    }
    
    /**
     * Modify phonebook in mobile directly.
     * @see writePhoneBook( PhoneBook[], Collection, String )
     */
    public void modifyPhoneBookInME( PhoneBook phoneBook ) throws MobileException
    {
        modifyPhoneBook( phoneBook, Mobile.PB_ME_MEMO );
    }
    
    /**
     * Modify phonebook in SIM card directly.
     * @see writePhoneBook( PhoneBook[], Collection, String )
     */
    public void modifyPhoneBookInSM( PhoneBook phoneBook ) throws MobileException
    {
        modifyPhoneBook( phoneBook, Mobile.PB_SM_MEMO );
    }
    
    /**
     * Modify phonebook in memory type specified directly.
     * @see writePhoneBook( PhoneBook[], Collection, String )
     */
    public synchronized void modifyPhoneBook( PhoneBook phoneBook, String phoneBookMemoType ) throws MobileException
    {
        int index = phoneBook.getIndex();
        if ( index < 1 ) throw new IllegalArgumentException( "The index of phonebook should be more than 0." );
        checkPhoneBookValidation( phoneBook, phoneBookMemoType );
        this.setPhoneBookMemo(phoneBookMemoType);
        this.savePhoneBook( index, phoneBook );
    }
    
    /**
     * Write phonebook array to mobile directly.
     * @see writePhoneBook( PhoneBook[], Collection, String )
     */
    public void writePhoneBookToME( PhoneBook[] phoneBook, Collection collection ) throws MobileException
    {
        writePhoneBook( phoneBook, collection, Mobile.PB_ME_MEMO );
    }
    
    /**
     * Write single phonebook to mobile card directly.
     * @see writePhoneBook( PhoneBook[], Collection, String )
     */
    public void writePhoneBookToME( PhoneBook phoneBook, Collection collection ) throws MobileException
    {
        writePhoneBook( phoneBook, collection, Mobile.PB_ME_MEMO );
    }
    
    /**
     * Write phonebook array to SIM card directly.
     * @see writePhoneBook( PhoneBook[], Collection, String )
     */
    public void writePhoneBookToSM( PhoneBook[] phoneBook, Collection collection ) throws MobileException
    {
        writePhoneBook( phoneBook, collection, Mobile.PB_SM_MEMO );
    }
    
    /**
     * Write single phonebook to SIM card directly.
     * @see writePhoneBook( PhoneBook[], Collection, String )
     */
    public void writePhoneBookToSM( PhoneBook phoneBook, Collection collection ) throws MobileException
    {
        writePhoneBook( phoneBook, collection, Mobile.PB_SM_MEMO );
    }
    
    /**
     * Write single phonebook to the memory type specified directly. This method will validate all the parameters inputed automaticlly,
     * and throw the MobileException to describe the problem.
     * @see writePhoneBook( PhoneBook[], Collection, String )
     */
    public void writePhoneBook( PhoneBook phoneBook, Collection collection, String phoneBookMemoType ) throws MobileException
    {
        PhoneBook[] phoneBooks = new PhoneBook[]{ phoneBook };
        writePhoneBook( phoneBooks, collection, phoneBookMemoType );
    }
    
    /**
     * Write phonebook array to the memory type specified directly. This method will validate all the parameters inputed automaticlly,
     * and throw the MobileException to describe the problem.
     * @param phoneBook phonebook array
     * @param collection the current phonebooks in the memory specified, get it by invoking listPhoneBooks( String phoneBookMemoType ) method
     * @param phoneBookMemoType
     * @throws MobileException
     */
    public synchronized void writePhoneBook( PhoneBook[] phoneBook, Collection collection, String phoneBookMemoType ) throws MobileException
    {
        int phoneBookSpace = this.getPhoneBookSpace( phoneBookMemoType );
        
        int[] index = new int[phoneBook.length];
        if ( !PhoneBookProcessor.getPhoneBookIndexes( index, phoneBookSpace, collection ) )
        {
            throw new MobileException( "No more space to store " + phoneBook.length + " phonebooks." );
        }
        checkPhoneBookValidation( phoneBook, phoneBookMemoType );
        this.savePhoneBook( index, phoneBook );
    }
    
    /**
     * Save phonebooks to the indexes specified.
     * @see savePhoneBook( int, phoneBook )
     * @param index
     * @param phoneBook
     * @throws MobileException
     */
    public void savePhoneBook( int[] index, PhoneBook[] phoneBook ) throws MobileException
    {
        for ( int i = 0; i < index.length; i++ )
        {
            savePhoneBook( index[i], phoneBook[i] );
        }
    }
    
    /**
     * Save phonebook to the index specified, if the index does not exist in memo type, add one phonebook or modify the phonebook existing
     * Please make sure the below items before invoke this method
     * <p> the memo type space is enough to store new phonebook
     * <p> telephone book name is not empty
     * <p> telephone book number is not empty
     * <p> telephone book number include \"*\", \"+\", \"#\" and numeric character only. @see PhoneBookProcessor.checkTeleNumValidation
     * <p> the max length of telephone book name for target memory type @see this.checkPhoneBookTextLength
     * <p> the max length of telephone book number for target memory type @see this.checkPhoneBookNumberLength
     * @param index the index in target memory type specified @see PhoneBookProcessor.getPhoneBookIndexes
     * @param phoneBook the phonebook you want to store
     * @throws MobileException
     */
    public synchronized void savePhoneBook( int index, PhoneBook phoneBook ) throws MobileException
    {
        String indexString = String.valueOf( index );
        
        String teleNum = phoneBook.getTeleNum();
        if ( teleNum.equals( "" ) ) throw new IllegalArgumentException( "Phone book number should be specified." );
        if ( !PhoneBookProcessor.checkTeleNumValidation( teleNum ) ) throw new IllegalArgumentException( "Telephone number can include \"*\", \"+\", \"#\" and numeric character only." );
        
        String name = phoneBook.getName();
        if ( name.equals( "" ) ) throw new IllegalArgumentException( "Phone book text should be specified." );
        
        String type = Mobile.NATIONAL_NUM;
        
        // if the phone book name is non-unicode, store the phone book as GSM format
        if ( name.getBytes().length == name.length() )
        {
            // Warning: if the mobile has been set to another encoding format, Mobile.ENCODE_GSM_FORMAT constant maybe
            // has been changed also.
            this.setMobileEncodeFormat( Mobile.ENCODE_GSM_FORMAT );
        }
        else
        {
            this.phoneBookEncodeFormat();
            teleNum = this.phoneBookTeleNumEncoder( teleNum );
            name = this.phoneBookNameEncoder( name );
        }
        
        
        if ( teleNum.substring( 0, 1 ).equals( "+" ) )
        {
            type = Mobile.INTERNATIONAL_NUM;
        }
        
        String atCommand = "AT+CPBW=%s,\"%s\",%s,\"%s\"\r";
        atCommand = atCommand.replaceFirst( "%s", indexString );
        atCommand = atCommand.replaceFirst( "%s", teleNum );
        atCommand = atCommand.replaceFirst( "%s", type );
        atCommand = atCommand.replaceFirst( "%s", name );
        
        this.receiveATCommandOKResult( atCommand );
        phoneBook.setIndex( index );
    }
    
    /*********************************************Delete PhoneBook***********************************************/
    
    /**
     * Delete the PhoneBook with specified index, you should synchronized this method with setPhoneBookMemo
     * @param index
     * @throws MobileException if the index does not exist
     */
    public void deletePhoneBook( int index ) throws MobileException
    {
        if ( index < 1 ) throw new IllegalArgumentException( "The index of PhoneBook should be more than 0." );
        String atCommand = "AT+CPBW=%s\r".replaceAll( "%s", String.valueOf( index ) );
        this.receiveATCommandOKResult( atCommand );
        return;
    }
    
    /**
     * Delete the PhoneBook specified, you should synchronized this method with setPhoneBookMemo
     * @param PhoneBook
     * @throws MobileException
     */
    public void deletePhoneBook( PhoneBook phoneBook ) throws MobileException
    {
        deletePhoneBook( phoneBook.getIndex() );
    }
    
    /**
     * Delete the PhoneBook array specified
     * @param phoneBooks
     * @param phoneBookMemoType
     * <li>Mobile.PB_SM_MEMO
     * <li>Mobile.PB_ME_MEMO
     * <li>Mobile.PB_DC_MEMO
     * <li>Mobile.PB_MC_MEMO
     * <li>Mobile.PB_RC_MEMO 
     * @throws MobileException
     */
    public synchronized void deletePhoneBook( PhoneBook[] phoneBooks, String phoneBookMemoType ) throws MobileException
    {
        this.setPhoneBookMemo(phoneBookMemoType);
        int length = phoneBooks.length;
        for ( int i = 0; i < length; i++ )
        {
            deletePhoneBook( phoneBooks[i] );
        }
    }
    
    /**
     * Delete the PhoneBook collection contains PhoneBook specified
     * @param collection
     * @throws MobileException
     */
    public void deletePhoneBook( Collection collection, String phoneBookMemoType ) throws MobileException
    {
        PhoneBook[] phoneBooks = (PhoneBook[]) collection.toArray( new PhoneBook[0] );
        deletePhoneBook( phoneBooks, phoneBookMemoType);
    }
    
    /**
     * Offer a chance for the client programmer to handler the new message arrival event.
     * Add the message arrival listener for the further processing. Mutiple listeners is allowed to be added.
     * @param listener
     * @throws MobileException
     */
    public synchronized void addMessageArrivalListener(MessageArrivalListener listener) throws MobileException {
        this.messageArrivalListeners.add(listener);
    }
    
    /**
     * Offer a chance for the client programmer to handler the new message arrival event.
     * Mobile will start a work thread to wait for the new mesage arrival and handle it with the specified message arrival listeners.
     * @param iWorkThread the number of the work thread that specified.
     * @param startOrStopMessageArrivalListener could be null.
     * @throws MobileException
     */
    public synchronized void startMessageArrivalWorkThread(int iWorkThread, StartOrStopMessageArrivalListener startOrStopMessageArrivalListener) throws MobileException {
        this.startOrStopMessageArrivalListener = startOrStopMessageArrivalListener;
        if (messageArrivalWorkThreads == null) messageArrivalWorkThreads = new MessageArrivalWorkThread[iWorkThread];
        String atCommand = mobileConfig.getNewMessageArrivalEnableATCommand();
        this.receiveATCommandOKResult( atCommand );
        for (int i = 0; i < iWorkThread; i++) {
        if ( messageArrivalWorkThreads[i] == null || messageArrivalWorkThreads[i].isShutdown() == true ) {
            messageArrivalWorkThreads[i] = new MessageArrivalWorkThread(channel, this, this.messageArrivalListeners);
            messageArrivalWorkThreads[i].start();
        }
        }
        if (this.startOrStopMessageArrivalListener != null) this.startOrStopMessageArrivalListener.startMessageArrival();
    }
   
    /**
     * Stop all the message arrival work thread.
     * @throws MobileException
     */
    public synchronized void stopMessageArrivalWorkThread() throws MobileException {
        if (messageArrivalWorkThreads == null) return;
        String atCommand = mobileConfig.getNewMessageArrivalDisableATCommand();
        this.receiveATCommandOKResult( atCommand );
        for (int i = 0; i < messageArrivalWorkThreads.length; i++) {
        if ( messageArrivalWorkThreads[i] != null && messageArrivalWorkThreads[i].isShutdown() == false ) {
            messageArrivalWorkThreads[i].shutdown();
        }
        }
        if (this.startOrStopMessageArrivalListener != null) this.startOrStopMessageArrivalListener.stopMessageArrival();
    }
    
    /**
     * Remove the specified message arrival listener.
     * @throws MobileException
     */
    public synchronized void removeMessageArrivalListener(MessageArrivalListener listener) throws MobileException {
        messageArrivalListeners.remove(listener);
    }
    
    /**
     * Remove all the specified message arrival listener.
     * @throws MobileException
     */
    public synchronized void removeAllMessageArrivalListener() throws MobileException {
        messageArrivalListeners.clear();
    }
    
    /**
     * Restart the message arrival work thread with the specified thread name (if exists).
     * @param messageArrivalWorkThreadName
     */
    public synchronized void restartMessageArrivalWorkThread(String messageArrivalWorkThreadName) {
        if (messageArrivalWorkThreads == null) return;
        for (int i = 0; i < messageArrivalWorkThreads.length; i++) {
            if (messageArrivalWorkThreads[i].getName().equalsIgnoreCase(messageArrivalWorkThreadName)) {
                if (!messageArrivalWorkThreads[i].isShutdown()) messageArrivalWorkThreads[i].shutdown();
                messageArrivalWorkThreads[i] = new MessageArrivalWorkThread(channel, this, this.messageArrivalListeners);
                messageArrivalWorkThreads[i].start();
                return;
            }
        }
    }
    
    /**
     * Use this method to retore all the previous message arrival work threads which are blocked.
     */
    public synchronized void restartPreviousHandleringMessageArrivalWorkThread() {
        if (messageArrivalWorkThreads == null) return;
        for (int i = 0; i < messageArrivalWorkThreads.length; i++) {
            if (messageArrivalWorkThreads[i] == Thread.currentThread() || !messageArrivalWorkThreads[i].isHandlering()) continue;
            if (!messageArrivalWorkThreads[i].isShutdown()) messageArrivalWorkThreads[i].shutdown();
            messageArrivalWorkThreads[i] = new MessageArrivalWorkThread(channel, this, this.messageArrivalListeners);
            messageArrivalWorkThreads[i].start();
        }
    }
    
    /**
     * Check the message arrival work thread with the specified thread name whether exists or not.
     * @param messageArrivalWorkThreadName
     */
    public synchronized boolean checkMessageArrivalWorkThread(String messageArrivalWorkThreadName) {
        if (messageArrivalWorkThreads == null) return false;
        for (int i = 0; i < messageArrivalWorkThreads.length; i++) {
            if (messageArrivalWorkThreads[i].getName().equalsIgnoreCase(messageArrivalWorkThreadName)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Return the count of the available message arrival work thread.
     * @return
     */
    public int getAvailableMessageArrivalWorkThreadCount() {
        int iCount = 0;
        if (messageArrivalWorkThreads == null) return 0;
        for (int i = 0; i < messageArrivalWorkThreads.length; i++) {
            if (!messageArrivalWorkThreads[i].isHandlering()) {
                iCount++;
            }
        }
        return iCount;
    }
    
    /**
     * Writes at commands to port and reads values from it.
     * @see receiveATCommandOKResult( String atCommand )
     * @param atCommands
     * @return
     */
    protected StringBuffer[] receiveATCommandOKResult( String[] atCommands )
    {
        int length = atCommands.length;
        StringBuffer[] portValues = new StringBuffer[length];
        
        for ( int i = 0; i < length; i++ )
        {
            try
            {
                portValues[i] = this.receiveATCommandOKResult( atCommands[i] );
            }
            catch ( MobileException me )
            {
                portValues[i] = new StringBuffer( "" );
            }
        }
        
        return portValues;
    }

    /**
     * Writes at command to port and reads values from it.
     * @param atCommand at command that write to port
     * @return port values respond with AT command, only the values are returned by end with "AT_COMMAND_OK" will be return, or Exception throws
     * @throws MobileException
     */
    protected StringBuffer receiveATCommandOKResult( String atCommand ) throws MobileException
    {
        return receiveATCommandValuesResult( atCommand, AT_COMMAND_OK );
    }
    
    /**
     * Writes at command to port and reads values from it.
     * @param atCommand at command that write to port
     * @return port values respond with AT command, only the values are returned by end with "MESSAGE_PREPARED" will be return, or Exception throws
     * @throws MobileException
     */
    protected StringBuffer receiveMessagePreparedResult( String atCommand ) throws MobileException
    {
        return receiveATCommandValuesResult( atCommand, MESSAGE_PREPARED );
    }
    
  /**
   * Writes at command to port and reads values from it.
   * 
   * @param atCommand
   *            at command that write to port
   * @return port values respond with AT command, only the values are returned
   *         by end with stringSuccessSign will be return, or Exception throws
   * @throws MobileException
   */
    protected synchronized StringBuffer receiveATCommandValuesResult( String atCommand, String stringSuccessSign ) throws MobileException
    {
        try
        {
            port.writePort( atCommand );
            StringBuffer portValues = channel.take();
            if ( portValues.lastIndexOf( stringSuccessSign ) == -1 )
            {
                throw new MobileException( "Error data come from port, current AT Command \"" + atCommand + "\" can not be recognized by Mobile." );
            }
            return portValues;
        }
        catch ( PortException pe )
        {
            throw new MobileException( pe.getMessage() );
        } catch (TimeoutException te) {
            throw new MobileException( te.getMessage() );
        } catch (InterruptedException ie) {
            throw new MobileException( ie.getMessage() );
        }
    }

	/**
	 * @return Returns the isConnecting.
	 */
	public boolean isConnecting() {
	    return isConnecting;
	}
}
