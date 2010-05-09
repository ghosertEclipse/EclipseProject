/* 
 * Holder.java
 * 
 * Created on 14.09.2004.
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
 * Holder is a class that encapsulates another Object. Use this class for output
 * parameters.
 * 
 * @author <a href="mailto:jb@eaio.com">Johann Burkard</a>
 * @version $Id: Holder.java,v 1.3 2006/04/19 20:54:58 grnull Exp $
 */
public class Holder {

    /**
     * The encapsulated object.
     * <p>
     * Accessed by native code. DO NOT RENAME THIS FIELD.
     */
    private Object o;

    /**
     * Constructor for Holder.
     * 
     * @param o the Object to encapsulate, may be <code>null</code>, but cannot
     * be of type Holder
     * @throws ClassCastException if o is of type Holder
     */
    public Holder(Object o) {
        if (o instanceof Holder) {
            throw new ClassCastException();
        }
        else if (o == null) {
            o = new Integer(0);
        }
        this.o = o;
    }

    /**
     * Returns the referenced Object.
     * 
     * @return an Object
     */
    public final Object get() {
        return o;
    }

    /**
     * Returns the hashCode of the encapsulated Object or
     * {@link java.lang.Object#hashCode()} if the Object is <code>null</code>.
     * 
     * @return the hashCode
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return getClass().getName().hashCode() ^ (o == null ? 0 : o.hashCode());
    }

    /**
     * Returns if this Object is equal to another Object.
     * 
     * @param obj the other Object, may be <code>null</code>
     * @return if both Objects are equal
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Holder)) {
            return false;
        }
        Holder h = (Holder) obj;
        return o == null ? h.o == null : o.equals(h.o);
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
            in = new StringBuffer(32);
        }
        else {
            in.ensureCapacity(in.length() + 32);
        }
        in.append("{ Holder: o = ");
        in.append(o);
        in.append(" }");
        return in;
    }

}
