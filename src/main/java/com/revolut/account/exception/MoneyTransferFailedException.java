package com.revolut.account.exception;

public class MoneyTransferFailedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7048479004044644763L;

	public MoneyTransferFailedException(Exception e) {
		super("Money Transfer Failed", e);
	}

}
