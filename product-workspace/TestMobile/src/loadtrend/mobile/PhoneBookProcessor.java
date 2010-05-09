/*
 * Created on 2005-6-21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.mobile;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;


/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PhoneBookProcessor extends Processor
{    
    /**
     * Get index array which is used to add phonebook(s)
     * @param int[numOfIndex] indexResults array should has been allocated size with numOfIndex.
     *        numOfIndex is the index number you want to get which is used to add phonebook(s)
     * @param phoneBookSpace the maximum phonebook number can be store to phoneBookMemoType
     * @param collection the collection contains phonebooks which should be belong to phoneBookMemoType parameter
     * @return true if successful or false if no more space to store phonebook(s)
     *         <p>Also the available index results will be passed to the first parameter int[] indexResults

     */
    public static boolean getPhoneBookIndexes( int[] indexResults, int phoneBookSpace, Collection collection )
    {
        int size = collection.size();
        int numOfIndex = indexResults.length;
        if ( numOfIndex + size > phoneBookSpace ) return false;

        // Get indexes of Collection
        int[] indexes = new int[size];
        Iterator it = collection.iterator();
        for ( int i = 0; it.hasNext(); i++ )
        {
            indexes[i] = ( (PhoneBook) it.next() ).getIndex();
        }
        Arrays.sort( indexes );
        
        // Get indexResults
        int k = 0;
        for ( int i = 0, j = 1; i < size && k < numOfIndex; )
        {
            if ( j == indexes[i] )
            {
                j++;
                i++;
            }
            else
            {
                indexResults[k++] = j++;
            }
        }
        
        int index = size == 0 ? 1 : ++indexes[size-1];
        while ( k < numOfIndex )
        {
            indexResults[k++] = index++;
        }
        
        return true;
    }
    
    /**
     * Check the validation of telephone number. Only *, +, # and numeric characters allowed.
     * @param teleNum
     * @return
     */
    public static boolean checkTeleNumValidation( String teleNum )
    {
        return teleNum.matches( "[0-9*+#]*" );
    }
}
