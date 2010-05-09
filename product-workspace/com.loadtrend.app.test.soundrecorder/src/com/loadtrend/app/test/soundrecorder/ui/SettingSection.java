package com.loadtrend.app.test.soundrecorder.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.Section;

import com.loadtrend.app.test.soundrecorder.SoundRecorder;
import com.loadtrend.app.test.soundrecorder.info.RecordingScheduleInfo;
import com.loadtrend.app.test.soundrecorder.ui.dialog.ScheduleDialog;
import com.loadtrend.app.test.soundrecorder.util.ColorUtil;
import com.loadtrend.app.test.soundrecorder.util.FormatUtil;
import com.loadtrend.app.test.soundrecorder.util.IOOperation;
import com.loadtrend.app.test.soundrecorder.util.ImageUtil;
import com.loadtrend.app.test.soundrecorder.util.PreferenceUtil;
import com.loadtrend.app.test.soundrecorder.win32native.RegisterHotKey;

public class SettingSection extends AbstractSharedSection {
	
	private SoundRecorder soundRecorder = null;

	public SettingSection(SoundRecorder soundRecorder, Composite parentComposite) {
		super(parentComposite);
		this.soundRecorder = soundRecorder;
        this.create();
	}
	
	private void create() {
		
		// wave recording setting tab item
		CTabItem wavRecording = new CTabItem(super.tabFolder, SWT.NONE);
		wavRecording.setText("WAV Setting");
		wavRecording.setImage(ImageUtil.getImage("images/wav.gif"));
		
		Form wavRecordingform = super.kit.createForm(super.tabFolder);
		wavRecording.setControl(wavRecordingform);
		FillLayout layout = new FillLayout(SWT.VERTICAL);
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		wavRecordingform.getBody().setLayout(layout);
		
		Composite composite = new Composite(wavRecordingform.getBody(), SWT.NONE);
		FillLayout fillLayout = new FillLayout();
		composite.setLayout(fillLayout);
		this.createRadioSection(composite, "Sample Bit", PreferenceUtil.SAMPLE_BIT, new String[] {"8 Bit", "16 Bit"});
		
		this.createRadioSection(composite, "Channels", PreferenceUtil.CHANNEL, new String[] {"Mono", "Stereo"});
		
		composite = new Composite(wavRecordingform.getBody(), SWT.NONE);
		composite.setLayout(fillLayout);
		this.createRadioSection(composite, "Sample Rate", PreferenceUtil.SAMPLE_RATE, new String[] {"8000 Hz", "11025 Hz", "22050 Hz", "44100 Hz"});
		
		
		// mp3 recording setting tab item.
		CTabItem mp3Recording = new CTabItem(super.tabFolder, SWT.NONE);
		mp3Recording.setText("MP3 Setting");
		mp3Recording.setImage(ImageUtil.getImage("images/mp3.gif"));
		
		Form mp3Recordingform = super.kit.createForm(super.tabFolder);
		mp3Recording.setControl(mp3Recordingform);
		mp3Recordingform.getBody().setLayout(layout);
		
		composite = new Composite(mp3Recordingform.getBody(), SWT.NONE);
		composite.setLayout(fillLayout);
		this.createRadioSection(composite, "Quality Selection", PreferenceUtil.MP3_QUALITY, new String[] {"Lowest", "Low", "Middle", "High", "Highest"});
		
		composite = new Composite(mp3Recordingform.getBody(), SWT.NONE);
		composite.setLayout(fillLayout);
		this.createRadioSection(composite, "Sample Bit Rate Selection", PreferenceUtil.MP3_BIT_RATE,
				                                            new String[] {"64 KBit/s", "128 KBit/s", "192 KBit/s", "256 KBit/s", "Variable Bit Rate"});
		
        // Recording schedule setting tab item.
		CTabItem schedule = new CTabItem(super.tabFolder, SWT.NONE);
		schedule.setText("Recording Schedule");
		schedule.setImage(ImageUtil.getImage("images/schedule.gif"));
		
		Form scheduleForm = super.kit.createForm(super.tabFolder);
		schedule.setControl(scheduleForm);
		scheduleForm.getBody().setLayout(layout);
		this.createTableSection(scheduleForm.getBody(), "Right click to edit the recording schedule planning");
		
        // General recording setting tab item.
		CTabItem generalSetting = new CTabItem(super.tabFolder, SWT.NONE);
		generalSetting.setText("General Setting");
		generalSetting.setImage(ImageUtil.getImage("images/general.gif"));
		
		Form generalSettingform = super.kit.createForm(super.tabFolder);
		generalSetting.setControl(generalSettingform);
		generalSettingform.getBody().setLayout(layout);
		
		composite = new Composite(generalSettingform.getBody(), SWT.NONE);
		composite.setLayout(fillLayout);
		this.createRadioSection(composite, "Recording Format", PreferenceUtil.RECORD_FORMAT, new String[] {"WAV format", "MP3 format"});
		this.createTextSection(composite, "Output Directory", PreferenceUtil.OUTPUT_DIRECOTRY);
        
        composite = new Composite(generalSettingform.getBody(), SWT.NONE);
        composite.setLayout(fillLayout);
        this.createRadioTextSection(composite, "Output Filename");
        this.createColorLabelSection(composite, "Interface Color", PreferenceUtil.MAIN_COLOR);
        
        // Feature setting tab item.
		CTabItem feature = new CTabItem(super.tabFolder, SWT.NONE);
		feature.setText("Features");
		feature.setImage(ImageUtil.getImage("images/feature.gif"));
		
		Form featureForm = super.kit.createForm(super.tabFolder);
		feature.setControl(featureForm);
		featureForm.getBody().setLayout(layout);
		
		composite = new Composite(featureForm.getBody(), SWT.NONE);
		composite.setLayout(fillLayout);
		this.createFeatureNoiseGatingSection(composite, "Noise Gating");
		composite = new Composite(featureForm.getBody(), SWT.NONE);
		composite.setLayout(fillLayout);
		this.createFeatureHotKeySection(composite, "HotKey Setting");
		
		super.tabFolder.setSelection(schedule);
	}
	
