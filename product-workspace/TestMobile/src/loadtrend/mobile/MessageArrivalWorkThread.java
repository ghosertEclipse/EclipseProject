package loadtrend.mobile;

import java.util.Iterator;
import java.util.List;

public class MessageArrivalWorkThread extends Thread {

    private Channel channel = null;
    
    private Mobile mobile = null;
    
    private List listeners = null;
    
    private boolean shutdownSign = false;
    
    private boolean isHandlering = false;
    
    public void shutdown() {
        shutdownSign = true;
        interrupt();
    }
    
    public boolean isShutdown() {
        return shutdownSign;
    }

    public MessageArrivalWorkThread(Channel channel, Mobile mobile, List listeners) {
        this.channel = channel;
        this.mobile = mobile;
        this.listeners = listeners;
    }
    
    public void run() {
        try {
	        while (!shutdownSign) {
	            StringBuffer portValues = channel.takeMessage();
	            String smsMemoType = Processor.getStringFromTokens( portValues, " ", "," );
	            String index = portValues.substring(1);
                this.isHandlering = true;
	            try {
	                Message message = mobile.readMessage(Integer.parseInt(index), smsMemoType);
                    Iterator it = listeners.iterator();
                    while (it.hasNext()) {
                        MessageArrivalListener listener = (MessageArrivalListener) it.next();
	                    listener.handler(message);
                    }
	            } catch (MobileException me) {
	                me.printStackTrace();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
                this.isHandlering = false;
	        }
        } catch (InterruptedException ie) {
        } finally {
            System.out.println("MessageArrivalThread: " + Thread.currentThread().getName() + " is termitated.");
        }
    }
    
    public boolean isHandlering() {
        return isHandlering;
    }
}
