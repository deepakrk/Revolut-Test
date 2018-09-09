package com.revolut.account.exception;

public class InsufficientFundException extends RuntimeException {

	/**
	* 
	*/
	private static final long serialVersionUID = -4823283788710918078L;

	public InsufficientFundException() {
		super("Insufficient Fund");
	}
}
