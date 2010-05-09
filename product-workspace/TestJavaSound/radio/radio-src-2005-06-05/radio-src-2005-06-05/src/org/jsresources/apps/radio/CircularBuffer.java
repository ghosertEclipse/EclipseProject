/*
 *	CircularBuffer.java
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

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package org.jsresources.apps.radio;

import java.io.*;
import javax.sound.sampled.*;
import org.jsresources.utils.Utils;
import org.jsresources.utils.audio.AudioUtils;
import org.jsresources.utils.audio.ReopenableAIS;
import static org.jsresources.apps.radio.Constants.*;

public class CircularBuffer extends OutputStream {

	private static final boolean DEBUG_IO = false;

	/** how many bytes does the speaker lag behind
	* the write cursor */
	private int speakerLag;

	// how many bytes does the recorder lag behind */
	private int recorderLag;

	/** current write position in the buffer */
	private int writePos;

	/** the number of valid bytes in the buffer */
	private int availRead;

	/** AudioFormat of the data */
	private AudioFormat audioFormat;

	/** the array holding the buffer's data */
	private byte[] buffer;

	// size of buffer next time it is inited
	private int nextSizeBytes;
	private int nextSizeMillis;

	private Object recorderReaderLock = new Object();
	private Object speakerReaderLock = new Object();

	// for debugging
	private boolean hasReadSpeaker, hasReadRecorder;

	public CircularBuffer() {
		nextSizeBytes = 100000;
	}

	public synchronized void init(AudioFormat format, int bufferSizeMillis) {
		setSizeMillis(bufferSizeMillis);
		init(format);
	}

	public synchronized void init(AudioFormat format) {
		setFormat(format);
		init();
	}

	public synchronized void init() {
		int size = getSizeBytes();
		if (VERBOSE) Debug.out("Circular Buffer: init. Size="+(size/1024)+"KB <=> "
		                       +AudioUtils.bytes2millis(size, getFormat()));

		if (buffer == null || size != buffer.length) {
			buffer = new byte[size];
		}
		speakerLag = 0;
		recorderLag = 0;
		writePos = 0;
		availRead = 0;
	}

	/** total valid bytes in buffer */
	public int availableRead() {
		return availRead;
	}

	private int availableReadSpeaker() {
		return speakerLag;
	}

	private int availableReadRecorder() {
		// try to adjust the recorder position to the speaker
		// position where possible
		int result = recorderLag - speakerLag;
		if (result < 0) result = 0;
		return result;
	}

	public int getSpeakerLag() {
		return speakerLag;
	}

	public int getRecorderLag() {
		return recorderLag;
	}

	public int getSpeakerLagMillis() {
		return (int) AudioUtils.bytes2millis(speakerLag, getFormat());
	}
	public int getRecorderLagMillis() {
		return (int) AudioUtils.bytes2millis(recorderLag, getFormat());
	}

	/** returns the actual size, in bytes, of the internal buffer */
	public int getEffectiveSize() {
		return (buffer==null)?0:buffer.length;
	}

	/** set size of buffer -- will only be effective upon next init */
	public void setSizeBytes(int newSize) {
		if (newSize != nextSizeBytes) {
			if (VERBOSE) Debug.out("Circular Buffer: Setting size to "+(newSize/1024)+"KB.");
			nextSizeBytes = newSize;
		}
		nextSizeMillis = 0;
	}

	// precondition: format is already set
	public void setSizeMillis(int millis) {
		if (millis != nextSizeMillis) {
			if (VERBOSE) Debug.out("Circular Buffer: Setting size to "+millis+" millis.");
			nextSizeMillis = millis;
		}
		nextSizeBytes = 0;
	}

	public int getSizeBytes() {
		if (nextSizeMillis > 0) {
			return (int) AudioUtils.millis2bytes(nextSizeMillis, getFormat());
		} else {
			return Utils.align(nextSizeBytes, getFormat().getFrameSize());
		}
	}

	public int getSizeMillis() {
		if (nextSizeMillis > 0) {
			return nextSizeMillis;
		} else {
			return (int) AudioUtils.bytes2millis(nextSizeBytes, getFormat());
		}
	}

	public AudioFormat getFormat() {
		return audioFormat;
	}

	public void setFormat(AudioFormat af) {
		if (audioFormat != af) {
			if (VERBOSE) Debug.out("Circular Buffer: Setting format to "+af);
			this.audioFormat = af;
		}
	}

	public synchronized void setRecorderPosToSpeakerPos() {
		recorderLag = speakerLag;
	}

	/* Writing/OutputStream methods */
	public void write(byte[] b) throws IOException {
		write(b,0,b.length);
	}

	public void write(int b) throws IOException {
		// no use for this method
		throw new IOException("illegal call to CircularBuffer.write(int)!");
	}

	public synchronized void write(byte[] b, int off, int len) throws IOException {
		// write always overwrites
		int si = getEffectiveSize();
		int fs = getFormat().getFrameSize();
		if (fs < 1) fs = 1;
		// aligning is necessary but still dangerous
		// if fractional frames are written:
		// the next write will again write the second half
		// of the fractional frame and therefore be unaligned
		// from then on.
		len = Utils.align(len, fs);
		// adjust available bytes
		availRead += len;
		if (availRead > si) availRead = si;

		// the read positions fall behind by len bytes
		speakerLag += len;
		recorderLag += len;
		// but we cannot exceed the available bytes
		if (speakerLag > si) {
			// buffer underrun!
			if (hasReadSpeaker) {
				if (VERBOSE) Debug.out("speaker buffer underrun");
				hasReadSpeaker = false;
			}
			speakerLag = si;
		}
		if (recorderLag > si) {
			// buffer underrun!
			if (hasReadRecorder) {
				if (VERBOSE) Debug.out("recorder buffer underrun");
				hasReadRecorder = false;
			}
			recorderLag = si;
		}

		// finally copy to the buffer
		while (len > 0) {
			int thisLen = len;
			if ((len + writePos) > si) {
				thisLen = si - writePos;
			}
			if (DEBUG_IO) {
				Debug.out("Writing from b["+b.length+"], off="+off+" to "+(off+thisLen-1));
				Debug.out("          to buffer["+buffer.length+"], writePos="+writePos+" to "+(writePos+thisLen-1));
			}
			if (DEBUG) {
				if (off+thisLen > b.length  || thisLen < 0 || off < 0) {
					Debug.out("## Error: CircularBuffer.readImpl: copying from b["+b.length+"] off="+off+" to "+(off+thisLen-1));
				}
				if (writePos+thisLen > buffer.length || thisLen < 0 || writePos < 0) {
					Debug.out("## Error: CircularBuffer.write: copying to buffer["+buffer.length+"] readPos="+writePos+" to "+(writePos+thisLen-1));
				}
			}
			System.arraycopy(b, off, buffer, writePos, thisLen);
			writePos+=thisLen;
			off+=thisLen;
			if (writePos>=si + 1 - fs) {
				writePos -= si;
			}
			len -= thisLen;
		}
		synchronized (speakerReaderLock) {
			speakerReaderLock.notifyAll();
		}
		synchronized (recorderReaderLock) {
			recorderReaderLock.notifyAll();
		}
	}


	// if not isSpeaker, it is read from Recorder position
	// this method is not blocking
	synchronized int readImpl(byte[] b, int off, int len, boolean isSpeaker) {
		int avail, oldLag;
		if (isSpeaker) {
			avail = availableReadSpeaker();
			oldLag = speakerLag;
		} else {
			avail = availableReadRecorder();
			oldLag = recorderLag;
		}

		if (isSpeaker) {
			hasReadSpeaker = true;
		} else {
			hasReadRecorder = true;
		}

		int si = getEffectiveSize();
		int fs = getFormat().getFrameSize();
		if (fs < 1) fs = 1;

		int maxRead = len;
		if (maxRead > avail) maxRead = avail;
		maxRead = Utils.align(maxRead, fs);
		if (DEBUG) {
			if (off+maxRead > b.length || off+len > b.length) {
				Debug.out("## Illegal params to CircularBuffer.readImpl: b["
				          +b.length+"], off="+off+"  len="+len+"  maxRead="+maxRead);
			}
		}

		// recompute the new lag after reading
		if (isSpeaker) {
			speakerLag -= maxRead;
		} else {
			recorderLag -= maxRead;
		}

		// copy the data
		int readPos = ((writePos - oldLag) + si) % si;
		len = maxRead;
		while (len > 0) {
			int thisLen = len;
			if ((readPos + thisLen) > si) {
				thisLen = si - readPos;
			}
			if (DEBUG_IO) {
				Debug.out("Reading from buffer["+buffer.length+"], readPos="
				          +readPos+" to "+(readPos+thisLen-1));
				Debug.out("          to b["+b.length+"], off="+off+" to "+(off+thisLen-1));
			}
			if (DEBUG) {
				if (readPos+thisLen > buffer.length || thisLen < 0 || readPos < 0) {
					Debug.out("## Error: CircularBuffer.readImpl: copying from buffer["
					          +buffer.length+"] readPos="+readPos+" to "+(readPos+thisLen-1));
				}
				if (off+thisLen > b.length  || thisLen < 0 || off < 0) {
					Debug.out("## Error: CircularBuffer.readImpl: copying to b["
					          +b.length+"] off="+off+" to "+(off+thisLen-1));
				}
			}
			System.arraycopy(buffer, readPos, b, off, thisLen);
			readPos += thisLen;
			off += thisLen;
			if (readPos>=si + 1 - fs) {
				readPos -= si;
			}
			len -= thisLen;
		}
		return maxRead;
	}

	public AudioInputStream getSpeakerAIS() {
		return new CircBufAIS(true);
	}

	public AudioInputStream getRecorderAIS() {
		return new CircBufAIS(false);
	}

	// an AudioInputStream that reads from this circular buffer
	private class CircBufAIS extends AudioInputStream implements ReopenableAIS {
		private boolean isSpeaker;
		private boolean closed;

		CircBufAIS(boolean isSpeaker) {
			super(new ByteArrayInputStream(new byte[0]), CircularBuffer.this.getFormat(), AudioSystem.NOT_SPECIFIED);
			this.isSpeaker = isSpeaker;
			if (VERBOSE) Debug.out("CircBufAIS("+(isSpeaker?"speaker":"recorder")+").<init>.getFormat()="+getFormat());
		}

		public int available() throws IOException {
			if (closed) return 0;
			return (isSpeaker)?availableReadSpeaker():availableReadRecorder();
		}

		public int read() throws IOException {
			throw new IOException("illegal call to CircBufAIS.read()!");
		}

		// this method is blocking
		public int read(byte[] b, int off, int len) throws IOException {
			if (closed) return -1;
			Object lock = recorderReaderLock;
			if (isSpeaker) lock = speakerReaderLock;

			int res = 0;
			int thisLen = len;

			while (true) {
				int thisRead = readImpl(b, off, thisLen, isSpeaker);
				off += thisRead;
				res += thisRead;
				thisLen -= thisRead;
				if (!closed && (res < len)) {
					synchronized(lock) {
						try {
							lock.wait(20);
						} catch (InterruptedException ie) {}
					}
				} else {
					break;
				}
			}
			return res;
		}

		private void notifyLock() {
			Object lock = recorderReaderLock;
			if (isSpeaker) lock = speakerReaderLock;
			synchronized(lock) {
				lock.notifyAll();
			}
		}
		public void close() throws IOException {
			closed = true;
			notifyLock();
		}

		public void open() {
			closed = false;
		}

		public int read(byte[] b) throws IOException {
			return read(b,0,b.length);
		}

		public long skip(long n) throws IOException {
			if (closed) return 0;
			synchronized (CircularBuffer.this) {
				int lag = recorderLag;
				if (isSpeaker) lag = speakerLag;

				if (lag - n > availableRead()) {
					n = lag - availableRead();
				}
				if (lag - n < 0) {
					n = lag;
				}
				lag -= (int) n;
				if (isSpeaker) {
					speakerLag = lag;
				} else {
					recorderLag = lag;
				}
			}
			notifyLock();
			return n;
		}

		public void mark(int readlimit) {
		}

		public void reset() throws IOException {
		}

		public boolean markSupported() {
			return false;
		}

	} // CircBufAIS
}
