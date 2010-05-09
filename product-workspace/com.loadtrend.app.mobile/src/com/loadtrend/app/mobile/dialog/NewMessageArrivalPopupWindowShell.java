/*
 * Created on 2005-7-18
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.loadtrend.app.mobile.dialog;

import java.text.MessageFormat;
import java.util.Timer;
import java.util.TimerTask;

import loadtrend.mobile.Message;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;

import com.loadtrend.api.win32.PlaySound;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.data.MobileAppConstant;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NewMessageArrivalPopupWindowShell
{
    private int iheight = 0;
    
    private int iY = 35;
    
    private int step = 10;
    
    private int delayTime = 0;
    
    private static final int POPUP_WINDOW_DELAY_TIME = 10000;
    
    private Shell parentShell = null;
    
    private Display display = null;
    
    private Message message = null;
    
    private Shell shell = null;
    
    private static final String smsContentFormat = "<A HREF=\"\">{0}</A>";
    
    private static final String WAV_FILE = MobileAppConstant.NEW_MESSAGE_ARRIVAL_WAV;
    
    public NewMessageArrivalPopupWindowShell(Shell parentShell, Message message) {
        this.parentShell = parentShell;
        this.message = message;
        this.display = parentShell.getDisplay();
    }
    
    public void create() {
        // for jmobile invoke
        parentShell.getDisplay().asyncExec(new Runnable() {
            public void run() {
                NewMessageArrivalPopupWindowShell.this.build();
            }
        });
        // for main() invoke;
        // this.build();
    }
    
    public void build() {
        shell = new Shell(parentShell, SWT.TOOL|SWT.CLOSE );
        
        final Rectangle bounds = this.display.getBounds();
        shell.setLocation(bounds.width - 220, bounds.height - iY);
        shell.setSize(200, iheight);
        final Timer upDownWindowTimer = new Timer(true);
        upDownWindowTimer.schedule(new TimerTask(){
            public void run() {
                display.syncExec(new Runnable() {
                    public void run() {
                        if (step < 0 && delayTime > 0) {
                            delayTime = delayTime - 30;
                            return;
                        }
                        shell.setLocation(bounds.width - 220, bounds.height - iY);
                        shell.setSize(200, iheight);
                        iheight = iheight + step;
                        iY = iY + step;
                        if (iheight == 150 && step > 0) {
                            step = -step;
                            delayTime = POPUP_WINDOW_DELAY_TIME;
                        }
                        if (iheight == 0 && step < 0) {
                            step = -step;
                            delayTime = 0;
                            upDownWindowTimer.cancel();
                            shell.close();
                        }
                    }
                });
            }
        }, 0, 30);
        
        shell.addShellListener(new ShellAdapter() {
            public void shellClosed(ShellEvent e) {
                upDownWindowTimer.cancel();
            }
        });
        
        // For the jmobile invoke.
        shell.setText(Messages.getString(MessagesConstant.NEW_SMS_TITLE));
        // For the main() invoke.
        // shell.setText("NEW SMS");
        
        setBackGroundWhite(shell);
        
        FormLayout layout = new FormLayout();
        layout.marginTop = 5;
        layout.marginLeft = 5;
        shell.setLayout(layout);
        
        // The first row: product name label
        FormData formData = new FormData();
        formData.left = new FormAttachment( 0, 0 );
        formData.right = new FormAttachment( 0, 200 );
        formData.top = new FormAttachment( 0, 0 );
        Label lbSender = new Label( shell, SWT.LEFT );
        lbSender.setLayoutData( formData );
        // For the jmobile invoke.
        lbSender.setText(Messages.getString(MessagesConstant.NEW_SMS_FROM, message.getTeleNum()));
        // For the main() invoke.
        // lbSender.setText(message.getTeleNum());
        setBackGroundWhite(lbSender);
        
        // The first row: product name label
        formData = new FormData();
        formData.left = new FormAttachment( 0, 0 );
        formData.right = new FormAttachment( 0, 200 );
        formData.top = new FormAttachment( lbSender, 10 );
        Link lkContent = new Link( shell, SWT.NONE);
        lkContent.setLayoutData( formData );
        lkContent.setText(MessageFormat.format(smsContentFormat, new Object[]{message.getContent()}));
        lkContent.addSelectionListener( new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                shell.close();
                NewSMSDialog dialog = new NewSMSDialog(NewMessageArrivalPopupWindowShell.this.parentShell);
                dialog.create();
                // set value to control in dialog
                dialog.getTxtReceiver().setText(message.getTeleNum());
                dialog.getTxtContent().setText(message.getContent());
                dialog.getTxtContent().setFocus();
                dialog.open();
            }
        });
        setBackGroundWhite(lkContent);
        
        shell.setFocus();
        
        shell.open();

        shell.forceActive();
        
        PlaySound.execute(WAV_FILE); 
    }
    
    public boolean isDisposed() {
        return shell.isDisposed();
    }
    
    private void setBackGroundWhite( Control control )
    {
        control.setBackground( this.display.getSystemColor( SWT.COLOR_WHITE ) );
    }
    
    public static void main(String[] args) {
        Message message = new Message();
        message.setTeleNum("13916939847");
        message.setContent("圣诞节快乐吗？我很快乐的啊，即使没有你也一样，哇哈哈哈哈哈哈哈圣诞节快乐吗？。");
        NewMessageArrivalPopupWindowShell shell = new NewMessageArrivalPopupWindowShell(new Shell(), message);
        shell.create();
        while ( !shell.isDisposed() )
        {
            if ( !Display.getDefault().readAndDispatch() )
            {
                Display.getDefault().sleep();
            }
        }
    }
}
