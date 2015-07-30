package tools.ctd.env;


@Properties(value="config.properties")
public class CTDProperties {
	
	private String daoFactory;
	
	private String connectionProvider;
	
	private String administrator;
	
	private String tmplRecordDir;
	
	private String ldapHost;
	
	private int ldapPort;
	
	private String ldapUser;
	
	private String ldapPassword;
	
	private String ldapSearchDn;

	public String getDaoFactory() {
		return daoFactory;
	}

	@Property(key="ctd.dao.factory.provider", defaultValue="tools.ctd.dao.impl.DAOFactoryProvider")
	public void setDaoFactory(String daoFactory) {
		this.daoFactory = daoFactory;
	}

	public String getConnectionProvider() {
		return connectionProvider;
	}

	@Property(key="ctd.dao.jdbc.connection.provider", defaultValue="tools.ctd.dao.impl.ConnectionProvider")
	public void setConnectionProvider(String connectionProvider) {
		this.connectionProvider = connectionProvider;
	}

	public String getAdministrator() {
		return administrator;
	}

	@Property(key="ctd.system.admin.userid", defaultValue="admin")
	public void setAdministrator(String administrator) {
		this.administrator = administrator;
	}
	
	public String getTmplRecordDir() {
		return tmplRecordDir;
	}
	
	@Property(key="ctd.tmpl.record.dir", defaultValue="tmplate/record")
	public void setTmplRecordDir(String value) {
		this.tmplRecordDir = value;
	}

	public String getLdapHost() {
		return ldapHost;
	}

	@Property(key="ctd.ldap.host", defaultValue="mpc-auth.neusoft.com")
	public void setLdapHost(String ldapHost) {
		this.ldapHost = ldapHost;
	}

	public int getLdapPort() {
		return ldapPort;
	}

	@Property(key="ctd.ldap.port", defaultValue="389")
	public void setLdapPort(int ldapPort) {
		this.ldapPort = ldapPort;
	}

	public String getLdapUser() {
		return ldapUser;
	}

	@Property(key="ctd.ldap.user", defaultValue="cn=zhangyw,ou=people,dc=neusoft,dc=internal")
	public void setLdapUser(String ldapUser) {
		this.ldapUser = ldapUser;
	}

	public String getLdapPassword() {
		return ldapPassword;
	}

	@Property(key="ctd.ldap.password")
	public void setLdapPassword(String ldapPassword) {
		this.ldapPassword = ldapPassword;
	}

	public String getLdapSearchDn(String userid) {
		if (ldapSearchDn != null) {
			return ldapSearchDn.replace("{userid}", userid);
		}

		return ldapSearchDn;
	}

	@Property(key="ctd.ldap.search.dn", defaultValue="cn={userid},ou=people,dc=neusoft,dc=internal")
	public void setLdapSearchDn(String ldapSearchDn) {
		this.ldapSearchDn = ldapSearchDn;
	}
}
