package tools.ctd.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tools.ctd.exception.CTDException;

public final class DateFormat {
	
	private static final Log LOG = LogFactory.getLog(DateFormat.class);
	
	private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	private static final SimpleDateFormat MONTHFORMAT = new SimpleDateFormat("yyyy-MM");
	
	private static final SimpleDateFormat TIMEFORMAT = new SimpleDateFormat("HH:mm:ss");
	
	private static final SimpleDateFormat TIMEFORMAT_SHORT = new SimpleDateFormat("HH:mm");
	
	public static Date parseDate(String value) throws CTDException {
		try {
			return DATEFORMAT.parse(value);
		} catch (ParseException e) {
			LOG.error(e);
			throw new CTDException(e);
		}
	}

	public static String formatDate(Date date) {
		return date != null ? DATEFORMAT.format(date) : "";
	}
	
	public static String formatDatePeroid(Date begin, Date end) {
		String rel;
		if (begin != null || end != null) {
			String b = (begin != null ? DATEFORMAT.format(begin) : "");
			StringBuilder period = new StringBuilder(b);
			period.append(" ~ ");
			String e = (end != null ? DATEFORMAT.format(end) : "");
			period.append(e);
			rel = period.toString();
		} else {
			rel = "";
		}
		
		return rel;
	}
	
	public static String formatMonth(Date date) {
		return date != null ? MONTHFORMAT.format(date) : "";
	}
	
	public static String formatTime(Date time) {		
		return time != null ? TIMEFORMAT.format(time) : "";
	}
	
	public static String formatTimePeroid(Date begin, Date end) {
		String rel;
		if (begin != null || end != null) {
			String b = (begin != null ? TIMEFORMAT_SHORT.format(begin) : "");
			StringBuilder period = new StringBuilder(b);
			period.append(" ~ ");
			String e = (end != null ? TIMEFORMAT_SHORT.format(end) : "");
			period.append(e);
			rel = period.toString();
		} else {
			rel = "";
		}
		
		return rel;
	}
	
	public static String formatTimeInterval(Date begin, Date end) {
		String rel;
		if (begin != null && end != null) {
			TimeZone zone = TimeZone.getDefault();
			long interval = end.getTime() - begin.getTime();
			Date d = new Date(interval - zone.getRawOffset());			
			rel = TIMEFORMAT_SHORT.format(d);
		} else {
			rel = "";
		}
		
		return rel;
	}

}
