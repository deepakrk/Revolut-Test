package com.revolut;

import com.revolut.account.db.dao.AccountDao;
import com.revolut.account.db.dao.MoneyTransferLogDao;
import com.revolut.account.db.entity.Account;
import com.revolut.account.db.entity.MoneyTransferLog;
import com.revolut.account.exception.InsufficientFundExceptionMapper;
import com.revolut.account.exception.PersistenceExceptionMapper;
import com.revolut.account.health.AccountHealthCheck;
import com.revolut.account.mapper.AccountEntityMapper;
import com.revolut.account.mapper.MoneyTransferEntityMapper;
import com.revolut.account.resource.AccountResource;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class AccountApplication extends Application<AppConfiguration> {

	@Override
	public void run(AppConfiguration configuration, Environment environment) throws Exception {

		final AccountDao accountDao = new AccountDao(hibernateBundle.getSessionFactory());
		final MoneyTransferLogDao moneyTransferLogDao = new MoneyTransferLogDao(hibernateBundle.getSessionFactory());

		environment.jersey().register(new AccountResource(accountDao, moneyTransferLogDao));
		environment.jersey().register(PersistenceExceptionMapper.class);
		environment.jersey().register(InsufficientFundExceptionMapper.class);
		environment.jersey().register(AccountEntityMapper.class);
		environment.jersey().register(MoneyTransferEntityMapper.class);

		final AccountHealthCheck healthCheck = new AccountHealthCheck();
		environment.healthChecks().register("template", healthCheck);

	}

	@Override
	public String getName() {
		return "revolut-account";
	}

	@Override
	public void initialize(Bootstrap<AppConfiguration> bootstrap) {
		bootstrap.addBundle(new SwaggerBundle<AppConfiguration>() {
			@Override
			protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(AppConfiguration configuration) {
				return configuration.swaggerBundleConfiguration;
			}
		});

		bootstrap.addBundle(hibernateBundle);
	}

	private final HibernateBundle<AppConfiguration> hibernateBundle = new HibernateBundle<AppConfiguration>(
			Account.class, MoneyTransferLog.class) {
		@Override
		public DataSourceFactory getDataSourceFactory(AppConfiguration configuration) {
			return configuration.getDataSourceFactory();
		}
	};

	public static void main(String[] args) throws Exception {
		new AccountApplication().run(args);
	}

}
