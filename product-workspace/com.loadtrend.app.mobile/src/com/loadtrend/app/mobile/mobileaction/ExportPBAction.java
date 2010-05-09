package com.loadtrend.app.mobile.mobileaction;

import java.util.ArrayList;
import java.util.Collection;

import loadtrend.mobile.Mobile;
import loadtrend.mobile.MobileException;
import loadtrend.mobile.PhoneBook;
import loadtrend.mobile.PhoneBookProcessor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindowPulldownDelegate2;
import org.eclipse.ui.PartInitException;

import com.loadtrend.app.mobile.data.Global;
import com.loadtrend.app.mobile.data.ImageConstant;
import com.loadtrend.app.mobile.data.ImageLoader;
import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.editors.PBEditor;
import com.loadtrend.app.mobile.util.SpecialEntity;
import com.loadtrend.app.trees.TreeObject;
import com.loadtrend.app.trees.TreeParent;

public class ExportPBAction extends MobileAction implements IWorkbenchWindowPulldownDelegate2
{
    protected String pbMemoType = null;
    
    private PhoneBook[] phoneBooks = null;
    
    private ArrayList phoneBookSaved = null;
    
    private TreeObject to = null;

    public boolean performUIBeforeNonUI()
    {
        if ( pbMemoType == null ) return false;
        
        phoneBooks = (PhoneBook[]) super.structuredSelection.toList().toArray( new PhoneBook[]{} );
        
        // Get the mobile TreeObject or simcard TreeObject which is decided by user select.
        TreeParent tp = Global.getPBInvisibleRoot();
        TreeObject toParent = tp.getChildren( SpecialEntity.getPBMemoDesc( pbMemoType ) );
        tp = (TreeParent) toParent;


        // Check the validation for phonebook when exporting them to simcard or mobile.
        for ( int i = 0; i < phoneBooks.length; i++ )
        {
            String errorDescription = SpecialEntity.checkPhoneBookNameNumber( phoneBooks[i].getName(), 
                                                                              phoneBooks[i].getTeleNum(), 
                                                                              pbMemoType );
            if ( !errorDescription.equals( "" ) )
            {
                MessageBox mb = new MessageBox( super.window.getShell(), SWT.ICON_ERROR | SWT.OK );
                mb.setText( Messages.getString( MessagesConstant.EXPORTPB_FALURE_MESSAGEBOX_TITLE ) );
                mb.setMessage( Messages.getString( MessagesConstant.EXPORT_PB_ACTION_INFO, 
                                                   phoneBooks[i].getName(), 
                                                   phoneBooks[i].getTeleNum(), 
                                                   errorDescription ) ) ;
                mb.open();
                return false;
            }
        }
        
        // Check whether the phonebook has been list
        to = tp.getChildren( Messages.getString( MessagesConstant.PBBOX_TEXT ) );
        if ( to.getCollection() == null )
        {
            MessageBox mb = new MessageBox( super.window.getShell(), SWT.ICON_WARNING | SWT.OK );
            mb.setText( Messages.getString( MessagesConstant.EXPORTPB_PROMPT_MESSAGEBOX_TITLE ) );
            mb.setMessage( Messages.getString( MessagesConstant.WARNING_SAVEPB_REFRESHLIST_FIRST ) );
            mb.open();
            return false;
        }
        
        return true;
    }
    
    public void performNonUI()
    {
        try
        {
            this.savePB();
        }
        catch ( Exception e )
        {
            super.saveError( Messages.getString( MessagesConstant.SAVEPB_FALURE_MESSAGEBOX_TITLE ), null, e );
        }
    }

