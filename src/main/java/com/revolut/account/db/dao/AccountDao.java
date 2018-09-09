package com.revolut.account.db.dao;

import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.revolut.account.db.entity.Account;

import io.dropwizard.hibernate.AbstractDAO;

public class AccountDao extends AbstractDAO<Account> {

	SessionFactory sessionFactory;

	public AccountDao(SessionFactory sessionFactory) {
		super(sessionFactory);
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFacory() {
		return this.sessionFactory;
	}

	public Account save(Account account) {
		account.setDefaultCurrencyType(Currency.getInstance("USD").getCurrencyCode());
		account.setCreatedAt(new Date());
		account.setUpdatedAt(new Date());
		return persist(account);
	}

	public Account findById(UUID uuid) {
		return get(uuid);
	}

	public List<Account> findAll() {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Account> cr = cb.createQuery(Account.class);
		Root<Account> root = cr.from(Account.class);
		cr.select(root);
		Query<Account> query = sessionFactory.getCurrentSession().createQuery(cr);
		List<Account> results = query.getResultList();

		return results;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public Account findByAccountName(String accountNumber) {
		return uniqueResult(namedQuery("com.revolut.account.db.entity.Account.findByAccountNumber")
				.setString("accountNumber", accountNumber));
	}
}
