package com.loadtrend.app.mobile.views;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.part.ViewPart;

import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.editors.PBEditor;

public class PBSearchView extends ViewPart
{
    public static final String VIEW_ID = "com.loadtrend.app.mobile.views.PBSearchView";
    
    private Text searchText = null;
    
    private Label descriptionLabel = null;
    
    public void createPartControl( Composite parent )
    {
        this.setPartName( Messages.getString( MessagesConstant.PBSearchView_PARTNAME ) );
        
        searchText = new Text( parent, SWT.BORDER );
        descriptionLabel = new Label( parent, SWT.NONE );
        
        searchText.addModifyListener( new ModifyListener()
        {
            public void modifyText( ModifyEvent e )
            {
                IEditorPart editor = getViewSite().getPage().getActiveEditor();
                if ( editor != null && editor instanceof PBEditor )
                {
                    TableViewer tableViewer = ( (PBEditor) editor ).getTableViewer();
                    tableViewer.refresh();
                }
            }
        });
    }

    public void setFocus()
    {
        searchText.setFocus();
    }

    /**
     * @return Returns the descriptionLabel.
     */
    public Label getDescriptionLabel()
    {
        return descriptionLabel;
    }

    /**
     * @return Returns the searchText.
     */
    public Text getSearchText()
    {
        return searchText;
    }
    
    public void dispose()
    {
        IEditorReference[] editorReferences = getViewSite().getPage().getEditorReferences();
        for ( int i = 0; i < editorReferences.length; i++ )
        {
            IEditorPart editor = editorReferences[i].getEditor( false );
            if ( editor != null && editor instanceof PBEditor )
            {
                TableViewer tableViewer = ( (PBEditor) editor ).getTableViewer();
                tableViewer.refresh();
            }
        }
        super.dispose();
    }
}
