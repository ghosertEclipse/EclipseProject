package com.loadtrend.app.mobile.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class WorkbenchWindowAction extends Action implements
        IWorkbenchWindowActionDelegate, IObjectActionDelegate, IViewActionDelegate, IPartListener {
    
    protected IWorkbenchWindow window = null;
    
    protected IStructuredSelection structuredSelection = null;
    
    protected IWorkbenchPart part = null;
    
    protected IViewPart view = null;
    
    protected WorkbenchWindowAction()
    {
    }

    protected WorkbenchWindowAction( String text )
    {
        super( text );
    }

    protected WorkbenchWindowAction( String text, ImageDescriptor image )
    {
        super( text, image );
    }

    protected WorkbenchWindowAction( String text, int style )
    {
        super( text, style );
    }

    public void dispose() {
//        if (this.window != null) {
//            this.window.getActivePage().removePartListener(this);
//        }
    }

    public void init(IWorkbenchWindow window) {
        this.window = window;
        this.window.getActivePage().addPartListener(this);
    }

    public void run(IAction action) {
        this.run();
    }

    public void selectionChanged(IAction action, ISelection selection) {
        structuredSelection = (IStructuredSelection) selection;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
     */
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        // Design for the popup menu action.
        this.window = targetPart.getSite().getWorkbenchWindow();
        this.part = targetPart;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
     */
    public void init(IViewPart view) {
        this.window = view.getSite().getWorkbenchWindow();
        this.view = view;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IPartListener#partActivated(org.eclipse.ui.IWorkbenchPart)
     */
    public void partActivated(IWorkbenchPart part) {
        this.part = part;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IPartListener#partBroughtToTop(org.eclipse.ui.IWorkbenchPart)
     */
    public void partBroughtToTop(IWorkbenchPart part) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IPartListener#partClosed(org.eclipse.ui.IWorkbenchPart)
     */
    public void partClosed(IWorkbenchPart part) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IPartListener#partDeactivated(org.eclipse.ui.IWorkbenchPart)
     */
    public void partDeactivated(IWorkbenchPart part) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IPartListener#partOpened(org.eclipse.ui.IWorkbenchPart)
     */
    public void partOpened(IWorkbenchPart part) {
        // TODO Auto-generated method stub
        
    }
}
