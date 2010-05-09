package com.loadtrend.api.win32;

import com.eaio.nativecall.IntCall;
import com.eaio.nativecall.NativeCall;

public class GetShortPathName {

    /**
     * returns the short filename (8.3) for a file in Windows
     * 
     * @param longFileName 
     * @return a string with the short filename, or null if an error occurred or the
     *         file does not exist.
     */
    public static String execute(String longFileName) {
        try {
            // the result
            String shortName = null;
            NativeCall.init();
            IntCall ic = new IntCall("GetShortPathNameW");
            try {
                // size of result is at most the long file name (times 2 for the
                // number
                // of bytes)
                byte[] data = new byte[longFileName.length() *2];
                int iResultCode = ic.executeCall(new Object[] { longFileName,
                        data, new Integer(longFileName.length()) });
                if (iResultCode > 0) {
                    // iResultCode is length
                    int length = data.length > (iResultCode * 2) ? (iResultCode * 2)
                            : data.length;
                    byte[] data2 = new byte[length];
                    System.arraycopy(data, 0, data2, 0, length);

                    // structResult contains letter space letter space letter
                    String structResult = new String(data2);
                    // remove spaces in between
                    StringBuffer result = new StringBuffer();
                    for (int i = 0; i < structResult.length(); i += 2)
                        result.append(structResult.charAt(i));
                    shortName = result.toString().trim();
                    if (shortName.equals(""))
                        shortName = longFileName;
                }
            } finally {
                ic.destroy();
            }
            return shortName;
        } catch (Exception e) {
            System.err.println("Exception of type " + e.getClass().getName()
                    + ":\n" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
