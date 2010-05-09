package com.loadtrend.app.mobile.loadtool.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.springframework.remoting.RemoteAccessException;

import com.loadtrend.app.mobile.loadtool.AppEditorInput;
import com.loadtrend.app.mobile.loadtool.WaitForNetProcessEditor;
import com.loadtrend.app.util.springClientRemote.SpringClientRemotePlugin;
import com.loadtrend.web.mobile.client.JMobileClientManager;
import com.loadtrend.web.mobile.service.JMobileManager;

public abstract class NetAction extends Action implements IViewActionDelegate {

    protected IViewPart view = null;
    
    protected IWorkbenchWindow window = null;
    
    protected IStructuredSelection structuredSelection = null;
    
    protected JMobileManager jMobileManager = SpringClientRemotePlugin.getDefault().getJMobileManager();
    
    protected JMobileClientManager jMobileClientManager = SpringClientRemotePlugin.getDefault().getJMobileClientManager();
    
    private static boolean isNetAcitonsEnabled = true;
    
    private static IEditorInput waitForNetProcessEditorInput = new AppEditorInput("net", null, "net");
    
    public NetAction() {
    }
    
    public NetAction(String text) {
    	super(text);
    }
    
    /* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
    	if (!isNetAcitonsEnabled) {
    		this.openWaitForNetProcessEditor();
    		return;
    	}
    	
    	if (!this.openWaitForNetProcessEditor()) return;
    	
        new Thread() {
            public void run() {
                NetAction.this.window.getShell().getDisplay().asyncExec(new Runnable() {
                    public void run() {
				    	try {
				    	    NetAction.this.netExecute();
				    	} catch (RemoteAccessException remoteAccessException) {
                            System.out.println(remoteAccessException.getMessage());
				    		MessageBox messageBox = new MessageBox(NetAction.this.window.getShell());
				    		messageBox.setMessage("无法访问远程服务器，请检查网络连接. 或服务器不可用, 请稍候再试.");
				    		messageBox.open();
				    	} catch (Exception e) {
                            System.out.println(e.getMessage());
				    		MessageBox messageBox = new MessageBox(NetAction.this.window.getShell());
				    		messageBox.setMessage(e.getMessage());
				    		messageBox.open();
				    	}
                        NetAction.this.closeWaitForNetProcessEditor();
                    }
                });
	       }
        }.start();
	}

	public void init(IViewPart view) {
        this.view = view;
    	this.window = view.getSite().getWorkbenchWindow();
    }

    public void run(IAction action) {
    	this.run();
    }
    
    protected abstract void netExecute();

    public void selectionChanged(IAction action, ISelection selection) {
        this.structuredSelection = (IStructuredSelection) selection;
    }
    
	private boolean openWaitForNetProcessEditor() {
		try {
            IWorkbenchPage page = window.getActivePage();
            if (page.isEditorAreaVisible()) {
			    page.openEditor(waitForNetProcessEditorInput, WaitForNetProcessEditor.EDITOR_ID );
            }
		} catch ( PartInitException e ) {
			MessageDialog.openError( window.getShell(),
					"Open net process editor error.",
					e.getMessage() );
			return false;
		}
		return true;
	}
	
	private void closeWaitForNetProcessEditor() {
        IWorkbenchPage page = window.getActivePage();
        page.closeEditor(page.findEditor(waitForNetProcessEditorInput), false);
		return;
	}
}
