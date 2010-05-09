package com.loadtrend.app.test.soundrecorder.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;

import com.loadtrend.app.test.soundrecorder.SoundRecorder;
import com.loadtrend.app.test.soundrecorder.info.RecordingResultInfo;
import com.loadtrend.app.test.soundrecorder.info.RecordingScheduleInfo;
import com.loadtrend.app.test.soundrecorder.util.IOOperation;
import com.loadtrend.app.test.soundrecorder.util.ImageUtil;
import com.loadtrend.app.test.soundrecorder.util.PreferenceUtil;

public class RecordingSection extends AbstractSharedSection {
	
	protected List resultRecordingList = null;
	
	private RecordingResultListItem recordingResultListItem = null;
	
	private RecordingControlItem recordingControlItem = null;
	
	private RecordingRadioUrlItem recordingRadioUrlItem = null;
	
	private RecordingEditItem recordingEditItem = null;
	
	protected SoundRecorder soundRecorder = null;
	
	private CTabItem recordingTabItem = null;
	
	private CTabItem radioUrlTabItem = null;
	
	protected CTabItem aboutTabItem = null;
	
	private CTabItem recordingEditTabItem = null;
	
	public RecordingSection(SoundRecorder soundRecorder, Composite parentComposite) {
		super(parentComposite);
		this.soundRecorder = soundRecorder;
        this.create();
	}
	
	private void create() {
		
		// Load the recording result files.
		if (IOOperation.isFileExist(PreferenceUtil.RESULT_DATA)) {
			// remove the not exist file firstly.
			this.resultRecordingList = (List) IOOperation.readObject(PreferenceUtil.RESULT_DATA);
			List tempList = new ArrayList();
			Iterator it = this.resultRecordingList.iterator();
			while (it.hasNext()) {
				RecordingResultInfo info = (RecordingResultInfo) it.next();
				if (!IOOperation.isFileExist(info.getLocation())) {
					tempList.add(info);
				}
			}
			// Get the valid result files.
			this.resultRecordingList.removeAll(tempList);
			IOOperation.writeObject(PreferenceUtil.RESULT_DATA, this.resultRecordingList);
		} else {
			this.resultRecordingList = new ArrayList();
		}
		
		// recording control details tab item
		this.recordingControlItem = new RecordingControlItem(this);
		this.recordingTabItem = this.recordingControlItem.createItem();
		recordingTabItem.setText("Recording Panel");
		recordingTabItem.setImage(ImageUtil.getImage("images/recording.gif"));
		
		// recording edit tab item
		this.recordingEditItem = new RecordingEditItem(this);
		this.recordingEditTabItem = this.recordingEditItem.createItem();
		this.recordingEditTabItem.setText("Sound Editor");
		this.recordingEditTabItem.setImage(ImageUtil.getImage("images/recording_edit.gif"));
		
		// recording result list tab item
		this.recordingResultListItem = new RecordingResultListItem(this);
		CTabItem resultList = this.recordingResultListItem.createItem();
		resultList.setText("History List");
		resultList.setImage(ImageUtil.getImage("images/history_list.gif"));
		
		// recording raido url item
		this.recordingRadioUrlItem = new RecordingRadioUrlItem(this);
		this.radioUrlTabItem = this.recordingRadioUrlItem.createItem();
		radioUrlTabItem.setText("Internet Radio");
		radioUrlTabItem.setImage(ImageUtil.getImage("images/radio_url.ico"));
		
		// recording about item
		RecordingAboutItem recordingAboutItem = new RecordingAboutItem(this);
		this.aboutTabItem = recordingAboutItem.createItem();
		aboutTabItem.setText("About Us");
		aboutTabItem.setImage(ImageUtil.getImage("images/about.gif"));
		
		super.tabFolder.setSelection(recordingTabItem);
	}
	
	/**
	 * Play result list.
	 * @param filename
	 * @param duration
	 */
	public void notifyToPlay(String filename, double duration) {
		super.tabFolder.setSelection(recordingTabItem);
		this.recordingControlItem.playFile(filename, duration);
	}
	
	/**
	 * Schedule record.
	 * @param info
	 * @return
	 */
	public int notifyToRecord(RecordingScheduleInfo info, boolean runNow) {
		super.tabFolder.setSelection(recordingTabItem);
		return this.recordingControlItem.record(info, runNow);
	}
	
	/**
	 * Stop schedule record.
	 *
	 */
	public void notifyToStopRecord() {
		this.recordingControlItem.stopRecord();
	}
	
	public void notifyToEdit(String filename) {
		super.tabFolder.setSelection(this.recordingEditTabItem);
		this.recordingEditItem.importSound(filename);
	}
	
	/**
	 * @param url nullable
	 */
	public void notifyToBrowser(String url) {
		if (url == null) {
			this.recordingRadioUrlItem.initBrowser();
		} else {
		    super.tabFolder.setSelection(radioUrlTabItem);
		    this.recordingRadioUrlItem.setUrl(url);
		}
	}
}
