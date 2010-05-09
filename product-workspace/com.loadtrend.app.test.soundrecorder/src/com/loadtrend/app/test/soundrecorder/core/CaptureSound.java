package com.loadtrend.app.test.soundrecorder.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.tritonus.share.sampled.AudioFormats;

public class CaptureSound implements Runnable {
    
    private String filename  = null;
    
    // capture audio format: PCM format.
    private AudioFormat audioFormat = null;
    
    // origal audio format.
    private AudioFormat origalAudioFormat = null;
    
    private AudioFileFormat.Type filetype = null;
    
    private String errStr = null;
    
    private long capturedAudioBytesLength = 0;

    private TargetDataLine line = null;
    
    private Thread captureSoundThread = null;
    
    private Thread saveSoundThread = null;
    
    private ByteArrayOutputStream saveByteArrayOutputStream = new ByteArrayOutputStream();
    
    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    
    private boolean pauseSign = false;
    
    private CaptureSoundListener listener = null;
    
    private Thread paintSoundThread = null;
    
    private boolean isSilenceDuration = false;
        
    
    // Default is 0;
    private int silenceDurationDefine = 0;
    
    public CaptureSound(String filename, AudioFormat audioFormat, AudioFileFormat.Type filetype) {
        this(filename, audioFormat, filetype, 0);
    }
    
