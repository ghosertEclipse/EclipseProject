package com.loadtrend.web.mobile.web.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.loadtrend.web.mobile.dao.model.Item;
import com.loadtrend.web.mobile.service.JMobileManager;
import com.loadtrend.web.mobile.web.form.UploadFileForm;
import com.loadtrend.web.mobile.web.util.StringUtil;

public class UploadFileController extends SimpleFormController {
	
	private static final String FILE_CONSTANT = "file";
	
	private final String UPLOAD_FOLDER = "uploadfiles";
	
	private static final String[] MUSIC_TYPE = new String[] {"mmf", "wav", "amr", "mid", "mp3", "imy", "wma"};
	
	private static final String[] PICTURE_TYPE = new String[] {"bmp", "gif", "ico", "jpg", "png"};
    
    private static final int FILE_UNKNOWN_TYPE = -1;
    
    private static final int FILE_PICTURE_TYPE = 0;
    
    private static final int FILE_MUSIC_TYPE = 1;
	
	private int maxFileSize = 0;
	
	private int maxFilenameLength = 0;
	
	private int topWeekPayTimesUploaderNumber = 0;
	
	private JMobileManager jmobileManager = null;
	
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map model = new HashMap();
		List list = this.jmobileManager.getTopWeekPayTimesUploaders(this.topWeekPayTimesUploaderNumber);
		Iterator it = list.iterator();
		while (it.hasNext()) {
			// Encoding uploader.
			Object[] object = (Object[]) it.next();
			String uploader = (String) object[0];
			try {
			    object[0] = uploader.substring(0, 3) + "*****" + uploader.substring(8, 11);
			} catch (Exception e) {
			}
		}
		model.put("uploaders", list);
		return model;
	}	
	
	protected void onBindAndValidate(HttpServletRequest request, Object object, BindException bindException) throws Exception {
		// validate the file path first.
	    MultipartHttpServletRequest multipartRequest  =  (MultipartHttpServletRequest) request;
	    MultipartFile file  =  multipartRequest.getFile(FILE_CONSTANT);
	    String filename = file.getOriginalFilename();
	    
	    if (file == null || file.getSize() == 0) {
	    	bindException.rejectValue("uploadfile", "UPLOAD_FILE_NOT_EXISTING");
	    }
	    
	    if (file.getSize() > maxFileSize) {
	    	bindException.rejectValue("uploadfile", "EXCEED_MAX_FILE_SIZE", new String[] {String.valueOf(maxFileSize / 1024.0)}, "");
	    }
	    
	    String name = this.getFileNameWithoutExtension(filename);
	    if (name.length() > maxFilenameLength) {
	    	bindException.rejectValue("uploadfile", "FILENAME_TOO_LONG", new String[] {String.valueOf(maxFilenameLength)}, "");
	    }
	    
        int type = this.getFileType(filename);
		if (type == FILE_UNKNOWN_TYPE) bindException.rejectValue("uploadfile", "UPLOAD_FILETYPE_INVALIDATE");
        
        UploadFileForm uploadFileForm = (UploadFileForm) object;
        uploadFileForm.setType(type);
        uploadFileForm.setUploadfile(file.getOriginalFilename());
        
		// validate the mobile parameter first.
		this.getValidator().validate(object, bindException);
		
	}

	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object object, BindException bindException) throws Exception {
		// Get file.
	    MultipartHttpServletRequest multipartRequest  =  (MultipartHttpServletRequest) request;
	    MultipartFile file  =  multipartRequest.getFile(FILE_CONSTANT);
	    String filename = file.getOriginalFilename();
	    
	    // Get UploadForm
	    UploadFileForm uploadFileForm = (UploadFileForm) object;
        String mobile = uploadFileForm.getMobile();
        String desc = uploadFileForm.getDescription();
        String author = uploadFileForm.getAuthor();
        int type = uploadFileForm.getType();
        
	    // Get the product id.
	    String productid = this.getProductIdByFileType(filename);
	    
	    // Create and save item.
	    Item item = new Item();
        item.setUploader(mobile);
	    item.setIsValid("0");
	    item.setName(!desc.equalsIgnoreCase("") ? desc : this.getFileNameWithoutExtension(filename));
        if (type == FILE_MUSIC_TYPE) {
            item.setAuthor(author);
        }
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
        this.saveFile(newFilname, file.getBytes());
	    
	    return new ModelAndView(MessageFormat.format(this.getSuccessView(), new String[] {StringUtil.encodeString(item.getUploader())}));
	}
	
	private void saveFile(String filename, byte[] bytes) throws IOException {
	    String realUploadFilename = super.getServletContext().getRealPath("/") + UPLOAD_FOLDER + File.separator + filename;
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

	/**
	 * @param maxFileSize The maxFileSize to set.
	 */
	public void setMaxFileSize(int maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	/**
	 * @param jmobileManager The jmobileManager to set.
	 */
	public void setJmobileManager(JMobileManager jmobileManager) {
		this.jmobileManager = jmobileManager;
	}

	/**
	 * @param maxFilenameLength The maxFilenameLength to set.
	 */
	public void setMaxFilenameLength(int maxFilenameLength) {
		this.maxFilenameLength = maxFilenameLength;
	}

	/**
	 * @param topWeekPayTimesUploaderNumber The topWeekPayTimesUploaderNumber to set.
	 */
	public void setTopWeekPayTimesUploaderNumber(int topWeekPayTimesUploaderNumber) {
		this.topWeekPayTimesUploaderNumber = topWeekPayTimesUploaderNumber;
	}

}
