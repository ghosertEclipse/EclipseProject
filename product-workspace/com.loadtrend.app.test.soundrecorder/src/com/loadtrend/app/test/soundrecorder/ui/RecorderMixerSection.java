package com.loadtrend.app.test.soundrecorder.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Control;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import com.loadtrend.app.test.soundrecorder.SoundRecorder;
import com.loadtrend.app.test.soundrecorder.core.SystemMixer;
import com.loadtrend.app.test.soundrecorder.info.SystemRecorderMixerInfo;
import com.loadtrend.app.test.soundrecorder.ui.dialog.CheckPCMixerDialog;
import com.loadtrend.app.test.soundrecorder.ui.dialog.NoSoundDialog;
import com.loadtrend.app.test.soundrecorder.util.ImageUtil;
import com.loadtrend.app.test.soundrecorder.util.PreferenceUtil;

public class RecorderMixerSection extends AbstractSharedSection {
	
    private List selectControlList = new ArrayList();
    
    private Form form = null;
    
    private List recorderInfoList = new ArrayList();
    
    private SystemRecorderMixerInfo defaultSelectedInfo = null;
    
	public RecorderMixerSection(SoundRecorder soundRecorder, Composite parentComposite) {
		super(parentComposite);
		this.create();
	}
	
	public void checkPCMixerAndSetDescToRecordingSource() {
        String pcMixer = PreferenceUtil.getValue(PreferenceUtil.PC_MIXER);
        String mixerIndex = "";
        String[] mixerIndexes = null;
		Shell shell = super.parentComposite.getShell();
        
        if (pcMixer.equals(PreferenceUtil.PC_MIXER_RECHECK)) {
			NoSoundDialog dialog = new NoSoundDialog(shell);
			dialog.create("Sound card setting",
					"Preparing to config sound card, make sure the speaker is not mute.", 1);
        } else {
            mixerIndexes = pcMixer.split(",");
        }
        
		boolean isFoundPCMixer = false;
		SystemRecorderMixerInfo pcMixerInfo = null;
        int iMixerIndex = 0;
		Iterator it = this.recorderInfoList.iterator();
		while (it.hasNext()) {
			SystemRecorderMixerInfo info = (SystemRecorderMixerInfo) it.next();
			if (info.getInfo() == Port.Info.MICROPHONE) {
				info.getSection().setDescription("Record audio from Microphone.\r\n\r\n");
				continue;
			}
			if (info.getInfo() == Port.Info.COMPACT_DISC) {
				info.getSection().setDescription("Record audio from CD/DVD rom.\r\n\r\n");
				continue;
			}
			if (info.getInfo() == Port.Info.LINE_IN) {
				info.getSection().setDescription("Record audio from Line In port.\r\n\r\n");
				continue;
			}
            if (pcMixer.equals(PreferenceUtil.PC_MIXER_RECHECK)) {
				// check the current recording soure and set the volum and balance.
				info.getBtSelect().setSelection(true);
	            info.getBtSelect().notifyListeners(SWT.Selection, new Event());	            if (info.getScaleVolumn().isEnabled()) {
	                info.getScaleVolumn().setSelection((info.getScaleVolumn().getMaximum() +
	                        info.getScaleVolumn().getMinimum()) / 2);
	                info.getScaleVolumn().notifyListeners(SWT.Selection, new Event());
	            }
	            if (info.getScaleBalance().isEnabled()) {
	                info.getScaleBalance().setSelection((info.getScaleBalance().getMaximum() +
	                        info.getScaleBalance().getMinimum()) / 2);
	                info.getScaleBalance().notifyListeners(SWT.Selection, new Event());
	            }
				if (new CheckPCMixerDialog(shell).create(info.getSection().getText())) {
					isFoundPCMixer = true;
					pcMixerInfo = info;
					info.getSection().setDescription("Capture any sound played by computer including live Internet radio(recommend!).\r\n\r\n");
                    mixerIndex = mixerIndex + this.recorderInfoList.indexOf(info) + ",";
				} else {
					info.getSection().setDescription("No description.\r\n\r\n");
	            }
            } else {
                if (iMixerIndex < mixerIndexes.length && !mixerIndexes[iMixerIndex].equals("") &&
                    this.recorderInfoList.indexOf(info) == Integer.parseInt(mixerIndexes[iMixerIndex])) {
					info.getSection().setDescription("Capture any sound played by computer including live Internet radio(recommend!).\r\n\r\n");
                    iMixerIndex++;
                } else {
					info.getSection().setDescription("No description.\r\n\r\n");
                }
            }
		}
        if (pcMixer.equals(PreferenceUtil.PC_MIXER_RECHECK)) {
			if (!isFoundPCMixer) {
                PreferenceUtil.setValue(PreferenceUtil.PC_MIXER, PreferenceUtil.PC_MIXER_NOTFOUND);
				this.defaultSelectedInfo.getBtSelect().setSelection(true);
				this.defaultSelectedInfo.getBtSelect().notifyListeners(SWT.Selection, new Event());
			} else {
                PreferenceUtil.setValue(PreferenceUtil.PC_MIXER, mixerIndex.substring(0, mixerIndex.length() - 1));
				pcMixerInfo.getBtSelect().setSelection(true);
				pcMixerInfo.getBtSelect().notifyListeners(SWT.Selection, new Event());
				if (pcMixerInfo != this.defaultSelectedInfo) {
				    this.defaultSelectedInfo.getSection().setExpanded(false);
				    pcMixerInfo.getSection().setExpanded(true);
				}
			}
        }
		this.form.getBody().layout(true);
	}
	
