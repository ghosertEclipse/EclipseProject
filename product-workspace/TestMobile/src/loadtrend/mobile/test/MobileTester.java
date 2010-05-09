/*
 * Created on 2005-6-1
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.mobile.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

import junit.framework.TestCase;
import loadtrend.mobile.Message;
import loadtrend.mobile.MessageProcessor;
import loadtrend.mobile.Mobile;
import loadtrend.mobile.MobileConfig;
import loadtrend.mobile.MobileException;
import loadtrend.mobile.MobileFactory;
import loadtrend.mobile.MobileInfo;
import loadtrend.mobile.PhoneBook;
import loadtrend.mobile.Port;
import loadtrend.mobile.SerialPort;
import loadtrend.mobile.ericsson.Ericsson;
import loadtrend.mobile.siemens.Siemens;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MobileTester extends TestCase
{
    private Mobile mobile = null;
    
    private MobileConfig mobileConfig = null;
    
    public static void main( String[] args )
    {
        junit.textui.TestRunner.run( MobileTester.class );
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        Port port = SerialPort.getInstance();
        ArrayList list = null;
        
        MobileFactory mf = MobileFactory.getInstance();
        list = mf.listTypes();
        System.out.println( list );
        
        String type = (String) list.get( 1 );
        mobile = mf.createMobile( port, "ERICSSON_T618" );
        
        mobileConfig = mobile.getMobileConfig();
        
        mobile.openMobilePort( "COM3" );      

        // Get a available mobile port
//        String portName = mobile.monitorMobilePort();
//        if ( portName != null )
//        {
//            // Throw MobileException if the port can not be opened
//            mobile.openMobilePort( portName );
//            System.out.println( portName + ": successful" );
//        }
//        else
//        {
//            System.out.println( "No mobile port is found" );
//        }
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        mobile.closeMobilePort();
    }

//  public void testMonitorMobilePhonePort() throws MobileException
//  {
//      mobile.closeMobilePort();
//      
//      String portName = null;
//      
//      // Get a available mobile port
//      portName = mobile.monitorMobilePort();
//      if ( portName != null )
//      {
//          // Throw MobileException if the port can not be opened
//          mobile.openMobilePort( portName );
//          mobile.closeMobilePort();
//          System.out.println( portName + ": successful" );
//      }
//      else
//      {
//          System.out.println( "No mobile port is found" );
//      }
//  }
    
//    public void testGetSMSCNum() throws MobileException
//    {
//        System.out.println( mobile.getSMSCNum() );
//    }
    
//    public void testGetMobileInfo()
//    {
//        MobileInfo mi = mobile.getMoibleInfo();
//        System.out.println( "nameOfManu: " + mi.getNameOfManu() );
//        System.out.println( "nameOfTele: " + mi.getNameOfTele() );
//        System.out.println( "verOfSW: " + mi.getVerOfSW() );
//        System.out.println( "valueOfIMEI: " + mi.getValueOfIMEI() );
//        System.out.println( "batteryCharge: " + mi.getBatteryCharge() );
//        System.out.println( "signalQuality: " + mi.getSignalQuality() );
//        System.out.println( "ownerTeleNum: " + mi.getOwnerTeleNum() );
//    }
    
//    public void testSaveMessage() throws MobileException
//    {
//      // 70 unicode character
//      // String string = "ÏûÏ¢±£´æ²âÊÔ!";
//      
//      // more than 70 unicode character
//      String string = "Ôª±¦´ó¶¹ÓÍ£¡£¡£¡Ôª±¦´ó¶¹ÓÍ£¡£¡£¡Ôª±¦´ó¶¹ÓÍ£¡£¡£¡Ôª±¦´ó¶¹ÓÍ£¡£¡£¡Ôª±¦´ó¶¹ÓÍ£¡£¡£¡Ôª±¦´ó¶¹ÓÍ£¡£¡£¡Ôª±¦´ó¶¹ÓÍ£¡£¡£¡Ôª±¦´ó¶¹ÓÍ£¡£¡£¡Ôª±¦´ó¶¹sjJiawei";
//      
//      Message message = new Message();
//      message.setContent( string );
//      message.setMessageCenterNum( "+8613800210500" );
//      message.setTeleNum( "13916939847" );
//      
//      Message[] messages = new Message[1];
//      
//      // set unsent message, only new message can be modify and create before sending
//      messages[0] = (Message) message.clone();
//      
//      // set sent, read, unread message, existing messages must not be modify before sending
//      mobile.setSMSMemoGetSMSNum( Mobile.SMS_SM_MEMO );
////      message = (Message) mobile.listMessages( Message.SENT_STATUS ).iterator().next();
////      messages[1] = (Message) message.clone();
////      message = (Message) mobile.listMessages( Message.READ_STATUS ).iterator().next();
////      messages[2] = (Message) message.clone();
////      message = (Message) mobile.listMessages( Message.UNREAD_STATUS ).iterator().next();
////      messages[3] = (Message) message.clone();
//      
//      // set and check mobile storage
//      if ( mobile.checkSmsSpace( messages.length, Mobile.SMS_SM_MEMO ) > 0 )
//      {
//          messages = mobile.saveMessage( messages );
//      }
//      
//      for ( int i = 0; i < messages.length; i++ )
//      {
//          System.out.println( messages[i].getIndex() );
//      }
//      
//      testReadSMS();
//      
////      mobile.deleteMessage( messages );
////      
////      testReadSMS();
//    }
    
//    public void testDeleteMessage() throws MobileException
//    {
//        mobile.setSMSMemoGetSMSNum( Mobile.SMS_SM_MEMO );
//        mobile.deleteMessage( 27 );
//    }
    
//    public void testCheckSmsSpace() throws MobileException
//    {
//        System.out.println( "SMS_ME_MEMO: " + mobile.getSmsSpace( Mobile.SMS_ME_MEMO ) );
//        System.out.println( "SMS_SM_MEMO: " + mobile.getSmsSpace( Mobile.SMS_SM_MEMO ) );
//        
//        System.out.println( "SMS_ME_MEMO: " + mobile.setSMSMemoGetSMSNum( Mobile.SMS_ME_MEMO ) );
//        System.out.println( "SMS_SM_MEMO: " + mobile.setSMSMemoGetSMSNum( Mobile.SMS_SM_MEMO ) );
//        
//        System.out.println( mobile.checkSmsSpace( 18, Mobile.SMS_SM_MEMO ) );
//    }
    
//    public void testMobileTime() throws MobileException
//    {
//
//        System.out.println( mobile.getMobileTime() );
//        
//        mobile.setMobileTime( "05/06/10,21:52:10" );
//        
//        System.out.println( mobile.getMobileTime() );
//        
//    }
    
//    public void testReadSMS() throws MobileException
//    {
//      Message message = null;
//      int num = mobile.setSMSMemoGetSMSNum( Mobile.SMS_SM_MEMO );
//      TreeSet messages = mobile.listMessages( Message.ALL_STATUS );
//      Iterator it = messages.iterator();
//      while ( it.hasNext() )
//      {
//          message = (Message) it.next();
//          System.out.println( "====================================================" );
//          System.out.println( "index: " + message.getIndex() );
//          System.out.println( "status: " + message.getStatus() );
//          System.out.println( "receive time: " + message.getReceiveTime() );
//          System.out.println( "delivery valid time: " + message.getDeliveryValidTime() );
//          System.out.println( "tele num: " + message.getTeleNum() );
//          System.out.println( "message center num: " + message.getMessageCenterNum() );
//          System.out.println( "reserve sign: " + message.getReserveSign() );
//          System.out.println( "content: " + message.getContent() );
//          System.out.println( "pdu: " + message.getPdu() );
//      }
//    }
    
//    public void testSendSMS() throws MobileException
//    {
//        // 70 unicode character
//        String string = "Ôª±¦´ó¶¹ÓÍ£¡£¡£¡Ôª±¦´ó¶¹ÓÍ£¡£¡£¡Ôª±¦´ó¶¹ÓÍ£¡£¡£¡Ôª±¦´ó¶¹ÓÍ£¡£¡£¡Ôª±¦´ó¶¹ÓÍ£¡£¡£¡Ôª±¦´ó¶¹ÓÍ£¡£¡£¡Ôª±¦´ó¶¹ÓÍ£¡£¡£¡Ôª±¦´ó¶¹ÓÍ£¡£¡£¡Ôª±¦´ó¶¹sj";
//        
//        // more than 70 unicode character
//        // String string = "Ôª±¦´ó¶¹ÓÍ£¡£¡£¡Ôª±¦´ó¶¹ÓÍ£¡£¡£¡Ôª±¦´ó¶¹ÓÍ£¡£¡£¡Ôª±¦´ó¶¹ÓÍ£¡£¡£¡Ôª±¦´ó¶¹ÓÍ£¡£¡£¡Ôª±¦´ó¶¹ÓÍ£¡£¡£¡Ôª±¦´ó¶¹ÓÍ£¡£¡£¡Ôª±¦´ó¶¹ÓÍ£¡£¡£¡Ôª±¦´ó¶¹sjJiawei";
//        
//        // 160 non-unicode
//        // String string = 
//        
//        // more than 160 non-unicode
//        // String stirng =
//        
//        // ""
//        // String string ="";
//        
//        Message message = new Message();
//        message.setContent( string );
//        message.setMessageCenterNum( "+8613800210500" );
//        
//        String[] teleNum = new String[]{ "13916939847", "+8613916846861" };
//        
//        mobile.sendMessage( message, teleNum );
//        
//        // equals above
//        // Message[] messages = MessageProcessor.splitMessageByTeleNum( message, teleNum );
//        // mobile.sendMessage( messages );
//        
//    }

/*********************************************PhoneBook handler**********************************************************/
    
