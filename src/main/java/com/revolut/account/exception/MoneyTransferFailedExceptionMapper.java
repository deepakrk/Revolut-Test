package com.revolut.account.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import io.dropwizard.jersey.errors.ErrorMessage;

public class MoneyTransferFailedExceptionMapper implements ExceptionMapper<MoneyTransferFailedException> {

	@Override
	public Response toResponse(MoneyTransferFailedException exception) {
		return Response.status(Status.CONFLICT).type(MediaType.APPLICATION_JSON_TYPE)
				.entity(new ErrorMessage(Status.CONFLICT.getStatusCode(), "Money Transfer Failed")).build();
	}

}
