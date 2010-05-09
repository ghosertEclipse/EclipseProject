package com.loadtrend.app.mobile.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.loadtrend.app.mobile.views.media.PictureExplorerView;

public class PicturePerspective implements IPerspectiveFactory {

    public static final String PERSPECTIVE_ID = "com.loadtrend.app.mobile.perspectives.PicturePerspective";
    
    public void createInitialLayout( IPageLayout layout )
    {
        layout.setEditorAreaVisible( true );
        layout.addView( PictureExplorerView.VIEW_ID, IPageLayout.LEFT, 0.25f, IPageLayout.ID_EDITOR_AREA );
        
        layout.addPerspectiveShortcut( PERSPECTIVE_ID );
        layout.getViewLayout( PictureExplorerView.VIEW_ID ).setCloseable( false );
    }
}
