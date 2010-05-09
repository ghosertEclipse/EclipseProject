package com.loadtrend.web.mobile.web.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.loadtrend.web.mobile.dao.model.Item;
import com.loadtrend.web.mobile.service.JMobileManager;

public class PushPopupController implements Controller {
    
    private String view = null;
    
    private JMobileManager jmobileManager = null;
    
    public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        Item item = jmobileManager.getItem(id, false);
        Map resultMap = new HashMap();
        resultMap.put("item", item);
        return new ModelAndView(this.view, resultMap);
    }

    public void setJmobileManager(JMobileManager jmobileManager) {
        this.jmobileManager = jmobileManager;
    }

    public void setView(String view) {
        this.view = view;
    }
}
