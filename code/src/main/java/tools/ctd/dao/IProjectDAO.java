package tools.ctd.dao;

import java.util.List;

import tools.ctd.exception.CTDException;
import tools.ctd.vo.Member;
import tools.ctd.vo.Project;

public abstract interface IProjectDAO {

	void create(Project project) throws CTDException;
	
	void delete(String projectid) throws CTDException;
	
	void update(String projectid, Project project) throws CTDException;
	
	Project getProject(String projectid) throws CTDException;
	
	List<Project> getProjects() throws CTDException;
	
	List<Project> getManagedProjects() throws CTDException;
	
	boolean isManager(String projectid) throws CTDException;
	
	boolean isMember(String projectid, String usrid) throws CTDException;

	void addMember(String projectid, Member member) throws CTDException;
	
	void deleteMember(String projectid, String usrid) throws CTDException;

	List<Member> getProjectMembers(String projectid) throws CTDException;
	
	int getProjectACL(String projectid) throws CTDException;	
	
}