	private void create() {
		
		super.tabFolder.setSingle(true);
		
		// Recorder mixer tab item
		CTabItem recorderMixer = new CTabItem(tabFolder, SWT.NONE);
		recorderMixer.setImage(ImageUtil.getImage("images/recordSouceList.gif"));
		this.form = super.kit.createForm(tabFolder);
		recorderMixer.setControl(this.form);
		this.form.getBody().setLayout(new GridLayout());
		
		// Create system mixer panel
		List portMixers = SystemMixer.getPortMixers();
		// No mix found, make sure whether the user install the sound card.
		if (portMixers.size() == 0) {
		    recorderMixer.setText("No recorder source found.");
		} else {
			Iterator iterator = portMixers.iterator();
			while (iterator.hasNext()) {
				Mixer mixer = (Mixer) iterator.next();
		        recorderMixer.setText("Recording Source List");
				createMixerPanel(mixer);
			}
		}	
		// tabFolder.setSelection(0);
	}
	
    // Create the label with mixername and get all the ports in this mixer.
	private void createMixerPanel(Mixer mixer) {
        String	mixername = mixer.getMixerInfo().getName();
    	super.kit.createLabel(this.form.getBody(), mixername);
		Port.Info[]	infosToCheck = SystemMixer.getPortInfo(mixer);
		for (int i = 0; i < infosToCheck.length; i++) {
			Port port = SystemMixer.getOpenedPort(mixer, infosToCheck[i]);
			if (port != null) {
				this.createPortPanel(port);
			}
			// port.close();
		}
	}
    
    // Create the section with specified port and get all the controls in this port.
	private void createPortPanel(Port port) {
		// String	strPortName = ((Port.Info) port.getLineInfo()).getName();
		// out("Portname:" + strPortName);
		Control[]	aControls = port.getControls();
		if (aControls.length > 1) {
			// In fact in the windows system, it named SPEAKER port, and it contains play controls.
			// out("ignore the port " + strPortName + " that contains more than one control.");
		} else {
			// this is record control.
			Control[] subControls = SystemMixer.getCompoundControlMembers(aControls[0]);
			if (subControls != null) {
				SystemRecorderMixerInfo info = this.createRecorderSection(aControls[0], subControls, port.getLineInfo());
				recorderInfoList.add(info);
			}
		}
	}
	
