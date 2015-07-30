package tools.ctd.dao;

import java.sql.Connection;

import tools.ctd.exception.CTDException;

/**
 * 
 * 该接口中定义了数据库连接的获取方法。
 * 
 * @author zhangyw
 *
 */
public interface IConnectionProvider {
	
	/**
	 * 取得数据库连接。
	 * 
	 * @return {@link java.sql.Connection}
	 * @throws CTDException 获取数据库连接的过程中出现了异常。
	 */
	public Connection getConnection() throws CTDException;

}
