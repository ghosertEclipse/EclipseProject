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

import com.loadtrend.app.mobile.action.GenerateAllPBReportAction;
import com.loadtrend.app.mobile.data.Global;
import com.loadtrend.app.mobile.data.ImageConstant;
import com.loadtrend.app.mobile.data.ImageLoader;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.data.MobileAppConstant;
import com.loadtrend.app.mobile.editors.AppEditorInput;
import com.loadtrend.app.mobile.mobileaction.OpenPBInPBEditorAction2;
import com.loadtrend.app.mobile.util.IOOperation;
import com.loadtrend.app.trees.TreeObject;
import com.loadtrend.app.trees.TreeObjectPBCollectionObserver;
import com.loadtrend.app.trees.TreeParent;

public class PBExplorerView extends ViewPart
{

    public static final String VIEW_ID = "com.loadtrend.app.mobile.views.PBExplorerView";

    private TreeViewer treeViewer = null;
    
    private TreeParent invisibleRoot = null;
    
    private OpenPBInPBEditorAction2 openPBInPBEditorAction2 = null;
    
    /**
     * This is a callback that will allow us to create the treeViewer and initialize
     * it.
     */
    public void createPartControl( Composite parent )
    {
        this.setPartName( Messages.getString( MessagesConstant.PBExplorerView_PARTNAME ) );
        
        treeViewer = new TreeViewer( parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL );
        getSite().setSelectionProvider(treeViewer);
        treeViewer.setAutoExpandLevel( TreeViewer.ALL_LEVELS );
        treeViewer.setContentProvider( new MobileViewContentProvider() );
        treeViewer.setLabelProvider( new MobileViewLabelProvider() );
        treeViewer.setInput(this.initialize());
        
        openPBInPBEditorAction2 = new OpenPBInPBEditorAction2(getSite().getWorkbenchWindow(), this);
        
        hookContextMenu();
        hookDoubleClickAction();
        
        IToolBarManager toolBarManager = this.getViewSite().getActionBars().getToolBarManager();
        // Other plug-ins can contribute there actions here
        toolBarManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        toolBarManager.add(new GenerateAllPBReportAction(this.getViewSite().getWorkbenchWindow()));
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
        menuMgr.add(new GenerateAllPBReportAction(super.getViewSite().getWorkbenchWindow()));
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
					openPBInPBEditorAction2.run(selection);
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
        TreeObjectPBCollectionObserver observer = new TreeObjectPBCollectionObserver(PBExplorerView.this);
        
        // initial mobile
        TreeObject to1 = new TreeObject( Messages.getString( MessagesConstant.PBBOX_TEXT ) );
        TreeParent p1 = new TreeParent( Messages.getString( MessagesConstant.MOBILE_TEXT ) );
        to1.addObserver( observer );
        p1.addChild( to1 );
        descriptor = ImageLoader.getInstance().getImageDescriptor( ImageConstant.MOBILE_IMAGE );
        setToEditorInput( to1, descriptor );

        // initial simcard
        TreeObject to2 = new TreeObject( Messages.getString( MessagesConstant.PBBOX_TEXT ) );
        TreeParent p2 = new TreeParent( Messages.getString( MessagesConstant.SIMCARD_TEXT ) );
        to2.addObserver( observer );
        p2.addChild( to2 );
        descriptor = ImageLoader.getInstance().getImageDescriptor( ImageConstant.SIMCARD_IMAGE );
        setToEditorInput( to2, descriptor );
        
        // initial local machine
        TreeObject to3 = new TreeObject( Messages.getString( MessagesConstant.PBBOX_TEXT ) );
        TreeObject to4 = new TreeObject( Messages.getString( MessagesConstant.DRAFT_TEXT ) );
        TreeParent p3 = new TreeParent( Messages.getString( MessagesConstant.LOCALMACHINE_TEXT ) );
        
        // Load the phone book data from local machine
        Object object = IOOperation.readObject( MobileAppConstant.JAVA_ROOT_PATH + MobileAppConstant.PBBOX_DATA );
        if ( object != null )
        {
            to3.setCollection( (LinkedHashSet) object );
        }
        else
        {
            to3.setCollection( new LinkedHashSet() );
        }
        object = IOOperation.readObject( MobileAppConstant.JAVA_ROOT_PATH + MobileAppConstant.PB_DRAFT_DATA );
        if ( object != null )
        {
            to4.setCollection( (LinkedHashSet) object );
        }
        else
        {
            to4.setCollection( new LinkedHashSet() );
        }
        
        to3.addObserver( observer );
        to4.addObserver( observer );
        p3.addChild( to3 );
        p3.addChild( to4 );
        descriptor = ImageLoader.getInstance().getImageDescriptor( ImageConstant.LOCAL_MACHINE_IMAGE );
        setToEditorInput( to3, descriptor );
        setToEditorInput( to4, descriptor );
        
        // Load mobile, sim card, local machine
        invisibleRoot = new TreeParent( "" );
        invisibleRoot.addChild( p1 );
        invisibleRoot.addChild( p2 );
        invisibleRoot.addChild( p3 );
        
        Global.setPBInvisibleRoot( invisibleRoot );
        
        return invisibleRoot;
    }
    
    private void setToEditorInput( TreeObject to, ImageDescriptor descriptor )
    {
        IEditorInput editorInput = new AppEditorInput( to.getTreeName(), 
                                                       descriptor, 
                                                       Messages.getString( MessagesConstant.PBEDITORINPUT_TOOLTIPTEXT, to.getTreeName() ) );
        to.setEditorInput( editorInput );
    }
}
