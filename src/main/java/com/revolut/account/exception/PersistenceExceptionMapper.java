package com.revolut.account.exception;

import static javax.ws.rs.core.Response.Status.CONFLICT;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import javax.persistence.PersistenceException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.hibernate.exception.ConstraintViolationException;

import io.dropwizard.jersey.errors.ErrorMessage;

@Provider
public class PersistenceExceptionMapper implements ExceptionMapper<PersistenceException> {

	@Override
	public Response toResponse(PersistenceException exception) {
		Response response;
		if (exception.getCause() instanceof ConstraintViolationException) {
			final String details = "Violated constraint. Account Number already exist";
			response = Response.status(CONFLICT).type(MediaType.APPLICATION_JSON_TYPE)
					.entity(new ErrorMessage(CONFLICT.getStatusCode(), "Constraint violation failure", details))
					.build();
		} else {
			response = Response.status(INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE)
					.entity(new ErrorMessage(INTERNAL_SERVER_ERROR.getStatusCode(), "Persistence failure")).build();
		}

		return response;
	}

}
