package tools.ctd.service.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.cache.NoCache;

import tools.ctd.exception.CTDException;
import tools.ctd.logic.CTDAccessorFactory;
import tools.ctd.logic.UserAccessor;
import tools.ctd.vo.Project;

@Path("/user")
@NoCache
public class UserService {
	
	@GET
	@Path("/props/project/default")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Project getDefaultProject() throws CTDException {
		UserAccessor accessor = CTDAccessorFactory.getUserAccessor();
		return accessor.getDefaultProject();
	}
	
	@POST
	@Path("/props/project/default")
	@Consumes(MediaType.APPLICATION_JSON)
	public void setDefaultProject(Project project) throws CTDException {
		UserAccessor accessor = CTDAccessorFactory.getUserAccessor();
		accessor.setDefaultProject(project);
	}

}
