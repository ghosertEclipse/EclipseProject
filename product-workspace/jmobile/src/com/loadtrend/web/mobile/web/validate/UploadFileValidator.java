package com.loadtrend.web.mobile.web.validate;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.loadtrend.web.mobile.web.form.UploadFileForm;

public class UploadFileValidator implements Validator {

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
		
		ValidationUtils.rejectIfEmpty(errors, "mobile", "MOBILE_REQUIRED");
		
		String mobile = uploadFileForm.getMobile();
        
        String description = uploadFileForm.getDescription();
        
        String author = uploadFileForm.getAuthor();
        
        int type = uploadFileForm.getType();
		
		if (!mobile.matches("[0-9]+")) {
			errors.rejectValue("mobile", "NUMBER_ONLY");
		}
		
		if (mobile.length() != 11) {
			errors.rejectValue("mobile", "EQUALS_ELEVEN");
		}
        
        if (description.length() > 6 && type == UploadFileForm.PICTURE_TYPE) {
            errors.rejectValue("description", "PICT_DESC_TOO_LONG", new String[]{String.valueOf(6)}, "");
        }
        
        if (description.length() > 20 && type == UploadFileForm.MUSIC_TYPE) {
            errors.rejectValue("description", "MUSIC_DESC_TOO_LONG", new String[]{String.valueOf(20)}, "");
        }
        
        if (author.length() > 20) {
            errors.rejectValue("author", "AUTHOR_TOO_LONG", new String[]{String.valueOf(20)}, "");
        }
        
	}
}
