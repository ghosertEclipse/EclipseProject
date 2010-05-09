/*
 *	AudioSettings.java
 */

/*
 * Copyright (c) 2004, 2005 by Florian Bomers
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

/*
  |<---            this code is formatted to fit into 80 columns             --->|
*/

package org.jsresources.apps.radio;

import java.util.*;
import javax.sound.sampled.*;
import static org.jsresources.apps.radio.Constants.*; // $$ static import

class AudioSettings {

    /** the selected port */
    private  Port[] port = new Port[2];
    /** the selected control for port volume */
    private FloatControl[] portVolume = new FloatControl[2];
    /** the selected control for port select (only for Source Ports) */
    private BooleanControl[] portSelect = new BooleanControl[2];
    private int[] portIndex = new int[2];

    /** all compound controls available on the system, as a display list */
    private List<String> portNames[] = new List[2]; // $$ generics

    /** all Ports available on the system (maybe the same ones) */
    private List<Port>[] ports = new List[2];

    /** the index of the controls array for the respective compound control */
    private List<Integer>[] controlIndex = new List[2];

    /** the selected mixer. If null, use default */
    private Mixer[] mixer = new Mixer[2];

    /** all mixers */
    private List<Mixer>[] mixers = new List[2];

    /** index in BUFFER_SIZE_MILLIS */
    private int[] bufferSizeIndex = new int[2];

    private boolean inited = false;

    private AudioFormat preferredFormat;
    private AudioFileFormat.Type preferredType;

    private int circBufMillis;

    public AudioSettings() {
	portNames[0] = new ArrayList<String>();
	portNames[1] = new ArrayList<String>();
	ports[0] = new ArrayList<Port>();
	ports[1] = new ArrayList<Port>();
	controlIndex[0] = new ArrayList<Integer>();
	controlIndex[1] = new ArrayList<Integer>();
	mixers[0] = new ArrayList<Mixer>();
	mixers[1] = new ArrayList<Mixer>();
	bufferSizeIndex[0] = BUFFER_SIZE_INDEX_DEFAULT;
	bufferSizeIndex[1] = BUFFER_SIZE_INDEX_DEFAULT;
	// preferred Audio Format
	setPreferredAudioFormatCode(FORMAT_CODE_DEFAULT);
    }

    public void init() {
	for (int d = 0; d < 2; d++) {
	    portNames[d].clear();
	    ports[d].clear();
	    controlIndex[d].clear();
	    mixers[d].clear();
	}
	// go through all mixers and fill all lists
	Mixer.Info[] infos = AudioSystem.getMixerInfo();
	for (int i = 0; i < infos.length; i++) {
	    try {
		Mixer mixer = AudioSystem.getMixer(infos[i]);
		addMixer(mixer, mixer.getSourceLineInfo(), DIR_SPK);
		addMixer(mixer, mixer.getTargetLineInfo(), DIR_MIC);
	    } catch (Exception e) {
		if (DEBUG) e.printStackTrace();
	    }
	}
	// add defaults, if multiples exist
	for (int d = 0; d < 2; d++) {
	    if (mixers[d].size() > 1) {
		mixers[d].add(0, null);
	    }
	}
	// find selected recording port
	for (int p = 0; p < ports[DIR_MIC].size(); p++) {
		setSelPort(DIR_MIC, p, false);
		if (portSelect[DIR_MIC] != null && portSelect[DIR_MIC].getValue()==true) {
			portIndex[DIR_MIC] = p;
			break;
		}
	}
	// set first port for speaker as default
	setSelPort(DIR_SPK, 0, false);
	inited = true;
	if (VERBOSE) {
	    out("Microphone Ports:  "+ports[DIR_MIC].size());
	    out("Microphone Mixers: "+mixers[DIR_MIC].size());
	    out("Speaker Ports:  "+ports[DIR_SPK].size());
	    out("Speaker Mixers: "+mixers[DIR_SPK].size());
	}
    }

    public void exit() {
	for (int d = 0; d < 2; d++) {
	    closePort(d);
	    portNames[d].clear();
	    controlIndex[d].clear();
	    ports[d].clear();
	    mixers[d].clear();
	}
    }

