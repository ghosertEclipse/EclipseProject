package com.loadtrend.app.mobile.views;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.model.IWorkbenchAdapter;

import com.loadtrend.app.trees.TreeParent;

public class MobileViewContentProvider implements ITreeContentProvider {
    
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
    
    public Object[] getChildren(Object parentElement) {
        return this.getAdapter(parentElement).getChildren(parentElement);
    }

    public Object getParent(Object element) {
        return this.getAdapter(element).getParent(element);
    }

    public boolean hasChildren(Object element) {
        return this.getAdapter(element).getChildren(element).length > 0;
    }

    public Object[] getElements(Object inputElement) {
        TreeParent tp = (TreeParent) inputElement;
        return tp.getChildren();
    }

    public void dispose() {
        // TODO Auto-generated method stub

    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        // TODO Auto-generated method stub

    }

}
