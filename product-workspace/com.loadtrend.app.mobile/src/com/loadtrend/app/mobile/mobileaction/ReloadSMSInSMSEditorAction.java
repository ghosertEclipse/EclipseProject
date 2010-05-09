package com.loadtrend.app.mobile.mobileaction;

import com.loadtrend.app.mobile.views.SMSExplorerView;

public class ReloadSMSInSMSEditorAction extends OpenSMSInSMSEditorAction
{
    
    /* (non-Javadoc)
     * @see com.loadtrend.app.mobile.mobileaction.MobileAction#performNonUI()
     */
    public void performNonUI()
    {
        super.retrieveMessage();
    }

    /* (non-Javadoc)
     * @see com.loadtrend.app.mobile.mobileaction.MobileAction#performUIAfterNonUI()
     */
    public void performUIAfterNonUI()
    {
        if ( super.hasError() ) return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.loadtrend.app.mobile.mobileaction.MobileAction#performUIBeforeNonUI()
     */
    public boolean performUIBeforeNonUI()
    {     
        invisibleRoot = ((SMSExplorerView) super.view).getInvisibleRoot();
        return true;
    }
}
