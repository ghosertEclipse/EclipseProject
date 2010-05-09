package com.loadtrend.app.mobile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.ui.plugin.*;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.BundleContext;

import com.loadtrend.app.mobile.data.MobileAppConstant;

/**
 * The main plugin class to be used in the desktop.
 */
public class MobilePlugin extends AbstractUIPlugin {

	//The shared instance.
	private static MobilePlugin plugin;
	
	/**
	 * The constructor.
	 */
	public MobilePlugin() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		// copy xml file when not existing.
		File file = new File(MobileAppConstant.MOBILE_XML);
		if (!file.exists()) {
			this.copyFileFromPluginToApp(MobileAppConstant.MOBILE_XML);
		}
		
		// copy wav file anyway.
		file = new File(MobileAppConstant.NEW_MESSAGE_ARRIVAL_WAV);
		this.copyFileFromPluginToApp(MobileAppConstant.NEW_MESSAGE_ARRIVAL_WAV);
	}
	
    private void copyFileFromPluginToApp(String filename) {
		URL mobileXml = this.getPluginResource(filename);
		InputStream inputStream = null;
		FileOutputStream fileOutputStream = null;
        try {
			inputStream = mobileXml.openStream();
			fileOutputStream = new FileOutputStream(filename);
			int bytedata = 0;
			while ((bytedata  = inputStream.read()) != -1) {
			    fileOutputStream.write(bytedata);
			}
			fileOutputStream.close();
			inputStream.close();
        } catch (IOException ioe) {
        	ioe.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                	fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
	private URL getPluginResource(String filename) {
		return plugin.find(new Path(filename)); 
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static MobilePlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin("com.loadtrend.app.mobile", path);
	}
}
