/*
 *	RadioModel.java
 */

/*
 * Copyright (c) 2004 by Matthias Pfisterer
 * Copyright (c) 2004,2005 by Florian Bomers
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

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.*;

import javax.swing.JOptionPane;
import javax.sound.sampled.*;
import org.jsresources.utils.audio.*;

/*
 *      Tritonus classes.
 *      You need tritonus_share
 *      in the CLASSPATH or the lib/ext directory.
 */
import org.tritonus.share.sampled.AudioFormats;

import static org.jsresources.apps.radio.Constants.*;

public class RadioModel {

    private MasterModel m_masterModel;
    private PropertyChangeSupport m_propertyChangeSupport;
    private Network			m_network;
    private DataInputStream		m_receiveStream;
    private AsynchronousRecorder recorder;

    // audio related: owned by RadioModel
    private AudioBase[] audio = new AudioBase[2];
    private CircularBuffer circBuf;
    private boolean m_audioActive;

    private String m_url; // current shoutcast server

    // realtime info, current time, level meter, etc
    private List<StatusListener> displayListeners = new ArrayList<StatusListener>();
    private StatusListenerThread statusThread;


    public RadioModel(MasterModel masterModel) {
	m_masterModel = masterModel;
	m_propertyChangeSupport = new PropertyChangeSupport(this);
	audio[DIR_MIC] = new AudioCapture(getCircBufFormat(),
					  getAudioSettings().getSelMixer(DIR_MIC),
					  getAudioSettings().getBufferSizeMillis(DIR_MIC));
	audio[DIR_SPK] = new AudioPlayback(getCircBufFormat(), getFileFormat(),
					   getAudioSettings().getSelMixer(DIR_SPK),
					   getAudioSettings().getBufferSizeMillis(DIR_SPK));
	circBuf = new CircularBuffer();
	m_audioActive = false;
    }


    private MasterModel getMasterModel()
    {
	return m_masterModel;
    }


    private ConnectionSettings getConnectionSettings()
    {
	return getMasterModel().getConnectionSettings();
    }

    private AudioSettings getAudioSettings() {
    	return getMasterModel().getAudioSettings();
    }


    private Network getNetwork()
    {
	return m_network;
    }


    public AudioBase getAudio(int d)
    {
	return audio[d];
    }

    public CircularBuffer getCircularBuffer() {
    	return circBuf;
    }

    public boolean isSourceCapture() {
	return ((m_url!=null) && (m_url.length() == 0));
    }

    public void startCapture() {
    	start("");
    }

    public void start(String sourceURL) {
    	m_url = sourceURL;
	try {
	    	if (isSourceCapture()) {
		    circBuf.init(getCircBufFormat(), getAudioSettings().getCircBufMillis());
		    Debug.out("Starting circular buffer with this format:");
		    Debug.out("   "+circBuf.getFormat());
		    // startAudio requires that the circular buffer is already set
		    startAudio(DIR_MIC);
		    initAudioOutputStream();
    		} else {
			// TODO: start network connection
			throw new Exception("Not implemented");
			// TODO: connect connection with circ buffer
		    	// can only init circ buffer after connection is established
			//circBuf.init(getCircBufFormat(), getAudioSettings().getCircBufMillis());
			//notifyConnection();
		}
	    //Debug.out("audio out: " + getAudio(DIR_SPK));
	    startAudio(DIR_SPK);
		// connect DIR_SPK with circular buffer
		((AudioPlayback) getAudio(DIR_SPK)).setAudioInputStream(circBuf.getSpeakerAIS());
	    setAudioActive(true);
	} catch (Exception e) {
	    Debug.out(e);
	    JOptionPane.showMessageDialog(null, new Object[]{"Error: ", e.getMessage()}, "Error", JOptionPane.ERROR_MESSAGE);
	    stop();
	}
	notifyStarted();
    }

    public void stop() {
	    Debug.out("closing audio...");
	    closeAudio();
	    Debug.out("...closed");
	    stopRecording();
	    if (isConnected())
	    {
		Debug.out("diconnecting network...");
		getNetwork().disconnect();
		Debug.out("disconnected...");
		notifyConnection();
	    }
	    circBuf.init();
	    for (int i = 0; i < displayListeners.size(); i++) {
	    	displayListeners.get(i).displayStatus(0,0);
	    }
	    notifyStarted();
    }

