/*
 *	PanelMain.java
 */

/*
 * Copyright (c) 1999 - 2002 by Matthias Pfisterer
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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import javax.sound.sampled.*;

import org.jsresources.utils.*;

import static org.jsresources.apps.radio.Constants.*;

public class PanelMain
extends JPanel
    implements ActionListener, PropertyChangeListener, ChangeListener, RadioModel.StatusListener
{

	private static final int WIND_MILLIS = 5000;

    private MasterModel m_masterModel;
	private JTextField m_filenameTextField;
	private JButton m_startButton;
	private JButton m_rewindButton;
	private JButton m_forwardButton;
	private JButton m_recButton;

	private JProgressBar m_volumeMeter;
	private JSlider m_volumeSlider;

	private JLabel m_playback;
	private JLabel m_realtime;
	private JLabel m_recording;

	private JProgressBar m_playProgress;
	private JProgressBar m_recorderProgress;
	private JProgressBar m_realtimeProgress;

	private JList m_urlList;

	public PanelMain(MasterModel masterModel)
	{

		//org.tritonus.share.TDebug.TraceAudioConverter=true;


	    m_masterModel = masterModel;

		setLayout(new StripeLayout(10, 2, 10, 2, 5));

		JPanel		panel = null;

		panel = new JPanel();
		m_startButton = new JButton("Start");
		m_startButton.addActionListener(this);
		m_startButton.setActionCommand("start");
		panel.add(m_startButton);

		m_rewindButton = new JButton("Rewind");
		m_rewindButton.addActionListener(this);
		m_rewindButton.setActionCommand("rewind");
		m_rewindButton.setEnabled(false);
		panel.add(m_rewindButton);

		m_forwardButton = new JButton("Forward");
		m_forwardButton.addActionListener(this);
		m_forwardButton.setActionCommand("forward");
		m_forwardButton.setEnabled(false);
		panel.add(m_forwardButton);

		m_recButton = new JButton("Record");
		m_recButton.addActionListener(this);
		m_recButton.setActionCommand("record");
		m_recButton.setEnabled(false);
		panel.add(m_recButton);

		add(panel);
		add(new HoriLine());

		panel = new JPanel();
		panel.add(new JLabel("Filename:"));
		m_filenameTextField = new JTextField(20);
		m_filenameTextField.setText("recording.wav");
		panel.add(m_filenameTextField);
		add(panel);
		add(new HoriLine());

		add(new JLabel("Level:"));
		m_volumeMeter = new JProgressBar(JProgressBar.HORIZONTAL, 0, 128);
		add(m_volumeMeter);

		m_volumeSlider = new JSlider(0, 100, 100);
		m_volumeSlider.addChangeListener(this);
		add(m_volumeSlider);

		add(new HoriLine());
		add(new JLabel("Status:"));

		panel = new JPanel();
		panel.setLayout(new BorderLayout());

		Box box = Box.createHorizontalBox();
		//JPanel box = new JPanel();
		//box.setLayout(new BorderLayout());
		JPanel p2 = new JPanel();
		p2.setLayout(new StripeLayout());
		p2.add(new JLabel("Playback: "));
		p2.add(new JLabel("Realtime: "));
		p2.add(new JLabel("Recording: "));
		//box.add(p2, BorderLayout.WEST);
		box.add(p2);
		p2 = new JPanel();
		p2.setLayout(new StripeLayout());
		m_playback = new JLabel(Utils.formatMinSecTenths(0));
		p2.add(m_playback);
		m_realtime = new JLabel(Utils.formatMinSecTenths(0));
		p2.add(m_realtime);
		m_recording = new JLabel(Utils.formatMinSecTenths(0));
		p2.add(m_recording);
		//box.add(p2, BorderLayout.EAST);
		box.add(p2);
		//box.add(Box.createHorizontalGlue());
		panel.add(box, BorderLayout.WEST);
		p2 = new JPanel();
		p2.setLayout(new StripeLayout(4, 2, 0, 2, 2));
		m_playProgress = new JProgressBar(JProgressBar.HORIZONTAL, 0, 1000);
		p2.add(m_playProgress);
		m_realtimeProgress = new JProgressBar(JProgressBar.HORIZONTAL, 0, 1000);
		p2.add(m_realtimeProgress);
		m_recorderProgress = new JProgressBar(JProgressBar.HORIZONTAL, 0, 1000);
		p2.add(m_recorderProgress);
		panel.add(p2, BorderLayout.CENTER);
		add(panel);

		add(new HoriLine());

		m_urlList = new JList(SOURCE_LIST);
		m_urlList.setSelectedIndex(0);
		add(m_urlList);

		getRadioModel().addPropertyChangeListener(this);
		getRadioModel().addStatusListener(this);
	}


    private MasterModel getMasterModel()
	{
	    return m_masterModel;
	}


    private RadioModel getRadioModel()
	{
	    return getMasterModel().getRadioModel();
	}

    private AudioSettings getAudioSettings() {
    	return getMasterModel().getAudioSettings();
    }

    public void setAudioLevel(int level) {
	m_volumeMeter.setValue(level);
    }

	public void actionPerformed(ActionEvent ae)
	{
		String	strActionCommand = ae.getActionCommand();
		if (strActionCommand.equals("record"))
		{
			if (getRadioModel().isRecording()) {
				getRadioModel().stopRecording();
			} else {
				String strFilename = m_filenameTextField.getText();
				try {
					getRadioModel().startRecording(strFilename);
				} catch (Exception e) {
					Debug.out(e);
					JOptionPane.showMessageDialog(null, new Object[]{"Error: ", e.getMessage()}, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		} else
		if (strActionCommand.equals("start")) {
			if (getRadioModel().isStarted()) {
				getRadioModel().stop();
			} else {
				int index = m_urlList.getSelectedIndex();
				if (index == 0) {
					// use LINE IN as input
					getRadioModel().startCapture();
				} else {
					Debug.out("Capture from shoutcast not implemented!");
				}
			}
		} else
		if (strActionCommand.equals("forward")) {
			getRadioModel().wind(DIR_SPK, WIND_MILLIS);
		} else
		if (strActionCommand.equals("rewind")) {
			getRadioModel().wind(DIR_SPK, -WIND_MILLIS);
		}

	}

	private void updateLevelSlider() {
		FloatControl c = getAudioSettings().getSelVolControl(DIR_SPK);
		m_volumeSlider.setEnabled(c != null);
		if (c != null) {
	    int newPos = getAudioSettings().getVolumeLevel(DIR_SPK);
	    if (newPos != m_volumeSlider.getValue()) {
		m_volumeSlider.setValue(newPos);
	    }
	}
	}

	public void setFilenameExtension() {
		RadioModel rm = getRadioModel();
		if (!rm.isRecording()) {
			String fn = m_filenameTextField.getText();
			AudioFileFormat.Type type = getAudioSettings().getPreferredAudioFileType();
			fn = Utils.stripExtension(fn)+"."+type.getExtension();
			m_filenameTextField.setText(fn);
		}
	}

    public void stateChanged(ChangeEvent e) {
	if (e.getSource() == m_volumeSlider) {
		if (m_volumeSlider.isEnabled()) {
			getAudioSettings().setVolumeLevel(DIR_SPK, m_volumeSlider.getValue());
		}
	} else
	if (e.getSource() instanceof JTabbedPane) {
		// when the current tab is changed, update the volume slider
		// which may have been changed on the audio settings page
		if (((JTabbedPane) e.getSource()).getSelectedComponent() == this) {
			// Selected
			updateLevelSlider();
			// update the default filename's extension
			setFilenameExtension();
		} else {
			// Unselected
		}
	}
    }



    public void propertyChange(PropertyChangeEvent e) {
	    boolean newValue = ((Boolean) e.getNewValue()).booleanValue();
	    if (e.getPropertyName().equals(CONNECTION_PROPERTY)) {

	    } else
	    if (e.getPropertyName().equals(STARTED_PROPERTY)) {
		m_recButton.setEnabled(newValue);
		m_rewindButton.setEnabled(newValue);
		m_forwardButton.setEnabled(newValue);
		if (newValue) {
			m_startButton.setText("Stop");
			//displayStatus(0,0);
		} else {
			m_startButton.setText("Start");
		}
	    } else
	    if (e.getPropertyName().equals(AUDIO_PROPERTY))
	    {

	    } else
	    if (e.getPropertyName().equals(RECORDING_PROPERTY)) {
	    	if (newValue) {
			m_recButton.setText("Stop Rec");
		} else {
			m_recButton.setText("Record");
			m_recording.setText(Utils.formatMinSecTenths(0));
			m_recorderProgress.setValue(0);
		}
	    }
	}

    public void displayStatus(int inLevel, int outLevel) {
    	m_volumeMeter.setValue(outLevel);
    	RadioModel rm = getRadioModel();
	CircularBuffer cb = rm.getCircularBuffer();

    	// position in millis
    	int playPos = rm.getPlayPositionMillis();
	int sl = rm.getSpeakerLagMillis();
	m_playback.setText(Utils.formatMinSecTenths(playPos-sl));
	m_realtime.setText(Utils.formatMinSecTenths(playPos));
	if (rm.isRecording()) {
		int rl = rm.getRecorderLagMillis();
		m_recording.setText(Utils.formatMinSecTenths(playPos-rl));
	}
	// circular buffer visualization
	int circBufSize = cb.getEffectiveSize(); // in bytes
	int rt = cb.availableRead(); // realtime in bytes
	sl = rt - cb.getSpeakerLag(); // speaker lag in bytes
	if (sl < 0) {
		sl = 0;
	}
	m_playProgress.setValue((int) (((long) sl) * 1000 / ((long) circBufSize)));
	m_realtimeProgress.setValue((int) (((long) rt) * 1000 / ((long) circBufSize)));
	if (rm.isRecording()) {
		int rl = rt - cb.getRecorderLag();
		if (rl < 0) {
			rl = 0;
		}
		m_recorderProgress.setValue((int) (((long) rl) * 1000 / ((long) circBufSize)));
	}
    }
}



/*** PanelMain.java ***/
