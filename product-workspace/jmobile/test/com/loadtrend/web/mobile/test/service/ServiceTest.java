package com.loadtrend.web.mobile.test.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.loadtrend.web.mobile.service.JMobileManager;

import junit.framework.TestCase;

public class ServiceTest extends TestCase {
    
    protected ApplicationContext applicationContext = null;
    
    protected JMobileManager jMobileManager = null;
    
    public ServiceTest() {
        String[] configs = new String[] {"/war/WEB-INF/applicationContext-service.xml",
                                         "/war/WEB-INF/applicationContext-hibernate.xml"};
        applicationContext = new FileSystemXmlApplicationContext(configs);
        jMobileManager = (JMobileManager) applicationContext.getBean("jMobileManager");
    }

}
