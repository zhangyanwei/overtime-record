package tools.ctd.service.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.cache.NoCache;

import tools.ctd.exception.CTDException;
import tools.ctd.logic.CTDAccessorFactory;
import tools.ctd.logic.ProjectAccessor;
import tools.ctd.vo.Member;
import tools.ctd.vo.Project;

@Path("/project")
@NoCache
public class ProjectService {

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public void createProject(Project project) throws CTDException {
		ProjectAccessor accessor = CTDAccessorFactory.getProjectAccessor();
		accessor.createProject(project);
	}

	@POST
	@Path("/{projectid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateProject(@PathParam("projectid") String projectid,
			Project project) throws CTDException {
		ProjectAccessor accessor = CTDAccessorFactory.getProjectAccessor();
		accessor.updateProject(projectid, project);
	}

	@DELETE
	@Path("/{projectid}")
	public void deleteProject(@PathParam("projectid") String projectid)
			throws CTDException {
		ProjectAccessor accessor = CTDAccessorFactory.getProjectAccessor();
		accessor.deleteProject(projectid);
	}

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public List<Project> getProjects() throws CTDException {
		ProjectAccessor accessor = CTDAccessorFactory.getProjectAccessor();
		return accessor.getProjects();
	}

	@GET
	@Path("/managed")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public List<Project> getManagedProjects() throws CTDException {
		ProjectAccessor accessor = CTDAccessorFactory.getProjectAccessor();
		return accessor.getManagedProjects();
	}
	
	@POST
	@Path("/{projectid}/member")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addMember(@PathParam("projectid") String projectid,
			Member member) throws CTDException {
		ProjectAccessor accessor = CTDAccessorFactory.getProjectAccessor();
		accessor.addMember(projectid, member);
	}

	@DELETE
	@Path("/{projectid}/member/{usrid}")
	public void deleteMember(@PathParam("projectid") String projectid,
			@PathParam("usrid") String usrid) throws CTDException {
		ProjectAccessor accessor = CTDAccessorFactory.getProjectAccessor();
		accessor.deleteMember(projectid, usrid);
	}

	@GET
	@Path("/{projectid}/member")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public List<Member> getProjectMembers(
			@PathParam("projectid") String projectid) throws CTDException {
		ProjectAccessor accessor = CTDAccessorFactory.getProjectAccessor();
		return accessor.getProjectMembers(projectid);
	}

}
