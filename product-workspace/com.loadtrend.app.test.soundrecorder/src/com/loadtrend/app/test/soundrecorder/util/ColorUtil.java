package com.loadtrend.app.test.soundrecorder.util;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class ColorUtil {
	
	private static HashMap hashMap = new HashMap();
	
	public static final String darkBlueColor = "darkBlueColor";
	
	public static final String smallBlueColor = "smallBlueColor";
	
	public static final String smallRedColor = "smallRedColor";
	
	public static final String whiteColor = "whiteColor";
	
	public static final String blackColor = "blackColor";
	
	public static final String darkYellowColor = "darkYellowColor";
	
	public static final String darkGreenColor = "darkGreenColor";
	
	public static final String smallBlackColor = "smallBlackColor";
	
	public static final String orangeColor = "orangeColor";
	
	public static final String darkOrangeColor = "darkOrangeColor";
	
    /**
     * 
     * @param colorString: ColorUtil.xxxxx or "255,255,255" for r,g,b
     * @return
     */
	public static Color getColor(String colorString) {
		if (hashMap.size() == 0) {
			hashMap.put(darkBlueColor, new Color(Display.getDefault(), 0, 0x4d, 0xe7));
			hashMap.put(smallBlueColor, new Color(Display.getDefault(), 0x87, 0xb0, 0xc2));
			hashMap.put(smallRedColor, new Color(Display.getDefault(), 0xed, 0x64, 0x5c));
			hashMap.put(whiteColor, Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			hashMap.put(blackColor, Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
			hashMap.put(smallBlackColor, new Color(Display.getDefault(), 0x73, 0x75, 0x73));
			hashMap.put(orangeColor, new Color(Display.getDefault(), 0xff, 0xfb, 0xef));
			hashMap.put(darkOrangeColor, new Color(Display.getDefault(), 0xff, 0x69, 0x00));
			hashMap.put(darkYellowColor, new Color(Display.getDefault(), 0xff, 0xff, 0x00));
			hashMap.put(darkGreenColor, new Color(Display.getDefault(), 0x1c, 0xab, 0x33));
		}
		Color color = (Color) hashMap.get(colorString);
        if (color == null) {
            String[] rgbStrings = colorString.split(",");
            RGB rgb = new RGB(Integer.parseInt(rgbStrings[0]), Integer.parseInt(rgbStrings[1]), Integer.parseInt(rgbStrings[2]));
            color = new Color(Display.getDefault(), rgb);
            hashMap.put(colorString, color);
        }
        return color;
	}
    
    /**
     * Change RGB to "red,green,blue"
     * @param rgb
     * @return
     */
    public static String rgbToString(RGB rgb) {
        String string = "{0},{1},{2}";
        return MessageFormat.format(string, new String[] {String.valueOf(rgb.red), String.valueOf(rgb.green), String.valueOf(rgb.blue)});
    }
    
	public static void dispose() {
		Iterator it = hashMap.values().iterator();
		while (it.hasNext()) {
			Color color = (Color) it.next();
			color.dispose();
		}
		hashMap.clear();	
	}

}
