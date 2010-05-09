/*
 *	AudioPlayback.java
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
import org.tritonus.share.sampled.AudioFormats;
import static org.jsresources.utils.audio.AudioDebug.*;


// Class that reads its audio from an AudioInputStream
public class AudioPlayback extends AudioBase {

    private static final boolean DEBUG_TRANSPORT = false;

    private PlayThread thread;

    public AudioPlayback(AudioFormat format, Mixer mixer, int bufferSizeMillis) {
	this(format, null, mixer, bufferSizeMillis);
    }

    public AudioPlayback(AudioFormat netFormat, AudioFormat lineFormat, Mixer mixer, int bufferSizeMillis) {
	super("Playback", netFormat, lineFormat, mixer, bufferSizeMillis);
    }

    protected void createLineImpl() throws Exception {
	DataLine.Info info = new DataLine.Info(SourceDataLine.class, lineFormat);

	// get the playback data line for capture.
	if (mixer != null) {
	    line = (SourceDataLine) mixer.getLine(info);
	} else {
	    line = AudioSystem.getSourceDataLine(lineFormat);
	}
    }

    protected void openLineImpl() throws Exception {
	SourceDataLine sdl = (SourceDataLine) line;
	sdl.open(lineFormat, bufferSize);
    }

    public synchronized void start() throws Exception {
    	if (thread != null && thread.isTerminating()) {
	    thread.terminate();
	    thread = null;
    	}
    	if (thread == null) {
		// start thread
		thread = new PlayThread();
		thread.start();
	}
	super.start();
	if ((ais!=null) && (ais instanceof ReopenableAIS)) {
		((ReopenableAIS) ais).open();
	}
    }

    protected void closeLine(boolean willReopen) {
    	PlayThread oldThread = null;
    	synchronized(this) {
		if (!willReopen) {
		    if (thread != null) {
			thread.terminate();
		    }
		    if (ais != null) {
		    	try {
		    		ais.close();
		    	} catch (IOException ioe) {}
		    }
		}
		super.closeLine(willReopen);
		if (!willReopen && thread != null) {
		    oldThread = thread;
		    thread = null;
		}
	}
	if (oldThread != null) {
	    if (VERBOSE) debugOut("AudioPlayback.closeLine(): closing thread, waiting for it to die");
	    oldThread.waitFor();
	    if (VERBOSE) debugOut("AudioPlayback.closeLine(): thread closed");
	}
    }

    // in network format
    public void setAudioInputStream(AudioInputStream ais) {
	if (!AudioFormats.matches(lineFormat, ais.getFormat())) {
		this.ais = AudioSystem.getAudioInputStream(lineFormat, ais);
	} else {
		this.ais = ais;
	}
	if ((ais!=null) && (ais instanceof ReopenableAIS)) {
		((ReopenableAIS) ais).open();
	}
	if (thread != null) {
		synchronized(thread) {
			thread.notifyAll();
		}
	}
    }

    class PlayThread extends Thread {
	private volatile boolean doTerminate = false;
	private volatile boolean terminated = false;
	// for debugging
	private boolean printedBytes = false;

	public void run() {
	    if (VERBOSE) debugOut("Start AudioPlayback pull thread");
	    if (VERBOSE) debugOut("     ais="+ais);
	    byte[] buffer = new byte[getBufferSize()];
	    try {
	    	AudioInputStream localAIS = ais;
		while (!doTerminate) {
		    SourceDataLine sdl = (SourceDataLine) line;
		    if (localAIS != null && localAIS == ais) {
			int r = localAIS.read(buffer, 0, buffer.length);
			if (doTerminate) break;
			if (r > 50 && DEBUG_TRANSPORT && !printedBytes) {
			    printedBytes = true;
			    debugOut("AudioPlayback: first bytes being played:");
			    String s = "";
			    for (int i = 0; i < 50; i++) {
				s+=" "+buffer[i];
			    }
			    debugOut(s);
			}
			if (r > 0) {
			    if (isMuted()) {
				muteBuffer(buffer, 0, r);
			    }
			    // run some simple analysis
			    calcCurrVol(buffer, 0, r);
			    if (sdl != null) {
			    	sdl.write(buffer, 0, r);
			    }
			} else {
			    if (r == 0) {
				synchronized(this) {
				    this.wait(40);
				}
			    }
			}
		    } else {
			synchronized(this) {
			    this.wait(50);
			}
			localAIS = ais;
			if (buffer.length != getBufferSize()) {
				buffer = new byte[getBufferSize()];
			}
		    }
		}
	    } catch (IOException ioe) {
		//if (DEBUG) ioe.printStackTrace();
	    } catch (InterruptedException ie) {
		if (DEBUG) ie.printStackTrace();
	    }
	    if (VERBOSE) debugOut("Stop AudioPlayback pull thread");
	    terminated = true;
	}

	public synchronized void terminate() {
	    doTerminate = true;
	    this.notifyAll();
	}

	public synchronized boolean isTerminating() {
		return doTerminate || terminated;
	}

	public synchronized void waitFor() {
	    if (!terminated) {
		try {
		    this.join();
		} catch (InterruptedException ie) {
		    if (DEBUG) ie.printStackTrace();
		}
	    }
	}
    }

}
