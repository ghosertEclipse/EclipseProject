package com.loadtrend.app.mobile.views.media;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IWorkbenchPage;

import com.loadtrend.app.mobile.MobilePlugin;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.editors.AppEditorInput;
import com.loadtrend.app.mobile.editors.MusicEditor;
import com.loadtrend.app.mobile.netaction.RefreshMusicRootProductAction;
import com.loadtrend.app.mobile.perspectives.MusicPerspective;

public class MusicExplorerView extends ResourceExplorerView {

    public static final String VIEW_ID = "com.loadtrend.app.mobile.views.media.MusicExplorerView";
    
    private IPerspectiveListener perspectiveListener = new IPerspectiveListener() {
        public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
            if (perspective.getId().equals(MusicPerspective.PERSPECTIVE_ID)) {
                new RefreshMusicRootProductAction(MusicExplorerView.this).run();
            }
        }
        public void perspectiveChanged(IWorkbenchPage page, IPerspectiveDescriptor perspective, String changeId) {
        }
    };
    
    public MusicExplorerView() {
        super.EDITOR_ID = MusicEditor.EDITOR_ID;
        super.resourceEditorInput = new AppEditorInput(
                                        Messages.getString( MessagesConstant.MusicEditor_NAME ),
                                        null,
                                        Messages.getString( MessagesConstant.MusicEditor_TOOLTIPTEXT )
                                        );
        super.viewImage = MobilePlugin.getImageDescriptor("icons/ringtone.gif").createImage();
    }
    
    public void createPartControl(Composite parent) {
        super.setPartName( Messages.getString( MessagesConstant.MusicExplorerView_PARTNAME ) );
        super.createPartControl(parent);
        super.getSite().getWorkbenchWindow().addPerspectiveListener(perspectiveListener);
    }
}
