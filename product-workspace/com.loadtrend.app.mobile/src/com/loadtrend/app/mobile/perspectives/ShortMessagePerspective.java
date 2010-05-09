package com.loadtrend.app.mobile.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPlaceholderFolderLayout;

import com.loadtrend.app.mobile.views.SMSDetailsView;
import com.loadtrend.app.mobile.views.SMSExplorerView;
import com.loadtrend.app.mobile.views.SMSSearchView;
import com.loadtrend.app.mobile.views.SMSSendView;

public class ShortMessagePerspective implements IPerspectiveFactory
{
    public static final String PERSPECTIVE_ID = "com.loadtrend.app.mobile.perspectives.ShortMessagePerspective";
	/**
	 * Fill the layout with view into the perspective
	 */
	public void createInitialLayout( IPageLayout layout )
	{
		layout.setEditorAreaVisible( true );
		layout.addView( SMSExplorerView.VIEW_ID, IPageLayout.LEFT, 0.25f, IPageLayout.ID_EDITOR_AREA );
		
		IPlaceholderFolderLayout topFolder = layout.createPlaceholderFolder( "TOP_FOLDER", IPageLayout.TOP, 0.08f, IPageLayout.ID_EDITOR_AREA );
		topFolder.addPlaceholder( SMSSearchView.VIEW_ID );
		topFolder.addPlaceholder( SMSSendView.VIEW_ID );
	
		IFolderLayout bottomFolder = layout.createFolder( "BOTTOM_FOLDER", IPageLayout.BOTTOM, 0.65f, IPageLayout.ID_EDITOR_AREA );
		bottomFolder.addView( SMSDetailsView.VIEW_ID );
		
        layout.addPerspectiveShortcut( PERSPECTIVE_ID );
        
        layout.getViewLayout( SMSExplorerView.VIEW_ID ).setCloseable( false );
        layout.getViewLayout( SMSDetailsView.VIEW_ID ).setCloseable( false );
        
		// layout.addFastView( SMSExplorerView.VIEW_ID );
		// layout.setFixed( true );
		
	}
}
