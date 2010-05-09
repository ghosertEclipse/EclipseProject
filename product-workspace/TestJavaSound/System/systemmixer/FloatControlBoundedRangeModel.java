package systemmixer;
/*
 *	FloatControlBoundedRangeModel.java
 */

/*
 * Copyright (c) 2003 by Matthias Pfisterer
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import javax.swing.DefaultBoundedRangeModel;

import javax.sound.sampled.FloatControl;


/**	TODO:
 */
public class FloatControlBoundedRangeModel
    extends DefaultBoundedRangeModel
{
    private static final boolean DEBUG = true;


    private FloatControl m_control;
    private float m_fStepsPerUnit;


    /**	TODO:
     */
    public FloatControlBoundedRangeModel(FloatControl control)
    {
		m_control = control;

		float range = control.getMaximum() - control.getMinimum();
		float steps = range / control.getPrecision();
		m_fStepsPerUnit = steps / range;
		if (DEBUG) { out("control: " + control); }
		if (DEBUG) { out("steps: " + steps); }
		if (DEBUG) { out("steps per unit: " + m_fStepsPerUnit); }
		int minimum = (int) (control.getMinimum() * m_fStepsPerUnit);
		int maximum = (int) (control.getMaximum() * m_fStepsPerUnit);
		int value = (int) (control.getValue() * m_fStepsPerUnit);
		setRangeProperties(value, 0, minimum, maximum, false);
	}


    public void setValue(int nValue)
    {
		super.setValue(nValue);
		// out("setValue: " + nValue);
		float fValue = (float) nValue / getScaleFactor();
		// out("setValue (float): " + fValue);
		m_control.setValue(fValue);
    }


    public int getValue()
    {
		return (int) (m_control.getValue() * getScaleFactor());
    }


    private float getScaleFactor()
    {
		return m_fStepsPerUnit;
    }

    private static void out(String message)
    {
		System.out.println(message);
    }
}



/*** FloatControlBoundedRangeModel.java ***/
