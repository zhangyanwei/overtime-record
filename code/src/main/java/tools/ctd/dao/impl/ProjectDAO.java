package tools.ctd.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tools.ctd.aop.annotation.Extend;
import tools.ctd.dao.IProjectDAO;
import tools.ctd.dao.Transaction;
import tools.ctd.env.RequestEnvironment;
import tools.ctd.exception.CTDException;
import tools.ctd.ldap.LDAPAccessor;
import tools.ctd.logic.security.ACL;
import tools.ctd.vo.Member;
import tools.ctd.vo.Project;
import tools.ctd.vo.VOUtils;

@Extend(Transaction.class)
public class ProjectDAO implements IProjectDAO {
	
	private static final String SQL_INSERT_PROJECT = "INSERT INTO ctd_project(" +
			"projectid, projectname, manager, opendate, closedate) " +
			"VALUES (?, ?, ?, ?, ?)";
	
	private static final String SQL_DELETE_PROJECT = "DELETE FROM ctd_project " +
			"WHERE projectid=?";
	
	private static final String SQL_UPDATE_PROJECT = "UPDATE ctd_project " +
			"SET projectid=?, projectname=?, manager=?, opendate=?, closedate=? " +
			"WHERE projectid=?";
	
	private static final String SQL_SELECT_PROJECT = 
			"SELECT p.projectname, p.manager, p.opendate, p.closedate " +
			"FROM ctd_project p " +
			"WHERE p.projectid = ?";
	
	private static final String SQL_SELECT_PROJECTS = 
			"SELECT p.projectid, p.projectname, p.manager, p.opendate, p.closedate " +
			"FROM ctd_project p, ctd_projectmember m " +
			"WHERE p.pid = m.pid " +
			"AND m.usrid = ?";
	
	private static final String SQL_SELECT_ALL_PROJECTS = 
			"SELECT projectid, projectname, manager, opendate, closedate " +
					"FROM ctd_project";
	
	private static final String SQL_SELECT_MANAGED_PROJECTS = 
			"SELECT projectid, projectname, manager, opendate, closedate " +
			"FROM ctd_project WHERE manager=?";
	
	private static final String SQL_IS_PROJECT_MANAGER = 
			"SELECT projectid, projectname, manager, opendate, closedate " +
			"FROM ctd_project WHERE projectid = ? " +
			"AND manager = ?";
	
	private static final String SQL_IS_PROJECT_MEMBER = 
			"SELECT m.pmserial " +
			"FROM ctd_project p, ctd_projectmember m WHERE p.pid = m.pid " +
			"AND p.projectid = ? " +
			"AND m.usrid = ?";
	
	private static final String SQL_INSERT_MEMBER = 
			"INSERT INTO ctd_projectmember(pid, usrid, adddate) " +
			"SELECT pid, ?, ? " +
			"FROM ctd_project WHERE projectid = ?";
	
	private static final String SQL_DELETE_MEMBER = "DELETE FROM ctd_projectmember m " +
			"WHERE m.pid IN (" +
			"	SELECT p.pid FROM ctd_project p WHERE p.projectid = ?" +
			") AND m.usrid = ?";
	
	private static final String SQL_SELECT_PROJECT_MEMBERS = 
			"SELECT m.usrid, m.adddate " +
			"FROM ctd_project p, ctd_projectmember m " +
			"WHERE p.pid = m.pid " +
			"AND p.projectid = ?";
	
	private static final String SQL_SELECT_PROJECT_ACL = "SELECT a.acl " +
			"FROM ctd_acl a, ctd_project p, ctd_projectmember m " +
			"WHERE a.pmserial =  m.pmserial " +
			"AND p.pid = m.pid " +
			"AND p.projectid = ? " +
			"AND m.usrid = ?";

