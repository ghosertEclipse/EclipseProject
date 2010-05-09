package com.loadtrend.app.test.soundrecorder.core;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.CompoundControl;
import javax.sound.sampled.Control;
import javax.sound.sampled.EnumControl;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;

/*
IDEAS:
- user setting tab style vs. show complete windows for multiple mixers (horicontal/vertical)
- user setting unix style (L/R volume) vs. windows style (volume/balance)
*/

/**	TODO:
 */
public class SystemMixer {

	/**	TODO:
	 */
	private static final Port.Info[]	EMPTY_PORT_INFO_ARRAY = new Port.Info[0];
	
	public static void main(String[] args) {
		new SystemMixer();
	}
	
	/**	TODO:
	 */
	public SystemMixer()
	{
		List portMixers = getPortMixers();
		if (portMixers.size() == 0) {
			out("There are no mixers that support Port lines.SystemMixer: Error");
			System.exit(1);
		}
		Iterator iterator = portMixers.iterator();
		while (iterator.hasNext())
		{
			Mixer	mixer = (Mixer) iterator.next();
			String	strMixerName = mixer.getMixerInfo().getName();
			out("mixername:" + strMixerName);
			createMixerPanel(mixer);
		}
	}


	/**	Returns the Mixers that support Port lines.
	 */
	public static List getPortMixers()
	{
		List supportingMixers = new ArrayList();
		Mixer.Info[]	aMixerInfos = AudioSystem.getMixerInfo();
		for (int i = 0; i < aMixerInfos.length; i++)
		{
			Mixer	mixer = AudioSystem.getMixer(aMixerInfos[i]);
			boolean	bSupportsPorts = arePortsSupported(mixer);
			if (bSupportsPorts)
			{
				supportingMixers.add(mixer);
			}
		}
		return supportingMixers;
	}


	/**	TODO:
	 */
	// should be implemented by:
	// Mixer.isLineSupported(new Line.Info(Port.class))
	private static boolean arePortsSupported(Mixer mixer)
	{
		Line.Info[]	infos;
		infos = mixer.getSourceLineInfo();
		for (int i = 0; i < infos.length; i++)
		{
			if (infos[i] instanceof Port.Info)
			{
				return true;
			}
		}
		infos = mixer.getTargetLineInfo();
		for (int i = 0; i < infos.length; i++)
		{
			if (infos[i] instanceof Port.Info)
			{
				return true;
			}
		}
		return false;
	}

	/**	TODO:
	 */
	private void createMixerPanel(Mixer mixer)
	{
		Port.Info[]	infosToCheck = getPortInfo(mixer);
		for (int i = 0; i < infosToCheck.length; i++) {
			Port port = getOpenedPort(mixer, infosToCheck[i]);
			if (port != null) {
				createPortPanel(port);
			}
		}
	}

	/**	TODO:
	 */
	public static Port.Info[] getPortInfo(Mixer mixer)
	{
		Line.Info[]	infos;
		List portInfoList = new ArrayList();
		infos = mixer.getSourceLineInfo();
		for (int i = 0; i < infos.length; i++)
		{
			if (infos[i] instanceof Port.Info)
			{
				portInfoList.add((Port.Info) infos[i]);
			}
		}
		infos = mixer.getTargetLineInfo();
		for (int i = 0; i < infos.length; i++)
		{
			if (infos[i] instanceof Port.Info)
			{
				portInfoList.add((Port.Info) infos[i]);
			}
		}
		Port.Info[]	portInfos = (Port.Info[]) portInfoList.toArray(EMPTY_PORT_INFO_ARRAY);
		return portInfos;
	}
	
	/**
	 * @param portInfo
	 * @return null is acceptable return value.
	 */
	public static Port getOpenedPort(Mixer mixer, Port.Info portInfo) {
		Port port = null;
		try
		{
			port = (Port) mixer.getLine(portInfo);
			port.open();
		    return port;
		}
		catch (LineUnavailableException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**	TODO:
	 */
	private void createPortPanel(Port port)
	{
		String	strPortName = ((Port.Info) port.getLineInfo()).getName();
		out("Portname:" + strPortName);
		Control[]	aControls = port.getControls();
//		for (int i = 0; i < aControls.length; i++)
//		{
		if (aControls.length > 1) {
			// In fact in the windows system, it named SPEAKER port, and it contains play controls.
			out("ignore the port " + strPortName + " that contains more than one control.");
		} else {
			// this is record control.
			out("control:" + aControls[0].toString());
			createControlComponent(aControls[0]);
			if (aControls[0] instanceof FloatControl)
			{
			}
			else
			{
			}
		}
//		}
	}
	
	/**
	 * 
	 * @param control
	 * @return null is an acceptable return value.
	 * Control[0]: BooleanControl Control[1]: FloatControl pan Control[2]: FloatControl balance
	 */
	public static Control[] getCompoundControlMembers(Control control) {
		if (!(control instanceof CompoundControl)) return null;
		Control[] resultControls = new Control[3];
        Control[] aControls = ((CompoundControl)control).getMemberControls();
		for (int i = 0; i < aControls.length; i++) {
			Control con = aControls[i];
			if ((con instanceof BooleanControl) && (con.getType().toString().equalsIgnoreCase("Select"))) {
				resultControls[0] = con;
			} else if (con instanceof FloatControl) {
				if (isBalanceOrPan((FloatControl) con)) {
					resultControls[2] = con;
				}
				else {
					resultControls[1] = con;
				}
			}
		}
		return resultControls;
	}


	/**	TODO:
	 */
	private void createControlComponent(Control control)
	{
		if (control instanceof BooleanControl)
		{
		String		strControlName = control.getType().toString();
		out("sub control type BooleanControl:" + strControlName);
		}
		else if (control instanceof EnumControl)
		{
		String	strControlName = control.getType().toString();
		out("sub control type EnumControl:" + strControlName);
		System.out.println("WARNING: EnumControl is not yet supported");
		}
		else if (control instanceof FloatControl)
		{
		String	strControlName = control.getType().toString();
		out("sub control type FloatControl:" + strControlName);
		}
		else if (control instanceof CompoundControl)
		{
		String	strControlName = control.getType().toString();
		out("sub control type CompoundControl:" + strControlName);
		Control[]	aControls = ((CompoundControl)control).getMemberControls();
		for (int i = 0; i < aControls.length; i++)
		{
			Control con = aControls[i];
			if (con instanceof BooleanControl) {
				out("sub sub control type BooleanControl:" + con.getType().toString());
				if (strControlName.equalsIgnoreCase("Stereo Mix")) {
					((BooleanControl) con).setValue(true);
				}
			}
			else if (con instanceof EnumControl) {
				out("sub sub control type EnumControl:" + con.getType().toString());
			}
			else if (con instanceof FloatControl)
			{
				if (isBalanceOrPan((FloatControl) con))
				{
				    out("sub sub control type FloatControl balance:" + con.getType().toString());
				}
				else
				{
				    out("sub sub control type FloatControl pan:" + con.getType().toString());
				}
			}
			else
			{
			}
		}
		}
	}

	/** Returns whether the type of a FloatControl is BALANCE or PAN.
	 */
	private static boolean isBalanceOrPan(FloatControl control)
	{
		Control.Type type = control.getType();
		return type.equals(FloatControl.Type.PAN) || type.equals(FloatControl.Type.BALANCE);
	}

	private static void out(String message)
	{
		System.out.println(message);
	}
}



/*** SystemMixerFrame.java ***/
