package com.loadtrend.app.test.soundrecorder.core;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.tritonus.share.sampled.Encodings;

/**
 * Write data to the OutputChannel.
 */
public class PlaybackSound implements Runnable {
	
	private AudioInputStream audioInputStream = null;
	
	private String errStr = null;
	
	// private double duration = 0.0f;

    private SourceDataLine line = null;
    
    private Thread playbackSoundThread = null;
    
    private boolean pauseSign = false;
    
    private String filename = null;
    
    private PlaybackSoundListener listener = null;
    
    private Thread paintSoundThread = null;
    
    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    
    private double startTimePosition = -1;
    
    private double endTimePosition = -1;
    
    private long startDataPosition = 0;
    
    private long endDataPosition = 0;
    
    private long readAudioBytesLength = 0;
    
    private AudioFormat audioFormat = null;
    
    public PlaybackSound(String filename) {
        this.filename = filename;
    }
    
    /**
     * Play part of the sound with the specified start time and end time
     * @param filename
     * @param startDuration
     * @param endDuration
     */
    public PlaybackSound(String filename, double startTimePosition, double endTimePosition) {
        this.filename = filename;
        this.startTimePosition = startTimePosition;
        this.endTimePosition = endTimePosition;
    }

    public void start() {
    	try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(this.filename));
			AudioFormat.Encoding encoding = audioInputStream.getFormat().getEncoding();
			if (encoding.equals(AudioFormat.Encoding.PCM_SIGNED) ||
			    encoding.equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
				this.audioInputStream = audioInputStream;
			}
			if (encoding.equals(Encodings.getEncoding("MPEG1L3")) || encoding.equals(Encodings.getEncoding("MPEG2L3"))) {
				// convert mp3 to pcm AudioInputStream.
	            AudioFormat audioFormat =  new AudioFormat(audioInputStream.getFormat().getSampleRate(), 16,
                                                           audioInputStream.getFormat().getChannels(),
                                                           true,  // signed
                                                           false); // big endian
				this.audioInputStream = AudioSystem.getAudioInputStream(audioFormat, audioInputStream);
			}
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (this.audioInputStream != null) {
			this.audioFormat = this.audioInputStream.getFormat();
			this.startDataPosition = this.getDataPosition(this.startTimePosition);
			this.endDataPosition = this.getDataPosition(this.endTimePosition);
		}
		
        this.pauseSign = false;
        errStr = null;
        playbackSoundThread = new Thread(this);
        playbackSoundThread.setName("Playback Sound Thread");
        playbackSoundThread.start();
        
