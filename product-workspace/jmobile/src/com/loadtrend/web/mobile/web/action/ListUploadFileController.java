package com.loadtrend.web.mobile.web.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.loadtrend.web.mobile.service.JMobileManager;
import com.loadtrend.web.mobile.web.info.ItemPageInfo;
import com.loadtrend.web.mobile.web.util.StringUtil;

public class ListUploadFileController implements Controller {
    
    private String view = null;
    
    private JMobileManager jmobileManager = null;
    
    private static final String PICTURE_TYPE = "0";
    
    private static final String MUSIC_TYPE = "1";
    
    private int picturePageSize = 0;
    
    private int musicPageSize = 0;

    public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String uploader = StringUtil.decodeString(request.getParameter("uploader"));
        String musicPageNumber = request.getParameter("mpage");
        String picturePageNumber = request.getParameter("ppage");
        // Get  picture type items.
        int totalSize = this.jmobileManager.getTotalSize(uploader, PICTURE_TYPE);
        List items = this.jmobileManager.getItemsByUploader(uploader, PICTURE_TYPE, Integer.parseInt(picturePageNumber), picturePageSize);
        ItemPageInfo pictureItemPageInfo = new ItemPageInfo(totalSize, (totalSize-1)/picturePageSize + 1, items);
        // Get music type items.
        totalSize = this.jmobileManager.getTotalSize(uploader, MUSIC_TYPE);
        items = this.jmobileManager.getItemsByUploader(uploader, MUSIC_TYPE, Integer.parseInt(musicPageNumber), musicPageSize);
        ItemPageInfo musicItemPageInfo = new ItemPageInfo(totalSize, (totalSize-1)/musicPageSize + 1, items);
        
        Map resultMap = new HashMap();
        resultMap.put("picture", pictureItemPageInfo);
        resultMap.put("music", musicItemPageInfo);
        resultMap.put("uploader", StringUtil.encodeString(uploader));
        resultMap.put("mpage", musicPageNumber);
        resultMap.put("ppage", picturePageNumber);
        return new ModelAndView(this.view, resultMap);
    }

    public void setJmobileManager(JMobileManager jmobileManager) {
        this.jmobileManager = jmobileManager;
    }

    public void setView(String view) {
        this.view = view;
    }

	/**
	 * @param musicPageSize The musicPageSize to set.
	 */
	public void setMusicPageSize(int musicPageSize) {
		this.musicPageSize = musicPageSize;
	}

	/**
	 * @param picturePageSize The picturePageSize to set.
	 */
	public void setPicturePageSize(int picturePageSize) {
		this.picturePageSize = picturePageSize;
	}
}
