/*
 *	AsynchronousRecorder.java
 */

/*
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

package org.jsresources.utils.audio;

import	java.io.*;
import	javax.sound.sampled.*;

import static org.jsresources.utils.audio.AudioDebug.*;

/**
 * Implements writing to file from an AudioInputStream
 * in an asynchronous fashion (i.e. in a separate thread).
 * This is useful for streaming recording.
 */
public class AsynchronousRecorder implements Runnable {

    private AudioInputStream ais = null;
    private String filename;
    private AudioFileFormat.Type filetype;
    private Thread thread = null;
    private volatile boolean isStarted = false;
    private volatile boolean inThread = false;

    public AsynchronousRecorder(String filename, AudioInputStream ais, AudioFileFormat.Type filetype) {
	this.filename = filename;
	this.ais = ais;
	this.filetype = filetype;
    }

    /**
     * Start writing to the file whatever can be read
     * from the AudioInputStream.
     * This method will return immediately, and writing to
     * the file will continue in the background until
     * one of the following 2 conditions are met:
     * 1) The AudioInputStream is closed, i.e. its read() method
     *    returns -1.
     * or
     * 2) the stop() method is called.
     *
     * You can use isActive() to check if this class is still
     * writing to file.
     */
    public synchronized void start() {
	if (VERBOSE) debugOut("AsynchronousRecorder.start()");
	thread = new Thread(this);
	isStarted = true;
	thread.start();
    }

    public synchronized void stop() {
	if (VERBOSE) debugOut("AsynchronousRecorder.stop()");
	isStarted = false;
    	if (ais != null) {
    		try {
    			ais.close();
    		} catch (IOException ioe) {
    			debugOut("AsynchronousRecorder: exception while closing ais: "+ioe);
    			if (DEBUG) ioe.printStackTrace();
    		}
	    	if (thread != null && inThread) {
			if (VERBOSE) debugOut("AsynchronousRecorder: closing thread, waiting for it to die");
    			try {
    				thread.join();
    			} catch (InterruptedException ie) {
	    			if (DEBUG) ie.printStackTrace();
    			}
    		}
    		thread = null;
    	}
    }

    public synchronized boolean isActive() {
    	return (thread != null) && isStarted;
    }

    public String getFilename() {
    	return filename;
    }

    // the method called in the different Thread
    public void run() {
    	inThread = true;
	if (VERBOSE) debugOut("AsynchronousRecorder: starting thread");
    	try {
    		AudioSystem.write(ais, filetype, new File(filename));
    	} catch (Throwable t) {
		if (DEBUG) debugOut("AsynchronousRecorder: got exception:"+t);
		if (VERBOSE) t.printStackTrace();
    	}
    	inThread = false;
	isStarted = true;
	if (VERBOSE) debugOut("AsynchronousRecorder: exiting thread");
    }
}
