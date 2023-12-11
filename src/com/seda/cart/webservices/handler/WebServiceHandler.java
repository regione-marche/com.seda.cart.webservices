package com.seda.cart.webservices.handler;

import java.util.Iterator;
import java.util.Properties;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.rpc.server.ServletEndpointContext;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;

import com.seda.cart.facade.handler.PropertiesPath;
import com.seda.cart.webservices.config.PrintStrings;
import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.webservice.spi.JaxRpc10WebServiceHandler;

public abstract class WebServiceHandler extends JaxRpc10WebServiceHandler {

	protected String loggerContextName = PrintStrings.LOGGER_CONTEXT_NAME.format();
	protected String treeContextName = PrintStrings.TREE_CONTEXT_NAME.format();
	protected String dbSchemaCodSocieta; 
	//inizio LP PG200070 - 20200812
	protected PropertiesTree configuration;
	
	//fine LP PG200070 - 20200812
	
	protected WebServiceContext webServiceContext;
	protected WebServiceContext getWebServiceContext() {
		return this.webServiceContext;
	}
	
	@Override
	public void init(Object endPointContext) throws ServiceException {

		super.init(endPointContext);
		webServiceContext = new WebServiceContext(endPointContext);
    	// logger(loggerContextName);
    	propertiesTree(treeContextName);
    	
    	setDbSchemaCodSocieta(endPointContext);
    	//inizio LP PG200070 - 20200812
    	configuration = propertiesTree();
    	
    	//fine LP PG200070 - 20200812
	}

	private void setDbSchemaCodSocieta (Object endPointContext) {
		ServletEndpointContext ctx=null;
		
		if (javax.xml.rpc.server.ServletEndpointContext.class.isInstance(endPointContext))
			ctx = (ServletEndpointContext) endPointContext;

		if (ctx != null) {


			SOAPMessageContext mc = (SOAPMessageContext)ctx.getMessageContext();
			// process SOAP header as shown in the message handler


			try {
				SOAPHeader header = mc.getMessage().getSOAPPart().getEnvelope().getHeader();

				Iterator headers = header.examineAllHeaderElements(); //header.extractHeaderElements("http://schemas.xmlsoap.org/soap/actor/next");
				while (headers.hasNext()) {
					SOAPHeaderElement he = (SOAPHeaderElement)headers.next();
					if(he.getNodeName().equals("dbSchemaCodSocieta"))
						dbSchemaCodSocieta=new String(he.getValue());

				} 



			} catch (SOAPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}