package tools.ctd.dao.impl;

import tools.ctd.env.Properties;
import tools.ctd.env.Property;

@Properties(dir="tools/ctd/dao/impl", value="db.properties")
public class DBProperties {
	
	private String serverName;
	
	private String databaseName;
	
	private String url;
	
	private String userName;
	
	private String password;

	private int maxPooledConnections;
	
	public String getServerName() {
		return serverName;
	}

	@Property(key="ctd.dao.jdbc.serverName")
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	@Property(key="ctd.dao.jdbc.databaseName")
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getUrl() {
		return url;
	}

	@Property(key="ctd.dao.jdbc.url")
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUserName() {
		return userName;
	}
	
	@Property(key="ctd.dao.jdbc.userName")
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	@Property(key="ctd.dao.jdbc.password")
	public void setPassword(String password) {
		this.password = password;
	}

	public int getMaxPooledConnections() {
		return maxPooledConnections;
	}

	@Property(key="ctd.dao.jdbc.maxPooledConnections")
	public void setMaxPooledConnections(int maxPooledConnections) {
		this.maxPooledConnections = maxPooledConnections;
	}

}
