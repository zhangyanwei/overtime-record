package tools.ctd.env;

import java.io.File;
import java.sql.Connection;

public class RequestEnvironment {
	
	private static InheritableThreadLocal<RequestEnvironment> env = new InheritableThreadLocal<RequestEnvironment>();
	
	private String userID;
	
	private String userName;
	
	private String userIP;
	
	private String password;
	
	private File tempFolder;
	
	private int level;
	
	private Connection connection;

	public RequestEnvironment() {
		// default constructor.
	}

	public RequestEnvironment(RequestEnvironment env) {
		this.userID = env.userID;
		this.password = env.password;
		this.userName = env.userName;
		this.userIP = env.userIP;
		this.tempFolder = env.tempFolder;
	}

	public static RequestEnvironment getEnv() {
		return env.get();
	}

	public static void setEnv(RequestEnvironment value) {
		env.set(value);
	}

	/**
	 * @return the userID
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * @param userID the userID to set
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the userIP
	 */
	public String getUserIP() {
		return userIP;
	}

	/**
	 * @param userIP the userIP to set
	 */
	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}

	/**
	 * @return the tempFolder
	 */
	public File getTempFolder() {
		return tempFolder;
	}

	/**
	 * @param tempFolder the tempFolder to set
	 */
	public void setTempFolder(File tempFolder) {
		this.tempFolder = tempFolder;
	}

	public int getLevel() {
		return level;
	}

	public static Connection getConnection() {
		RequestEnvironment env = getEnv();
		return env.connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public int increaseLevel() {
		return ++(this.level);
	}

	public int decreaseLevel() {
		return --(this.level);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

   
}
