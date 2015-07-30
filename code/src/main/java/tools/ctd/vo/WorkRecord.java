/**
 * 
 */
package tools.ctd.vo;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author zhangyw
 *
 */
@XmlRootElement(name="record")
public class WorkRecord {
	
	private String userId;
	
	private String userName;
	
	private Date workDate;
	
	private PlanRecord plan;
	
	private ActualRecord actual;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@XmlElement
	@XmlJavaTypeAdapter(JaxbDateSerializer.class)
	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workdate) {
		this.workDate = workdate;
	}

	@XmlElement
	public PlanRecord getPlan() {
		return plan;
	}

	public void setPlan(PlanRecord plan) {
		this.plan = plan;
	}

	@XmlElement
	public ActualRecord getActual() {
		return actual;
	}

	public void setActual(ActualRecord actual) {
		this.actual = actual;
	}

}