        if (listener != null && audioInputStream != null) {
        	// Get 0.08 seconds buffer size to paint.
        	final int bufferSeconds = 80;
        	final int bufferSize = (int) (audioFormat.getSampleRate() * audioFormat.getFrameSize() / 1000 * bufferSeconds);
	        paintSoundThread = new Thread(new Runnable() {
				public void run() {
					try {
						byte[] bytes = null;
						while (playbackSoundThread != null) {
							long time = System.currentTimeMillis();
							synchronized (PlaybackSound.this.byteArrayOutputStream) {
								while ((bytes = SoundUtil.getPartOfPaintData(byteArrayOutputStream, bufferSize)) == null) {
									PlaybackSound.this.byteArrayOutputStream.wait();
								}
							}
		                    // listener.processingPlaybackSound(((long)(line.getMicrosecondPosition() / 1000)) / 1000.0);
		                    listener.processingPlaybackSound(getDuration());
		                    // convert read data to paint data and add it to paint data list, prepare to paint to canvas.
		                    byte[] paintDatas = SoundUtil.getPaintBytes(audioFormat, bytes, bytes.length);
		                    if (paintDatas != null && paintDatas.length > 0) {
		                        listener.paintSound(paintDatas, audioFormat.getChannels());
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
        Thread thread = playbackSoundThread;
        synchronized (PlaybackSound.this) {
            pauseSign = false;
            PlaybackSound.this.notifyAll();
            playbackSoundThread = null;
        }
        // wait for thread dead.
        while (thread != null && thread.isAlive()) {
        }
    }
    
    public void pause() {
        synchronized (PlaybackSound.this) {
            pauseSign = true;
        }
    }
    
    public void resume() {
        synchronized (PlaybackSound.this) {
            pauseSign = false;
            PlaybackSound.this.notifyAll();
        }
    }
    
    private void shutDown(String message) {
        if ((errStr = message) != null) {
            System.err.println(errStr);
        }
        if (playbackSoundThread != null) {
        	playbackSoundThread = null;
        } 
    }
    
    public void addPlaybackSoundListener(PlaybackSoundListener listener) {
        this.listener = listener;
    }

    public void run() {

        // make sure we have something to play
        if (audioInputStream == null) {
            shutDown("No loaded audio to play back");
            return;
        }
        
        // define the required attributes for our line, 
        // and make sure a compatible line is supported.

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        
        if (!AudioSystem.isLineSupported(info)) {
            shutDown("Line matching " + info + " not supported.");
            return;
        }

        // get and open the source data line for playback.

        // get 0.1 seconds data as buffer.
	    int bufSize = (int) (audioFormat.getFrameSize() * audioFormat.getFrameRate() * 0.1);
	
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(audioFormat, bufSize);
        } catch (LineUnavailableException ex) { 
            shutDown("Unable to open the line: " + ex);
            return;
        }

        // play back the captured audio data
        byte[] data = new byte[bufSize];
        int numBytesRead = 0;
        
        boolean isAutoFinished = false;
        
        // skip the data before startDataPosition
        if (startTimePosition != -1) {
    	    long numBytesRemaining = startDataPosition;
            while (numBytesRemaining > 0 ) {
                try {
                    numBytesRemaining -= audioInputStream.read(data);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            readAudioBytesLength = readAudioBytesLength + startDataPosition;
        }

        // start the source data line
        line.start();
        
        while (playbackSoundThread != null) {
            try {
                synchronized (this) {
                    while (this.pauseSign == true) {
                        this.wait();
                    }
                }
                
                if ((numBytesRead = audioInputStream.read(data)) == -1) {
                	isAutoFinished = true;
                    break;
                }
                
                // stop playing after arriving at the endDataPosition.
                readAudioBytesLength = readAudioBytesLength + numBytesRead;
                if (startTimePosition != -1 && readAudioBytesLength >= endDataPosition) {
                	break;
                }
                
                // notify to paint data.
	            if (paintSoundThread != null) {
	                synchronized (this.byteArrayOutputStream) {
	                	this.byteArrayOutputStream.write(data, 0, numBytesRead);
	                	this.byteArrayOutputStream.notifyAll();
					}
	            }
	            
                // playback the data.
                int numBytesRemaining = numBytesRead;
                while (numBytesRemaining > 0 ) {
                    numBytesRemaining -= line.write(data, 0, numBytesRemaining);
                }
            } catch (Exception e) {
                shutDown("Error during playback: " + e);
                break;
            }
        }
        
        if (paintSoundThread != null && !paintSoundThread.isInterrupted()) paintSoundThread.interrupt();
        
        // wait for thread dead.
        while (paintSoundThread != null && paintSoundThread.isAlive()) {
        }
        
        // we reached the end of the stream.  let the data play out, then
        // stop and close the line.
        if (playbackSoundThread != null) {
            line.drain();
        }
        line.stop();
        line.close();
        line = null;
        shutDown(null);        
        
        try {
			this.byteArrayOutputStream.close();
            this.audioInputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        if (listener != null) {
            listener.stopPlaybackSound(isAutoFinished);
        }
    }
    
	/**
	 * @return Returns the duration.
	 */
	public double getDuration() {
		return readAudioBytesLength * 1.0 / audioFormat.getFrameSize() / audioFormat.getFrameRate();
	}
    
	public long getDataPosition(double timePosition) {
		long dataPosition = (long) (timePosition * audioFormat.getFrameSize() * audioFormat.getFrameRate());
       	// get a integer frame length bytes, if use the dataPosition directly, error happens when invoke line.write
		return dataPosition / audioFormat.getFrameSize() * audioFormat.getFrameSize();
	}
	
    public SourceDataLine getSourceDataLine() {
    	return line;
    }
} // End class Playback
