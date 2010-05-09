package com.loadtrend.api.win32;
 
import com.eaio.nativecall.IntCall;
import com.eaio.nativecall.NativeCall;
 
public class RegisterHotKey {
 
   public static final int WM_HOTKEY = 0x312;
   
   public static final int PM_REMOVE = 0x1;
   
   public static final int vkF9 = 120;
   
   public static final int vkF10 = 121;
   
   public static final int vkF11 = 122;
   
   private static int[] hotkey = null;
   
   public static void main(String[] args) {
       System.out.println(registerHotKey(new int[] {vkF9, vkF10, vkF11}));
       System.out.println(pressHotKeyHandler(new HotKeyHandler[] {new HotKeyHandler() {
	    public void handler() {
	    	System.out.println("F9 pressed.");
		}
       }}));
       System.out.println(unRegisterHotKey());
   }
 
    public static boolean registerHotKey(int[] hotkeys) {
        boolean success = false;
        hotkey = hotkeys;
        try {
            NativeCall.init();
            IntCall ic = new IntCall("user32", "RegisterHotKey");
            try {
            	// for the third parameters.
//                int MOD_ALT = 0x1;
//                int MOD_CONTROL = 0x2;
//                int MOD_SHIFT = 0x4;
            	for (int i = 0; i < hotkeys.length; i++) {
                    success = ic.executeBooleanCall(new Object[] { new Integer(0), new Integer(i), new Integer(0), new Integer(hotkeys[i])});
            	}
            } finally {
                ic.destroy();
            }
        } catch (Exception e) {
            System.err.println("Exception of type " + e.getClass().getName()
                    + ":\n" + e.getMessage());
            e.printStackTrace();
        }
        return success;
    }
    
    public static boolean unRegisterHotKey() {
        boolean success = false;
        try {
            NativeCall.init();
            IntCall ic = new IntCall("user32", "UnregisterHotKey");
            try {
            	for (int i = 0; i < hotkey.length; i++) {
                    success = ic.executeBooleanCall(new Object[] { new Integer(0), new Integer(i)});
            	}
            } finally {
                ic.destroy();
            }
        } catch (Exception e) {
            System.err.println("Exception of type " + e.getClass().getName()
                    + ":\n" + e.getMessage());
            e.printStackTrace();
        }
        return success;
    }
    
    public static boolean pressHotKeyHandler(HotKeyHandler[] handlers) {
        boolean success = false;
        boolean isCancel = false;
        try {
            NativeCall.init();
            IntCall ic = new IntCall("user32", "PeekMessageW");
            IntCall ic3 = new IntCall("user32", "WaitMessage");
            try {
            	while (!isCancel) {
            	    ic3.executeBooleanCall();
	                byte[] MSG = new byte[48];
	                if (ic.executeBooleanCall(new Object[] { MSG, new Integer(0), new Integer(WM_HOTKEY), new Integer(WM_HOTKEY), new Integer(PM_REMOVE)})) {
	                	int wParam = getIntFromBytes(MSG, 8, 4);
	                	if (wParam < handlers.length) {
	                		handlers[wParam].handler();
	                	}
	                }
            	}
            } finally {
                ic.destroy();
                ic3.destroy();
            }
        } catch (Exception e) {
            System.err.println("Exception of type " + e.getClass().getName()
                    + ":\n" + e.getMessage());
            e.printStackTrace();
        }
        return success;
    }
    
    private static int getIntFromBytes(byte[] bytes, int startPos, int length) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int j = bytes[i + startPos];
            if (j < 0) j = 256 + j;
            String string = Integer.toHexString(j);
            if (string.length() == 1) string = "0" + string;
            hexString = hexString.insert(0, string);
        }
        return Integer.parseInt(hexString.toString(), 16);
    }
    
    interface HotKeyHandler {
    	public void handler();
    }
}
