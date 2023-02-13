package com.seda.cart.facade.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.seda.cart.facade.dto.ItemDto;
import com.seda.cart.facade.exception.FacadeException;
import com.seda.cart.facade.exception.SessionLimitExceededException;
import com.seda.payer.core.bean.CarrelloVirtuale;
import com.seda.payer.core.bean.Item;

public class ShoppingCartDto {
	private String shoppingCartId;
	private String securityToken;
	private String canalePagamento;
	private String codiceSocieta;
	private int maxSessions;
	private boolean stateful = true;
	
	Map<String,CartPaymentSession> sessions = new HashMap<String,CartPaymentSession>();

	public String getShoppingCartId() {
		return shoppingCartId;
	}


	public void setShoppingCartId(String shoppingCartId) {
		this.shoppingCartId = shoppingCartId;
	}


	public String getSecurityToken() {
		return securityToken;
	}


	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
	}
	
	
	public int getMaxSessions() {
		return maxSessions;
	}


	public void setMaxSessions(int maxSessions) {
		this.maxSessions = maxSessions;
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
    
	public boolean isStateful() {
		return stateful;
	}

	public void setStateful(boolean stateful) {
		this.stateful = stateful;
	}

	public ShoppingCartDto(){
	}
	
	public ShoppingCartDto (CarrelloVirtuale shoppingCart){
    	if (shoppingCart == null){
    		return;
		} 
    	setShoppingCartId(shoppingCart.getShoppingCartId());
    	setSecurityToken(shoppingCart.getSecurityToken());
    	setCanalePagamento(shoppingCart.getCanalePagamento());
    	setCodiceSocieta(shoppingCart.getCodiceSocieta());
    	setMaxSessions(shoppingCart.getMaxSessions());
    	setStateful(shoppingCart.isStateful());
    	for (Item item : shoppingCart.getItems()) {
    		CartPaymentSession session = sessions.get(item.getCodiceUtente());
    		if (session == null) {
    			session = new CartPaymentSession();
    			session.addItem(new ItemDto(item));
    			sessions.put(item.getCodiceUtente(), session);
    		}
    		else {
    			session.addItem(new ItemDto(item));
    		}
		}
	}
	
	public void addItem(ItemDto itemDto) throws SessionLimitExceededException, FacadeException  {
        CartPaymentSession session = sessions.get(itemDto.getCodiceUtente());
        if (session == null) {
        	int currentSessions = sessions.size();
        	//verifica se è possibile creare una nuova sessione di pagamento
        	if (currentSessions >= maxSessions) {
        		throw new SessionLimitExceededException("Superato il numero massimo di sessioni ammesse per il carrello", maxSessions);
        	}
            session = new CartPaymentSession();
            sessions.put(itemDto.getCodiceUtente(),session);
        }
        //verifica che l'item non sia già stato inserito nel carrello
        else if (session.contains(itemDto)){
    		throw new FacadeException("Tentativo di inserire un item già presente nel carrello");
        }
        session.addItem(itemDto);
		
	}
	
	public boolean contains(ItemDto itemDto) throws FacadeException {
		return sessions.containsValue(itemDto);
	}
	
	
	public void removeItem(ItemDto itemDto) throws FacadeException {
        //ItemSessionKey sessionKey = new ItemSessionKey(itemDto.getCodiceSocieta(),itemDto.getCodiceUtente());
        CartPaymentSession session = sessions.get(itemDto.getCodiceUtente());
        if (session == null) {
        	throw new FacadeException("Cancellazione dell'item dal carrello fallita: item non presente");
        }
        session.removeItem(itemDto);
	}
	
	public CarrelloVirtuale getShoppingCart() {
		CarrelloVirtuale cart = new CarrelloVirtuale();
		cart.setSecurityToken(getSecurityToken());
		cart.setShoppingCartId(getShoppingCartId());
		cart.setCodiceSocieta(getCodiceSocieta());
		cart.setCanalePagamento(getCanalePagamento());
		cart.setMaxSessions(getMaxSessions());
		cart.setStateful(isStateful());
		cart.setItems(getAllItems());
		return cart;
	}
	
	private List<Item> getAllItems() {
		List<Item> list = new ArrayList<Item>();
		for (CartPaymentSession session : sessions.values()) {
			for (ItemDto itemDto : session.getItems()) {
				Item item = itemDto.getItem();
				item.setIdCarrello(getShoppingCartId());
				list.add(item);
			}
		}
		return list;
	}
	
	public List<ItemDto> getItemsAsList() {
		List<ItemDto> list = new ArrayList<ItemDto>();
		for (CartPaymentSession session : sessions.values()) {
			for (ItemDto itemDto : session.getItems()) {
				list.add(itemDto);
			}
		}
		return list;
	}


	public List<ItemDto> getItemsForSession(String codiceUtente) throws FacadeException {
		CartPaymentSession session  = sessions.get(codiceUtente);
		if (session != null) {
			return session.getItems();
		}
		else {
			throw new FacadeException("Sessione inesistente per il codice utente ["+codiceUtente+"]");
		}
	}
	
	void checkStatefulBehaviour() throws FacadeException {
		if (!isStateful()) {
			throw new FacadeException("Operazione non valida per carrello in modalità stateless");
		}
	}
}
