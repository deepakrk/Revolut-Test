package com.revolut.account.db.dao;

import java.util.Date;

import org.hibernate.SessionFactory;

import com.revolut.account.db.entity.MoneyTransferLog;

import io.dropwizard.hibernate.AbstractDAO;

public class MoneyTransferLogDao extends AbstractDAO<MoneyTransferLog> {

	SessionFactory sessionFactory;

	public MoneyTransferLogDao(SessionFactory sessionFactory) {
		super(sessionFactory);
		this.sessionFactory = sessionFactory;
	}

	public MoneyTransferLog create(MoneyTransferLog moneyTransferLog) {
		moneyTransferLog.setCreatedAt(new Date());
		return persist(moneyTransferLog);
	}
}
