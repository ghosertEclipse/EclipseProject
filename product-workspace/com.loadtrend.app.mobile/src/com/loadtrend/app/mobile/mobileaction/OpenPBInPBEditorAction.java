package com.loadtrend.app.mobile.mobileaction;

import java.util.Collection;

import loadtrend.mobile.Mobile;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.editors.PBEditor;
import com.loadtrend.app.mobile.util.SpecialEntity;
import com.loadtrend.app.mobile.views.PBExplorerView;
import com.loadtrend.app.trees.TreeObject;
import com.loadtrend.app.trees.TreeParent;

public class OpenPBInPBEditorAction extends MobileAction
{
    private IWorkbenchPage page = null;
    
    private TreeObject to = null;
    
    protected TreeParent invisibleRoot = null;
    
    private TreeObject phonebookBox = null;
    
    /* (non-Javadoc)
     * @see com.loadtrend.app.mobile.mobileaction.MobileAction#performNonUI()
     */
    public void performNonUI()
    {
        if ( to.getCollection() != null )  return;
        retrievePhoneBook();
    }

    /* (non-Javadoc)
     * @see com.loadtrend.app.mobile.mobileaction.MobileAction#performUIAfterNonUI()
     */
    public void performUIAfterNonUI()
    {
        if ( super.hasError() ) return;
        try
        {
            PBEditor editorPart = (PBEditor) page.openEditor( to.getEditorInput(), PBEditor.EDITOR_ID );
            editorPart.setTreeObject( to );
        }
        catch ( PartInitException e )
        {
            MessageDialog.openError( super.window.getShell(), 
                                     Messages.getString( MessagesConstant.OPEN_PBEDITOR_FAILURE_MESSAGEBOX_TITLE ),
                                     e.getMessage() );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.loadtrend.app.mobile.mobileaction.MobileAction#performUIBeforeNonUI()
     */
    public boolean performUIBeforeNonUI()
    {
		page = super.window.getActivePage();
        
        to = (TreeObject) super.structuredSelection.getFirstElement();
        
        if ( to.getParent().getName().startsWith( Messages.getString( MessagesConstant.LOCALMACHINE_TEXT ) ) )
        {
            // handle the local machine
            this.performUIAfterNonUI();
            return false;
        }
        
        IEditorInput editorInput = to.getEditorInput();
        
        IEditorPart editorPart = page.findEditor( editorInput );
        if ( editorPart == null )
        {
            invisibleRoot = ((PBExplorerView) super.view).getInvisibleRoot();
            return true;
        }
        else
        {
            page.bringToTop( editorPart );
            editorPart.setFocus();
            return false;
        }
    }
    
    protected void retrievePhoneBook()
    {
        TreeObject localMachine = invisibleRoot.getChildren( Messages.getString( MessagesConstant.LOCALMACHINE_TEXT ) );
        phonebookBox = ( (TreeParent) localMachine ).getChildren( Messages.getString( MessagesConstant.PBBOX_TEXT ) );
        
        TreeObject[] mobileSimCard = invisibleRoot.getChildren();
        
        for ( int i = 0; i < mobileSimCard.length; i++ )
        {
            TreeParent tp = (TreeParent) mobileSimCard[i];
            
            // Only read the message from Mobile and SimCard
            if ( !tp.getName().startsWith( Messages.getString( MessagesConstant.MOBILE_TEXT ) ) &&
                 !tp.getName().startsWith( Messages.getString( MessagesConstant.SIMCARD_TEXT ) ) )
            {
                continue;
            }
            
            try
            {
                TreeObject to = tp.getChildren( Messages.getString( MessagesConstant.PBBOX_TEXT ) );
                this.retrievePhoneBookByTreeObject( to );
            }
            catch ( Exception e )
            {
                // You can't invoke SWT control in NonUI
                super.saveError( Messages.getString( MessagesConstant.OPEN_PBINPBEDITOR_FAILURE_MESSAGEBOX_TITLE ), null, e );
                return;
            }
        }
    }
    
    private void retrievePhoneBookByTreeObject( TreeObject to ) throws Exception
    {
        Mobile mobile = super.mobile;
        if ( mobile == null )
            return;

        String deviceName = to.getParent().getName();
        String deviceParam = SpecialEntity.getPBMemoParam( deviceName );
        deviceName = deviceName + "-" + to.getName();
        
        Collection collection = null;
        
        super.showRealTimeInfo( Messages.getString( MessagesConstant.OPEN_PBINPBEDITOR_ACTION_INFO, deviceName ) );
        collection = mobile.listPhoneBooks( deviceParam );
        
        to.setCollection( collection );
        phonebookBox.getCollection().addAll( collection );
        phonebookBox.setCollection( phonebookBox.getCollection() );
    }
}
