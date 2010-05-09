/* 
 * NativeCall.java
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

import java.io.IOException;

import sun.misc.ServiceConfigurationError;

import com.eaio.util.lang.NativeLoader;

/**
 * NativeCall loads the native library and prepares the matching
 * {@link com.eaio.nativecall.Verifier}.
 * <p>
 * Before being able to use NativeCall, the {@link #init()} method must have
 * been called:
 * <pre>
 * try {
 *     NativeCall.init();
 * }
 * catch (IOException ex) { ... }
 * catch (SecurityException ex) { ... }
 * catch (UnsatisfiedLinkError er) { ... }
 * catch (sun.misc.ServiceConfigurationError) { ... }
 * catch (UnsupportedOperationException) { ... }
 * </pre>
 * After usage, each NativeCall object must be destroyed to release 
 * resources. This is done by calling the {@link #destroy()} method. Failure
 * to call this method might result in memory leaks.
 * 
 * @see #destroy()
 * @see #init()
 * @author <a href="mailto:jb@eaio.com">Johann Burkard</a>
 * @version $Id: NativeCall.java,v 1.3 2006/04/19 20:54:58 grnull Exp $
 */
public abstract class NativeCall {

    /**
     * The error code of the last call.
     * <p>
     * Accessed by native code. DO NOT RENAME THIS FIELD.
     */
    private int lastErrorCode;

    /**
     * The internal handle to the function and the module.
     * <p>
     * These are set in native code, so ignore any warnings.
     * <p>
     * Accessed by native code. DO NOT RENAME THIS FIELD.
     */
    private int functionHandle, moduleHandle;

    /**
     * The name of the function to call.
     * <p>
     * Accessed by native code. DO NOT RENAME THIS FIELD.
     */
    private String function;

    /**
     * The name of the module to call.
     * <p>
     * Accessed by native code. DO NOT RENAME THIS FIELD.
     */
    private String module;

    /**
     * Initialize JNI field and method IDs
     */
    private static native void initIDs();

    /**
     * Whether the class has been initialized properly.
     */
    private static boolean initialized = false;

    /**
     * Before NativeCall may be used, this method must be called.
     * It loads the native library, prepares JNI field and method IDs and loads
     * the matching {@link Verifier}.
     * <p>
     * Multiple calls are ignored.
     * 
     * @throws IOException if an IOException occured during unpacking of
     * the native library
     * @throws SecurityException if accessing system properties was forbidden
     * by the {@link SecurityManager}
     * @throws UnsatisfiedLinkError if the <code>NativeCall.dll</code> could
     * not be found
     * @throws sun.misc.ServiceConfigurationError
     * @throws UnsupportedOperationException if no matching
     * {@link Verifier} could be found
     */
    public static synchronized void init()
        throws
            IOException,
            SecurityException,
            UnsatisfiedLinkError,
            ServiceConfigurationError,
            UnsupportedOperationException {
        if (!initialized) {
            Verifiers.init();
            if (Verifiers.getInstance() == null) {
                throw new UnsupportedOperationException();
            }
            new NativeLoader("NativeCall").load();
            initIDs();
            initialized = true;
        }
    }

    /**
     * Constructor for NativeCall.
     * 
     * @param function the name of the function to use, may not be
     * <code>null</code>
     * @throws IllegalArgumentException if the function could not be found
     * @throws NullPointerException if function is <code>null</code>
     * @see Verifier#getDefaultModule()
     * @see NativeCall#NativeCall(String, String)
     */
    public NativeCall(String function)
        throws IllegalArgumentException, NullPointerException {
        this(Verifiers.getInstance().getDefaultModule(), function);
    }

    /**
     * Constructor for NativeCall.
     * 
     * @param module the name of the module the function is stored in, may be
     * <code>null</code>
     * @param function the name of the function to use, may not be
     * <code>null</code>
     * @throws IllegalArgumentException if the function could not be found
     * @throws NullPointerException if function is <code>null</code>
     */
    public NativeCall(String module, String function)
        throws IllegalArgumentException, NullPointerException {
        Verifier v = Verifiers.getInstance();
        this.function = v.verifyFunctionName(function);
        this.module = v.verifyModuleName(module);
        if (!initHandles()) {
            if (lastErrorCode != 0) {
                throw new IllegalArgumentException(getLastError());
            }
            throw new IllegalArgumentException();
        }
    }

