package com.loadtrend.app.mobile.views.smsArrival;

import java.util.HashMap;

import org.dom4j.Element;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.loadtrend.app.mobile.util.MobileXMLUtil;

public class RightSectionClientFactory {
    
    private static HashMap map = new HashMap();
    
    private static RightSectionClientFactory factory = null;
    
    private RightSectionClientFactory() {
    }
    
    public static RightSectionClientFactory getInstance() {
        if (factory == null) {
            factory = new RightSectionClientFactory();
            map.put(MobileXMLUtil.SETTINGS, new RightSectionClientSettingsElement());
            map.put(MobileXMLUtil.SMSCENTER, new RightSectionClientSMSCenterElement());
            map.put(MobileXMLUtil.SMSCONTROL, new RightSectionClientSMSControlElement());
            map.put(MobileXMLUtil.SMSCONSOLE, new RightSectionClientSMSConsoleElement());
            map.put(MobileXMLUtil.NODE, new RightSectionClientNodeElement());
            map.put(MobileXMLUtil.CONTROL, new RightSectionClientControlElement());
            map.put(MobileXMLUtil.FILE, new RightSectionClientFileElement());
            map.put(MobileXMLUtil.CONSOLE, new RightSectionClientConsoleElement());
        }
        return factory;
    }
    
    public void createRightSectionClient(Element element, Composite rightSectionClient, FormToolkit kit) {
        ((RightSectionClient) map.get(element.getName())).build(element, rightSectionClient, kit);
    }
}
