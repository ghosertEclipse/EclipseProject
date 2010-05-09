/* 
 * SimpleTest.java
 * 
 * Created on 01.04.2006.
 *
 * Copyright (c) 2005, 2006 Johann Burkard (<mailto:jb@eaio.com>)
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
import com.eaio.nativecall.Holder;
import com.eaio.nativecall.IntCall;
import com.eaio.nativecall.NativeCall;
import com.eaio.nativecall.VoidCall;

/**
 * 
 * @author <a href="mailto:jb@eaio.com">Johann Burkard</a>
 * @version $Id$
 */
public class SimpleTest {

    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            System.out.println(
                "Usage: java "
                    + SimpleTest.class.getName()
                    + " [path to nctest.dll]");
            System.exit(1);
        }

        NativeCall.init();

        IntCall ic = new IntCall(args[0], "_test@4");
        int x = ic.executeCall(new Integer(42));
        System.out.println("x = " + x);
        ic.destroy();

        VoidCall myFunc = new VoidCall(args[0], "_myFunc@12");
        Holder output1Holder = new Holder(new Integer(0));
        Holder output2Holder = new Holder(new Integer(0));
        myFunc.executeCall(
            new Object[] { new Integer(2), output1Holder, output2Holder });
        System.out.println(output1Holder.get());
        System.out.println(output2Holder.get());
        myFunc.destroy();

    }

}
