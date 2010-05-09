package com.loadtrend.api.win32;

import junit.framework.TestCase;

public class TerminateProcessTest extends TestCase {
	
	public void testTerminateProcess() throws Exception {
        StringBuffer resultCodeErrorInfo = new StringBuffer();
        String shortPath = GetShortPathName.execute("F:\\music\\I Believe.mp3");
        boolean success = ShellExecuteEx.execute("C:\\Program Files\\Windows Media Player\\wmplayer.exe",
                                                 shortPath,
                                                 ShellExecuteEx.NORMAL,
                                                 resultCodeErrorInfo);
        int iResultCode = 0;
        if (success) {
            Thread.sleep(5000);
		    iResultCode = TerminateProcess.execute(Integer.valueOf(resultCodeErrorInfo.toString()));
        }
	    System.out.println("testTerminateProcess App id result code: " + iResultCode);
	}
}
