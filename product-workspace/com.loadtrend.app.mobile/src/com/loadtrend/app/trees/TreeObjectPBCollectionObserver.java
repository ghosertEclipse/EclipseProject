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
import com.loadtrend.app.mobile.editors.PBEditor;
import com.loadtrend.app.mobile.util.IOOperation;
import com.loadtrend.app.mobile.views.PBExplorerView;

public class TreeObjectPBCollectionObserver implements Observer
{
    private PBExplorerView view = null;
    
    public TreeObjectPBCollectionObserver(PBExplorerView view) {
        this.view = view;
    }
    
    public void update( Observable o, Object arg )
    {
        if ( arg instanceof Collection )
        {
            final TreeObject to = (TreeObject) o;
            final Collection pbCollection = (Collection) arg;
            
            // Save the data to local machine
            if ( to.getParent().getName().equals( Messages.getString( MessagesConstant.LOCALMACHINE_TEXT ) ) )
            {
                if ( to.getName().startsWith( Messages.getString( MessagesConstant.PBBOX_TEXT ) ) )
                {
                    IOOperation.writeObject( MobileAppConstant.JAVA_ROOT_PATH + MobileAppConstant.PBBOX_DATA,
                                             pbCollection );
                }
                if ( to.getName().startsWith( Messages.getString( MessagesConstant.DRAFT_TEXT ) ) )
                {
                    IOOperation.writeObject( MobileAppConstant.JAVA_ROOT_PATH + MobileAppConstant.PB_DRAFT_DATA,
                                             pbCollection );
                }
            }
            
            
            final IWorkbenchWindow window = view.getViewSite().getWorkbenchWindow();
            
            Display display = window.getShell().getDisplay();
            
            display.asyncExec( new Runnable()
            {
                public void run()
                {
                    IWorkbenchPage page = window.getActivePage();
                    
                    PBEditor editorPart = (PBEditor) page.findEditor( to.getEditorInput() );
                    if ( editorPart != null )
                    {
                        editorPart.getTableViewer().setInput( pbCollection );
                    }
                    
                    view.getTreeViewer().refresh();
                    view.getTreeViewer().setSelection(view.getTreeViewer().getSelection());
                }
            });
        }
    }
}
