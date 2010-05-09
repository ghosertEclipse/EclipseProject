package com.loadtrend.app.mobile.loadtool;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.setFixed(true);
		
        IFolderLayout folder = layout.createFolder("LEFT_FOLDER", IPageLayout.LEFT, 0.2f, editorArea);
        folder.addView(PictureExplorerView.ID);
        folder.addView(MusicExplorerView.ID);
	}

}