    private void addMixer(Mixer mixer, Line.Info[] infos, int dirDataLine) {

	for (int i = 0; i<infos.length; i++) {
	    try {
		if (infos[i] instanceof Port.Info) {
		    Port.Info info = (Port.Info) infos[i];
		    int d;
		    if (info.isSource()) {
		    	// microphone port
		    	d = DIR_MIC;
		    } else {
		    	d = DIR_SPK;
		    }
		    // walk through all top-level controls
		    Port p = (Port) mixer.getLine(info);
		    p.open();
		    try {
		    	Control[] cs = p.getControls();
		    	for (int c = 0 ; c < cs.length; c++) {
			    if (cs[c] instanceof CompoundControl) {
				ports[d].add(p);
				portNames[d].add(mixer.getMixerInfo().getName()+": "+cs[c].getType().toString());
				// $$ autoboxing
				controlIndex[d].add(c);
			    }
		    	}
		    } finally {
		    	p.close();
		    }
		}
		if (infos[i] instanceof DataLine.Info) {
		    if (!mixers[dirDataLine].contains(mixer)) {
		    	mixers[dirDataLine].add(mixer);
		    }
		}
	    } catch (Exception e) {
		if (DEBUG) e.printStackTrace();
	    }
	}
    }

    // MIXER HANDLING //

    public List<String> getMixers(int d) {
	if (!inited) init();
	List<String> res = new ArrayList<String>();
	for (Mixer m : mixers[d]) {   // $$ enhanced for
	    if (m == null) {
		res.add("(Default)");
	    } else {
		res.add(m.getMixerInfo().getName());
	    }
	}
	return res;
    }

    public Mixer getSelMixer(int d) {
	return mixer[d];
    }

    /** set index in list returned in getMixers */
    public void setSelMixer(int d, int index) {
	if (index < 0 || index >= mixers[d].size()) {
	    if (DEBUG) out("setSelMixer out of range: index="+index);
	    return;
	} else {
	    mixer[d] = mixers[d].get(index);
	}
    }



    // PORT HANDLING //

    public List<String> getPorts(int d) {
	if (!inited) init();
	return portNames[d];
    }


    public Port getSelPort(int d) {
	return port[d];
    }

    public int getSelPortIndex(int d) {
	return portIndex[d];
    }

    public FloatControl getSelVolControl(int d) {
	return portVolume[d];
    }

    public int getVolumeLevel(int d) {
	FloatControl c = getSelVolControl(d);
	int newPos = 0;
	if (c != null) {
	    newPos = (int) (((c.getValue() - c.getMinimum())
		 / (c.getMaximum() - c.getMinimum())) * 100.0f);
		}
	return newPos;
    }

    public void setVolumeLevel(int d, int level) {
	FloatControl c = getSelVolControl(d);
	if (c != null) {
	    float newVol = ((level / 100.0f)
			    * (c.getMaximum() - c.getMinimum())) + c.getMinimum();
	    c.setValue(newVol);
	}
    }

    /** set index in list returned in getMixers */
    public void setSelPort(int d, int index) {
    	setSelPort(d, index, true);
    }

    public void setSelPort(int d, int index, boolean doSelect) {
	if (index < 0 || index >= ports[d].size()) {
	    if (DEBUG) out("setSelPort out of range: iondex="+index);
	    closePort(d);
	    return;
	} else {
	    portIndex[d] = index;
	    setupPort(d, ports[d].get(index), controlIndex[d].get(index)); // $$ autoboxing
	    if (doSelect && d == DIR_MIC && portSelect[d] != null) {
		portSelect[d].setValue(true);
	    }
	    if (DEBUG) out("Selected "+portNames[d].get(index));
	}
    }

    private void closePort(int d) {
	if (port[d] != null) {
	    port[d].close();
	    port[d] = null;
	}
	portVolume[d] = null;
	portSelect[d] = null;
    }

    private void setupPort(int d, Port p, int controlIndex) {
	if (p != port[d] && port[d] != null) {
	    port[d].close();
	}
	portVolume[d] = null;
	portSelect[d] = null;
	port[d] = p;
	try {
	    p.open();
	    Control[] cs = p.getControls();
	    if (controlIndex >= cs.length) {
		throw new Exception("control not found!");
	    }
	    if (!(cs[controlIndex] instanceof CompoundControl)) {
		throw new Exception("control not found!");
	    }
	    CompoundControl cc = (CompoundControl) cs[controlIndex];
	    cs = cc.getMemberControls();
	    // find VolumeControl and select
	    for (Control c : cs) {  // $$ enhanced for
		if ((c instanceof FloatControl)
		    && c.getType().equals(FloatControl.Type.VOLUME)
		    && (portVolume[d] == null)) {
		    portVolume[d] = (FloatControl) c;
		}
		if ((c instanceof BooleanControl)
		    && c.getType().toString().contains("elect")
		    && (portSelect[d] == null)) {
		    portSelect[d] = (BooleanControl) c;
		}
	    }

	} catch (Exception e) {
	    if (DEBUG) e.printStackTrace();
	    closePort(d);
	}
    }

