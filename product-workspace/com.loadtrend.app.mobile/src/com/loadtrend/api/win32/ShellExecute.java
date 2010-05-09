package com.loadtrend.api.win32;

import com.eaio.nativecall.IntCall;
import com.eaio.nativecall.NativeCall;

public class ShellExecute {
	public static final Integer HIDDEN = new Integer(0);
	public static final Integer NORMAL = new Integer(1);
	public static final Integer MINIMIZED = new Integer(2);
	public static final Integer MAXIMIZED = new Integer(3);
	
	private static final String[] errorInfo = new String[] {
		   "The operating system is out of memory or resources.",
		   "undocumented error.",
		   "The specified file was not found.",
		   "The specified path was not found.",
		   "undocumented error.",
		   "Windows 95 only: The operating system denied access to the specified file.",
		   "undocumented error.",
		   "undocumented error.",
		   "Windows 95 only: There was not enough memory to complete the operation.",
		   "undocumented error.",
		   "Wrong Windows version",
		   "The .EXE file is invalid (non-Win32 .EXE or error in .EXE image).",
		   "Application was designed for a different operating system",
		   "Application was designed for MS-DOS 4.0",
		   "undocumented error.",
		   "Attempt to load a real-mode program",
		   "Attempt to load a second instance of an application with non-readonly data segments",
		   "undocumented error.",
		   "undocumented error.",
		   "Attempt to load a compressed application file",
		   "Dynamic-link library (DLL) file failure",
		   "undocumented error.",
		   "undocumented error.",
		   "undocumented error.",
		   "undocumented error.",
		   "undocumented error.",
		   "A sharing violation occurred.",
		   "The filename association is incomplete or invalid.",
		   "The DDE transaction could not be completed because the request timed out.",
		   "The DDE transaction failed.",
		   "The DDE transaction could not be completed because other DDE transactions were being processed.",
		   "There is no application associated with the given filename extension.",
		   "Windows 95 only: The specified dynamic-link library was not found."
	};
	
	/**
	 * Execute the file or program or program and file. 
	 * @param command file path or program path. Not nullable.
     * @param path file path, null and "" is acceptable and should be 8.3 file format.
	 * @param showCommand
	 * <li>ShellExecute.HIDDEN
	 * <li>ShellExecute.NORMAL
	 * <li>ShellExecute.MINIMIZED
	 * <li>ShellExecute.MAXIMIZED
	 * @param resultCodeErrorInfo if true return HINSTANCE, otherwise false return error info.
	 * @return true if no error, otherwise false.
	 */
    public static boolean execute(String command, String path, Integer showCommand, StringBuffer resultCodeErrorInfo) {
        try {
            NativeCall.init();
            IntCall ic = new IntCall("shell32", "ShellExecuteW");
            try {
            	int iResultCode = 0;
            	if (path == null || path.equals("")) {
                    iResultCode = ic.executeCall(new Object[] { new Integer(0), "open", command, "", "", showCommand});
            	} else {
                    iResultCode = ic.executeCall(new Object[] { new Integer(0), "open", command, path, "", showCommand});
            	}
                if (iResultCode >= 0 && iResultCode <= 32) {
                	resultCodeErrorInfo.append(errorInfo[iResultCode]);
                	return false;
                }
              	resultCodeErrorInfo.append(String.valueOf(iResultCode));
                return true;
            } finally {
                ic.destroy();
            }
        } catch (Exception e) {
            System.err.println("Exception of type " + e.getClass().getName()
                    + ":\n" + e.getMessage());
            e.printStackTrace();
        }
        resultCodeErrorInfo.append("native call exception.");
        return false;
    }
}
