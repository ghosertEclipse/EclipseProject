package loadtrend.mobile;

public class ListenPortThread extends Thread {
    
    private boolean shutdownSign = false;
    
    private Port port = null;
    
    private Channel channel = null;
    
    private StringBuffer portValues = new StringBuffer();
    
    public ListenPortThread(Port port, Channel channel) {
        this.port = port;
        this.channel = channel; 
    }
    
    public void shutdown() {
        shutdownSign = true;
        interrupt();
    }
    
    public boolean isShutdown() {
        return shutdownSign;
    }

    /* (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    public void run() {
        try {
	        while (!shutdownSign) {
	                Thread.sleep(10);
	                StringBuffer buffer = port.readPort();
	                //
	                System.out.print(buffer);
	                portValues.append(buffer);
	            
	            // Split the new message arrival information from the port values.
	            while (true) {
	                String newMessageArrivalValues = Processor.getRidOfString(portValues, "\r\n+CMTI:", "\r\n");
	                if (!newMessageArrivalValues.equals("")) {
	                    channel.putMessage(new StringBuffer(newMessageArrivalValues));
	                } else {
	                    break;
	                }
	            }
	                
	            // Split the at command ok information from the port values.
	            this.checkResponsePortValues(Mobile.AT_COMMAND_OK);
	            
	            // Split the at command error information from the port values.
	            this.checkResponsePortValues(Mobile.AT_COMMAND_ERROR);
	            
	            // Split the at command message prepared to save or send information from the port values.
	            this.checkResponsePortValues(Mobile.MESSAGE_PREPARED);
	            
	            // Split the send message error information from the port values.
	            if (portValues.indexOf("\r\n+CMS ERROR:") != -1 ) {
	                String code = Processor.getRemainOfString(portValues, "\r\n+CMS ERROR:", "\r\n");
	                if (!code.equals("")) {
	                    this.checkResponsePortValues("\r\n+CMS ERROR:" + code + "\r\n");
	                }
	            }
	        }
        } catch (PortException e) {
        } catch (InterruptedException e) {
        } finally {
            System.out.println("ListenPortThread is termitated.");
        }
    }
    
    private void checkResponsePortValues(String command) {
        int i = portValues.indexOf(command);
        if (i!=-1) {
            String portValue = portValues.substring(0, i + command.length());
            portValues.delete(0, i + command.length());
            channel.put(new StringBuffer(portValue));
        }
    }
}
