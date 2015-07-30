/**
 * 
 */
package tools.ctd.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tools.ctd.aop.annotation.Extend;
import tools.ctd.dao.IRecordDAO;
import tools.ctd.dao.Transaction;
import tools.ctd.env.RequestEnvironment;
import tools.ctd.exception.CTDException;
import tools.ctd.ldap.LDAPAccessor;
import tools.ctd.vo.ActualRecord;
import tools.ctd.vo.PlanRecord;
import tools.ctd.vo.VOUtils;
import tools.ctd.vo.WorkRecord;

/**
 * @author zhangyw
 *
 */
@Extend(Transaction.class)
public class RecordDAO implements IRecordDAO {
	
	private static final String SQL_ADD_PLAN_RECORD = 
			"INSERT INTO ctd_planrecord(" +
			"pmserial, workdate, begintime, endtime, holiday, taxi, applied) " +
			"SELECT pmserial, ?, ?, ?, ?, ?, ? " +
			"FROM ctd_project p, ctd_projectmember m " +
			"WHERE p.pid = m.pid AND p.projectid = ? AND m.usrid = ?";
	
	private static final String SQL_UPDATE_PLAN_RECORD = 
			"UPDATE ctd_planrecord " +
			"SET begintime=?, endtime=?, holiday=?, taxi=?, applied=? " +
			"WHERE pmserial IN (" +
			"SELECT pmserial FROM ctd_project p, ctd_projectmember m " +
			"WHERE p.pid = m.pid AND p.projectid = ? AND m.usrid = ?) " +
			"AND workdate = ?";
	
	private static final String SQL_UPDATE_ACTUAL_RECROD =
			"UPDATE ctd_actualrecord " +
			"SET begintime=?, endtime=?, taxitimebegin=?, taxitimeend=?, " +
			"taxistartlocation=?, taxiendlocation=?, taxiticket=? " +
			"WHERE pmserial IN (SELECT pmserial " +
			"FROM ctd_project p, ctd_projectmember m " +
			"WHERE p.pid = m.pid AND p.projectid = ? " +
			"AND m.usrid = ?) " +
			"AND workdate = ?";
	
	private static final String SQL_UPDATE_PUNCH_RECORD = 
			"UPDATE ctd_actualrecord SET endtime= ? " +
			"WHERE pmserial IN (SELECT pmserial " +
			"FROM ctd_project p, ctd_projectmember m " +
			"WHERE p.pid = m.pid AND p.projectid = ? AND m.usrid = ?) " +
			"AND workdate = ?";

	private static final String SQL_ADD_ACTUAL_RECORD = 
			"INSERT INTO ctd_actualrecord(" +
			"pmserial, workdate, begintime, endtime, taxitimebegin, " +
			"taxitimeend, taxistartlocation, taxiendlocation, taxiticket) " +
			"SELECT pmserial, ?, ?, ?, ?, ?, ?, ?, ? " +
			"FROM ctd_project p, ctd_projectmember m " +
			"WHERE p.pid = m.pid AND p.projectid = ? AND m.usrid = ?";
	
	private static final String SQL_DELETE_PLAN_RECORD = 
			"DELETE FROM ctd_planrecord pr " +
			"WHERE pr.pmserial IN (SELECT m.pmserial FROM ctd_project p, ctd_projectmember m " +
			"WHERE p.pid = m.pid " +
			"AND p.projectid = ? " +
			"AND m.usrid = ?) " +
			"AND pr.workdate = ?";
	
	private static final String SQL_DELETE_ACTUAL_RECORD = 
			"DELETE FROM ctd_actualrecord ar " +
			"WHERE ar.pmserial IN (SELECT m.pmserial FROM ctd_project p, ctd_projectmember m " +
			"WHERE p.pid = m.pid " +
			"AND p.projectid = ? " +
			"AND m.usrid = ?) " +
			"AND ar.workdate = ?";
	
	private static final String SQL_SELECT_PLAN_RECORD = 
			"SELECT pr.begintime, pr.endtime, pr.holiday, pr.taxi, pr.applied " +
			"FROM ctd_planrecord pr, ctd_project p, ctd_projectmember m " +
			"WHERE m.pmserial = pr.pmserial " +
			"AND p.pid = m.pid " +
			"AND p.projectid = ? " +
			"AND pr.workdate = ? " +
			"AND m.usrid = ? ";

