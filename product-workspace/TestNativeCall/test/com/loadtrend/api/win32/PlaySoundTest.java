package com.loadtrend.api.win32;

import junit.framework.TestCase;

public class PlaySoundTest extends TestCase {
    public void testPlaySound() {
        String filename = "nudge.wav";
        PlaySound.execute(filename);
        PlaySound.execute(filename, PlaySound.SND_SYNC);
        PlaySound.execute("SystemStart", PlaySound.SND_SYNC);
    }
}
