/*
 *	AudioUtils.java
 */

/*
 * Copyright (c) 2001, 2004, 2005 by Florian Bomers
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

package org.jsresources.utils.audio;

import java.io.InputStream;
import javax.sound.sampled.*;

public class AudioUtils {

    public static long bytes2millis(long bytes, AudioFormat format) {
	return (long) (bytes/format.getFrameRate()*1000/format.getFrameSize());
    }

    public static long millis2bytes(long ms, AudioFormat format) {
	return (long) (ms*format.getFrameRate()/1000*format.getFrameSize());
    }

    public static boolean isPCM(AudioFormat.Encoding enc) {
    	return    enc.equals(AudioFormat.Encoding.PCM_SIGNED)
    	       || enc.equals(AudioFormat.Encoding.PCM_UNSIGNED);
    }

    public static boolean isPCM(AudioFormat af) {
    	return isPCM(af.getEncoding());
    }


}

