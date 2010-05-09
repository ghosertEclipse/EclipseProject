package com.loadtrend.app.mobile.mobileaction;

import loadtrend.mobile.Mobile;

import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;

import com.loadtrend.app.mobile.data.ImageConstant;
import com.loadtrend.app.mobile.data.ImageLoader;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.mobileActionListener.MobileActionStatusChangedListener;

public class OpenMobilePortAction extends MobileAction implements MobileActionStatusChangedListener
{	
	/* (non-Javadoc)
     * @see com.loadtrend.app.mobile.mobileActionListener.MobileActionStatusChangedListener#changeStatus()
     * Enable or disable the CLOSE_MOBILEPORT_ACTION by mobile.isConnecting.
     */
    public void changeStatus(Mobile mobile) {
        super.setEnabled(!mobile.isConnecting());
    }

    public OpenMobilePortAction(IWorkbenchWindow window)
	{
		super( Messages.getString( MessagesConstant.OPEN_MOBILEPORT_ACTION_TEXT ), AS_PUSH_BUTTON );
		setToolTipText( Messages.getString( MessagesConstant.OPEN_MOBILEPORT_ACTION_TOOLTIPTEXT ) );
		setAccelerator( SWT.CTRL | 'C' );
		ImageLoader imageLoader = ImageLoader.getInstance();
		setImageDescriptor( imageLoader.getImageDescriptor( ImageConstant.OPEN_MOBILEPORT_ACTION_IMAGE ) );
		setDisabledImageDescriptor( imageLoader.getImageDescriptor( ImageConstant.OPEN_MOBILEPORT_ACTION_DIMAGE ) );
        super.addMobileActionStatusChangedListener(this);
        super.window = window;
	}

	/* (non-Javadoc)
	 * @see com.loadtrend.app.mobile.mobileaction.MobileAction#performNonUI()
	 */
	public void performNonUI()
	{
		// super.checkConnection() will invoke the actual code to open the mobile port.
	}

	/* (non-Javadoc)
	 * @see com.loadtrend.app.mobile.mobileaction.MobileAction#performUIAfterNonUI()
	 */
	public void performUIAfterNonUI()
	{
	}

	/* (non-Javadoc)
	 * @see com.loadtrend.app.mobile.mobileaction.MobileAction#performUIBeforeNonUI()
	 */
	public boolean performUIBeforeNonUI()
	{
		return true;
	}
	
	
}