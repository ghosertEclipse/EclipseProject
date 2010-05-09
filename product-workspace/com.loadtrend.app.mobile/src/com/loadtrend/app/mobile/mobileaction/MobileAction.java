package com.loadtrend.app.mobile.mobileaction;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import loadtrend.mobile.Message;
import loadtrend.mobile.Mobile;
import loadtrend.mobile.PhoneBook;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import com.loadtrend.app.mobile.action.WorkbenchWindowAction;
import com.loadtrend.app.mobile.data.Global;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.data.Preferences;
import com.loadtrend.app.mobile.data.PreferencesConstant;
import com.loadtrend.app.mobile.editors.AppEditorInput;
import com.loadtrend.app.mobile.editors.WaitForMobileProcessEditor;
import com.loadtrend.app.mobile.mobileActionListener.MobileActionStatusChangedListener;
import com.loadtrend.app.mobile.util.SpecialEntity;

public abstract class MobileAction extends WorkbenchWindowAction
{
	protected Mobile mobile = null;
	
	protected String errorTitle = null;
	
	protected String errorMessage = null;
	
	protected Exception exception = null;
    
    private static boolean isMobileAcitonsEnabled = true;
    
    protected boolean mobileProcessingPrompt = false;
    
    private static Set mobileActionStatusChangedListenerSet = new HashSet();
    
	private static IEditorInput waitForMobileProcessEditorInput = new AppEditorInput(
            Messages.getString( MessagesConstant.WAITFOR_MOBILEPROCESS_EDITORINPUT_NAME ),
            null,
            Messages.getString( MessagesConstant.WAITFOR_MOBILEPROCESS_EDITORINPUT_TOOLTIPTEXT )
            );
    
	protected MobileAction()
	{
	}

	protected MobileAction( String text )
	{
		super( text );
	}

	protected MobileAction( String text, ImageDescriptor image )
	{
		super( text, image );
	}

	protected MobileAction( String text, int style )
	{
		super( text, style );
	}

	public void run()
	{
        if (!isMobileAcitonsEnabled) {
            if (mobileProcessingPrompt) {
                MessageBox mb = new MessageBox( super.window.getShell(), SWT.OK | SWT.ICON_WARNING );
                mb.setText( Messages.getString( MessagesConstant.MAIN_WINDOW_TITLE) );
                mb.setMessage( Messages.getString( MessagesConstant.WARNING_MOBILE_PROCESSING) );
                mb.open();
            } else {
                openWaitForMobileProcessEditor();
            }
            return;
        }
        
		if ( !checkMobile() ) return;
		
		if ( !performUIBeforeNonUI() ) return;
        
		if ( !openWaitForMobileProcessEditor() ) return;
		
        isMobileAcitonsEnabled = false;
		
		Runnable runner = new Runnable()
		{
			public void run()
			{
				if ( checkConnection() )
				{
					performNonUI();
				}
				window.getShell().getDisplay().asyncExec( new Runnable()
				{
					public void run()
					{
						closeWaitForMobileProcessEditor();
						showErrorInNonUI();
						performUIAfterNonUI();
						clearError();
                        isMobileAcitonsEnabled = true;
					}
				});
			}
		};
		new Thread( runner ).start();
	}
	
	/**
	 * Please overwrite the method with the UI thread code.
	 * You can prepare the necessary parameter and action before invoking performNonUI method.
	 * If the condition is not matched to continue to performNonUI method, please return false, otherwise true.
	 */
	public abstract boolean performUIBeforeNonUI();
	
	/**
	 * Please overwrite the method with the only NON-UI thread code,
	 * do not put any UI code or controls in this method, otherwise, SWTException throws.
	 * You have a chance to save the exception and then show it in popup window later by invoking saveError()
	 * method in performNonUI method.
	 */
	public abstract void performNonUI();
	
	/**
	 * The chance to refesh UI with the results from NON-UI thread. 
	 * Also restore the UI changes because of invoking performUIBeforeNonUI if necessary.
	 * If you want to skip the method because of error happens when performing performNonUI, please invoking
	 * hasError method in performUIAfterNonUI to decide whether continue to perform as per the result from hasError
	 */
	public abstract void performUIAfterNonUI();

	private boolean checkMobile()
	{
	    this.mobile = Global.getOrCreateMobile();
		if ( this.mobile == null ) return false;
		return true;
	}

