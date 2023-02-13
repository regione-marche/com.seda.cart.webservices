package com.seda.cart.facade.dto;

import java.io.Serializable;

public class CartRequestDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String cartSessionId;
    private String securityToken;
    private String callerURL;
    private String responseCode;
    private String codiceSocieta;
    private String canalePagamento;

    public String getCartSessionId() {
        return cartSessionId;
    }

    public void setCartSessionId(String cartSessionId) {
        this.cartSessionId = cartSessionId;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

	public String getCallerURL() {
		return callerURL;
	}

	public void setCallerURL(String callerURL) {
		this.callerURL = callerURL;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
 
	public String getCanalePagamento() {
        return canalePagamento;
    }

    public void setCanalePagamento(String canalePagamento) {
        this.canalePagamento = canalePagamento;
    }

	public String getCodiceSocieta() {
        return codiceSocieta;
    }

    public void setCodiceSocieta(String codiceSocieta) {
        this.codiceSocieta = codiceSocieta;
    }
}
