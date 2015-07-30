package tools.ctd.vo;

import java.text.SimpleDateFormat;

public final class VOUtils {
	
	private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	public static final java.sql.Date convertToSQLDate(java.util.Date date)
	{
		if (date != null) {
			String formated = DATEFORMAT.format(date);
			return java.sql.Date.valueOf(formated);
		}
		
		return null;
	}
	
	public static final java.sql.Time convertToSQLTime(java.util.Date date) {
		if (date != null) {
			long time = date.getTime();
			return new java.sql.Time(time);
		}
		
		return null;
	}

}
