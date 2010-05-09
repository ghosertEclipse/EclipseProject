package com.loadtrend.web.mobile.web.util;

import org.apache.commons.lang.builder.ToStringBuilder;

public class PushProduct {

    private String productcode = null;

    private String paysmscode = null;

    private String pushsmscode = null;

    private boolean isMisc = false;

    private String miscOrderSign = null;

    public PushProduct(String productcode, String paysmscode, String pushsmscode,
                       boolean isMisc, String miscOrderSign) {
        this.productcode = productcode;
        this.paysmscode = paysmscode;
        this.pushsmscode = pushsmscode;
        this.isMisc = isMisc;
        this.miscOrderSign = miscOrderSign;
    }

    public boolean isMiscOrder(String moContent) {
        if (this.isMisc && this.miscOrderSign.equalsIgnoreCase(moContent))
            return true;
        return false;
    }

    /**
     * @return Returns the isMisc.
     */
    public boolean isMisc() {
        return isMisc;
    }

    /**
     * @return Returns the paysmscode.
     */
    public String getPaysmscode() {
        return paysmscode;
    }

    /**
     * @return Returns the pushsmscode.
     */
    public String getPushsmscode() {
        return pushsmscode;
    }

    /**
     * @return Returns the productcode.
     */
    public String getProductcode() {
        return productcode;
    }

    public String toString() {
        return new ToStringBuilder(this).append("productcode", productcode)
                .append("paysmscode", paysmscode).append("pushsmscode",
                        pushsmscode).append("isMisc", isMisc).append(
                        "miscOrderSign", miscOrderSign).toString();
    }
}
