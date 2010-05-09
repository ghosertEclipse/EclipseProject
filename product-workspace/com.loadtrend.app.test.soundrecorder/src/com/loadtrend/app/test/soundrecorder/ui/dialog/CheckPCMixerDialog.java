package com.loadtrend.app.test.soundrecorder.ui.dialog;

import java.io.File;

import javax.sound.sampled.AudioFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.tritonus.share.sampled.AudioFileTypes;

import com.loadtrend.app.test.soundrecorder.core.CaptureSound;
import com.loadtrend.app.test.soundrecorder.core.CaptureSoundListener;
import com.loadtrend.app.test.soundrecorder.core.PlaybackSound;
import com.loadtrend.app.test.soundrecorder.core.PlaybackSoundListener;
import com.loadtrend.app.test.soundrecorder.core.SoundUtil;
import com.loadtrend.app.test.soundrecorder.util.PreferenceUtil;

public class CheckPCMixerDialog {
	// private Shell shell = new Shell(SWT.SYSTEM_MODAL | SWT.CLOSE);
	private Shell shell = new Shell(SWT.MODELESS | SWT.TITLE);
    
    private static final double silencePercent = 0.05;
    
	private boolean isFoundPCMixer = false;

	public CheckPCMixerDialog(Shell parentShell) {
		shell.setSize(300, 120);
		int x = parentShell.getLocation().x + parentShell.getSize().x / 2
				- shell.getSize().x / 2;
		int y = parentShell.getLocation().y + parentShell.getSize().y / 2
				- shell.getSize().y / 2;
		shell.setLocation(x, y);
	}

	/**
	 * Return found or not;
	 * @return
	 */
	public boolean create(String deviceName) {
		shell.setText("Sound card setting");

		FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
		fillLayout.marginHeight = 20;
		fillLayout.marginWidth = 20;
		shell.setLayout(fillLayout);

		Label label = new Label(shell, SWT.CENTER);
		label.setText("Testing " + deviceName);

		shell.open();
		
        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
        float rate = Float.parseFloat(PreferenceUtil.SAMPLE_RATES[Integer.parseInt(PreferenceUtil.getValue(PreferenceUtil.SAMPLE_RATE))]); // 11025f, 16000f, 22050f, 44100f
        int sampleSize = Integer.parseInt(PreferenceUtil.SAMPLE_BITS[Integer.parseInt(PreferenceUtil.getValue(PreferenceUtil.SAMPLE_BIT))]); // 8, 16
        boolean bigEndian = false;
        int channels = Integer.parseInt(PreferenceUtil.CHANNELS[Integer.parseInt(PreferenceUtil.getValue(PreferenceUtil.CHANNEL))]); // 1: mono; 2: stereo
        AudioFormat format = new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize/8)*channels, rate, bigEndian);
        final CaptureSound captureSound = new CaptureSound(format, AudioFileTypes.WAVE);
        captureSound.addCaptureSoundListener(new CaptureSoundListener() {
			public boolean paintSound(byte[] paintBytes, int channels) {
                boolean isPlainLine = SoundUtil.isPlainLine(paintBytes, channels, silencePercent);
                if (!isPlainLine) isFoundPCMixer = true;
                return isPlainLine;
			}

			public void processingCaptureSound(boolean isSilenceDuration, double duration, double position) {
			}

			public void stopCaptureSound(double duration, long capturedAudioBytesLength) {
                Display.getDefault().syncExec(new Runnable() {
                    public void run() {
                        shell.dispose();
                    }
                });
			}
        });
        captureSound.start();
        
        PlaybackSound playbackSound = new PlaybackSound(System.getProperty("user.dir") + File.separator + "test.wav");
        playbackSound.addPlaybackSoundListener(new PlaybackSoundListener() {
            public void paintSound(byte[] paintBytes, int channels) {
            }
            public void processingPlaybackSound(double position) {
            }
            public void stopPlaybackSound(boolean isAutoFinished) {
                captureSound.stop();
            }
        });
        playbackSound.start();
        
		Display display = shell.getDisplay();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return this.isFoundPCMixer;
	}
}
