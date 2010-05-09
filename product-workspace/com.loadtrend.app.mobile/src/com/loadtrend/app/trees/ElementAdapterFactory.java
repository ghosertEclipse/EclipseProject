package com.loadtrend.app.trees;

import org.dom4j.Element;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;

import com.loadtrend.app.mobile.data.ImageConstant;
import com.loadtrend.app.mobile.data.ImageLoader;
import com.loadtrend.app.mobile.util.MobileXMLUtil;

public class ElementAdapterFactory implements IAdapterFactory {
    
    private IWorkbenchAdapter elementAdapter = new IWorkbenchAdapter() {

        public Object[] getChildren(Object o) {
            Element element = (Element) o;
            return element.elements().toArray();
        }

        public ImageDescriptor getImageDescriptor(Object object) {
            Element element = (Element) object;
            ImageDescriptor descriptor = null;
            if (element.getName().equalsIgnoreCase(MobileXMLUtil.SETTINGS)) {
                descriptor = ImageLoader.getInstance().getImageDescriptor(ImageConstant.SMSArrivalView_SETTINGS);
                return descriptor;
            }
            if (element.getName().equalsIgnoreCase(MobileXMLUtil.SMSCENTER)) {
                descriptor = ImageLoader.getInstance().getImageDescriptor(ImageConstant.SMSArrivalView_SMSCENTER);
                return descriptor;
            }
            if (element.getName().equalsIgnoreCase(MobileXMLUtil.NODE)) {
                descriptor = ImageLoader.getInstance().getImageDescriptor(ImageConstant.SMSArrivalView_NODE);
                return descriptor;
            }
            if (element.getName().equalsIgnoreCase(MobileXMLUtil.SMSCONTROL)) {
                descriptor = ImageLoader.getInstance().getImageDescriptor(ImageConstant.SMSArrivalView_SMSCONTROL);
                return descriptor;
            }
            if (element.getName().equalsIgnoreCase(MobileXMLUtil.CONTROL)) {
                descriptor = ImageLoader.getInstance().getImageDescriptor(ImageConstant.SMSArrivalView_CONTROL);
                return descriptor;
            }
            if (element.getName().equalsIgnoreCase(MobileXMLUtil.FILE)) {
                descriptor = ImageLoader.getInstance().getImageDescriptor(ImageConstant.SMSArrivalView_FILE);
                return descriptor;
            }
            if (element.getName().equalsIgnoreCase(MobileXMLUtil.SMSCONSOLE)) {
                descriptor = ImageLoader.getInstance().getImageDescriptor(ImageConstant.SMSArrivalView_SMSCONSOLE);
                return descriptor;
            }
            if (element.getName().equalsIgnoreCase(MobileXMLUtil.CONSOLE)) {
                descriptor = ImageLoader.getInstance().getImageDescriptor(ImageConstant.SMSArrivalView_CONSOLE);
                return descriptor;
            }
            return descriptor;
        }

        public String getLabel(Object o) {
            String code = ((Element) o).attributeValue(MobileXMLUtil.CODE);
            String value = ((Element) o).attributeValue(MobileXMLUtil.VALUE);
            if (code == null || code.equals("")) {
                return value;
            } else {
                return value + "[" + code + "]";
            }
        }

        public Object getParent(Object o) {
            Element element = (Element) o;
            return element.getParent();
        }
    };
    
    public Object getAdapter(Object adaptableObject, Class adapterType) {
        if (adapterType == IWorkbenchAdapter.class && adaptableObject instanceof Element) {
            return elementAdapter;
        }
        return null;
    }

    public Class[] getAdapterList() {
        return new Class[] { IWorkbenchAdapter.class };
    }
}
