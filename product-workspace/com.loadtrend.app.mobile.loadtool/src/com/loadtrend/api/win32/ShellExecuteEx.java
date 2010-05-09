package com.loadtrend.api.win32;

import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.TCHAR;

import com.eaio.nativecall.IntCall;
import com.eaio.nativecall.NativeCall;

public class ShellExecuteEx {
    
    private static final int SEE_MASK_NOCLOSEPROCESS = 0x40;
    private static final int SEE_MASK_FLAG_NO_UI = 0x400;
    
    public static final int HIDDEN = 0;
    public static final int NORMAL = 1;
    public static final int MINIMIZED = 2;
    public static final int MAXIMIZED = 3;
    
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
    
//    public static int launch (String fileName) {
//        if (fileName == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
//        
//
//        TCHAR buffer2 = new TCHAR (0, fileName, true);
//        int byteCount2 = buffer2.length () * TCHAR.sizeof;
//        int lpFile = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount2);
//        OS.MoveMemory (lpFile, buffer2, byteCount2);
//        
//        SHELLEXECUTEINFO info = new SHELLEXECUTEINFO ();
//        info.cbSize = SHELLEXECUTEINFO.sizeof;
//        info.lpVerb = lpVerb;
//        info.lpFile = lpFile;
//        info.nShow = OS.SW_SHOW;
//        info.fMask = SEE_MASK_NOCLOSEPROCESS;
//        
//        OS.ShellExecuteEx (info);
//        
//        int result = info.hProcess;
//        
//        if (lpVerb != 0) OS.HeapFree (hHeap, 0, lpVerb);    
//        if (lpFile != 0) OS.HeapFree (hHeap, 0, lpFile);
//        
//        return result;
//    }
    
    /**
     * Execute the file or program or program and file. 
     * @param command file path or program path. Not nullable.
     * @param path file path, null and "" is acceptable and should be 8.3 file format.
     * @param showCommand
     * <li>ShellExecuteEx.HIDDEN
     * <li>ShellExecuteEx.NORMAL
     * <li>ShellExecuteEx.MINIMIZED
     * <li>ShellExecuteEx.MAXIMIZED
     * @param resultCodeErrorInfo if true return Application process handle, otherwise false return error info.
     * @return true if no error, otherwise false.
     */
    public static boolean execute(String command, String path, int showCommand, StringBuffer resultCodeErrorInfo) {
        try {
            NativeCall.init();
            IntCall ic = new IntCall("shell32", "ShellExecuteExW");
            try {
                int hHeap = OS.GetProcessHeap ();
                int lpVerb = getStringPointer(hHeap, "open");
                int lpFile = getStringPointer(hHeap, command);
                int lpParameters = 0;
                if (path != null && !path.equals("")) {
                    lpParameters = getStringPointer(hHeap, path);
                }
                byte[] SHELLEXECUTEINFO = new byte[60];
                SHELLEXECUTEINFO[0] = 60;                             //DWORD cbSize;
                setIntToBytes(SEE_MASK_NOCLOSEPROCESS | SEE_MASK_FLAG_NO_UI, SHELLEXECUTEINFO, 4);          //ULONG fMask;
                SHELLEXECUTEINFO[8] = 0;                              //HWND hwnd;
                setIntToBytes(lpVerb, SHELLEXECUTEINFO, 12);          //LPCTSTR lpVerb;
                setIntToBytes(lpFile, SHELLEXECUTEINFO, 16);          //LPCTSTR lpFile;
                setIntToBytes(lpParameters, SHELLEXECUTEINFO, 20);    //LPCTSTR lpParameters;
                SHELLEXECUTEINFO[24] = 0;                             //LPCTSTR lpDirectory;
                SHELLEXECUTEINFO[28] = (byte) showCommand;            //int nShow: 0:hidden, 1:normal, 2:MINIMIZED, 3:MAXIMIZED;
                
                ic.executeCall(new Object[] {SHELLEXECUTEINFO});
                if (lpVerb != 0) OS.HeapFree (hHeap, 0, lpVerb);    
                if (lpFile != 0) OS.HeapFree (hHeap, 0, lpFile);
                if (lpParameters != 0) OS.HeapFree (hHeap, 0, lpParameters);
                
                int iResultCode = getIntFromBytes(SHELLEXECUTEINFO, 32, 4); //HINSTANCE hInstApp;
                if (iResultCode >= 0 && iResultCode <= 32) {
                    resultCodeErrorInfo.append(errorInfo[iResultCode]);
                    return false;
                }
                resultCodeErrorInfo.append(getIntFromBytes(SHELLEXECUTEINFO, 56, 4)); //HANDLE hProcess;
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
    
    private static int getStringPointer(int hHeap, String string) {
        /* Use the character encoding for the default locale */
        TCHAR buffer1 = new TCHAR (0, string, true); //$NON-NLS-1$
        int byteCount1 = buffer1.length () * TCHAR.sizeof;
        int lpVerb = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount1);
        OS.MoveMemory (lpVerb, buffer1, byteCount1);
        return lpVerb;
    }
    
    private static void setIntToBytes(int target, byte[] bytes, int startPos) {
        StringBuffer hexString = new StringBuffer(Integer.toHexString(target));
        if (hexString.length() % 2 == 1) hexString = hexString.insert(0, "0");
        while (hexString.length() != 0) {
            String string = hexString.substring(hexString.length() - 2, hexString.length());
            hexString.delete(hexString.length() - 2, hexString.length());
            bytes[startPos++] = (byte) Integer.parseInt(string, 16);
        }
    }
    
    private static int getIntFromBytes(byte[] bytes, int startPos, int length) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int j = bytes[i + startPos];
            if (j < 0) j = 256 + j;
            String string = Integer.toHexString(j);
            if (string.length() == 1) string = "0" + string;
            hexString = hexString.insert(0, string);
        }
        return Integer.parseInt(hexString.toString(), 16);
    }
}

//DWORD cbSize;
//ULONG fMask;
//HWND hwnd;
//LPCTSTR lpVerb;
//LPCTSTR lpFile;
//LPCTSTR lpParameters;
//LPCTSTR lpDirectory;
//int nShow;
//HINSTANCE hInstApp;
//LPVOID lpIDList;
//LPCTSTR lpClass;
//HKEY hkeyClass;
//DWORD dwHotKey;
//union {
//    HANDLE hIcon;
//    HANDLE hMonitor;
//} DUMMYUNIONNAME;
//HANDLE hProcess;


