package tools.ctd.logic;

import java.util.List;

import tools.ctd.aop.annotation.Extend;
import tools.ctd.dao.DAOFactoryManager;
import tools.ctd.dao.IDAOFactoryProvider;
import tools.ctd.dao.IProjectDAO;
import tools.ctd.exception.CTDException;
import tools.ctd.logic.security.CheckProjectAccessor;
import tools.ctd.vo.Member;
import tools.ctd.vo.Project;

@Extend(CheckProjectAccessor.class)
public class ProjectAccessor {
	
	public void createProject(Project project) throws CTDException {
		IDAOFactoryProvider df = DAOFactoryManager.getProvider();
		IProjectDAO projectDAO = df.getProjectDAO();
		projectDAO.create(project);
	}
	
	public void updateProject(String projectid, Project project) throws CTDException {
		IDAOFactoryProvider df = DAOFactoryManager.getProvider();
		IProjectDAO projectDAO = df.getProjectDAO();
		projectDAO.update(projectid, project);
	}
	
	public void deleteProject(String projectid) throws CTDException {
		IDAOFactoryProvider df = DAOFactoryManager.getProvider();
		IProjectDAO projectDAO = df.getProjectDAO();
		projectDAO.delete(projectid);
	}
	
	public List<Project> getProjects() throws CTDException {
		IDAOFactoryProvider provider = DAOFactoryManager.getProvider();
		IProjectDAO projectDAO = provider.getProjectDAO();
		return projectDAO.getProjects();
	}
	
	public List<Project> getManagedProjects() throws CTDException {
		IDAOFactoryProvider df = DAOFactoryManager.getProvider();
		IProjectDAO projectDAO = df.getProjectDAO();
		return projectDAO.getManagedProjects();
	}
	
	public void addMember(String projectid, Member member) throws CTDException {
		IDAOFactoryProvider df = DAOFactoryManager.getProvider();
		IProjectDAO projectDAO = df.getProjectDAO();
		projectDAO.addMember(projectid, member);
	}
	
	public void deleteMember(String projectid, String usrid) throws CTDException {
		IDAOFactoryProvider df = DAOFactoryManager.getProvider();
		IProjectDAO projectDAO = df.getProjectDAO();
		projectDAO.deleteMember(projectid, usrid);
	}

	public List<Member> getProjectMembers(String projectid) throws CTDException {
		IDAOFactoryProvider df = DAOFactoryManager.getProvider();
		IProjectDAO projectDAO = df.getProjectDAO();
		return projectDAO.getProjectMembers(projectid);
	}

}
