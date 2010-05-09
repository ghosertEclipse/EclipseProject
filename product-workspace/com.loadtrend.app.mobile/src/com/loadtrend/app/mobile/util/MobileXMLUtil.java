package com.loadtrend.app.mobile.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.loadtrend.app.mobile.data.MobileAppConstant;

public class MobileXMLUtil {
    
    public static final String VALUE = "value";
    
    public static final String CODE = "code";
    
    public static final String NODE = "node";
    
    public static final String CLASS = "class";
    
    public static final String SETTINGS = "settings";
    
    public static final String SMSCENTER = "smscenter";
    
    public static final String SMSCONTROL = "smscontrol";
    
    public static final String SMSCONSOLE = "smsconsole";
    
    public static final String ERRORPROMPT = "errorprompt";
    
    public static final String PROMPT = "prompt";
    
    public static final String RESULTRETURNABLE = "resultReturnable";
    
    public static final String CONTINUEPROMPT = "continuePrompt";
    
    public static final String CONTINUECOMMAND = "continueCommand";
    
    public static final String NUMBEROFTHREAD = "numberOfThread";
        
    public static final String RESTARTTHREADCOMMAND = "restartThreadCommand";
    
    public static final String RESTARTALLTHREADCOMMAND = "restartAllThreadCommand";
    
    public static final String DELETEAFTERARRIVAL = "deleteAfterArrival";
    
    public static final String CLOSECOMMAND = "closeCommand";
    
    public static final String CONSOLECOMMAND = "consoleCommand";
    
    public static final String ENABLE = "enable";
    
    public static final String DELETEUNABLE = "deleteUnable";
    
    public static final String ADDUNABLE = "addUnable";
    
    public static final String CONTROL = "control";
    
    public static final String CONSOLE = "console";
    
    public static final String FILE = "file";
    
    public static final String PATH = "path";
    
    public static final String COMMAND = "command";
    
    public static final String EDITABLE = "editable";
    
    private static final String filename = MobileAppConstant.MOBILE_XML;
    
    private static Document document = null;
    
    private static MobileXMLModifyListener listener = null;
    
    private static final String NEW_LINE = "\n";

    private static Document getDocument() {
        if (document == null) {
            // Create the XML document.
            try {
                InputStream inputStream = new FileInputStream(filename);
                SAXReader reader = new SAXReader();
                document = reader.read(inputStream);
                inputStream.close();
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (DocumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return document;
    }
    
    public static Element getRootElement() {
        return getDocument().getRootElement();
    }

    public static Element getElement(String elementName) {
            return getRootElement().element(elementName);
    }
    
    /**
     * Add the attribute if it does not exist, or modify the specified attribute name with the specified attribute value.
     * If the attribute specified with the atrribute name is not null, and attribute value is null, remove the attribute.
     * @param element
     * @param attributeName defined in MobileXMLUtil, such as: MobileXMLUtil.CODE, can not be null.
     * @param attributeValue remove the attribute name specifed if attribute value is null value passed in.
     */
    public static void modifyElement(Element element, String attributeName, String attributeValue) {
        Attribute attribute = element.attribute(attributeName);
        if (attributeValue == null) {
            if (attribute != null) element.remove(attribute);
        } else {
	        if (attribute == null) {
	            element.addAttribute(attributeName, attributeValue);
	        } else {
	            element.attribute(attributeName).setValue(attributeValue);
	        }
        }
        saveToXML();
        MobileXMLUtil.notifyMobileXMLModifyListener(element);
    }
    
    /**
     * Add the new child element with the specified name, attribute name and value.
     * @param parentElement
     * @param childElementName defined in MobileXMLUtil, such as: MobileXMLUtil.NODE
     * @param attributeName defined in MobileXMLUtil, such as: MobileXMLUtil.CODE
     * @param attributeValue
     * @return the added element object.
     */
    public static Element addElement(Element parentElement, String childElementName, String attributeName, String attributeValue) {
        Element newElement = parentElement.addElement(childElementName)
                                    .addAttribute(attributeName, attributeValue);
        saveToXML();
        MobileXMLUtil.notifyMobileXMLModifyListener(newElement);
        return newElement;
    }
    
    public static void removeElement(Element element) {
        Element parentElement = element.getParent();
        parentElement.remove(element);
        saveToXML();
        MobileXMLUtil.notifyMobileXMLModifyListener(parentElement);
    }
    
    private static void saveToXML() {
        try {
            FileOutputStream outputStream = new FileOutputStream(filename);
            XMLWriter writer = new XMLWriter(outputStream, OutputFormat.createPrettyPrint());
            writer.write(getDocument());
            writer.close();
            outputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
    /**
     * Get the command list.
     * return "" if no command list.
     */
    public static String getCommandList(Element element) {
        String command = "";
        Iterator it = element.elementIterator();
        while(it.hasNext()) {
            Element tempElement = (Element) it.next();
            command = command + tempElement.attributeValue(MobileXMLUtil.CODE) + NEW_LINE;
        }
        return command;
    }
    
    /**
     * Get child element with the specified parent element and matched code for searching child element.
     * return null if no matched element.
     */
    public static Element getMatchedElement(Element element, String code) {
        Iterator it = element.elementIterator();
        while (it.hasNext()) {
            Element tempElement = (Element) it.next();
            if ( code.equalsIgnoreCase(tempElement.attributeValue(MobileXMLUtil.CODE))) {
                return tempElement;
            } 
        }
        return null;
    }
    
    public static void addMobileXMLModifyListener(MobileXMLModifyListener listener) {
        MobileXMLUtil.listener = listener;
    }
    
    private static void notifyMobileXMLModifyListener(Element element) {
        if (MobileXMLUtil.listener != null) {
            listener.mobileXMLModified(element);
        }
    }
    
    public static int getInitMessageArrivalWorkThreadCount() {
        String numberOfThread = getElement(MobileXMLUtil.SETTINGS).attributeValue(MobileXMLUtil.NUMBEROFTHREAD);
        return Integer.parseInt(numberOfThread);
    }
}
