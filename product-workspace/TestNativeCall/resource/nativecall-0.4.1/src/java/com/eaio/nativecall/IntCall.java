/* 
 * IntCall.java
 * 
 * Created on 07.09.2004.
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
 * An IntCall instance encapsulates an operating system method that returns
 * an integer. 
 * 
 * @author <a href="mailto:jb@eaio.com">Johann Burkard</a>
 * @version $Id: IntCall.java,v 1.1 2006/01/05 20:02:44 grnull Exp $
 */
public class IntCall extends NativeCall {

    /**
     * Constructor for IntCall.
     * 
     * @see NativeCall#NativeCall(String)
     */
    public IntCall(String function) throws SecurityException,
            IllegalArgumentException, NullPointerException {
        super(function);
    }

    /**
     * Constructor for IntCall.
     * 
     * @see NativeCall#NativeCall(String, String)
     */
    public IntCall(String module, String function) throws SecurityException,
            IllegalArgumentException, NullPointerException {
        super(module, function);
    }

    /**
     * Returns <code>false</code> if calling {@link #executeCall()} returned
     * 0, <code>true</code> otherwise. 
     * <p>
     * <em>Updates the error code field. See {@link #getLastError()}.</em>
     * 
     * @return <code>true</code> or <code>false</code>
     */
    public boolean executeBooleanCall() {
        return executeCall() == 0 ? false : true;
    }

    /**
     * Returns <code>false</code> if calling {@link #executeCall(Object)}
     * returned 0, <code>true</code> otherwise. 
     * <p>
     * <em>Updates the error code field. See {@link #getLastError()}.</em>
     * 
     * @param param the parameter, may be <code>null</code>
     * @return <code>true</code> or <code>false</code>
     * @see #executeBooleanCall(Object[])
     */
    public boolean executeBooleanCall(Object param) {
        return executeCall(param) == 0 ? false : true;
    }

    /**
     * Returns <code>false</code> if calling
     * {@link #executeCall(Object[])} returned 0, <code>true</code> otherwise. 
     * <p>
     * <em>Updates the error code field. See {@link #getLastError()}.</em>
     * <p>
     * During this operation, the contents of the array might be changed.
     * 
     * @param params the parameter array, may be <code>null</code>
     * @return <code>true</code> or <code>false</code>
     */
    public boolean executeBooleanCall(Object[] params) {
        return executeCall(params) == 0 ? false : true;
    }

    /**
     * Calls the function, returning its output.
     * <p>
     * <em>Updates the error code field. See {@link #getLastError()}.</em>
     * 
     * @return an <code>int</code>
     */
    public native int executeCall();

    /**
     * Calls the function using the given parameter.
     * <p>
     * <em>Updates the error code field. See {@link #getLastError()}.</em>
     * 
     * @param param the parameter, may be <code>null</code>
     * @return an <code>int</code>
     * @see #executeCall(Object[])
     */
    public int executeCall(Object param) {
        return executeCall(new Object[] { param });
    }

    /**
     * Calls the function using the given parameters.
     * <p>
     * <em>Updates the error code field. See {@link #getLastError()}.</em>
     * <p>
     * During this operation, the contents of the array might be changed.
     * 
     * @param params the parameter array, may be <code>null</code>
     * @return an <code>int</code>
     */
    public int executeCall(Object[] params) {
        if (params == null || params.length == 0) {
            return executeCall();
        }
        check(params);
        return executeCall0(params);
    }

    private native int executeCall0(Object[] params);

}
