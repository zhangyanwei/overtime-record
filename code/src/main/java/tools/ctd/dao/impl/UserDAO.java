package tools.ctd.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import tools.ctd.aop.annotation.Extend;
import tools.ctd.dao.IUserDAO;
import tools.ctd.dao.Transaction;
import tools.ctd.env.RequestEnvironment;
import tools.ctd.exception.CTDException;
import tools.ctd.vo.Project;

@Extend(Transaction.class)
public class UserDAO implements IUserDAO {
	
	private static final String SQL_SELECT_DEFAULT_PROJECT = 
			"SELECT p.projectid, p.projectname, p.manager, p.opendate, p.closedate " +
			"FROM ctd_project p, ctd_usrprops u " +
			"WHERE u.usrid = ? " +
			"AND u.propname = ? " +
			"AND p.projectid = u.propvalue";
	
	private static final String SQL_UPDATE_DEFAULT_PROJECT =
			"UPDATE ctd_usrprops SET propvalue= ? " +
			"WHERE usrid= ? " +
			"AND propname='punch.projectid.default'";
	
	private static final String SQL_CHECK_PROJECT_MEMBER = 
			"SELECT pmserial FROM ctd_project p, ctd_projectmember m " +
			"WHERE p.pid = m.pid " +
			"AND p.projectid = ? " +
			"AND m.usrid = ?";
	
	private static final String SQL_INSERT_DEFAULT_PROJECT = 
			"INSERT INTO ctd_usrprops(usrid, propname, propvalue) " +
			"VALUES (?, ?, ?)";

	@Override
	public Project getDefaultProject() throws CTDException {
		Connection connection = RequestEnvironment.getConnection();
		try {
			RequestEnvironment env = RequestEnvironment.getEnv();
			PreparedStatement statement = connection.prepareStatement(SQL_SELECT_DEFAULT_PROJECT);
			statement.setString(1, env.getUserID());
			statement.setString(2, "punch.projectid.default");

			Project project = null;
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				project = new Project();
				project.setId(rs.getString(1));
				project.setName(rs.getString(2));
				project.setManager(rs.getString(3));
				project.setOpenDate(rs.getDate(4));
				project.setCloseDate(rs.getDate(5));
			}
			
			return project;
		} catch (SQLException e) {
			throw new CTDException(CTDException.DB_SQL_ERROR_1, e);
		}
	}

	@Override
	public void setDefaultProject(Project project) throws CTDException {
		Connection connection = RequestEnvironment.getConnection();
		try {
			RequestEnvironment env = RequestEnvironment.getEnv();
			PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_DEFAULT_PROJECT);
			statement.setString(1, project.getId());
			statement.setString(2, env.getUserID());
			int rowCount = statement.executeUpdate();
			if (rowCount < 1) {
				statement.close();
				statement = connection.prepareStatement(SQL_CHECK_PROJECT_MEMBER);
				statement.setString(1, project.getId());
				statement.setString(2, env.getUserID());
				ResultSet rs = statement.executeQuery();
				if (rs.next()) {
					statement.close();
					statement = connection.prepareStatement(SQL_INSERT_DEFAULT_PROJECT);
					statement.setString(1, env.getUserID());
					statement.setString(2, "punch.projectid.default");
					statement.setString(3, project.getId());
					rowCount = statement.executeUpdate();
				}
			}
			
			if (rowCount < 1) {
				throw new CTDException(CTDException.DB_SQL_INSERT_USER_DEFAULT_PROJECT_ERROR);
			}
		} catch (SQLException e) {
			throw new CTDException(CTDException.DB_SQL_ERROR_1, e);
		}
	}

}
