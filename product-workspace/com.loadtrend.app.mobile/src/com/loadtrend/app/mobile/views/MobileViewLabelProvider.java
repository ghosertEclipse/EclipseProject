package com.loadtrend.app.mobile.views;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.model.IWorkbenchAdapter;

public class MobileViewLabelProvider extends LabelProvider {

	private Map imageTable = new HashMap(7);
    
    protected IWorkbenchAdapter getAdapter(Object element) {
        IWorkbenchAdapter adapter = null;
        if (element instanceof IAdaptable)
            adapter = (IWorkbenchAdapter) ((IAdaptable) element)
                    .getAdapter(IWorkbenchAdapter.class);
        if (element != null && adapter == null)
            adapter = (IWorkbenchAdapter) Platform.getAdapterManager()
                    .loadAdapter(element, IWorkbenchAdapter.class.getName());
        return adapter;
    }
    
    public Image getImage(Object element) {
        ImageDescriptor descriptor = this.getAdapter(element).getImageDescriptor(element);
        Image image = (Image) imageTable.get(descriptor);
        if (image == null) {
            image = descriptor.createImage();
            imageTable.put(descriptor, image);
        }
        return image;
    }

    public String getText(Object element) {
        return this.getAdapter(element).getLabel(element);
    }
    
    public void dispose() {
        if (imageTable != null) {
            for (Iterator i = imageTable.values().iterator(); i.hasNext();) {
                ((Image) i.next()).dispose();
            }
            imageTable = null;
        }
    }
}