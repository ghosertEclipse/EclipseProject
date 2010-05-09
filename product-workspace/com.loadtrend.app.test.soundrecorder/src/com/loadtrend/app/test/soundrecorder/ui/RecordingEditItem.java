package com.loadtrend.app.test.soundrecorder.ui;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.Section;

import com.loadtrend.app.test.soundrecorder.core.PlaybackSound;
import com.loadtrend.app.test.soundrecorder.core.PlaybackSoundListener;
import com.loadtrend.app.test.soundrecorder.core.SoundUtil;
import com.loadtrend.app.test.soundrecorder.util.ColorUtil;
import com.loadtrend.app.test.soundrecorder.util.FontUtil;
import com.loadtrend.app.test.soundrecorder.util.FormatUtil;
import com.loadtrend.app.test.soundrecorder.util.ImageUtil;
import com.loadtrend.app.test.soundrecorder.util.PreferenceUtil;
import com.loadtrend.app.test.soundrecorder.util.UndoRedoUtil;

public class RecordingEditItem {
	
	private RecordingSection section = null;
	
    private Canvas canvas = null;
    
    private Image bufferImage = null;
	
	private Label lbStatus = null;
	
	private Label lbStatus2 = null;
	
	private ToolItem openItem = null;
	
	private ToolItem saveItem = null;
	
	private ToolItem playItem = null;
	
	private ToolItem stopItem = null;
	
	private ToolItem cutItem = null;
	
	private ToolItem copyItem = null;
	
	private ToolItem pasteItem = null;
	
	private ToolItem deleteItem = null;
	
	private ToolItem undoItem = null;
	
	private ToolItem redoItem = null;
	
	private ToolItem zoomInItem = null;
	
	private ToolItem zoomOutItem = null;
	
	private Menu exportMenu = null;
	
	private static final int maxZoomRate = 32;
	
	private UndoRedoUtil undoRedoUtil = new UndoRedoUtil();
	
	public RecordingEditItem(RecordingSection section) {
		this.section = section;
	}
	
