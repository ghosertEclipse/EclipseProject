package com.loadtrend.app.test.soundrecorder.util;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ImageUtil {
	
	private static HashMap map = new HashMap();

	public static Image getImage(String filename) {
		Image image = (Image) map.get(filename);
		if (image == null) {
			image = new Image(Display.getDefault(), filename);
			map.put(filename, image);
		}
		return image;
	}
	
	public static void dispose() {
		Iterator it = map.values().iterator();
		while (it.hasNext()) {
			Image image = (Image) it.next();
			image.dispose();
		}
		map.clear();
	}
}
