package com.loadtrend.app.mobile.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.loadtrend.app.mobile.views.media.MusicExplorerView;

public class MusicPerspective implements IPerspectiveFactory {

    public static final String PERSPECTIVE_ID = "com.loadtrend.app.mobile.perspectives.MusicPerspective";
    
    public void createInitialLayout( IPageLayout layout )
    {
        layout.setEditorAreaVisible( true );
        layout.addView( MusicExplorerView.VIEW_ID, IPageLayout.LEFT, 0.25f, IPageLayout.ID_EDITOR_AREA );
        
        layout.addPerspectiveShortcut( PERSPECTIVE_ID );
        layout.getViewLayout( MusicExplorerView.VIEW_ID ).setCloseable( false );
    }
}
