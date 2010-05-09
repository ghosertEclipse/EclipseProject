package com.loadtrend.app.mobile.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import loadtrend.mobile.PhoneBook;

public class GeneratePBReport
{   
    public static void generateTXT( String fileName, Collection pbCollection )
    {
        try
        {
            FileWriter fileWriter = new FileWriter( fileName );
            
            Iterator it = pbCollection.iterator();
            while ( it.hasNext() )
            {
                PhoneBook phoneBook = (PhoneBook) it.next();
                
                fileWriter.write( "Index: " + phoneBook.getIndex() + "\r\n" );
                fileWriter.write( "Name: " + phoneBook.getName() + "\r\n" + 
                                  "TeleNumber: " + phoneBook.getTeleNum() + "\r\n" + 
                                  "Time: " + phoneBook.getTime() + "\r\n\r\n" );
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
