package tools.ctd.logic.security;

import tools.ctd.aop.DefaultInvokeFilter;
import tools.ctd.exception.CTDException;
import tools.ctd.vo.Member;
import tools.ctd.vo.Project;

public class CheckProjectAccessor extends DefaultInvokeFilter {

	public void createProject(Project project) throws CTDException {
		ACL acl = ACL.initACL();
		acl.checkACL(ACL.PROJECT_CREATE);
		super.invoke();
	}

	public void deleteProject(String projectid) throws CTDException {
		ACL acl = ACL.initACL();
		acl.checkACL(ACL.PROJECT_CREATE);
		super.invoke();
	}

	public void updateProject(String projectid, Project project)
			throws CTDException {
		ACL acl = ACL.initACL();
		acl.checkACL(ACL.PROJECT_CREATE);
		super.invoke();
	}

	public void addMember(String projectid, Member member) throws CTDException {
		ACL acl = ACL.initACL();
		acl.setProjectId(projectid);
		acl.checkACL(ACL.PROJECT_MEMBER_CREATE);
		super.invoke();
	}

	public void deleteMember(String projectid, String usrid)
			throws CTDException {
		ACL acl = ACL.initACL();
		acl.setProjectId(projectid);
		acl.checkACL(ACL.PROJECT_MEMBER_CREATE);
		super.invoke();
	}

}