	private void createFeatureNoiseGatingSection(Composite parent, String sectionText) {
        Section section = super.kit.createSection(parent, Section.DESCRIPTION | Section.TITLE_BAR | Section.TREE_NODE | Section.EXPANDED);
        section.setText(sectionText);
        section.setTitleBarBackground(super.orangeColor);
        section.setTitleBarGradientBackground(super.orangeColor);
        section.setDescription("Pause recording when the volume is lower than");
        
        Composite featureComposite = super.kit.createComposite(section);
        GridLayout gridLayout = new GridLayout(5, false);
        featureComposite.setLayout(gridLayout);
        
        final Spinner volumeSpinner = new Spinner(featureComposite, SWT.BORDER);
        volumeSpinner.setMinimum(1);
        volumeSpinner.setMaximum(100);
        volumeSpinner.setSelection(Integer.parseInt(PreferenceUtil.getValue(PreferenceUtil.NOISE_GATE_PERCENT)));
        volumeSpinner.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				PreferenceUtil.setValue(PreferenceUtil.NOISE_GATE_PERCENT, String.valueOf(volumeSpinner.getSelection()));
			}
        });

        super.kit.createLabel(featureComposite, "percent for ");
        final Text durationText = super.kit.createText(featureComposite,
        		                                       PreferenceUtil.getValue(PreferenceUtil.NOISE_GATE_DURATION));
        durationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        durationText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (!durationText.getText().trim().matches("[0-9]+")) {
					durationText.setText(PreferenceUtil.getValue(PreferenceUtil.NOISE_GATE_DURATION));
				} else {
					PreferenceUtil.setValue(PreferenceUtil.NOISE_GATE_DURATION, durationText.getText());
				}
			}
        });
        
        super.kit.createLabel(featureComposite, "milliseconds, resume recording once sound loudly.");
        
        final Button enableNoiseGating = super.kit.createButton(featureComposite, "Enable It Now", SWT.CHECK);
        enableNoiseGating.setSelection(Boolean.valueOf(PreferenceUtil.getValue(PreferenceUtil.NOISE_GATE_ENABLE)).booleanValue());
        enableNoiseGating.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				PreferenceUtil.setValue(PreferenceUtil.NOISE_GATE_ENABLE, String.valueOf(enableNoiseGating.getSelection()));
			}
        });
        
        super.kit.paintBordersFor(featureComposite);
        
        section.setClient(featureComposite);
	}
	
	private void createFeatureHotKeySection(Composite parent, String sectionText) {
        Section section = super.kit.createSection(parent, Section.DESCRIPTION | Section.TITLE_BAR | Section.TREE_NODE | Section.EXPANDED);
        section.setText(sectionText);
        section.setTitleBarBackground(super.orangeColor);
        section.setTitleBarGradientBackground(super.orangeColor);
        section.setDescription("Ctrl, Shift, Alt combined with digit or letter and function key are recommended to be hotkey.");
        
        Composite hotKeyComposite = super.kit.createComposite(section);
        GridLayout gridLayout = new GridLayout(8, false);
        hotKeyComposite.setLayout(gridLayout);
        
        GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        
        MyFocusListener focusListener = new MyFocusListener();
        
        super.kit.createLabel(hotKeyComposite, "Record");
        final Text txtRecord = super.kit.createText(hotKeyComposite, PreferenceUtil.getValue(PreferenceUtil.RECORD_HOTKEY));
        txtRecord.setLayoutData(gridData);
        txtRecord.addKeyListener(new MyKeyListener(txtRecord, PreferenceUtil.RECORD_HOTKEY));
        txtRecord.addFocusListener(focusListener);
        
        super.kit.createLabel(hotKeyComposite, "Pause");
        Text txtPause = super.kit.createText(hotKeyComposite, PreferenceUtil.getValue(PreferenceUtil.PAUSE_HOTKEY));
        txtPause.setLayoutData(gridData);
        txtPause.addKeyListener(new MyKeyListener(txtPause, PreferenceUtil.PAUSE_HOTKEY));
        txtPause.addFocusListener(focusListener);
        
        super.kit.createLabel(hotKeyComposite, "Stop");
        Text txtStop = super.kit.createText(hotKeyComposite, PreferenceUtil.getValue(PreferenceUtil.STOP_HOTKEY));
        txtStop.setLayoutData(gridData);
        txtStop.addKeyListener(new MyKeyListener(txtStop, PreferenceUtil.STOP_HOTKEY));
        txtStop.addFocusListener(focusListener);
        
        super.kit.createLabel(hotKeyComposite, "Playback");
        Text txtPlayback = super.kit.createText(hotKeyComposite, PreferenceUtil.getValue(PreferenceUtil.PLAYBACK_HOTKEY));
        txtPlayback.setLayoutData(gridData);
        txtPlayback.addKeyListener(new MyKeyListener(txtPlayback, PreferenceUtil.PLAYBACK_HOTKEY));
        txtPlayback.addFocusListener(focusListener);
        
        super.kit.paintBordersFor(hotKeyComposite);
        section.setClient(hotKeyComposite);
	}
	
	private class MyFocusListener implements FocusListener {
		public void focusGained(FocusEvent e) {
			RegisterHotKey.unRegisterHotKey();
		}

		public void focusLost(FocusEvent e) {
	        // register hotkey
			RegisterHotKey.registerHotKey(new String[] {PreferenceUtil.getValue(PreferenceUtil.RECORD_HOTKEY), PreferenceUtil.getValue(PreferenceUtil.PAUSE_HOTKEY),
					                                    PreferenceUtil.getValue(PreferenceUtil.STOP_HOTKEY), PreferenceUtil.getValue(PreferenceUtil.PLAYBACK_HOTKEY)},
					                                    null);
		}
	}
	
	private class MyKeyListener implements KeyListener {

		private String[] MODIFIER_KEYS = new String[] { "Shift + ", "Ctrl + ",
				"Alt + ", "Alt + Shift + ", "Ctrl + Alt + ", "Ctrl + Shift + ",
				"Ctrl + Alt + Shift + " };

		private boolean isOnlyModifierKeyPressed = false;
		
		private Text text = null;
		
		private String hotkey = null;
		
		public MyKeyListener(Text text, String hotkey) {
			this.text = text;
			this.hotkey = hotkey;
		}
		
		public void keyPressed(KeyEvent e) {
			String preString = "";
			if (e.stateMask != SWT.NONE) {
				preString = this.getKeyString(e.stateMask);
			}
			this.text.setText(preString + this.getKeyString(e.keyCode));
			e.doit = false;
		}

		public void keyReleased(KeyEvent e) {
			if (this.isOnlyModifierKeyPressed) {
				this.text.setText(PreferenceUtil.getValue(hotkey));
			} else {
				PreferenceUtil.setValue(hotkey, this.text.getText());
				this.text.getShell().setFocus();
			}
		}

		private String getKeyString(int keyCode) {
			this.isOnlyModifierKeyPressed = true;
			switch (keyCode) {
			case SWT.SHIFT:
				return this.MODIFIER_KEYS[0];
			case SWT.CTRL:
				return this.MODIFIER_KEYS[1];
			case SWT.ALT:
				return this.MODIFIER_KEYS[2];
			case SWT.ALT | SWT.SHIFT:
				return this.MODIFIER_KEYS[3];
			case SWT.ALT | SWT.CTRL:
				return this.MODIFIER_KEYS[4];
			case SWT.CTRL | SWT.SHIFT:
				return this.MODIFIER_KEYS[5];
			case SWT.CTRL | SWT.SHIFT | SWT.ALT:
				return this.MODIFIER_KEYS[6];
			}
			// Function key handler
			if (keyCode >= SWT.F1 && keyCode <= SWT.F15) {
			    this.isOnlyModifierKeyPressed = false;
				return "F" + String.valueOf(keyCode - SWT.KEYCODE_BIT - 9);
			}
			char c = (char) keyCode;
			if (Character.isLetterOrDigit(c)) {
			    this.isOnlyModifierKeyPressed = false;
				return String.valueOf(c);
			}
			return "";
		}

	}
	
	private Button[] createRadioSection(Composite parent, String sectionText, final String preferenceKey, String[] radioName) {
		Section section = super.kit.createSection(parent, Section.TITLE_BAR | Section.TREE_NODE | Section.EXPANDED);
		// Section section = super.kit.createSection(parent, Section.TREE_NODE | Section.EXPANDED);
		section.setText(sectionText);
		section.setTitleBarBackground(super.orangeColor);
		section.setTitleBarGradientBackground(super.orangeColor);
        
		Composite sampleBitComposite = super.kit.createComposite(section);
		FillLayout fillLayout = new FillLayout();
		fillLayout.marginHeight = 10;
		sampleBitComposite.setLayout(fillLayout);
		
		final Button[] buttons = new Button[radioName.length];
		for (int i = 0; i < radioName.length; i++) {
			final int index = i;
		    buttons[i] = super.kit.createButton(sampleBitComposite, radioName[i], SWT.RADIO);
		    buttons[i].addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					PreferenceUtil.setValue(preferenceKey, String.valueOf(index));
				}
		    });
		}
		// Set value from properties.
		int j = Integer.parseInt(PreferenceUtil.getValue(preferenceKey));
		buttons[j].setSelection(true);
		
		section.setClient(sampleBitComposite);
		
		return buttons;
	}
    
    private Text createTextSection(Composite parent, String sectionText, final String preferenceKey) {
        Section section = super.kit.createSection(parent, Section.TITLE_BAR | Section.TREE_NODE | Section.EXPANDED);
        // Section section = super.kit.createSection(parent, Section.TREE_NODE | Section.EXPANDED);
        section.setText(sectionText);
        section.setTitleBarBackground(super.orangeColor);
        section.setTitleBarGradientBackground(super.orangeColor);
        
        Composite outputComposite = super.kit.createComposite(section);
        FillLayout fillLayout = new FillLayout();
        fillLayout.marginHeight = 10;
        fillLayout.marginWidth = 5;
        outputComposite.setLayout(fillLayout);
        final Text text = super.kit.createText(outputComposite, PreferenceUtil.getValue(preferenceKey), SWT.MULTI | SWT.WRAP);
        text.setEditable(false);
        text.addMouseListener(new MouseAdapter() {
            public void mouseDown(MouseEvent e) {
                DirectoryDialog dialog = new DirectoryDialog(text.getShell());
                dialog.setText("Select Path");
                dialog.setMessage("Select path to save recording result files.");
                dialog.setFilterPath(PreferenceUtil.getValue(preferenceKey));
                String path = dialog.open();
                if (path != null) {
                    PreferenceUtil.setValue(preferenceKey, path);
                    text.setText(path);
                }
            }
        });
        super.kit.paintBordersFor(outputComposite);
        section.setClient(outputComposite);
        
        return text;
    }
    
    private void createRadioTextSection(Composite parent, String sectionText) {
        Section section = super.kit.createSection(parent, Section.TITLE_BAR | Section.TREE_NODE | Section.EXPANDED);
        // Section section = super.kit.createSection(parent, Section.TREE_NODE | Section.EXPANDED);
        section.setText(sectionText);
        section.setTitleBarBackground(super.orangeColor);
        section.setTitleBarGradientBackground(super.orangeColor);
        
        Composite radioTextComposite = super.kit.createComposite(section);
        FillLayout fillLayout = new FillLayout();
        fillLayout.marginHeight = 5;
        radioTextComposite.setLayout(fillLayout);
        
        // date raido.
        Composite composite = super.kit.createComposite(radioTextComposite);
        composite.setLayout(new GridLayout(1, false));
        final Button dateRadio = super.kit.createButton(composite, "Date + Time", SWT.RADIO);
        boolean isDateRadioTrue = Boolean.valueOf(PreferenceUtil.getValue(PreferenceUtil.OUTPUT_FILENAME_DATETIME)).booleanValue();
        dateRadio.setSelection(isDateRadioTrue);
        
        // custom radio.
        composite = super.kit.createComposite(radioTextComposite);
        composite.setLayout(new GridLayout(2, false));
        final Button customRadio = super.kit.createButton(composite, "Customize", SWT.RADIO);
        customRadio.setSelection(!isDateRadioTrue);
        
        // custom text.
        final Text text = super.kit.createText(composite, PreferenceUtil.getValue(PreferenceUtil.OUTPUT_FILENAME_CUSTOMIZE));
        text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        text.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                PreferenceUtil.setValue(PreferenceUtil.OUTPUT_FILENAME_CUSTOMIZE, text.getText());
            }
        });
        
        // add selection adapter.
        customRadio.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                PreferenceUtil.setValue(PreferenceUtil.OUTPUT_FILENAME_DATETIME, "false");
                dateRadio.setSelection(false);
                text.setFocus();
            }
        });
        dateRadio.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                PreferenceUtil.setValue(PreferenceUtil.OUTPUT_FILENAME_DATETIME, "true");
                customRadio.setSelection(false);
            }
        });
        super.kit.paintBordersFor(composite);
        
        section.setClient(radioTextComposite);
    }
    
    private void createColorLabelSection(Composite parent, String sectionText, final String preferenceKey) {
        Section section = super.kit.createSection(parent, Section.TITLE_BAR | Section.TREE_NODE | Section.EXPANDED);
        // Section section = super.kit.createSection(parent, Section.TREE_NODE | Section.EXPANDED);
        section.setText(sectionText);
        section.setTitleBarBackground(super.orangeColor);
        section.setTitleBarGradientBackground(super.orangeColor);
        
        Composite colorComposite = super.kit.createComposite(section);
        GridLayout gridLayout = new GridLayout(1, true);
        gridLayout.marginHeight = 5;
        colorComposite.setLayout(gridLayout);
        
        final Label label = super.kit.createLabel(colorComposite, "");
        GridData gridData = new GridData(100, 20);
        label.setLayoutData(gridData);
        label.setBackground(ColorUtil.getColor(PreferenceUtil.getValue(preferenceKey)));
        label.addMouseListener(new MouseAdapter() {
            public void mouseUp(MouseEvent e) {
                ColorDialog colorDialog = new ColorDialog(label.getShell());
                colorDialog.setRGB(label.getBackground().getRGB());
                RGB rgb = colorDialog.open();
                if (rgb != null) {
                    PreferenceUtil.setValue(preferenceKey, ColorUtil.rgbToString(rgb));
                    label.setBackground(ColorUtil.getColor(ColorUtil.rgbToString(rgb)));
                    SettingSection.super.changeInterfaceColor();
                }
            }
        });
        
        section.setClient(colorComposite);
    }
    
    private void createTableSection(Composite parent, String sectionText) {
        Section section = super.kit.createSection(parent, Section.TITLE_BAR | Section.TREE_NODE | Section.EXPANDED);
        section.setText(sectionText);
        section.setTitleBarBackground(super.orangeColor);
        section.setTitleBarGradientBackground(super.orangeColor);
		
		// list table
        final Table listTable = super.kit.createTable(section, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER | SWT.CHECK);
        section.setClient(listTable);
        // listTable.setLayout(tableLayout);
        listTable.setHeaderVisible(true);
        listTable.setLinesVisible(true);
        TableColumn enableColumn = new TableColumn(listTable, SWT.CENTER);
        TableColumn startTimeColumn = new TableColumn(listTable, SWT.CENTER);
        TableColumn durationColumn = new TableColumn(listTable, SWT.CENTER);
        TableColumn dateColumn = new TableColumn(listTable, SWT.CENTER);
        TableColumn urlColumn = new TableColumn(listTable, SWT.LEFT);
        enableColumn.setText("Enable");
        startTimeColumn.setText("Start Time");
        durationColumn.setText("End Time");
        dateColumn.setText("Date");
        urlColumn.setText("Radio Url");
        enableColumn.setWidth(75);
        startTimeColumn.setWidth(75);
        durationColumn.setWidth(75);
        dateColumn.setWidth(75);
        urlColumn.setWidth(250);
        
        // Read schedule class from file.
        List list = null;
        if (IOOperation.isFileExist(PreferenceUtil.SCHEDULE_DATA)) {
            list = (List) IOOperation.readObject(PreferenceUtil.SCHEDULE_DATA);
        } else {
            list = new LinkedList();
        }
        final List scheduleList = list;
        
        // handler the action to select the check button in the table.
        listTable.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if (e.detail == SWT.CHECK) {
                    TableItem item = (TableItem) e.item;
                    int selectIndex = listTable.indexOf(item);
                    RecordingScheduleInfo info = (RecordingScheduleInfo) scheduleList.get(selectIndex);
                    info.setEnable(item.getChecked());
                    IOOperation.writeObject(PreferenceUtil.SCHEDULE_DATA, scheduleList);
                    SettingSection.this.scheduleRecord(scheduleList);
                }
            }
        });
        
        // fill in the table with schedule list saved.
        this.refreshTable(listTable, scheduleList);
        
        // Create context menu for list table.
        Menu menu = new Menu(listTable);
        MenuItem item = new MenuItem(menu, SWT.PUSH);
        item.setText("Add Schedule");
        item.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                int status = new ScheduleDialog(parentComposite.getShell(), scheduleList).create(null);
                if (status == SWT.OK) SettingSection.this.refreshTable(listTable, scheduleList);
            }
        });
        item = new MenuItem(menu, SWT.PUSH);
        item.setText("Edit Schedule");
        item.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                int[] selectIndexes = listTable.getSelectionIndices();
                if (selectIndexes.length == 0) return;
                RecordingScheduleInfo info = (RecordingScheduleInfo) scheduleList.get(selectIndexes[0]);
                int status = new ScheduleDialog(parentComposite.getShell(), scheduleList).create(info);
                if (status == SWT.OK) SettingSection.this.refreshTable(listTable, scheduleList);
            }
        });
        item = new MenuItem(menu, SWT.PUSH);
        item.setText("Delete Schedule");
        item.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                int[] selectIndexes = listTable.getSelectionIndices();
                if (selectIndexes.length == 0) return;
                List tempList = new ArrayList();
                for (int i = 0; i < selectIndexes.length; i++) {
                    tempList.add(scheduleList.get(selectIndexes[i]));
                }
                scheduleList.removeAll(tempList);
                IOOperation.writeObject(PreferenceUtil.SCHEDULE_DATA, scheduleList);
                SettingSection.this.refreshTable(listTable, scheduleList);
            }
        });
        item = new MenuItem(menu, SWT.PUSH);
        item.setText("Record From Url Now!");
        item.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                int[] selectIndexes = listTable.getSelectionIndices();
                if (selectIndexes.length == 0) return;
                RecordingScheduleInfo info = (RecordingScheduleInfo) scheduleList.get(selectIndexes[0]);
                soundRecorder.record(info, true);
            }
        });
        
        listTable.setMenu(menu);
    }
    
    private void refreshTable(Table listTable, List list) {
        listTable.removeAll();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            RecordingScheduleInfo info = (RecordingScheduleInfo) it.next();
            TableItem item = new TableItem(listTable, SWT.NONE);
            
            // Get once or daily or weekly String
            String date = info.getDate() == null ? null : info.getDate();
            if (date == null) date = (date == null && info.isDaily()) ? "Daily" : null;
            if (date == null) date = FormatUtil.getDayOfWeek(info.getDayOfWeek());
            String[] texts = new String[] {"Enable",
                                           FormatUtil.getFormatedTime(info.getStartTime()),
                                           FormatUtil.getFormatedTime(info.getEndTime()),
                                           date,
                                           info.getUrl()
                                           };
            item.setText(texts);
            item.setChecked(info.isEnable());
        }
        // Create timer to implements the recording schedule.
        this.scheduleRecord(list);
    }
    
    // Create timer to implements the recording schedule.
    private void scheduleRecord(List scheduleList) {
    	// Cancel the valid schedule firstly.
    	Iterator it = this.validScheduleList.iterator();
    	while (it.hasNext()) {
    		Timer timer = (Timer) it.next();
    		timer.cancel();
    	}
    	this.validScheduleList.clear();
    	
        it = scheduleList.iterator();
        while (it.hasNext()) {
        	final RecordingScheduleInfo info = (RecordingScheduleInfo) it.next();
        	if (!info.isEnable()) continue;
        	final int duration = (info.getEndTime() - info.getStartTime()) * 1000;
        	if (duration <= 0) continue;
        	Date firstTimeDate = null;
        	long period = 0;
        	if (info.getDate() != null) {
        		firstTimeDate = FormatUtil.getDate(info.getDate(), info.getStartTime());
        		if (firstTimeDate.before(new Date())) continue;
        	} else if (info.isDaily()) {
        		Date date = new Date();
        		long firstTime = info.getStartTime() * 1000 +
        		                 new Date(date.getYear(), date.getMonth(), date.getDate()).getTime();
        		if (date.getTime() >  firstTime) {
        			firstTimeDate = new Date(firstTime + 3600 * 24 * 1000);
        		} else {
        			firstTimeDate = new Date(firstTime);
        		}
        		period = 3600 * 24 * 1000;
        	} else {
        		Date date = new Date();
        		int iWeekMilliSeconds = date.getDay() * 3600 * 24 * 1000 +
        		                        (date.getHours() * 3600 + date.getMinutes() * 60 + date.getSeconds()) * 1000;
        		long firstTime = info.getStartTime() * 1000 + info.getDayOfWeek() * 3600 * 24 * 1000 +
        		                 (date.getTime() - iWeekMilliSeconds);
        		if (date.getTime() >  firstTime) {
        			firstTimeDate = new Date(firstTime + 3600 * 24 * 1000 * 7);
        		} else {
        			firstTimeDate = new Date(firstTime);
        		}
        		period = 3600 * 24 * 1000 * 7;
        	}
        	Timer timer = new Timer();
        	TimerTask timerTask = new TimerTask() {
                private int recordDuration = 0;
				public void run() {
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							recordDuration = soundRecorder.record(info, false);
						}
					});
					if (recordDuration > 0) {
						boolean isInterrupted = false;
						synchronized (info) {
							try {
								info.setInterrupted(false);
								info.wait(recordDuration * 1000);
								isInterrupted = info.isInterrupted();
								info.setInterrupted(false);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if (!isInterrupted) {
							Display.getDefault().syncExec(new Runnable() {
								public void run() {
							        soundRecorder.stopRecord();
								}
							});
						}
					}
				}
        	};
        	if (period == 0) {
        	    timer.schedule(timerTask, firstTimeDate);
        	} else {
        	    timer.schedule(timerTask, firstTimeDate, period);
        	}
        	validScheduleList.add(timer);
        }
    }
    
    private List validScheduleList = new ArrayList();
}