    /**
     * Attempts to acquire handles to the functions. Returns if these could be
     * acquired.
     * 
     * @return if the handles could be acquired
     */
    private native boolean initHandles();

    /**
     * Returns the error code that was returned during the last method call or
     * 0 if the last method call did not produce an error.
     * 
     * @see #getLastError()
     * @return the last error code or 0
     */
    public final int getLastErrorCode() {
        return lastErrorCode;
    }

    /**
     * Returns a formatted String containing the last error code or
     * <code>null</code> if the last call did not produce an error.
     * 
     * @see #getLastErrorCode()
     * @return a String or <code>null</code> if the last error code is 0
     */
    public final native String getLastError();

    /**
     * Releases acquired module handles. This method must be called if the
     * instance is not used anymore. After this method is called, methods of this
     * NativeCall Object cannot be called anymore.
     * <p>
     * <strong>Failure to call this method might result in memory leaks.</strong>
     * <p>
     * <em>Updates the error code field. See {@link #getLastError()}.</em>
     */
    public native synchronized void destroy();

    /**
     * Checks the supplied Object array for illegal/unsupported types.
     * <p>
     * <strong>During the verification, the contents of the array might be
     * changed.</strong>
     * 
     * @param params the Object array, may be <code>null</code>
     * @throws ClassCastException if the type of one argument is not supported
     */
    protected void check(Object[] params) throws ClassCastException {
        if (params == null) {
            return;
        }
        for (int i = 0; i < params.length; ++i) {
            checkParam(params[i]);
            if (params[i] instanceof String) {
                params[i] =
                    Verifiers.getInstance().handleString(
                        (String) params[i],
                        module,
                        function);
            }
        }
    }

    /**
     * Checks one Object for illegal/unsupported types.
     * 
     * @param o the Object, may be <code>null</code>
     * @throws ClassCastException if the type of one argument is not supported
     */
    protected void checkParam(Object o) throws ClassCastException {
        if (o == null
            || o instanceof Boolean
            || o instanceof Integer
            || o instanceof byte[]
            || o instanceof char[]
            || o instanceof String) {
            return;
        }
        if (o instanceof Holder) {
            checkParam(((Holder) o).get());
            return;
        }
        throw new ClassCastException(o.getClass().getName());
    }

    /**
     * Returns if this Object is equal to another Object. The other Object must
     * be an instance of the same type as this Object. Also, both the module
     * and the function field must be equal.
     * 
     * @param obj the other Object
     * @return if this and the other Object are equal
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NativeCall)) {
            return false;
        }
        if (!getClass().getName().equals(obj.getClass().getName())) {
            return false;
        }
        NativeCall c = (NativeCall) obj;
        return module.equals(c.module) && function.equals(c.function);
    }

    /**
     * Returns the hashCode of this Object. The hashCode is computed by XOR'ing
     * the hash codes of the function and the module names.
     * 
     * @return the hashCode
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        int out = function.hashCode();
        out ^= module.hashCode();
        return out;
    }

    /**
     * Calls {@link #destroy()}.
     * 
     * @see java.lang.Object#finalize()
     */
    protected void finalize() throws Throwable {
        try {
            destroy();
        }
        finally {
            super.finalize();
            // in case NativeCall is a subclass of a class other than Object
        }
    }

    /**
     * Returns a String representation of this Object.
     * 
     * @return a String, never <code>null</code>
     * @see java.lang.Object#toString()
     * @see #toStringBuffer(StringBuffer)
     */
    public final String toString() {
        return toStringBuffer(null).toString();
    }

    /**
     * Appends a String representation of this Object to the given
     * {@link StringBuffer} or creates a new one if none is given.
     * 
     * @param in the StringBuffer to append to, may be <code>null</code>
     * @return a StringBuffer, never <code>null</code>
     */
    public StringBuffer toStringBuffer(StringBuffer in) {
        if (in == null) {
            in = new StringBuffer(64);
        }
        else {
            in.ensureCapacity(in.length() + 64);
        }
        in.append("{ ");
        int idx = getClass().getName().lastIndexOf(".");
        if (idx > -1) {
            in.append(getClass().getName().substring(++idx));
        }
        else {
            in.append(getClass().getName());
        }
        in.append(": module = ");
        in.append(module);
        in.append(", function = ");
        in.append(function);
        in.append(" }");
        return in;
    }

}
