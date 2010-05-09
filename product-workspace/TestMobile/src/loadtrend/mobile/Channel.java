package loadtrend.mobile;

import java.util.LinkedList;

public class Channel {

    private final LinkedList queue = new LinkedList();
    
    private final LinkedList messageQueue = new LinkedList();
    
    // 1 minitue time out setting.
    private final long timeout = 60000;

    /**
     * Client thread will invoke this method to put the port value to the queue, and waiting for work thread to handle.
     * @param portValue
     */
    public synchronized void put(StringBuffer portValue) {
//        System.out.println("==============Add portValue to queue of channel================" );
//        System.out.println(portValue);
//        System.out.println("===============================================================\n");
        queue.add(portValue);
        notifyAll();
    }
    
    /**
     * Work thread will invoke this method to get the port value from the queue, and response the request.
     * @return
     */
    public synchronized StringBuffer take() throws TimeoutException, InterruptedException {
        long start = System.currentTimeMillis();
        while (queue.size() <= 0) {
                long now = System.currentTimeMillis();
                long rest = timeout - (now - start);
                if (rest <= 0) {
                    throw new TimeoutException("Waiting for port value timeout: " + timeout);
                }
                wait(rest);
        }
        return (StringBuffer)queue.removeFirst();
    }
    
    /**
     * Client thread will invoke this method to put the new message arrival port value to the message queue, and waiting for work thread to handle.
     * @param portValue
     */
    public synchronized void putMessage(StringBuffer portValue) {
//        System.out.println("==============Add message portValue to message queue of channel================" );
//        System.out.println(portValue);
//        System.out.println("===============================================================\n");
        messageQueue.add(portValue);
        notifyAll();
    }
    
    /**
     * Work thread will invoke this method to get the new message arrival port value from the message queue, and response the request.
     * @return
     */
    public synchronized StringBuffer takeMessage() throws InterruptedException {
        while (messageQueue.size() <= 0) {
            wait();
        }
        return (StringBuffer)messageQueue.removeFirst();
    }
}
