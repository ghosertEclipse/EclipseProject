package com.loadtrend.app.mobile.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;

import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.util.UpdateJobChangeListener;

public class UpdateAction extends Action {
	
    private UpdateJobChangeListener jobListener = null;
    
    public UpdateAction(IWorkbenchWindow window) {
        super( Messages.getString( MessagesConstant.UPDATE_ACTION_TEXT ), AS_PUSH_BUTTON );
        // super.setAccelerator( SWT.CTRL | 'T' );
		setText("检查更新...");
		setToolTipText("搜索可升级组件");
		this.jobListener = UpdateJobChangeListener.getInstance(window);
    }
    
    public void run() {
		jobListener.start(false);
    }
}