    public synchronized void startRecording(String filename) throws Exception {
	// set the recorder write position to current
	// speaker position
	circBuf.setRecorderPosToSpeakerPos();
	// we need an own thread to not block the calling
	// thread with AudioSystem.write()
	if (recorder != null) {
		stopRecording();
	}
	AudioFileFormat.Type afft = getAudioSettings().getPreferredAudioFileType();
	AudioFormat prefFormat = getAudioSettings().getPreferredAudioFormat();
	AudioInputStream ais = circBuf.getRecorderAIS();
	// convert stream, if necessary
	if (!AudioFormats.matches(ais.getFormat(), prefFormat)) {
		// this may throw an exception
		ais = AudioSystem.getAudioInputStream(prefFormat, ais);
	}
	recorder = new AsynchronousRecorder(filename, ais, afft);
	recorder.start();
	notifyRecording();
    }

    public synchronized void stopRecording() {
	if (recorder != null) {
		recorder.stop();
		recorder = null;
	}
	notifyRecording();
    }

    public synchronized boolean isRecording() {
    	return (recorder != null) && (recorder.isActive());
    }


    // if we have an incoming network connection
    public boolean isConnected() {
	return getNetwork()!=null && getNetwork().isConnected();
    }

    // if the system is started
    // the speaker must be activated for that
    public boolean isStarted() {
	return getAudio(DIR_SPK).isStarted();
    }


    // audio related

    public void initAudioOutputStream() {
    	if (getAudio(DIR_MIC).isStarted()) {
	    // connect DIR_MIC with circ buffer
	    Debug.out("Connect line in with circular buffer.");
	    ((AudioCapture) getAudio(DIR_MIC)).setOutputStream(circBuf);
	}
    }

    private void closeAudio() {
    	setAudioActive(false);
    	closeAudio(DIR_SPK);
    	closeAudio(DIR_MIC);
    }

    public boolean isAudioActive() {
	return m_audioActive;
    }

    private void setAudioActive(boolean active) {
	m_audioActive = active;
	notifyAudio();
    }

    private void closeAudio(int d) {
	if (getAudio(d) != null) {
	    getAudio(d).close();
	}
    }

    private void startAudio(int d) throws Exception {
	String dir;
	if (isAudioActive()) {
	    throw new Exception("Cannot start audio if already active!");
	}
	if (d == DIR_MIC) {
	    dir = "line in";
	} else {
	    dir = "speaker";
	}
	Debug.out("Start audio: "+dir);

	if (d == DIR_MIC) {
		// net format, line format
		getAudio(d).setFormat(getCircBufFormat(), getSourceFormat());
	} else {
		getAudio(d).setFormat(getCircBufFormat(), getCircBufFormat());
	}
	Debug.out("Opening "+dir+" with this format:");
	Debug.out("   "+getAudio(d).getLineFormat());
	getAudio(d).open();
	getAudio(d).start();
    }

    /* the format of the circular buffer: some kind of PCM */
    public AudioFormat getCircBufFormat() {
	AudioFormat res = getSourceFormat();
	if (AudioUtils.isPCM(res)) return res;
	return new AudioFormat(res.getSampleRate(), 16,
	                       res.getChannels(),
	                       true,  // signed
	                       false); // big endian
    }

    /* the format of the recorded file */
    public AudioFormat getFileFormat() {
	AudioFormat cbFormat = getCircBufFormat();
	AudioFormat prefFormat = getAudioSettings().getPreferredAudioFormat();
	if (AudioFormats.matches(cbFormat, prefFormat)) {
		return cbFormat;
	}
	// if PCM is preferred then we don't need to
	// convert, because that can be written to file directly
	if (AudioUtils.isPCM(prefFormat)) {
		return cbFormat;
	}
	// if a compressed file format is wished,
	// need to find one with the samplerate
	if ((Math.abs(prefFormat.getSampleRate() - cbFormat.getSampleRate()) > 0.1)
	    || (prefFormat.getChannels() != cbFormat.getChannels())) {
		// hack sample rate and/or channels
		return new AudioFormat(prefFormat.getEncoding(),
		                       cbFormat.getSampleRate(),
		                       prefFormat.getSampleSizeInBits(),
		                       cbFormat.getChannels(),
		                       AudioSystem.NOT_SPECIFIED, // we don't know frame size
		                       AudioSystem.NOT_SPECIFIED, // we don't know frame rate
		                       false); // big endian?
	}
	// otherwise we can ctually use the preferred format
	return prefFormat;
    }

