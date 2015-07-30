package tools.ctd.service.rest;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.cache.NoCache;

import tools.ctd.env.RequestEnvironment;
import tools.ctd.vo.UserEnvironment;

@Path("/env")
@NoCache
public class EnvironmentService {
	
	@GET
	@Path("/user")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public UserEnvironment getUserEnv() {
		RequestEnvironment env = RequestEnvironment.getEnv();
		UserEnvironment userEnv = new UserEnvironment();
		userEnv.setUserID(env.getUserID());
		userEnv.setUserName(env.getUserName());
		userEnv.setUserIP(env.getUserIP());
		return userEnv;
	}
	
	@GET
	@Path("/time/now")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Date getCurrentTime() {
		Date now = new Date();
		//return DateFormat.formatTime(now);
		return now;
	}

}
