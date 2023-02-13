package com.seda.cart.facade.exception;

public class SessionLimitExceededException extends FacadeException {
	
	private static final long serialVersionUID = 1L;
	private Integer sessionLimit;

	public SessionLimitExceededException(String message, Integer sessionLimit) {
		super(message);
		this.sessionLimit = sessionLimit;
	}

	public Integer getSessionLimit() {
		return sessionLimit;
	}

	public void setSessionLimit(Integer sessionLimit) {
		this.sessionLimit = sessionLimit;
	}

}
