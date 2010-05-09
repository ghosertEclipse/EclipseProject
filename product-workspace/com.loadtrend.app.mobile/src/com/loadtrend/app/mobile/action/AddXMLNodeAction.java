package com.loadtrend.app.mobile.action;

import org.dom4j.Element;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.util.MobileXMLUtil;

public class AddXMLNodeAction extends WorkbenchWindowAction {
    
    public void run() {
        IStructuredSelection selection = super.structuredSelection;
        Element element = (Element) selection.getFirstElement(); 
        String addUnable = element.attributeValue(MobileXMLUtil.ADDUNABLE);
        if (addUnable != null) {
            MessageDialog.openWarning(super.window.getShell(),
                                      Messages.getString(MessagesConstant.WARNING_TREE_EDITOR),
                                      Messages.getString(addUnable));
            return; 
        }
        if (element.getName().equalsIgnoreCase(MobileXMLUtil.SMSCENTER) || element.getName().equalsIgnoreCase(MobileXMLUtil.NODE)) {
            Element newElement = MobileXMLUtil.addElement(element, MobileXMLUtil.NODE, MobileXMLUtil.VALUE, "default");
            MobileXMLUtil.modifyElement(newElement, MobileXMLUtil.ADDUNABLE, MessagesConstant.WARNING_CODE_MANDATORY_FORADDNODE);
        }
        if (element.getName().equalsIgnoreCase(MobileXMLUtil.SMSCONTROL)) {
            Element newElement = MobileXMLUtil.addElement(element, MobileXMLUtil.CONTROL, MobileXMLUtil.CODE, "default");
            MobileXMLUtil.modifyElement(newElement, MobileXMLUtil.VALUE, "default");
            MobileXMLUtil.modifyElement(newElement, MobileXMLUtil.COMMAND, "default");
        }
        if (element.getName().equalsIgnoreCase(MobileXMLUtil.SMSCONSOLE)) {
            Element newElement = MobileXMLUtil.addElement(element, MobileXMLUtil.CONSOLE, MobileXMLUtil.CODE, "default");
            MobileXMLUtil.modifyElement(newElement, MobileXMLUtil.VALUE, "default");
            MobileXMLUtil.modifyElement(newElement, MobileXMLUtil.COMMAND, "default");
        }
        if (element.getName().equalsIgnoreCase(MobileXMLUtil.CONTROL)) {
            Element newElement = MobileXMLUtil.addElement(element, MobileXMLUtil.FILE, MobileXMLUtil.CODE, "default");
            MobileXMLUtil.modifyElement(newElement, MobileXMLUtil.VALUE, "default");
            MobileXMLUtil.modifyElement(newElement, MobileXMLUtil.PATH, "default");
        }
    }
}
