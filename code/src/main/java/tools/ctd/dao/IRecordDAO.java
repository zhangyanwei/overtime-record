/**
 * 
 */
package tools.ctd.dao;

import java.util.Date;
import java.util.List;

import tools.ctd.exception.CTDException;
import tools.ctd.vo.WorkRecord;

/**
 * @author zhangyw
 * 
 */
public interface IRecordDAO {

	void addRecord(String projectid, String usrid, WorkRecord record)
			throws CTDException;

	void delRecord(String projectid, String usrid, Date workDate)
			throws CTDException;

	void updateRecord(String projectid, String usrid, Date workDate,
			WorkRecord record) throws CTDException;

	WorkRecord getRecord(String projectid, Date workDate) throws CTDException;

	List<WorkRecord> getRecords(String projectid) throws CTDException;

	List<WorkRecord> getRecords(String projectid, String usrid)
			throws CTDException;

	void punchProject(String projectid) throws CTDException;

}
