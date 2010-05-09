package com.loadtrend.app.mobile.views;

import java.util.LinkedHashSet;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;

import com.loadtrend.app.mobile.action.GenerateAllSMSReportAction;
import com.loadtrend.app.mobile.data.Global;
import com.loadtrend.app.mobile.data.ImageConstant;
import com.loadtrend.app.mobile.data.ImageLoader;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.data.MobileAppConstant;
import com.loadtrend.app.mobile.editors.AppEditorInput;
import com.loadtrend.app.mobile.mobileaction.OpenSMSInSMSEditorAction2;
import com.loadtrend.app.mobile.util.IOOperation;
import com.loadtrend.app.trees.TreeObject;
import com.loadtrend.app.trees.TreeObjectSMSCollectionObserver;
import com.loadtrend.app.trees.TreeParent;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class SMSExplorerView extends ViewPart
{

	public static final String VIEW_ID = "com.loadtrend.app.mobile.views.SMSExplorerView";

	private TreeViewer treeViewer = null;
    
	private TreeParent invisibleRoot = null;
    
    private OpenSMSInSMSEditorAction2 openSMSInSMSEditorAction2 = null;

	/**
	 * This is a callback that will allow us to create the treeViewer and initialize
	 * it.
	 */
	public void createPartControl( Composite parent )
	{
        this.setPartName( Messages.getString( MessagesConstant.SMSExplorerView_PARTNAME ) );
        
		treeViewer = new TreeViewer( parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL );
        getSite().setSelectionProvider(treeViewer);
		treeViewer.setAutoExpandLevel( TreeViewer.ALL_LEVELS );
		treeViewer.setContentProvider( new MobileViewContentProvider() );
		treeViewer.setLabelProvider( new MobileViewLabelProvider() );
		treeViewer.setInput(initialize());
        
        openSMSInSMSEditorAction2 = new OpenSMSInSMSEditorAction2(getSite().getWorkbenchWindow(), this);
        
		hookContextMenu();
		hookDoubleClickAction();
        
        IToolBarManager toolBarManager = this.getViewSite().getActionBars().getToolBarManager();
        // Other plug-ins can contribute there actions here
        toolBarManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        toolBarManager.add(new GenerateAllSMSReportAction(this.getViewSite().getWorkbenchWindow()));
	}
	
	public TreeViewer getTreeViewer()
	{	
		return treeViewer;
	}
	
	/**
	 * @return Returns the invisibleRoot.
	 */
	public TreeParent getInvisibleRoot()
	{
		return invisibleRoot;
	}

	private void hookContextMenu()
	{
		// Create the context menu and register it with the Workbench.
		MenuManager menuMgr = new MenuManager( "#PopupMenu" );
		Menu menu = menuMgr.createContextMenu( treeViewer.getControl() );
		treeViewer.getControl().setMenu( menu );
        menuMgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        menuMgr.add(new GenerateAllSMSReportAction(super.getViewSite().getWorkbenchWindow()));
        // Design for apply popupMenus extenstion defined in plugin.xml
		getSite().registerContextMenu( menuMgr, treeViewer );
	}
    
	private void hookDoubleClickAction()
	{
		treeViewer.addDoubleClickListener( new IDoubleClickListener() {
			public void doubleClick( DoubleClickEvent event ) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				TreeObject to = (TreeObject) selection.getFirstElement();
				if ( to instanceof TreeParent ) {
					boolean expandedState = !treeViewer.getExpandedState( to );
					treeViewer.setExpandedState( to, expandedState );
				}
			}
		} );
		
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				TreeObject to = (TreeObject) selection.getFirstElement();
				if (! (to instanceof TreeParent)) {
                    openSMSInSMSEditorAction2.run(selection);
				}
			}
		});
	}

	/**
	 * Passing the focus request to the treeViewer's control.
	 */
	public void setFocus()
	{
		treeViewer.getControl().setFocus();
	}
    
    /*
     * We will set up a dummy model to initialize tree heararchy. In a real
     * code, you will connect to a real model and expose its hierarchy.
     */
    private TreeParent initialize()
    {
        ImageDescriptor descriptor = null;
        TreeObjectSMSCollectionObserver observer = new TreeObjectSMSCollectionObserver(SMSExplorerView.this);
        
        // initial mobile, inbox, outbox
        TreeObject to1 = new TreeObject( Messages.getString( MessagesConstant.INBOX_TEXT ) );
        TreeObject to2 = new TreeObject( Messages.getString( MessagesConstant.OUTBOX_TEXT ) );
        TreeParent p1 = new TreeParent( Messages.getString( MessagesConstant.MOBILE_TEXT ) );
        to1.addObserver( observer );
        to2.addObserver( observer );
        p1.addChild( to1 );
        p1.addChild( to2 );
        descriptor = ImageLoader.getInstance().getImageDescriptor( ImageConstant.MOBILE_IMAGE );
        setToEditorInput( to1, descriptor );
        setToEditorInput( to2, descriptor );

        // initial simcard, inbox, outbox
        TreeObject to3 = new TreeObject( Messages.getString( MessagesConstant.INBOX_TEXT ) );
        TreeObject to4 = new TreeObject( Messages.getString( MessagesConstant.OUTBOX_TEXT ) );
        TreeParent p2 = new TreeParent( Messages.getString( MessagesConstant.SIMCARD_TEXT ) );
        to3.addObserver( observer );
        to4.addObserver( observer );
        p2.addChild( to3 );
        p2.addChild( to4 );
        descriptor = ImageLoader.getInstance().getImageDescriptor( ImageConstant.SIMCARD_IMAGE );
        setToEditorInput( to3, descriptor );
        setToEditorInput( to4, descriptor );
        
        // initial local machine, smsbox, draft
        TreeObject to5 = new TreeObject( Messages.getString( MessagesConstant.SMSBOX_TEXT ) );
        TreeObject to6 = new TreeObject( Messages.getString( MessagesConstant.DRAFT_TEXT ) );
        TreeParent p3 = new TreeParent( Messages.getString( MessagesConstant.LOCALMACHINE_TEXT ) );
        
        // Load the sms data from local machine
        Object object = IOOperation.readObject( MobileAppConstant.JAVA_ROOT_PATH + MobileAppConstant.SMSBOX_DATA );
        if ( object != null )
        {
            to5.setCollection( (LinkedHashSet) object );
        }
        else
        {
            to5.setCollection( new LinkedHashSet() );
        }
        object = IOOperation.readObject( MobileAppConstant.JAVA_ROOT_PATH + MobileAppConstant.SMS_DRAFT_DATA );
        if ( object != null )
        {
            to6.setCollection( (LinkedHashSet) object );
        }
        else
        {
            to6.setCollection( new LinkedHashSet() );
        }
        
        to5.addObserver( observer );
        to6.addObserver( observer );
        p3.addChild( to5 );
        p3.addChild( to6 );
        descriptor = ImageLoader.getInstance().getImageDescriptor( ImageConstant.LOCAL_MACHINE_IMAGE );
        setToEditorInput( to5, descriptor );
        setToEditorInput( to6, descriptor );
        
        // Load mobile, sim card, local machine
        invisibleRoot = new TreeParent( "" );
        invisibleRoot.addChild( p1 );
        invisibleRoot.addChild( p2 );
        invisibleRoot.addChild( p3 );
        
        Global.setSMSInvisibleRoot( invisibleRoot );
        
        return invisibleRoot;
    }
    
    private void setToEditorInput( TreeObject to, ImageDescriptor descriptor )
    {
        IEditorInput editorInput = new AppEditorInput( to.getTreeName(), 
                                                       descriptor, 
                                                       Messages.getString( MessagesConstant.SMSEDITORINPUT_TOOLTIPTEXT, to.getTreeName() ) );
        to.setEditorInput( editorInput );
    }
}