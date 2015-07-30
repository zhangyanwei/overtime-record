package tools.ctd.ldap;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;

import tools.ctd.env.CTDProperties;
import tools.ctd.env.CoreProperties;
import tools.ctd.exception.CTDException;

public class LDAPAccessor {
	
	private static final Log LOG = LogFactory.getLog(LDAPAccessor.class);
	
	private static LdapConnection connection;
	
	public synchronized static String getUserName(String userid) throws CTDException {
		
		String userName = userid;
		try {
			CTDProperties props = CoreProperties.getProperties(CTDProperties.class);
			
			String host = props.getLdapHost();
			int port = props.getLdapPort();
			
			if (connection == null) {
				connection = new LdapNetworkConnection( host, port );
			}
			
			String user = props.getLdapUser();
			String pwd = props.getLdapPassword();
			connection.bind( user, pwd );
			
			String lookupdn = props.getLdapSearchDn(userid);
			Entry entry = connection.lookup(lookupdn);
			
			if (entry != null) {
				Attribute snAttr = entry.get("sn");
				userName = snAttr.getString();
			}

		} catch (Exception e) {
			LOG.error(e);
			if (connection != null) {
				try {
					connection.close();
					connection = null;
				} catch (IOException e1) {}
			}
			
			throw new CTDException(e);
		}

		// 为用户ID时，表示该员工离职？
		return userName;
	}
}