    /* the format of the incoming stream -- either from Line IN, or
     * from shoutcast */
    public AudioFormat getSourceFormat() {
	if (m_network==null || isSourceCapture()) {
		// for capture, use the recording format
		AudioFormat prefFormat = getAudioSettings().getPreferredAudioFormat();
		if (AudioUtils.isPCM(prefFormat)) {
			return prefFormat;
		} else {
			return new AudioFormat(prefFormat.getSampleRate(), 16,
			                       prefFormat.getChannels(),
			                       true,  // signed
			                       false); // big endian
		}
	} else {
		// TODO
		// return Network.getAudioFormat();
		return null;
	}
    }

    public int getPlayPositionMillis() {
	return getAudio(DIR_SPK).getPositionMillis();
    }

    public int getSpeakerLagMillis() {
    	return circBuf.getSpeakerLagMillis();
    }

    public int getRecorderLagMillis() {
    	return circBuf.getRecorderLagMillis();
    }

    public void wind(int dir, int millis) {
	AudioInputStream ais = getAudio(dir).getAudioInputStream();
	if (ais != null) {
		try {
			ais.skip(AudioUtils.millis2bytes(millis, ais.getFormat()));
		} catch (IOException ioe) {
			// nothing to do
		}
	}
    }


    public  DataInputStream getReceiveStream()
    {
	return m_receiveStream;
    }

    private void streamError(String strError)
    {
	JOptionPane.showMessageDialog(null, new Object[]{strError, "Connection will be terminated"}, "Error", JOptionPane.ERROR_MESSAGE);
	getNetwork().disconnect();
	closeAudio();
	notifyConnection();
    }


    /*
     * Properties Support
     */

    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
	m_propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
	m_propertyChangeSupport.removePropertyChangeListener(listener);
    }


    private void notifyConnection()
	{
	    m_propertyChangeSupport.firePropertyChange(CONNECTION_PROPERTY, !isConnected(), isConnected());
	}

    private void notifyStarted()
	{
	    m_propertyChangeSupport.firePropertyChange(STARTED_PROPERTY, !isStarted(), isStarted());
		if (isStarted()) {
	    	    	startStatusListenerThread();
		} else {
	    	    	stopStatusListenerThread();
	    	}
	}

    private void notifyAudio()
	{
	    m_propertyChangeSupport.firePropertyChange(AUDIO_PROPERTY, !isAudioActive(), isAudioActive());

	}

    private void notifyRecording()
	{
	    m_propertyChangeSupport.firePropertyChange(RECORDING_PROPERTY, !isRecording(), isRecording());
	}

    // ----------------------- status listeners ---------------------------
    public void addStatusListener(StatusListener l) {
    	    displayListeners.add(l);
    }

    public void removeStatusListener(StatusListener l) {
    	    displayListeners.remove(l);
    }

    private int getCurrLevel(int dir, int oldLevel) {
    	AudioBase ab = getAudio(dir);
    	int newlevel = 0;
    	if (ab != null) {
	    newlevel = ab.getLevel();
	}
	if (newlevel<0) newlevel=0;
	// decrease slowly
	if (oldLevel - newlevel > 2) {
		return oldLevel - 2;
	}
	return newlevel;
    }

    private synchronized void startStatusListenerThread() {
    	stopStatusListenerThread();
    	statusThread = new StatusListenerThread();
    	statusThread.start();
    }

    private synchronized void stopStatusListenerThread() {
    	if (statusThread != null) {
    		statusThread.terminate();
    		statusThread = null;
    	}
    }

    private class StatusListenerThread extends Thread {
		private volatile boolean terminated;

		public void run() {
		    if (DEBUG) out("Meter Thread: start");
		    try {
			int inlevel = 0;
			int outlevel = 0;
			while (!terminated) {
			    inlevel = getCurrLevel(0, inlevel);
			    outlevel = getCurrLevel(1, outlevel);
			    Thread.sleep(30);
			    for (int i = 0; i < displayListeners.size(); i++) {
			    	displayListeners.get(i).displayStatus(inlevel, outlevel);
			    }
			}
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		    // TODO: would be nicer if it slowly goes down to 0...
		    for (int i = 0; i < displayListeners.size(); i++) {
		    	displayListeners.get(i).displayStatus(0,0);
		    }
		    if (DEBUG) out("Meter Thread: stop");
		}

		public void terminate() {
			terminated = true;
		}
    }

public interface StatusListener {
	public void displayStatus(int inLevel, int outLevel);
}

}

/*** RadioModel.java ***/
