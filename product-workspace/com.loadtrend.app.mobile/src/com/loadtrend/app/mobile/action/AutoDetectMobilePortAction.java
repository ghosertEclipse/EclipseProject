package com.loadtrend.app.mobile.action;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;

import loadtrend.mobile.Port;
import loadtrend.mobile.PortException;
import loadtrend.mobile.SerialPort;
import loadtrend.mobile.util.BlockOperation;
import loadtrend.mobile.util.BlockOperationUtil;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.data.Preferences;
import com.loadtrend.app.mobile.data.PreferencesConstant;
import com.loadtrend.app.mobile.dialog.MobileProgressMonitorDialog;

public class AutoDetectMobilePortAction extends Action
{
	private static final String AT_COMMAND_OK = "\r\nOK\r\n";

	private static final String AT_COMMAND_ERROR = "\r\nERROR\r\n";

	private String portName = null;
	
	private String mobilePortName = null;
	
	private int resultCode = 0;
    
    private Shell shell = null;
	
	public AutoDetectMobilePortAction(Shell shell) {
        this.shell = shell;
	}

	public void run()
	{
		final Port port = SerialPort.getInstance();
		final ArrayList list = port.monitorPort();
		final Iterator it = list.iterator();

		// Create Progress Dialog
        ProgressMonitorDialog monitorDialog = new MobileProgressMonitorDialog(this.shell)
        {
            protected void configShellTextImage( Shell shell )
            {
                shell.setText( Messages.getString( MessagesConstant.SEARCH_MOBILEPORT_MESSAGEBOX_TITLE ) );
            }
            
            protected int[] configureShellWidthHeight()
            {
                int[] widthHeight = new int[] { 300, 180 };
                return widthHeight;
            }
        };
        
		monitorDialog.create();
		final Shell shell = monitorDialog.getShell();
		
		final MessageBox findMessageBox = new MessageBox( shell, SWT.YES | SWT.NO | SWT.ICON_QUESTION );
		findMessageBox.setText( Messages.getString( MessagesConstant.FIND_MOBILEPORT_MESSAGEBOX_TITLE ) );
		final MessageBox finishMessageBox = new MessageBox( shell, SWT.OK | SWT.ICON_INFORMATION );
		finishMessageBox.setText( Messages.getString( MessagesConstant.FINISH_MOBILEPORT_MESSAGEBOX_TITLE ) );
		
		try
		{
			IRunnableWithProgress runnableWithProgress = new IRunnableWithProgress()
			{
				public void run( IProgressMonitor monitor ) throws InvocationTargetException, InterruptedException
				{
					monitor.beginTask( Messages.getString( MessagesConstant.SEARCH_MOBILEPORT_MESSAGEBOX_BEGINTASK ), list.size() );
					while ( it.hasNext() )
					{
						if ( monitor.isCanceled() ) {
							throw new InterruptedException();
						}
						
						portName = (String) it.next();
						monitor.worked( 1 );
						monitor.subTask( Messages.getString( MessagesConstant.SEARCH_MOBILEPORT_MESSAGEBOX_SUBTASK, portName ) );

						try
						{
				    		// open port blocking handler
				    		boolean isSuccessOperation = BlockOperationUtil.start(new BlockOperation() {
								public void run() throws PortException {
					                port.openPort( portName );
								}
				    		}, 5000);
				    		if (!isSuccessOperation) continue;
				    		// write port blocking handler
				    		isSuccessOperation = BlockOperationUtil.start(new BlockOperation() {
								public void run() throws PortException {
									port.writePort("AT+CMGC=?\r");
								}
				    		}, 5000);
							if (!isSuccessOperation) {
								port.closePort();
								continue;
							}
							
							StringBuffer portValues = port.readPort( new String[] { AT_COMMAND_OK, AT_COMMAND_ERROR },
									1000 );
							port.closePort();
							if ( portValues.lastIndexOf( AT_COMMAND_OK ) == -1 ) {
								continue;
							}
							mobilePortName = portName;
							shell.getDisplay().syncExec( new Runnable()
							{
								public void run()
								{
									findMessageBox.setMessage( Messages.getString( MessagesConstant.FIND_MOBILEPORT_MESSAGEBOX_BODY, mobilePortName ) );
									resultCode = findMessageBox.open();
								}
							});
							if ( resultCode == SWT.NO ) break;
						}
						catch ( Exception e ) {
						}
					}
					// finish the long time performing
					monitor.done();
					shell.getDisplay().syncExec( new Runnable()
							{
								public void run()
								{
									String message = null;
									if ( mobilePortName != null )
									{
										message = Messages.getString( MessagesConstant.FINISH_MOBILEPORT_MESSAGEBOX_BODY_FOUND, mobilePortName );
										Preferences.setValue( PreferencesConstant.PORT_NAME, mobilePortName );
									}
									else
									{
										message = Messages.getString( MessagesConstant.MONITOR_MOBILEPORT_FAILURE_MESSAGEBOX_BODY );
										Preferences.setValue( PreferencesConstant.PORT_NAME, "" );
									}
									finishMessageBox.setMessage( message );
									finishMessageBox.open();
								}
							});
				}
			};
			monitorDialog.run( true, true, runnableWithProgress );
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}
}
