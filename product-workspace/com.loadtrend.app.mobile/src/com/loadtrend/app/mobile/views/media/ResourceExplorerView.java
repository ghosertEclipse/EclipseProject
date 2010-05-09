package com.loadtrend.app.mobile.views.media;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;

import com.loadtrend.app.mobile.netaction.GetValidItemsByProductAction;
import com.loadtrend.web.mobile.dao.model.Product;

public class ResourceExplorerView extends ViewPart {
    public static final String ID = "com.loadtrend.app.mobile.views.media.ResourceExplorerView";

    private TreeViewer treeViewer;
    
    protected String EDITOR_ID = null;
    
    protected IEditorInput resourceEditorInput = null;
    
    protected Image viewImage = null;
    
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
            return super.getText(product.getName());
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
        
        this.hookContextMenu();
        
//        treeViewer.addDoubleClickListener(new IDoubleClickListener() {
//            public void doubleClick(DoubleClickEvent event) {
//                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
//                Product product = (Product) selection.getFirstElement();
//                new GetValidItemsByProductAction(ResourceExplorerView.this, product).run();
//            }
//        });
        
        treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                Product product = (Product) selection.getFirstElement();
                if (product != null) new GetValidItemsByProductAction(ResourceExplorerView.this, product).run();
			}
        });
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
        treeViewer.setInput(rootProduct);
    }
    
    public void setFocus() {
        treeViewer.getControl().setFocus();
    }
    
	public TreeViewer getTreeViewer() {
        return treeViewer;
    }

    public void dispose() {
		super.dispose();
		this.viewImage.dispose();
	}

    public String getEDITOR_ID() {
        return EDITOR_ID;
    }

    public IEditorInput getResourceEditorInput() {
        return resourceEditorInput;
    }
}