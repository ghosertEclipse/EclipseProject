package com.loadtrend.web.mobile.web.action;


import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.loadtrend.web.mobile.dao.model.Item;
import com.loadtrend.web.mobile.dao.model.Order;
import com.loadtrend.web.mobile.service.JMobileManager;
import com.loadtrend.web.mobile.web.util.HttpClientPost;
import com.loadtrend.web.mobile.web.util.PushProduct;
import com.loadtrend.web.mobile.web.util.PushProductHolder;

public class SynchDataController implements Controller {
    
    private PushProductHolder pushProductHolder = new PushProductHolder();
    
    private HttpClientPost httpClientPost = new HttpClientPost();
    
    private JMobileManager jmobileManager = null;
    
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("GB2312");
        // 判断业务类型的依据,8元包月,1元点播.注意:包月有定制关系了才给你同步的
        String productcode = request.getParameter("productcode");
        String payer = request.getParameter("payer");
        String sender = request.getParameter("sender");
        String linkid = request.getParameter("reserve");
        String param = request.getParameter("param").trim(); // GB2312 mo content
        String optime = request.getParameter("optime"); // 2006-7-11 12:32:34
        
//        System.out.println(productcode);
//        System.out.println(payer);
//        System.out.println(sender);
//        System.out.println(linkid == null ? "null" : linkid);
//        System.out.println(param); 
//        System.out.println(optime);
//        if (true) return new ModelAndView("index");
        
        PushProduct pushProduct = pushProductHolder.getPushProduct(productcode);
        
        if (pushProduct == null) return new ModelAndView("index");
        
        // pay money prompt message.
        String msg = null;
        // 1 rmb
        if (!pushProduct.isMisc()) {
            msg = "恭喜！您已成功订阅手机点点通图铃资源(没有收到不收费,请稍候重试),欢迎继续订阅图铃支持国产软件开发。";
        }
        // 8 rmb
        if (pushProduct.isMiscOrder(param)) {
            msg = "恭喜！您已成为手机点点通的包月用户，可在一个月内免费无限下载软件内的所有图铃资源。";
        }
        
        StringBuffer resultString = null;
        boolean paySuccess = (pushProduct.isMisc() && !pushProduct.isMiscOrder(param)) ? true : false;
        boolean pushSuccess = false;
        Order order = new Order();
        order.setLinkid(linkid);
        order.setOptime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(optime));
        order.setParam(param);
        order.setPayer(payer);
        order.setProductcode(productcode);
        order.setReceiver(payer);
        order.setSender(sender);
        order.setPaySuccess(paySuccess ? "1" : "0");
        order.setPushSuccess(pushSuccess ? "1" : "0");

        // pay the money interface. mo and BL will enter this if clause.
        if (msg != null) {
            resultString = new StringBuffer();
            String url = "http://219.239.88.96:8080/do/sendsms.jsp?payer={0}&smscode={1}&receiver={2}&linkid={3}&msg={4}&sender={5}";
            url = MessageFormat.format(url, new String[]{payer,
                                                         pushProduct.getPaysmscode(),
                                                         payer,
                                                         linkid,
                                                         URLEncoder.encode(msg, "gb2312"),
                                                         sender});
            paySuccess = httpClientPost.post(url, resultString);
            if (paySuccess) {
                // Continue to handle redirect url.
                String location = resultString.toString();
                resultString = new StringBuffer();
                paySuccess = httpClientPost.get(location, resultString);
            }
            order.setPaySuccess(paySuccess ? "1" : "0");
            this.jmobileManager.saveOrder(order);
        }
        
        // push resouce interface. you can implement the send to friends function here. mo and misc but not BL will enter this if clause.
        if (!pushProduct.isMiscOrder(param) && paySuccess) {
            String mobile = null;
            String[] strings = param.split(" ");
            if (strings.length == 2) {
                param = strings[0];
                mobile = strings[1];
            } else {
                mobile = payer;
            }
            Item item = this.jmobileManager.getItem(param, true);
            if (item != null) {
                resultString = new StringBuffer();
                String url = "http://219.239.88.96:8080/do/sendpush.jsp?sernum={0}&smscode={1}&mobile={2}&url={3}&disc={4}&linkid={5}";
                url = MessageFormat.format(url, new String[] {sender,
                                                              pushProduct.getPushsmscode(),
                                                              mobile, // can be different from the payer.
                                                              URLEncoder.encode(item.getUrl(), "GB2312"),
                                                              URLEncoder.encode(item.getName(), "GB2312"),
                                                              linkid});
                pushSuccess = httpClientPost.post(url, resultString);
                order.setItem(item);
                order.setPushSuccess(pushSuccess ? "1" : "0");
                this.jmobileManager.saveOrder(order);
                    
                // just for unique mo data.
                if (!item.getOrders().contains(order) && !pushProduct.isMisc()) {
                    item.setPaytimes(new Integer(item.getPaytimes().intValue() + 1));
                    item.setWeekpaytimes(new Integer(item.getWeekpaytimes().intValue() + 1));
                    this.jmobileManager.saveItem(item);
                }
            }
        }
        System.out.println(payer);
        
        return new ModelAndView("index");
    }

    public void setJmobileManager(JMobileManager jmobileManager) {
        this.jmobileManager = jmobileManager;
    }
}
