package com.loadtrend.web.mobile.web.action;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.loadtrend.web.mobile.service.JMobileManager;
import com.loadtrend.web.mobile.web.form.UploadFileForm;
import com.loadtrend.web.mobile.web.util.StringUtil;

public class ViewUploadFileController extends SimpleFormController {
    
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

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(java.lang.Object)
	 */
	protected ModelAndView onSubmit(Object command) throws Exception {
		UploadFileForm uploadFileForm = (UploadFileForm) command;
		String mobile = uploadFileForm.getMobile();
	    return new ModelAndView(MessageFormat.format(this.getSuccessView(),
	    		                                     new String[] {StringUtil.encodeString(mobile)}));
	}

    public void setJmobileManager(JMobileManager jmobileManager) {
        this.jmobileManager = jmobileManager;
    }

    public void setTopWeekPayTimesUploaderNumber(int topWeekPayTimesUploaderNumber) {
        this.topWeekPayTimesUploaderNumber = topWeekPayTimesUploaderNumber;
    }
    
    

}
