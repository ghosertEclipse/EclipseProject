/*
 *	Radio.java
 */

/*
 * Copyright (c) 1999 - 2001 by Matthias Pfisterer
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

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package org.jsresources.apps.radio;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;



public class Radio
{
	boolean	m_bPackFrame = false;

	public Radio()
	{
	        JFrame.setDefaultLookAndFeelDecorated(true);
        	Toolkit.getDefaultToolkit().setDynamicLayout(true);
        	System.setProperty("sun.awt.noerasebackground","true");
		JFrame frame = new JFrame();
		frame.setTitle("Radio");
		frame.setSize(new Dimension(600, 500));
		WindowAdapter	windowAdapter = new WindowAdapter()
			{
				public void windowClosing(WindowEvent we)
				{
					System.exit(0);
				}
			};
		frame.addWindowListener(windowAdapter);

		JComponent tabbedPane = new RadioPane();
		frame.getContentPane().add(tabbedPane);

		//Validate frames that have preset sizes
		//Pack frames that have useful preferred size info, e.g. from their layout
		if (m_bPackFrame)
		{
			frame.pack();
		}
		else
		{
			frame.validate();
		}

		//Center the window
		Dimension	screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension	frameSize = frame.getSize();
		if (frameSize.height > screenSize.height)
		{
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width)
		{
			frameSize.width = screenSize.width;
		}
		frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		frame.setVisible(true);
	}



	public static void main(String[] args)
	{
		try {
			new Radio();
		} catch (Throwable t) {
			System.err.println("Exception occurred in main():");
			t.printStackTrace();
			System.exit(1);
		}
	}
}


/*** Radio.java ***/
