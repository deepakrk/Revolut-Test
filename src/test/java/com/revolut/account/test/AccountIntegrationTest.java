package com.revolut.account.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.AccountApplication;
import com.revolut.AppConfiguration;
import com.revolut.account.api.request.dto.AccountCreateRequestDto;
import com.revolut.account.api.request.dto.MoneyTransferRequestDto;
import com.revolut.rs.core.MediaTypeCustom;

import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

@RunWith(MockitoJUnitRunner.class)
public class AccountIntegrationTest {

	private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

	private static final String TMP_FILE = createTempFile();
	private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("test-config.yml");

	@ClassRule
	public static final DropwizardAppRule<AppConfiguration> RULE = new DropwizardAppRule<>(AccountApplication.class,
			CONFIG_PATH, ConfigOverride.config("database.url", "jdbc:h2:" + TMP_FILE));

	private static String createTempFile() {
		try {
			return File.createTempFile("test-example", null).getAbsolutePath();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Test
	public void testPostAccount() throws Exception {

		AccountCreateRequestDto accountCreateRequestDto = new AccountCreateRequestDto();
		accountCreateRequestDto.setAccountNumber("2ERTYUIO3456");
		accountCreateRequestDto.setFullName("Deepak R Kiran");
		accountCreateRequestDto.setDepositAmount(500d);

		AccountCreateRequestDto accountCreateRequestDto2 = new AccountCreateRequestDto();
		accountCreateRequestDto2.setAccountNumber("2ERTYUIO9856");
		accountCreateRequestDto2.setFullName("Mohit Gupta");
		accountCreateRequestDto2.setDepositAmount(300d);

		createAccount(accountCreateRequestDto, accountCreateRequestDto2);

		MoneyTransferRequestDto moneyTransferRequestDto = new MoneyTransferRequestDto();
		moneyTransferRequestDto.setAmount(200d);
		moneyTransferRequestDto.setFromAccountNumber("2ERTYUIO3456");
		moneyTransferRequestDto.setToAccountNumber("2ERTYUIO9856");

		transferMoney(moneyTransferRequestDto);
	}

	private void createAccount(AccountCreateRequestDto account, AccountCreateRequestDto account2) {
		MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		Response.status(Status.FOUND).type(MediaTypeCustom.APPLICATION_JSON_REVOLUT).entity(account).build();

		Response response = RULE.client()
				.target("http://localhost:" + RULE.getLocalPort() + "/revolut-api/account/create").request()
				.post(Entity.entity(account, MediaTypeCustom.APPLICATION_JSON_REVOLUT));

		int statusCode = response.getStatus();
		assertThat(statusCode == 200);

		Response response2 = RULE.client()
				.target("http://localhost:" + RULE.getLocalPort() + "/revolut-api/account/create").request()
				.post(Entity.entity(account2, MediaTypeCustom.APPLICATION_JSON_REVOLUT));

		int statusCode2 = response2.getStatus();
		assertThat(statusCode2 == 200);
	}

	private void transferMoney(MoneyTransferRequestDto moneyTransferRequestDto) {

		Response response = RULE.client()
				.target("http://localhost:" + RULE.getLocalPort() + "/revolut-api/account/money-transfer").request()
				.post(Entity.entity(moneyTransferRequestDto, MediaTypeCustom.APPLICATION_JSON_REVOLUT));

		int statusCode = response.getStatus();
		assertThat(statusCode == 200);
	}
}
