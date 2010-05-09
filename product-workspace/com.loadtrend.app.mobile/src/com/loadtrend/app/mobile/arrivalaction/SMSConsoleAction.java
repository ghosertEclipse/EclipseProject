package com.loadtrend.app.mobile.arrivalaction;

import loadtrend.mobile.Message;

import org.dom4j.Element;

import com.loadtrend.app.mobile.util.MobileXMLUtil;
import com.loadtrend.app.mobile.util.RuntimeEx;

public class SMSConsoleAction extends MessageArrivalAction {
    
    private boolean resultReturnable = false;
    
    private String consoleCommand = null;
    
    public MessageArrivalCommand getMessageArrivalCommand(Element element, final Message message) {
        
        final String content = message.getContent().trim();
        String[] codes = content.split(" ");
        resultReturnable = element.attributeValue(MobileXMLUtil.RESULTRETURNABLE).equalsIgnoreCase("true");
        consoleCommand = element.attributeValue(MobileXMLUtil.CONSOLECOMMAND);
        
        // execute the command line, if user send console command predefined in the mobile.xml.
        if (codes.length > 1 && codes[0].equalsIgnoreCase(consoleCommand)) {
            return new MessageArrivalCommand() {
                public String execute() {
                    return SMSConsoleAction.this.executeCommand(message, content.substring(content.indexOf(" ") + 1));
                }
            };
        }
        
        Element childElement = MobileXMLUtil.getMatchedElement(element, content);
        
        // no command found, send the prompt message if necessary and return.
        if (childElement == null) return null;
        
        final String command = childElement.attributeValue(MobileXMLUtil.COMMAND);
        return new MessageArrivalCommand() {
            public String execute() {
                return SMSConsoleAction.this.executeCommand(message, command);
            }
        };
        
    }
    
    private String executeCommand(Message message, String command) {
        String commandLine = "cmd /c " + command;
        String resultString = RuntimeEx.executeCommand(commandLine);
        if (resultReturnable) {
            return resultString;
        }
        return null;
    }
}