	@Override
	public void create(Project project) throws CTDException {
		Connection connection = RequestEnvironment.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(SQL_INSERT_PROJECT);
			statement.setString(1, project.getId());
			statement.setString(2, project.getName());
			statement.setString(3, project.getManager());
			statement.setDate(4, VOUtils.convertToSQLDate(project.getOpenDate()));
			statement.setDate(5, VOUtils.convertToSQLDate(project.getCloseDate()));
			
			int rowCount = statement.executeUpdate();
			if (rowCount < 1) {
				throw new CTDException(CTDException.DB_SQL_INSERT_PROJECT_ERROR);
			}
		} catch (SQLException e) {
			throw new CTDException(CTDException.DB_SQL_ERROR_1, e);
		}
	}
	
	@Override
	public void delete(String projectid) throws CTDException {
		Connection connection = RequestEnvironment.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(SQL_DELETE_PROJECT);
			statement.setString(1, projectid);
			
			int rowCount = statement.executeUpdate();
			if (rowCount < 1) {
				throw new CTDException(CTDException.DB_SQL_DELETE_PROJECT_ERROR);
			}
		} catch (SQLException e) {
			throw new CTDException(CTDException.DB_SQL_ERROR_1, e);
		}
	}

	@Override
	public void update(String projectid, Project project) throws CTDException {
		Connection connection = RequestEnvironment.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_PROJECT);
			statement.setString(1, project.getId());
			statement.setString(2, project.getName());
			statement.setString(3, project.getManager());
			statement.setDate(4, VOUtils.convertToSQLDate(project.getOpenDate()));
			statement.setDate(5, VOUtils.convertToSQLDate(project.getCloseDate()));
			statement.setString(6, projectid);
			
			int rowCount = statement.executeUpdate();
			if (rowCount < 1) {
				throw new CTDException(CTDException.DB_SQL_UPDATE_PROJECT_ERROR);
			}
		} catch (SQLException e) {
			throw new CTDException(CTDException.DB_SQL_ERROR_1, e);
		}
	}
	
	@Override
	public Project getProject(String projectid) throws CTDException {
		// SQL_SELECT_PROJECT
		Connection connection = RequestEnvironment.getConnection();
		try {			
			PreparedStatement statement = connection.prepareStatement(SQL_SELECT_PROJECT);
			statement.setString(1, projectid);
			
			ResultSet query = statement.executeQuery();
			Project project = new Project();
			if (query.next()) {
				project.setId(projectid);
				project.setName(query.getString(1));
				project.setManager(query.getString(2));
				project.setOpenDate(query.getDate(3));
				project.setCloseDate(query.getDate(4));
			} else {
				throw new CTDException(CTDException.DB_SQL_SElECT_PROJECT_ERROR);
			}
			
			return project;
		} catch (SQLException e) {
			throw new CTDException(CTDException.DB_SQL_ERROR_1, e);
		}
	}

	@Override
	public List<Project> getProjects() throws CTDException {
		Connection connection = RequestEnvironment.getConnection();
		try {
			List<Project> projects = new ArrayList<Project>();
			
			PreparedStatement statement = connection.prepareStatement(SQL_SELECT_PROJECTS);
			RequestEnvironment env = RequestEnvironment.getEnv();
			statement.setString(1, env.getUserID());
			
			ResultSet query = statement.executeQuery();
			while(query.next()) {
				Project project = new Project();
				project.setId(query.getString(1));
				project.setName(query.getString(2));
				project.setManager(query.getString(3));
				project.setOpenDate(query.getDate(4));
				project.setCloseDate(query.getDate(5));
				
				projects.add(project);
			}
			
			return projects;
		} catch (SQLException e) {
			throw new CTDException(CTDException.DB_SQL_ERROR_1, e);
		}
	}

	@Override
	public List<Project> getManagedProjects() throws CTDException {
		Connection connection = RequestEnvironment.getConnection();
		try {
			List<Project> projects = new ArrayList<Project>();
			
			PreparedStatement statement;
			ACL acl = ACL.initACL();
			if (acl.hasACL(ACL.ROLE_ADMIN_ACL)) {
				statement = connection.prepareStatement(SQL_SELECT_ALL_PROJECTS);
			} else {
				RequestEnvironment env = RequestEnvironment.getEnv();
				statement = connection.prepareStatement(SQL_SELECT_MANAGED_PROJECTS);
				statement.setString(1, env.getUserID());
			}
			
			ResultSet query = statement.executeQuery();
			while(query.next()) {
				Project project = new Project();
				project.setId(query.getString(1));
				project.setName(query.getString(2));
				project.setManager(query.getString(3));
				project.setOpenDate(query.getDate(4));
				project.setCloseDate(query.getDate(5));
				
				projects.add(project);
			}
			
			return projects;
		} catch (SQLException e) {
			throw new CTDException(CTDException.DB_SQL_ERROR_1, e);
		}
	}
	
	@Override
	public boolean isManager(String projectid) throws CTDException {
		Connection connection = RequestEnvironment.getConnection();
		try {
			RequestEnvironment env = RequestEnvironment.getEnv();
			PreparedStatement statement = connection.prepareStatement(SQL_IS_PROJECT_MANAGER);
			statement.setString(1, projectid);
			statement.setString(2, env.getUserID());
			
			ResultSet query = statement.executeQuery();
			return query.next();
		} catch (SQLException e) {
			throw new CTDException(CTDException.DB_SQL_ERROR_1, e);
		}
	}
	
	@Override
	public boolean isMember(String projectid, String usrid) throws CTDException {
		//SQL_IS_PROJECT_MEMBER
		Connection connection = RequestEnvironment.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(SQL_IS_PROJECT_MEMBER);
			statement.setString(1, projectid);
			statement.setString(2, usrid);
			
			ResultSet query = statement.executeQuery();
			return query.next();
		} catch (SQLException e) {
			throw new CTDException(CTDException.DB_SQL_ERROR_1, e);
		}
	}
	
	@Override
	public void addMember(String projectid, Member member) throws CTDException {
		Connection connection = RequestEnvironment.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(SQL_INSERT_MEMBER);
			statement.setString(1, member.getUserid());
			java.sql.Date addDate = VOUtils.convertToSQLDate(member.getAddDate());
			statement.setDate(2, addDate == null ? VOUtils.convertToSQLDate(new Date()) : addDate);
			statement.setString(3, projectid);
			
			int rowCount = statement.executeUpdate();
			if (rowCount < 1) {
				throw new CTDException(CTDException.DB_SQL_INSERT_PROJECT_ERROR);
			}
		} catch (SQLException e) {
			throw new CTDException(CTDException.DB_SQL_ERROR_1, e);
		}
	}

	@Override
	public void deleteMember(String projectid, String usrid)
			throws CTDException {
		Connection connection = RequestEnvironment.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(SQL_DELETE_MEMBER);
			statement.setString(1, projectid);
			statement.setString(2, usrid);
			
			int rowCount = statement.executeUpdate();
			if (rowCount < 1) {
				throw new CTDException(CTDException.DB_SQL_DELETE_MEMBER_ERROR);
			}
		} catch (SQLException e) {
			throw new CTDException(CTDException.DB_SQL_ERROR_1, e);
		}
	}

	@Override
	public List<Member> getProjectMembers(String projectid) throws CTDException {
		Connection connection = RequestEnvironment.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(SQL_SELECT_PROJECT_MEMBERS);
			statement.setString(1, projectid);
			
			List<Member> members = new ArrayList<Member>();
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				Member member = new Member();
				String userid = result.getString(1);
				member.setUserid(userid);
				String userName = LDAPAccessor.getUserName(userid);
				member.setUserName(userName);
				member.setAddDate(result.getDate(2));
				members.add(member);
			}
			
			return members;
		} catch (SQLException e) {
			throw new CTDException(CTDException.DB_SQL_ERROR_1, e);
		}
	}

	@Override
	public int getProjectACL(String projectid) throws CTDException {
		Connection connection = RequestEnvironment.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(SQL_SELECT_PROJECT_ACL);
			statement.setString(1, projectid);
			
			RequestEnvironment env = RequestEnvironment.getEnv();
			statement.setString(2, env.getUserID());
			
			ResultSet result = statement.executeQuery();
			return result.next() ? result.getInt(1) : 0;
		} catch (SQLException e) {
			throw new CTDException(CTDException.DB_SQL_ERROR_1, e);
		}
	}

}
