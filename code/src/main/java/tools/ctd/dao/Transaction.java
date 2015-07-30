package tools.ctd.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tools.ctd.aop.DefaultInvokeFilter;
import tools.ctd.exception.CTDException;

public class Transaction extends DefaultInvokeFilter {

	private static final Log LOG = LogFactory.getLog(Transaction.class);

	@Override
	public Object invoke() throws CTDException {
		Object retValue = null;
		ConnectionManager.getConnection();
		try {			
			retValue = super.invoke();
		} catch (CTDException e) {
			LOG.error(e);
			ConnectionManager.rollbackConnection();
			throw e;
		}
		
		ConnectionManager.commitConnection();
		return retValue;
	}

}
