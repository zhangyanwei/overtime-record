package tools.ctd.exception;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class CTDException extends Exception {
	
	private static final long serialVersionUID = -3595143143148007167L;
	
	private static final String RESOURCE_BUNDLE_BASE_NAME = "exception";
	
	private static final String CODE_PREFIX = "CTD";
	
	private static final DecimalFormat MODULE_FORMAT = new DecimalFormat("00");
	
	private static final DecimalFormat CODE_FORMAT = new DecimalFormat("000");

	public static String METHOD_PARAMETER_NUMBER_ERROR_2;
	
	/// ******************************************
	/// ********** 其他异常定义 **********
	/// ********** 异常号的范围是 CTD00001 ~ CTD00050 **
	/// ******************************************
	
	/** 未定义异常： {0} */
	public static final String UNKNOWN_ERROR_1 = createErrorCode(0, 1);
	
	public static final String PARAMS_ERROR_1 = createErrorCode(0, 2);
	
	/// ******************************************
	/// ********** 数据库访问相关的异常定义 **********
	/// ********** 异常号的范围是 CTD01001 ~ CTD01150 **
	/// ******************************************
	
	/** 创建数据库连接时出现了错误, 详细： {0} */
	public static final String DB_CONNECTION_CREATE_ERROR_1 = createErrorCode(1, 0);
	
	/** 数据库访问时出现了错误, 详细： {0} */
	public static final String DB_SQL_ERROR_1 = createErrorCode(1, 1);
	
	/** 追加项目信息没有成功，请重试，如果仍然不成功，请联系管理员。 */
	public static final String DB_SQL_INSERT_PROJECT_ERROR = createErrorCode(1, 2);
	
	/** 追加项目成员没有成功，请重试，如果仍然不成功，请联系管理员。 */
	public static final String DB_SQL_INSERT_MEMBER_ERROR = createErrorCode(1, 3);
	
	/** 更新项目信息没有成功，请重试，如果仍然不成功，请联系管理员。 */
	public static final String DB_SQL_UPDATE_PROJECT_ERROR = createErrorCode(1, 4);
	
	/** 删除项目信息没有成功，请重试，如果仍然不成功，请联系管理员。 */
	public static final String DB_SQL_DELETE_PROJECT_ERROR = createErrorCode(1, 5);
	
	/** 删除项目成员没有成功，请重试，如果仍然不成功，请联系管理员。 */
	public static final String DB_SQL_DELETE_MEMBER_ERROR = createErrorCode(1, 6);
	
	/** 获取的项目信息不存在。 */
	public static final String DB_SQL_SElECT_PROJECT_ERROR = createErrorCode(1, 7);
	
	//// record处理相关
	/** 添加加班记录没有成功，请重试，如果仍然不成功，请联系管理员。 */
	public static final String DB_SQL_INSERT_RECORD_ERROR = createErrorCode(1, 100);
	
	/** 删除加班记录没有成功，请重试，如果仍然不成功，请联系管理员。 */
	public static final String DB_SQL_DELETE_RECORD_ERROR = createErrorCode(1, 101);
	
	/** 更新加班记录没有成功，请重试，如果仍然不成功，请联系管理员。 */
	public static final String DB_SQL_UPDATE_RECORD_ERROR = createErrorCode(1, 102);
	
	/** 打卡失败，请重试，如果仍然不成功，请联系管理员。 */
	public static final String DB_SQL_PUNCH_RECORD_ERROR = createErrorCode(1, 103);
	
	//// 用户处理相关	
	/** 更新默认项目的设置没有成功，请重试，如果仍然不成功，请联系管理员。 */
	public static final String DB_SQL_INSERT_USER_DEFAULT_PROJECT_ERROR = createErrorCode(1, 200);
	
	/// ******************************************
	/// ********** 权限相关的异常定义 **********
	/// ********** 异常号的范围是 CTD02001 ~ CTD02150 **
	/// ******************************************
	
	/** 追加项目信息没有成功，请重试，如果仍然不成功，请联系管理员。 */
	public static final String ACL_NO_RIGHT_ERROR = createErrorCode(2, 1);
	
	private String errorCode;
	private String message;
	private Throwable throwable;

	public CTDException(Throwable throwable) {
		this.throwable = throwable;
		if (this.throwable instanceof CTDException) {
			CTDException exception = (CTDException) throwable;
			this.errorCode = exception.errorCode;
			this.message = exception.message;
		} else {
			this.errorCode = UNKNOWN_ERROR_1;
			
			ResourceBundle bundle = 
					ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME, Locale.getDefault());
			String msg = bundle.getString(this.errorCode);
			this.message = MessageFormat.format(msg, throwable.getLocalizedMessage());
		}
	}
	
	public CTDException(String errorCode) {
		this.errorCode = errorCode;
		
		ResourceBundle bundle = 
				ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME, Locale.getDefault());
		this.message = bundle.getString(errorCode);
	}
	
	public CTDException(String errorCode, Object ... params) {
		this(errorCode);
		this.message = MessageFormat.format(this.message, params);
	}

	public CTDException(String errorCode, Throwable throwable) {
		this(errorCode);
		this.message = MessageFormat.format(this.message, throwable.getLocalizedMessage());
		this.throwable = throwable;
	}
	
	public CTDException(String errorCode, String name,
			int i) {
		// TODO Auto-generated constructor stub
	}
	
	private static String createErrorCode(int module, int code) {
		StringBuilder errorCode = new StringBuilder(CODE_PREFIX);
		errorCode.append(MODULE_FORMAT.format(module));
		errorCode.append(CODE_FORMAT.format(code));
		return errorCode.toString();
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public String getLocalizedMessage() {
		return this.message;
	}
	
	public String getCode() {
		return this.errorCode;
	}

	@Override
	public synchronized Throwable getCause() {
		Throwable throwable = super.getCause();
		if (throwable == null) {
			return this.throwable;
		}
		
		return throwable;
	}
}
