package com.loadtrend.app.test.soundrecorder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.loadtrend.app.test.soundrecorder.info.RecordingScheduleInfo;
import com.loadtrend.app.test.soundrecorder.ui.RecorderMixerSection;
import com.loadtrend.app.test.soundrecorder.ui.RecorderMixerSectionForVista;
import com.loadtrend.app.test.soundrecorder.ui.RecordingSection;
import com.loadtrend.app.test.soundrecorder.ui.SettingSection;
import com.loadtrend.app.test.soundrecorder.util.ColorUtil;
import com.loadtrend.app.test.soundrecorder.util.FontUtil;
import com.loadtrend.app.test.soundrecorder.util.ImageUtil;
import com.loadtrend.app.test.soundrecorder.util.PreferenceUtil;
import com.loadtrend.app.test.soundrecorder.win32native.RegisterHotKey;
import com.loadtrend.lib.sound.VistaAudioDevice;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SoundRecorder
{
    protected Shell shell = null;
    
    private RecordingSection recordingSection = null;
    
    public static void main( String[] args ) {
    	SoundRecorder soundRecorder = new SoundRecorder();
    	soundRecorder.run();
    }
    
    public void run() {
    	Shell shell = this.createShell();
    	Display display = shell.getDisplay();
        
        SashForm sashForm = new SashForm(shell, SWT.HORIZONTAL);
        
        FillLayout fillLayout = new FillLayout();
        // Create recorder mixer section.
        Composite recorderMixerSectionCompoiste = new Composite(sashForm, SWT.NONE);
        recorderMixerSectionCompoiste.setLayout(fillLayout);
        
        // Two RecorderMixerSection for both VISTA and Non-VISTA Operation System.
        RecorderMixerSection recorderMixerSection = null;
        RecorderMixerSectionForVista recorderMixerSectionForVista = null;
        	
        // Vista Operation System Version: 6.0 || XP Media Center Edition: 5.1 && SM_MEDIACENTER = 87
        if (OS.WIN32_VERSION >= OS.VERSION(6, 0) || (OS.WIN32_VERSION == OS.VERSION(5, 1) && OS.GetSystemMetrics(87) != 0)) {
        	VistaAudioDevice.initialize();
            recorderMixerSectionForVista = new RecorderMixerSectionForVista(this, recorderMixerSectionCompoiste);
        } else { // Other Operation System Version
            recorderMixerSection = new RecorderMixerSection(this, recorderMixerSectionCompoiste);
        }
        
        // Create right SashForm
        SashForm rightSashForm = new SashForm(sashForm, SWT.VERTICAL);
        
        // Create recording graphy area in the right SashForm.
        Composite recordingSection = new Composite(rightSashForm, SWT.NONE);
        recordingSection.setLayout(fillLayout);
        this.recordingSection = new RecordingSection(this, recordingSection);
        
        // Create setting area in the right SashForm.
        Composite settingSection = new Composite(rightSashForm, SWT.NONE);
        settingSection.setLayout(fillLayout);
        new SettingSection(this, settingSection);
        
        rightSashForm.setWeights(new int[] {2, 1});
        
        sashForm.setWeights(new int[]{1, 3});
        
		// release the resource and force to stop the java sound thread.
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				ImageUtil.dispose();
				ColorUtil.dispose();
				FontUtil.dispose();
				RegisterHotKey.unRegisterHotKey();
				
		        // Vista Operation System Version: 6.0 || XP Media Center Edition: 5.1 && SM_MEDIACENTER = 87
		        if (OS.WIN32_VERSION >= OS.VERSION(6, 0) || (OS.WIN32_VERSION == OS.VERSION(5, 1) && OS.GetSystemMetrics(87) != 0)) {
				    VistaAudioDevice.destory();
				}
			}
		});
        
        shell.open();
        
		shell.setFocus();
		
        // Two RecorderMixerSection for both VISTA and Non-VISTA Operation System.
		if (recorderMixerSection != null) {
            recorderMixerSection.checkPCMixerAndSetDescToRecordingSource();
		} else {
            recorderMixerSectionForVista.checkPCMixerAndSetDescToRecordingSource();
		}
        
        while ( !shell.isDisposed() )
        {
            // special code for global hot key.
            RegisterHotKey.processHotKeyHandler(display.msg);
            
            if ( !display.readAndDispatch() )
            {
                display.sleep();
            }
        }
        display.dispose();
        
        // The code below is design for exit totally because of audio invoker.
        // And if the code add to shell.addDisposeListener(), this will conflict with FileDialog class.
	    System.exit(0);
    }
    
    private Shell createShell() {
    	Display display = new Display();
    	Rectangle rectangle = display.getClientArea();
    	this.shell =  new Shell( display, SWT.TITLE | SWT.MIN | SWT.CLOSE );
    	// this.shell =  new Shell( new Display(), SWT.MODELESS | SWT.BORDER);
        this.shell.setText("Windows Audio Recorder Professional 4.54");
        // this.shell.setText("Windows Audio Recorder 3.45");
        this.shell.setImage(ImageUtil.getImage(PreferenceUtil.APP_IMAGE));
    	this.shell.setLayout(new FillLayout());
    	this.shell.setSize(850, 600);
		int x = rectangle.x + rectangle.width / 2 - this.shell.getSize().x / 2;
        int y = rectangle.y + rectangle.height / 2 - this.shell.getSize().y / 2;
        this.shell.setLocation(x, y);
        return this.shell;
    }
    
    public int record(RecordingScheduleInfo info, boolean runNow) {
    	return this.recordingSection.notifyToRecord(info, runNow);
    }
    
    public void stopRecord() {
    	this.recordingSection.notifyToStopRecord();
    }
    
    /**
     * @param url nullable to initialize browser
     */
    public void browser(String url) {
    	this.recordingSection.notifyToBrowser(url);
    }
}
