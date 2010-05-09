package com.loadtrend.app.test.soundrecorder.ui;

import java.io.File;
import java.text.MessageFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.sound.sampled.AudioFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.Section;
import org.tritonus.share.sampled.AudioFileTypes;
import org.tritonus.share.sampled.Encodings;

import com.loadtrend.app.test.soundrecorder.core.CaptureSound;
import com.loadtrend.app.test.soundrecorder.core.CaptureSoundListener;
import com.loadtrend.app.test.soundrecorder.core.PlaybackSound;
import com.loadtrend.app.test.soundrecorder.core.PlaybackSoundListener;
import com.loadtrend.app.test.soundrecorder.core.SoundUtil;
import com.loadtrend.app.test.soundrecorder.info.RecordingResultInfo;
import com.loadtrend.app.test.soundrecorder.info.RecordingScheduleInfo;
import com.loadtrend.app.test.soundrecorder.ui.dialog.NoSoundDialog;
import com.loadtrend.app.test.soundrecorder.ui.dialog.StopRecordDialog;
import com.loadtrend.app.test.soundrecorder.util.FormatUtil;
import com.loadtrend.app.test.soundrecorder.util.IOOperation;
import com.loadtrend.app.test.soundrecorder.util.PreferenceUtil;
import com.loadtrend.app.test.soundrecorder.win32native.HotKeyHandler;
import com.loadtrend.app.test.soundrecorder.win32native.RegisterHotKey;

public class RecordingControlItem {

	private RecordingSection section = null;
	
	private CaptureSound captureSound = null;
	
	private PlaybackSound playbackSound = null;
	
	private CLabel lbRecord = null;
	
	private CLabel lbPause = null;
	
	private CLabel lbStop = null;
	
	private CLabel lbPlay = null;
	
	private CLabel lbEdit = null;
	
	private Mediator mediator = new Mediator();
    
    private CLabel lbStatusBar = null;
    
    private Canvas canvas = null;
    
    private Image bufferImage = null;
	
	private String currentFilename = null;
	
	private double currentDuration = 0;
	
	private Date startTime = null;
	
	private RecordingScheduleInfo info = null;
    
	public RecordingControlItem(RecordingSection section) {
		this.section = section;
	}
	
