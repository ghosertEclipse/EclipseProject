package com.loadtrend.app.mobile.arrivalaction;

import java.util.HashMap;
import java.util.Iterator;

import loadtrend.mobile.Message;

import org.dom4j.Element;

import com.loadtrend.api.win32.GetShortPathName;
import com.loadtrend.api.win32.ShellExecuteEx;
import com.loadtrend.api.win32.TerminateProcess;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.util.MobileXMLUtil;

public class SMSControllerAction extends MessageArrivalAction {
    
    private HashMap map = new HashMap();
    
    private boolean resultReturnable = false;
    
    private String closeCommand = null;
    
    public MessageArrivalCommand getMessageArrivalCommand(Element element, final Message message) {
        
        final String[] codes = message.getContent().trim().split(" ");
        resultReturnable = element.attributeValue(MobileXMLUtil.RESULTRETURNABLE).equalsIgnoreCase("true");
        closeCommand = element.attributeValue(MobileXMLUtil.CLOSECOMMAND);
        
        // Close the app, if user send closecommand predefined in the mobile.xml.
        if (codes.length == 2 && codes[0].equalsIgnoreCase(closeCommand)) {
            if (codes[1].matches("[0-9]+")) {
                return new MessageArrivalCommand() {
                    public String execute() {
                        Integer appId = Integer.valueOf(codes[1]);
                        int iResultCode = TerminateProcess.execute(appId);
                        if (resultReturnable) {
                            String resultReturn = iResultCode == 0 ? 
                                                  Messages.getString(MessagesConstant.SMSControllerAction_CLOSEAPP_FAILED, appId.toString()) :
                                                  Messages.getString(MessagesConstant.SMSControllerAction_CLOSEAPP_SUCCESS, appId.toString()); 
                            return resultReturn;
                        }
                        return null;
                    }
                };
            }
        }
        
        // Get command.
        return this.getCommand(element, codes, message);
    }
    
    // Match the coming code with the command defined in XML to get the pre-defined command.
    // If not matched, return null.
    private MessageArrivalCommand getCommand(Element element, String[] codes, final Message message) {
        String codeStored = null;
        Element controlElement = MobileXMLUtil.getMatchedElement(element, codes[0]);
        if (controlElement == null) {
            if (codes.length != 1) return null;
            codeStored = (String) map.get(message.getTeleNum());
            if (codeStored == null) return null;
            controlElement = MobileXMLUtil.getMatchedElement(element, codeStored);
            if (controlElement == null) return null;
        } else {
            // Store the current existed command from telephone number.
            map.put(message.getTeleNum(), codes[0]);
        }
        
        // Use resultCodeErrorInfo to store the app id or the error info for the executing.
        final StringBuffer resultCodeErrorInfo = new StringBuffer();
        
        // Find command.
        final String command = controlElement.attributeValue(MobileXMLUtil.COMMAND);
        
        // Send the command list if only one word is sent to jmobile.
        if (codes.length == 1 && codeStored == null) {
            final StringBuffer commandList = new StringBuffer();
	        Iterator it = controlElement.elementIterator();
            
            // Execute the execuutable file now, if no child file elements.
            if (!it.hasNext()) {
                return new MessageArrivalCommand() {
                    public String execute() {
                        boolean success = ShellExecuteEx.execute(command, null, ShellExecuteEx.NORMAL, resultCodeErrorInfo);
                        return getExecuteResult(success, resultCodeErrorInfo);
                    }
                };
            }
            
            // Send the command list.
	        while (it.hasNext()) {
	            Element fileElement = (Element) it.next();
                commandList.append(fileElement.attributeValue(MobileXMLUtil.CODE) + "  " + fileElement.attributeValue(MobileXMLUtil.VALUE) + NEW_LINE);
	        }
            return new MessageArrivalCommand() {
                public String execute() {
                    return commandList.toString();
                }
            };
        }
        
        // If not find file path, return null.
        String code = (codeStored != null ? codes[0] : codes[1]);
        Element fileElement = MobileXMLUtil.getMatchedElement(controlElement, code);
        if (fileElement == null) return null;
        
        // Find file path, return it.
        final String path = GetShortPathName.execute(fileElement.attributeValue(MobileXMLUtil.PATH));
        return new MessageArrivalCommand() {
            public String execute() {
                boolean success = ShellExecuteEx.execute(command, path, ShellExecuteEx.NORMAL, resultCodeErrorInfo);
                return getExecuteResult(success, resultCodeErrorInfo);
            }
        };
    }
    
    private String getExecuteResult(boolean executeSuccess, StringBuffer resultCodeErrorInfo) {
        if (resultReturnable) {
            String resultReturn = executeSuccess ? 
                                  Messages.getString(MessagesConstant.SMSControllerAction_EXECUTEAPP_SUCCESS, closeCommand + " " + resultCodeErrorInfo) :
                                  Messages.getString(MessagesConstant.SMSControllerAction_EXECUTEAPP_FAILED, resultCodeErrorInfo); 
            return resultReturn;
        }
        return null;
    }

}
