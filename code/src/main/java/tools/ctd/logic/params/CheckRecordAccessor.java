/**
 * 
 */
package tools.ctd.logic.params;

import tools.ctd.aop.DefaultInvokeFilter;
import tools.ctd.exception.CTDException;
import tools.ctd.vo.WorkRecord;

/**
 * @author zhangyw
 *
 */
public class CheckRecordAccessor extends DefaultInvokeFilter {

	public void addRecord(String projectid, WorkRecord record)
			throws CTDException {
		if (record.getWorkDate() == null || 
				(record.getPlan() == null && record.getActual() == null)) {
			throw new CTDException(CTDException.PARAMS_ERROR_1, record);
		}
		
		super.invoke();
	}
	
}
