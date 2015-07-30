package tools.ctd.service.rest.provider;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import tools.ctd.exception.CTDException;
import tools.ctd.vo.ErrorMessage;

@Provider
public class CTDExceptionMapper implements ExceptionMapper<CTDException> {

	@Override
	public Response toResponse(CTDException exception) {
		
		ErrorMessage message = new ErrorMessage();
		message.setCode(exception.getCode());
		message.setMessage(exception.getMessage());
		
		ResponseBuilder responseBuilder = Response.serverError();
		responseBuilder.type(MediaType.APPLICATION_JSON + ";charset=utf-8");
		responseBuilder.entity(message);
		return responseBuilder.build();
	}
	
}
