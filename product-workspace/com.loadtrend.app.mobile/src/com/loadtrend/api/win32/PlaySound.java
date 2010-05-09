package com.loadtrend.api.win32;

import com.eaio.nativecall.IntCall;
import com.eaio.nativecall.NativeCall;

public class PlaySound {
    public static final Integer SND_FILENAME = new Integer(0x20000);
    public static final Integer SND_ALIAS = new Integer(0x10000);
    public static final Integer SND_SYNC = new Integer(0);
    public static final Integer SND_ASYNC = new Integer(0x1);
    
    public static boolean execute(String filename) {
        return execute(filename, SND_ASYNC);
    }
    
    public static boolean execute(String filename, Integer dwFlags) {
        boolean success = false;
        try {
            NativeCall.init();
            IntCall ic = new IntCall("winmm", "PlaySound");
            try {
                success = ic.executeBooleanCall(new Object[] { filename, new Integer(0), dwFlags});
            } finally {
                ic.destroy();
            }
        } catch (Exception e) {
            System.err.println("Exception of type " + e.getClass().getName()
                    + ":\n" + e.getMessage());
            e.printStackTrace();
        }
        return success;
    }
}