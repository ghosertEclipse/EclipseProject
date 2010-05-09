/* 
 * Win32Verifier.java
 * 
 * Created on 08.09.2004.
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

/**
 * A {@link com.eaio.nativecall.Verifier} for the Windows environment.
 * 
 * @author <a href="mailto:jb@eaio.com">Johann Burkard</a>
 * @version $Id: Win32Verifier.java,v 1.3 2006/04/19 20:54:58 grnull Exp $
 */
public class Win32Verifier implements Verifier {

    /**
     * Constructor for Win32Verifier. Does nothing.
     */
    public Win32Verifier() {}

    /**
     * Verifies that the {@link java.lang.System} property "os.name" starts
     * with "Windows".
     * 
     * @see Verifier#supports()
     */
    public boolean supports() throws SecurityException {
        return System.getProperty("os.name").startsWith("Windows");
    }

    /**
     * Returns the default module name if the module name is <code>null</code>
     * or an empty String. If the module name contains forward slashes (/), they
     * are converted to backward slashes (\).
     * 
     * @see <a
     * href="http://msdn.microsoft.com/library/default.asp?url=/library/en-us/dllproc/base/loadlibrary.asp"
     * target="_top">
     * http://msdn.microsoft.com/library/default.asp?url=/library/en-us/dllproc/base/loadlibrary.asp
     * </a> 
     * @see com.eaio.nativecall.Verifier#verifyModuleName(java.lang.String)
     */
    public String verifyModuleName(String module) {
        if (module == null || module.length() == 0) {
            return getDefaultModule();
        }
        if (module.indexOf('/') != -1) {
            module = module.replace('/', '\\');
        }
        return module;
    }

    /**
     * Throws a {@link java.lang.NullPointerException} if the function
     * name is <code>null</code> or an empty String. No further processing is
     * done.
     * 
     * @see com.eaio.nativecall.Verifier#verifyFunctionName(java.lang.String)
     */
    public String verifyFunctionName(String function) {
        if (function == null || function.length() == 0) {
            throw new NullPointerException();
        }
        return function;
    }

    /**
     * Returns "kernel32".
     * 
     * @return "kernel32" 
     * @see com.eaio.nativecall.Verifier#getDefaultModule()
     */
    public String getDefaultModule() {
        return "kernel32";
    }

    /**
     * If the function name ends on 'W' (Windows' Unicode functions), a
     * <code>char</code> array is returned, otherwise a <code>byte</code> array
     * is returned.
     * <p>
     * The arrays are always <code>null</code>-terminated.
     * 
     * @see com.eaio.nativecall.Verifier#handleString(java.lang.String,
     * java.lang.String,
     * java.lang.String)
     */
    public Object handleString(String val, String module, String function) {
        if (function.charAt(function.length() - 1) == 'W') {
            char[] buf = new char[val.length() + 1];
            val.getChars(0, val.length(), buf, 0);
            return buf;
        }
        byte[] buf = new byte[val.length() + 1];
        val.getBytes(0, val.length(), buf, 0);
        return buf;
    }

}