//    public void testReadDialledPhoneBooks() throws MobileException
//    {
//        if ( mobileConfig.canListDialledPhoneBooks() )
//        {
//            TreeSet ts = mobile.listDialledPhoneBooks();
//            System.out.println( "Listing Dialled PhoneBooks................................\n" );
//            print( ts );
//        }
//        else
//        {
//            System.out.println( "This type of mobile do not support to list Dialled PhoneBooks.............\n" );
//        }
//    }
//    
//    public void testReadMissedPhoneBooks() throws MobileException
//    {
//        TreeSet ts = mobile.listMissedPhoneBooks();
//        System.out.println( "Listing Missed PhoneBooks................................\n" );
//        print( ts );
//    }
//    
//    public void testReadReceivedPhoneBooks() throws MobileException
//    {
//        TreeSet ts = mobile.listReceivedPhoneBooks();
//        System.out.println( "Listing Received PhoneBooks................................\n" );
//        print( ts );
//    }
//    
//    private void print( Collection collection )
//    {
//        Iterator it = collection.iterator();
//        while ( it.hasNext() )
//        {
//            PhoneBook phoneBook = (PhoneBook) it.next();
//            System.out.println( "====================================================" );
//            System.out.println( "index: " + phoneBook.getIndex() );
//            System.out.println( "teleNum: " + phoneBook.getTeleNum() );
//            System.out.println( "name: " + phoneBook.getName() );
//            System.out.println( "time: " + phoneBook.getTime() );
//        }
//        System.out.println();
//    }
//    
//    public void savePhoneBook( PhoneBook phoneBook, String phoneBookMemoType ) throws MobileException
//    {
//        System.out.println( "Prepare to add phone book......................................................" );
//        System.out.println( "Phone book text: " + phoneBook.getName() );
//        System.out.println( "Phone book telephone number: " + phoneBook.getTeleNum() );
//        
//        TreeSet ts = mobile.listPhoneBooks( phoneBookMemoType );
//        System.out.println( "Listing PhoneBooks in " + phoneBookMemoType + " ................................" );
//        print( ts );
//        
//        mobile.writePhoneBook( phoneBook, ts, phoneBookMemoType );
//        System.out.println( "PhoneBook added, its index is: " + phoneBook.getIndex() );
//        ts = mobile.listPhoneBooks( phoneBookMemoType );
//        System.out.println( "Listing PhoneBooks in " + phoneBookMemoType + " after add new phoneBook................................" );
//        print( ts );
//        
//        phoneBook.setTeleNum( "99999999" );
//        mobile.modifyPhoneBook( phoneBook, phoneBookMemoType );
//        System.out.println( "PhoneBook modified, its index is: " + phoneBook.getIndex() );
//        ts = mobile.listPhoneBooks( phoneBookMemoType );
//        System.out.println( "Listing PhoneBooks in " + phoneBookMemoType + " after modify added phoneBook................................" );
//        print( ts );
//        
//        mobile.deletePhoneBook( phoneBook );
//        System.out.println( "PhoneBook deleted, its index is: " + phoneBook.getIndex() );
//        ts = mobile.listPhoneBooks( phoneBookMemoType );
//        System.out.println( "Listing PhoneBooks in " + phoneBookMemoType + " after delete added phonebook................................" );
//        print( ts );
//    }
//    
//    public void testSavePhoneBookToSMWithNoneUnicode() throws MobileException
//    {
//        PhoneBook phoneBook = new PhoneBook();
//        phoneBook.setName( "aaaaaaaaaaaa" );
//        phoneBook.setTeleNum( "10101010101" );
//        savePhoneBook( phoneBook, Mobile.PB_SM_MEMO );
//    }
//    
//    public void testSavePhoneBookToSMWithUnicode() throws MobileException
//    {
//        PhoneBook phoneBook = new PhoneBook();
//        phoneBook.setName( "ÄãºÃÄãºÃÅ·" );
//        phoneBook.setTeleNum( "10101010101" );
//        savePhoneBook( phoneBook, Mobile.PB_SM_MEMO );
//    }
//    
//    public void testSavePhoneBookToME() throws MobileException
//    {
//        if ( mobileConfig.canWritePhoneBookToME() )
//        {
//            PhoneBook phoneBook = new PhoneBook();
//            phoneBook.setName( "ÄãºÃab!" );
//            phoneBook.setTeleNum( "10101010101" );
//            savePhoneBook( phoneBook, Mobile.PB_ME_MEMO );
//        }
//        else
//        {
//            System.out.println( "This type of mobile do not support to operate PhoneBooks in ME." );
//        }
//    }

}
