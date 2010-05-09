package com.loadtrend.api.win32;

import com.eaio.nativecall.IntCall;
import com.eaio.nativecall.NativeCall;

public class TerminateProcess {

    /**
     * If the function succeeds, the return value is nonzero.
     * If the function fails, the return value is zero. To get extended error information, call GetLastError.
     * @param hProcess
     * @return
     */
    public static int execute(Integer hProcess) {
        try {
            NativeCall.init();
            IntCall ic = new IntCall("TerminateProcess");
            try {
                int iResultCode = ic.executeCall(new Object[] {hProcess, new Integer(1)});
                return iResultCode;
            } finally {
                ic.destroy();
            }
        } catch (Exception e) {
            System.err.println("Exception of type " + e.getClass().getName()
                    + ":\n" + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
}
