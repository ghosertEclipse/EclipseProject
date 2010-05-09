package test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

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
    
    // private double duration = 0;

    private TargetDataLine line = null;
    
    private Thread captureSoundThread = null;
    
    private Thread saveSoundThread = null;
    
    private LinkedList dataList = new LinkedList();
    
    private boolean pauseSign = false;
    
    public CaptureSound(String filename, AudioFormat audioFormat, AudioFileFormat.Type filetype) {
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
        dataList.clear();
        this.pauseSign = false;
        errStr = null;
        captureSoundThread = new Thread(this);
        captureSoundThread.setName("Capture Sound Thread");
        captureSoundThread.start();
        saveSoundThread = new Thread() {
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

    public void stop() {
        synchronized (CaptureSound.this) {
            pauseSign = false;
            CaptureSound.this.notifyAll();
            captureSoundThread = null;
        }
    }
    
    public void pause() {
        synchronized (CaptureSound.this) {
	        if (line != null && line.isRunning()) {
	            line.stop();
	            pauseSign = true;
	        }
        }
    }
    
    public void resume() {
        synchronized (CaptureSound.this) {
	        if (line != null && !line.isRunning()) {
	            line.start();
	            pauseSign = false;
                CaptureSound.this.notifyAll();
	        }
        }
    }
    
    private void shutDown(String message) {
        if ((errStr = message) != null && captureSoundThread != null) {
            captureSoundThread = null;
            System.err.println(errStr);
        }
    }

    public void run() {

        // duration = 0;
        
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

        // captured audio data
        int frameSizeInBytes = audioFormat.getFrameSize();
        int bufferLengthInFrames = line.getBufferSize() / 8;
        int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
        byte[] data = new byte[bufferLengthInBytes];
        int numBytesRead;
        
        line.start();

        while (captureSoundThread != null) {
            synchronized (this) {
                while (this.pauseSign == true) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if((numBytesRead = line.read(data, 0, bufferLengthInBytes)) == -1) {
                    break;
                }
            }

            byte[] datas = new byte[numBytesRead];
            System.arraycopy(data, 0, datas, 0, numBytesRead);
            if (numBytesRead > 0) {
	            synchronized (this) {
	                dataList.add(datas);
	                this.notifyAll();
	            }
            }
        }

        // we reached the end of the stream.  stop and close the line.
        line.stop();
        line.close();
        line = null;

        // long milliseconds = (long)((audioInputStream.getFrameLength() * 1000) / format.getFrameRate());
        // duration = milliseconds / 1000.0;
    }
    
    public TargetDataLine getTargetDataLine() {
        return this.line;
    }

    public String getFilename() {
        return filename;
    }
    
    private class RecorderAudioInputStream extends AudioInputStream {
        
        private byte[] datas = null;
        
        private boolean isFinished = false;
        
        public RecorderAudioInputStream() {
            super(new ByteArrayInputStream(new byte[0]), audioFormat, AudioSystem.NOT_SPECIFIED);
        }
        
        public int read() throws IOException {
            throw new IOException("illegal call to RecorderAudioInputStream.read()!");
        }

        // this method is blocking
        public int read(byte[] b, int off, int len) throws IOException {
            if (datas != null) {
                return this.readImpl(b, off, len);
            }
            if (this.isFinished) return -1;
            synchronized (CaptureSound.this) {
                while (dataList.size() <= 0) {
                    // finish thread handler
                    if (CaptureSound.this.captureSoundThread == null) {
                        this.isFinished = true;
                        if (dataList.size() <= 0) return -1;
                        // all handler here later............................................// modify later
                        datas = (byte[]) dataList.removeFirst();
                        return this.readImpl(b, off, len);
                    }
                    try {
                        CaptureSound.this.wait();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                datas = (byte[]) dataList.removeFirst();
            }
            return this.readImpl(b, off, len);
        }

        public int read(byte[] b) throws IOException {
            return read(b,0,b.length);
        }
        
        public int readImpl(byte[] b, int off, int len) {
            if (len >= datas.length) {
                System.arraycopy(datas, 0, b, off, datas.length);
                int length = datas.length;
                datas = null;
                return length;
            } else {
                System.arraycopy(datas, 0, b, off, len);
                int length = datas.length - len;
                byte[] tempDatas = new byte[length];
                System.arraycopy(datas, len, tempDatas, 0, length);
                datas = tempDatas;
                return len;
            }
        }
    }
}


