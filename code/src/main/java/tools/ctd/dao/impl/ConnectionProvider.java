package tools.ctd.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.postgresql.ds.PGPoolingDataSource;

import tools.ctd.dao.IConnectionProvider;
import tools.ctd.env.CoreProperties;
import tools.ctd.exception.CTDException;

public class ConnectionProvider implements IConnectionProvider {
	
	private static final Log LOG = LogFactory.getLog(ConnectionProvider.class);
	
	/** javax.sql.DataSource的PostgreSQL实现 */
	private static PGPoolingDataSource source = new PGPoolingDataSource();

	static {
		init();
	}

	@Override
	public synchronized Connection getConnection() throws CTDException {
		Connection conn = null;
		try {
			conn = source.getConnection();
			// 不自动提交
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
			LOG.fatal(e);
			throw new CTDException(CTDException.DB_CONNECTION_CREATE_ERROR_1, e);
		}
		
		return conn;
	}

	/**
	 * 初始化数据库连接池，数据库的配置信息请参照{@link DBProperties}类的描述。
	 */
	private static void init() {

		try {
			DBProperties initProperties = CoreProperties
					.getProperties(DBProperties.class);
			String SourceName = "CTD";
			String serverName = initProperties.getServerName();
			String databaseName = initProperties.getDatabaseName();
			String url = initProperties.getUrl();
			String userName = initProperties.getUserName();
			String password = initProperties.getPassword();
			int maxPooledConnections = initProperties.getMaxPooledConnections();
			init(SourceName, serverName, databaseName, url, userName, password,
					maxPooledConnections);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化数据库连接缓存
	 * 
	 * @param url
	 *            PostgreSQL数据库连接URL ex.) "jdbc:oracle:oci8:@croccore"
	 * @param userName
	 *            PostgreSQL数据库用户名
	 * @param password
	 *            PostgreSQL数据库密码
	 */
	private static void init(String SourceName, String serverName,
			String databaseName, String url, String userName, String password,
			int maxPooledConnections) {
		source.setDataSourceName(SourceName);
		source.setServerName(serverName);
		source.setDatabaseName(databaseName);
		source.setUser(userName);
		source.setPassword(password);
		source.setMaxConnections(maxPooledConnections);
	}
}
