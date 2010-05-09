package com.loadtrend.app.mobile.editors;

import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;

public class WaitForNetProcessEditor extends WaitForMobileProcessEditor {

    public static final String EDITOR_ID = "com.loadtrend.app.mobile.editors.WaitForNetProcessEditor"; 

    /**
     * Define the content format to be shown.
     * @param content
     */
    public void createBodyHtml( String content )
    {
        bodyHtml = Messages.getString( MessagesConstant.WAITFOR_NETPROCESS_EDITOR_BODY_HTML, picKey, content );
    }

}
