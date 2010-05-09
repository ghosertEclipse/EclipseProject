package com.loadtrend.app.trees;

import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;

import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.data.MobileAppConstant;
import com.loadtrend.app.mobile.editors.SMSEditor;
import com.loadtrend.app.mobile.util.IOOperation;
import com.loadtrend.app.mobile.views.SMSExplorerView;

public class TreeObjectSMSCollectionObserver implements Observer
{
    private SMSExplorerView view = null;
    
    public TreeObjectSMSCollectionObserver(SMSExplorerView view) {
        this.view = view;
    }
    
    public void update( Observable o, Object arg )
    {
        if ( arg instanceof Collection )
        {
            final TreeObject to = (TreeObject) o;
            final Collection smsCollection = (Collection) arg;
            
            // Save the data to local machine
            if ( to.getParent().getName().equals( Messages.getString( MessagesConstant.LOCALMACHINE_TEXT ) ) )
            {
                if ( to.getName().startsWith( Messages.getString( MessagesConstant.SMSBOX_TEXT ) ) )
                {
                    IOOperation.writeObject( MobileAppConstant.JAVA_ROOT_PATH + MobileAppConstant.SMSBOX_DATA,
                                             smsCollection );
                }
                if ( to.getName().startsWith( Messages.getString( MessagesConstant.DRAFT_TEXT ) ) )
                {
                    IOOperation.writeObject( MobileAppConstant.JAVA_ROOT_PATH + MobileAppConstant.SMS_DRAFT_DATA,
                                             smsCollection );
                }
            }
            
            final IWorkbenchWindow window = view.getViewSite().getWorkbenchWindow();
            
            Display display = window.getShell().getDisplay();
            
            display.asyncExec( new Runnable()
            {
                public void run()
                {
                    IWorkbenchPage page = window.getActivePage();
                    
                    SMSEditor editorPart = (SMSEditor) page.findEditor( to.getEditorInput() );
                    if ( editorPart != null )
                    {
                        editorPart.getTableViewer().setInput( smsCollection );
                    }
                    
                    view.getTreeViewer().refresh();
                    view.getTreeViewer().setSelection(view.getTreeViewer().getSelection());
                }
            });
        }
    }
}
