package com.loadtrend.app.mobile.editors;

import java.util.Collection;

import loadtrend.mobile.PhoneBook;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.loadtrend.app.mobile.data.ImageConstant;
import com.loadtrend.app.mobile.data.ImageLoader;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.views.PBSearchView;
import com.loadtrend.app.trees.TreeObject;

public class PBEditor extends EditorPart
{
    public static final String EDITOR_ID = "com.loadtrend.app.mobile.editors.PBEditor";
    
    private Table table = null;
    
    private TableViewer tableViewer = null;
    
    private Image titleImage = null;
    
    private IWorkbenchWindow window = null;
    
    private TreeObject treeObject = null;
    
    public void doSave( IProgressMonitor monitor )
    {
        // TODO Auto-generated method stub

    }

    public void doSaveAs()
    {
        // TODO Auto-generated method stub

    }

    public void init( IEditorSite site, IEditorInput input ) throws PartInitException
    {
        setSite( site );
        setInput( input );
//      设置Editor标题栏的显示名称。不要，则名称用plugin.xml中的name属性
        setPartName(input.getName());
//      设置Editor标题栏的图标。不要，则会自动使用一个默认的图标
        titleImage = input.getImageDescriptor().createImage();
        setTitleImage( titleImage );
    }

    public boolean isDirty()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isSaveAsAllowed()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void createPartControl( Composite parent )
    {
        window = getEditorSite().getWorkbenchWindow();
        
        table = new Table( parent, SWT.MULTI | SWT.FULL_SELECTION );
        TableLayout layout = new TableLayout();
        layout.addColumnData( new ColumnWeightData( 10, true ));
        layout.addColumnData( new ColumnWeightData( 21, true ));
        layout.addColumnData( new ColumnWeightData( 43, true ));
        
        table.setLayout( layout);       
        TableColumn status = new TableColumn( table, SWT.LEFT );
        status.setText( Messages.getString( MessagesConstant.PBEditor_TABLECOLUMN_ICON ) );
        TableColumn from = new TableColumn( table, SWT.LEFT );
        from.setText( Messages.getString( MessagesConstant.PBEditor_TABLECOLUMN_NAME ) );
        TableColumn content = new TableColumn( table, SWT.LEFT );
        content.setText( Messages.getString( MessagesConstant.PBEditor_TABLECOLUMN_TELENUMBER ) );
        
        table.setHeaderVisible( true );
        
        tableViewer = new TableViewer( table );
        
        // Design for the actions can get selection from tableViewer.
        getSite().setSelectionProvider(tableViewer);
        
        // Set the context menu
        final MenuManager menuManager = new MenuManager( "#PopupMenu" );
        menuManager.setRemoveAllWhenShown( true );
        menuManager.addMenuListener( new IMenuListener() {
            public void menuAboutToShow( IMenuManager manager )
            {
                manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
            }
        });
        
        Menu menu = menuManager.createContextMenu( tableViewer.getControl() );
        tableViewer.getControl().setMenu( menu );
        // Design for apply popupMenus extenstion defined in plugin.xml
        getSite().registerContextMenu(menuManager, tableViewer);
        
        
        tableViewer.setContentProvider( new IStructuredContentProvider()
        {
            public void inputChanged( Viewer viewer, Object oldInput, Object newInput )
            {
                // TODO Auto-generated method stub
        
            }
        
            public void dispose()
            {
                // TODO Auto-generated method stub
        
            }
        
            public Object[] getElements( Object inputElement )
            {
                // TODO Auto-generated method stub
                return ( (Collection) ( inputElement ) ).toArray();
            }
        
        });
        
        tableViewer.setLabelProvider( new ITableLabelProvider()
        {
        
            public void removeListener( ILabelProviderListener listener )
            {
                // TODO Auto-generated method stub
        
            }
        
            public boolean isLabelProperty( Object element, String property )
            {
                // TODO Auto-generated method stub
                return false;
            }
        
            public void dispose()
            {
                // TODO Auto-generated method stub
        
            }
        
            public void addListener( ILabelProviderListener listener )
            {
                // TODO Auto-generated method stub
        
            }
        
            public String getColumnText( Object element, int columnIndex )
            {
                PhoneBook phoneBook = (PhoneBook) element;
                switch ( columnIndex )
                {
                    case 0:
                        return null;
                    case 1:
                        return phoneBook.getName();
                    case 2:
                        return phoneBook.getTeleNum();
                    default:
                        return Messages.getString( MessagesConstant.PBEditor_TABLECOLUMN_INVALID, new Integer( columnIndex) );
                }
            }
        
            public Image getColumnImage( Object element, int columnIndex )
            {
                ImageLoader loader = ImageLoader.getInstance();
                if ( columnIndex == 0 )
                {
                    return loader.getImage( ImageConstant.PB_TABLECOLUMN_IMAGE );
                }
                else
                {
                    return null;
                }
            }       
        });

        tableViewer.addSelectionChangedListener( new ISelectionChangedListener()
        {
            public void selectionChanged( SelectionChangedEvent event )
            {
                // Get the first message selected, return if message equals null.
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                PhoneBook phoneBook = (PhoneBook) selection.getFirstElement();
                if ( phoneBook == null ) return;
            }
        });
        
        tableViewer.addFilter( new ViewerFilter()
        {
            public boolean select( Viewer viewer, Object parentElement, Object element )
            {
                PBSearchView searchView = (PBSearchView) window.getActivePage().findView( PBSearchView.VIEW_ID );
                if ( searchView != null )
                {
                    String keyword = searchView.getSearchText().getText();
                    PhoneBook phoneBook = (PhoneBook) element;
                    if ( keyword.equals( "" ) )
                        return true;
                    if ( phoneBook.getName().indexOf( keyword ) != -1 )
                    {
                        return true;
                    }
                    if ( phoneBook.getTeleNum().indexOf( keyword ) != -1 )
                    {
                        return true;
                    }
                    return false;
                }
                return true;
            }
        } );
        
        // The following code is designed for sorting data when clicking column.
        class PBSorter extends ViewerSorter
        {
            int columnIndex = 3;
            boolean revert = true;
            
            public int compare( Viewer viewer, Object e1, Object e2 )
            {
                PhoneBook phoneBook1 = (PhoneBook) e1;
                PhoneBook phoneBook2 = (PhoneBook) e2;
                int comparation = 0;
                
                switch ( columnIndex )
                {
                    case 0:
                        break;
                    case 1:
                        comparation = phoneBook1.getName().compareTo( phoneBook2.getName() );
                        break;
                    case 2:
                        comparation = phoneBook1.getTeleNum().compareTo( phoneBook2.getTeleNum() );
                        break;
                    default:
                        comparation = 0;
                }
                
                if ( revert ) comparation = -comparation;
                
                 // Set ASC order image or DESC order image
                ImageLoader loader = ImageLoader.getInstance();
                if ( revert )
                {
                    this.setColumnImage( columnIndex, loader.getImage( ImageConstant.DESC_ORDER_IMAGE ) );
                }
                else
                {
                    this.setColumnImage( columnIndex, loader.getImage( ImageConstant.ASC_ORDER_IMAGE ) );
                }
                
                return comparation;
            }
            
            // Set ASC order image or DESC order image
            private void setColumnImage( int columnIndex, Image image )
            {
                for ( int i = 0; i < table.getColumnCount(); i++ )
                {
                    if ( i == columnIndex )
                    {
                        table.getColumn( i ).setImage( image );
                    }
                    else
                    {
                        table.getColumn( i ).setImage( null );
                    }
                }
            }
        }
        
        final PBSorter sort = new PBSorter();
        
        tableViewer.setSorter( sort );
        
        SelectionListener selectionListener = new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                sort.columnIndex = tableViewer.getTable().indexOf( (TableColumn) e.widget );
                sort.revert = !sort.revert;
                tableViewer.refresh();
            }
        };
        
        for ( int i = 0; i < table.getColumnCount(); i++ )
        {
            table.getColumn( i ).addSelectionListener( selectionListener );
        }
        
        
    }

    public void setFocus()
    {
        tableViewer.getControl().setFocus();
    }
    
    public void dispose()
    {
        super.dispose();
        titleImage.dispose();
    }
    
    public TableViewer getTableViewer()
    {
        return this.tableViewer;
    }

    public TreeObject getTreeObject()
    {
        return treeObject;
    }

    public void setTreeObject( TreeObject treeObject )
    {
        this.treeObject = treeObject;
        tableViewer.setInput( treeObject.getCollection() );
    }
}
