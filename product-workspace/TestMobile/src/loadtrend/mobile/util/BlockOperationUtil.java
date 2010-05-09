package loadtrend.mobile.util;

import loadtrend.mobile.PortException;

/**
 *  This class is used to solved the block problem such as write port or open port:
 *  <li>write port: when writing data to unknown COM, such as some virtual bluetooth COM,
 *      the thread will block on port.writePort method.
 *  <li>open port: when the blue tooth is prepared on pc, but not enable on mobile, the thread will block
 *  @param timeout force to interrupt the operation after specified timeout.
 *  @author jiawei.zhang
 *
 */
public class BlockOperationUtil {
    public static boolean start(BlockOperation blockOperation, int timeout) throws InterruptedException {
    	BlockOperationThread blockOperationThread = new BlockOperationThread(blockOperation);
    	blockOperationThread.start();
        
        synchronized (blockOperationThread) {
            long start = System.currentTimeMillis();
            while (!blockOperationThread.isWritingFinished()) {
                    long now = System.currentTimeMillis();
                    long rest = timeout - (now - start);
                    // force to interrupt the WritePortThread if timeout.
                    if (rest <= 0) {
                    	blockOperationThread.shutdown();
                    	break;
                    }
                    blockOperationThread.wait(rest);
            }
        }
        return blockOperationThread.isSuccessWriting();
    }
    
    private static class BlockOperationThread extends Thread {
        
    	private BlockOperation blockOperation = null;
    	
        private boolean isWritingFinished = false;
        
        private boolean isSuccessWriting = false;
        
        public BlockOperationThread(BlockOperation blockOperation) {
        	this.blockOperation = blockOperation;
        }
        
        public void shutdown() {
            interrupt();
        }
        
        /* (non-Javadoc)
         * @see java.lang.Thread#run()
         */
        public void run() {
            try {
            	this.blockOperation.run();
    	        synchronized (this) {
    	            this.isWritingFinished = true;
    	            this.isSuccessWriting = true;
    	            this.notifyAll();
				}
            } catch (PortException e) {
            } finally {
                System.out.println("BlockOperationThread is termitated.");
            }
        }

		public boolean isSuccessWriting() {
			return isSuccessWriting;
		}

		public boolean isWritingFinished() {
			return isWritingFinished;
		}
    }
}
