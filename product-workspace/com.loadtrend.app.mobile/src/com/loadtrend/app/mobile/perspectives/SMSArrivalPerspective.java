package com.loadtrend.app.mobile.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.loadtrend.app.mobile.views.smsArrival.SMSArrivalView;
import com.loadtrend.app.mobile.views.smsArrival.SMSArrivalViewLog;

public class SMSArrivalPerspective implements IPerspectiveFactory {
    public static final String PERSPECTIVE_ID = "com.loadtrend.app.mobile.perspectives.SMSArrivalPerspective";
    
    public void createInitialLayout( IPageLayout layout )
    {
        layout.setEditorAreaVisible(false);
        layout.addView( SMSArrivalView.VIEW_ID, IPageLayout.TOP, 0.6f, IPageLayout.ID_EDITOR_AREA );
        layout.addView( SMSArrivalViewLog.VIEW_ID, IPageLayout.BOTTOM, 0.4f, IPageLayout.ID_EDITOR_AREA );
        layout.addPerspectiveShortcut( PERSPECTIVE_ID );
        layout.getViewLayout( SMSArrivalView.VIEW_ID ).setCloseable( false );
        layout.getViewLayout( SMSArrivalViewLog.VIEW_ID ).setCloseable( false );
    }
}
