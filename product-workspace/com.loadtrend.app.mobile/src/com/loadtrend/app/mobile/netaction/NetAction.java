package com.loadtrend.app.mobile.netaction;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import com.loadtrend.app.mobile.action.WorkbenchWindowAction;
import com.loadtrend.app.mobile.clientRemote.ClientRemotePlugin;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.editors.AppEditorInput;
import com.loadtrend.app.mobile.editors.WaitForNetProcessEditor;
import com.loadtrend.web.mobile.client.JMobileClientManager;

public abstract class NetAction extends WorkbenchWindowAction {

    protected JMobileClientManager jMobileClientManager = ClientRemotePlugin.getDefault().getJMobileClientManager();
    
    private static boolean isNetAcitonsEnabled = true;
    
    private Exception exception = null;
    
    private static IEditorInput waitForNetProcessEditorInput= new AppEditorInput(
            Messages.getString( MessagesConstant.WAITFOR_NETPROCESS_EDITORINPUT_NAME ),
            null,
            Messages.getString( MessagesConstant.WAITFOR_NETPROCESS_EDITORINPUT_TOOLTIPTEXT )
            );

    public void run() {
    	if (!isNetAcitonsEnabled) {
    		this.openWaitForNetProcessEditor();
    		return;
    	}
    	
    	if (!this.openWaitForNetProcessEditor()) return;
    	
        isNetAcitonsEnabled = false;
        
        new Thread() {
            public void run() {
		    	try {
	                NetAction.this.netExecute();
		    	} catch (Exception e) {
                    exception = e;
                    System.out.println(e.getMessage());
		    	}
                window.getShell().getDisplay().asyncExec(new Runnable() {
                    public void run() {
                        NetAction.this.closeWaitForNetProcessEditor();
                        if (exception != null) {
					        MessageBox messageBox = new MessageBox(NetAction.this.window.getShell());
					    	messageBox.setMessage("无法访问远程服务器，请检查网络连接. 或服务器不可用, 请稍候再试.");
					    	messageBox.open();
                            exception = null;
                        } else {
                            NetAction.this.performUIAfterNetExecute();
                        }
                        isNetAcitonsEnabled = true;
                    }
                });
	       }
        }.start();
    }
    
    /**
     * Please overwrite the method with the only NON-UI thread code,
     * do not put any UI code or controls in this method, otherwise, SWTException throws.
     */
    protected abstract void netExecute() throws Exception;
    
    /**
     * The chance to refesh UI with the results from NON-UI thread if no exception throwed in netExecute() method. 
     */
    protected abstract void performUIAfterNetExecute();

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
    
    /**
     * Show the real time progress when application is communicating with net.
     * @param content
     */
    public void showRealTimeInfo( String content )
    {
        final String finalContent = content;
        super.window.getShell().getDisplay().asyncExec( new Runnable()
        {
            public void run()
            {
                IWorkbenchPage page = window.getActivePage();
                WaitForNetProcessEditor editor = (WaitForNetProcessEditor) page.findEditor(waitForNetProcessEditorInput);
                if ( editor != null )
                {
                    editor.createBodyHtml( finalContent );
                }
            }
        });
    }
}
