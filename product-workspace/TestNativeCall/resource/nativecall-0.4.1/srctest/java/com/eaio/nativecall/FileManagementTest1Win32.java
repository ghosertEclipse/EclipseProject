/*
 * FileManagementTest1Win32.java
 * 
 * Created on 11.12.2004
 *
 * eaio: NativeCall - calling operating system methods from Java
 * Copyright (c) 2004-2006 Johann Burkard (<mailto:jb@eaio.com>)
 * <http://eaio.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */

package com.eaio.nativecall;

import java.io.File;
import java.security.AccessController;
import java.util.Arrays;

import junit.framework.TestCase;
import sun.security.action.GetPropertyAction;

/**
 * Some more complicated tests for general file management functions.
 * 
 * @author <a href="mailto:jb@eaio.com">Johann Burkard</a>
 * @version $Id: FileManagementTest1Win32.java,v 1.3 2006/04/12 22:11:41 grnull Exp $
 */
public class FileManagementTest1Win32 extends TestCase {

    static {
        try {
            NativeCall.init();
        }
        catch (Throwable thrw) {
            thrw.printStackTrace();
            fail(thrw.getLocalizedMessage());
        }
    }

    /**
     * Constructor for FileManagementTest1Win32.
     * @param arg0
     */
    public FileManagementTest1Win32(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.awtui.TestRunner.run(FileManagementTest1Win32.class);
    }

    public void testGetTempPath() {

        IntCall getTempPathA = new IntCall("GetTempPathA");

        byte[] byteBuf = new byte[256];

        int bufLength =
            getTempPathA.executeCall(
                new Object[] { new Integer(byteBuf.length), byteBuf });

        String path = new String(byteBuf, 0, bufLength);

        File tempDir = new File(path);
        assertTrue(tempDir.exists());
        assertTrue(tempDir.isDirectory());

        GetPropertyAction a = new GetPropertyAction("java.io.tmpdir");

        assertEquals(
            tempDir,
            new File(((String) AccessController.doPrivileged(a))));

        getTempPathA.destroy();

        IntCall getTempPathW = new IntCall("kernel32", "GetTempPathW");

        char[] charBuf = new char[256];

        bufLength =
            getTempPathW.executeCall(
                new Object[] { new Integer(charBuf.length), charBuf });
        assertEquals(0, getTempPathW.getLastErrorCode());

        path = new String(charBuf, 0, bufLength);

        tempDir = new File(path);
        assertTrue(tempDir.exists());
        assertTrue(tempDir.isDirectory());

        assertEquals(
            tempDir,
            new File(((String) AccessController.doPrivileged(a))));

        getTempPathW.destroy();
        assertEquals(0, getTempPathW.getLastErrorCode());

    }

    public void testCreateFile() {

        File target = new File("documents/file_management_test.txt");

        IntCall createFileA = new IntCall("CreateFileA");
        IntCall closeHandle = new IntCall("CloseHandle");

        byte[] securityAttributes = new byte[12];
        securityAttributes[0] = (byte) 12;

        // Different methods to do the same

        int fileHandle = 0;

        try {

            fileHandle =
                createFileA.executeCall(
                    new Object[] {
                        "documents/file_management_test.txt",
                        new Integer(0x80000000 | 0x40000000 | 0x00010000),
                        null,
                        securityAttributes,
                        new Integer(2),
                        null,
                        null });

        }
        catch (NullPointerException ex) {
            ex.printStackTrace();
            fail(ex.getLocalizedMessage());
        }

        assertEquals(0, createFileA.getLastErrorCode());
        assertTrue(target.exists());

        boolean closed =
            closeHandle.executeBooleanCall(new Integer(fileHandle));
        assertEquals(0, closeHandle.getLastErrorCode());

        assertTrue(closed);

        createFileA.destroy();
        assertEquals(0, createFileA.getLastErrorCode());
        closeHandle.destroy();
        assertEquals(0, closeHandle.getLastErrorCode());

        target.delete();

        assertFalse(target.exists());
    }

    public void testGetVolumeInformation() {

        IntCall getVolumeInformationA = new IntCall("GetVolumeInformationA");

        Holder volumeSerialNumber = new Holder(new Integer(0));
        Holder maximumComponentLength = new Holder(new Integer(0));
        Holder fileSystemFlags = new Holder(new Integer(0));

        int result =
            getVolumeInformationA.executeCall(
                new Object[] {
                    "c:\\",
                    null,
                    null,
                    volumeSerialNumber,
                    maximumComponentLength,
                    fileSystemFlags,
                    null,
                    null });
        assertEquals(0, getVolumeInformationA.getLastErrorCode());
        assertNull(getVolumeInformationA.getLastError());
        getVolumeInformationA.destroy();
        assertEquals(0, getVolumeInformationA.getLastErrorCode());
        assertNull(getVolumeInformationA.getLastError());

        assertTrue(result != 0);
        assertTrue(((Integer) volumeSerialNumber.get()).intValue() != 0);

    }

    public void testGetFileAttributesEx() {

        IntCall getFileAttributesExA = new IntCall("GetFileAttributesExA");

        byte[] buf = new byte[36];
        Arrays.fill(buf, (byte) 0xff);

        int success =
            getFileAttributesExA.executeCall(
                new Object[] { "c:\\autoexec.bat", new Integer(0), buf });
        getFileAttributesExA.destroy();
        assertTrue(0 != success);

//        try {
//            new HexDumpEncoder().encodeBuffer(buf, System.out);
//        }
//        catch (IOException ex) {
//            ex.printStackTrace();
//            fail(ex.toString());
//        }
//
//        if (success == 0) {
//            IntCall getLastError = new IntCall("GetLastError");
//            System.out.println(getLastError.executeCall());
//            getLastError.destroy();
//        }

    }

}