	private static final String SQL_SELECT_ACTUAL_RECORD = 
			"SELECT ar.begintime, ar.endtime, ar.taxitimebegin, ar.taxitimeend, " +
			"ar.taxistartlocation, ar.taxiendlocation, ar.taxiticket " +
			"FROM ctd_actualrecord ar, ctd_project p, ctd_projectmember m " +
			"WHERE m.pmserial = ar.pmserial " +
			"AND p.pid = m.pid " +
			"AND p.projectid = ? " +
			"AND ar.workdate = ? " +
			"AND m.usrid = ? ";
	
	private static final String SQL_SELECT_RECORDS = 
			"SELECT pr.*, ar.*, m.usrid FROM (" +
			"ctd_planrecord pr FULL JOIN ctd_actualrecord ar " +
			"ON ar.pmserial = pr.pmserial AND pr.workdate = ar.workdate), " +
			"ctd_project p, ctd_projectmember m " +
			"WHERE p.pid = m.pid " +
			"AND (pr.pmserial = m.pmserial OR ar.pmserial = m.pmserial) " +
			"AND p.projectid = ? ";
	
	private static final String SQL_SELECT_RECORDS_FOR_USER = 
			"SELECT pr.*, ar.* FROM (" +
			"ctd_planrecord pr FULL JOIN ctd_actualrecord ar " +
			"ON ar.pmserial = pr.pmserial AND pr.workdate = ar.workdate), " +
			"ctd_project p, ctd_projectmember m " +
			"WHERE p.pid = m.pid " +
			"AND (pr.pmserial = m.pmserial OR ar.pmserial = m.pmserial) " +
			"AND p.projectid = ? " +
			"AND m.usrid = ?";
	
	@Override
	public void addRecord(String projectid, String usrid, WorkRecord record)
			throws CTDException {
		int rowCount = addPlanRecord(projectid, usrid, record);
		rowCount += addActualRecord(projectid, usrid, record);
		if (rowCount < 1) {
			throw new CTDException(CTDException.DB_SQL_INSERT_RECORD_ERROR);
		}
	}

