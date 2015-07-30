package tools.ctd.dao;

import java.sql.Connection;
import java.sql.SQLException;

import tools.ctd.env.CTDProperties;
import tools.ctd.env.RequestEnvironment;
import tools.ctd.env.CoreProperties;
import tools.ctd.exception.CTDException;

public final class ConnectionManager {

	private static String connectionProviderClassName;

	private static IConnectionProvider connProvider = null;

	static {
		try {
			CTDProperties properties = CoreProperties
					.getProperties(CTDProperties.class);
			connectionProviderClassName = properties.getConnectionProvider();
			Class<?> providerClass = Class.forName(connectionProviderClassName);
			connProvider = (IConnectionProvider) providerClass.newInstance();
		} catch (CTDException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws CTDException {
		RequestEnvironment env = RequestEnvironment.getEnv();
		if (env == null) {
			// throw new QAException(QAException.ENV_NOT_EXIST_ERR);
		}

		synchronized (env) {
			if (env.getLevel() == 0) {
				Connection connection = connProvider.getConnection();
				env.setConnection(connection);
			}

			env.increaseLevel();
		}

		return RequestEnvironment.getConnection();
	}


	public static final void closeConnection() throws CTDException {
		RequestEnvironment env = RequestEnvironment.getEnv();
		if (env == null) {
			// throw new QAException(QAException.ENV_NOT_EXIST_ERR);
		}
		synchronized (env) {
			env.decreaseLevel();
			if (env.getLevel() == 0) {
				Connection conn = RequestEnvironment.getConnection();
				try {
					conn.close();
					env.setConnection(null);
				} catch (SQLException se) {
					throw new CTDException(se);
				}
			}
		}
	}

	public static final void commitConnection() throws CTDException {
		RequestEnvironment env = RequestEnvironment.getEnv();
		if (env == null) {
			// throw new QAException(QAException.ENV_NOT_EXIST_ERR);
		}
		synchronized (env) {
			env.decreaseLevel();
			if (env.getLevel() == 0) {
				Connection conn = RequestEnvironment.getConnection();
				try {
					conn.commit();
					conn.close();
					env.setConnection(null);
				} catch (SQLException se) {
					// throw new QAException(QAException.CANNOT_COMMIT_ERR);
				}
			}
		}
	}

	public static final void rollbackConnection() throws CTDException {
		RequestEnvironment env = RequestEnvironment.getEnv();
		if (env == null) {
			// throw new QAException(QAException.ENV_NOT_EXIST_ERR);
		}
		synchronized (env) {
			env.decreaseLevel();
			if (env.getLevel() == 0) {
				Connection conn = RequestEnvironment.getConnection();
				try {
					conn.rollback();
					conn.close();
					// releaseGlobalLockAll(false);
					env.setConnection(null);
				} catch (SQLException se) {
					// logger.fatal(se);
					// throw new QAException(QAException.CANNOT_ROLLBACK_ERR);
				}
			}
		}
	}

}
