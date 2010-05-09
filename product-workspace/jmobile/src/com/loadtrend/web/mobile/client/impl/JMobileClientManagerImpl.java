package com.loadtrend.web.mobile.client.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.loadtrend.web.mobile.client.RequestHolder;
import com.loadtrend.web.mobile.client.JMobileClientManager;
import com.loadtrend.web.mobile.dao.model.Item;
import com.loadtrend.web.mobile.dao.model.Product;
import com.loadtrend.web.mobile.dao.model.UserRecorder;
import com.loadtrend.web.mobile.service.JMobileManager;

public class JMobileClientManagerImpl implements JMobileClientManager {
    
    private final String UPLOAD_FOLDER = "uploadfiles";
    
    private static final String[] MUSIC_TYPE = new String[] {"mmf", "wav", "amr", "mid", "mp3", "imy", "wma"};
    
    private static final String[] PICTURE_TYPE = new String[] {"bmp", "gif", "ico", "jpg", "png"};
    
    private static final int FILE_UNKNOWN_TYPE = -1;
    
    private static final int FILE_PICTURE_TYPE = 0;
    
    private static final int FILE_MUSIC_TYPE = 1;
    
    private JMobileManager jmobileManager = null;
    
   public Item uploadLocalFile(String filename, byte[] bytes) throws IOException {
	    HttpServletRequest request = RequestHolder.getRequest();
        int type = this.getFileType(filename);
        
        // Get the product id.
        String productid = this.getProductIdByFileType(filename);
        
        // Create and save item.
        Item item = new Item();
        item.setIsValid("0");
        item.setName(this.getFileNameWithoutExtension(filename));
        item.setWeekpaytimes(new Integer(0));
        item.setPaytimes(new Integer(0));
        item.setUploadtime(new Date());
        item.setProduct(this.jmobileManager.getProduct(productid, false));
        item.setItemType(String.valueOf(type));
        this.jmobileManager.saveItem(item);
        
        // Create url and save item once again.
        String newFilname = this.getNewFilename(filename, item.getId());
	    String url = request.getScheme() + "://" + request.getServerName() + ":" + 
        request.getLocalPort() + request.getContextPath() + "/" + UPLOAD_FOLDER + "/" + newFilname;
        item.setUrl(url);
        this.jmobileManager.saveItem(item);
        
        // Get real upload file name and create the file now.
        this.saveFile(newFilname, bytes);
        
        return item;
    }
    
    private void saveFile(String filename, byte[] bytes) throws IOException {
	    HttpServletRequest request = RequestHolder.getRequest();
        String realUploadFilename = request.getSession().getServletContext().getRealPath("/") + UPLOAD_FOLDER + File.separator + filename;
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(realUploadFilename);
            outputStream.write(bytes);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
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
    
    private String getFileNameWithoutExtension(String filename) {
        String name = null;
        if (filename.lastIndexOf(".") != -1) {
            name = filename.substring(0, filename.lastIndexOf("."));
        } else {
            name = filename;
        }
        return name;
    }
    
    private String getNewFilename(String oldFilename, String id) {
        String extenstion = oldFilename.substring(oldFilename.lastIndexOf(".") + 1);
        return id + "." + extenstion;
    }
    
    private String getProductIdByFileType(String filename) {
        String productid = null;
        if (this.getFileType(filename) == FILE_PICTURE_TYPE) {
            // picture area net friend upload
            productid = "4"; // productid
        } else {
            // music area net friend upload
            productid = "5"; // productid
        }
        return productid;
    }

    public Product getRootProduct(boolean initialize) {
        return this.jmobileManager.getRootProduct(initialize);
    }
    
    public Product getProduct(String id, boolean initialize) {
        return this.jmobileManager.getProduct(id, initialize);
    }

    public List getValidItemsByProduct(Product product) {
        return this.jmobileManager.getValidItemsByProduct(product);
    }
    
    public List getValidItemsBySearch(String key, String itemType) {
        return this.jmobileManager.getValidItemsBySearch(key, itemType);
    }
    
    public boolean isRegistedUser(String mobileNumber) {
		return false;
	}

    public void saveUserRecorder(String mobile) {
    	UserRecorder userRecorder = new UserRecorder();
    	userRecorder.setMobile(mobile);
    	userRecorder.setLoginTime(new Date());
    	userRecorder.setIp(RequestHolder.getRequest().getRemoteAddr());
    	this.jmobileManager.saveUserRecorder(userRecorder);
    }
    
	public void setJmobileManager(JMobileManager jmobileManager) {
        this.jmobileManager = jmobileManager;
    }
}
