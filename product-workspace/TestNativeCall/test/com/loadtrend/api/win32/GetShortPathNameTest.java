package com.loadtrend.api.win32;

import junit.framework.TestCase;

public class GetShortPathNameTest extends TestCase {
	public void testFileExist() {
		String longPathName = "C:\\Documents and Settings";
		String shortPathName = GetShortPathName.execute(longPathName);
		assertEquals("C:\\DOCUME~1", shortPathName);
	}
	
	public void testFileNotExist() {
		String longPathName = "C:\\Documents and Settings\\zhangni";
		String shortPathName = GetShortPathName.execute(longPathName);
		assertNull(shortPathName);
	}
}
