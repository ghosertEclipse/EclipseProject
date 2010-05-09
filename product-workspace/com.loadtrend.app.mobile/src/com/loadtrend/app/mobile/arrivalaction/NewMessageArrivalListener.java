package com.loadtrend.app.mobile.arrivalaction;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;

import org.dom4j.Element;

import com.loadtrend.app.mobile.data.Global;
import com.loadtrend.app.mobile.util.MobileXMLUtil;
import com.loadtrend.app.mobile.views.smsArrival.SMSArrivalViewListener;

import loadtrend.mobile.Message;
import loadtrend.mobile.MessageArrivalListener;
import loadtrend.mobile.Mobile;

public class NewMessageArrivalListener implements MessageArrivalListener {
    
    private Element rootElement = MobileXMLUtil.getRootElement();
    
    private Mobile mobile = Global.getOrCreateMobile();
    
    private static final String NEW_LINE = "\n";
    
    private static HashMap continueSMSContentMap = new HashMap();
    
    private SMSArrivalViewListener listener = null;
    
    public NewMessageArrivalListener(SMSArrivalViewListener listener) {
        this.listener = listener;
    }
    
    public void handler(Message message) throws Exception {
        
        // pop up the prompt window.
        listener.popupWindow(message);
        
        // Get setting element.
        Element settingsElement = rootElement.element(MobileXMLUtil.SETTINGS);
        
        // Delete arrival sms if necessary.
        if (settingsElement.attributeValue(MobileXMLUtil.DELETEAFTERARRIVAL).equalsIgnoreCase("true")) {
            mobile.deleteMessage(message);
            listener.deleteMessageLog(message);
        }
        
        // Add receiving sms to the monitorText
        listener.refreshMonitorText(message, true);
        
        // Restart all the message arrival work thread.
        if (message.getContent().equalsIgnoreCase(settingsElement.attributeValue(MobileXMLUtil.RESTARTALLTHREADCOMMAND))) {
            mobile.restartPreviousHandleringMessageArrivalWorkThread();
            this.sendMessage(mobile, message.getTeleNum(), "All the channels are restored, please resend your request.", 2);
            return;
        }
        
        // Warning the available arrival work thread is lack. that means the last available arrival work thread,
        // will be remained to send the warning info just like below.
        if (mobile.getAvailableMessageArrivalWorkThreadCount() == 0) {
            this.sendMessage(mobile, message.getTeleNum(), "No available sms arrival work thread, please inform the admin to solve the problem by restarting all the work threads.", 2);
            return;
        }
        
        // Restart the message arrival work thread.
        if (message.getContent().equalsIgnoreCase(settingsElement.attributeValue(MobileXMLUtil.RESTARTTHREADCOMMAND))) {
            mobile.restartMessageArrivalWorkThread(message.getTeleNum());
            this.sendMessage(mobile, message.getTeleNum(), "The channel is restored, please resend your request.", 2);
            return;
        }
        
        // This listener will skip the request which the sender's previous request is handlering.
        if (mobile.checkMessageArrivalWorkThread(message.getTeleNum())) {
	        this.sendMessage(mobile, message.getTeleNum(), MessageFormat.format("Handlering your previous request, reply {0} to cancel",
	                          new Object[]{settingsElement.attributeValue(MobileXMLUtil.RESTARTTHREADCOMMAND)}), 2);
            return;
        }
        
        String initName = Thread.currentThread().getName();
        Thread.currentThread().setName(message.getTeleNum());
        // Real handlering method.
        realHandler(message, settingsElement);
        Thread.currentThread().setName(initName);
    }
    
    private void realHandler(Message message, Element settingsElement) throws Exception {
        
        // Matched MobileXMLUtil.CONTINUECOMMAND. Send the remained sms.
        String[] keys = message.getContent().trim().split(" ");
        if (keys[0].equalsIgnoreCase(settingsElement.attributeValue(MobileXMLUtil.CONTINUECOMMAND))) {
            if ((keys.length == 2 && keys[1].matches("[0-9]+")) || keys.length == 1) {
                int iCount = keys.length == 1 ? 1 : Integer.parseInt(keys[1]);
                Object object = continueSMSContentMap.get(message.getTeleNum());
                String content = null;
                if (object != null) {
                    content = (String) object;
                } else {
                    content = "No remained next sms.";
                }
                String contentRemain = this.sendMessage(mobile, message.getTeleNum(), content, iCount);
                if (contentRemain != null && contentRemain.equals("")) {
                    continueSMSContentMap.put(message.getTeleNum(), null);
                }
                return;
            }
        }
        
        // Prepared to matched messageArrivalCommand and execute it.
        StringBuffer commandList = new StringBuffer();
        Iterator it = rootElement.elementIterator();
        while (it.hasNext()) {
            Element element = (Element) it.next();
            
            // Skip the setting element.
            if (element.getName().equalsIgnoreCase(MobileXMLUtil.SETTINGS)) {
                continue;
            }
            
            // Skip if not enable.
            if (element.attributeValue(MobileXMLUtil.ENABLE).equalsIgnoreCase("false")) continue;
            
            // Get the command list.
            if (message.getContent().trim().equals("")) {
                commandList.append(element.attributeValue(MobileXMLUtil.VALUE) + NEW_LINE);
                commandList.append(MobileXMLUtil.getCommandList(element));
                continue;
            }
            
            // Run the MessageArrivalAction
            String fullname = element.attributeValue(MobileXMLUtil.CLASS);
            MessageArrivalAction action = MessageArrivalActionFactory.getInstance().getAction(fullname);
            MessageArrivalCommand messageArrivalCommand = action.getMessageArrivalCommand(element, message);
            if (messageArrivalCommand != null) {
                String resultToBeSent = messageArrivalCommand.execute();
                this.sendMessage(mobile, message.getTeleNum(), resultToBeSent, 2);
                return;
            }
        }        
        // Get the sms content "", so send commandList to the target number if exist.
        if (commandList.length() > 0) {
            this.sendMessage(mobile, message.getTeleNum(), commandList.toString(), 2);
            return;
        }
        // no command found, send the prompt message if necessary and return.
        if (settingsElement.attributeValue(MobileXMLUtil.ERRORPROMPT).equalsIgnoreCase("true")) {
            String replyContent = settingsElement.attributeValue(MobileXMLUtil.PROMPT);
            this.sendMessage(mobile, message.getTeleNum(), replyContent, 2);
        } 
    }

    private String sendMessage(Mobile mobile, String teleNum, String content, int iCount) throws Exception {
        Element settingsElement = this.rootElement.element(MobileXMLUtil.SETTINGS);
        String continuePrompt = NEW_LINE + settingsElement.attributeValue(MobileXMLUtil.CONTINUEPROMPT);
        // Send message
        String[] resultString = mobile.sendMessage(teleNum, content, continuePrompt, iCount);
        // refresh the monitor text.
        String contentSent = resultString[0];
        Message message = new Message();
        message.setTeleNum(teleNum);
        message.setContent(contentSent);
        this.listener.refreshMonitorText(message, false);
        // store the remained content.
        String contentRemain = resultString[1];
        if (!contentRemain.equals("")) continueSMSContentMap.put(teleNum, contentRemain.toString());
        return contentRemain;
    }
}
