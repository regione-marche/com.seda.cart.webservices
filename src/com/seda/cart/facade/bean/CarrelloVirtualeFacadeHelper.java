package com.seda.cart.facade.bean;

import java.util.List;

import com.seda.cart.facade.dto.CartRequestDto;
import com.seda.cart.facade.dto.CartResponseDto;
import com.seda.cart.facade.dto.ItemDto;
import com.seda.cart.facade.exception.CartSecurityException;
import com.seda.cart.facade.exception.CartVerifyException;
import com.seda.cart.facade.exception.FacadeException;
import com.seda.cart.facade.model.ShoppingCartDto;
import com.seda.commons.security.TokenGenerator;

public class CarrelloVirtualeFacadeHelper {

	static void verify(ShoppingCartDto shoppingCartDto, CartRequestDto request) throws CartSecurityException, CartVerifyException {
		verifyCartData(shoppingCartDto, request);
		verifyToken(shoppingCartDto, request);
	}
	
	static CartResponseDto generateResponse(String cartId, String securityToken, List<ItemDto> items) {
		CartResponseDto response = new CartResponseDto();
		response.setCartSessionId(cartId);
		response.setSecurityToken(securityToken);
		response.setItemList(items);
		return response;
	} 
	
	static String generateSecurityToken() throws FacadeException {
		return TokenGenerator.generateInternalToken(20);
	}
	
	private static void verifyToken(ShoppingCartDto shoppingCartDto, CartRequestDto request) throws CartSecurityException {
		String securityToken = request.getSecurityToken(); 
		if(securityToken != null &&  securityToken.equals(shoppingCartDto.getSecurityToken())) {
			return ;
		}
		throw new CartSecurityException("invalid security token", securityToken, null);
		
	}
	
	private static void verifyCartData(ShoppingCartDto shoppingCartDto, CartRequestDto request) throws CartVerifyException {
		String codiceSocieta = shoppingCartDto.getCodiceSocieta();
		String canalePagamento = shoppingCartDto.getCanalePagamento();
		if (codiceSocieta.equals(request.getCodiceSocieta()) && canalePagamento.equals(request.getCanalePagamento())) {
			return;
		}
		throw new CartVerifyException("I dati forniti {codiceSocieta='"+
				request.getCodiceSocieta() + "', canalePagamento='" + request.getCanalePagamento() + "', cartSessionId='" + 
				request.getCartSessionId() +"'} non corrispondono ad una carrello attivo. Possibile causa: dati in ingresso errati o sessione scaduta");
	}

}
