package tools.ctd.vo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="userEnv")
public class UserEnvironment {
	
	private String userID;
	
	private String userName;
	
	private String userIP;

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserIP() {
		return userIP;
	}

	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}

}
