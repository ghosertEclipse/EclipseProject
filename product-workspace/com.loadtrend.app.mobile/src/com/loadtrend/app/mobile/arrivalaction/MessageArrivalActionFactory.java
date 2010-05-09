package com.loadtrend.app.mobile.arrivalaction;

import java.util.HashMap;

public class MessageArrivalActionFactory {
    private static MessageArrivalActionFactory factory = null;
    
    private HashMap map = new HashMap();
    
    private MessageArrivalActionFactory() {
    }
    
    public static MessageArrivalActionFactory getInstance() {
        if (factory == null) {
            factory = new MessageArrivalActionFactory();
        }
        return factory;
    }
    
    public MessageArrivalAction getAction(String fullname) {
        try {
            if (map.get(fullname) != null) return (MessageArrivalAction)map.get(fullname);
            MessageArrivalAction action = (MessageArrivalAction) Class.forName(fullname).newInstance();
            map.put(fullname, action);
            return action;
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        
    }
}
