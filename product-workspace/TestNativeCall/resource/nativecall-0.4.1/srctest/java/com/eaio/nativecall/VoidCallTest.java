/* 
 * VoidCallTest.java
 * 
 * Created on 05.01.2006.
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

import java.util.HashSet;

import junit.framework.TestCase;

/**
 * Test case for the {@link com.eaio.nativecall.VoidCall} class.
 * 
 * @author <a href="mailto:jb@eaio.com">Johann Burkard</a>
 * @version $Id: VoidCallTest.java,v 1.2 2006/04/12 22:11:41 grnull Exp $
 */
public class VoidCallTest extends TestCase {

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
     * Constructor for VoidCallTest.
     * @param arg0
     */
    public VoidCallTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.awtui.TestRunner.run(VoidCallTest.class);
    }

    /*
     * Test for void executeCall()
     */
    public void testExecuteCall() {}

    /*
     * Test for void executeCall(Object)
     */
    public void testExecuteCallObject() {
        /* This is of course not really a void call. */
        VoidCall cT = new VoidCall("GetCurrentThread");
        try {
            /* to trigger an Exception */
            cT.executeCall(new StringBuffer());
            fail("Did not throw ClassCastException");
        }
        catch (ClassCastException ex) {}
    }

    /*
     * Test for void executeCall(Object[])
     */
    public void testExecuteCallObjectArray() {
        /* This is of course not really a void call. */
        VoidCall cT = new VoidCall("GetCurrentThread");
        try {
            /* to trigger an Exception */
            cT.executeCall(new Object[] { new StringBuffer() });
            fail("Did not throw ClassCastException");
        }
        catch (ClassCastException ex) {}

        VoidCall cT2 = new VoidCall("GetCurrentThread");
        assertEquals(cT, cT2);
        assertEquals(cT, cT);
        assertFalse(cT.equals(null));
        assertTrue(cT.hashCode() == cT2.hashCode());

        HashSet set = new HashSet();
        set.add(cT);
        set.add(cT2);
        assertEquals(1, set.size());

    }

}
