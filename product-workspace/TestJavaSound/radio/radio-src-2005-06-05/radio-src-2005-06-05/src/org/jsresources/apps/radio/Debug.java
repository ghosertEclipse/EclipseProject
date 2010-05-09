/*
 *	Debug.java
 */

/*
 * Copyright (c) 1999 - 2001 by Matthias Pfisterer
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

import java.io.PrintStream;
import  java.util.StringTokenizer;
import  java.security.AccessControlException;



public class Debug
{
	public static boolean		SHOW_ACCESS_CONTROL_EXCEPTIONS = false;
	private static final String	PROPERTY_PREFIX = "radio.";
	// The stream we output to
	private static PrintStream	sm_printStream = System.out;

	private static String indent="";

	// general
	private static boolean	sm_bTraceAllExceptions = getBooleanProperty("TraceAllExceptions");

	// specific
	private static boolean	sm_bTraceMixerPanel = getBooleanProperty("TraceMixerPanel");
	private static boolean	sm_bTraceControlsPanel = getBooleanProperty("TraceControlsPanel");
	private static boolean	sm_bTraceControlPropertiesPanel = getBooleanProperty("TraceControlPropertiesPanel");
	private static boolean	sm_bTraceLineTableModel = getBooleanProperty("TraceLineTableModel");

	private static boolean sm_bTraceServiceProviderTableModel = getBooleanProperty("TraceServiceProviderTableModel");
	private static boolean sm_bTraceConfigurationFilesTableModel = getBooleanProperty("TraceConfigurationFilesTableModel");

	public static final boolean getTraceAllExceptions()
	{
		return sm_bTraceAllExceptions;
	}

	public static final boolean getTraceMixerPanel()
	{
		return sm_bTraceMixerPanel;
	}

	public static final boolean getTraceControlsPanel()
	{
		return sm_bTraceControlsPanel;
	}

	public static final boolean getTraceControlPropertiesPanel()
	{
		return sm_bTraceControlPropertiesPanel;
	}

	public static final boolean getTraceLineTableModel()
	{
		return sm_bTraceLineTableModel;
	}

	public static final boolean getTraceServiceProviderTableModel()
	{
		return sm_bTraceServiceProviderTableModel;
	}


	public static final boolean getTraceConfigurationFilesTableModel()
	{
		return sm_bTraceConfigurationFilesTableModel;
	}


	// make this method configurable to write to file, write to stderr,...
	public static void out(String strMessage)
	{
		if (strMessage.length()>0 && strMessage.charAt(0)=='<') {
			if (indent.length()>2) {
				indent=indent.substring(2);
			} else {
				indent="";
			}
		}
		String newMsg=null;
		if (indent!="" && strMessage.indexOf("\n")>=0) {
			newMsg="";
			StringTokenizer tokenizer=new StringTokenizer(strMessage, "\n");
			while (tokenizer.hasMoreTokens()) {
				newMsg+=indent+tokenizer.nextToken()+"\n";
			}
		} else {
			newMsg=indent+strMessage;
		}
		sm_printStream.println(newMsg);
		if (strMessage.length()>0 && strMessage.charAt(0)=='>') {
				indent+="  ";
		}
	}



	public static void out(Throwable throwable)
	{
		throwable.printStackTrace(sm_printStream);
	}



	private static boolean getBooleanProperty(String strName)
	{
		String	strPropertyName = PROPERTY_PREFIX + strName;
		String	strValue = "false";
		try
		{
			strValue = System.getProperty(strPropertyName, "false");
		}
		catch (AccessControlException e)
		{
			if (SHOW_ACCESS_CONTROL_EXCEPTIONS)
			{
				out(e);
			}
		}
		// Debug.out("property: " + strPropertyName + "=" + strValue);
		boolean	bValue = strValue.toLowerCase().equals("true");
		// Debug.out("bValue: " + bValue);
		return bValue;
	}
}



/*** Debug.java ***/
