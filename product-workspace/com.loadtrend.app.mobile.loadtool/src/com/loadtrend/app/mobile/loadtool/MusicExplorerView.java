package com.loadtrend.app.mobile.loadtool;

public class MusicExplorerView extends ResourceExplorerView {

    public static final String ID = "com.loadtrend.app.mobile.loadtool.MusicExplorerView";
    
    public MusicExplorerView() {
        super.EDITOR_ID = MusicEditor.EDITOR_ID;
        super.resourceEditorInput = new AppEditorInput("music", null, "music");
        super.viewImage = LoadtoolPlugin.getImageDescriptor("icons/ringtone.gif").createImage();
    }
}
