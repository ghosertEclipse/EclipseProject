package com.loadtrend.app.test.soundrecorder.core;

public interface PlaybackSoundListener {
    public void stopPlaybackSound(boolean isAutoFinished);
    public void processingPlaybackSound(double position);
    public void paintSound(final byte[] paintBytes, final int channels);
}
