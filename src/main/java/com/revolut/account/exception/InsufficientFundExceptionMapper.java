package com.revolut.account.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import io.dropwizard.jersey.errors.ErrorMessage;

@Provider
public class InsufficientFundExceptionMapper implements ExceptionMapper<InsufficientFundException> {

	public Response toResponse(InsufficientFundException exception) {
		return Response.status(Status.CONFLICT).
				type(MediaType.APPLICATION_JSON_TYPE)
				.entity(new ErrorMessage(Status.CONFLICT.getStatusCode(), " Insufficient Fund")).build();
	
	}

}
