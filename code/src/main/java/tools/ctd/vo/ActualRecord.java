/**
 * 
 */
package tools.ctd.vo;

import java.util.Date;

/**
 * @author zhangyw
 *
 */
public class ActualRecord {
	
	private Date beginTime;
	
	private Date endTime;
	
	private Date taxiTimeBegin;
	
	private Date taxiTimeEnd;
	
	private String taxiStartLocation;
	
	private String taxiEndLocation;
	
	private float taxiTicket;

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

	public Date getTaxiTimeBegin() {
		return taxiTimeBegin;
	}

	public void setTaxiTimeBegin(Date taxiTimeBegin) {
		this.taxiTimeBegin = taxiTimeBegin;
	}

	public Date getTaxiTimeEnd() {
		return taxiTimeEnd;
	}

	public void setTaxiTimeEnd(Date taxiTimeEnd) {
		this.taxiTimeEnd = taxiTimeEnd;
	}

	public String getTaxiStartLocation() {
		return taxiStartLocation;
	}

	public void setTaxiStartLocation(String taxiStartLocation) {
		this.taxiStartLocation = taxiStartLocation;
	}

	public String getTaxiEndLocation() {
		return taxiEndLocation;
	}

	public void setTaxiEndLocation(String taxiEndLocation) {
		this.taxiEndLocation = taxiEndLocation;
	}

	public float getTaxiTicket() {
		return taxiTicket;
	}

	public void setTaxiTicket(float taxiTicket) {
		this.taxiTicket = taxiTicket;
	}
	
}
