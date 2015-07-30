/**
 * 
 */
package tools.ctd.vo;

import java.util.Date;

/**
 * @author zhangyw
 *
 */
public class PlanRecord {

private Date beginTime;
	
	private Date endTime;
	
	private boolean holiday;
	
	private boolean taxi;
	
	private boolean applied;

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public boolean isHoliday() {
		return holiday;
	}

	public void setHoliday(boolean holiday) {
		this.holiday = holiday;
	}

	public boolean isTaxi() {
		return taxi;
	}

	public void setTaxi(boolean taxi) {
		this.taxi = taxi;
	}

	public boolean isApplied() {
		return applied;
	}

	public void setApplied(boolean applied) {
		this.applied = applied;
	}
	
}
