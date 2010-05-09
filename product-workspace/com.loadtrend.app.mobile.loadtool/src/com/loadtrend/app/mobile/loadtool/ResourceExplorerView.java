package com.loadtrend.app.mobile.loadtool;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import com.loadtrend.app.mobile.loadtool.action.RefreshRootProductAction;
import com.loadtrend.web.mobile.dao.model.Product;

public class ResourceExplorerView extends ViewPart {
    public static final String ID = "com.loadtrend.app.mobile.loadtool.ResourceExplorerView";

    private TreeViewer treeViewer;
    
    protected String EDITOR_ID = null;
    
    protected IEditorInput resourceEditorInput = null;
    
    protected Image viewImage = null;
    
    private Product rootProduct = null;
    
    /**
     * The content provider class is responsible for providing objects to the
     * view. It can wrap existing objects in adapters or simply return objects
     * as-is. These objects may be sensitive to the current input of the view,
     * or ignore it and always show the same content (like Task List, for
     * example).
     */
    class ViewContentProvider implements ITreeContentProvider {
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }

        public void dispose() {
        }

        public Object[] getElements(Object parent) {
            Product product = (Product) parent;
            return product.getChildren().toArray();
        }

        public Object[] getChildren(Object parentElement) {
            Product product = (Product) parentElement;
            return product.getChildren().toArray();
        }

        public Object getParent(Object element) {
            Product product = (Product) element;
            return product.getParent();
        }

        public boolean hasChildren(Object element) {
            Product product = (Product) element;
            return product.getChildren().size() > 0;
        }
        
    }

    class ViewLabelProvider extends LabelProvider {
        
        public String getText(Object element) {
            Product product = (Product) element;
            return super.getText(product.getName() + "[" + product.getId() + "]");
        }

        public Image getImage(Object obj) {
        	return viewImage;
        }
    }

    /**
     * This is a callback that will allow us to create the treeViewer and initialize
     * it.
     */
    public void createPartControl(Composite parent) {
        
        // Define tree treeViewer.
        Tree tree = new Tree(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
        treeViewer = new TreeViewer(tree); 
        
        // For the action set desgin in the plugin.xml can be affected by the treeViewer.
        getSite().setSelectionProvider(treeViewer);
        treeViewer.setContentProvider(new ViewContentProvider());
        treeViewer.setLabelProvider(new ViewLabelProvider());
        treeViewer.setAutoExpandLevel( TreeViewer.ALL_LEVELS );
        treeViewer.setInput(null);
        
        this.refreshTreeViewerByRootProduct();
        
        this.hookContextMenu();
        
        treeViewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                Product product = (Product) selection.getFirstElement();
                ResourceExplorerView.this.showResourceEditor(product);
            }
        });
    }
    
    public void showResourceEditor(Product product) {
        IWorkbenchWindow window = this.getSite().getWorkbenchWindow();
        IWorkbenchPage page = window.getActivePage();
        IEditorPart editorPart = page.findEditor(resourceEditorInput);
        if ( editorPart != null ) {
            page.closeEditor(editorPart, false );
        } 
        if (product.getItems().size() == 0) {
            boolean expandedState = !treeViewer.getExpandedState(product);
            treeViewer.setExpandedState( product, expandedState );
        } else {
            try {
                editorPart = page.openEditor(resourceEditorInput, EDITOR_ID);
                ResourceEditor resourceEditor = (ResourceEditor) editorPart;
                resourceEditor.showResourceFromProduct(this, product, 1);
            }
            catch ( PartInitException e ) {
                MessageDialog.openError(window.getShell(),
                                        "Open resource editor error.",
                                        e.getMessage());
            } 
        }
    }
    
    private void hookContextMenu()
    {
        // Create the context menu and register it with the Workbench.
        MenuManager menuMgr = new MenuManager( "#PopupMenu" );
        Menu menu = menuMgr.createContextMenu( treeViewer.getControl() );
        treeViewer.getControl().setMenu( menu );
        menuMgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        // Design for apply popupMenus extenstion defined in plugin.xml
        getSite().registerContextMenu( menuMgr, treeViewer );
    }
    
    public void refresh(Product rootProduct) {
    	this.rootProduct = rootProduct;
        treeViewer.setInput(rootProduct);
    }
    
    public void refreshTreeViewerByRootProduct() {
        new RefreshRootProductAction(this).run(null);
    }

    public void setFocus() {
        treeViewer.getControl().setFocus();
    }

	public void dispose() {
		super.dispose();
		this.viewImage.dispose();
	}

	/**
	 * @return Returns the rootProduct.
	 */
	public Product getRootProduct() {
		return rootProduct;
	}
}