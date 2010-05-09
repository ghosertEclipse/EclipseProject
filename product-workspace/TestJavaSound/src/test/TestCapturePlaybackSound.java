package test;

import javax.sound.sampled.AudioFormat;

import org.tritonus.share.sampled.AudioFileTypes;
import org.tritonus.share.sampled.Encodings;

public class TestCapturePlaybackSound {
    public static void main(String[] args) {
        
//        String filename = "test.wav";
//        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
//        float rate = 44100f; // 11025f, 16000f, 22050f, 44100f
//        int sampleSize = 16; // 16
//        boolean bigEndian = false;
//        int channels = 2; // 1: mono; 2: stereo
//
//        AudioFormat format = new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize/8)*channels, rate, bigEndian);
//        
//        CaptureSound capture = new CaptureSound(filename, format, AudioFileTypes.WAVE);
     
        String filename = "test.mp3";
        AudioFormat.Encoding encoding = Encodings.getEncoding("MPEG1L3");
        float rate = 44100f; 
        int sampleSize = -1;
        boolean bigEndian = false;
        int channels = 2; // 1: mono; 2: stereo

        AudioFormat format = new AudioFormat(encoding, rate, sampleSize, channels, -1, -1, bigEndian);
        
        CaptureSound capture = new CaptureSound(filename, format, AudioFileTypes.getType("MP3", "mp3"));
        
        // capture.encodeMP3Param("middle", "128", false);
        
        System.out.println("start capture");
        capture.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        System.out.println("pause capture");
        capture.pause();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        System.out.println("resume capture");
        capture.resume();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        System.out.println("stop capture");
        capture.stop();
        
        
        // Play back the captured audio.
        PlaybackSound playback = new PlaybackSound(filename);
        System.out.println("start playback");
        playback.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        System.out.println("pause playback");
        playback.pause();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        System.out.println("resume playback");
        playback.resume();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        System.out.println("stop playback");
        playback.stop();
    }
}
