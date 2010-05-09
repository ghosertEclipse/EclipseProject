package com.loadtrend.app.test.soundrecorder.info;

import java.io.Serializable;

import com.loadtrend.app.test.soundrecorder.util.FormatUtil;

public class RecordingScheduleInfo implements Serializable {
    private int startTime = 0;

    private int endTime = 0;

    private String date = null;
    
    private boolean daily = false;
    
    private int dayOfWeek = 0;
    
    private String url = null;

    private boolean isEnable = false;
    
    private boolean isInterrupted = false;
    
    /**
     * Only one option is valid: date is not null means once, daily is true means daily, otherwise weekly.
     * @param startTime
     * @param endTime
     * @param date "yyyy-mm-dd" format nullable
     * @param daily false or true
     * @param dayOfWeek Sunday: 0 ... 
     * @param url nullable
     * @param isEnable false or true
     */
    public RecordingScheduleInfo(int startTime, int endTime, String date, boolean daily, int dayOfWeek, String url, boolean isEnable) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.daily = daily;
        this.dayOfWeek = dayOfWeek;
        this.url = url;
        this.isEnable = isEnable;
    }

    public boolean isDaily() {
        return daily;
    }

    public void setDaily(boolean daily) {
        this.daily = daily;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public boolean isInterrupted() {
		return isInterrupted;
	}

	public void setInterrupted(boolean isInterrupted) {
		this.isInterrupted = isInterrupted;
	}

	public String toString() {
    	String desc = null;
    	if (this.date != null) {
    		desc = this.date;
    	} else if (this.isDaily()) {
    		desc = "Daily";
    	} else {
    		desc = "Weekly " + FormatUtil.getDayOfWeek(this.dayOfWeek);
    	}
    	return desc + " " + FormatUtil.getFormatedTime(this.startTime);
    }
}