	public CTabItem createItem() {
        // recording information details tab item
		final CTabItem recording = new CTabItem(section.tabFolder, SWT.NONE);
		
		Form recordingForm = section.kit.createForm(section.tabFolder);
		recording.setControl(recordingForm);
		FormLayout layout = new FormLayout();
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		layout.spacing = 10;
		recordingForm.getBody().setLayout(layout);
		
		// Sample Graph section
		Section recordingGraph = section.kit.createSection(recordingForm.getBody(), Section.TITLE_BAR | Section.DESCRIPTION | Section.TREE_NODE);
		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(70, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		recordingGraph.setLayoutData(formData);
		recordingGraph.setText("Recording && Playback Sample Graph:");
		recordingGraph.setExpanded(true);
		recordingGraph.setTitleBarBackground(section.orangeColor);
		recordingGraph.setTitleBarGradientBackground(section.orangeColor);
		
        Composite recordingComposite = section.kit.createComposite(recordingGraph);
		recordingGraph.setClient(recordingComposite);
        FormLayout formLayout2 = new FormLayout();
        recordingComposite.setLayout(formLayout2);
        // Canvas
		// Canvas canvas = new Canvas(recordingComposite, SWT.BORDER);
		this.canvas = new Canvas(recordingComposite, SWT.DOUBLE_BUFFERED);
        FormData data = new FormData();
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(90, 0);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100);
        this.canvas.setLayoutData(data);
		this.canvas.setBackground(section.blackColor);
		// Create bufferImage
		this.canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				e.gc.drawImage(bufferImage, 0, 0);
			}
		});
		this.canvas.addControlListener(new ControlListener() {
			public void controlMoved(ControlEvent e) {
			}
			public void controlResized(ControlEvent e) {
		        PaletteData paletteData = new PaletteData(0, 0, 0);
		        ImageData imageData = new ImageData(canvas.getBounds().width, canvas.getBounds().height, 16, paletteData);
		        bufferImage = new Image(null, imageData);
			}
		});
		// Distroy bufferImage
        this.canvas.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                if (bufferImage != null && !bufferImage.isDisposed()) {
                    bufferImage.dispose();
                }
            }
        });
        
        // Status bar
        this.lbStatusBar = new CLabel(recordingComposite, SWT.RIGHT);
        data = new FormData();
        data.top = new FormAttachment(this.canvas);
        data.bottom = new FormAttachment(90, 20);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        lbStatusBar.setLayoutData(data);
        lbStatusBar.setBackground(section.blackColor);
        lbStatusBar.setForeground(section.whiteColor);
		
		// Operation section
		Section operationSection = section.kit.createSection(recordingForm.getBody(), Section.TITLE_BAR | Section.TREE_NODE);
		formData = new FormData();
		formData.top = new FormAttachment(recordingGraph);
		formData.bottom = new FormAttachment(100, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		operationSection.setLayoutData(formData);
		operationSection.setText("Recording && Playback Operation:");
		operationSection.setExpanded(true);
		operationSection.setTitleBarBackground(section.orangeColor);
		operationSection.setTitleBarGradientBackground(section.orangeColor);
		
		Composite operationComposite = section.kit.createComposite(operationSection);
		operationSection.setClient(operationComposite);
		FormLayout formLayout = new FormLayout();
		formLayout.marginHeight = 5;
		formLayout.marginWidth = 20;
		formLayout.spacing = 20;
		operationComposite.setLayout(formLayout);
		
		
		// record button
		this.lbRecord = section.createOperationButton(operationComposite, null, "Record", "images/btRecord.gif");
		this.addMouseListener(this.lbRecord);
		section.changeCLabelStatus(this.lbRecord, AbstractSharedSection.ENABLE_STATUS);
		
		// pause button
		this.lbPause = section.createOperationButton(operationComposite, this.lbRecord, "Pause", "images/btPause.gif");
		this.addMouseListener(this.lbPause);
		section.changeCLabelStatus(this.lbPause, AbstractSharedSection.DISABLE_STATUS);
		
		// stop button
		this.lbStop = section.createOperationButton(operationComposite, this.lbPause, "Stop", "images/btStop.gif");
		this.addMouseListener(this.lbStop);
		section.changeCLabelStatus(this.lbStop, AbstractSharedSection.DISABLE_STATUS);
		
		// play button
		this.lbPlay = section.createOperationButton(operationComposite, this.lbStop, "Play", "images/btPlay.gif");
		this.addMouseListener(this.lbPlay);
		section.changeCLabelStatus(this.lbPlay, AbstractSharedSection.DISABLE_STATUS);
		
		// edit button
		this.lbEdit = section.createOperationButton(operationComposite, this.lbPlay, "Edit", null);
		this.addMouseListener(this.lbEdit);
		section.changeCLabelStatus(this.lbEdit, AbstractSharedSection.DISABLE_STATUS);
        
        // register hotkey
		RegisterHotKey.registerHotKey(new String[] {PreferenceUtil.getValue(PreferenceUtil.RECORD_HOTKEY), PreferenceUtil.getValue(PreferenceUtil.PAUSE_HOTKEY),
				                                    PreferenceUtil.getValue(PreferenceUtil.STOP_HOTKEY), PreferenceUtil.getValue(PreferenceUtil.PLAYBACK_HOTKEY)},
				new HotKeyHandler() {
					public void handler(int virtualKeyCode) {
						switch (virtualKeyCode) {
							case 0:
								mediator.mouseUp(lbRecord);
								break;
							case 1:
								mediator.mouseUp(lbPause);
								break;
							case 2:
								mediator.mouseUp(lbStop);
								break;
							case 3:
								mediator.mouseUp(lbPlay);
								break;
							default:
								break;
						}
					}
				});
		return recording;
	}
	
	private void addMouseListener(final CLabel label) {
		label.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				mediator.mouseUp(label);
			}
		});
	}
	
	/**
	 * Design for the RecordingResultListItem invoke.
	 * @param filename
	 * @param duration
	 */
	public void playFile(final String filename, final double duration) {
		if (lbRecord.getData(AbstractSharedSection.STATUS_KEY) == AbstractSharedSection.RUNNING_STATUS) {
	    	NoSoundDialog box = new NoSoundDialog(Display.getDefault().getActiveShell());
	    	box.create("Prompt", "Stop recording before playback.", 1);
			return;
		}
		if (lbPlay.getData(AbstractSharedSection.STATUS_KEY) == AbstractSharedSection.RUNNING_STATUS) {
			this.mediator.mouseUp(this.lbStop);
			currentFilename = filename;
			currentDuration = duration;
			mediator.mouseUp(lbPlay);
			return;
		}
		currentFilename = filename;
		currentDuration = duration;
	    section.changeCLabelStatus(lbPlay, AbstractSharedSection.ENABLE_STATUS);
		mediator.mouseUp(lbPlay);
	}
	
	/**
	 * Design for SettingSection - schedule table list invoking.
	 * @param timerTask
	 * @return record duration
	 */
	public synchronized int record(final RecordingScheduleInfo info, boolean runNow) {
		int duration = 0;
		if (!runNow) {
			duration = info.getEndTime() - info.getStartTime();
			if (lbRecord.getData(AbstractSharedSection.STATUS_KEY) == AbstractSharedSection.RUNNING_STATUS) {
				duration = new StopRecordDialog(lbRecord.getShell()).create(info);
				if (duration <= 0) return duration;
			}
		}
		this.mediator.mouseUp(this.lbStop);
		this.mediator.mouseUp(this.mediator.new Action() {
			public void run() {
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
			            // open the browser.
	                    RecordingControlItem.this.browser(info);
					}
				});
				RecordingControlItem.this.mediator.doAction(RecordingControlItem.this.lbRecord, AbstractSharedSection.ENABLE_STATUS);
			}
		});
		return duration;
	}
	
	/**
	 * Design for SettingSection - schedule table list invoking.
	 */
	public void stopRecord() {
	    // stop the browser.
		this.browser(null);
		mediator.mouseUp(lbStop);
	}
	
	// control the Internet radio url Tabitem
	private void browser(RecordingScheduleInfo info) {
		this.info = info;
		if (info == null) {
			RecordingControlItem.this.section.soundRecorder.browser(null);
			return;
		}
	    if (info != null && !info.getUrl().trim().equals("")) {
	    	RecordingControlItem.this.section.soundRecorder.browser(info.getUrl());
	    }
	}
	
	private class Mediator {
		
		private LinkedList toDoList = new LinkedList();
		
		private abstract class Action {
			public abstract void run();
		}
		
		private Action action = null;
		
		private Thread capturePlaybackActionThread = new Thread("capturePlaybackActionThread") {
			public void run() {
				try {
					while (true) {
						synchronized (this) {
							while (toDoList.size() <= 0) {
								this.wait();
							}
							action = (Action) toDoList.removeFirst();
						}
						action.run();
					}
				} catch (InterruptedException e) {
				} finally {
					System.out.println("Finish capturePlaybackActionThread.");
				}
			}
		};
		
		public Mediator() {
			this.capturePlaybackActionThread.start();
		}
		
		public void mouseUp(final CLabel label) {
        	synchronized (capturePlaybackActionThread) {
        		toDoList.add(new Action() {
        			private Integer status = null;
					public void run() {
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
						        status = (Integer) label.getData(AbstractSharedSection.STATUS_KEY);
							}
						});
						doAction(label, status);
					}
        		});
        		capturePlaybackActionThread.notifyAll();
			}
		}
		
		public void mouseUp(Action action) {
        	synchronized (capturePlaybackActionThread) {
        		toDoList.add(action);
        		capturePlaybackActionThread.notifyAll();
			}
		}
		
		public void doAction(final CLabel label, Integer status) {
			
			// disable status
			if (status == AbstractSharedSection.DISABLE_STATUS) return;
			
			// running_status: just for pause action.
			if (status == AbstractSharedSection.RUNNING_STATUS) {
				if (label == lbPause) {
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							label.setText("Pause");
							section.changeCLabelStatus(label, AbstractSharedSection.ENABLE_STATUS);
						}
					});
                    // record & playback action;
                    if (captureSound != null) {
                        captureSound.resume();
                        return;
                    }
                    if (playbackSound != null) {
                        playbackSound.resume();
                        return;
                    }
				}
			}
			
			// enable status
			if (status == AbstractSharedSection.ENABLE_STATUS) {
				if (label == lbRecord) {
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
						    section.changeCLabelStatus(lbRecord, AbstractSharedSection.RUNNING_STATUS);
						    section.changeCLabelStatus(lbPause, AbstractSharedSection.ENABLE_STATUS);
						    section.changeCLabelStatus(lbStop, AbstractSharedSection.ENABLE_STATUS);
						    section.changeCLabelStatus(lbPlay, AbstractSharedSection.DISABLE_STATUS);
						    section.changeCLabelStatus(lbEdit, AbstractSharedSection.DISABLE_STATUS);
						}
					});
                    // record & playback action;
                    captureSound = Mediator.this.createCaptureSound();
                    captureSound.start();
                    startTime = new Date();
				    return;
				}
				if (label == lbPause) {
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							label.setText("Resume");
						    section.changeCLabelStatus(lbPause, AbstractSharedSection.RUNNING_STATUS);
						}
					});
                    // record & playback action;
                    if (captureSound != null) {
                        captureSound.pause();
                        return;
                    }
                    if (playbackSound != null) {
                        playbackSound.pause();
                        return;
                    }
				}
				if (label == lbStop) {
					// some code written for the scheduled record.
					if (RecordingControlItem.this.info != null) {
						synchronized (RecordingControlItem.this.info) {
							RecordingControlItem.this.info.setInterrupted(true);
						    RecordingControlItem.this.info.notify();
						}
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
						        RecordingControlItem.this.browser(null);
							}
						});
					}
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							section.changeCLabelStatus(lbRecord, AbstractSharedSection.ENABLE_STATUS);
							section.changeCLabelStatus(lbPause, AbstractSharedSection.DISABLE_STATUS);
							section.changeCLabelStatus(lbStop, AbstractSharedSection.DISABLE_STATUS);
							section.changeCLabelStatus(lbPlay, AbstractSharedSection.ENABLE_STATUS);
							section.changeCLabelStatus(lbEdit, AbstractSharedSection.ENABLE_STATUS);
							lbPause.setText("Pause");
						}
					});
                    // record & playback action;
                    if (captureSound != null) {
                        captureSound.stop();
                        currentDuration = captureSound.getDuration();
                        currentFilename = captureSound.getFilename();
                        // Save the recording result files.
                        RecordingResultInfo info = new RecordingResultInfo(startTime, currentDuration, currentFilename);
                        section.resultRecordingList.add(0, info);
                        IOOperation.writeObject(PreferenceUtil.RESULT_DATA, section.resultRecordingList);
                        captureSound = null;
                        return;
                    }
                    if (playbackSound != null) {
                        playbackSound.stop();
                        return;
                    }
				}
				if (label == lbPlay) {
					if (currentFilename != null) {
					    final File file = new File(currentFilename);
					    if (!file.exists()) {
					    	Display.getDefault().syncExec(new Runnable() {
								public void run() {
					    	        lbStatusBar.setText("Audio file " + file.getName() + " doesn't exist or is deleted.");
								}
					    	});
					    	return;
					    }
					}
			    	Display.getDefault().syncExec(new Runnable() {
						public void run() {
						    section.changeCLabelStatus(lbRecord, AbstractSharedSection.DISABLE_STATUS);
						    section.changeCLabelStatus(lbPause, AbstractSharedSection.ENABLE_STATUS);
						    section.changeCLabelStatus(lbStop, AbstractSharedSection.ENABLE_STATUS);
						    section.changeCLabelStatus(lbPlay, AbstractSharedSection.RUNNING_STATUS);
						    section.changeCLabelStatus(lbEdit, AbstractSharedSection.DISABLE_STATUS);
						}
			    	});
                    // record & playback action;
                    playbackSound = Mediator.this.createPlaybackSound(currentFilename, currentDuration);
                    playbackSound.start();
				    return;
				}
				if (label == lbEdit) {
					if (currentFilename != null) {
					    final File file = new File(currentFilename);
					    if (!file.exists()) {
					    	Display.getDefault().syncExec(new Runnable() {
								public void run() {
					    	        lbStatusBar.setText("Audio file " + file.getName() + " doesn't exist or is deleted.");
								}
					    	});
					    	return;
					    }
					}
                    // edit sound;
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
					        section.notifyToEdit(currentFilename);
						}
					});
				}
			}
		}
		
		private CaptureSound createCaptureSound() {
            CaptureSound captureSound = null;
			String recordFormat = PreferenceUtil.getValue(PreferenceUtil.RECORD_FORMAT);
			boolean noiseGateEnable = Boolean.valueOf(PreferenceUtil.getValue(PreferenceUtil.NOISE_GATE_ENABLE)).booleanValue();
            int silenceDurationDefine = noiseGateEnable ? Integer.parseInt(PreferenceUtil.getValue(PreferenceUtil.NOISE_GATE_DURATION).trim()) : 0;
            final double silencePercent = noiseGateEnable ? Integer.parseInt(PreferenceUtil.getValue(PreferenceUtil.NOISE_GATE_PERCENT)) * 0.01 : 0;
	
			// wav format
			if (recordFormat.equals("0")) {
		        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
		        float rate = Float.parseFloat(PreferenceUtil.SAMPLE_RATES[Integer.parseInt(PreferenceUtil.getValue(PreferenceUtil.SAMPLE_RATE))]); // 11025f, 16000f, 22050f, 44100f
		        int sampleSize = Integer.parseInt(PreferenceUtil.SAMPLE_BITS[Integer.parseInt(PreferenceUtil.getValue(PreferenceUtil.SAMPLE_BIT))]); // 8, 16
		        boolean bigEndian = false;
		        int channels = Integer.parseInt(PreferenceUtil.CHANNELS[Integer.parseInt(PreferenceUtil.getValue(PreferenceUtil.CHANNEL))]); // 1: mono; 2: stereo
		        AudioFormat format = new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize/8)*channels, rate, bigEndian);
                captureSound = new CaptureSound(FormatUtil.getFilename(".wav"), format, AudioFileTypes.WAVE, silenceDurationDefine);
			}
			// mp3 format
			if (recordFormat.equals("1")) {
		      AudioFormat.Encoding encoding = Encodings.getEncoding("MPEG1L3");
		      float rate = 44100f; 
		      int sampleSize = -1;
		      boolean bigEndian = false;
		      int channels = 2; // 1: mono; 2: stereo
		      AudioFormat format = new AudioFormat(encoding, rate, sampleSize, channels, -1, -1, bigEndian);
              captureSound = new CaptureSound(FormatUtil.getFilename(".mp3"), format, AudioFileTypes.getType("MP3", "mp3"), silenceDurationDefine);
              String quality = PreferenceUtil.MP3_QUALITYS[Integer.parseInt(PreferenceUtil.getValue(PreferenceUtil.MP3_QUALITY))];
              String bitRate = PreferenceUtil.MP3_BIT_RATES[Integer.parseInt(PreferenceUtil.getValue(PreferenceUtil.MP3_BIT_RATE))];
              boolean isVBR = bitRate.equals("-1");
              captureSound.encodeMP3Param(quality, bitRate, isVBR);
			}
            if (captureSound != null) {
            	final String filename = captureSound.getFilename();
                final String filename2 = filename.substring(filename.lastIndexOf(File.separator) + 1);
                captureSound.addCaptureSoundListener(new CaptureSoundListener() {
                    private boolean isSilence = false;
                    public void processingCaptureSound(final boolean isSilenceDuration, final double duration, final double position) {
                        Display.getDefault().syncExec(new Runnable() {
                            public void run() {
                                String description = MessageFormat.format("Recording  {0}  {1}",
                                                                           new String[] {filename2,
                                		                                                 FormatUtil.getFormatedTime(position, true)});
                                if (isSilenceDuration) {
                                	description = "Silence duration pause " + description;
                                }
                                if (info != null) {
                                	description = "Schedule: " + info + " " + description;
                                }
                                lbStatusBar.setText(description);
                                // Trial
//                                if (duration > 60) {
//                                    mediator.mouseUp(lbStop);
//                                    RecordingControlItem.this.section.tabFolder.setSelection(section.aboutTabItem);
//                                }
                            }
                        });
                    }

                    public void stopCaptureSound(final double duration, final long capturedAudioBytesLength) {
                        Display.getDefault().syncExec(new Runnable() {
                            public void run() {
                                String description = MessageFormat.format("Stop recording  {0}  {1}",
                                                                          new String[] {filename2,
                                		                                                FormatUtil.getFormatedTime(duration, true)});
                               lbStatusBar.setText(description);
                               // Clear the canvas.
                           	   SoundUtil.paintSound(canvas,
           			                                bufferImage,
           			                                new Color[] {section.blackColor,
           			                                section.darkOrangeColor},
           			                                null,
           			                                0,
                                                    0);
                            }
                        });
                    }
                    
                    public boolean paintSound(final byte[] paintBytes, final int channels) {
                        Display.getDefault().syncExec(new Runnable() {
                            public void run() {
                                isSilence = SoundUtil.paintSound(canvas,
                            			             bufferImage,
                            			             new Color[] {section.blackColor,
                            			                          section.darkOrangeColor},
                            			             paintBytes,
                            			             channels,
                                                     silencePercent); // 5% silence define.
                            }
                        });
                        return isSilence;
                    }
                });
            }
			return captureSound;
		}
		
		private PlaybackSound createPlaybackSound(String filename, final double duration) {
			PlaybackSound playbackSound = new PlaybackSound(filename);
            final String filename2 = filename.substring(filename.lastIndexOf(File.separator) + 1);
            playbackSound.addPlaybackSoundListener(new PlaybackSoundListener() {
                public void stopPlaybackSound(final boolean isAutoFinished) {
                    Display.getDefault().syncExec(new Runnable() {
                        public void run() {
                        	// Design for natural stop playing.
                        	if (isAutoFinished) mediator.mouseUp(lbStop);
        					//
                            String description = MessageFormat.format("Stop playback  {0}  {1}",
                                                                       new String[] {filename2,
                            		                                                 FormatUtil.getFormatedTime(duration, true)
                                                                                     });
                            lbStatusBar.setText(description);
                            // Clear the canvas.
                        	SoundUtil.paintSound(canvas,
        			                             bufferImage,
        			                             new Color[] {section.blackColor,
        			                             section.darkOrangeColor},
        			                             null,
        			                             0,
                                                 0);
                        }
                    });
                }
                public void processingPlaybackSound(final double position) {
                    Display.getDefault().syncExec(new Runnable() {
                        public void run() {
                            String description = MessageFormat.format("Playing  {0}  {1} / {2}",
                                                                       new String[] {filename2,
                            		                                                 FormatUtil.getFormatedTime(position, true),
                            		                                                 FormatUtil.getFormatedTime(duration, true)
                                                                                     });
                            lbStatusBar.setText(description);
                        }
                    });
                }
                public void paintSound(final byte[] paintBytes, final int channels) {
                    Display.getDefault().syncExec(new Runnable() {
                        public void run() {
                        	SoundUtil.paintSound(canvas,
                        			             bufferImage,
                        			             new Color[] {section.blackColor,
                        			                          section.darkOrangeColor},
                        			             paintBytes,
                        			             channels,
                                                 0);
                        }
                    });
                }
            });
            return playbackSound;
		}
	}
}
