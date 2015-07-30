package tools.ctd.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "error")
public class ErrorMessage {

	private String code_;
	private String message_;

	@XmlElement(name = "code")
	public String getCode() {
		return code_;
	}
	
	public void setCode(String errorCode) {
		this.code_ = errorCode;
	}

	@XmlElement(name = "message")
	public String getMessage() {
		return this.message_;
	}
	
	public void setMessage(String errorMessage) {
		this.message_ = errorMessage;
	}

}