    // buffer size //

    public int getBufferSizeMillis(int d) {
    	return BUFFER_SIZE_MILLIS[bufferSizeIndex[d]];
    }

    public int getBufferSizeIndex(int d) {
    	return bufferSizeIndex[d];
    }

    public void setBufferSizeIndex(int d, int bufferSizeIndex) {
    	this.bufferSizeIndex[d] = bufferSizeIndex;
    }

    // audio format //
    public AudioFormat getPreferredAudioFormat() {
    	return preferredFormat;
    }

    public AudioFileFormat.Type getPreferredAudioFileType() {
    	return preferredType;
    }

    // quick look-up tables
    private static final int[] frameSize={
	1, // nothing
	4, // CD
	4, // FM
	1, // Telephone
	33, // GSM
	-1, -1, -1, -1, -1, -1, // MP3 and ogg
    };

    private static final int[] sampleSize={
	1,  // nothing
	16, // CD
	16, // FM
	8,  // Telephone
	-1,  // GSM
	-1, -1, -1, -1, -1, -1, // MP3 and ogg
    };

    private static final float[] sampleRate={
	1.0f,  // nothing
	44100.0f, // CD
	22050.0f, // FM
	8000.0f,  // Telephone
	8000.0f,  // GSM
	44100.0f, 44100.0f, 22050.0f, // mp3
	44100.0f, 44100.0f, 22050.0f, // ogg
    };

    private static final int[] channels={
	1,  // nothing
	2, // CD
	2, // FM
	1,  // Telephone
	1,  // GSM
	2, 2, 2, 2, 2, 2, // MP3 and ogg
    };

    private static final float[] frameRate={
	1.0f,  // nothing
	44100.0f, // CD
	22050.0f, // FM
	8000.0f,  // Telephone
	50.0f,   // GSM
	-1, -1, -1, -1, -1, -1
    };

    private static final String[] encodings={
    	"", // nothing
    	"PCM_SIGNED",
    	"PCM_SIGNED",
    	"ULAW",
    	"GSM0610",
    	"MPEG1L3","MPEG1L3","MPEG1L3",
    	"VORBIS","VORBIS","VORBIS",
    };

    private static final AudioFileFormat.Type TYPE_MP3 = new AudioFileFormat.Type("MP3", "mp3");
    private static final AudioFileFormat.Type TYPE_OGG = new AudioFileFormat.Type("Vorbis", "ogg");

    private static final AudioFileFormat.Type[] types={
    	null, // nothing
    	AudioFileFormat.Type.WAVE,
    	AudioFileFormat.Type.WAVE,
    	AudioFileFormat.Type.AU,
    	new AudioFileFormat.Type("GSM", "gsm"),
    	TYPE_MP3,TYPE_MP3,TYPE_MP3,
    	TYPE_OGG,TYPE_OGG,TYPE_OGG,
    };

    public AudioFormat getAudioFormatFromCode(int code) {
	AudioFormat.Encoding enc = new AudioFormat.Encoding(encodings[code]);
	AudioFormat af = new AudioFormat(enc, sampleRate[code], sampleSize[code],
	                       channels[code], frameSize[code], frameRate[code], false);
	return af;
    }

    public AudioFileFormat.Type getTypeFromCode(int code) {
	return types[code];
    }


    public void setPreferredAudioFormatCode(int formatCode) {
	preferredFormat = getAudioFormatFromCode(formatCode);
	preferredType = getTypeFromCode(formatCode);
	if (VERBOSE) out("Preferred format: "+preferredType+": "+preferredFormat);
    }

    public void setCircBufMillis(int millis) {
    	circBufMillis = millis;
	if (VERBOSE) out("Circular Buffer size: "+(millis/1000)+" seconds.");
    }

    public int getCircBufMillis() {
    	return circBufMillis;
    }
}