    public void performUIAfterNonUI()
    {
        if ( !super.hasError() )
        {
            IWorkbenchPage page = super.window.getActivePage();
            
            // Set or add saved phonebook, then open the editor part to show it.
            if ( to.getCollection() != null )
            {
                // Notify the observer to refresh interface.
                to.getCollection().addAll( phoneBookSaved );
                to.setCollection( to.getCollection() );
                if ( !pbMemoType.equals( "" ) )
                {
                    // Load data to pbBox if the data is saved from simcard or mobile
                    TreeObject localMachine = Global.getPBInvisibleRoot().getChildren( Messages.getString( MessagesConstant.LOCALMACHINE_TEXT ) );
                    TreeObject pbBox = ( (TreeParent) localMachine ).getChildren( Messages.getString( MessagesConstant.PBBOX_TEXT ) );
                    pbBox.getCollection().addAll( phoneBookSaved );
                    pbBox.setCollection( pbBox.getCollection() );
                }
                
                // Open the editor if it does not exist.
                IEditorPart editorPart = page.findEditor( to.getEditorInput() );
                if ( editorPart == null )
                {
                    try
                    {
                        editorPart = page.openEditor( to.getEditorInput(), PBEditor.EDITOR_ID );
                        ( (PBEditor) editorPart ).setTreeObject( to );
                    }
                    catch ( PartInitException e )
                    {
                        e.printStackTrace();
                    }
                }
                page.bringToTop( editorPart );
            }
        }
        
        pbMemoType = null;
    }
    
    private void savePB() throws MobileException
    {        
        Mobile mobile = super.mobile;
        
        int pieces = phoneBooks.length;
        
        int phoneBookSpace = mobile.getPhoneBookSpace( pbMemoType );
        
        Collection collection = to.getCollection();
        
        // Check the phonebook space before save the phonebooks.
        int capability = phoneBookSpace - pieces - collection.size();
        if ( capability < 0 )
        {
            throw new IllegalArgumentException( Messages.getString( MessagesConstant.ERROR_SAVEPB_CAPABILITY_FULL, 
                                                                    new Integer( pieces ), 
                                                                    new Integer( pieces + capability ) ) );
        }
        
        // Save phonebooks
        int[] index = new int[pieces];
        PhoneBookProcessor.getPhoneBookIndexes( index, phoneBookSpace, collection );
        
        phoneBookSaved = new ArrayList();
        for ( int i = 0, n = 1; i < index.length; i++ )
        {
            PhoneBook clonePhoneBook = (PhoneBook) phoneBooks[i].clone();
            super.showRealTimeInfo( Messages.getString( MessagesConstant.SAVE_PB_ACTION_INFO, 
                                                        getShortenContent( clonePhoneBook ), 
                                                        clonePhoneBook.getTeleNum(), 
                                                        new Integer( n++ ), 
                                                        new Integer( pieces ) ) );
            mobile.savePhoneBook( index[i], clonePhoneBook );
            phoneBookSaved.add( clonePhoneBook );
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchWindowPulldownDelegate#getMenu(org.eclipse.swt.widgets.Control)
     */
    public Menu getMenu(Control parent) {
        Menu exportPBMenu = new Menu( parent );
        return this.configurateMenu( exportPBMenu );
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchWindowPulldownDelegate2#getMenu(org.eclipse.swt.widgets.Menu)
     */
    public Menu getMenu(Menu parent) {
        Menu exportPBMenu = new Menu( parent );
        return this.configurateMenu( exportPBMenu );
    }
    
    private Menu configurateMenu( Menu menu )
    {
        ImageLoader loader = ImageLoader.getInstance();
        
        // Click Mobile item
        MenuItem mobileItem = new MenuItem( menu, SWT.PUSH );
        mobileItem.setText( Messages.getString( MessagesConstant.MOBILE_TEXT ) );
        mobileItem.setImage( loader.getImage( ImageConstant.MOBILE_IMAGE ) );
        mobileItem.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                pbMemoType = Mobile.PB_ME_MEMO;
                ExportPBAction.this.run();
            }
        } );
        
        // Click Sim Card item
        MenuItem simCardItem = new MenuItem( menu, SWT.PUSH );
        simCardItem.setText( Messages.getString( MessagesConstant.SIMCARD_TEXT ) );
        simCardItem.setImage( loader.getImage( ImageConstant.SIMCARD_IMAGE ) );
        simCardItem.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                pbMemoType = Mobile.PB_SM_MEMO;
                ExportPBAction.this.run();
            }
        } );

        return menu;
    }
}
