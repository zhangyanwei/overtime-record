package tools.ctd.service.rest;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

@Path("/jslib")
public class JSFileService {
	
	private static final String DOJO_VERSION = "1.8.3";
	
	private static final String CTD_VERSION = "1.0";
	
	private static final String JQUERY_VERSION = "1.9.1";

	@GET
	@Path("/dojo/{filename:.+}")
	public void getDojoLibPath(@Context ServletContext context, @Context HttpServletResponse resposne, @PathParam("filename") String fileName) {
		StringBuilder urlsb = new StringBuilder();
		urlsb.append(context.getContextPath());
		urlsb.append("/js/dojo/");
		urlsb.append(DOJO_VERSION);
		urlsb.append('/');
		urlsb.append(fileName);
		
		try {
			resposne.sendRedirect(urlsb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@GET
	@Path("/jquery/{filename:.+}")
	public void getJQueryLibPath(@Context ServletContext context, @Context HttpServletResponse resposne, @PathParam("filename") String fileName) {
		StringBuilder urlsb = new StringBuilder();
		urlsb.append(context.getContextPath());
		urlsb.append("/js/jquery/");
		urlsb.append(JQUERY_VERSION);
		urlsb.append('/');
		urlsb.append(fileName);
		
		try {
			resposne.sendRedirect(urlsb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@GET
	@Path("/ctd/{filename:.+}")
	public void getCTDLibPath(@Context ServletContext context, @Context HttpServletResponse resposne, @PathParam("filename") String fileName) {
		StringBuilder urlsb = new StringBuilder();
		urlsb.append(context.getContextPath());
		urlsb.append("/js/ctd/");
		urlsb.append(CTD_VERSION);
		urlsb.append('/');
		urlsb.append(fileName);
		
		try {
			resposne.sendRedirect(urlsb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
