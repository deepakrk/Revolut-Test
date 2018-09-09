package com.revolut.account.exception;

import javax.persistence.EntityExistsException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AccountAlreadyExistExceptionMapper implements ExceptionMapper<javax.persistence.EntityExistsException> {

	@Override
	public Response toResponse(EntityExistsException ex) {
		return Response.status(Status.CONFLICT).entity(" ex mapp " + ex.getMessage()).type("text/plain").build();
	}

}
