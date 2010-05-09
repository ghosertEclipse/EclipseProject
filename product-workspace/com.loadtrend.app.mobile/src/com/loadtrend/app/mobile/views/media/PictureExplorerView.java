package com.loadtrend.app.mobile.views.media;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IWorkbenchPage;

import com.loadtrend.app.mobile.MobilePlugin;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.editors.AppEditorInput;
import com.loadtrend.app.mobile.editors.PictureEditor;
import com.loadtrend.app.mobile.netaction.RefreshPictureRootAction;
import com.loadtrend.app.mobile.perspectives.PicturePerspective;

public class PictureExplorerView extends ResourceExplorerView {
	public static final String VIEW_ID = "com.loadtrend.app.mobile.views.media.PictureExplorerView";
    
    private IPerspectiveListener perspectiveListener = new IPerspectiveListener() {
        public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
            if (perspective.getId().equals(PicturePerspective.PERSPECTIVE_ID)) {
                new RefreshPictureRootAction(PictureExplorerView.this).run();
            }
        }
        public void perspectiveChanged(IWorkbenchPage page, IPerspectiveDescriptor perspective, String changeId) {
        }
    };
    
    public PictureExplorerView() {
        super.EDITOR_ID = PictureEditor.EDITOR_ID;
        super.resourceEditorInput = new AppEditorInput(
						                Messages.getString( MessagesConstant.PictureEditor_NAME ),
						                null,
						                Messages.getString( MessagesConstant.PictureEditor_TOOLTIPTEXT )
						                );
        super.viewImage = MobilePlugin.getImageDescriptor("icons/mobile_pic.ico").createImage();
    }

    public void createPartControl(Composite parent) {
        super.setPartName( Messages.getString( MessagesConstant.PictureExplorerView_PARTNAME ) );
        super.createPartControl(parent);
        super.getSite().getWorkbenchWindow().addPerspectiveListener(perspectiveListener);
    }
    
}