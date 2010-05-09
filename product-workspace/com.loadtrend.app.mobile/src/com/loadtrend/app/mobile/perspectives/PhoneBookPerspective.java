package com.loadtrend.app.mobile.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPlaceholderFolderLayout;

import com.loadtrend.app.mobile.views.PBExplorerView;
import com.loadtrend.app.mobile.views.PBSearchView;

public class PhoneBookPerspective implements IPerspectiveFactory
{
    public static final String PERSPECTIVE_ID = "com.loadtrend.app.mobile.perspectives.PhoneBookPerspective";
    
    public void createInitialLayout( IPageLayout layout )
    {
        layout.setEditorAreaVisible( true );
        layout.addView( PBExplorerView.VIEW_ID, IPageLayout.LEFT, 0.25f, IPageLayout.ID_EDITOR_AREA );
        
        IPlaceholderFolderLayout topFolder = layout.createPlaceholderFolder( "TOP_FOLDER", IPageLayout.TOP, 0.08f, IPageLayout.ID_EDITOR_AREA );
        topFolder.addPlaceholder( PBSearchView.VIEW_ID );

        layout.addPerspectiveShortcut( PERSPECTIVE_ID );
        
        layout.getViewLayout( PBExplorerView.VIEW_ID ).setCloseable( false );
        
        // layout.addFastView( PBExplorerView.VIEW_ID );
        // layout.setFixed( true );
    }

}
