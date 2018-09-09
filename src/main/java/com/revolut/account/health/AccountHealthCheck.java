package com.revolut.account.health;

import com.codahale.metrics.health.HealthCheck;

public class AccountHealthCheck extends HealthCheck {

	@Override
	protected Result check() throws Exception {

		final String saying = String.format("TEST", "TEST");
		if (!saying.contains("TEST")) {
			return Result.unhealthy("template doesn't include a name");
		}
		return Result.healthy();
	}

}
