package com.loadtrend.app.mobile.arrivalaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import com.loadtrend.app.mobile.util.MobileXMLUtil;

import loadtrend.mobile.Message;

public class SMSCenterAction extends MessageArrivalAction {
    
    private HashMap map = new HashMap();
    
    public MessageArrivalCommand getMessageArrivalCommand(Element element, final Message message) {
        
        // Coming code is: 天气;天气 上海;
        String[] codes =  message.getContent().trim().split(" ");
        
        String replyContent = this.treeWalk(element, codes, 0);
        
        List localCodeList = Arrays.asList(codes);
        
        if(replyContent != null) {
            // Get the matched value from xml file and save it to the map with the key telephone number.
            List list = (List) map.get(message.getTeleNum());
            if (list == null) {
                list = new ArrayList();
            } else {
                list.clear();
            }
            list.addAll(localCodeList);
            map.put(message.getTeleNum(), list);
        } else {
            // If not get, merge the value with exist value that saved before by the same telephone number,
            // and then try to get the matched value again. 
            List list = (List) map.get(message.getTeleNum());
            if (list != null) {
                List tempList = new ArrayList();
                tempList.addAll(list);
                tempList.addAll(localCodeList);
                replyContent = this.treeWalk(element, (String[])(tempList.toArray(new String[]{})), 0);
                if (replyContent != null) {
                    map.put(message.getTeleNum(), tempList);
                }
            }
        }
        
        if (replyContent == null) {
            return null;
        } else {
            // Reply target telephone number.
            final String content = replyContent;
            return new MessageArrivalCommand() {
                public String execute() {
                    return content;
                }
            };
        }
    }
    
    // Match the coming code with the value defined in XML to get the pre-defined reply content.
    // If not matched, return null.
    private String treeWalk(Element element, String[] codes, int i) {
        if ( i == codes.length) {
            String result = "";
            Iterator it = element.elementIterator();
            while(it.hasNext()) {
                Element tempElement = (Element) it.next();
                result = result + tempElement.attributeValue(MobileXMLUtil.VALUE) + NEW_LINE;
            }
            return result;
        }
        
        String result = null;
        Iterator it = element.elementIterator();
        while (it.hasNext()) {
            Element tempElement = (Element) it.next();
            if ( i < codes.length && codes[i].equalsIgnoreCase(tempElement.attributeValue(MobileXMLUtil.CODE))) {
                result = this.treeWalk(tempElement, codes, ++i);
            } else {
                continue;
            }
        }
        return result;
    }

//    public static void main(String[] args) {
//        SMSCenterAction action = new SMSCenterAction();
//        
//        Element element = MobileXMLUtil.getElement("smscenter");
//        Message message = new Message(); 
//        message.setContent("天气");
//        message.setTeleNum("13916939847");
//        action.execute(null, element, message);
//        
//        message = new Message(); 
//        message.setContent("香港");
//        message.setTeleNum("13916939847");
//        action.execute(null, element, message);
//        
//        message = new Message(); 
//        message.setContent("香港");
//        message.setTeleNum("13916939847");
//        action.execute(null, element, message);
//    }
}
