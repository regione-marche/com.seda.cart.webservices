/**
 * 
 */
package com.seda.cart.webservices.handler;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.xml.rpc.server.ServletEndpointContext;


/**
 * @author barnocchi
 *
 */
public class WebServiceContext {

	private ServletEndpointContext endPointContext;
	private ServletContext context;
	
	public final ServletEndpointContext getServletEndpointContext() {
		return this.endPointContext;
	}
	public final ServletContext getServletContext() {
		return this.context;
	}	
	
	public WebServiceContext(Object endPoint) {
		this.endPointContext=(ServletEndpointContext)endPoint;
		this.context= endPointContext.getServletContext();
	}
	
	public String showMessageContext() {
		HttpSession session = endPointContext.getHttpSession();
		//System.out.println("???? " + session.getAttribute("callerAddr"));
		return (String) (session.getAttribute("callerAddr"));
		
	}
	
	
}