    // Create Recorder source section
    private SystemRecorderMixerInfo createRecorderSection(Control compoundControl, Control[] subControls, Line.Info info) {
        Section section = super.kit.createSection(this.form.getBody(), Section.TITLE_BAR | Section.DESCRIPTION | Section.TWISTIE | Section.EXPANDED);
        section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        section.setText(compoundControl.getType().toString());
        section.setExpanded(false);
        section.setTitleBarBackground(super.orangeColor);
        section.setTitleBarGradientBackground(super.orangeColor);
        
        BooleanControl select = (BooleanControl) subControls[0];
        FloatControl volume = (FloatControl) subControls[1];
        FloatControl balance = (FloatControl) subControls[2];
        
        // Create composite contains volume, balance and select control.
        Composite composite = super.kit.createComposite(section);
        TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 1;
        composite.setLayout(layout);
        
        // Create floatControl label, number, scale.
        Scale scaleVolume = this.createFloatcontrolComposite(composite, volume, false, "Recording Volume:");
        Scale scaleBalance = this.createFloatcontrolComposite(composite, balance, true, "Channel Balance:");
        
        // create select label.
        final Button btSelect = super.kit.createButton(composite, "Select it as record source", SWT.CHECK);
        TableWrapData data = new TableWrapData();
        btSelect.setLayoutData(data);
        
        SystemRecorderMixerInfo systemRecorderMixerInfo = new SystemRecorderMixerInfo(section, btSelect, scaleVolume, scaleBalance, info);
        
    	if (select != null) {
	        btSelect.setSelection(select.getValue());
	        if (select.getValue()) {
	        	section.setExpanded(true);
	        	this.defaultSelectedInfo = systemRecorderMixerInfo;
	        }
	        btSelect.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					RecorderMixerSection.this.checkSelectControl(btSelect);
				}
	        });
	        btSelect.setData(select);
	        this.selectControlList.add(btSelect);
    	} else {
    		btSelect.setEnabled(false);
    	}
        
    	// set client to section.
        section.setClient(composite);
        
        return systemRecorderMixerInfo;
    }
    
    private Scale createFloatcontrolComposite(Composite composite, FloatControl floatControl, boolean isBalance, String desc) {
        // create volume/balance composite contains volume/balance label and volume/balance number.
        Composite volumeBalanceComposite = super.kit.createComposite(composite);
        FillLayout fillLayout = new FillLayout();
        volumeBalanceComposite.setLayout(fillLayout);
        // Label lbVolumeBalance
        super.kit.createLabel(volumeBalanceComposite, desc);
        Label lbNumber = super.kit.createLabel(volumeBalanceComposite, "", SWT.CENTER);
        // create volume/balance scale.
        Scale scale = new Scale(composite, SWT.HORIZONTAL);
        String number = this.initScale(scale, floatControl, lbNumber, isBalance);
        lbNumber.setText(number);
        return scale;
    }
    
    private String initScale(final Scale scale, final FloatControl control, final Label number, final boolean isBalance) {
		scale.setBackground(super.whiteColor);
        if (control != null) {
    	    int maximun = (int) ((control.getMaximum() - control.getMinimum()) * 100);
    	    int current = (int) ((control.getValue() - control.getMinimum()) * 100);
			scale.setMaximum(maximun);
			scale.setSelection(current);
			scale.setIncrement(maximun / 100 * 5);
			scale.setPageIncrement(maximun / 100 * 10);
			scale.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					float value = scale.getSelection() / 100f + control.getMinimum();
					control.setValue(value);
					String scaleNumber = RecorderMixerSection.this.getScaleNumber(scale.getSelection(), control.getMinimum(), isBalance);
					number.setText(scaleNumber);
				}
			});
			return this.getScaleNumber(scale.getSelection(), control.getMinimum(), isBalance);
        } else {
        	scale.setSelection(50);
        	scale.setEnabled(false);
        	return "Disable";
        }
    }
    
    private String getScaleNumber(int scalValue, float minimunFloatControlValue, boolean isBalance) {
		if (!isBalance) {
		    return String.valueOf(scalValue) + "%";
		} else {
		    return String.valueOf((int) (scalValue + minimunFloatControlValue * 100)) + "%";
		}
    }
    
    private void checkSelectControl(final Button btSelect) {
    	BooleanControl select = (BooleanControl) btSelect.getData();
	    select.setValue(btSelect.getSelection());
		btSelect.setSelection(select.getValue());
		Iterator it = this.selectControlList.iterator();
		while (it.hasNext()) {
			Button bt = (Button) it.next();
			BooleanControl bc = (BooleanControl) bt.getData();
			bt.setSelection(bc.getValue());
		}
    }
}