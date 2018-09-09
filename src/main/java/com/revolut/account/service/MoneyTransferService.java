package com.revolut.account.service;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;

import com.revolut.account.api.request.dto.MoneyTransferRequestDto;
import com.revolut.account.db.dao.AccountDao;
import com.revolut.account.db.dao.MoneyTransferLogDao;
import com.revolut.account.db.entity.Account;
import com.revolut.account.db.entity.MoneyTransferLog;
import com.revolut.account.exception.InsufficientFundException;
import com.revolut.account.exception.MoneyTransferFailedException;
import com.revolut.account.mapper.MoneyTransferEntityMapper;

/**
 * Service responsible for transfer money within transactional scope.
 * 
 * @author deepak rk
 *
 */
public class MoneyTransferService {

	private final AccountDao accountDao;
	private final MoneyTransferLogDao moneyTransferLogDao;

	public MoneyTransferService(AccountDao accountDao, MoneyTransferLogDao moneyTransferLogDao) {

		this.accountDao = accountDao;
		this.moneyTransferLogDao = moneyTransferLogDao;
	}

	public MoneyTransferLog transferProcess(MoneyTransferRequestDto moneyTransferRequestDto) {
		String fromAccountNumber = moneyTransferRequestDto.getFromAccountNumber();
		MoneyTransferLog moneyTransferLog = null;
		Account fromAccount = accountDao.findByAccountName(fromAccountNumber);

		if (fromAccount.getAmount() < moneyTransferRequestDto.getAmount()) {
			throw new InsufficientFundException();
		} else {
			// Transation started
			SessionFactory sessionFactory = accountDao.getSessionFacory();
			Session session = sessionFactory.getCurrentSession();
			ManagedSessionContext.bind(session);
			Transaction transaction = session.getTransaction();

			try {
				Account toAccount = accountDao.findByAccountName(moneyTransferRequestDto.getToAccountNumber());
				Double amount = toAccount.getAmount() + moneyTransferRequestDto.getAmount();
				toAccount.setAmount(amount);
				accountDao.save(toAccount);

				moneyTransferLog = MoneyTransferEntityMapper.INSTANCE.transferTo(moneyTransferRequestDto);
				moneyTransferLog.setCreatedAt(new Date());
				moneyTransferLogDao.create(moneyTransferLog);

				transaction.commit();

			} catch (Exception e) {
				transaction.rollback();
				throw new MoneyTransferFailedException(e);
			} finally {
				session.close();
				ManagedSessionContext.unbind(sessionFactory);
			}
		}
		return moneyTransferLog;
	}

}
