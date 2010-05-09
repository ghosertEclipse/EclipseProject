/* 
 * AllTests.java
 * 
 * Created on 18.11.2005.
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

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Runs all tests.
 * 
 * @author <a href="mailto:jb@eaio.com">Johann Burkard</a>
 * @version $Id: AllTests.java,v 1.2 2006/01/05 19:57:07 grnull Exp $
 */
public class AllTests {

    public static void main(String[] args) {
        junit.awtui.TestRunner.run(AllTests.class);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for com.eaio.nativecall");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(FileManagementTest1Win32.class));
        suite.addTest(new TestSuite(HolderTest.class));
        suite.addTest(new TestSuite(IntCallTest1Win32.class));
        suite.addTest(new TestSuite(Win32VerifierTest.class));
        suite.addTest(new TestSuite(VerifiersTest.class));
        suite.addTest(new TestSuite(VoidCallTest.class));
        //$JUnit-END$
        return suite;
    }
}
