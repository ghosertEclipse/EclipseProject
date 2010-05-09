package com.loadtrend.app.mobile.loadtool.action;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;

import com.loadtrend.app.mobile.loadtool.ResourceExplorerView;
import com.loadtrend.web.mobile.dao.model.Item;
import com.loadtrend.web.mobile.dao.model.Product;

public class ImportResourceAction extends NetAction {

    private Product product = null;
    
    private static final String[] MUSIC_TYPE = new String[] {"mmf", "wav", "amr", "mid", "mp3", "imy", "wma"};
    
    private static final String[] PICTURE_TYPE = new String[] {"bmp", "gif", "ico", "jpg", "png"};
    
    private static final int FILE_UNKNOWN_TYPE = -1;
    
    private static final int FILE_PICTURE_TYPE = 0;
    
    private static final int FILE_MUSIC_TYPE = 1;
    
    private Item buildItem(String name, String author, String url, int type) {
        Item item = new Item();
        item.setName(name);
        item.setAuthor(author);
        item.setUrl(url);
        item.setWeekpaytimes(new Integer(0));
        item.setPaytimes(new Integer(0));
        item.setProduct(this.product);
        item.setUploadtime(new Date());
        item.setIsValid("1");
        item.setItemType(String.valueOf(type));
        return item;
    }

	/* (non-Javadoc)
	 * @see com.loadtrend.app.mobile.action.NetAction#netExecute(org.eclipse.jface.action.IAction, com.loadtrend.web.mobile.service.JMobileManager)
	 */
	protected void netExecute() {
        this.product = (Product) this.structuredSelection.getFirstElement();
        FileDialog fileDialog = new FileDialog(super.window.getShell());
        fileDialog.setFilterExtensions(new String[] {"*.txt"});
        String filename = fileDialog.open();
        if (filename != null) {
            try {
                // Get items from txt file.
                Set items = new LinkedHashSet();
                FileReader fileReader = new FileReader(filename);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String lineString = null;
                while ((lineString = bufferedReader.readLine()) != null) {
                	lineString = lineString.trim();
                    if (lineString.equals("")) continue;
                    String[] strings = lineString.split(" ");
                    int length = strings.length;
                    String url = strings[length - 1];
                    int type = this.getFileType(url);
                    String name = null;
                    String author = null;
                    if (type == FILE_PICTURE_TYPE && length >= 2) {
                    	name = lineString.substring(0, lineString.length() - url.length() - " ".length());
                    }
                    if (type == FILE_MUSIC_TYPE && length >= 3) {
                    	author = strings[length - 2];
                    	name = lineString.substring(0, lineString.length() - url.length()-
                    			                       author.length() - " ".length() * 2);
                    }
                    if (name != null || author != null) {
                    	Item item = this.buildItem(name, author, url, type);
                        if (items.contains(item)) items.remove(item);
                        items.add(item);
                    }
                }
                // Import items to the database.
                if (items.size() > 0) {
                	super.jMobileManager.saveUniqueItems(this.product, items);
                	this.product = super.jMobileManager.getProduct(this.product.getId(), true);
                }
                
                // Refresh treeViewer.
                ((ResourceExplorerView)super.view).refreshTreeViewerByRootProduct();
                ((ResourceExplorerView)super.view).showResourceEditor(product);
                
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            MessageBox messageBox = new MessageBox(super.window.getShell());
            messageBox.setMessage("导入完成！");
            messageBox.open();
        }
	}
    
    private int getFileType(String filename) {
        int lastIndex = filename.lastIndexOf(".");
        if (lastIndex == -1) return FILE_UNKNOWN_TYPE;
        String extenstion = filename.substring(lastIndex + 1);
        for (int i = 0; i < PICTURE_TYPE.length; i++) {
            if (extenstion.equalsIgnoreCase(PICTURE_TYPE[i])) return FILE_PICTURE_TYPE;
        }
        for (int i = 0; i < MUSIC_TYPE.length; i++) {
            if (extenstion.equalsIgnoreCase(MUSIC_TYPE[i])) return FILE_MUSIC_TYPE;
        }
        return FILE_UNKNOWN_TYPE;
    }

}
