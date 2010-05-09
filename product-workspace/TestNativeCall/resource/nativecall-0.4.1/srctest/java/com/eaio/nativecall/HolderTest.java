/* 
 * HolderTest.java
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
 * Test case for the {@link com.eaio.nativecall.Holder} class.
 * 
 * @author <a href="mailto:jb@eaio.com">Johann Burkard</a>
 * @version $Id: HolderTest.java,v 1.2 2006/04/19 20:54:58 grnull Exp $
 */
public class HolderTest extends TestCase {

    /**
     * Constructor for HolderTest.
     * @param arg0
     */
    public HolderTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.awtui.TestRunner.run(HolderTest.class);
    }

    public void testHolder() {
        try {
            new Holder(new Holder(null));
            fail("Did not throw ClassCastException");
        }
        catch (ClassCastException ex) {}
    }

    public void testHashCode() {
        Holder h1 = new Holder(new Integer(10));
        assertEquals(-7460404, h1.hashCode());
        Holder h2 = new Holder(new Integer(10));
        assertEquals(h1.hashCode(), h2.hashCode());

        HashSet set = new HashSet();
        set.add(h1);
        set.add(h2);
        assertEquals(1, set.size());
    }

    public void testGet() {
        Integer i = new Integer(20);
        Holder h = new Holder(i);
        assertTrue(i == h.get());
    }

    /*
     * Test for boolean equals(Object)
     */
    public void testEqualsObject() {
        Integer i = new Integer(42);
        Holder h1 = new Holder(i);
        Holder h2 = new Holder(i);
        assertEquals(h1, h2);
        assertFalse(h1.equals(null));
        assertEquals(h1, h1);
        assertFalse(h1.equals(new Holder(new Integer(0))));
    }

}
