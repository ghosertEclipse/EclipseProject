package com.loadtrend.app.trees;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;

import com.loadtrend.app.mobile.data.ImageConstant;
import com.loadtrend.app.mobile.data.ImageLoader;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;

public class MobileAdapterFactory implements IAdapterFactory {
    
    private IWorkbenchAdapter treeParentAdapter = new IWorkbenchAdapter() {

        public Object[] getChildren(Object o) {
            TreeParent parent = (TreeParent) o;
            return parent.getChildren();
        }

        public ImageDescriptor getImageDescriptor(Object object) {
            TreeParent parent = (TreeParent) object;
            ImageDescriptor descriptor = null;
            // PlatformUI.getWorkbench().getSharedImages().getImage( ISharedImages.IMG_OBJ_FOLDER );
            if ( parent.getName().startsWith( Messages.getString( MessagesConstant.MOBILE_TEXT ) ) ) {
                descriptor = ImageLoader.getInstance().getImageDescriptor( ImageConstant.MOBILE_IMAGE );
            }
            if ( parent.getName().startsWith( Messages.getString( MessagesConstant.SIMCARD_TEXT ) ) ) {
                descriptor = ImageLoader.getInstance().getImageDescriptor( ImageConstant.SIMCARD_IMAGE );
            }
            if ( parent.getName().startsWith( Messages.getString( MessagesConstant.LOCALMACHINE_TEXT ) ) ) {
                descriptor = ImageLoader.getInstance().getImageDescriptor( ImageConstant.LOCAL_MACHINE_IMAGE );
            }
            return descriptor;
        }

        public String getLabel(Object o) {
            TreeParent parent = (TreeParent) o;
            if ( parent.getCollection() != null ) {
                return parent.toString() + " [" + parent.getCollection().size() + "]";
            }
            return parent.toString();
            
        }

        public Object getParent(Object o) {
            TreeParent parent = (TreeParent) o;
            return parent.getParent();
        }
    };
    
    private IWorkbenchAdapter treeObjectAdapter = new IWorkbenchAdapter() {

        public Object[] getChildren(Object o) {
            return new Object[0];
        }

        public ImageDescriptor getImageDescriptor(Object object) {
            TreeObject to = (TreeObject) object;
            ImageDescriptor descriptor = null;
            
            // For the sms tree object.
            if ( to.getName().startsWith( Messages.getString( MessagesConstant.INBOX_TEXT ) ) ) {
                descriptor = ImageLoader.getInstance().getImageDescriptor( ImageConstant.SMS_INBOX_IMAGE );
            }
            if ( to.getName().startsWith( Messages.getString( MessagesConstant.OUTBOX_TEXT ) ) ) {
                descriptor = ImageLoader.getInstance().getImageDescriptor( ImageConstant.SMS_OUTBOX_IMAGE );
            }
            if ( to.getName().startsWith( Messages.getString( MessagesConstant.SMSBOX_TEXT ) ) ) {
                descriptor = ImageLoader.getInstance().getImageDescriptor( ImageConstant.SMS_SMSBOX_IMAGE );
            }
            if ( to.getName().startsWith( Messages.getString( MessagesConstant.DRAFT_TEXT ) ) ) {
                descriptor = ImageLoader.getInstance().getImageDescriptor( ImageConstant.SMS_DRAFT_IMAGE );
            }
            
            // For the phone book tree object.
            if ( to.getName().startsWith( Messages.getString( MessagesConstant.PBBOX_TEXT ) ) )
            {
                descriptor = ImageLoader.getInstance().getImageDescriptor( ImageConstant.PB_PBBOX_IMAGE );
            }
            if ( to.getName().startsWith( Messages.getString( MessagesConstant.DRAFT_TEXT ) ) )
            {
                descriptor = ImageLoader.getInstance().getImageDescriptor( ImageConstant.PB_DRAFT_IMAGE );
            }
            return descriptor;
        }

        public String getLabel(Object o) {
            TreeObject to = (TreeObject) o;
            if ( to.getCollection() != null ) {
                return to.toString() + " [" + to.getCollection().size() + "]";
            }
            return to.toString();            
        }

        public Object getParent(Object o) {
            TreeObject to = (TreeObject) o;
            return to.getParent();
        }
        
    };
    
    public Object getAdapter(Object adaptableObject, Class adapterType) {
        if (adapterType == IWorkbenchAdapter.class && adaptableObject instanceof TreeParent) {
            return treeParentAdapter;
        }
        if (adapterType == IWorkbenchAdapter.class && adaptableObject instanceof TreeObject) {
            return treeObjectAdapter;
        }
        return null;
    }

    public Class[] getAdapterList() {
		return new Class[] { IWorkbenchAdapter.class };
    }
}
