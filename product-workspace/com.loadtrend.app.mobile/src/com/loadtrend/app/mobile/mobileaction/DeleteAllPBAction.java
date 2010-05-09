package com.loadtrend.app.mobile.mobileaction;

import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.trees.TreeObject;

public class DeleteAllPBAction extends DeletePBAction
{
    public boolean performUIBeforeNonUI()
    {
        if ( !super.confirmToDelete() ) return false;
        
        // Get the TreeObject in which the PB will be deleted.
        to = (TreeObject) super.structuredSelection.getFirstElement();
        
        collection = to.getCollection();
        
        if ( to.getParent().getName().startsWith( Messages.getString( MessagesConstant.LOCALMACHINE_TEXT ) ) )
        {
            // handle the local machine
            this.performUIAfterNonUI();
            return false;
        }
        
        return true;
    }
    
    public void performUIAfterNonUI()
    {
        if ( !super.hasError() )
        {
            to.getCollection().clear();

            // Notify the observer to refresh interface.
            to.setCollection( to.getCollection() );
        }
    }
}
