package com.seda.cart.facade.exception;

public class FacadeException extends Exception {

	private static final long serialVersionUID = 1L;
	private int errorCode;
	private String errorDescription;

	public FacadeException() {
		super();
	}

	/**
	 * 
	 * @param message
	 */
	public FacadeException(String message) {
		super(message);
		this.errorDescription = message;
	}


	/**
	 * 
	 * @param code
	 * @param message
	 */
	public FacadeException(int code, String message) {
		super(code + " - " + message);
		this.errorCode = code;
		this.errorDescription = message;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
}
