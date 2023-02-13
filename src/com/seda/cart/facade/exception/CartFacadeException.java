package com.seda.cart.facade.exception;

public class CartFacadeException extends FacadeException {
	
	private static final long serialVersionUID = 1L;

	private String securityToken;

	public CartFacadeException(String message, String securityToken) {
		super(message);
		this.securityToken = securityToken;
	}

	public String getSecurityToken() {
		return securityToken;
	}

	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
	}

}
