package com.seda.cart.facade.handler;

import java.sql.Connection;
import java.util.Properties;
import org.apache.log4j.Logger;

import com.seda.cart.webservices.config.PrintStrings;
import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.compatibility.SystemVariable;
import com.seda.data.dao.DAOHelper;
import com.seda.j2ee5.jndi.JndiProxy;
import com.seda.j2ee5.jndi.JndiProxyException;

/**
 * @author SEDA
 * 
 */
public abstract class BaseFacadeHandler {

	protected PropertiesTree propertiesTree;
	
	protected Logger logger;
	/*
	protected SessionContext ctx;
	
	public void ejbActivate() { }
	public void ejbPassivate() { }
	public void ejbRemove() { ctx = null; }
	
	public void setSessionContext(SessionContext arg0) {
		try {
			ctx = arg0;
			applicationStartup();
		} catch (Exception e) {
			System.out.println("%%%% Error Inizialize Properties SessionBean %%%% " + e.getMessage());
		}
	}*/
	
	//public BaseFacadeHandler() throws Exception {
	//	super();
	public BaseFacadeHandler(PropertiesTree propertiesTree) throws Exception {
		super();
		this.propertiesTree = propertiesTree;
		
	//fine LP PG200070 - 20200812
		applicationStartup();
	}
	
	/**
	 * applicationStartup()
	 */
	protected void applicationStartup() throws Exception {
		//inizio LP PG200070 - 20200812
		//// get SystemVariable handler
		//SystemVariable sv = new SystemVariable();
		//// load the tree properties for this application 
		//String propertiesRootPath = sv.getSystemVariableValue(PrintStrings.ROOT.format());
		//fine LP PG200070 - 20200812
		try {
			//inizio LP PG200070 - 20200812
			//propertiesTree = new PropertiesTree(propertiesRootPath);
			//fine LP PG200070 - 20200812
			//String catalogName = PropertiesPath.baseCatalogName.format();

			/* we initialize logger */
			//inizio LP PG200070 - 20200812
			//Properties log4jConfiguration = propertiesTree.getProperties(PropertiesPath.baseLogger.format());                         
			//fine LP PG200070 - 20200812
//            LoggerHierarchyServer loggerHierarchyServer = new LoggerHierarchyServer();
//            Hierarchy hierarchy = loggerHierarchyServer.configure(log4jConfiguration);
//            logger = hierarchy.getLogger("FILE");
            
            logger = Logger.getLogger("FILE");
            
            logger.info("<com.seda.payer.facade  - applicationStartup>");
            
            
		} catch (Exception e) { throw e; }
    }
	/**
	 * @return the dataSourceName
	 */
/*	public String getDataSourceName() {
		return dataSourceName;
	}
	*/
	public String getDataSourceName(String dbSchemaCodSocieta) {
		return propertiesTree.getProperty(PropertiesPath.baseCatalogJndiAlias.format(dbSchemaCodSocieta));
	}
	public Connection getConnection(String dbSchemaCodSocieta) throws JndiProxyException {
		return new JndiProxy().getSqlConnection(getInitialContextFactory(dbSchemaCodSocieta),
					getDataSourceName(dbSchemaCodSocieta), true);
	}
	public Connection getConnection(String dbSchemaCodSocieta, boolean autoCommit) throws JndiProxyException {
		return new JndiProxy().getSqlConnection(getInitialContextFactory(dbSchemaCodSocieta),
					getDataSourceName(dbSchemaCodSocieta), autoCommit);
	}
	public String getInitialContextFactory(String dbSchemaCodSocieta) {
		return propertiesTree.getProperty(PropertiesPath.baseCatalogInitialContextFactory.format(dbSchemaCodSocieta));
	}
	public String getSchema(String dbSchemaCodSocieta) {
		return propertiesTree.getProperty(PropertiesPath.baseCatalogSchema.format(dbSchemaCodSocieta));
	}
	/**
	 * @return the connection
	 *//*
	public Connection getConnection(boolean autoCommit) throws JndiProxyException {
		return new JndiProxy().getSqlConnection(this.initialContextFactory, this.dataSourceName, autoCommit);
	}
	
	
	*//**
	 * @return the initialContextFactory
	 *//*
	public String getInitialContextFactory() {
		return initialContextFactory;
	}
	*//**
	 * @return the schema
	 *//*
	public String getSchema() {
		return schema;
	}*/
	
	/**
	 * @param connection
	 */
	public void closeConnection(Connection connection) {
		DAOHelper.closeIgnoringException(connection);
	}

}