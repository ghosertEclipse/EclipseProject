package com.loadtrend.app.test.soundrecorder.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PreferenceUtil
{
	public static final String SAMPLE_RATE = "SAMPLE_RATE";
	
	public static final String SAMPLE_BIT = "SAMPLE_BIT";
	
	public static final String CHANNEL = "CHANNEL";
	
	public static final String MP3_QUALITY = "MP3_QUALITY";
	
	public static final String MP3_BIT_RATE = "MP3_BIT_RATE";
	
	public static final String RECORD_FORMAT = "RECORD_FORMAT";
    
	public static final String[] SAMPLE_RATES = new String[] {"8000", "11025", "22050", "44100"};
	
	public static final String[] SAMPLE_BITS = new String[] {"8", "16"};
	
	public static final String[] CHANNELS = new String[] {"1", "2"};
	
	public static final String[] MP3_QUALITYS = new String[] {"lowest", "low", "middle", "high", "highest"};
	
	public static final String[] MP3_BIT_RATES = new String[] {"64", "128", "192", "256", "-1"};
	
	public static final String[] RECORD_FORMATS = new String[] {"WAV", "MP3"};
	
	public static final String OUTPUT_DIRECOTRY = "OUTPUT_DIRECTORY";
    
	public static final String OUTPUT_FILENAME_DATETIME = "OUTPUT_FILENAME_DATETIME";
    
	public static final String OUTPUT_FILENAME_CUSTOMIZE = "OUTPUT_FILENAME_CUSTOMIZE";
    
	public static final String MAIN_COLOR = "MAIN_COLOR";
    
	public static final String PC_MIXER = "PC_MIXER";
    
	public static final String PC_MIXER_RECHECK = "-1";
    
	public static final String PC_MIXER_NOTFOUND = "";
	
	public static final String NOISE_GATE_ENABLE = "NOISE_GATE_ENABLE";
	
	public static final String NOISE_GATE_PERCENT = "NOISE_GATE_PERCENT";
	
	public static final String NOISE_GATE_DURATION = "NOISE_GATE_DURATION";
	
	public static final String RECORD_HOTKEY = "RECORD_HOTKEY";
	
	public static final String PAUSE_HOTKEY = "PAUSE_HOTKEY";
	
	public static final String STOP_HOTKEY = "STOP_HOTKEY";
	
	public static final String PLAYBACK_HOTKEY = "PLAYBACK_HOTKEY";
	
	private static final String PREFERENCE_NAME = "preference.properties";
	
	private static Properties properties = loadProperties();
	
	public static final String RESULT_DATA = "result.dat";
    
	public static final String SCHEDULE_DATA = "schedule.dat";
    
    public static final String APP_IMAGE = "images/war.ico";
    
    public static final String TEMP_PATH = System.getProperty("user.dir") + File.separator + "temp" + File.separator;
    
	private PreferenceUtil() {
	}
	
	private static Properties loadProperties() {
		Properties properties = new Properties();
		try {
            File file = new File(PREFERENCE_NAME);
            if (file.exists()) {
                properties.load(new FileInputStream(PREFERENCE_NAME));
            } else {
                // the first time default setting.
            	properties.setProperty(SAMPLE_RATE, "3");
            	properties.setProperty(SAMPLE_BIT, "1");
            	properties.setProperty(CHANNEL, "1");
            	properties.setProperty(MP3_QUALITY, "2");
            	properties.setProperty(MP3_BIT_RATE, "1");
            	properties.setProperty(RECORD_FORMAT, "1");
            	properties.setProperty(OUTPUT_DIRECOTRY, System.getProperty("user.dir"));
            	properties.setProperty(OUTPUT_FILENAME_DATETIME, "true");
            	properties.setProperty(OUTPUT_FILENAME_CUSTOMIZE, "default");
            	properties.setProperty(MAIN_COLOR, "128,64,0");
            	properties.setProperty(PC_MIXER, PC_MIXER_RECHECK);
            	properties.setProperty(NOISE_GATE_ENABLE, "true");
            	properties.setProperty(NOISE_GATE_PERCENT, "5");
            	properties.setProperty(NOISE_GATE_DURATION, "3000");
            	properties.setProperty(RECORD_HOTKEY, "F5");
            	properties.setProperty(PAUSE_HOTKEY, "F6");
            	properties.setProperty(STOP_HOTKEY, "F7");
            	properties.setProperty(PLAYBACK_HOTKEY, "F8");
            }
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	/**
	 * Get the PREFERENCE_FULLNAME value with specified key.
	 * @param key
	 * @return PREFERENCE_FULLNAME value
	 */
	public static String getValue( String key )
	{
		return properties.getProperty( key );
	}
	
	/**
	 * Set PREFERENCE_FULLNAME value with specified key, also the pair key-value will be store to corresponding properties file
	 * @param key PREFERENCE_FULLNAME key
	 * @param value PREFERENCE_FULLNAME value
	 */
	public static void setValue( String key, String value )
	{
		properties.setProperty( key, value );
		storeToProperties();
	}
	
	private static void storeToProperties()
	{
		try
		{
			properties.store( new FileOutputStream( PREFERENCE_NAME ), null );
		}
		catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

