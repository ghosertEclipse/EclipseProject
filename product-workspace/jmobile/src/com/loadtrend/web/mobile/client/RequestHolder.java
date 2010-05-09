package com.loadtrend.web.mobile.client;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;

public class RequestHolder {
	private static ThreadLocal threadLocal = new ThreadLocal();
	
	private static Set ips = new HashSet();
	
	// Clear the ips at 23 o'clock every day.
	static {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				Date date = new Date();
				if (date.getHours() == 23) {
					ips.clear();
					System.out.println("ips cleared at: " + date.toLocaleString());
				}
			}
		}, 0, 1000 * 60 * 60);
	}
	
	public static void setRequest(HttpServletRequest request) {
		String ip = request.getRemoteAddr();
		if (!ips.contains(ip)) {
		    ips.add(ip);
		    System.out.println(new Date().toLocaleString() + " New user ip: " + request.getRemoteAddr() +
				               " Today user number is: " + ips.size() + " (ips set will be cleared at 23 o'clock)");
		}
		threadLocal.set(request);
	}
	
	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) threadLocal.get();
	}
	
	public static void clearup() {
		threadLocal.set(null);
	}
}
