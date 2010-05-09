package com.loadtrend.api.win32;

import junit.framework.TestCase;

public class ShellExecuteTest extends TestCase {
	
	private boolean success = false;
	
	private StringBuffer resultCode = null;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		success = false;
		resultCode = new StringBuffer();
	}

	public void testExecuteNoAssociationFileErroInfo() {
		String filePath = "noassociation.zjw";
	    success = ShellExecute.execute(filePath, null, ShellExecute.NORMAL, resultCode);
	    System.out.println("testExecuteNoAssociationFileErroInfo error info: " + resultCode);
	    assertEquals(false, success);
	}
	
	public void testExecuteNotExistFileErrorInfo() {
		String filePath = "notExistFile.mp3";
	    success = ShellExecute.execute(filePath, null, ShellExecute.NORMAL, resultCode);
	    System.out.println("testExecuteNotExistFileErrorInfo error info: " + resultCode);
	    assertEquals(false, success);
	}
	 
	public void testExecuteShortPathProgramFile() {
		String programPath = "C:\\Program Files\\Windows Media Player\\wmplayer.exe";
		String filePath = "ni pei wo.mp3";
		filePath = GetShortPathName.execute(filePath);
	    success = ShellExecute.execute(programPath, filePath, ShellExecute.NORMAL, resultCode);
	    System.out.println("testExecuteShortPathProgramFile App id: " + resultCode);
	    assertEquals(true, success);
	}
	
	public void testExecuteLongPathProgramFile() {
		String programPath = "C:\\Program Files\\Windows Media Player\\wmplayer.exe";
		String filePath = "ni pei wo.mp3";
	    success = ShellExecute.execute(programPath, filePath, ShellExecute.NORMAL, resultCode);
	    System.out.println("testExecuteLongPathProgramFile App id: " + resultCode);
	    assertEquals(true, success);
	}
	
	public void testExecuteLongPathFile() {
		String filePath = "ni pei wo.mp3";
	    success = ShellExecute.execute(filePath, null, ShellExecute.NORMAL, resultCode);
	    System.out.println("testExecuteLongPathFile App id: " + resultCode);
	    assertEquals(true, success);
	}
	
	public void testBrowser() {
		String url = "http://www.google.com";
	    success = ShellExecute.execute(url, null, ShellExecute.NORMAL, resultCode);
	    System.out.println("Browser App id: " + resultCode);
	    assertEquals(true, success);
	}
	
	public void testEmail() {
	    String email = "mailto:rc.den.boer@hccnet.nl"
				          + "?cc=piet@nowhere.nl"
				          + "&subject=Test%20by%20Rob"  /* notice %20 instead of space */
				          + "&body=How are you doing";
	    success = ShellExecute.execute(email, null, ShellExecute.NORMAL, resultCode);
	    System.out.println("Email App id: " + resultCode);
	    assertEquals(true, success);
	}
}
