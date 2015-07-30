/**
 *
 */
package tools.ctd.logic;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import tools.ctd.aop.annotation.Extend;
import tools.ctd.dao.DAOFactoryManager;
import tools.ctd.dao.IDAOFactoryProvider;
import tools.ctd.dao.IRecordDAO;
import tools.ctd.exception.CTDException;
import tools.ctd.logic.report.RecordReport;
import tools.ctd.vo.WorkRecord;

/**
 * @author zhangyw
 * 
 */
@Extend({ tools.ctd.logic.params.CheckRecordAccessor.class,
		tools.ctd.logic.security.CheckRecordAccessor.class })
public class RecordAccessor {

	public void addRecord(String projectid, String usrid, WorkRecord record)
			throws CTDException {
		IDAOFactoryProvider provider = DAOFactoryManager.getProvider();
		IRecordDAO recordDAO = provider.getRecordDAO();
		recordDAO.addRecord(projectid, usrid, record);
	}

	public void delRecord(String projectid, String usrid, Date workDate)
			throws CTDException {
		IDAOFactoryProvider provider = DAOFactoryManager.getProvider();
		IRecordDAO recordDAO = provider.getRecordDAO();
		recordDAO.delRecord(projectid, usrid, workDate);
	}

	public void updateRecord(String projectid, String usrid, Date workDate,
			WorkRecord record) throws CTDException {
		IDAOFactoryProvider provider = DAOFactoryManager.getProvider();
		IRecordDAO recordDAO = provider.getRecordDAO();
		recordDAO.updateRecord(projectid, usrid, workDate, record);
	}

	public WorkRecord getRecord(String projectid, Date workDate)
			throws CTDException {
		IDAOFactoryProvider provider = DAOFactoryManager.getProvider();
		IRecordDAO recordDAO = provider.getRecordDAO();
		return recordDAO.getRecord(projectid, workDate);
	}

	public List<WorkRecord> getRecords(String projectid, String usrid)
			throws CTDException {
		IDAOFactoryProvider provider = DAOFactoryManager.getProvider();
		IRecordDAO recordDAO = provider.getRecordDAO();
		return recordDAO.getRecords(projectid, usrid);
	}

	public void punchProject(String projectid) throws CTDException {
		IDAOFactoryProvider provider = DAOFactoryManager.getProvider();
		IRecordDAO recordDAO = provider.getRecordDAO();
		recordDAO.punchProject(projectid);
	}
	
	public void reportRecord(String projectid, OutputStream out)
			throws CTDException {
		RecordReport report = new RecordReport();
		report.report(projectid, out);
	}

	public void reportRecord(String projectid, String usrid, OutputStream out)
			throws CTDException {
		RecordReport report = new RecordReport();
		report.report(projectid, usrid, out);
	}

}
