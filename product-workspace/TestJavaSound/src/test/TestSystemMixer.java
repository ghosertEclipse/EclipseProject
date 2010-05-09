package test;
import java.util.Iterator;
import java.util.List;

import javax.sound.sampled.Control;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;


/*
IDEAS:
- user setting tab style vs. show complete windows for multiple mixers (horicontal/vertical)
- user setting unix style (L/R volume) vs. windows style (volume/balance)
*/

/**	TODO:
 */
public class TestSystemMixer {

	public static void main(String[] args) {
		List portMixers = SystemMixer.getPortMixers();
		if (portMixers.size() == 0) {
			out("There are no mixers that support Port lines.SystemMixer: Error");
			System.exit(1);
		}
		Iterator iterator = portMixers.iterator();
		while (iterator.hasNext()) {
			Mixer mixer = (Mixer) iterator.next();
			String	strMixerName = mixer.getMixerInfo().getName();
			out("mixername:" + strMixerName);
			createMixerPanel(mixer);
		}
		System.exit(0);
	}
	
	private static void createMixerPanel(Mixer mixer) {
		Port.Info[]	infosToCheck = SystemMixer.getPortInfo(mixer);
		for (int i = 0; i < infosToCheck.length; i++) {
			Port port = SystemMixer.getOpenedPort(mixer, infosToCheck[i]);
			if (port != null) {
				createPortPanel(port);
			}
			port.close();
		}
	}
	
	private static void createPortPanel(Port port)
	{
		String	strPortName = ((Port.Info) port.getLineInfo()).getName();
		out("Portname:" + strPortName);
		Control[]	aControls = port.getControls();
		if (aControls.length > 1) {
			// In fact in the windows system, it named SPEAKER port, and it contains play controls.
			out("ignore the port " + strPortName + " that contains more than one control.");
		} else {
			// this is record control.
			Control[] resultControls = SystemMixer.getCompoundControlMembers(aControls[0]);
			if (resultControls != null) {
			    out("compound control description:" + aControls[0].toString());
			    out("compound control name:" + aControls[0].getType().toString());
			    showResultControls(resultControls);
			}
		}
	}
	
	private static void showResultControls(Control[] resultControls) {
		out("BooleanControl:" + (resultControls[0] != null ? resultControls[0].getType().toString() : ""));
		out("FloatControl pan:" + (resultControls[1] != null ? resultControls[1].getType().toString() : ""));
		out("FloatControl balance:" + (resultControls[2] != null ? resultControls[2].getType().toString() : ""));
	}
	
	private static void out(String message) {
		System.out.println(message);
	}
}