	@Override
	public void delRecord(String projectid, String usrid, Date workDate)
			throws CTDException {
		Connection connection = RequestEnvironment.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(SQL_DELETE_PLAN_RECORD);
			statement.setString(1, projectid);
			statement.setString(2, usrid);
			statement.setDate(3, VOUtils.convertToSQLDate(workDate));
			int delCount = statement.executeUpdate();
			
			statement = connection.prepareStatement(SQL_DELETE_ACTUAL_RECORD);
			statement.setString(1, projectid);
			statement.setString(2, usrid);
			statement.setDate(3, VOUtils.convertToSQLDate(workDate));
			delCount += statement.executeUpdate();
			
			if (delCount < 1) {
				throw new CTDException(CTDException.DB_SQL_DELETE_RECORD_ERROR);
			}
			
		} catch (SQLException e) {
			throw new CTDException(CTDException.DB_SQL_ERROR_1, e);
		}
	}
	
	@Override
	public void updateRecord(String projectid, String usrid, Date workDate, WorkRecord record)
			throws CTDException {
		int planCount = updatePlanRecord(projectid, usrid, record);
		if (planCount < 1) {
			planCount += addPlanRecord(projectid, usrid, record);
		}
		
		int actualCount = updateActualRecord(projectid, usrid, record);
		if (actualCount < 1) {
			actualCount += addActualRecord(projectid, usrid, record);
		}
		
		if ((planCount + actualCount) < 1) {
			throw new CTDException(CTDException.DB_SQL_UPDATE_RECORD_ERROR);
		}
	}

	@Override
	public WorkRecord getRecord(String projectid, Date workDate) throws CTDException {
		Connection connection = RequestEnvironment.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(SQL_SELECT_PLAN_RECORD);
			statement.setString(1, projectid);
			statement.setDate(2, VOUtils.convertToSQLDate(workDate));		
			RequestEnvironment env = RequestEnvironment.getEnv();
			statement.setString(3, env.getUserID());
			
			WorkRecord workRecord = new WorkRecord();
			workRecord.setWorkDate(workDate);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				PlanRecord plan = new PlanRecord();
				//pr.begintime, pr.endtime, pr.holiday, pr.taxi, pr.applied
				plan.setBeginTime(rs.getTime(1));
				plan.setEndTime(rs.getTime(2));
				plan.setHoliday(rs.getBoolean(3));
				plan.setTaxi(rs.getBoolean(4));
				plan.setApplied(rs.getBoolean(5));

				workRecord.setPlan(plan);
			}
			
			statement.close();
			statement = connection.prepareStatement(SQL_SELECT_ACTUAL_RECORD);
			statement.setString(1, projectid);
			statement.setDate(2, VOUtils.convertToSQLDate(workDate));
			statement.setString(3, env.getUserID());
			rs = statement.executeQuery();
			if (rs.next()) {
				ActualRecord actual = new ActualRecord();
				// ar.begintime, ar.endtime, ar.taxitimebegin, ar.taxitimeend, 
				// ar.taxistartlocation, ar.taxiendlocation, ar.taxiticket "
				actual.setBeginTime(rs.getTime(1));
				actual.setEndTime(rs.getTime(2));
				actual.setTaxiTimeBegin(rs.getTime(3));
				actual.setTaxiTimeEnd(rs.getTime(4));
				actual.setTaxiStartLocation(rs.getString(5));
				actual.setTaxiEndLocation(rs.getString(6));
				actual.setTaxiTicket(rs.getFloat(7));
				workRecord.setActual(actual);
			}
			
			return workRecord;
		} catch (SQLException e) {
			throw new CTDException(CTDException.DB_SQL_ERROR_1, e);
		}
	}
	
	@Override
	public List<WorkRecord> getRecords(String projectid) throws CTDException {
		Connection connection = RequestEnvironment.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(SQL_SELECT_RECORDS);
			statement.setString(1, projectid);
			
			List<WorkRecord> records = new ArrayList<WorkRecord>();
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				WorkRecord workRecord = new WorkRecord();
				int prid = rs.getInt(1); // pr.prid
				if (prid > 0) {
					workRecord.setWorkDate(rs.getDate(3));// pr.workDate
					
					PlanRecord plan = new PlanRecord();
					//pr.begintime, pr.endtime, pr.holiday, pr.taxi, pr.applied
					plan.setBeginTime(rs.getTime(4));//pr.begintime
					plan.setEndTime(rs.getTime(5));//pr.endtime
					plan.setHoliday(rs.getBoolean(6));//pr.holiday
					plan.setTaxi(rs.getBoolean(7));//pr.taxi
					plan.setApplied(rs.getBoolean(8));//pr.applied
					workRecord.setPlan(plan);
				}
				
				int arid = rs.getInt(9); //ar.arid
				if (arid > 0) {
					workRecord.setWorkDate(rs.getDate(11));// pr.workDate
					
					ActualRecord actual = new ActualRecord();
					// ar.begintime, ar.endtime, ar.taxitimebegin, ar.taxitimeend, 
					// ar.taxistartlocation, ar.taxiendlocation, ar.taxiticket "
					actual.setBeginTime(rs.getTime(12)); //ar.begintime
					actual.setEndTime(rs.getTime(13)); //ar.endtime
					actual.setTaxiTimeBegin(rs.getTime(14)); // ar.taxitimebegin
					actual.setTaxiTimeEnd(rs.getTime(15)); // ar.taxitimeend
					actual.setTaxiStartLocation(rs.getString(16)); //ar.taxistartlocation
					actual.setTaxiEndLocation(rs.getString(17)); //ar.taxiendlocation
					actual.setTaxiTicket(rs.getFloat(18)); //ar.taxiticket
					workRecord.setActual(actual);
				}
				
				workRecord.setUserId(rs.getString(19)); //m.usrid
				
				records.add(workRecord);
			}
			
			return records;
		} catch (SQLException e) {
			throw new CTDException(CTDException.DB_SQL_ERROR_1, e);
		}
	}

	@Override
	public List<WorkRecord> getRecords(String projectid, String usrid)
			throws CTDException {
		Connection connection = RequestEnvironment.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(SQL_SELECT_RECORDS_FOR_USER);
			statement.setString(1, projectid);		
			statement.setString(2, usrid);
			
			List<WorkRecord> records = new ArrayList<WorkRecord>();
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				WorkRecord workRecord = new WorkRecord();
				int prid = rs.getInt(1); // pr.prid
				if (prid > 0) {
					workRecord.setWorkDate(rs.getDate(3));// pr.workDate
					
					PlanRecord plan = new PlanRecord();
					//pr.begintime, pr.endtime, pr.holiday, pr.taxi, pr.applied
					plan.setBeginTime(rs.getTime(4));//pr.begintime
					plan.setEndTime(rs.getTime(5));//pr.endtime
					plan.setHoliday(rs.getBoolean(6));//pr.holiday
					plan.setTaxi(rs.getBoolean(7));//pr.taxi
					plan.setApplied(rs.getBoolean(8));//pr.applied
					workRecord.setPlan(plan);
				}
				
				int arid = rs.getInt(9); //ar.arid
				if (arid > 0) {
					workRecord.setWorkDate(rs.getDate(11));// pr.workDate
					
					ActualRecord actual = new ActualRecord();
					// ar.begintime, ar.endtime, ar.taxitimebegin, ar.taxitimeend, 
					// ar.taxistartlocation, ar.taxiendlocation, ar.taxiticket "
					actual.setBeginTime(rs.getTime(12)); //ar.begintime
					actual.setEndTime(rs.getTime(13)); //ar.endtime
					actual.setTaxiTimeBegin(rs.getTime(14)); // ar.taxitimebegin
					actual.setTaxiTimeEnd(rs.getTime(15)); // ar.taxitimeend
					actual.setTaxiStartLocation(rs.getString(16)); //ar.taxistartlocation
					actual.setTaxiEndLocation(rs.getString(17)); //ar.taxiendlocation
					actual.setTaxiTicket(rs.getFloat(18)); //ar.taxiticket
					workRecord.setActual(actual);
				}
				
				workRecord.setUserId(usrid);
				
				String userName = LDAPAccessor.getUserName(usrid);
				workRecord.setUserName(userName);
				
				records.add(workRecord);
			}
			
			return records;
		} catch (SQLException e) {
			throw new CTDException(CTDException.DB_SQL_ERROR_1, e);
		}
	}

	@Override
	public void punchProject(String projectid) throws CTDException {
		Connection connection = RequestEnvironment.getConnection();
		try {
			// update actual record
			PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_PUNCH_RECORD);
			statement.setTime(1, VOUtils.convertToSQLTime(new Date()));
			statement.setString(2, projectid);			
			RequestEnvironment env = RequestEnvironment.getEnv();
			statement.setString(3, env.getUserID());
			statement.setDate(4, VOUtils.convertToSQLDate(new Date()));			
			int rowCount = statement.executeUpdate();
			if (rowCount < 1) {
				statement.close();
				WorkRecord record = new WorkRecord();
				record.setWorkDate(new Date());
				ActualRecord actual = new ActualRecord();
				record.setActual(actual);
				actual.setBeginTime(new Date());
				rowCount += addActualRecord(projectid, env.getUserID(), record);
			}
			
			if (rowCount < 1) {
				throw new CTDException(CTDException.DB_SQL_PUNCH_RECORD_ERROR);
			}
		} catch (SQLException e) {
			throw new CTDException(CTDException.DB_SQL_ERROR_1, e);
		}
	}

	private int addPlanRecord(String projectid, String usrid, WorkRecord record) throws CTDException {
		Connection connection = RequestEnvironment.getConnection();
		try {
			int rowCount = 0;
			PlanRecord plan = record.getPlan();
			if (plan != null) {
				PreparedStatement statement = connection.prepareStatement(SQL_ADD_PLAN_RECORD);
				// workdate, begintime, endtime, holiday, taxi,applied
				statement.setDate(1, VOUtils.convertToSQLDate(record.getWorkDate()));
				statement.setTime(2, VOUtils.convertToSQLTime(plan.getBeginTime()));
				statement.setTime(3, VOUtils.convertToSQLTime(plan.getEndTime()));
				statement.setBoolean(4, plan.isHoliday());
				statement.setBoolean(5, plan.isTaxi());
				statement.setBoolean(6, plan.isApplied());
				statement.setString(7, projectid);
				statement.setString(8, usrid);
				
				rowCount = statement.executeUpdate();
			}
			
			return rowCount;
		} catch (SQLException e) {
			throw new CTDException(CTDException.DB_SQL_ERROR_1, e);
		}
	}

	private int addActualRecord(String projectid, String usrid, WorkRecord record) throws CTDException {
		Connection connection = RequestEnvironment.getConnection();
		try {
			int rowCount = 0;
			ActualRecord actual = record.getActual();
			if (actual != null) {
				PreparedStatement statement = connection.prepareStatement(SQL_ADD_ACTUAL_RECORD);
				//workdate, begintime, endtime, taxitimebegin, taxitimeend, taxistartlocation, taxiendlocation, taxiticket
				statement.setDate(1, VOUtils.convertToSQLDate(record.getWorkDate()));
				statement.setTime(2, VOUtils.convertToSQLTime(actual.getBeginTime()));
				statement.setTime(3, VOUtils.convertToSQLTime(actual.getEndTime()));
				statement.setTime(4, VOUtils.convertToSQLTime(actual.getTaxiTimeBegin()));
				statement.setTime(5, VOUtils.convertToSQLTime(actual.getTaxiTimeEnd()));
				statement.setString(6, actual.getTaxiStartLocation());
				statement.setString(7, actual.getTaxiEndLocation());
				statement.setFloat(8, actual.getTaxiTicket());
				statement.setString(9, projectid);
				statement.setString(10, usrid);
				
				rowCount = statement.executeUpdate();
			}
			
			return rowCount;
		} catch (SQLException e) {
			throw new CTDException(CTDException.DB_SQL_ERROR_1, e);
		}
	}
	
	private int updatePlanRecord(String projectid, String usrid, WorkRecord record) throws CTDException {
		Connection connection = RequestEnvironment.getConnection();
		try {
			int rowCount = 0;
			PlanRecord plan = record.getPlan();
			if (plan != null) {
				// update record
				PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_PLAN_RECORD);
				statement.setTime(1, VOUtils.convertToSQLTime(plan.getBeginTime()));
				statement.setTime(2, VOUtils.convertToSQLTime(plan.getEndTime()));
				statement.setBoolean(3, plan.isHoliday());
				statement.setBoolean(4, plan.isTaxi());
				statement.setBoolean(5, plan.isApplied());
				statement.setString(6, projectid);
				statement.setString(7, usrid);
				statement.setDate(8, VOUtils.convertToSQLDate(record.getWorkDate()));
				
				rowCount = statement.executeUpdate();
			}
			
			return rowCount;
		} catch (SQLException e) {
			throw new CTDException(CTDException.DB_SQL_ERROR_1, e);
		}
	}
	
	private int updateActualRecord(String projectid, String usrid, WorkRecord record) throws CTDException {
		Connection connection = RequestEnvironment.getConnection();
		try {
			int rowCount = 0;
			ActualRecord actual = record.getActual();
			if (actual != null) {
				// update actual record
				PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_ACTUAL_RECROD);
				statement.setTime(1, VOUtils.convertToSQLTime(actual.getBeginTime()));
				statement.setTime(2, VOUtils.convertToSQLTime(actual.getEndTime()));
				statement.setTime(3, VOUtils.convertToSQLTime(actual.getTaxiTimeBegin()));
				statement.setTime(4, VOUtils.convertToSQLTime(actual.getTaxiTimeEnd()));
				statement.setString(5, actual.getTaxiStartLocation());
				statement.setString(6, actual.getTaxiEndLocation());
				statement.setFloat(7, actual.getTaxiTicket());
				statement.setString(8, projectid);
				statement.setString(9, usrid);
				statement.setDate(10, VOUtils.convertToSQLDate(record.getWorkDate()));
				
				rowCount = statement.executeUpdate();
			}
			
			return rowCount;
		} catch (SQLException e) {
			throw new CTDException(CTDException.DB_SQL_ERROR_1, e);
		}
	}

}
