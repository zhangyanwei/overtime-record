package tools.ctd.service.rest.provider;

import java.util.Date;

import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.spi.StringConverter;

import tools.ctd.exception.CTDException;
import tools.ctd.util.DateFormat;

@Provider
public class DateConverter implements StringConverter<Date> {
	
	private static final Log LOG = LogFactory.getLog(DateConverter.class);
	
	public Date fromString(String value) {		
		try {
			return DateFormat.parseDate(value);
		} catch (CTDException e) {
			LOG.error(e);
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public String toString(Date date) {
		return DateFormat.formatDate(date);
	}
}