package tools.ctd.logic.security;

import java.util.Date;

import tools.ctd.aop.DefaultInvokeFilter;
import tools.ctd.exception.CTDException;
import tools.ctd.vo.WorkRecord;

public class CheckRecordAccessor extends DefaultInvokeFilter {

	public void addRecord(String projectid, String usrid, WorkRecord record)
			throws CTDException {
		ACL acl = ACL.initACL();
		acl.setUserId(usrid);
		acl.checkACL(ACL.RECORD_CREATE);
		super.invoke();
	}

	public void delRecord(String projectid, String usrid, Date workDate)
			throws CTDException {
		ACL acl = ACL.initACL();
		acl.setUserId(usrid);
		acl.checkACL(ACL.RECORD_DELETE);
		super.invoke();
	}

	public void updateRecord(String projectid, String usrid, Date workDate,
			WorkRecord record) throws CTDException {
		ACL acl = ACL.initACL();
		acl.setUserId(usrid);
		acl.checkACL(ACL.RECORD_UPDATE);
		super.invoke();
	}

	public Object getRecords(String projectid, String usrid)
			throws CTDException {
		ACL acl = ACL.initACL();
		acl.setProjectId(projectid);
		acl.checkACL(ACL.RECORD_QUERY);
		return super.invoke();
	}

}
