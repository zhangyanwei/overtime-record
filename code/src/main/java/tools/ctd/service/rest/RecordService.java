/**
 * 
 */
package tools.ctd.service.rest;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.annotations.cache.NoCache;

import tools.ctd.exception.CTDException;
import tools.ctd.ldap.LDAPAccessor;
import tools.ctd.logic.CTDAccessorFactory;
import tools.ctd.logic.RecordAccessor;
import tools.ctd.vo.WorkRecord;

/**
 * @author zhangyw
 * 
 */
@Path("/record")
@NoCache
public class RecordService {
	
	private static final Log LOG = LogFactory.getLog(RecordService.class);

	@GET
	@Path("/project/{projectid}/member/{usrid}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public List<WorkRecord> getRecords(
			@PathParam("projectid") String projectid,
			@PathParam("usrid") String usrid) throws CTDException {
		RecordAccessor accessor = CTDAccessorFactory.getRecordAccessor();
		return accessor.getRecords(projectid, usrid);
	}
	
	//record/file/project/{projectid}
	@GET
	@Path("/file/project/{projectid}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public void downloadRecords(@PathParam("projectid") String projectid,
			@Context HttpServletResponse resp) throws CTDException {
		RecordAccessor accessor = CTDAccessorFactory.getRecordAccessor();
		try {
			resp.setHeader("Content-Disposition",
					"attachment;filename=" + URLEncoder.encode("项目组加班记录.xlsx","UTF-8"));
			accessor.reportRecord(projectid, resp.getOutputStream());
		} catch (IOException e) {
			LOG.error(e);
			throw new CTDException(e);
		}
	}
	
	// record/project/{projectid}/member/{usrid}?action=download
	@GET
	@Path("/file/project/{projectid}/member/{usrid}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public void downloadRecords(@PathParam("projectid") String projectid,
			@PathParam("usrid") String usrid, @Context HttpServletResponse resp)
			throws CTDException {
		RecordAccessor accessor = CTDAccessorFactory.getRecordAccessor();
		try {
			String userName = LDAPAccessor.getUserName(usrid);
			resp.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(userName + "-加班记录.xlsx","UTF-8"));
			accessor.reportRecord(projectid, usrid, resp.getOutputStream());
		} catch (IOException e) {
			LOG.error(e);
			throw new CTDException(e);
		}
	}

	@GET
	@Path("/project/{projectid}/date/{workDate}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public WorkRecord getRecord(@PathParam("projectid") String projectid,
			@PathParam("workDate") Date workDate) throws CTDException {
		RecordAccessor accessor = CTDAccessorFactory.getRecordAccessor();
		return accessor.getRecord(projectid, workDate);
	}

	@GET
	@Path("/project/{projectid}/date/today")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public WorkRecord getTodayRecord(@PathParam("projectid") String projectid)
			throws CTDException {
		RecordAccessor accessor = CTDAccessorFactory.getRecordAccessor();
		return accessor.getRecord(projectid, new Date());
	}

	@POST
	@Path("/project/{projectid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void punchProject(@PathParam("projectid") String projectid)
			throws CTDException {
		RecordAccessor accessor = CTDAccessorFactory.getRecordAccessor();
		accessor.punchProject(projectid);
	}

	@POST
	@Path("/project/{projectid}/member/{usrid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addRecord(@PathParam("projectid") String projectid,
			@PathParam("usrid") String usrid, WorkRecord record)
			throws CTDException {
		RecordAccessor accessor = CTDAccessorFactory.getRecordAccessor();
		accessor.addRecord(projectid, usrid, record);
	}

	@DELETE
	@Path("/project/{projectid}/member/{usrid}/date/{workDate}")
	public void delRecord(@PathParam("projectid") String projectid,
			@PathParam("usrid") String usrid,
			@PathParam("workDate") Date workDate) throws CTDException {
		RecordAccessor accessor = CTDAccessorFactory.getRecordAccessor();
		accessor.delRecord(projectid, usrid, workDate);
	}

	@POST
	@Path("/project/{projectid}/member/{usrid}/date/{workDate}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateRecord(@PathParam("projectid") String projectid,
			@PathParam("usrid") String usrid,
			@PathParam("workDate") Date workDate, WorkRecord record)
			throws CTDException {
		RecordAccessor accessor = CTDAccessorFactory.getRecordAccessor();
		accessor.updateRecord(projectid, usrid, workDate, record);
	}

}
