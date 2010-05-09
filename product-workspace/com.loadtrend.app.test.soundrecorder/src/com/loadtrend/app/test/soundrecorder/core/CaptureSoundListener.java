package com.loadtrend.app.test.soundrecorder.core;

public interface CaptureSoundListener {
    public void stopCaptureSound(double duration, long capturedAudioBytesLength);
    public void processingCaptureSound(boolean isSilenceDuration, double duration, double position);
    /**
     * Is Silence
     * @param paintBytes
     * @param channels
     * @return
     */
    public boolean paintSound(byte[] paintBytes, int channels);
}
