package tools.ctd.logic.security;

import tools.ctd.dao.DAOFactoryManager;
import tools.ctd.dao.IDAOFactoryProvider;
import tools.ctd.dao.IProjectDAO;
import tools.ctd.env.CTDProperties;
import tools.ctd.env.RequestEnvironment;
import tools.ctd.env.CoreProperties;
import tools.ctd.exception.CTDException;

public class ACL {
	
	public static ThreadLocal<ACL> localACL = new ThreadLocal<ACL>();
	
	public static final int PROJECT_CREATE = 1;
	public static final int PROJECT_MEMBER_CREATE = 1 << 1;
	
	public static final int RECORD_QUERY= 1 << 2;
	public static final int RECORD_CREATE = 1 << 3;
	public static final int RECORD_DELETE = 1 << 4;
	public static final int RECORD_UPDATE = 1 << 5;
	
	public static final int ROLE_ADMIN_ACL = PROJECT_CREATE
			| PROJECT_MEMBER_CREATE;
	public static final int ROLE_PROJECT_MANAGER_ACL = PROJECT_MEMBER_CREATE;
	
	private String projectId;
	
	private String userId;
	
	public static ACL initACL() {
		ACL acl = new ACL();
		localACL.set(acl);
		return acl;
	}
	
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getACL() throws CTDException {
		
		int acl = 0;
		RequestEnvironment env = RequestEnvironment.getEnv();
		String userID = env.getUserID();
		
		CTDProperties properties = CoreProperties.getProperties(CTDProperties.class);
		String administrator = properties.getAdministrator();
		if (administrator.equals(userID)) {
			acl |=  ROLE_ADMIN_ACL;
		}
		
		if (projectId != null) {
			IDAOFactoryProvider factory = DAOFactoryManager.getProvider();
			IProjectDAO projectDAO = factory.getProjectDAO();
			if (projectDAO.isManager(projectId)) {
				acl |= ROLE_PROJECT_MANAGER_ACL;
			} else {
				int projectAcl = projectDAO.getProjectACL(projectId);
				acl |= projectAcl;
			}
			
			if (projectDAO.isMember(projectId, userID)) {
				acl |= RECORD_QUERY;
			}
		}
		
		if (userId != null && userId.equals(userID)) {
			acl |= RECORD_CREATE | RECORD_DELETE | RECORD_UPDATE;
		}
		
		return acl;
	}
	
	public boolean hasACL(int acl) throws CTDException {
		int right = getACL();
		return right != 0 && (right & acl) == acl;
	}
	
	public void checkACL(int acl) throws CTDException {
		if (!hasACL(acl)) {
			throw new CTDException(CTDException.ACL_NO_RIGHT_ERROR);
		}
	}

}