	public CTabItem createItem() {
        // recording edit item
		CTabItem editItem = new CTabItem(section.tabFolder, SWT.NONE);
		
		Form editItemForm = section.kit.createForm(section.tabFolder);
		editItem.setControl(editItemForm);
		FillLayout layout = new FillLayout();
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		layout.spacing = 10;
		editItemForm.getBody().setLayout(layout);
		
		// edit toolbar graph section
		Section toolbarGraph = section.kit.createSection(editItemForm.getBody(), Section.TITLE_BAR | Section.DESCRIPTION | Section.TREE_NODE);
		toolbarGraph.setText("Edit Sound Toolbar && Sample Graph");
		toolbarGraph.setDescription("Drag mouse with left button down along the sample graph to select the duration you want to edit.");
		toolbarGraph.setExpanded(true);
		toolbarGraph.setTitleBarBackground(section.orangeColor);
		toolbarGraph.setTitleBarGradientBackground(section.orangeColor);
		
		// composite that contains toolbar and sample graph.
		Composite composite = section.kit.createComposite(toolbarGraph);
		FormLayout formLayout = new FormLayout();
		formLayout.marginTop = 10;
		// formLayout.spacing = 10;
		composite.setLayout(formLayout);
		
		// Toolbar
        FormData data = new FormData();
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(0, 40);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100);
		final ToolBar toolBar = new ToolBar(composite, SWT.HORIZONTAL | SWT.FLAT);
		this.openItem = new ToolItem(toolBar, SWT.PUSH);
		openItem.setText("Open");
		openItem.setImage(ImageUtil.getImage("images/open.gif"));
		this.openItem.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(toolBar.getShell());
				fileDialog.setFilterExtensions(new String[] {"*.wav;*.mp3" });
                fileDialog.setFilterNames(new String[] {"Music Format(*.wav;*.mp3)"});
				String filename = fileDialog.open();
				if (filename != null) {
					importSound(filename);
				}
			}
		});
		
		this.saveItem= new ToolItem(toolBar, SWT.PUSH);
		saveItem.setText("Save");
		saveItem.setImage(ImageUtil.getImage("images/save.gif"));
		saveItem.setDisabledImage(ImageUtil.getImage("images/save_d.gif"));
		saveItem.setEnabled(false);
		this.saveItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				exportMenu.setVisible(true);
			}
		});
		this.createExportMenu(toolBar);
		
		ToolItem separatorItem1 = new ToolItem(toolBar, SWT.PUSH);
		separatorItem1.setText("|");
		separatorItem1.setEnabled(false);
		
		this.playItem = new ToolItem(toolBar, SWT.PUSH);
		playItem.setText("Play");
		playItem.setImage(ImageUtil.getImage("images/play.gif"));
		playItem.setDisabledImage(ImageUtil.getImage("images/play_d.gif"));
		playItem.setEnabled(false);
		this.playItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (startTimePosition >= endTimePosition) startTimePosition = 0;
				playbackSound = new PlaybackSound(soundUtil.getTempFilename(), startTimePosition, endTimePosition);
	            playbackSound.addPlaybackSoundListener(new PlaybackSoundListener() {
	                public void stopPlaybackSound(final boolean isAutoFinished) {
	                    Display.getDefault().syncExec(new Runnable() {
	                        public void run() {
	    						GC gc = new GC(canvas);
	    						gc.drawImage(bufferImage, origin.x, origin.y);
	    						gc.dispose();
					            playItem.setEnabled(true);
					            stopItem.setEnabled(false);
	                        }
	                    });
	                }
	                public void processingPlaybackSound(final double position) {
	                    Display.getDefault().syncExec(new Runnable() {
	                        public void run() {
	    						GC gc = new GC(canvas);
						        gc.drawImage(bufferImage, origin.x, origin.y);
	    						gc.setForeground(ColorUtil.getColor(ColorUtil.darkGreenColor));
	    						int imagePosition = getImagePositionByTimePosition(position);
	    						int x = imagePosition - canvas.getHorizontalBar().getSelection();
	    						gc.drawLine(x, 0, x, imageHeight);
	    						gc.drawText(getSoundPositionInfo(imagePosition), canvas.getClientArea().width - 140, 0, false);
	    						gc.dispose();
	    						// scroll tha bar when the palying line through one screen client area.
	    						ScrollBar bar = canvas.getHorizontalBar();
	    						if (imagePosition > bar.getSelection() + canvas.getClientArea().width) {
		    						bar.setSelection(bar.getSelection() + canvas.getClientArea().width);
				                    Event event = new Event();
		    						bar.notifyListeners(SWT.Selection, event);
	    						}
	    						if (imagePosition < bar.getSelection()) {
		    						bar.setSelection(bar.getSelection() - canvas.getClientArea().width);
				                    Event event = new Event();
		    						bar.notifyListeners(SWT.Selection, event);
	    						}
	                        }
	                    });
	                }
	                public void paintSound(final byte[] paintBytes, final int channels) {
	                }
	            });
	            playbackSound.start();
	            playItem.setEnabled(false);
	            stopItem.setEnabled(true);
			}
		});
		
		this.stopItem = new ToolItem(toolBar, SWT.PUSH);
		stopItem.setText("Stop");
		stopItem.setImage(ImageUtil.getImage("images/stop.gif"));
		stopItem.setDisabledImage(ImageUtil.getImage("images/stop_d.gif"));
		stopItem.setEnabled(false);
		this.stopItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				new Thread() {
					public void run() {
						playbackSound.stop();
					}
				}.start();
			}
		});
		
		ToolItem separatorItem3 = new ToolItem(toolBar, SWT.PUSH);
		separatorItem3.setText("|");
		separatorItem3.setEnabled(false);
		
		this.cutItem = new ToolItem(toolBar, SWT.PUSH);
		cutItem.setText("Cut");
		cutItem.setImage(ImageUtil.getImage("images/cut.gif"));
		cutItem.setDisabledImage(ImageUtil.getImage("images/cut_d.gif"));
		cutItem.setEnabled(false);
		this.cutItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (stopItem.getEnabled()) {
					stopPlayingWhenEditWarning();
					return;
				}
				UndoRedoUtil.Action action = undoRedoUtil.new Action() {
					private double startTime = -1;
					private double endTime = -1;
					private String filename = null;
					public void redo() {
						if (startTime == -1) startTime = startTimePosition;
						if (endTime == -1) endTime = endTimePosition;
						this.filename = RecordingEditItem.this.cutOperation(startTime, endTime);
						pasteFilename = this.filename;
						pasteItem.setEnabled(true);
					}
					public void undo() {
						RecordingEditItem.this.pasteOperation(startTime, this.filename);
						if (!pasteFilename.equals(this.filename)) {
							new File(this.filename).delete();
						}
					}
				};
				action.redo();
				undoRedoUtil.addAction(action);
			}
		});
		this.copyItem = new ToolItem(toolBar, SWT.PUSH);
		copyItem.setText("Copy");
		copyItem.setImage(ImageUtil.getImage("images/copy.gif"));
		copyItem.setDisabledImage(ImageUtil.getImage("images/copy_d.gif"));
		copyItem.setEnabled(false);
		this.copyItem.addSelectionListener(new SelectionAdapter() {
			private String copyFilename  = null;
			public void widgetSelected(SelectionEvent e) {
				if (copyFilename != null) new File(copyFilename).delete();
				this.copyFilename = soundUtil.copySnippet(startTimePosition, endTimePosition);
				pasteFilename = this.copyFilename;
				pasteItem.setEnabled(true);
			}
		});
		this.pasteItem = new ToolItem(toolBar, SWT.PUSH);
		pasteItem.setText("Paste");
		pasteItem.setImage(ImageUtil.getImage("images/paste.gif"));
		pasteItem.setDisabledImage(ImageUtil.getImage("images/paste_d.gif"));
		pasteItem.setEnabled(false);
		this.pasteItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (stopItem.getEnabled()) {
					stopPlayingWhenEditWarning();
					return;
				}
				UndoRedoUtil.Action action = undoRedoUtil.new Action() {
					private double startTime = -1;
					private double endTime = -1;
					public void redo() {
						if (startTime == -1) startTime = startTimePosition;
						double pasteDuration = RecordingEditItem.this.pasteOperation(startTime, pasteFilename);
						endTime = startTime + pasteDuration;
					}
					public void undo() {
						String filename = RecordingEditItem.this.cutOperation(startTime, endTime);
						new File(filename).delete();
					}
				};
				action.redo();
				undoRedoUtil.addAction(action);
			}
		});
		this.deleteItem = new ToolItem(toolBar, SWT.PUSH);
		deleteItem.setText("Delete");
		deleteItem.setImage(ImageUtil.getImage("images/delete.gif"));
		deleteItem.setDisabledImage(ImageUtil.getImage("images/delete_d.gif"));
		deleteItem.setEnabled(false);
		this.deleteItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (stopItem.getEnabled()) {
					stopPlayingWhenEditWarning();
					return;
				}
				UndoRedoUtil.Action action = undoRedoUtil.new Action() {
					private double startTime = -1;
					private double endTime = -1;
					private String filename = null;
					public void redo() {
						if (startTime == -1) startTime = startTimePosition;
						if (endTime == -1) endTime = endTimePosition;
						this.filename = RecordingEditItem.this.cutOperation(startTime, endTime);
					}
					public void undo() {
						RecordingEditItem.this.pasteOperation(startTime, this.filename);
						new File(this.filename).delete();
					}
				};
				action.redo();
				undoRedoUtil.addAction(action);
			}
		});
		ToolItem separatorItem2 = new ToolItem(toolBar, SWT.PUSH);
		separatorItem2.setText("|");
		separatorItem2.setEnabled(false);
		
		this.undoItem = new ToolItem(toolBar, SWT.PUSH);
		undoItem.setText("Undo");
		undoItem.setImage(ImageUtil.getImage("images/undo.gif"));
		undoItem.setDisabledImage(ImageUtil.getImage("images/undo_d.gif"));
		undoItem.setEnabled(false);
		this.undoItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (stopItem.getEnabled()) {
					stopPlayingWhenEditWarning();
					return;
				}
				undoRedoUtil.undo();
			}
		});
		this.redoItem = new ToolItem(toolBar, SWT.PUSH);
		redoItem.setText("Redo");
		redoItem.setImage(ImageUtil.getImage("images/redo.gif"));
		redoItem.setDisabledImage(ImageUtil.getImage("images/redo_d.gif"));
		redoItem.setEnabled(false);
		this.redoItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (stopItem.getEnabled()) {
					stopPlayingWhenEditWarning();
					return;
				}
				undoRedoUtil.redo();
			}
		});
		ToolItem separatorItem4 = new ToolItem(toolBar, SWT.PUSH);
		separatorItem4.setText("|");
		separatorItem4.setEnabled(false);
		
		this.zoomInItem = new ToolItem(toolBar, SWT.PUSH);
		zoomInItem.setText("Zoom In");
		zoomInItem.setImage(ImageUtil.getImage("images/zoom_in.bmp"));
		zoomInItem.setDisabledImage(ImageUtil.getImage("images/zoom_in_d.bmp"));
		zoomInItem.setEnabled(false);
		this.zoomInItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				repaintSound(2 * imageWidth, imageHeight);
			}
		});
		
		this.zoomOutItem = new ToolItem(toolBar, SWT.PUSH);
		zoomOutItem.setText("Zoom Out");
		zoomOutItem.setImage(ImageUtil.getImage("images/zoom_out.bmp"));
		zoomOutItem.setDisabledImage(ImageUtil.getImage("images/zoom_out_d.bmp"));
		zoomOutItem.setEnabled(false);
		this.zoomOutItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				repaintSound(imageWidth / 2, imageHeight);
			}
		});
		
		undoRedoUtil.addActionListChangedListener(new UndoRedoUtil.ActionListChangedListener() {
			public void notifyToUndo() {
				undoItem.setEnabled(true);
			}
			public void noAvailableUndo() {
				undoItem.setEnabled(false);
			}
			public void noAvailableRedo() {
				redoItem.setEnabled(false);
			}
			public void notifyToRedo() {
				redoItem.setEnabled(true);
			}
		});
		
		// Status bar composite
		Composite lbComposite = section.kit.createComposite(composite);
        data = new FormData();
        data.top = new FormAttachment(100, -15);
        data.bottom = new FormAttachment(100);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100);
        lbComposite.setLayoutData(data);
        lbComposite.setLayout(new FormLayout());
        
        this.lbStatus = section.kit.createLabel(lbComposite, "");
		this.lbStatus.setForeground(ColorUtil.getColor(ColorUtil.blackColor));
		this.lbStatus.setFont(FontUtil.getBoldSystemFont());
        data = new FormData();
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(65);
        this.lbStatus.setLayoutData(data);
        this.lbStatus2 = section.kit.createLabel(lbComposite, "", SWT.RIGHT);
		this.lbStatus2.setForeground(ColorUtil.getColor(ColorUtil.blackColor));
		this.lbStatus2.setFont(FontUtil.getBoldSystemFont());
        data = new FormData();
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100);
        data.left = new FormAttachment(this.lbStatus);
        data.right = new FormAttachment(100);
        this.lbStatus2.setLayoutData(data);
		
		// Sample graph
		this.canvas = new Canvas(composite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.DOUBLE_BUFFERED);
        data = new FormData();
        data.top = new FormAttachment(toolBar);
        data.bottom = new FormAttachment(lbComposite);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100);
        this.canvas.setLayoutData(data);
		this.canvas.setBackground(this.section.whiteColor);
		
		final ScrollBar bar = canvas.getHorizontalBar();
		bar.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
                int hSelection = bar.getSelection();
                int destX = -hSelection - origin.x;
                canvas.scroll(destX, 0, 0, 0, imageWidth, imageHeight, false);
            	origin.x = -hSelection;
			}
		});
        
        canvas.addControlListener(new ControlListener() {
			public void controlMoved(ControlEvent e) {
			}

			public void controlResized(ControlEvent e) {
				if (canvas.getClientArea().width <= 0 || canvas.getClientArea().height <=0) return;
                ScrollBar bar = canvas.getHorizontalBar();
                bar.setMaximum(canvas.getClientArea().width);
    			bar.setThumb(canvas.getClientArea().width);
    			bar.setPageIncrement(canvas.getClientArea().width);
    			bar = canvas.getVerticalBar();
                bar.setMaximum(canvas.getClientArea().height);
    			bar.setThumb(canvas.getClientArea().height);
    			bar.setPageIncrement(canvas.getClientArea().height);
    			if (soundUtil != null) {
    				initialSoundInfo(canvas.getClientArea().width, canvas.getClientArea().height);
    			}
			}
        });

		// Create bufferImage
		this.canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				if (bufferImage != null) {
                    e.gc.drawImage(bufferImage, origin.x, origin.y);
				}
			}
		});
		
		// Distroy bufferImage
        this.canvas.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
		        releaseImage(initialBufferImage);
		        releaseImage(bufferImage);
            }
        });
        
        this.canvas.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent e) {
				if (soundUtil != null) {
					if (leftClickSign == false) {
						if (playItem.isEnabled()) {
						    ScrollBar bar = canvas.getHorizontalBar();
						    int currentPosition = bar.getSelection() + e.x;
					        if (currentPosition >= imageWidth) return;
							GC gc = new GC(canvas);
							gc.drawImage(bufferImage, origin.x, origin.y);
							gc.setForeground(ColorUtil.getColor(ColorUtil.darkBlueColor));
							gc.drawLine(e.x, 0, e.x, imageHeight);
							gc.dispose();
						}
						int currentPosition = bar.getSelection() + e.x;
					    lbStatus2.setText("Current: " + getSoundPositionInfo(currentPosition));
					} else {
						ScrollBar bar = canvas.getHorizontalBar();
						int currentPosition = bar.getSelection() + e.x;
						if (currentPosition > imageWidth) currentPosition = imageWidth;
						if (currentPosition < 0) currentPosition = 0;
				        int width = currentPosition - leftClickPosition;
				        if (width == 0) return;
				        releaseImage(bufferImage);
				        bufferImage = new Image(null, initialBufferImage, SWT.IMAGE_COPY);
				        int realLeft = 0;
				        if (width < 0) {
				        	realLeft = currentPosition;
				        	width = width * -1;
				        } else {
				        	realLeft = leftClickPosition;
				        }
				        
				        Image image = new Image(null, width, imageHeight);
						GC gc = new GC(image);
				        gc.drawImage(bufferImage, realLeft, 0, width, imageHeight,  0, 0, width, imageHeight);
				        gc.dispose();
				        ImageData imageData = image.getImageData();
				        for (int i = 0; i < imageData.width; i++) {
				        	for (int j = 0; j < imageData.height; j++) {
				        		int pixel = imageData.getPixel(i, j);
				        		RGB rgb = imageData.palette.getRGB(pixel);
				        		RGB newRgb = new RGB(0xff - rgb.red, 0xff - rgb.green, 0xff - rgb.blue);
				                imageData.setPixel(i, j, imageData.palette.getPixel(newRgb));
				        	}
				        }
				        image.dispose();
				        image = new Image(null, imageData);
						gc = new GC(bufferImage);
				        gc.drawImage(image, 0, 0, width, imageHeight, realLeft, 0, width, imageHeight);
						gc.dispose();
						gc = new GC(canvas);
						gc.drawImage(bufferImage, origin.x, origin.y);
						gc.dispose();
				        image.dispose();
						lbStatus.setText("Selection: " + getSoundPositionInfo(realLeft) + " - " + getSoundPositionInfo(realLeft + width));
					    lbStatus2.setText("Current: " + getSoundPositionInfo(currentPosition));
						startTimePosition = getTimePositionByImagePosition(realLeft);
						endTimePosition = getTimePositionByImagePosition(realLeft + width);
						changeEditItemState(true);
						exportSelectionWavItem.setEnabled(true);
						exportSelectionMp3Item.setEnabled(true);
						// scroll tha bar when the palying line through one screen client area.
						if (currentPosition > bar.getSelection() + canvas.getClientArea().width) {
    						bar.setSelection(bar.getSelection() + bar.getIncrement());
		                    Event event = new Event();
    						bar.notifyListeners(SWT.Selection, event);
						}
						if (currentPosition < bar.getSelection()) {
    						bar.setSelection(bar.getSelection() - bar.getIncrement());
		                    Event event = new Event();
    						bar.notifyListeners(SWT.Selection, event);
						}
					}
				}
			}
        });
        
        this.canvas.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if (soundUtil != null) {
					ScrollBar bar = canvas.getHorizontalBar();
					int currentPosition = bar.getSelection() + e.x;
					if (currentPosition >= imageWidth) {
					    lbStatus.setText("Cursor: " + getSoundPositionInfo(0));
						return;
					}
			        releaseImage(bufferImage);
			        bufferImage = new Image(null, initialBufferImage, SWT.IMAGE_COPY);
					GC gc = new GC(bufferImage);
					gc.setForeground(ColorUtil.getColor(ColorUtil.darkYellowColor));
					gc.drawLine(currentPosition, 0, currentPosition, imageHeight);
					gc.dispose();
					gc = new GC(canvas);
					gc.drawImage(bufferImage, origin.x, origin.y);
					gc.dispose();
					leftClickPosition = currentPosition;
					lbStatus.setText("Cursor: " + getSoundPositionInfo(currentPosition));
					startTimePosition = getTimePositionByImagePosition(currentPosition);
					endTimePosition = soundUtil.getDuration();
		            leftClickSign = true;
		            changeEditItemState(false);
					exportSelectionWavItem.setEnabled(false);
					exportSelectionMp3Item.setEnabled(false);
				}
			}
			public void mouseUp(MouseEvent arg0) {
				if (soundUtil != null) {
				    leftClickSign = false;
				}
			}
        });
        
		toolbarGraph.setClient(composite);
		
		return editItem;
	}
	
	private void changeEditItemState(boolean isEnable) {
		cutItem.setEnabled(isEnable);
		copyItem.setEnabled(isEnable);
		deleteItem.setEnabled(isEnable);
	}
	
    /**
     * Return the duration of the paste file.
     * @param startTimePosition
     * @return
     */
	public double pasteOperation(double startTimePosition, String filename) {
		double originalDuration = soundUtil.getDuration();
		double pasteDuration = soundUtil.pasteSnippet(startTimePosition, filename);
		double newDuration = pasteDuration + originalDuration;
		int imageWidth = (int) (newDuration / originalDuration * RecordingEditItem.this.imageWidth);
		repaintSound(imageWidth, imageHeight);
		endTimePosition = soundUtil.getDuration();
        Event event = new Event();
        event.x = getImagePositionByTimePosition(startTimePosition) - canvas.getHorizontalBar().getSelection();
        canvas.notifyListeners(SWT.MouseDown, event);
        canvas.notifyListeners(SWT.MouseUp, event);
        this.changeEditItemState(false);
		return pasteDuration;
	}
	
	public String cutOperation(double startTimePosition, double endTimePosition) {
		int reduceImageWidth = getImagePositionByTimePosition(endTimePosition) - getImagePositionByTimePosition(startTimePosition);
		String filename = soundUtil.cutSnippet(startTimePosition, endTimePosition);
		repaintSound(imageWidth - reduceImageWidth, imageHeight);
		endTimePosition = soundUtil.getDuration();
        Event event = new Event();
        event.x = getImagePositionByTimePosition(startTimePosition) - canvas.getHorizontalBar().getSelection();
        canvas.notifyListeners(SWT.MouseDown, event);
        canvas.notifyListeners(SWT.MouseUp, event);
        this.changeEditItemState(false);
		return filename;
	}
	
	public void importSound(String filename) {

		if (stopItem.isEnabled() == true) stopItem.notifyListeners(SWT.Selection, new Event());
		
		// create the temp folder if not exists.
        File tempPath = new File(PreferenceUtil.TEMP_PATH);
        if (!tempPath.exists()) tempPath.mkdir();
		
		SoundUtil soundUtil = new SoundUtil(filename);
		
		if (!soundUtil.importSound()) {
			MessageBox box = new MessageBox(this.canvas.getShell(), SWT.OK);
			box.setText("Error");
			box.setMessage("The file selected is not supported, or you abort importing file.");
			box.open();
			new File(soundUtil.getTempFilename()).delete();
			return;
		}
		
		if (this.soundUtil != null) this.soundUtil.releaseResource();
		this.soundUtil = soundUtil;
		
		// Clear the temp folder except the paste filename and current temp file.
    	File[] files = tempPath.listFiles();
    	for (int i = 0; i < files.length; i++) {
    		if (files[i].getAbsolutePath().equals(this.pasteFilename) ||
    			files[i].getAbsolutePath().equals(this.soundUtil.getTempFilename())) {
    			continue;
    		}
   		    files[i].delete();
    	}
        
		
		this.initialSoundInfo(this.canvas.getClientArea().width, this.canvas.getClientArea().height);
		
		this.undoRedoUtil.clear();
		
		this.saveItem.setEnabled(true);
		this.playItem.setEnabled(true); 
		this.zoomInItem.setEnabled(true);
		this.zoomOutItem.setEnabled(true);
		this.undoItem.setEnabled(false);
		this.redoItem.setEnabled(false);
		this.changeEditItemState(false);
		this.lbStatus.setText("");
		this.lbStatus2.setText("");
	}
	
	private void initialSoundInfo(int imageWidth, int imageHeight) {
		
		if (imageWidth <= 0 || imageHeight <= 0) return;
		
		this.startTimePosition = 0;
		this.endTimePosition = this.soundUtil.getDuration();
		
        ScrollBar bar = this.canvas.getHorizontalBar();
        bar.setMaximum(this.canvas.getClientArea().width);
        
       	this.validCanvasWidth = imageWidth;
        
		releaseImage(initialBufferImage);
        initialBufferImage = new Image(null, new Rectangle(0, 0, imageWidth, imageHeight));
        
		origin.x = 0;
		origin.y = 0;
		
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		
		this.paintSoundImage(0);
	}
	
	private void repaintSound(int imageWidth, int imageHeight) {
		
		if (imageWidth <= 0 || imageHeight <= 0) return;
		
		while (imageWidth >= canvas.getClientArea().width * maxZoomRate * 2) {
			imageWidth = imageWidth / 2;
		}
		
		if (imageWidth <= canvas.getClientArea().width / maxZoomRate / 2) {
			imageWidth = imageWidth * 2;
		}
		
		if (imageWidth >= canvas.getClientArea().width * maxZoomRate) zoomInItem.setEnabled(false);
		if (imageWidth > canvas.getClientArea().width / maxZoomRate) zoomOutItem.setEnabled(true);
		
		if (imageWidth <= canvas.getClientArea().width / maxZoomRate) zoomOutItem.setEnabled(false);
		if (imageWidth < canvas.getClientArea().width * maxZoomRate) zoomInItem.setEnabled(true);
		
        if (imageWidth % canvas.getClientArea().width == 0) {
        	this.validCanvasWidth = imageWidth;
        } else {
        	this.validCanvasWidth = (imageWidth / canvas.getClientArea().width + 1) * canvas.getClientArea().width;
        }
        
        ScrollBar bar = this.canvas.getHorizontalBar();
        
    	bar.setMaximum(this.validCanvasWidth);
    	if (this.validCanvasWidth > canvas.getClientArea().width) {
    		bar.setIncrement(this.canvas.getClientArea().width / 10);
    	}
        
		releaseImage(initialBufferImage);
        initialBufferImage = new Image(null, new Rectangle(0, 0, imageWidth, imageHeight));
        
        // reset bar selection.
	    bar.setSelection(bar.getSelection() * imageWidth / this.imageWidth);
        origin.x = -bar.getSelection();
		
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		
		this.paintSoundImage(0);
	}
	
	private void releaseImage(Image image) {
        if (image != null && !image.isDisposed()) {
        	image.dispose();
        }
	}
	
	// Get a start position to paint initialBufferImage, the default end position is "start position + initialBufferImage width".
	private void paintSoundImage(int startImagePosition) {
		if (this.imageWidth > 0 && this.imageHeight > 0) {
		    soundUtil.paintSound2(initialBufferImage, this.imageWidth, this.getTimePositionByImagePosition(startImagePosition),
		    		              this.getTimePositionByImagePosition(startImagePosition + this.imageWidth),
		    		              new Color[] {section.whiteColor, section.smallBlueColor, section.smallRedColor});
	        GC canvasGC = new GC(canvas);
	        canvasGC.fillRectangle(0, 0, validCanvasWidth, imageHeight);
	        canvasGC.drawImage(initialBufferImage, origin.x, origin.y);
	        canvasGC.dispose();
	        
            releaseImage(bufferImage);
            bufferImage = new Image(null, initialBufferImage, SWT.IMAGE_COPY);
		}
	}
	
	private String getSoundPositionInfo(int x) {
		return FormatUtil.getFormatedTime(this.getTimePositionByImagePosition(x), true) + " min:sec";	
	}
	
	private double getTimePositionByImagePosition(int x) {
		return x * 1.0 / imageWidth * soundUtil.getDuration();
	}
	
	private int getImagePositionByTimePosition(double timePosition) {
		return (int) (timePosition / soundUtil.getDuration() * imageWidth);
	}
	
	private void stopPlayingWhenEditWarning() {
		MessageBox box = new MessageBox(Display.getDefault().getActiveShell(), SWT.OK);
		box.setMessage("Please stop playing when you begin to edit sound.");
		box.setText("Sound Editor Warning");
		box.open();
	}
	
	private void createExportMenu(Control control) {
		this.exportMenu = new Menu(control);
		MenuItem exportWavItem = new MenuItem(exportMenu, SWT.PUSH);
		exportWavItem.setText("Export As WAV...");
		exportWavItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				openSelectionDialog(".wav", soundUtil.getTempFilename());
			}
		});
		this.exportSelectionWavItem = new MenuItem(exportMenu, SWT.PUSH);
		exportSelectionWavItem.setText("Export Selection As WAV...");
		exportSelectionWavItem.setEnabled(false);
		exportSelectionWavItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				openSelectionDialog(".wav", soundUtil.copySnippet(startTimePosition, endTimePosition));
			}
		});
		new MenuItem(exportMenu, SWT.SEPARATOR);
		MenuItem exportMp3Item = new MenuItem(exportMenu, SWT.PUSH);
		exportMp3Item.setText("Export As MP3...");
		exportMp3Item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				openSelectionDialog(".mp3", soundUtil.getTempFilename());
			}
		});
		this.exportSelectionMp3Item = new MenuItem(exportMenu, SWT.PUSH);
		exportSelectionMp3Item.setText("Export Selection As MP3...");
		exportSelectionMp3Item.setEnabled(false);
		exportSelectionMp3Item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				openSelectionDialog(".mp3", soundUtil.copySnippet(startTimePosition, endTimePosition));
			}
		});
	}
	
	/**
	 * @param postfix such as ".wav", ".mp3"
	 * @param inputFilename the source file to be converted.
	 */
	private void openSelectionDialog(String postfix, String inputFilename) {
		
		// Trial version
//		if (true) {
//            section.tabFolder.setSelection(section.aboutTabItem);
//            return;
//		}
        
		FileDialog fileDialog = new FileDialog(Display.getDefault().getActiveShell(), SWT.SAVE);
		String originalname = soundUtil.getFilename();
		originalname = new File(originalname).getName();
		originalname = originalname.substring(0, originalname.length() - 4) + postfix;
        fileDialog.setFileName(originalname);
		fileDialog.setFilterExtensions(new String[] {"*" + postfix});
        fileDialog.setFilterNames(new String[] {"Music Format(*" + postfix + ")"});
		String filename = fileDialog.open();
		if (filename != null) {
			File file = new File(filename);
			if (file.exists()) {
				MessageBox box = new MessageBox(Display.getDefault().getActiveShell(), SWT.YES | SWT.NO);
				box.setText("Warning");
				box.setMessage("The file selected already exists, Do you want to replace the existing file?");
				int resultCode = box.open();
				if (resultCode == SWT.NO) return;
				file.delete();
			}
			if (!soundUtil.outputSound(inputFilename, filename)) {
				MessageBox box = new MessageBox(Display.getDefault().getActiveShell(), SWT.OK);
				box.setText("Error");
				box.setMessage("The file selected is not supported, or you abort outputting file.");
				box.open();
				new File(filename).delete();
				return;
			}
		}
	}
	
	private MenuItem exportSelectionWavItem = null;
	
	private MenuItem exportSelectionMp3Item = null;
	
	private SoundUtil soundUtil = null;
	
	private String pasteFilename = null;
	
	private Image initialBufferImage = null;
	
	private int leftClickPosition = 0;
	
	private boolean leftClickSign = false;
	
	private PlaybackSound playbackSound = null;
	
	private double startTimePosition = 0;
	
	private double endTimePosition = 0;
	
	private int imageWidth = 0;
	
	private int imageHeight = 0;
	
	private int validCanvasWidth = 0;
	
    private Point origin = new Point(0, 0);
}
