package com.loadtrend.app.test.soundrecorder.info;

import javax.sound.sampled.Line;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.ui.forms.widgets.Section;

public class SystemRecorderMixerInfo {
	private Section section = null;
	private Button btSelect = null;
	private Scale scaleVolumn = null;
	private Scale scaleBalance = null;
	private Line.Info info = null;
	public SystemRecorderMixerInfo(Section section, Button btSelect, Scale scaleVolumn, Scale scaleBalance, Line.Info info) {
		super();
		// TODO Auto-generated constructor stub
		this.section = section;
		this.btSelect = btSelect;
		this.scaleVolumn = scaleVolumn;
		this.scaleBalance = scaleBalance;
		this.info = info;
	}
	/**
	 * @return Returns the btSelect.
	 */
	public Button getBtSelect() {
		return btSelect;
	}
	/**
	 * @return Returns the info.
	 */
	public Line.Info getInfo() {
		return info;
	}
	/**
	 * @return Returns the scaleBalance.
	 */
	public Scale getScaleBalance() {
		return scaleBalance;
	}
	/**
	 * @return Returns the scaleVolumn.
	 */
	public Scale getScaleVolumn() {
		return scaleVolumn;
	}
	/**
	 * @return Returns the section.
	 */
	public Section getSection() {
		return section;
	}
}

