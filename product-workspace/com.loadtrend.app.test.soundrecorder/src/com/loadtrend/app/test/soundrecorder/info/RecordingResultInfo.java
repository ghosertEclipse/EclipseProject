package com.loadtrend.app.test.soundrecorder.info;

import java.io.Serializable;
import java.util.Date;

public class RecordingResultInfo implements Serializable {
	private Date startTime = null;

	private double duration = 0;

	private String location = null;

	public RecordingResultInfo(Date startTime, double duration, String location) {
		this.startTime = startTime;
		this.duration = duration;
		this.location = location;
	}

	/**
	 * @return Returns the duration.
	 */
	public double getDuration() {
		return duration;
	}

	/**
	 * @return Returns the location.
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @return Returns the startTime.
	 */
	public Date getStartTime() {
		return startTime;
	}
}
