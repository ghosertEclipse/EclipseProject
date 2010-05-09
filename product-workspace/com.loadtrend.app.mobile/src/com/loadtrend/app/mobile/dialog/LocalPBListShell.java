package com.loadtrend.app.mobile.dialog;

import java.util.Collection;

import loadtrend.mobile.PhoneBook;

import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.loadtrend.app.mobile.data.Global;
import com.loadtrend.app.mobile.data.ImageConstant;
import com.loadtrend.app.mobile.data.ImageLoader;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;

public class LocalPBListShell
{
    private Shell parentShell = null;
    
    private Text txtReceiver = null;
    
    private Shell shell = null;
    
    public LocalPBListShell( Shell parentShell, Text txtReceiver )
    {
        this.parentShell = parentShell;
        this.txtReceiver = txtReceiver;
    }
    
    public void create()
    {
        shell = new Shell( parentShell, SWT.TOOL | SWT.CLOSE );
        shell.setText( Messages.getString( MessagesConstant.LOCALPBLIST_SHELL_TITLE ) );
        shell.setLayout(new FormLayout());
        
        this.showFollowWithParentShell();
        
        // Add Search label.
        Label label = new Label(shell, SWT.SHADOW_IN);
        label.setText(Messages.getString(MessagesConstant.LocalPBListShell_SEARCH_LABEL));
        FormData data = new FormData();
        data.top = new FormAttachment( 2, 0 );
        data.left = new FormAttachment( 2, 0 );
        data.right = new FormAttachment(30);
        data.bottom = new FormAttachment(7);
        label.setLayoutData(data);
        
        // Add search text box.
        final Text txtSearch = new Text(shell, SWT.BORDER);
        data = new FormData();
        data.left = new FormAttachment(label);
        data.right = new FormAttachment(100);
        data.bottom = new FormAttachment(7);
        txtSearch.setLayoutData(data);
        
        // Add table viewer.
        final TableViewer tableViewer = this.createTableViewer(txtSearch);
        
        txtSearch.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                tableViewer.refresh();
            }
        });
        
        tableViewer.setInput( Global.getLocalPBCollection() );
        
        tableViewer.addSelectionChangedListener( new ISelectionChangedListener()
        {
            public void selectionChanged( SelectionChangedEvent event )
            {
                String numbers = "";
                TableItem[] items = tableViewer.getTable().getItems();
                for ( int i = 0; i < items.length; i++ )
                {
                    if ( items[i].getChecked() )
                    {
                        PhoneBook phoneBook = (PhoneBook) tableViewer.getElementAt( i );
                        numbers = numbers + phoneBook.getTeleNum() + ";";
                    }
                }
                
                // Get rid of the last ";"
                int lengthOfNumbers = numbers.length();
                if ( lengthOfNumbers > 0 && numbers.substring( lengthOfNumbers - 1, lengthOfNumbers ).equals( ";" ) )
                {
                    numbers = numbers.substring( 0, lengthOfNumbers - 1 );
                }
                
                txtReceiver.setText( numbers );
            }
        });
        
        tableViewer.addFilter(new ViewerFilter() {
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                PhoneBook phoneBook = (PhoneBook) element;
                String keywords = txtSearch.getText();
                if (phoneBook.getName().indexOf(keywords) != -1) return true;
                if (phoneBook.getTeleNum().indexOf(keywords) != -1) return true;
                return false;
            }
        });
        
        shell.open();
    }
    
    public boolean isDisposed()
    {
        return shell.isDisposed();
    }
    
    public void showFollowWithParentShell()
    {
        Rectangle parentBounds = parentShell.getBounds();
        
        shell.setLocation( parentBounds.x + parentBounds.width, parentBounds.y );
        shell.setSize( 180, parentBounds.height );
    }
    
    private TableViewer createTableViewer(Text txtSearch)
    {
        Table table = new Table( shell, SWT.CHECK );
        
        FormData data = new FormData();
        data.top = new FormAttachment(txtSearch);
        data.left = new FormAttachment( 0, 0 );
        data.right = new FormAttachment(100);
        data.bottom = new FormAttachment(100);
        table.setLayoutData(data);
        
        TableLayout layout = new TableLayout();
        layout.addColumnData( new ColumnWeightData( 50, true ));
        layout.addColumnData( new ColumnWeightData( 130, true ));
        table.setLayout( layout);       
        
        TableColumn status = new TableColumn( table, SWT.LEFT );
        status.setText( Messages.getString( MessagesConstant.PBEditor_TABLECOLUMN_ICON ) );
        TableColumn from = new TableColumn( table, SWT.LEFT );
        from.setText( Messages.getString( MessagesConstant.PBEditor_TABLECOLUMN_NAME ) );
        
        table.setHeaderVisible( false );
        
        TableViewer tableViewer = new TableViewer( table );
        
        tableViewer.setContentProvider( new IStructuredContentProvider()
        {
            public void inputChanged( Viewer viewer, Object oldInput, Object newInput )
            {
            }
        
            public void dispose()
            {
            }
        
            public Object[] getElements( Object inputElement )
            {
                return ( (Collection) ( inputElement ) ).toArray();
            }
        
        });
        
        tableViewer.setLabelProvider( new ITableLabelProvider()
        {
        
            public void removeListener( ILabelProviderListener listener )
            {
            }
        
            public boolean isLabelProperty( Object element, String property )
            {
                return false;
            }
        
            public void dispose()
            {
            }
        
            public void addListener( ILabelProviderListener listener )
            {
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
        
        return tableViewer;
    }
}
