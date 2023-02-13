package com.seda.cart.facade.exception;

public class CartSecurityException extends CartFacadeException {

	private static final long serialVersionUID = 1L;
	String callerUrl;

	public CartSecurityException(String message, String securityToken, String callerUrl) {
		super(message, securityToken);
		this.callerUrl = callerUrl;
	}

	public String getCallerUrl() {
		return callerUrl;
	}
	public void setCallerUrl(String callerUrl) {
		this.callerUrl = callerUrl;
	}
}
