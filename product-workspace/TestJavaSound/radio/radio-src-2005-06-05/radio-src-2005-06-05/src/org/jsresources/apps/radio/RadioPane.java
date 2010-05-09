/*
 *	RadioPane.java
 */

/*
 * Copyright (c) 1999 - 2004 by Matthias Pfisterer
 * Copyright (c) 2005 by Florian Bomers
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

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;



public class RadioPane
extends JPanel
{
	private JLabel		m_statusLabel;


	public RadioPane()
	{
		super();

		MasterModel masterModel = new MasterModel();
		setLayout(new BorderLayout());
		JComponent tabbed = createTabbedPane(masterModel);
		this.add(tabbed, BorderLayout.CENTER);
		//m_statusLabel = new JLabel();
		//this.add(m_statusLabel, BorderLayout.SOUTH);

		//setStatusLine(" ");
	}


	private JComponent createTabbedPane(MasterModel masterModel)
	{
		JTabbedPane tabbedPane = new JTabbedPane();
		JComponent	component;
		JComponent main = new PanelMain(masterModel);
		tabbedPane.add(main, "Radio");
		tabbedPane.addChangeListener((ChangeListener) main);

		component = new PanelSettings(masterModel);
		tabbedPane.add(component, "Settings");

		component = new PanelAudio(masterModel);
		tabbedPane.addChangeListener((ChangeListener) component);
		tabbedPane.add(component, "Audio");

		component = new PanelInfo(masterModel);
		tabbedPane.add(component, "Info");

		//tabbedPane.fireStateChange();
		((ChangeListener) main).stateChanged(new ChangeEvent(tabbedPane));

		return tabbedPane;
	}


	public void setStatusLine(String strMessage)
	{
		//m_statusLabel.setText(strMessage);
	}
}



/*** RadioPane.java ***/
