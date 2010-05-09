/*
 * IntCallTest1Win32.java
 * 
 * Created on 17.09.2004
 *
 * eaio: NativeCall - calling operating system methods from Java
 * Copyright (c) 2004-2006 Johann Burkard (<mailto:jb@eaio.com>)
 * <http://eaio.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 */
package com.eaio.nativecall;
import java.io.File;

import junit.framework.TestCase;

/**
 * Test case for the {@link com.eaio.nativecall.IntCall} class.
 * 
 * @author <a href="mailto:jb@eaio.com">Johann Burkard</a>
 * @version $Id: IntCallTest1Win32.java,v 1.3 2006/04/12 22:11:41 grnull Exp $
 */
public class IntCallTest1Win32 extends TestCase {

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
     * Constructor for IntCallTest1Win32.
     * @param arg0
     */
    public IntCallTest1Win32(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.awtui.TestRunner.run(IntCallTest1Win32.class);
    }

    /*
     * Test for int executeCall()
     */
    public void testExecuteCall() {
        IntCall cT = new IntCall("GetCurrentThread");
        assertEquals(0, cT.getLastErrorCode());
        int threadHandle = cT.executeCall();
        assertEquals(0, cT.getLastErrorCode());
        assertTrue(threadHandle != 0);
        cT.destroy();
        assertEquals(0, cT.getLastErrorCode());
    }

    /*
     * Test for int executeCall(Object)
     */
    public void testExecuteCallObject() {

        IntCall cT = new IntCall("GetCurrentThread");

        int threadHandle = cT.executeCall();

        IntCall tP = new IntCall("GetThreadPriority");

        int threadPriority = tP.executeCall(new Integer(threadHandle));

        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        Object o = new Object();
        synchronized (o) {
            try {
                o.wait(1000);
            }
            catch (InterruptedException ex) {}
        }

        tP.destroy();
        cT.destroy();

        synchronized (o) {
            try {
                o.wait(1000);
            }
            catch (InterruptedException ex) {}
        }

    }

    /*
     * Test for int executeCall(Object[])
     */
    public void testExecuteCallObjectArray() {

        // Copying files for fun

        IntCall cF = new IntCall("kernel32", "CopyFileA");

        assertEquals("{ IntCall: module = kernel32, function = CopyFileA }", cF.toString());

        File copy = new File("documentation_copy.txt");
        if (copy.exists())
            copy.delete();

        boolean copied =
            cF.executeBooleanCall(
                new Object[] {
                    "documentation/copying.txt",
                    "documentation_copy.txt",
                    Boolean.FALSE });

        cF.destroy();

        assertTrue(copied);
        assertTrue(copy.exists());
        assertTrue(
            copy.length() == new File("documentation/copying.txt").length());

        copy.delete();

        // Setting file attributes

        File copying = new File("documentation/copying.txt");
        assertTrue(copying.exists());

        IntCall getFileAttributesA = new IntCall("GetFileAttributesA");

        int attributes =
            getFileAttributesA.executeCall(copying.getAbsolutePath());

        assertEquals(0, attributes & 0xdf); // might be compressed

        IntCall setFileAttributesA = new IntCall("SetFileAttributesA");

        assertTrue(
            setFileAttributesA.executeBooleanCall(
                new Object[] {
                    copying.getAbsolutePath(),
                    new Integer(attributes | 2)}));

        assertEquals(
            2,
            getFileAttributesA.executeCall(copying.getAbsolutePath()) & 0xdf);

        assertTrue(
            setFileAttributesA.executeBooleanCall(
                new Object[] {
                    copying.getAbsolutePath(),
                    new Integer(attributes)}));

        assertEquals(
            0,
            getFileAttributesA.executeCall(copying.getAbsolutePath()) & 0xdf);

        getFileAttributesA.destroy();

        setFileAttributesA.destroy();

        // Setting file attributes (Unicode)

        IntCall getFileAttributesW = new IntCall("GetFileAttributesW");

        attributes =
            getFileAttributesW.executeCall(
                "\\\\?\\" + copying.getAbsolutePath());

        assertEquals(0, attributes & 0xdf); // might be compressed

        IntCall setFileAttributesW = new IntCall("SetFileAttributesW");

        assertTrue(
            setFileAttributesW.executeBooleanCall(
                new Object[] {
                    "\\\\?\\" + copying.getAbsolutePath(),
                    new Integer(attributes | 2)}));

        assertEquals(
            2,
            getFileAttributesW.executeCall(
                "\\\\?\\" + copying.getAbsolutePath())
                & 0xdf);

        assertTrue(
            setFileAttributesW.executeBooleanCall(
                new Object[] {
                    copying.getAbsolutePath(),
                    new Integer(attributes)}));

        assertEquals(
            0,
            getFileAttributesW.executeCall(
                "\\\\?\\" + copying.getAbsolutePath())
                & 0xdf);

        getFileAttributesW.destroy();

        setFileAttributesW.destroy();

        IntCall messageBoxA = new IntCall("user32", "MessageBoxA");

        messageBoxA.executeCall(
            new Object[] {
                new Integer(0),
                "No native code required!",
                "NativeCall",
                new Integer(0)});

        messageBoxA.destroy();

        // Enumerating process IDs

        IntCall enumProcesses = new IntCall("psapi", "EnumProcesses");

        byte[] buffer = new byte[1024];
        Holder length = new Holder(new Integer(0));

        boolean success =
            enumProcesses.executeBooleanCall(
                new Object[] { buffer, new Integer(buffer.length), length });

        assertTrue(success);
        assertTrue(((Integer) length.get()).intValue() > 0);

        enumProcesses.destroy();

    }

}
