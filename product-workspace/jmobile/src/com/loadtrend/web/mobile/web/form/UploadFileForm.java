package com.loadtrend.web.mobile.web.form;

public class UploadFileForm {
	
    public static final int UNKNOWN_TYPE = -1;
    
    public static final int PICTURE_TYPE = 0;
    
    public static final int MUSIC_TYPE = 1;
    
	private String mobile = null;
    
    private String description = null;
    
    private String author = null;
    
    private int type = 0;
    
    private String uploadfile = null;

	/**
	 * @return Returns the uploadfile.
	 */
	public String getUploadfile() {
		return uploadfile;
	}

	/**
	 * @param uploadfile The uploadfile to set.
	 */
	public void setUploadfile(String uploadfile) {
		this.uploadfile = uploadfile;
	}

	public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    /**
	 * @return Returns the mobile.
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile The mobile to set.
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
    
}
