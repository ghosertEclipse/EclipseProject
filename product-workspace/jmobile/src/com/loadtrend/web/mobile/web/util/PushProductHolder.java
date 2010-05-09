package com.loadtrend.web.mobile.web.util;

import java.util.HashMap;

public class PushProductHolder {
    
    private HashMap hashMap = new HashMap();
    
    public PushProductHolder() {
        PushProduct product = new PushProduct("10001028", "111246", "111247", false, null);
        hashMap.put(product.getProductcode(), product);
        product = new PushProduct("10001027", "111244", "111245", true, "BL");
        hashMap.put(product.getProductcode(), product);
    }
    
    public PushProduct getPushProduct(String productcode) {
        return (PushProduct) hashMap.get(productcode);
    }
}
