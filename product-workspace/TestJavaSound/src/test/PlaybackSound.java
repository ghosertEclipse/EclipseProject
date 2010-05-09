package test;

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
	
	private static final int bufSize = 16384;
	
	private AudioInputStream audioInputStream = null;
	
	private String errStr = null;
	
	// private double duration = 0.0f;

    private SourceDataLine line = null;
    
    private Thread playbackSoundThread = null;
    
    private boolean pauseSign = false;
    
    private String filename = null;
    
    private String filename2 = null;
    
    // private double duration = 0.0f;
    
    private PlaybackSoundListener listener = null;
    
    public PlaybackSound(String filename) {
        this.filename = filename;
        this.filename2 = this.filename.substring(this.filename.lastIndexOf(File.separator) + 1);
    }

    public void start() {
    	try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(this.filename));
			AudioFormat.Encoding encoding = audioInputStream.getFormat().getEncoding();
			if (encoding.equals(AudioFormat.Encoding.PCM_SIGNED) ||
			    encoding.equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
				this.audioInputStream = audioInputStream;
			}
			if (encoding.equals(Encodings.getEncoding("MPEG1L3"))) {
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
//        long milliseconds = (long)((this.audioInputStream.getFrameLength() * 1000) / this.audioInputStream.getFormat().getFrameRate());
//        duration = milliseconds / 1000.0;
        this.pauseSign = false;
        errStr = null;
        playbackSoundThread = new Thread(this);
        playbackSoundThread.setName("Playback");
        playbackSoundThread.start();
    }

    public void stop() {
        synchronized (PlaybackSound.this) {
	        if (line != null && !line.isRunning()) {
	            line.start();
            }
            pauseSign = false;
            PlaybackSound.this.notifyAll();
            playbackSoundThread = null;
        }
    }
    
    public void pause() {
        synchronized (PlaybackSound.this) {
	        if (line != null && line.isRunning()) {
	            line.stop();
	            pauseSign = true;
	        }
        }
    }
    
    public void resume() {
        synchronized (PlaybackSound.this) {
	        if (line != null && !line.isRunning()) {
	            line.start();
	            pauseSign = false;
	            PlaybackSound.this.notifyAll();
	        }
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

        AudioFormat format = audioInputStream.getFormat();
        
        // define the required attributes for our line, 
        // and make sure a compatible line is supported.

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, 
            format);
        if (!AudioSystem.isLineSupported(info)) {
            shutDown("Line matching " + info + " not supported.");
            return;
        }

        // get and open the source data line for playback.

        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format, bufSize);
        } catch (LineUnavailableException ex) { 
            shutDown("Unable to open the line: " + ex);
            return;
        }

        // play back the captured audio data

        int frameSizeInBytes = format.getFrameSize();
        int bufferLengthInFrames = line.getBufferSize() / 8;
        int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
        byte[] data = new byte[bufferLengthInBytes];
        int numBytesRead = 0;

        // start the source data line
        line.start();

        while (playbackSoundThread != null) {
            try {
                synchronized (this) {
                    while (this.pauseSign == true) {
                        this.wait();
                    }
                    if ((numBytesRead = audioInputStream.read(data)) == -1) {
                        break;
                    }
                    int numBytesRemaining = numBytesRead;
                    while (numBytesRemaining > 0 ) {
                        numBytesRemaining -= line.write(data, 0, numBytesRemaining);
                    }
                }
                if (listener != null) {
                    listener.processingPlaybackSound(this.filename2, ((long)(line.getMicrosecondPosition() / 1000)) / 1000.0);
                }
            } catch (Exception e) {
                shutDown("Error during playback: " + e);
                break;
            }
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
        
        if (listener != null) {
            listener.stopPlaybackSound();
        }
    }
    
    public SourceDataLine getSourceDataLine() {
    	return line;
    }
} // End class Playback
