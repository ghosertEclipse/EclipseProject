package com.loadtrend.app.test.soundrecorder.util;

import java.io.File;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUtil {
	
    private static DecimalFormat decimalFormat = new DecimalFormat("00");
    
    public static String getDayOfWeek(int i) {
        switch(i) {
            case 0:
                return "Sunday";
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6:
                return "Saturday";
            default:
                return null;
        }
    }
    
    /**
     * Convert int year month day to yyyy-mm-dd
     * @param year
     * @param month from 0 to 11
     * @param day from 1 to unknown
     * @return
     */
    public static String getFormatedDate(int year, int month, int day) {
        return decimalFormat.format(year) + "-" + decimalFormat.format(month + 1) + "-" + decimalFormat.format(day);
    }
    
    /**
     * Convert yyyy-mmd-dd to int year month day
     * @param stringDate
     * @return
     */
    public static int[] getYearMonthDay(String stringDate) {
        int[] ymd = new int[3];
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);
            ymd[0] = date.getYear();
            ymd[1] = date.getMonth();
            ymd[2] = date.getDate();
            return ymd;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Return 00:00:00 format
     * @param duration
     * @return
     */
    public static String getFormatedTime(double duration) {
    	return getFormatedTime(duration, false);
    }
    
    /**
     * Return 00:00:00 or 00:00:00.00 format
     * @param duration
     * @param morePrecision
     * @return
     */
    public static String getFormatedTime(double duration, boolean morePrecision) {
        int[] hms = getHoursMinutesSeconds(duration);
        String formatedTime = morePrecision ? MessageFormat.format("{0}:{1}:{2}.{3}",
                                                   new String[] {decimalFormat.format(hms[0]),
                                                                 decimalFormat.format(hms[1]),
                                                                 decimalFormat.format(hms[2]),
                                                                 decimalFormat.format(hms[3])})
                                            : MessageFormat.format("{0}:{1}:{2}",
                                                   new String[] {decimalFormat.format(hms[0]),
                                                                 decimalFormat.format(hms[1]),
                                                                 decimalFormat.format(hms[2])});
        return formatedTime;
    }
    
    /**
     * Get hours, minutes and seconds int
     * @param totalSeconds total seconds
     * @return
     */
    public static int[] getHoursMinutesSeconds(double totalSeconds) {
        int[] hms = new int[4];
        hms[0] = (int) (totalSeconds / 3600);
        hms[1] = (int) (totalSeconds - 3600 * hms[0]) / 60;
        hms[2] = (int) totalSeconds - 3600 * hms[0] - 60 * hms[1];
        hms[3] = (int) ((totalSeconds - 3600 * hms[0] - 60 * hms[1] - hms[2]) * 100);
        return hms;
    }
    
    public static Date getDate(String stringDate, double totalSeconds) {
    	int[] hms = getHoursMinutesSeconds(totalSeconds);
    	int[] ymd = getYearMonthDay(stringDate);
    	Date date = new Date(ymd[0], ymd[1], ymd[2], hms[0], hms[1], hms[2]);
    	return date;
    }
    
    
    public static String getFilename(String postfix) {
        String filename = null;
        if (Boolean.valueOf(PreferenceUtil.getValue(PreferenceUtil.OUTPUT_FILENAME_DATETIME)).booleanValue()) {
             // date time file name
            filename = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            return PreferenceUtil.getValue(PreferenceUtil.OUTPUT_DIRECOTRY) + File.separator + filename + postfix;
        } else {
            // customize file name
            filename = PreferenceUtil.getValue(PreferenceUtil.OUTPUT_FILENAME_CUSTOMIZE);
            if (filename.equals("")) filename = "default";
            String filenameWithoutPostfix = PreferenceUtil.getValue(PreferenceUtil.OUTPUT_DIRECOTRY) + File.separator + filename;
            int i = 1;
            File file = new File(filenameWithoutPostfix + "_" + String.valueOf(i++) + postfix);
            while (file.exists()) {
                file = new File(filenameWithoutPostfix + "_" + String.valueOf(i++) + postfix);
            }
            return filenameWithoutPostfix + "_" + String.valueOf(--i) + postfix;
        }
    }
}
