/*
 *	Utils.java
 */

/*
 * Copyright (c) 2001, 2005 by Florian Bomers
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.jsresources.utils;

import java.util.Calendar;
import java.util.Date;
import java.io.File;

public class Utils {

	public static String formatNumber(long i, int digits) {
		return formatNumberImpl(i, digits, "0");
	}

	public static String formatNumberImpl(long i, int digits, String fill) {

		int ten=10;
		String res="";
		while (digits>1) {
			if (i<ten) {
				res+=fill;
			}
			digits--;
			ten*=10;
		}
		return res+String.valueOf(i);
	}

	public static String formatMinSec(long millis) {
		if (millis<0) {
			return " error";
		} else {
			return formatNumberImpl(millis/60000, 3, " ")+":"+formatNumber((millis % 60000)/1000, 2);
		}
	}

	public static String formatMinSecTenths(long millis) {
		if (millis<0) {
			return " ./.";
		} else {
			return formatMinSec(millis)+"."+formatNumberImpl((millis % 1000)/100, 1, " ");
		}
	}

	public static long str2long(String s, long def) {
		try {
			def=Long.parseLong(s);
		} catch (NumberFormatException nfe) {}
		return def;
	}

	/**
	 * Returns a SQL formatted date+time, like 2000-03-18 20:10:05.
	 *
	 */
	public static String getFormattedDate(long millis) {
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date(millis));
		return String.valueOf(cal.get(Calendar.YEAR))+"-"
		       +formatNumber(cal.get(Calendar.MONTH)+1, 2)+"-"
		       +formatNumber(cal.get(Calendar.DAY_OF_MONTH), 2)+" "
		       +formatNumber(cal.get(Calendar.HOUR_OF_DAY), 2)+":"
		       +formatNumber(cal.get(Calendar.MINUTE), 2)+":"
		       +formatNumber(cal.get(Calendar.SECOND), 2);
	}

	public static long align(long value, int al) {
		int rest=(int) (value % (long) al);
		return value-rest;
	}

	public static int align(int value, int al) {
		int rest=(value % al);
		return value-rest;
	}

	public static String stripExtension(String filename) {
		int	ind = filename.lastIndexOf(".");
		if (ind == -1
		        || ind == filename.length()
		        || filename.lastIndexOf(File.separator) > ind) {
			// when dot is at last position,
			// or a slash is after the dot, there isn't an extension
			return filename;
		}
		return filename.substring(0, ind);
	}

}
