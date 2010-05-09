/*
 * Created on 2005-7-9
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.mobile.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import loadtrend.mobile.Message;
import loadtrend.mobile.MessageArrivalListener;
import loadtrend.mobile.Mobile;
import loadtrend.mobile.MobileException;
import loadtrend.mobile.MobileFactory;
import loadtrend.mobile.Port;
import loadtrend.mobile.SerialPort;
import loadtrend.mobile.StartOrStopMessageArrivalListener;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NewMessageArrivalTester
{   
    private static Mobile  mobile = null;
    
    public static void main( String[] args ) throws MobileException
    {
        Port port = SerialPort.getInstance();
        ArrayList list = null;

        MobileFactory mf = MobileFactory.getInstance();
        list = mf.listTypes();
        System.out.println( list );

        mobile = mf.createMobile( port, "ERICSSON_T618" );

        mobile.openMobilePort( "COM3" );

        long seconds = System.currentTimeMillis();
        
        mobile.addMessageArrivalListener( new MessageArrivalListener() {
            public void handler(Message message) {
                if (message.getContent().equals("上海")) {
                    Message sentMessage = new Message();
                    sentMessage.setTeleNum(message.getTeleNum());
                    sentMessage.setContent("6月25日  阵雨 29℃/24℃ 东南风3-4级转南风3-4级 6月26日  阵雨 31℃/24℃ 南风3-4级转西南风3-4级");
                    try {
                        mobile.sendMessage(sentMessage);
                    } catch (MobileException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
        StartOrStopMessageArrivalListener listener = new StartOrStopMessageArrivalListener() {
            public void startMessageArrival() {
                System.out.println("start");
            }

            public void stopMessageArrival() {
                System.out.println("stop");
            }
        };
        mobile.startMessageArrivalWorkThread(1, listener);
        Message message = new Message();
        message.setContent( "上海" );
        message.setTeleNum( "13916939847" );
        mobile.sendStandardMessage( message );
//        mobile.saveStandardMessage( message );
//        readMessages();
//        readMessages();
//        readMessages();
        
        try {
            while (true) {
                Thread.sleep( 1000 );
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println( "\r\n" + ( System.currentTimeMillis() - seconds )
                / 1000.0 + " seconds passed." );

        mobile.closeMobilePort();
    }
    
    private static void readMessages() throws MobileException
    {
        Message message = null;
        TreeSet messages = mobile.listMessages( Message.ALL_STATUS, Mobile.SMS_ME_MEMO);
        Iterator it = messages.iterator();
        while ( it.hasNext() )
        {
            message = (Message) it.next();
            System.out.println( "====================================================" );
            System.out.println( "index: " + message.getIndex() );
            System.out.println( "status: " + message.getStatus() );
            System.out.println( "receive time: " + message.getReceiveTime() );
            System.out.println( "delivery valid time: " + message.getDeliveryValidTime() );
            System.out.println( "tele num: " + message.getTeleNum() );
            System.out.println( "message center num: " + message.getMessageCenterNum() );
            System.out.println( "reserve sign: " + message.getReserveSign() );
            System.out.println( "content: " + message.getContent() );
            System.out.println( "pdu: " + message.getPdu() );
        }
    }
}
