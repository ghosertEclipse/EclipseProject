/*
 *	PanelSettings.java
 */

/*
 * Copyright (c) 2005 by Florian Bomers
 * Copyright (c) 1999 - 2004 by Matthias Pfisterer
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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.jsresources.utils.StripeLayout;
import org.jsresources.utils.HoriLine;
import static org.jsresources.apps.radio.Constants.*;

public class PanelSettings extends JPanel implements PropertyChangeListener, ItemListener {

    private MasterModel m_masterModel;
	private JComboBox m_qualityComboBox;
	private JComboBox m_circbufComboBox;

	public PanelSettings(MasterModel masterModel) {
	    m_masterModel = masterModel;
		setLayout(new StripeLayout(10, 2, 10, 2, 5));

		add(new JLabel("Preferred Recording Audio Format:"));
		m_qualityComboBox = new JComboBox(FORMAT_NAMES);
		m_qualityComboBox.addItemListener(this);
		add(m_qualityComboBox);

		add(new HoriLine());
		add(new JLabel("Size of buffer:"));
		m_circbufComboBox = new JComboBox(CIRCBUF_NAMES);
		m_circbufComboBox.addItemListener(this);
		add(m_circbufComboBox);

		init();
		getRadioModel().addPropertyChangeListener(this);
	}


    private MasterModel getMasterModel() {
	    return m_masterModel;
	}

    private RadioModel getRadioModel() {
    	return m_masterModel.getRadioModel();
    }

    private ConnectionSettings getConnectionSettings() {
	    return getMasterModel().getConnectionSettings();
	}

    private AudioSettings getAudioSettings() {
	    return getMasterModel().getAudioSettings();
	}

    public void itemStateChanged(ItemEvent e) {
	if (e.getStateChange() == ItemEvent.SELECTED) {
		if (e.getSource() == m_qualityComboBox) {
			int nFormatCode = FORMAT_CODES[m_qualityComboBox.getSelectedIndex()];
			getAudioSettings().setPreferredAudioFormatCode(nFormatCode);
		} else
		if (e.getSource() == m_circbufComboBox) {
			int millis = CIRCBUF_MILLIS[m_circbufComboBox.getSelectedIndex()];
			getAudioSettings().setCircBufMillis(millis);
		}
	}
    }

    private void init() {
	    int nFormatCode = FORMAT_CODE_DEFAULT;
	    int nIndex = -1;
	    for (int i = 0; i < FORMAT_CODES.length; i++)
	    {
		if (nFormatCode == FORMAT_CODES[i])
		{
		    nIndex = i;
		    break;
		}
	    }
	    m_qualityComboBox.setSelectedIndex(nIndex);
	    m_circbufComboBox.setSelectedIndex(CIRCBUF_INDEX_DEFAULT);
	}

    public void propertyChange(PropertyChangeEvent e) {
	    boolean newValue = ((Boolean) e.getNewValue()).booleanValue();
	    if (e.getPropertyName().equals(STARTED_PROPERTY)) {
	    	m_qualityComboBox.setEnabled(!newValue);
	    	m_circbufComboBox.setEnabled(!newValue);
	    }
	}

}



/*** PanelSettings.java ***/
