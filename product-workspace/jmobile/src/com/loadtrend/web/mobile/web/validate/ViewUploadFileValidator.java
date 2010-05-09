package com.loadtrend.web.mobile.web.validate;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.loadtrend.web.mobile.web.form.UploadFileForm;

public class ViewUploadFileValidator implements Validator {

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class clazz) {
		return UploadFileForm.class.isAssignableFrom(clazz);
	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	public void validate(Object obj, Errors errors) {
		UploadFileForm uploadFileForm = (UploadFileForm) obj;
		
		ValidationUtils.rejectIfEmpty(errors, "mobile", "MOBILE_VIEW_REQUIRED");
		
		String mobile = uploadFileForm.getMobile();
        
		if (!mobile.matches("[0-9]+")) {
			errors.rejectValue("mobile", "NUMBER_ONLY");
		}
		
		if (mobile.length() != 11) {
			errors.rejectValue("mobile", "EQUALS_ELEVEN");
		}
	}

}
