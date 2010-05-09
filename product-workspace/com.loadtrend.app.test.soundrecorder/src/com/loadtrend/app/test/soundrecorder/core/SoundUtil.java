package com.loadtrend.app.test.soundrecorder.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import javazoom.jl.converter.Converter;
import javazoom.jl.converter.Converter.ProgressListener;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.Obuffer;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.tritonus.share.sampled.AudioFileTypes;
import org.tritonus.share.sampled.Encodings;

import com.loadtrend.app.test.soundrecorder.ui.dialog.ProgressBarDialog;
import com.loadtrend.app.test.soundrecorder.util.ColorUtil;
import com.loadtrend.app.test.soundrecorder.util.PreferenceUtil;

public class SoundUtil {
	
	private AudioInputStream audioInputStream = null;
	
	private double duration = 0.0f;
	
	private byte[] paintBytes = null;

    private String filename = null;
    
    private AudioFormat audioFormat = null;
    
    private String tempFilename = null;
    
    private static int tempId = 0;
    
    public SoundUtil(String filename) {
        this.filename = filename;
    }

    public boolean importSound() {
    	try {
	        // Save the wave file to temp path with specified temp id filename.
    		this.tempFilename = this.getNextTempIdFileName();
	        
	        if (this.filename.toLowerCase().endsWith(".wav")) {
	        	boolean isSuccess =  this.copyWavFile(this.filename, this.tempFilename, "Importing wav file, please waiting ...", "Import");
	        	if (!isSuccess) return false;
	        }
    		
	        if (this.filename.toLowerCase().endsWith(".mp3")) {
		        // Begin to convert mp3 to wav
				File f = new File(this.filename);
	            Bitstream m_bitstream = new Bitstream(new FileInputStream(f));
	            Header m_header = m_bitstream.readFrame();
	            int mediaLength = (int)f.length();
	            final int mp3Frames = (mediaLength == AudioSystem.NOT_SPECIFIED ? 0 : m_header.max_number_of_frames(mediaLength));
	            
				ProgressBarDialog progressBarDialog = new ProgressBarDialog(Display.getDefault().getActiveShell()) {
					public void initGuage() {
						    this.setExecuteTime(100);
						    this.setMayCancel(true);
						    this.setProcessMessage("Importing MP3 file, please waiting ...");
						    this.setShellTitle("Import");
					}
					protected String process(int times) {
						try {
							if (times > 1) return "";
							Converter conv = new Converter();
							ProgressListener listener = new ProgressListener() {
								public boolean converterException(Throwable t) {
									isClosed = true;
									return false;
								}
				
								public void converterUpdate(int updateID, int param1, int param2) {
								}
				
								public void decodedFrame(int frameNo, Header header, Obuffer o) {
								}
				
								public void parsedFrame(int frameNo, Header header) {
								}
				
								public void readFrame(int frameNo, Header header) {
									try {
										if (isClosed == true) throw new IllegalStateException();
										final int i = (int) Math.round(frameNo * 1.0 / mp3Frames * 100);
									    final String message = "Processing " + i + "%";
									    Display.getDefault().syncExec(new Runnable() {
											public void run() {
									            processMessageLabel.setText(message);
									            progressBar.setSelection(i);
											}
									    });
									} catch (Exception e) {
										throw new IllegalStateException("abort convert");
									}
								}
							};
				            conv.convert(filename, tempFilename, listener);
						    return "";
						} catch (Exception e) {
							super.isClosed = true;
							e.printStackTrace();
						}
					    return "";
					}
				};
				progressBarDialog.initGuage();
				progressBarDialog.open();
				if (progressBarDialog.isClosed) return false;
	        }
	        
	        if (!new File(tempFilename).exists()) return false;
	        
			// load the converted wav file.
			this.resetAudioInputStream();
		} catch (JavaLayerException e) {
			System.err.println("Convertion failure: " + e);
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
        return true;
    }
    
    public boolean outputSound(final String inputFilename, final String outputFilename) {
    	try {
	        if (outputFilename.toLowerCase().endsWith(".wav")) {
	        	boolean isSuccess = this.copyWavFile(inputFilename, outputFilename, "Outputing wav file, please waiting ...", "Outport");
	        	if (!isSuccess) return false;
	        }
    		
	        if (outputFilename.toLowerCase().endsWith(".mp3")) {
				File file = new File(inputFilename);
				final long totalSize = file.length();
				final AudioInputStream originalAudioInputStream = AudioSystem.getAudioInputStream(file);
				ProgressBarDialog progressBarDialog = new ProgressBarDialog(Display.getDefault().getActiveShell()) {
					public void initGuage() {
						    this.setExecuteTime(100);
						    this.setMayCancel(true);
						    this.setProcessMessage("Outputing MP3 file, please waiting ...");
						    this.setShellTitle("Output");
					}
					protected String process(int times) {
						try {
							if (times > 1) return "";
				            class RecorderAudioInputStream extends AudioInputStream {
				            	private long currentSize = 0;
				                public RecorderAudioInputStream() {
			                        super(new ByteArrayInputStream(new byte[0]), originalAudioInputStream.getFormat(), AudioSystem.NOT_SPECIFIED);
				                }
				                
				                public int read() throws IOException {
				                    throw new IOException("illegal call to RecorderAudioInputStream.read()!");
				                }

				                // this method is blocking
				                public int read(byte[] b, int off, int len) throws IOException {
									if (isClosed == true) return -1;
									int length = originalAudioInputStream.read(b, off, len);
									if (length == -1) return -1;
									currentSize = currentSize + length;
									final int i = (int) Math.round(currentSize * 1.0 / totalSize * 100);
								    final String message = "Processing " + i + "%";
								    Display.getDefault().syncExec(new Runnable() {
										public void run() {
								            processMessageLabel.setText(message);
								            progressBar.setSelection(i);
										}
								    });
				                	return length;
				                }

				                public int read(byte[] b) throws IOException {
				                    return read(b,0,b.length);
				                }
				            }
			                AudioInputStream audioInputStream = new RecorderAudioInputStream();
		                	// convert pcm to mp3 AudioInputStream.
	                        audioInputStream = AudioSystem.getAudioInputStream(Encodings.getEncoding("MPEG1L3"), audioInputStream);
			                AudioSystem.write(audioInputStream, AudioFileTypes.getType("MP3", "mp3"), new File(outputFilename));
			                audioInputStream.close();
						    return "";
						} catch (Exception e) {
							super.isClosed = true;
							e.printStackTrace();
						}
					    return "";
					}
				};
				progressBarDialog.initGuage();
				progressBarDialog.open();
				originalAudioInputStream.close();
				if (progressBarDialog.isClosed) return false;
	        }
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
        return true;
    }
    
    private boolean copyWavFile(String inputFilename, String outputFilename, final String message, final String title) throws Exception {
		final FileInputStream fileInputStream = new FileInputStream(inputFilename);
		final FileOutputStream fileOutputStream = new FileOutputStream(outputFilename);
		long totalSize = new File(inputFilename).length();
		final byte[] bytes = new byte[fileInputStream.available() / 10];
		final int totalTimes = (int) (totalSize / bytes.length) + 1;
		ProgressBarDialog progressBarDialog = new ProgressBarDialog(Display.getDefault().getActiveShell()) {
			public void initGuage() {
				    this.setExecuteTime(totalTimes);
				    this.setMayCancel(true);
				    this.setProcessMessage(message);
				    this.setShellTitle(title);
			}
			protected String process(int times) {
				try {
					int numRead = fileInputStream.read(bytes);
					if (numRead != -1) {
						fileOutputStream.write(bytes, 0, numRead);
						return "Processing " + String.valueOf(Math.round(times * 1.0 / totalTimes * 100) + "%");
					}
				} catch (Exception e) {
					super.isClosed = true;
					e.printStackTrace();
				}
			    return "";
			}
		};
		progressBarDialog.initGuage();
		progressBarDialog.open();
		fileOutputStream.close();
		fileInputStream.close();
		if (progressBarDialog.isClosed) return false;
		return true;
    }
    
    /**
     * Invoke this after never use the instance of SoundUtil.
     */
    public void releaseResource() {
    	if (this.audioInputStream != null) {
    		try {
				this.audioInputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    
    /**
	 * @return Returns the duration.
	 */
	public double getDuration() {
		return duration;
	}

	/**
	 * @return Returns the paintBytes.
	 */
	public byte[] getPaintBytes() {
		return paintBytes;
	}
	
	/**
	 * @return Returns the audioFormat.
	 */
	public AudioFormat getAudioFormat() {
		return audioFormat;
	}
	
	public String getTempFilename() {
		return tempFilename;
	}
	
	/**
	 * The filename that imported.
	 * @return
	 */
	public String getFilename() {
		return filename;
	}

	/**
     * Convert the audio bytes to the bytes can be paint. from -128 to 127
     * @param audioBytes
     * @return
     */
    public static byte[] getPaintBytes(AudioFormat audioFormat, byte[] audioBytes, int numBytesRead) {
        byte[] paintBytes = null;
        if (audioFormat.getSampleSizeInBits() == 16) {
             int nlengthInSamples = numBytesRead / 2;
             paintBytes = new byte[nlengthInSamples];
             if (audioFormat.isBigEndian()) {
                for (int i = 0; i < nlengthInSamples; i++) {
                     /* First byte is MSB (high order) */
                     int MSB = (int) audioBytes[2*i];
                     /* Second byte is LSB (low order) */
                     int LSB = (int) audioBytes[2*i+1];
                     // Combine two bytes(high order one and low order one) in one int
                     int tempByte = MSB << 8 | (255 & LSB);
                     // Convert one int to one byte
                     paintBytes[i] = (byte) (128 * tempByte / 32768 );
                 }
             } else {
                 for (int i = 0; i < nlengthInSamples; i++) {
                     /* First byte is LSB (low order) */
                     int LSB = (int) audioBytes[2*i];
                     /* Second byte is MSB (high order) */
                     int MSB = (int) audioBytes[2*i+1];
                     // Combine two bytes(high order one and low order one) in one int
                     int tempByte = MSB << 8 | (255 & LSB);
                     // Convert one int to one byte
                     paintBytes[i] = (byte) (128 * tempByte / 32768 );
                 }
             }
         } else if (audioFormat.getSampleSizeInBits() == 8) {
             int nlengthInSamples = numBytesRead;
             paintBytes = new byte[nlengthInSamples];
             if (audioFormat.getEncoding().toString().startsWith("PCM_SIGN")) {
                 for (int i = 0; i < numBytesRead; i++) {
                     paintBytes[i] = audioBytes[i];
                 }
             } else {
                 for (int i = 0; i < numBytesRead; i++) {
                     paintBytes[i] = (byte) (audioBytes[i] - 128);
                 }
             }
        }
        return paintBytes;
    }
    
    /**
     * Paint sound on canvas here. set paintBytes as null for clearing the canvas with specified backGroundColor
     * @param canvas
     * @param bufferImage
     * @param colors color[0]: backGroundColor, color[1]: foreGroundColor
     * @param paintBytes paintBytes, null for clear the canvas.
     * @param channels
     * @param silencePercent between 0.0 - 1.0 plain line define. input 0 to skip judgement (sometime for playback)
     * @return Whether the paint line plain: means whether silence duration, true plain, otherwise false.
     */
    public static boolean paintSound(Canvas canvas, Image bufferImage, Color[] colors, byte[] paintBytes, int channels, double silencePercent) {
        int w = canvas.getSize().x;
        int h = canvas.getSize().y;
        Compare compare = new Compare();
        // specified the number of points will be paint to the canvas.
        // default one is equals to w.
        int pointNumbers = w; 
        double rate = w * 1.0 / pointNumbers;
        int frames_per_pixel = paintBytes == null ? 0 : paintBytes.length / channels / pointNumbers;
        int[] pointArray = paintBytes == null ? null : new int[2 * pointNumbers];
        for (int x = 0; x < pointNumbers && paintBytes != null; x++) {
            int idx = (frames_per_pixel * channels * x);
            int y = (h * (128 - paintBytes[idx]) / 256);
            pointArray[2 * x] = (int) (rate * x);
            pointArray[(2 * x) + 1] = y;
            
            // whether pain line judgement
            if (silencePercent > 0) {
            	compare.compare(paintBytes[idx]);
            }
        }
        GC imageGc = new GC(bufferImage);
        imageGc.setBackground(colors[0]);
        imageGc.fillRectangle(0, 0, bufferImage.getImageData().width, bufferImage.getImageData().height);
        if (paintBytes != null) {
            imageGc.setForeground(colors[1]);
            imageGc.drawPolyline(pointArray);
        }
        imageGc.dispose();
        GC canvasGC = new GC(canvas);
        canvasGC.drawImage(bufferImage, 0, 0);
        canvasGC.dispose();
        return silencePercent > 0 ? compare.isPaintLine(silencePercent) : false;
    }
    
    private static class Compare {
    	private byte minh = 0;
    	private byte maxh = 0;
    	private boolean initial = false;
    	public void compare(byte y) {
    		if (initial == false) {
    			minh = y;
    			maxh = y;
    			initial = true;
    		} else {
	    		if (minh > y) minh = y;
	    		if (maxh < y) maxh = y;
    		}
    	}
    	public boolean isPaintLine(double silencePercent) {
    		if ((maxh - minh) >= silencePercent * 255) {
    			return false;
    		} else {
    			return true;
    		}
    	}
    }
    
    /**
     * Paint sound on canvas here. set paintBytes as null for clearing the canvas with specified backGroundColor
     * @param bufferImage
     * @param colors color[0]: backGroundColor, color[1]: left channel foreGroundColor color[2]: right channel foreGroundColor
     */
    public void paintSound2(Image bufferImage, int realImageWidth, double startTimePosition, double endTimePosition, Color[] colors) {
    	
    	this.resetAudioInputStream();
    	
      	long startDataPosition = this.getDataPosition(startTimePosition);
    	long endDataPosition = this.getDataPosition(endTimePosition);
	    long numBytesRemaining = 0;
        long paintBytesLength = endDataPosition - startDataPosition;
		int channels = this.audioFormat.getChannels();
		if (this.audioFormat.getSampleSizeInBits() == 16) paintBytesLength = paintBytesLength / 2;
		
        int w = realImageWidth;
        int h = bufferImage.getBounds().height;
        // specified the number of points will be paint to the canvas.
        // default one is equals to w.
        int pointNumbers = w; 
        double rate = w * 1.0 / pointNumbers;
        int frames_per_pixel = (int) (paintBytesLength / channels / pointNumbers);
      	byte[] audioBytes = new byte[frames_per_pixel * channels * (this.audioFormat.getSampleSizeInBits() / 8)];
      	
      	// skip the data before start time position
      	try {
	        while (true) {
	            if (numBytesRemaining >= startDataPosition) break;
	            int i = audioInputStream.read(audioBytes);
	            numBytesRemaining += i;
	        }
      	} catch (IOException e) {
        	e.printStackTrace();
        }
        
        GC imageGc = new GC(bufferImage);
        imageGc.setBackground(colors[0]);
        imageGc.fillRectangle(0, 0, bufferImage.getImageData().width, bufferImage.getImageData().height);
        for (int i = 0; i < pointNumbers; i++) {
            int leftMaxY = 0;
            int leftMinY = h / 2;
            int rightMaxY = 0;
            int rightMinY = h / 2;
    		try {
				audioInputStream.read(audioBytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
        	byte[] paintBytes = SoundUtil.getPaintBytes(this.audioFormat, audioBytes, audioBytes.length);
        	for (int idx = 0; idx < paintBytes.length; idx++) {
            	int y = (h / 2 * (128 - paintBytes[idx]) / 256);
            	leftMaxY = leftMaxY > y ? leftMaxY : y;
            	leftMinY = leftMinY < y ? leftMinY : y;
            	if (channels == 2) {
	            	y = (h / 2 * (128 - paintBytes[++idx]) / 256);
	            	rightMaxY = rightMaxY > y ? rightMaxY : y;
	            	rightMinY = rightMinY < y ? rightMinY : y;
            	}
        	}
            int x = (int) (rate * i);
            imageGc.setForeground(colors[1]);
            imageGc.drawLine(x, leftMinY, x, leftMaxY);
            if (channels == 2) {
                imageGc.setForeground(colors[2]);
                imageGc.drawLine(x, rightMinY + h / 2, x, rightMaxY + h / 2);
            }
        }
        imageGc.setForeground(ColorUtil.getColor(ColorUtil.blackColor));
        imageGc.drawLine(0, h / 2, w, h / 2);
        imageGc.drawString("Left channel", 0, 0, true);
        imageGc.drawString("Right channel", 0, h / 2, true);
        
        imageGc.dispose();
    }
    
    public String cutSnippet(double startTimePosition, double endTimePosition) {
    	
    	this.resetAudioInputStream();
    	
    	long startDataPosition = this.getDataPosition(startTimePosition);
    	long endDataPosition = this.getDataPosition(endTimePosition);
    	// Get 0.1s frame length
    	int frameLength = (int) (this.audioFormat.getFrameRate() * 0.1);
    	byte[] data = new byte[frameLength * this.audioFormat.getFrameSize()];
	    long numBytesRemaining = 0;
        try {
        	startDataPosition = startDataPosition / data.length * data.length;
        	endDataPosition = endDataPosition / data.length * data.length;
        	long slipFileAudioDataLength = endDataPosition - startDataPosition;
        	long tempFileAudioDataLength = startDataPosition + this.getDataPosition(this.duration) - endDataPosition;
        	// write the wave head firstly.
        	String newTempFileName = this.getNextTempIdFileName();
        	AudioInputStream tempAudioInputStream = new AudioInputStream(new ByteArrayInputStream(new byte[0]), this.audioFormat, tempFileAudioDataLength / this.audioFormat.getFrameSize());
        	AudioSystem.write(tempAudioInputStream, AudioFileFormat.Type.WAVE, new File(newTempFileName));
        	tempAudioInputStream.close();
        	FileOutputStream tempFileOutputStream = new FileOutputStream(newTempFileName, true);
        	
        	// write the wave head firstly.
        	String splitFileName = this.getNextTempIdFileName();
        	AudioInputStream splitAudioInputStream = new AudioInputStream(new ByteArrayInputStream(new byte[0]), this.audioFormat, slipFileAudioDataLength / this.audioFormat.getFrameSize());
        	AudioSystem.write(splitAudioInputStream, AudioFileFormat.Type.WAVE, new File(splitFileName));
        	splitAudioInputStream.close();
        	FileOutputStream splitFileOutputStream = new FileOutputStream(splitFileName, true);
        	
        	// create new temp file.
	        while (true) {
	            if (numBytesRemaining >= startDataPosition) break;
	            int i = audioInputStream.read(data);
	            tempFileOutputStream.write(data, 0, i);
	            numBytesRemaining += i;
	        }
	        // create splitted temp file.
			while (true) {
			    if (numBytesRemaining >= endDataPosition) break;
				int i = audioInputStream.read(data);
				if (i == -1) break;
	            splitFileOutputStream.write(data, 0, i);
			    numBytesRemaining += i;
			}
        	// create new temp file.
	        while (true) {
	            if (numBytesRemaining >= this.getDataPosition(this.duration)) break;
	            int i = audioInputStream.read(data);
	            tempFileOutputStream.write(data, 0, i);
	            numBytesRemaining += i;
	        }
//	        System.out.println(tempFileAudioDataLength);
//	        System.out.println(new FileInputStream(newTempFileName).available());
//	        System.out.println(slipFileAudioDataLength);
//	        System.out.println(new FileInputStream(splitFileName).available());
//	        System.out.println(new FileInputStream(newTempFileName).available() + new FileInputStream(splitFileName).available());
//	        System.out.println(new FileInputStream(tempFile).available());
	        tempFileOutputStream.close();
	        splitFileOutputStream.close();
	        this.audioInputStream.close();
	        new File(this.tempFilename).delete();
	        
			this.tempFilename = newTempFileName;
			this.resetAudioInputStream();
			
			return splitFileName;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
    }
    
    public String copySnippet(double startTimePosition, double endTimePosition) {
    	
    	this.resetAudioInputStream();
    	
    	long startDataPosition = this.getDataPosition(startTimePosition);
    	long endDataPosition = this.getDataPosition(endTimePosition);
    	// Get 0.1s frame length
    	int frameLength = (int) (this.audioFormat.getFrameRate() * 0.1);
    	byte[] data = new byte[frameLength * this.audioFormat.getFrameSize()];
	    long numBytesRemaining = 0;
        try {
        	startDataPosition = startDataPosition / data.length * data.length;
        	endDataPosition = endDataPosition / data.length * data.length;
        	long slipFileAudioDataLength = endDataPosition - startDataPosition;
        	
        	// write the wave head firstly.
        	String splitFileName = this.getNextTempIdFileName();
        	AudioInputStream splitAudioInputStream = new AudioInputStream(new ByteArrayInputStream(new byte[0]), this.audioFormat, slipFileAudioDataLength / this.audioFormat.getFrameSize());
        	AudioSystem.write(splitAudioInputStream, AudioFileFormat.Type.WAVE, new File(splitFileName));
        	splitAudioInputStream.close();
        	FileOutputStream splitFileOutputStream = new FileOutputStream(splitFileName, true);
        	
        	// skip new temp file.
	        while (true) {
	            if (numBytesRemaining >= startDataPosition) break;
	            int i = audioInputStream.read(data);
	            numBytesRemaining += i;
	        }
	        // create splitted temp file.
			while (true) {
			    if (numBytesRemaining >= endDataPosition) break;
				int i = audioInputStream.read(data);
				if (i == -1) break;
	            splitFileOutputStream.write(data, 0, i);
			    numBytesRemaining += i;
			}
	        splitFileOutputStream.close();
	        this.audioInputStream.close();
	        
	        return splitFileName;
	        
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * Return the duration of the paste file.
     * @param startTimePosition
     * @return
     */
    public double pasteSnippet(double startTimePosition, String filename) {
    	
    	this.resetAudioInputStream();

    	// Get 0.1s frame length
    	int frameLength = (int) (this.audioFormat.getFrameRate() * 0.1);
    	byte[] data = new byte[frameLength * this.audioFormat.getFrameSize()];
	    long numBytesRemaining = 0;
        try {
        	// write the wave head firstly.
        	AudioInputStream splitAudioInputStream = AudioSystem.getAudioInputStream(new File(filename));
        	long startDataPosition = this.getDataPosition(startTimePosition);
        	long endDataPosition = startDataPosition + splitAudioInputStream.available();
        	startDataPosition = startDataPosition / data.length * data.length;
        	endDataPosition = endDataPosition / data.length * data.length;
        	long tempFileAudioDataLength = this.getDataPosition(this.duration) + endDataPosition - startDataPosition;
        	// write the wave head firstly.
        	String newTempFileName = this.getNextTempIdFileName();
        	AudioInputStream tempAudioInputStream = new AudioInputStream(new ByteArrayInputStream(new byte[0]), this.audioFormat, tempFileAudioDataLength / this.audioFormat.getFrameSize());
        	AudioSystem.write(tempAudioInputStream, AudioFileFormat.Type.WAVE, new File(newTempFileName));
        	tempAudioInputStream.close();
        	FileOutputStream tempFileOutputStream = new FileOutputStream(newTempFileName, true);
        	
        	// create new temp file.
	        while (true) {
	            if (numBytesRemaining >= startDataPosition) break;
	            int i = audioInputStream.read(data);
	            tempFileOutputStream.write(data, 0, i);
	            numBytesRemaining += i;
	        }
	        // add splitted temp file.
			while (true) {
			    if (numBytesRemaining >= endDataPosition) break;
				int i = splitAudioInputStream.read(data);
				if (i == -1) break;
				tempFileOutputStream.write(data, 0, i);
			    numBytesRemaining += i;
			}
        	// add new temp file.
	        while (true) {
	            if (numBytesRemaining >= tempFileAudioDataLength) break;
	            int i = audioInputStream.read(data);
	            tempFileOutputStream.write(data, 0, i);
	            numBytesRemaining += i;
	        }
	        tempFileOutputStream.close();
    	    splitAudioInputStream.close();
	        this.audioInputStream.close();
	        new File(this.tempFilename).delete();
	        
			this.tempFilename = newTempFileName;
			this.resetAudioInputStream();
			
        	return (endDataPosition - startDataPosition) * 1.0 / this.audioFormat.getFrameSize() / this.audioFormat.getFrameRate();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
    }
    
	private long getDataPosition(double timePosition) {
		long dataPosition = (long) (timePosition * audioFormat.getFrameSize() * audioFormat.getFrameRate());
       	// get a integer frame length bytes, if use the dataPosition directly, error happens when invoke line.write
		return dataPosition / audioFormat.getFrameSize() * audioFormat.getFrameSize();
	}
	
	private String getNextTempIdFileName() {
		return PreferenceUtil.TEMP_PATH + (tempId++) + ".wav";
	}
	
	/**
	 * Let the audioInputStream reset.
	 */
	private void resetAudioInputStream() {
		try {
			this.releaseResource();
			this.audioInputStream = AudioSystem.getAudioInputStream(new File(tempFilename));
            this.audioFormat = this.audioInputStream.getFormat();
            this.duration = this.audioInputStream.getFrameLength() * 1.0 / this.audioFormat.getFrameRate();
		} catch (UnsupportedAudioFileException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
    
    /**
     * Design for the check pc mixer dialog.
     * @param paintBytes
     * @param channels
     * @param silencePercent
     * @return
     */
    public static boolean isPlainLine(byte[] paintBytes, int channels, double silencePercent) {
        Compare compare = new Compare();
        int length = paintBytes.length / channels;
        for (int x = 0; x < length && paintBytes != null; x++) {
            int idx = channels * x;
            
            // whether pain line judgement
            if (silencePercent > 0) {
            	compare.compare(paintBytes[idx]);
            }
        }
        return silencePercent > 0 ? compare.isPaintLine(silencePercent) : false;
    }
    
    /**
     * Get the specified part of paint data with number, if no enough number return null,
     * Or remove specified number of paint bytes from byteArrayOutputStream and return it.
     */
	public static byte[] getPartOfPaintData(ByteArrayOutputStream byteArrayOutputStream, int numOfPartPaintData) {
		if (byteArrayOutputStream.size() < numOfPartPaintData) return null;
		byte[] partOfPaintData = new byte[numOfPartPaintData];
		byte[] totalBytes = byteArrayOutputStream.toByteArray();
		System.arraycopy(totalBytes, 0, partOfPaintData, 0, numOfPartPaintData);
		byteArrayOutputStream.reset();
		byteArrayOutputStream.write(totalBytes, numOfPartPaintData, totalBytes.length - numOfPartPaintData);
		return partOfPaintData;
	}
}

//protected static void loadInfo(AudioFileFormat aff) 
//throws Exception {
//System.out.println("[MP3 info] ---------------------");
//String type = aff.getType().toString();
//if (!type.equalsIgnoreCase("mp3")) 
//    throw new Exception("Not MP3 audio format");
//if (aff instanceof TAudioFileFormat) {
//    Map props = ((TAudioFileFormat) aff).properties();
//    System.out.println("title: "+props.get("title"));
//    System.out.println("author: "+props.get("author"));
//    System.out.println("album: "+props.get("album"));
//    if (props.containsKey("mp3.mode")) {
//        String channelsMode = "";
//        int mode = 
//            ((Integer)props.get("mp3.mode")).intValue();
//        if (mode==0) 
//            channelsMode = "Stereo";
//        else if (mode==1) 
//            channelsMode = "Joint Stereo"; 
//        else if (mode==2) 
//            channelsMode = "Dual Channel";
//        else if (mode==3) 
//            channelsMode = "Single Channel";
//        System.out.println("mode: "+channelsMode);
//    }
//    System.out.println
//        ("channels: "+props.get("mp3.channels"));
//    System.out.println
//        ("rate: "+props.get("mp3.frequency.hz"));
//    System.out.println("nominalbitrate: "+
//        props.get("mp3.bitrate.nominal.bps"));
//    System.out.println
//        ("layer: "+props.get("mp3.version.layer"));
//    if (props.containsKey("mp3.version.mpeg")) {
//        String version = (String)
//            props.get("mp3.version.mpeg");
//        if (version.equals("1")) 
//            version = "MPEG1"; 
//        else if (version.equals("2")) 
//            version = "MPEG2-LSF";
//        else if (version.equals("2.5")) 
//            version = "MPEG2.5-LSF";
//        System.out.println("version: "+version);
//    }
//    System.out.println("crc: "+props.get("mp3.crc"));
//    System.out.println("vbr: "+props.get("mp3.vbr"));
//    System.out.println
//        ("copyright: "+props.get("mp3.copyright"));
//    System.out.println
//        ("original: "+props.get("mp3.original"));
//    System.out.println
//        ("date: "+props.get("date"));
//    System.out.println
//        ("duration: "+props.get("duration"));
//    System.out.println
//        ("genre: "+props.get("mp3.id3tag.genre"));
//    System.out.println
//        ("track: "+props.get("mp3.id3tag.track"));        
//}    
//}
