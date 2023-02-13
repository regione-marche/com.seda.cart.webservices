package com.seda.cart.facade.dto;

public class CartSessionRequestDto extends CartRequestDto {
	
	private static final long serialVersionUID = 1L;

	String codiceUtente;

	public String getCodiceUtente() {
		return codiceUtente;
	}

	public void setCodiceUtente(String codiceUtente) {
		this.codiceUtente = codiceUtente;
	}

}
