package com.loadtrend.web.mobile.client;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.web.servlet.ModelAndView;

public class RequestHttpInvokerServiceExporter extends HttpInvokerServiceExporter {
	
	/* (non-Javadoc)
	 * @see org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ClassNotFoundException {
		RequestHolder.setRequest(request);
		ModelAndView modelAndView = super.handleRequest(request, response);
		RequestHolder.clearup();
		return modelAndView;
	}
}
