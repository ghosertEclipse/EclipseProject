/*
 *	PanelAudio.java
 */

/*
 * Copyright (c) 2004,2005 by Florian Bomers
 * Copyright (c) 1999 - 2002 by Matthias Pfisterer
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

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.sound.sampled.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import org.jsresources.utils.StripeLayout;
import org.jsresources.utils.HoriLine;
import org.jsresources.utils.audio.*;
import static org.jsresources.apps.radio.Constants.*; // $$ static import

public class PanelAudio extends JPanel implements ActionListener, ItemListener,
						  ChangeListener, PropertyChangeListener,
						  RadioModel.StatusListener {
    private JComboBox[] mixerSelector = new JComboBox[2];
    private JProgressBar[] volumeMeter = new JProgressBar[2];
    private JSlider[] volumeSlider = new JSlider[2];
    private JComboBox[] volumePort = new JComboBox[2];

    private JComboBox[] bufferSelector = new JComboBox[2];

    private MasterModel master;


    public PanelAudio(MasterModel masterModel) {
    	this.master = masterModel;
	setLayout(new GridLayout(1,2));

	createGUI(0, "Line In Radio");
	createGUI(1, "Speaker");

	getRadioModel().addPropertyChangeListener(this);
    }

    private void createGUI(int d, String title) {
	JPanel p = new JPanel();
	p.setLayout(new StripeLayout((d==0)?10:4, 2, (d==0)?4:10, 2, 5));
	p.add(new JLabel(title));
	p.add(new HoriLine());

	p.add(new JLabel("Mixer:"));
	mixerSelector[d] = new JComboBox(getAudioSettings().getMixers(d).toArray());
	mixerSelector[d].addItemListener(this);
	p.add(mixerSelector[d]);
	p.add(new HoriLine());

	p.add(new JLabel("Level:"));
	volumeMeter[d] = new JProgressBar(JProgressBar.HORIZONTAL, 0, 128);
	p.add(volumeMeter[d]);

	volumeSlider[d] = new JSlider(0, 100, 100);
	volumeSlider[d].addChangeListener(this);
	p.add(volumeSlider[d]);
	p.add(new HoriLine());

	volumePort[d] = new JComboBox(getAudioSettings().getPorts(d).toArray());
	volumePort[d].addItemListener(this);
	p.add(volumePort[d]);
	p.add(new HoriLine());

	p.add(new JLabel("Buffer size in millis:"));
	bufferSelector[d] = new JComboBox(BUFFER_SIZE_MILLIS_STR);
	bufferSelector[d].setSelectedIndex(getAudioSettings().getBufferSizeIndex(d));
	bufferSelector[d].addItemListener(this);
	p.add(bufferSelector[d]);

	add(p);
	// show current port
	initNewPort(d);
    }

    private RadioModel getRadioModel() {
    	return master.getRadioModel();
    }

    private AudioSettings getAudioSettings() {
    	return master.getAudioSettings();
    }

    private ConnectionSettings getConnectionSettings() {
    	return master.getConnectionSettings();
    }

    private AudioBase getAudio(int d) {
	return getRadioModel().getAudio(d);
    }

    private void initNewPort(int d) {
	int pIndex = getAudioSettings().getSelPortIndex(d);
	Port port = getAudioSettings().getSelPort(d);
	FloatControl c = getAudioSettings().getSelVolControl(d);
	volumeSlider[d].setEnabled(port != null && c != null);
	updateVolumeSlider(d);
	if (volumePort[d].getSelectedIndex() != pIndex) {
		volumePort[d].setSelectedIndex(pIndex);
	}
    }

    private void updateVolumeSlider(int d) {
	if (volumeSlider[d].isEnabled()) {
	    int newPos = getAudioSettings().getVolumeLevel(d);
	    if (newPos != volumeSlider[d].getValue()) {
		volumeSlider[d].setValue(newPos);
		//if (VERBOSE) out("Setting slider to: "+newPos);
	    }
	}
    }

    private void updateVolume(int d) {
	if (volumeSlider[d].isEnabled()) {
	    getAudioSettings().setVolumeLevel(d, volumeSlider[d].getValue());
	    //if (VERBOSE) out("Setting vol: "+newVol);
	}
    }

    private void initNewMixer(int d) {
	try {
	    getAudio(d).setMixer(getAudioSettings().getSelMixer(d));
	    if (d == DIR_MIC) {
	    	getRadioModel().initAudioOutputStream();
	    }
	} catch (Exception e) {
	    if (DEBUG) e.printStackTrace();
	    out(e.getMessage());
	}
    }

    private void initNewBufferSize(int d) {
	try {
	    getAudio(d).setBufferSizeMillis(getAudioSettings().getBufferSizeMillis(d));
	    if (d == DIR_MIC) {
	    	//TODO: getRadioModel().initAudioStream();
	    }
	} catch (Exception e) {
	    if (DEBUG) e.printStackTrace();
	    out(e.getMessage());
	}
    }

    public void itemStateChanged(ItemEvent e) {
	int d = -1;
	if (e.getSource() == volumePort[0]) {
	    d = 0;
	} else if (e.getSource() == volumePort[1]) {
	    d = 1;
	}
	if ((d >= 0) && (e.getStateChange() == ItemEvent.SELECTED)) {
	    getAudioSettings().setSelPort(d, volumePort[d].getSelectedIndex());
	    initNewPort(d);
	    return;
	}
	d = -1;
	if (e.getSource() == mixerSelector[0]) {
	    d = 0;
	} else if (e.getSource() == mixerSelector[1]) {
	    d = 1;
	}
	if ((d >= 0) && (e.getStateChange() == ItemEvent.SELECTED)) {
	    getAudioSettings().setSelMixer(d, mixerSelector[d].getSelectedIndex());
	    initNewMixer(d);
	    return;
	}
	d = -1;
	if (e.getSource() == bufferSelector[0]) {
	    d = 0;
	} else if (e.getSource() == bufferSelector[1]) {
	    d = 1;
	}
	if ((d >= 0) && (e.getStateChange() == ItemEvent.SELECTED)) {
	    getAudioSettings().setBufferSizeIndex(d, bufferSelector[d].getSelectedIndex());
	    initNewBufferSize(d);
	    return;
	}
    }

    public void stateChanged(ChangeEvent e) {
	int d;
	if (e.getSource() == volumeSlider[0]) {
	    d = 0;
	} else if (e.getSource() == volumeSlider[1]) {
	    d = 1;
	} else {
		if (e.getSource() instanceof JTabbedPane) {
			if (((JTabbedPane) e.getSource()).getSelectedComponent() == this) {
				// Selected
				getRadioModel().addStatusListener(this);
				initNewPort(0);
				initNewPort(1);
			} else {
				// Unselected
				getRadioModel().removeStatusListener(this);
			}
		}
	    return;
	}
	updateVolume(d);
    }

    public void actionPerformed(ActionEvent e) {
    }

    public void propertyChange(PropertyChangeEvent e) {
	boolean newValue = ((Boolean) e.getNewValue()).booleanValue();
	if (DEBUG) out("Property change '"+e.getPropertyName()+"="+e.getNewValue()+"'. "
		       +" AudioActive:"+getRadioModel().isAudioActive()
		       +" Started:"+getRadioModel().isStarted()
		       +" Connected:"+getRadioModel().isConnected());

    }

    public void displayStatus(int inLevel, int outLevel) {
	    volumeMeter[DIR_MIC].setValue(inLevel);
	    volumeMeter[DIR_SPK].setValue(outLevel);
    }


}

/*** PanelAudio.java ***/
