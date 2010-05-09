package loadtrend.mobile.test;

import java.awt.Robot;
import java.awt.event.KeyEvent;

import junit.framework.TestCase;

public class RobotTester extends TestCase {
    
    Robot robot = null;

    public RobotTester(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        robot = new Robot();
    }

    protected void tearDown() throws Exception {
    }
    
    public void testKeyPress() {
        robot.setAutoWaitForIdle(true);
        robot.setAutoDelay(1000);
        
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_ALT);
        
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_N);
        robot.keyRelease(KeyEvent.VK_N);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        
        
        robot.keyPress(KeyEvent.VK_N);
        robot.keyRelease(KeyEvent.VK_N);
    }

}
