package com.loadtrend.app.mobile.loadtool;

public class PictureExplorerView extends ResourceExplorerView {
	public static final String ID = "com.loadtrend.app.mobile.loadtool.PictureExplorerView";
    
    public PictureExplorerView() {
        super.EDITOR_ID = PictureEditor.EDITOR_ID;
        super.resourceEditorInput = new AppEditorInput("picture", null, "picture");
        super.viewImage = LoadtoolPlugin.getImageDescriptor("icons/mobile_pic.ico").createImage();
    }
}