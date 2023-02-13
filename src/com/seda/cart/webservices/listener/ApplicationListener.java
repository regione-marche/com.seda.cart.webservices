package com.seda.cart.webservices.listener;

import javax.servlet.ServletContextEvent;

import com.seda.compatibility.SystemVariable;
import com.seda.j2ee5.listener.spi.ApplicationListenerHandler;
import com.seda.cart.webservices.config.PrintStrings;

public class ApplicationListener extends ApplicationListenerHandler {
	/* (non-Javadoc)
	 * @see com.seda.j2ee5.listener.spi.ApplicationListenerHandler#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent event) { }
	/* (non-Javadoc)
	 * @see com.seda.j2ee5.listener.spi.ApplicationListenerHandler#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		
		String rootPath = event.getServletContext().getInitParameter(PrintStrings.CONFIG_FILE.format());
		if (rootPath==null) {
			SystemVariable sv = new SystemVariable();
			rootPath=sv.getSystemVariableValue(PrintStrings.ROOT.format());
			loggerServer().info("lettura variabile ambiente eseguita");
			sv=null;
		} 
		loggerServer().info("rootPath = " + rootPath);
		if (rootPath!=null) {
			configurePropertiesTree(PrintStrings.TREE_CONTEXT_NAME.format(), rootPath);
			// initialize log4j for this application context
//			configureLogger(PrintStrings.LOGGER_CONTEXT_NAME.format(), 
//					propertiesTree().getProperties(PrintStrings.LOGGER_PROPERTIES_NAME.format()));
		}
		
		loggerServer().info("contextInitialized");
	}
}