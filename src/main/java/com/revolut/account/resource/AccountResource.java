package com.revolut.account.resource;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.revolut.account.api.request.dto.AccountCreateRequestDto;
import com.revolut.account.api.request.dto.MoneyTransferRequestDto;
import com.revolut.account.db.dao.AccountDao;
import com.revolut.account.db.dao.MoneyTransferLogDao;
import com.revolut.account.db.entity.Account;
import com.revolut.account.db.entity.MoneyTransferLog;
import com.revolut.account.mapper.AccountEntityMapper;
import com.revolut.account.mapper.MoneyTransferEntityMapper;
import com.revolut.account.service.MoneyTransferService;
import com.revolut.rs.core.MediaTypeCustom;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;

@Api("revolut-account-service")
@Path("/account")
@Produces(MediaTypeCustom.APPLICATION_JSON_REVOLUT)
public class AccountResource extends GeneralResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountResource.class);

	private final AccountDao accountDao;
	private final MoneyTransferLogDao moneyTransferLogDao;

	public AccountResource(AccountDao accountDao, MoneyTransferLogDao moneyTransferLogDao) {
		this.accountDao = accountDao;
		this.moneyTransferLogDao = moneyTransferLogDao;
	}

	@POST
	@Path("/create")
	@Timed
	@Consumes(MediaTypeCustom.APPLICATION_JSON_REVOLUT)
	@UnitOfWork
	@ExceptionMetered
	public Response createAccount(AccountCreateRequestDto accountDto) {

		Account account = accountDao.save(AccountEntityMapper.INSTANCE.accountRequestDtoToEntity(accountDto));

		return Response.status(Status.CREATED).entity(account).build();
	}

	@GET
	@Path("/{accountNumber}")
	@Timed
	@UnitOfWork
	public Response fetchByAccountByACN(@PathParam("accountNumber") String accountNumber) {
		Account account = accountDao.findByAccountName(accountNumber);
		if (account != null) {
			return Response.status(Status.FOUND).type(MediaTypeCustom.APPLICATION_JSON_REVOLUT).entity(account).build();
		} else {
			return Response.status(Status.NOT_FOUND).type(MediaTypeCustom.APPLICATION_JSON_REVOLUT).entity(null)
					.build();
		}
	}

	@POST
	@Path("/money-transfer")
	@Timed
	@Consumes(MediaTypeCustom.APPLICATION_JSON_REVOLUT)
	@UnitOfWork
	public Response transfer(MoneyTransferRequestDto moneyTransferRequestDto) {
		moneyTransferRequestDto.setCreatedAt(new Date());
		MoneyTransferLog moneyTransferLog = MoneyTransferEntityMapper.INSTANCE.transferTo(moneyTransferRequestDto);
		MoneyTransferService moneyTransferService = new MoneyTransferService(accountDao, moneyTransferLogDao);
		MoneyTransferLog moneyTransferLogdbobj = moneyTransferService.transferProcess(moneyTransferRequestDto);

		return Response.status(Status.CREATED).type(MediaType.APPLICATION_JSON_TYPE).entity(moneyTransferLogdbobj)
				.build();
	}

	@GET
	@Path("/all")
	@Timed
	@UnitOfWork
	public List<Account> allAcount() {
		return accountDao.findAll();
	}
}
