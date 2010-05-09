package com.loadtrend.app.mobile.arrivalaction;

import loadtrend.mobile.Message;

import org.dom4j.Element;

public abstract class MessageArrivalAction {
    protected static final String NEW_LINE = "\n";
    public abstract MessageArrivalCommand getMessageArrivalCommand(Element element, Message message);
}
