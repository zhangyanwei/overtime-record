package tools.ctd.service.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import tools.ctd.service.rest.provider.CTDExceptionMapper;
import tools.ctd.service.rest.provider.DateConverter;

public class CTDRSApplication extends Application {
	
	HashSet<Object> singletons = new HashSet<Object>();
	
	public CTDRSApplication() {
		singletons.add(new JSFileService());
		singletons.add(new EnvironmentService());
		singletons.add(new ProjectService());
		singletons.add(new RecordService());
		singletons.add(new UserService());
		singletons.add(new DateConverter());
		singletons.add(new CTDExceptionMapper());
	}
	
	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
	
}
