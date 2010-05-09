package systemmixer;

/*
 *	SystemMixerFrame.java
 */

/*
 * Copyright (c) 2001 - 2002 by Matthias Pfisterer
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoundedRangeModel;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

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
public class SystemMixerFrame
	extends JFrame
{
	private static final boolean DEBUG = true;

	/**	TODO:
	 */
	private static final Port.Info[]	EMPTY_PORT_INFO_ARRAY = new Port.Info[0];




	/**	TODO:
	 */
	public SystemMixerFrame()
	{
		setTitle("System Mixer");
		setSize(new Dimension(800, 500));
		WindowAdapter	windowAdapter = new WindowAdapter()
			{
				public void windowClosing(WindowEvent we)
				{
					System.exit(0);
				}
			};
		addWindowListener(windowAdapter);

		JTabbedPane tabbedPane = new JTabbedPane();
		List portMixers = getPortMixers();
		if (portMixers.size() == 0)
		{
			JOptionPane.showMessageDialog(
				null,
				"There are no mixers that support Port lines.",
				"SystemMixer: Error",
				JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		Iterator iterator = portMixers.iterator();
		while (iterator.hasNext())
		{
			Mixer	mixer = (Mixer) iterator.next();
			JPanel	mixerPanel = createMixerPanel(mixer);
			JScrollPane	scrollPane = new JScrollPane(
				mixerPanel,
				JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			String	strMixerName = mixer.getMixerInfo().getName();
			tabbedPane.add(scrollPane, strMixerName);
		}
		getContentPane().add(tabbedPane);
	}


	/**	Returns the Mixers that support Port lines.
	 */
	private List getPortMixers()
	{
		List supportingMixers = new ArrayList();
		Mixer.Info[]	aMixerInfos = AudioSystem.getMixerInfo();
		Line.Info	portInfo = new Line.Info(Port.class);
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
	private boolean arePortsSupported(Mixer mixer)
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
	private Port.Info[] getPortInfo(Mixer mixer)
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



	/**	TODO:
	 */
	private JPanel createMixerPanel(Mixer mixer)
	{
		JPanel	mixerPanel = new JPanel();
		mixerPanel.setLayout(new BoxLayout(mixerPanel,
										   BoxLayout.X_AXIS));
		Port.Info[]	infosToCheck = getPortInfo(mixer);
		for (int i = 0; i < infosToCheck.length; i++)
		{
			// 			if (mixer.isLineSupported(infosToCheck[i]))
			// 			{
			Port	port = null;
			try
			{
				port = (Port) mixer.getLine(infosToCheck[i]);
				port.open();
			}
			catch (LineUnavailableException e)
			{
				e.printStackTrace();
			}
			if (port != null)
			{
				JPanel	portPanel = createPortPanel(port);
				mixerPanel.add(portPanel);
			}
			// 			}
		}
		return mixerPanel;
	}



	/**	TODO:
	 */
	private JPanel createPortPanel(Port port)
	{
		JPanel	portPanel = new JPanel();
		JPanel	controlsPanelCenter = new JPanel();
		JPanel	controlsPanelSouth = new JPanel();
		String	strPortName = ((Port.Info) port.getLineInfo()).getName();
		portPanel.setBorder(new TitledBorder(new EtchedBorder(), strPortName));
		portPanel.setLayout(new BorderLayout());
		controlsPanelCenter.setLayout(new BoxLayout(controlsPanelCenter, BoxLayout.Y_AXIS));
		controlsPanelSouth.setLayout(new BoxLayout(controlsPanelSouth, BoxLayout.Y_AXIS));
		//portPanel.add(new JLabel(strPortName), BorderLayout.NORTH);
		portPanel.add(controlsPanelCenter, BorderLayout.CENTER);
		portPanel.add(controlsPanelSouth, BorderLayout.SOUTH);
		Control[]	aControls = port.getControls();
		for (int i = 0; i < aControls.length; i++)
		{
			if (DEBUG) { out("control: " + aControls[i]); }
			JComponent	controlComponent = createControlComponent(aControls[i]);
			if (aControls[i] instanceof FloatControl)
			{
				controlsPanelCenter.add(controlComponent);
			}
			else
			{
				controlsPanelSouth.add(controlComponent);
			}
		}
		return portPanel;
	}



	/**	TODO:
	 */
	private JComponent createControlComponent(Control control)
	{
		JComponent	component = null;
		if (control instanceof BooleanControl)
		{
			component = createControlComponent((BooleanControl) control);
		}
		else if (control instanceof EnumControl)
		{
			component = createControlComponent((EnumControl) control);
		}
		else if (control instanceof FloatControl)
		{
			component = createControlComponent((FloatControl) control);
		}
		else if (control instanceof CompoundControl)
		{
			component = createControlComponent((CompoundControl) control);
		}
		return component;
	}



	/**	TODO:
	 */
	private JComponent createControlComponent(BooleanControl control)
	{
		AbstractButton	button;
		String		strControlName = control.getType().toString();
		ButtonModel model = new BooleanControlButtonModel(control);
		button = new JCheckBox(strControlName);
		button.setModel(model);
		return button;
	}



	/**	TODO:
	 */
	private JComponent createControlComponent(EnumControl control)
	{
		JPanel	component = new JPanel();
		String	strControlName = control.getType().toString();
		component.setBorder(new TitledBorder(new EtchedBorder(), strControlName));
		// TODO:
		System.out.println("WARNING: EnumControl is not yet supported");
		return component;
	}



	/**	TODO:
	 */
	private JComponent createControlComponent(FloatControl control)
	{
		Control.Type type = control.getType();
		JPanel	controlPanel = new JPanel();
		controlPanel.setLayout(new BorderLayout());
		String	strControlName = type.toString();
		controlPanel.setBorder(new TitledBorder(new EtchedBorder(), strControlName));
		int orientation = isBalanceOrPan(control) ? JSlider.HORIZONTAL : JSlider.VERTICAL;
		BoundedRangeModel model = new FloatControlBoundedRangeModel(control);
		JSlider slider = new JSlider(model);
		slider.setOrientation(orientation);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		setTickSpacings(slider);
		controlPanel.add(slider, BorderLayout.CENTER);
		return controlPanel;
	}



	/**	Create GUI component for a CompoundControl.
		@param control The CompoundControl a GUI representation
		should be created for.

		@return A JComponent that represents the passed
		CompoundControl.

	*/
	private JComponent createControlComponent(CompoundControl control)
	{
		JComponent controlPanel = new Box(BoxLayout.Y_AXIS);
		// for BooleanControl and EnumControl
		JComponent booleanPanel = new Box(BoxLayout.Y_AXIS);
		// for FloatControl, BALANCE and PAN
		JComponent balancePanel = new Box(BoxLayout.Y_AXIS);
		// for FloatControl, all other types
		JComponent volumePanel = new Box(BoxLayout.X_AXIS);
		controlPanel.add(booleanPanel);
		controlPanel.add(balancePanel);
		controlPanel.add(volumePanel);
		String	strControlName = control.getType().toString();
		controlPanel.setBorder(new TitledBorder(new EtchedBorder(), strControlName));
		Control[]	aControls = control.getMemberControls();
		for (int i = 0; i < aControls.length; i++)
		{
			Control con = aControls[i];
			JComponent member = createControlComponent(con);
			if (con instanceof BooleanControl ||
				con instanceof EnumControl)
			{
				booleanPanel.add(member);
			}
			else if (con instanceof FloatControl)
			{
				if (isBalanceOrPan((FloatControl) con))
				{
					balancePanel.add(member);
				}
				else
				{
					volumePanel.add(member);
				}
			}
			else
			{
				controlPanel.add(member);
			}
		}
		return controlPanel;
	}



	private void setTickSpacings(JSlider slider)
	{
		int nRange = slider.getMaximum() - slider.getMinimum();
		if (nRange < 10)
		{
			slider.setMajorTickSpacing(1);
		}
		else if (nRange < 50)
		{
			slider.setMajorTickSpacing(5);
			// slider.setMinorTickSpacing(1);
		}
		else if (nRange < 100)
		{
			slider.setMajorTickSpacing(10);
			// slider.setMinorTickSpacing(5);
		}
		else if (nRange < 200)
		{
			slider.setMajorTickSpacing(20);
			// slider.setMinorTickSpacing(5);
		}
		else if (nRange < 500)
		{
			slider.setMajorTickSpacing(50);
			// slider.setMinorTickSpacing(10);
		}
		else
		{
			slider.setMajorTickSpacing(100);
			// slider.setMinorTickSpacing(100);
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
