package com.loadtrend.app.mobile.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import loadtrend.mobile.Message;

public class GenerateSMSReport
{    
    public static void generateTXT( String fileName, Collection smsCollection )
    {
        try
        {
            FileWriter fileWriter = new FileWriter( fileName );
            
            Iterator it = smsCollection.iterator();
            while ( it.hasNext() )
            {
                Message message = (Message) it.next();
                
                fileWriter.write( "Index: " + message.getIndex() + "\r\n" );
                switch ( message.getStatus() )
                {
                    case Message.UNREAD_STATUS:
                        fileWriter.write( "Status: Unread\r\n" );
                    case Message.READ_STATUS:
                        fileWriter.write( "Status: Read\r\n" );
                    case Message.SENT_STATUS:
                        fileWriter.write( "Status: Sent\r\n" );
                    case Message.UNSENT_STATUS:
                        fileWriter.write( "Status: UnSent\r\n" );
                }
                fileWriter.write( "TeleNumber: " + message.getTeleNum() + "\r\n" + 
                                  "Conetnet: " + message.getContent() + "\r\n" + 
                                  "Time: " + message.getReceiveTime() + "\r\n\r\n" );
            }
            fileWriter.close();
        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
