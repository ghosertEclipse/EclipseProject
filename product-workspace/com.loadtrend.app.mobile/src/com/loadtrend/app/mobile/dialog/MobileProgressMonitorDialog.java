package com.loadtrend.app.mobile.dialog;

import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;

public abstract class MobileProgressMonitorDialog extends ProgressMonitorDialog
{
    private Rectangle parentBounds = null;
    
    public MobileProgressMonitorDialog( Shell parentShell )
    {
        super( parentShell );
        parentBounds = parentShell.getBounds();
    }
    
    public void configureShell( Shell shell )
    {   
        // Computer location to this dialog.
        super.configureShell( shell );
        
        int[] widthHeight = configureShellWidthHeight();
        
        computeShellWidthHeight( shell, widthHeight );
        
        configShellTextImage( shell );
    }
    
    protected abstract int[] configureShellWidthHeight();
    
    protected abstract void configShellTextImage( Shell shell );
    
    
    private void computeShellWidthHeight( Shell shell, int[] widthHeight )
    {
        int width = widthHeight[0];
        int height = widthHeight[1];
        
        int centerX = parentBounds.x + parentBounds.width / 2;
        int centerY = parentBounds.y + parentBounds.height / 2;
        
        int locactionX = centerX - width / 2;
        int locactionY = centerY - height /2;
        
        shell.setLocation( locactionX, locactionY );
        shell.setSize( width, height );
    }
    
    protected void createCancelButton(Composite parent)
    {
        super.createCancelButton( parent );
        cancel.setText( Messages.getString( MessagesConstant.MOBILE_DIALOG_CANCELBUTTON_TEXT ) );
    }
}