	/**
	 * Check whether the connection is valid or not. Every mobile action will
	 * check this method first, then if the connection is not valid, it will try
	 * to reconnect then perform their own action. If the reconnection action
	 * failed, their own action will be skipped.
	 * 
	 * @return true or false
	 */
	private boolean checkConnection()
	{
		if ( !this.mobile.isConnecting() )
		{
			String portName = Preferences.getValue( PreferencesConstant.PORT_NAME );

			if ( !portName.equals( "" ) )
			{
				try
				{
                    this.showRealTimeInfo( Messages.getString( MessagesConstant.OPEN_MOBILEPORT_ACTION_OPEN_INFO, portName ) );
					mobile.openMobilePort( portName );
                    this.notifyMobileActionStatusChangedListener();
					return true;
				}
				catch ( Exception e )
				{
				}
			}
			
			// Monitor the port if the portName is not specified in properties file,
			// or the specified portName is not the current one which is connecting.
			try
			{
                this.showRealTimeInfo( Messages.getString( MessagesConstant.OPEN_MOBILEPORT_ACTION_MONITOR_INFO ) );
				portName = mobile.monitorMobilePort();
				if ( portName == null )
				{
					throw new IllegalStateException(
							Messages.getString( MessagesConstant.MONITOR_MOBILEPORT_FAILURE_MESSAGEBOX_BODY ) );
				}
				// Store the portName is monitored to Preferences properties
				Preferences.setValue( PreferencesConstant.PORT_NAME, portName );
                
                this.showRealTimeInfo( Messages.getString( MessagesConstant.OPEN_MOBILEPORT_ACTION_OPEN_INFO, portName ) );
				mobile.openMobilePort( portName );
                this.notifyMobileActionStatusChangedListener();
				return true;
			}
			catch ( IllegalStateException ise )
			{
				saveError( Messages.getString( MessagesConstant.MONITOR_MOBILEPORT_FAILURE_MESSAGEBOX_TITLE ), null, ise );
				return false;
			}
			catch ( Exception me )
			{
				saveError(
						Messages.getString( MessagesConstant.OPEN_MOBILEPORT_FAILURE_MESSAGEBOX_TITLE ),
						Messages.getString( MessagesConstant.OPEN_MOBILEPORT_FAILURE_MESSAGEBOX_BODY ),
						me );
				return false;
			}
		}
		return true;
	}
	
	protected void showErrorInNonUI()
	{
		if ( this.exception != null )
		{
			if ( this.errorMessage == null )
			{
				this.errorMessage = exception.getMessage();
			}
			// MessageDialog.openError( window.getShell(), this.errorTitle, errorMessage );
			MessageBox box = new MessageBox(window.getShell(), SWT.OK | SWT.ICON_INFORMATION);
			box.setText(this.errorTitle);
			box.setMessage(this.errorMessage);
			box.open();
		}
	}
	
	private void clearError()
	{
		this.exception = null;
		this.errorTitle = null;
		this.errorMessage = null;
	}
	
	private void closeWaitForMobileProcessEditor()
	{
        IWorkbenchPage page = window.getActivePage();
        // if (page.isEditorAreaVisible()) {
            page.closeEditor( page.findEditor( waitForMobileProcessEditorInput ), false );
        // }
		return;
	}
	
	private boolean openWaitForMobileProcessEditor()
	{
        IWorkbenchPage page = window.getActivePage();
		IEditorPart editorPart = page.findEditor( waitForMobileProcessEditorInput );
		if ( editorPart == null )
		{
			try
			{
                if (page.isEditorAreaVisible()) {
				    editorPart = page.openEditor( waitForMobileProcessEditorInput, WaitForMobileProcessEditor.EDITOR_ID );
                }
			}
			catch ( PartInitException e )
			{
				MessageDialog.openError( window.getShell(),
						Messages.getString( MessagesConstant.OPEN_WAITFORMOBILEPROCESSEDITOR_FAILURE_MESSAGEBOX_TITLE ),
						e.getMessage() );
				return false;
			}
		}
		else
		{
			page.bringToTop( editorPart );
		}
		return true;
	}
	
    /**
     * Show the real time progress when application is communicating with mobile.
     * @param content
     */
    public void showRealTimeInfo( String content )
    {
        final String finalContent = content;
        super.window.getShell().getDisplay().asyncExec( new Runnable()
        {
            public void run()
            {
                IWorkbenchPage page = window.getActivePage();
                WaitForMobileProcessEditor editor = (WaitForMobileProcessEditor) page.findEditor(waitForMobileProcessEditorInput);
                if ( editor != null )
                {
                    editor.createBodyHtml( finalContent );
                }
            }
        });
    }
    
    /**
     *  Shorten the message or phonebook content.
     */
    protected String getShortenContent( Object object )
    {
        String content = null;
        if (object instanceof Message )
        {
            content = ( (Message) object ).getContent();
        }
        else if (object instanceof PhoneBook )
        {
            content = ( (PhoneBook) object ).getName();
        }
        else
        {
            return "";
        }
        
        content = content.length() < 15 ? content : content.substring( 0, 15 );
        content = SpecialEntity.encode( content );
        content = content.replaceAll( "<br></br>", " " );
        return content;
    }

	/**
	 * Save the errors in NonUI, the error will be shown later.
	 * @param errorTitle popup window title, null if none
	 * @param errorMessage popup window message body, null if none, but the message will be replace with the string from exception.getMessage()
	 * @param exception required
	 */
	public void saveError( String errorTitle, String errorMessage, Exception exception )
	{
		this.errorTitle = errorTitle;
		this.errorMessage = errorMessage;
		this.exception = exception;
	}
	
	/**
	 * Check whether the error occured when invoking performNonUI method, user can get this value returned
	 * to decide whether perform the performUIAfterNonUI method or not.
	 * @return
	 */
	public boolean hasError()
	{
		if ( this.exception == null ) return false;
		return true;
	}
    
    public void addMobileActionStatusChangedListener(MobileActionStatusChangedListener listener) {
        mobileActionStatusChangedListenerSet.add(listener);
    }
    
    public void notifyMobileActionStatusChangedListener() {
        Iterator it = mobileActionStatusChangedListenerSet.iterator();
        while (it.hasNext()) {
            MobileActionStatusChangedListener listener = (MobileActionStatusChangedListener) it.next();
            listener.changeStatus(this.mobile);
        }
    }
}
