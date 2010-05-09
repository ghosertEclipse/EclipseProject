package com.loadtrend.app.mobile.arrivalaction;

public interface MessageArrivalCommand {
    
    /**
     * Execute the command, and return the result to be sent to the message sender.
     * return null if no result.
     * @return
     */
    public String execute();
}
