package com.loadtrend.app.test.soundrecorder.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

public class FontUtil {
	
	private static Font systemFont = null;
	
	private static Font boldSystemFont = null;
	
	public static Font getBoldSystemFont() {
		if (systemFont == null) {
			systemFont = Display.getDefault().getSystemFont();
			FontData fontData = systemFont.getFontData()[0];
			boldSystemFont = new Font(Display.getDefault(), fontData.getName(), fontData.getHeight(), SWT.BOLD);
		}
		return boldSystemFont;
	}
	
	public static void dispose() {
		if (systemFont != null) {
			systemFont.dispose();
			boldSystemFont.dispose();
			systemFont = null;
			boldSystemFont = null;
		}
	}

}
