/*
 * Win32VerifierTest.java
 * 
 * Created on 16.09.2004
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
import junit.framework.TestCase;

import com.eaio.nativecall.Win32Verifier;

/**
 * Test case for the {@link com.eaio.nativecall.Win32Verifier} class.
 * 
 * @author <a href="mailto:jb@eaio.com">Johann Burkard</a>
 * @version $Id: Win32VerifierTest.java,v 1.2 2006/01/05 19:57:07 grnull Exp $
 */
public class Win32VerifierTest extends TestCase {

    /**
     * Constructor for Win32VerifierTest.
     * @param arg0
     */
    public Win32VerifierTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.awtui.TestRunner.run(Win32VerifierTest.class);
    }

    public void testVerifyModuleName() {
        Win32Verifier ver = new Win32Verifier();
        
        assertEquals("kernel32", ver.verifyModuleName(null));
        assertEquals("kernel32", ver.verifyModuleName(""));

        String module = "blabla.dll";
        assertTrue(module == ver.verifyModuleName(module));

        module = "bla\\blorb.dll";
        assertTrue(module == ver.verifyModuleName(module));

        module = "c:/bla/blorb/blub\\bla.dll";
        assertEquals(
            "c:\\bla\\blorb\\blub\\bla.dll",
            ver.verifyModuleName(module));
    }

    public void testVerifyFunctionName() {
        Win32Verifier ver = new Win32Verifier();

        try {
            ver.verifyFunctionName(null);
            fail("Did not throw NPE.");
        }
        catch (NullPointerException ex) {}

        try {
            ver.verifyFunctionName("");
            fail("Did not throw NPE.");
        }
        catch (NullPointerException ex) {}

        String function = "GetProcessId";
        assertTrue(function == ver.verifyFunctionName(function));
    }

    public void testGetDefaultModule() {
        Win32Verifier ver = new Win32Verifier();
        assertEquals("kernel32", ver.getDefaultModule());
    }

    public void testHandleString() {
        Win32Verifier ver = new Win32Verifier();

        String bla = "blorb";

        assertTrue(ver.handleString(bla, "kernel32", "Blub") instanceof byte[]);
        assertEquals(
            6,
            ((byte[]) ver.handleString(bla, "kernel32", "Blub")).length);
        assertTrue(
            ver.handleString(bla, "kernel32", "BlubA") instanceof byte[]);
        assertEquals(
            6,
            ((byte[]) ver.handleString(bla, "kernel32", "BlubA")).length);

        assertTrue(
            ver.handleString(bla, "kernel32", "BlubW") instanceof char[]);
        assertEquals(
            6,
            ((char[]) ver.handleString(bla, "kernel32", "BlubW")).length);
    }

}
