package com.loadtrend.app.mobile.action;

import org.dom4j.Element;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.util.MobileXMLUtil;

public class RemoveXMLNodeAction extends WorkbenchWindowAction {
    
    public void run() {
        MessageBox mb = new MessageBox( super.window.getShell(), SWT.YES | SWT.NO | SWT.ICON_WARNING );
        mb.setText( Messages.getString( MessagesConstant.CONFIRM_DELETENODE_MESSAGEBOX_TITLE ) );
        mb.setMessage( Messages.getString( MessagesConstant.CONFIRM_DELETENODE_MESSAGEBOX_BODY ) );
        int resultCode = mb.open();
        if ( resultCode == SWT.NO ) return;
        
        // Only the node has code value can be added child node.
        IStructuredSelection selection = super.structuredSelection;
        Element element = (Element) selection.getFirstElement(); 
        String deleteUnable = element.attributeValue(MobileXMLUtil.DELETEUNABLE);
        if (deleteUnable != null) {
            MessageDialog.openWarning( super.window.getShell(),
                                       Messages.getString( MessagesConstant.WARNING_TREE_EDITOR ),
                                       Messages.getString(deleteUnable));    
            return;
        }
        MobileXMLUtil.removeElement(element);
    }
}