    /**
     * 
     * @param filename
     * @param audioFormat
     * @param filetype
     * @param silenceDurationDefine define silence duration, and if silence pause recording util not silence. millisecond.
     *        input 0 to avoid silence affects description before.
     */
    public CaptureSound(String filename, AudioFormat audioFormat, AudioFileFormat.Type filetype, int silenceDurationDefine) {
        this.silenceDurationDefine = silenceDurationDefine;
        this.filename = filename;
        this.origalAudioFormat = audioFormat;
        this.filetype = filetype;
        if (audioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED) ||
            audioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
            this.audioFormat = audioFormat;
        } else {
            this.audioFormat =  new AudioFormat(audioFormat.getSampleRate(), 16,
                                   audioFormat.getChannels(),
                                   true,  // signed
                                   false); // big endian
        }
    }
    
    /**
     * For test the recording source, will not save the files.
     * @param audioFormat
     * @param filetype
     */
    public CaptureSound(AudioFormat audioFormat, AudioFileFormat.Type filetype) {
        this.origalAudioFormat = audioFormat;
        this.filetype = filetype;
        if (audioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED) ||
            audioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
            this.audioFormat = audioFormat;
        } else {
            this.audioFormat =  new AudioFormat(audioFormat.getSampleRate(), 16,
                                   audioFormat.getChannels(),
                                   true,  // signed
                                   false); // big endian
        }
    }
    
    /**
     * Config the mp3 encoding parameters.
     * @param quality Quality of output mp3 file. In VBR mode, this affects the size of the mp3 file. (Default middle) One of: lowest, low, middle, high, highest
     * @param bitrate Bitrate in KBit/s. Useless in VBR mode. (Default 128) One of: 32 40 48 56 64 80 96 112 128 160 192 224 256 320 (MPEG1) Or: 8 16 24 32 40 48 56 64 80 96 112 128 144 160 (MPEG2 and MPEG2.5
     * @param isVBR VBR (variable bit rate) mode. Slower, but potentially better quality. (Default off)
     */
    public void encodeMP3Param(String quality, String bitrate, boolean isVBR) {
    	if (isVBR) {
    		System.setProperty("tritonus.lame.vbr", "true");
    	} else {
		    System.setProperty("tritonus.lame.bitrate", bitrate);
    	}
		System.setProperty("tritonus.lame.quality", quality);
    }
    
    public void start() {
        this.pauseSign = false;
        this.capturedAudioBytesLength = 0;
        errStr = null;
        
        // Caputre Sound Thread
        captureSoundThread = new Thread(this);
        captureSoundThread.setName("Capture Sound Thread");
        captureSoundThread.start();
        
        // Save Sound Thread
        if (this.filename != null) {
	        this.saveSoundThread = new Thread() {
	            public void run() {
	                AudioInputStream audioInputStream = new RecorderAudioInputStream();
	                // convert stream, if necessary
	                if (!AudioFormats.matches(audioFormat, origalAudioFormat)) {
	                    // this may throw an exception
	                	// convert pcm to mp3 AudioInputStream.
	                    audioInputStream = AudioSystem.getAudioInputStream(origalAudioFormat, audioInputStream);
	                }
	                try {
	                    AudioSystem.write(audioInputStream, filetype, new File(filename));
	                } catch (IOException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	            }
	        };
	        saveSoundThread.setName("Save Sound Thread");
	        saveSoundThread.start();
        }
        
        if (listener != null) {
        	// Get 0.08 seconds buffer size to paint.
        	final int bufferSeconds = 80;
        	final int bufferSize = (int) (audioFormat.getSampleRate() * audioFormat.getFrameSize() / 1000 * bufferSeconds);
	        paintSoundThread = new Thread(new Runnable() {
				public void run() {
					try {
						byte[] bytes = null;
                        long silenceTime = 0;
						while (captureSoundThread != null) {
							long time = System.currentTimeMillis();
							synchronized (CaptureSound.this.byteArrayOutputStream) {
								while ((bytes = SoundUtil.getPartOfPaintData(byteArrayOutputStream, bufferSize)) == null) {
									CaptureSound.this.byteArrayOutputStream.wait();
								}
							}
                            listener.processingCaptureSound(isSilenceDuration, CaptureSound.this.getDuration(), ((long)(line.getMicrosecondPosition() / 1000)) / 1000.0);
		                    // convert read data to paint data and add it to paint data list, prepare to paint to canvas.
		                    byte[] paintDatas = SoundUtil.getPaintBytes(audioFormat, bytes, bytes.length);
		                    if (paintDatas != null && paintDatas.length > 0) {
		                        boolean isPaintLine = listener.paintSound(paintDatas, audioFormat.getChannels());
		                        // Silence duration function only for having define the silence duration.
		                        if (CaptureSound.this.silenceDurationDefine != 0) {
		                            if (isPaintLine) {
		                                // judgement overdue Silence duration
		                                if (silenceTime == 0) {
		                                    silenceTime = System.currentTimeMillis();
		                                } else {
		                                    isSilenceDuration = (System.currentTimeMillis() - silenceTime) > CaptureSound.this.silenceDurationDefine;
		                                }
		                            } else {
		                                // reset non-duration varable
		                                silenceTime = 0;
		                                isSilenceDuration = false;
		                            }
		                        }
		                    }
		                    time = bufferSeconds - (System.currentTimeMillis() - time);
		                    if (time > 0) {
		                    	Thread.sleep(time);
		                    } else {
		                    	// Get rid of the rest data. When the GUI thread is occupied by others.
		                    	time = time * (-1);
		                    	SoundUtil.getPartOfPaintData(byteArrayOutputStream, (int) (bufferSize / bufferSeconds * time));
		                    }
						}
					} catch (InterruptedException interruptedException) {
					} finally {
						// System.out.println("Paint Sound Thread over.");
					}
				}
	        });
	        paintSoundThread.setName("Paint Sound Thread");
	        paintSoundThread.start();
        }
    }
    
    /**
     * run this method in non-ui thread.
     */
    public void stop() {
    	Thread thread = captureSoundThread;
        synchronized (CaptureSound.this) {
            pauseSign = false;
            CaptureSound.this.notifyAll();
            captureSoundThread = null;
        }
        // wait for thread dead.
        while (thread != null && thread.isAlive()) {
        }
    }
    
    public void pause() {
        synchronized (CaptureSound.this) {
	        pauseSign = true;
        }
    }
    
    public void resume() {
        synchronized (CaptureSound.this) {
	        pauseSign = false;
            CaptureSound.this.notifyAll();
        }
    }
    
    private void shutDown(String message) {
        if ((errStr = message) != null && captureSoundThread != null) {
            captureSoundThread = null;
            System.err.println(errStr);
        }
    }
    
    public void addCaptureSoundListener(CaptureSoundListener listener) {
        this.listener = listener;
    }

    public void run() {

        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
                    
        if (!AudioSystem.isLineSupported(info)) {
            shutDown("Line matching " + info + " not supported.");
            return;
        }

        // get and open the target data line for capture.
        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(audioFormat, line.getBufferSize());
        } catch (LineUnavailableException ex) { 
            shutDown("Unable to open the line: " + ex);
            return;
        } catch (SecurityException ex) { 
            shutDown(ex.toString());
            return;
        } catch (Exception ex) { 
            shutDown(ex.toString());
            return;
        }
        
        int bufSize = line.getBufferSize() / 8 * audioFormat.getFrameSize();

        // captured audio data
        byte[] data = new byte[bufSize];
        int numBytesRead = 0;
        
        line.start();
        
        while (captureSoundThread != null) {
        	try {
	            synchronized (this) {
	                while (this.pauseSign == true) {
	                	line.stop();
	                    this.wait();
	                    line.start();
	                }
	            }
	            
	            if((numBytesRead = line.read(data, 0, bufSize)) == -1) {
	                break;
	            }
	            
	            // notify to paint data.
	            if (paintSoundThread != null) {
		            synchronized (this.byteArrayOutputStream) {
		            	this.byteArrayOutputStream.write(data, 0, numBytesRead);
		            	this.byteArrayOutputStream.notifyAll();
					}
	            }
	            
	            // add read data to write data stream, prepare to write to disc.
	            if (isSilenceDuration == false && saveSoundThread != null) {
		            synchronized (this.saveByteArrayOutputStream) {
		            	this.saveByteArrayOutputStream.write(data, 0, numBytesRead);
		                this.saveByteArrayOutputStream.notifyAll();
		            }
	            }
	        } catch (Exception e) {
	            shutDown("Error during playback: " + e);
	            break;
	        }
        }
        
        if (saveSoundThread != null && !saveSoundThread.isInterrupted()) saveSoundThread.interrupt();
        
        if (paintSoundThread != null && !paintSoundThread.isInterrupted()) paintSoundThread.interrupt();
        
        // wait for thread dead.
        while (saveSoundThread != null && saveSoundThread.isAlive()) {
        }
        
        // wait for thread dead.
        while (paintSoundThread != null && paintSoundThread.isAlive()) {
        }

        // we reached the end of the stream.  stop and close the line.
        line.stop();
        line.close();
        line = null;
        
        try {
			this.byteArrayOutputStream.close();
			this.saveByteArrayOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        if (this.listener != null) {
            this.listener.stopCaptureSound(this.getDuration(), this.getCapturedAudioBytesLength());
        }
    }
    
    /**
	 * @return Returns the capturedAudioBytesLength.
	 */
	public long getCapturedAudioBytesLength() {
		return capturedAudioBytesLength;
	}

	/**
	 * @return Returns the duration.
	 */
	public double getDuration() {
		return capturedAudioBytesLength * 1.0 / audioFormat.getFrameSize() / audioFormat.getFrameRate();
	}

	public TargetDataLine getTargetDataLine() {
        return this.line;
    }

    public String getFilename() {
        return filename;
    }
    
    private class RecorderAudioInputStream extends AudioInputStream {
        
        private byte[] datas = null;
        
        public RecorderAudioInputStream() {
            super(new ByteArrayInputStream(new byte[0]), audioFormat, AudioSystem.NOT_SPECIFIED);
        }
        
        public int read() throws IOException {
            throw new IOException("illegal call to RecorderAudioInputStream.read()!");
        }

        // this method is blocking
        public int read(byte[] b, int off, int len) throws IOException {
        	try {
        		if (captureSoundThread == null) return -1;
	            synchronized (CaptureSound.this.saveByteArrayOutputStream) {
	            	while ((datas = SoundUtil.getPartOfPaintData(CaptureSound.this.saveByteArrayOutputStream, len)) == null) {
	            		CaptureSound.this.saveByteArrayOutputStream.wait();
	            	}
				}
	            System.arraycopy(datas, 0, b, 0, len);
	            CaptureSound.this.capturedAudioBytesLength = capturedAudioBytesLength + len;
	            return len;
        	} catch (InterruptedException e) {
        		return -1;
			}
        }

        public int read(byte[] b) throws IOException {
            return read(b,0,b.length);
        }
    }
}