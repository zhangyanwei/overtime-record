package tools.ctd.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tools.ctd.env.CTDProperties;
import tools.ctd.env.CoreProperties;
import tools.ctd.exception.CTDException;

public final class DAOFactoryManager {
	
	private static final Log LOG = LogFactory.getLog(DAOFactoryManager.class);
	
	private static IDAOFactoryProvider instance;
	
	public static synchronized IDAOFactoryProvider getProvider() throws CTDException {
		
		if (instance == null) {
			CTDProperties properties = CoreProperties.getProperties(CTDProperties.class);
			String daoFactory = properties.getDaoFactory();
			try {
				Class<?> factory = Class.forName(daoFactory);
				instance = (IDAOFactoryProvider) factory.newInstance();
			} catch (ClassNotFoundException e) {
				LOG.fatal(e);
				throw new CTDException(e);
			} catch (InstantiationException e) {
				LOG.fatal(e);
				throw new CTDException(e);
			} catch (IllegalAccessException e) {
				LOG.fatal(e);
				throw new CTDException(e);
			}
		}
		
		return instance;		
	}

}
