package com.revolut.account.resource;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;

public class GeneralResource {

	protected void formatAndThrow(Logger logger, Response.Status status, String message) {
		logger.error("response status {} message \"{}\".", status, message);
		throw new WebApplicationException(Response.status(status).entity("{\"error\":\"" + message + "\"}").build());
	}
}
