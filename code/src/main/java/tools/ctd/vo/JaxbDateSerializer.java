package tools.ctd.vo;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class JaxbDateSerializer extends XmlAdapter<String, Date> {

	@Override
	public String marshal(Date date) throws Exception {
		return tools.ctd.util.DateFormat.formatDate(date);
	}

	@Override
	public Date unmarshal(String date) throws Exception {
		return tools.ctd.util.DateFormat.parseDate(date);
	}

}
