/*
 *	AudioBase.java
 */

/*
 * Copyright (c) 2001, 2004, 2005 by Florian Bomers
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

package org.jsresources.utils.audio;

import	java.io.*;
import	javax.sound.sampled.*;
import org.tritonus.share.sampled.*;

import static org.jsresources.utils.audio.AudioDebug.*;

// base class for stream-based audio i/o
public abstract class AudioBase implements LineListener {

    protected AudioFormat lineFormat;
    protected AudioFormat netFormat;
    protected AudioInputStream ais;

    protected int bufferSizeMillis;
    protected int bufferSize;
    protected Mixer mixer;
    protected String title;
    protected DataLine line;

    // current volume level: 0..128, or -1 for (none)
    protected int lastLevel = -1;
    protected boolean levelEnabled = true;
    protected boolean muted = false;
    protected boolean started = false;

    protected AudioBase(String title, AudioFormat netFormat, AudioFormat lineFormat, Mixer mixer, int bufferSizeMillis) {
	this.title = title;
	this.bufferSizeMillis = bufferSizeMillis;
	this.mixer = mixer;
	try {
	    setFormat(netFormat, lineFormat);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void setTitle(String s) {
    	this.title = s;
    }

    public String getTitle() {
    	return title;
    }

    public void update(LineEvent event) {
	if (DEBUG) {
	    if (event.getType().equals(LineEvent.Type.STOP)) {
		debugOut(title+": STOP event");
	    } else if (event.getType().equals(LineEvent.Type.START)) {
		debugOut(title+": START event");
	    } else if (event.getType().equals(LineEvent.Type.OPEN)) {
		debugOut(title+": OPEN event");
	    } else if (event.getType().equals(LineEvent.Type.CLOSE)) {
		debugOut(title+": CLOSE event");
	    }
	}
    }

    // opens the sound hardware
    public void open() throws Exception {
	closeLine(false);
	destroyLine();
	createLine();
	openLine();
    }

    protected abstract void createLineImpl() throws Exception;

    private void createLine() throws Exception {
	try {
	    line = null;
	    createLineImpl();
	    line.addLineListener(this);
	    if (DEBUG) {
		debugOut("Got line for "+title+": "+line.getClass());
	    }
	} catch (LineUnavailableException ex) {
	    throw new Exception("Unable to open "+title+": "+ex.getMessage());
	}
    }

    protected abstract void openLineImpl() throws Exception;

    private void openLine() throws Exception {
	try {
	    // align to frame size
	    bufferSize = (int) AudioUtils.millis2bytes(bufferSizeMillis, lineFormat);
	    bufferSize -= bufferSize % lineFormat.getFrameSize();
	    openLineImpl();
	    if (DEBUG) debugOut(title+": opened line");
	    bufferSize = line.getBufferSize();
	    if (VERBOSE) {
		debugOut(title+": buffersize="+bufferSize+" bytes.");
	    }
	} catch (LineUnavailableException ex) {
	    started = false;
	    throw new Exception("Unable to open "+title+": "+ex.getMessage());
	}
    }

    public void start() throws Exception {
	if (line == null) {
	    if (DEBUG) debugOut(title+": Call to start(), but line not created!");
	    throw new Exception(title+": cannot start");
	}
	line.flush();
	line.start();
	started = true;
	if (DEBUG) debugOut(title+": started line");
    }

    public void close() {
	close(false);
    }

    public void close(boolean willReopen) {
	closeLine(willReopen);
	destroyLine();
    }

    protected void closeLine(boolean willReopen) {
	if (!willReopen) lastLevel = -1;
	if (line!=null) {
	    line.flush();
	    line.stop();
	    line.close();
	    started = false;
	    if (DEBUG && title!=null) debugOut(title+": line closed.");
	}
    }

    private void destroyLine() {
	if (line != null) {
	    line.removeLineListener(this);
	}
	line = null;
    }

    public boolean isStarted() {
    	return (line != null) && started;
    }

    public boolean isOpen() {
    	return (line != null) && (line.isOpen());
    }

    public int getPositionMillis() {
    	if (line!=null) return (int) line.getMicrosecondPosition()/1000;
    	return 0;
    }

    public int getBufferSize() {
	return bufferSize;
    }

    public int getBufferSizeMillis() {
	return bufferSizeMillis;
    }

    public void setBufferSizeMillis(int bufferSizeMillis) throws Exception {
	if (this.bufferSizeMillis == bufferSizeMillis) return;
	boolean wasOpen = isOpen();
	boolean wasStarted = isStarted();
	closeLine(true);

	this.bufferSizeMillis = bufferSizeMillis;

	if (wasOpen) {
	    openLine();
	    if (wasStarted) {
		start();
	    }
	}
    }

    // may be null!
    public AudioInputStream getAudioInputStream() {
    	return ais;
    }

    public AudioFormat getNetFormat() {
	return netFormat;
    }

    public AudioFormat getLineFormat() {
	return lineFormat;
    }

    public void setFormat(AudioFormat format) throws Exception {
    	setFormat(format, null);
    }

    // this method cannot be called when open
    // because we would have to re-setup the streams
    public void setFormat(AudioFormat netFormat, AudioFormat lineFormat) throws Exception {
	// if no conversion is wanted, allow one of the formats to be null
	if (netFormat == null) {
		netFormat = lineFormat;
	}
	if (lineFormat == null) {
		lineFormat = netFormat;
	}
	if (this.netFormat!=null && this.lineFormat!=null
	    && AudioFormats.matches(this.netFormat, netFormat)
	    && AudioFormats.matches(this.lineFormat, lineFormat)) {
	    	return;
	}
	boolean wasOpen = isOpen();
	if (wasOpen) {
	    throw new Exception("cannot change format while open");
	}
	this.lineFormat = lineFormat;
	this.netFormat = netFormat;
    }

    public void setMixer(Mixer mixer) throws Exception {
	if (this.mixer == mixer) return;
	boolean wasOpen = isOpen();
	boolean wasStarted = isStarted();
	close(true);

	this.mixer = mixer;

	if (wasOpen) {
	    createLine();
	    openLine();
	    if (wasStarted) {
		start();
	    }
	}
    }

    public void setMuted(boolean muted) {
	this.muted = muted;
    }

    public boolean isMuted() {
	return this.muted;
    }

    public int getLevel() {
	return lastLevel;
    }

    public void setLevelEnabled(boolean enabled) {
    	levelEnabled = enabled;
    }

    public boolean isLevelEnabled() {
    	return levelEnabled;
    }

    // find the current playback level: the maximum value of this buffer
    protected void calcCurrVol(byte[] b, int off, int len) {
	if (!levelEnabled) {
		lastLevel = -1;
		return;
	}

	int end = off+len;
	int sampleSize = (lineFormat.getSampleSizeInBits() + 7) / 8;
	int max = 0;
	if (sampleSize == 1) {
	    // 8-bit
	    for ( ; off < end; off++) {
		int sample = (byte) (b[off] + 128);
		if (sample < 0) sample = -sample;
		if (sample > max) max = sample;
	    }
	    lastLevel = max;
	} else if (sampleSize == 2) {
	    if (lineFormat.isBigEndian()) {
		// 16-bit big endian
		for ( ; off < end; off+=2) {
		    int sample = (short) ((b[off]<<8) | (b[off+1] & 0xFF));
		    if (sample < 0) sample = -sample;
		    if (sample > max) max = sample;
		}
	    } else {
		// 16-bit little endian
		for ( ; off < end; off+=2) {
		    int sample = (short) ((b[off+1]<<8) | (b[off] & 0xFF));
		    if (sample < 0) sample = -sample;
		    if (sample > max) max = sample;
		}
	    }
	    //System.out.print("max="+max+" ");
	    lastLevel = max >> 8;
	    //System.out.print(":"+len+":");
	    //System.out.print("[lL="+lastLevel+" "+getClass().toString()+"]");
	} else {
	    lastLevel = -1;
	}
    }

    // silence this array
    protected void muteBuffer(byte[] b, int off, int len) {
	int end = off+len;
	int sampleSize = (lineFormat.getSampleSizeInBits() + 7) / 8;
	byte filler = 0;
	if (sampleSize == 1) {
	    filler = -128;
	}
	for ( ; off < end; off++) {
	    b[off] = filler;
	}
    }

}
