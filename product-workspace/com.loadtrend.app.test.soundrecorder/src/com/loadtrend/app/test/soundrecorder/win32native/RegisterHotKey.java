package com.loadtrend.app.test.soundrecorder.win32native;

import org.eclipse.swt.internal.win32.MSG;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.TCHAR;
import org.eclipse.swt.widgets.Display;

import com.eaio.nativecall.IntCall;
import com.eaio.nativecall.NativeCall;

public class RegisterHotKey {

	private static HotKeyHandler hotKeyHandler = null;

	private static int[] hotkeyIds = null;
	
	public static int MOD_ALT = 0x1;
	
	public static int MOD_CONTROL = 0x2;
	
	public static int MOD_SHIFT = 0x4;
	
	private static String[] MODIFIER_KEYS = new String[] { "Shift + ", "Ctrl + ",
			"Alt + ", "Alt + Shift + ", "Ctrl + Alt + ", "Ctrl + Shift + ",
			"Ctrl + Alt + Shift + " };
	
	private static int[] MODIFIER_CODES = new int[] {MOD_SHIFT, MOD_CONTROL, MOD_ALT,
		                                             MOD_ALT | MOD_SHIFT, MOD_ALT | MOD_CONTROL, MOD_CONTROL | MOD_SHIFT ,
		                                             MOD_ALT | MOD_CONTROL | MOD_SHIFT};
	
	/**
	 * String hotkeys like Alt + S, F5, S
	 * @param strings
	 * @param hotKeyHandler
	 */
	public static void registerHotKey(String[] strings, HotKeyHandler hotKeyHandler) {
		int[] hotkeys = new int[strings.length];
		int[] modifierKey = new int[strings.length];
		for (int i = 0; i < strings.length; i++) {
			String string = strings[i];
			int k = -1;
			for (int j = 0; j < MODIFIER_KEYS.length; j++) {
				if (string.indexOf(MODIFIER_KEYS[j]) != -1) k = j;
			}
			if (k != -1) {
				string = string.substring(MODIFIER_KEYS[k].length());
				modifierKey[i] = MODIFIER_CODES[k];
			}
			if (string.matches("F[0-9]+")) {
				hotkeys[i] = Integer.parseInt(string.substring(1)) + 0x6f;
			} else {
				// to hanlder the letter and digit.
				hotkeys[i] = (int) string.toUpperCase().charAt(0);
			}
		}
		registerHotKey(hotkeys, modifierKey, hotKeyHandler);
	}
	
	/**
	 * hotkeys combined with modifier key, and a hot key handler offer.
	 * @param hotkeys eg. OS.VK_F5
	 * @param modifierKey RegisterHotKey.MOD_ALT RegisterHotKey.MOD_CONTROL RegisterHotKey.MOD_SHIFT, zero if none.
	 * @param hotKeyHandler null is acceptable.
	 */
	public static void registerHotKey(int[] hotkeys, int[] modifierKey, HotKeyHandler hotKeyHandler) {
		if (hotKeyHandler != null) RegisterHotKey.hotKeyHandler = hotKeyHandler;
		RegisterHotKey.hotkeyIds = new int[hotkeys.length];
		IntCall ic = null;
		IntCall ic2 = null;
		try {
			NativeCall.init();
			ic = new IntCall("user32", "RegisterHotKey");
			if (OS.IsUnicode) {
			    ic2 = new IntCall("kernel32", "GlobalAddAtomW");
			} else {
			    ic2 = new IntCall("kernel32", "GlobalAddAtom");
			}
			// for the third parameters.
			// int MOD_ALT = 0x1;
			// int MOD_CONTROL = 0x2;
			// int MOD_SHIFT = 0x4;
            int hHeap = OS.GetProcessHeap ();
            int[] lpVerb = new int[hotkeys.length];
			for (int i = 0; i < hotkeys.length; i++) {
                lpVerb[i] = getStringPointer(hHeap, "HotKey" + i);
                byte[] parameterBytes = new byte[4];
	            setIntToBytes(lpVerb[i], parameterBytes, 0);
				int returnValue = ic2.executeCall(parameterBytes);
				hotkeyIds[i] = returnValue;
				ic.executeBooleanCall(new Object[] { new Integer(0), new Integer(hotkeyIds[i]),
				                                     new Integer(modifierKey[i]), new Integer(hotkeys[i])});
//				boolean success = ic.executeBooleanCall(new Object[] { new Integer(0), new Integer(hotkeyIds[i]),
//						                                               new Integer(0), new Integer(hotkeys[i])});
//				if (success) {
//					System.out.println("success to register " + hotkeys[i]);
//				} else {
//					System.out.println("fail to register " + hotkeys[i]);
//				}
			}
			for (int i = 0; i < lpVerb.length; i++) {
                if (lpVerb[i] != 0) OS.HeapFree (hHeap, 0, lpVerb[i]);    
			}
		} catch (Exception e) {
			System.err.println("Exception of type " + e.getClass().getName()
					+ ":\n" + e.getMessage());
			e.printStackTrace();
		} finally {
			if (ic != null) ic.destroy();
			if (ic2 != null) ic2.destroy();
		}
	}

	public static void processHotKeyHandler(MSG msg) {
		if (msg.message == OS.WM_HOTKEY) {
			for (int i = 0; i < hotkeyIds.length; i++) {
				if (msg.wParam == hotkeyIds[i]) {
			        hotKeyHandler.handler(i);
			        // make sure the message be handlered only once.
			        OS.PeekMessage (msg, 0, 0, 0, OS.PM_REMOVE);
			        Display.getDefault().sleep();
			        return;
				}
			}
		}
	}

	public static void unRegisterHotKey() {
		IntCall ic = null;
		IntCall ic2 = null;
		try {
			NativeCall.init();
			ic = new IntCall("user32", "UnregisterHotKey");
		    ic2 = new IntCall("kernel32", "GlobalDeleteAtom");
			for (int i = 0; i < hotkeyIds.length; i++) {
				// boolean success = ic.executeBooleanCall(new Object[] {new Integer(0), new Integer(hotkeyIds[i])});
				ic.executeBooleanCall(new Object[] {new Integer(0), new Integer(hotkeyIds[i])});
				ic2.executeCall(new Integer(hotkeyIds[i]));
//				if (success) {
//					System.out.println("success to unregister " + hotkeys[i]);
//				} else {
//					System.out.println("fail to unregister " + hotkeys[i]);
//				}
			}
		} catch (Exception e) {
			System.err.println("Exception of type " + e.getClass().getName()
					+ ":\n" + e.getMessage());
			e.printStackTrace();
		} finally {
			if (ic != null) ic.destroy();
			if (ic2 != null) ic2.destroy();
		}
	}
	
	private static int getStringPointer(int hHeap, String string) {
	    /* Use the character encoding for the default locale */
	    TCHAR buffer1 = new TCHAR (0, string, true); //$NON-NLS-1$
	    int byteCount1 = buffer1.length () * TCHAR.sizeof;
	    int lpVerb = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount1);
	    OS.MoveMemory (lpVerb, buffer1, byteCount1);
	    return lpVerb;
	}
	    
	private static void setIntToBytes(int target, byte[] bytes, int startPos) {
	    StringBuffer hexString = new StringBuffer(Integer.toHexString(target));
	    if (hexString.length() % 2 == 1) hexString = hexString.insert(0, "0");
	    while (hexString.length() != 0) {
	        String string = hexString.substring(hexString.length() - 2, hexString.length());
	        hexString.delete(hexString.length() - 2, hexString.length());
	        bytes[startPos++] = (byte) Integer.parseInt(string, 16);
	    }
	}
}
