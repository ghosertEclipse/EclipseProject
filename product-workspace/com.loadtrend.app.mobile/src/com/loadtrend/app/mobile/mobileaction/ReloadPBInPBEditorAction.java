package com.loadtrend.app.mobile.mobileaction;

import com.loadtrend.app.mobile.views.PBExplorerView;

public class ReloadPBInPBEditorAction extends OpenPBInPBEditorAction
{   
    
    /* (non-Javadoc)
     * @see com.loadtrend.app.mobile.mobileaction.MobileAction#performNonUI()
     */
    public void performNonUI()
    {
        super.retrievePhoneBook();
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
        invisibleRoot = ((PBExplorerView) super.view).getInvisibleRoot();
        return true;
    }
}
