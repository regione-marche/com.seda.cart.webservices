package com.seda.cart.webservices.listener;

import javax.servlet.ServletContextEvent;

import com.seda.cart.webservices.config.PrintStrings;
import com.seda.commons.logger.CustomLoggerManager;
import com.seda.commons.logger.LoggerWrapper;
import com.seda.compatibility.SystemVariable;
import com.seda.j2ee5.listener.spi.ApplicationListenerHandler;

public class ApplicationListener extends ApplicationListenerHandler {
	/* (non-Javadoc)
	 * @see com.seda.j2ee5.listener.spi.ApplicationListenerHandler#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	private LoggerWrapper logger =  CustomLoggerManager.get(ApplicationListener.class);

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
			info("lettura variabile ambiente eseguita");
			sv=null;
		} 
		info("rootPath = " + rootPath);
		logger.info("rootPath = " + rootPath);
		if (rootPath!=null) {
			configurePropertiesTree(PrintStrings.TREE_CONTEXT_NAME.format(), rootPath);
			// initialize log4j for this application context
//			configureLogger(PrintStrings.LOGGER_CONTEXT_NAME.format(), 
//					propertiesTree().getProperties(PrintStrings.LOGGER_PROPERTIES_NAME.format()));
		}
		
		info("contextInitialized");
	}
}