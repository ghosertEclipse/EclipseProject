package com.loadtrend.app.mobile.loadtool.control;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.FormColors;

public class MobileImage extends Composite {
	
	private Image image = null;
	
	private ImageLoader loader = null;
	
	private ImageData imageData = null;
	
	private ImageData[] imageDataArray = null;
	
	private Canvas imageCanvas = null;
    
    private Label label = null;
	
	private Color canvasBackground = null;
	
	private boolean animate = false;
	
	private GC imageCanvasGC = null;
	
    private Cursor waitingCursor = null;
    
    private Cursor handCursor = null;
    
    private static String[] errorPrompt = new String[]{"图片不存在，请检查资源路径或网络连接", "Check the filepath or net connection"};
    
    private static String[] loadingPrompt = new String[]{"正在装载图片...", "Loading picture..."};
    
    private int imageWidth = 0;
    
    private int imageHeight = 0;
    
	/**
	 * Invoke dispose method to release resouce after using this class.
	 * @param parent
	 * @param filename
	 */
	public MobileImage(Composite parent, int imageWidth, int imageHeight) {
		super(parent, SWT.NONE);
		
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth =  0;
        gridLayout.verticalSpacing = 0;
		this.setLayout(gridLayout);
		
        final Color whiteColor = super.getDisplay().getSystemColor(SWT.COLOR_WHITE);
        FormColors formColors = new FormColors(super.getDisplay());
        final Color blueColor = formColors.getColor(FormColors.TITLE);
        waitingCursor = new Cursor(super.getDisplay(), SWT.CURSOR_WAIT);
        handCursor = new Cursor(super.getDisplay(), SWT.CURSOR_HAND);
        
		// Create a GC for drawing, and hook the listener to dispose it.
        imageCanvas = new Canvas(this, SWT.NO_REDRAW_RESIZE);
        GridData gridData = new GridData(imageWidth, imageHeight);
        gridData.horizontalIndent = 1;
        gridData.verticalIndent = 1;
        imageCanvas.setLayoutData(gridData);
        imageCanvas.setBackground(whiteColor);
		imageCanvasGC = new GC(imageCanvas);
		imageCanvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				if (image != null && imageDataArray.length == 1) MobileImage.this.paintImage(e.gc);
			}
		});
		imageCanvas.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				imageCanvasGC.dispose();
			}
		});
        // Create Label
		Composite composite = new Composite(this, SWT.NORMAL);
        gridData = new GridData(imageWidth, 35);
        gridData.horizontalIndent = 1;
        gridData.verticalIndent = 0;
        composite.setLayoutData(gridData);
        composite.setBackground(whiteColor);
        FillLayout layout = new FillLayout();
        layout.marginHeight = 5;
        composite.setLayout(layout);
        label = new Label(composite, SWT.CENTER);
        label.setForeground(blueColor);
        label.setBackground(whiteColor);
        
        // Dispose handler.
		this.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				animate = false;
				if (image != null) image.dispose();
				if (canvasBackground != null) canvasBackground.dispose();
                whiteColor.dispose();
                blueColor.dispose();
                handCursor.dispose();
                waitingCursor.dispose();
                
			}
		});
	}
    
    public int getMobileImageWidth() {
        return this.imageWidth + 2;
    }
    
    public int getMobileImageHeight() {
        return this.imageHeight + 2 + 35;
    }
    
	/**
	 * Asyncrhonized to paint the image.
	 * @param filename the filename or url path.
	 * @param isUrl is url or not.
     * @param labelString the description of the picture. null is available.
     * @param toolTipText the tool tip text of the picture. null is available.
     * @param mouseAdapter invoke this when click the image. null is available.
	 * @return
	 */
	public void paintImageAsyn(final String filename,final boolean isUrl, final String labelString, final String toolTipText, final MouseAdapter mouseAdapter) {
        new Thread() {
            public void run() {
                if (!MobileImage.super.isDisposed()) {
	                MobileImage.this.getDisplay().syncExec(new Runnable() {
	                    public void run() {
	                        MobileImage.this.label.setText(loadingPrompt[0]);
	                    }
	                });
                }
                InputStream inputStream = MobileImage.this.createInputStream(filename, isUrl);
                final boolean success = MobileImage.this.paintImageSyn(inputStream);
                if (!MobileImage.super.isDisposed()) {
	                MobileImage.this.getDisplay().syncExec(new Runnable() {
	                    public void run() {
	                        if (!success) {
	                            MobileImage.this.label.setText(errorPrompt[0]);
	                        } else {
	                            if (labelString != null) {
	                                MobileImage.this.label.setText(labelString);
	                            } else {
	                                MobileImage.this.label.setText("");
	                            }
	                            if (toolTipText != null) MobileImage.this.imageCanvas.setToolTipText(toolTipText);
	                            if (mouseAdapter != null) MobileImage.this.imageCanvas.addMouseListener(mouseAdapter);
	                        }
	                    }
	                });
                }
            }
        }.start();
    }
    
    public void paintImageAsyn(final InputStream inputStream, final String labelString, final String toolTipText, final MouseAdapter mouseAdapter) {
        new Thread() {
            public void run() {
                if (!MobileImage.super.isDisposed()) {
                    MobileImage.this.getDisplay().syncExec(new Runnable() {
                        public void run() {
                            if (!MobileImage.this.label.isDisposed()) MobileImage.this.label.setText(loadingPrompt[0]);
                        }
                    });
                }
                final boolean success = MobileImage.this.paintImageSyn(inputStream);
                if (!MobileImage.super.isDisposed()) {
                    MobileImage.this.getDisplay().syncExec(new Runnable() {
                        public void run() {
                            if (!success) {
                                if (!MobileImage.this.label.isDisposed()) MobileImage.this.label.setText(errorPrompt[0]);
                            } else {
                                if (labelString != null) {
                                    if (!MobileImage.this.label.isDisposed()) MobileImage.this.label.setText(labelString);
                                } else {
                                    if (!MobileImage.this.label.isDisposed()) MobileImage.this.label.setText("");
                                }
                                if (toolTipText != null) MobileImage.this.imageCanvas.setToolTipText(toolTipText);
                                if (mouseAdapter != null) MobileImage.this.imageCanvas.addMouseListener(mouseAdapter);
                            }
                        }
                    });
                }
            }
        }.start();
    }
    
    private boolean paintImageSyn(InputStream inputStream) {
        try {
            final Display display = super.getDisplay();
            
            // Before loading...
            MobileImage.super.getDisplay().syncExec(new Runnable() {
                public void run() {
                    if (!imageCanvas.isDisposed()) imageCanvas.setCursor(waitingCursor);
                }
            });
            
            // Load image from filename or url.
            if (inputStream == null) return false;
            loader = new ImageLoader();
            imageDataArray = loader.load(inputStream);
            inputStream.close();
            
            // After loading...
            if (!MobileImage.super.isDisposed()) {
                MobileImage.super.getDisplay().syncExec(new Runnable() {
                    public void run() {
                        if (!imageCanvas.isDisposed()) imageCanvas.setCursor(handCursor);
                    }
                });
            }
            
            if (imageDataArray.length > 0) {
                // Display the first image in the file, display the non gif.
                image = new Image(display, imageDataArray[0]);
                imageData = imageDataArray[0];
                if (imageDataArray.length == 1) {
                    MobileImage.super.getDisplay().syncExec(new Runnable() {
                        public void run() {
                            imageCanvas.redraw();
                        }
                    });
                    return true;
                }
                
                // Display the gif.
                if (image != null && imageDataArray.length > 1 && loader.logicalScreenWidth > 0 && loader.logicalScreenHeight > 0) {
                    animate = true;
                    Thread animateThread = new Thread("Animation") {
                        public void run() {
                            try {
                                animateLoop(display);
                            } catch (final SWTException e) {
                                display.syncExec(new Runnable() {
                                    public void run() {
                                        e.printStackTrace();
                                    }
                                });
                            }
                        }
                    };
                    animateThread.setDaemon(true);
                    animateThread.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (!MobileImage.super.isDisposed()) {
                MobileImage.super.getDisplay().syncExec(new Runnable() {
                    public void run() {
                        if (!imageCanvas.isDisposed()) imageCanvas.setCursor(handCursor);
                    }
                });
            }
        }
        return true;
    }
    
	/**
	 * Syncrhonized to paint the image, and return the success or fail.
	 * @param filename the filename or url path.
	 * @param isUrl is url or not.
	 * @return
	 */
    private InputStream createInputStream(String filename, boolean isUrl) {
        try {
	        InputStream inputStream = null;
	        if (isUrl) {
	            URL url = new URL(filename);
	            inputStream = url.openStream();
	        } else {
	            inputStream = new FileInputStream(filename);
	        }
	        return inputStream;
        } catch (Exception e) {
            return null;
        }
    }
	
   private void animateLoop (Display display) {
		// Create an off-screen image to draw on, and a GC to draw with.
		// Both are disposed after the animation.
		Image offScreenImage = new Image(display, loader.logicalScreenWidth, loader.logicalScreenHeight);
		GC offScreenImageGC = new GC(offScreenImage);
		
		try {
			// Use syncExec to get the background color of the imageCanvas.
			display.syncExec(new Runnable() {
				public void run() {
                    if (!imageCanvas.isDisposed()) {
					  canvasBackground = imageCanvas.getBackground();
                    }
				}
			});
            
            if (canvasBackground == null) return;

			// Fill the off-screen image with the background color of the canvas.
			offScreenImageGC.setBackground(canvasBackground);
			offScreenImageGC.fillRectangle(
				0,
				0,
				loader.logicalScreenWidth,
				loader.logicalScreenHeight);
					
			// Draw the current image onto the off-screen image.
			offScreenImageGC.drawImage(
				image,
				0,
				0,
				imageData.width,
				imageData.height,
				imageData.x,
				imageData.y,
				imageData.width,
				imageData.height);
			
			int imageDataIndex = 0;

			int repeatCount = loader.repeatCount;
			while (animate && (loader.repeatCount == 0 || repeatCount > 0)) {
				if (imageData.disposalMethod == SWT.DM_FILL_BACKGROUND) {
					// Fill with the background color before drawing.
					offScreenImageGC.setBackground(canvasBackground);
					offScreenImageGC.fillRectangle(
						imageData.x,
						imageData.y,
						imageData.width,
						imageData.height);
				} else if (imageData.disposalMethod == SWT.DM_FILL_PREVIOUS) {
					// Restore the previous image before drawing.
					offScreenImageGC.drawImage(
						image,
						0,
						0,
						imageData.width,
						imageData.height,
						imageData.x,
						imageData.y,
						imageData.width,
						imageData.height);
				}
									
				// Get the next image data.
				imageDataIndex = (imageDataIndex + 1) % imageDataArray.length;
				imageData = imageDataArray[imageDataIndex];
				image.dispose();
				image = new Image(display, imageData);
				
                // Draw the new image data.
                offScreenImageGC.drawImage(
                        image,
                        0,
                        0,
                        imageData.width,
                        imageData.height,
                        imageData.x,
                        imageData.y,
                        imageWidth > imageData.width ? imageData.width : imageWidth,
                        imageHeight > imageData.height ? imageData.height : imageHeight);

                // Draw the off-screen image to the screen.
                if (!imageCanvas.isDisposed()) {
                    imageCanvasGC.drawImage(offScreenImage, 0, 0);
                }
				
				// Sleep for the specified delay time before drawing again.
				try {
					Thread.sleep(visibleDelay(imageData.delayTime * 10));
				} catch (InterruptedException e) {
				}
				
				// If we have just drawn the last image in the set,
				// then decrement the repeat count.
				if (imageDataIndex == imageDataArray.length - 1) repeatCount--;
			}
		} finally {
			offScreenImage.dispose();
			offScreenImageGC.dispose();
		}
    }
    
	/*
	 * Return the specified number of milliseconds.
	 * If the specified number of milliseconds is too small
	 * to see a visual change, then return a higher number.
	 */
	private int visibleDelay(int ms) {
		if (ms < 20) return ms + 30;
		if (ms < 30) return ms + 10;
		return ms;
	}
    
	private void paintImage(GC gc) {
		Image paintImage = image;
		gc.drawImage(
			paintImage,
			0,
			0,
			imageData.width,
			imageData.height,
			imageData.x,
			imageData.y,
            imageWidth,
            imageHeight);
	}

	/**
	 * @return Returns the imageCanvas.
	 */
	public Canvas getImageCanvas() {
		return imageCanvas;
	}
}
