/**
 * WebServiceCartPortBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf280936.11 v91609102051
 */

package com.seda.cart.webservices.source;



import com.seda.cart.facade.bean.CarrelloVirtualeBean;
import com.seda.cart.facade.dto.CartItemRequestDto;
import com.seda.cart.facade.dto.CartRequestDto;
import com.seda.cart.facade.dto.CartResponseDto;
import com.seda.cart.facade.dto.ItemAttributeDto;
import com.seda.cart.facade.dto.ItemDto;
import com.seda.cart.facade.exception.CartFacadeException;
import com.seda.cart.facade.exception.FacadeException;
import com.seda.cart.facade.exception.SessionLimitExceededException;
import com.seda.cart.webservices.dati.AddItemWSResponse;
import com.seda.cart.webservices.dati.DeleteItemWSResponse;
import com.seda.cart.webservices.dati.GetItemsWSResponse;
import com.seda.cart.webservices.dati.Item;
import com.seda.cart.webservices.dati.ItemAttribute;
import com.seda.cart.webservices.dati.PurgeCartWSResponse;
import com.seda.cart.webservices.dati.UpdateItemWSResponse;
import com.seda.cart.webservices.handler.WebServiceHandler;

public class WebServiceCartPortBindingImpl extends WebServiceHandler implements com.seda.cart.webservices.source.WebServiceCart{
	//inizio LP PG200070 - 20200812
	private CarrelloVirtualeBean carrelloFacade = null;

	private CarrelloVirtualeBean getFacadeService() throws Exception
	{
		if(carrelloFacade == null) {
			carrelloFacade = new CarrelloVirtualeBean(configuration);
		}
		return carrelloFacade;
	}
	//fine LP PG200070 - 20200812
    
	public com.seda.cart.webservices.dati.AddItemWSResponse addItem(com.seda.cart.webservices.dati.AddItemWSRequest arg0) throws java.rmi.RemoteException, com.seda.cart.webservices.srv.FaultType {

		AddItemWSResponse addItemResponse = new AddItemWSResponse();
    	addItemResponse.setResponseCode("00");
    	addItemResponse.setResponseMessage("Operazione effettuata con successo!!");

    	try {	
    		//inizio LP PG200070 - 20200812
    		//CarrelloVirtualeBean service = new CarrelloVirtualeBean();
    		CarrelloVirtualeBean service = getFacadeService();
    		//fine LP PG200070 - 20200812
        	
        	Item item = arg0.getItem();
        	// Creates WS request object
        	ItemDto itemDto = getItemDto(item);
        	CartItemRequestDto itemRequestDto = new CartItemRequestDto();
        	
        	// Get the request URL for security check
			if (getWebServiceContext() == null) {
				itemRequestDto.setCallerURL("127.0.0.1");
			}
			else {
				itemRequestDto.setCallerURL(getWebServiceContext().showMessageContext());
			}
        	itemRequestDto.setItemDto(itemDto);
        	itemRequestDto.setCanalePagamento(arg0.getCanalePagamento());
        	itemRequestDto.setCartSessionId(arg0.getCartSessionId());
        	itemRequestDto.setCodiceSocieta(arg0.getCodiceSocieta());
        	itemRequestDto.setSecurityToken(arg0.getSecurityToken());
        	
        	CartResponseDto result = service.addItemToCart(itemRequestDto, dbSchemaCodSocieta);
        	
        	// Creates WS response object      	
        	Item[] itemListResponse = generateItemArrayFromResponse(result);
        	
        	
        	addItemResponse.setCartSessionId(result.getCartSessionId());
        	addItemResponse.setSecurityToken(result.getSecurityToken());
        	addItemResponse.setItems(itemListResponse);
        	
		}
		catch (CartFacadeException e) {
			
			error(e.getMessage());
	    	addItemResponse.setResponseCode("01");
	    	addItemResponse.setResponseMessage(e.getMessage());
		}
		catch (SessionLimitExceededException e) {
			error(e.getMessage());
			
	    	addItemResponse.setResponseCode("04");
	    	addItemResponse.setResponseMessage(e.getMessage());
		}
		catch (FacadeException e) {
			
			error(e.getMessage());
			
	    	addItemResponse.setResponseCode("02");
	    	addItemResponse.setResponseMessage(e.getMessage());
		}
		catch (Exception e) {
			
			error(e.getMessage());
	    	addItemResponse.setResponseCode("03");
	    	addItemResponse.setResponseMessage(e.getMessage());
		}
		
		return addItemResponse;
    }

    public com.seda.cart.webservices.dati.GetItemsWSResponse getItems(com.seda.cart.webservices.dati.GetItemsWSRequest arg0) throws java.rmi.RemoteException, com.seda.cart.webservices.srv.FaultType {
    	
    	GetItemsWSResponse getItemsResponse = new GetItemsWSResponse();
    	getItemsResponse.setResponseCode("00");
    	getItemsResponse.setResponseMessage("Operazione effettuata con successo!!");
    	
    	try {						
    		//inizio LP PG200070 - 20200812
    		//CarrelloVirtualeBean service = new CarrelloVirtualeBean();
    		CarrelloVirtualeBean service = getFacadeService();
    		//fine LP PG200070 - 20200812
        	
        	// Creates WS request object
        	CartRequestDto cartRequestDto = new CartRequestDto();
        	
        	// Get the request URL for security check
			if (getWebServiceContext() == null) {
				cartRequestDto.setCallerURL("127.0.0.1");
			}
			else {
				cartRequestDto.setCallerURL(getWebServiceContext().showMessageContext());
			}
        	cartRequestDto.setCartSessionId(arg0.getCartSessionId());
        	cartRequestDto.setSecurityToken(arg0.getSecurityToken());
        	cartRequestDto.setCodiceSocieta(arg0.getCodiceSocieta());
        	cartRequestDto.setCanalePagamento(arg0.getCanalePagamento());
        	
        	CartResponseDto result = service.getItemList(cartRequestDto, dbSchemaCodSocieta);
        	
        	// Creates WS response object      	
        	Item[] itemListResponse = generateItemArrayFromResponse(result);
        	
        	
        	getItemsResponse.setCartSessionId(result.getCartSessionId());
        	getItemsResponse.setSecurityToken(result.getSecurityToken());
        	getItemsResponse.setItems(itemListResponse);
        	
		}
		catch (CartFacadeException e) {
			
			error(e.getMessage());
			getItemsResponse.setResponseCode("01");
			getItemsResponse.setResponseMessage(e.getMessage());
			//throw new FaultType(e.getErrorCode(),e.getErrorDescription(),e.getSecurityToken());
		}
		catch (FacadeException e) {
			
			error(e.getMessage());
			getItemsResponse.setResponseCode("02");
			getItemsResponse.setResponseMessage(e.getMessage());
			//throw new FaultType(e.getErrorCode(),e.getErrorDescription(),arg0.getSecurityToken());
		}
		catch (Exception e) {
			
			error(e.getMessage());
			getItemsResponse.setResponseCode("03");
			getItemsResponse.setResponseMessage(e.getMessage());
			//throw new FaultType(505,e.getMessage(),arg0.getSecurityToken());
		}
		
		return getItemsResponse;
    }


	public com.seda.cart.webservices.dati.DeleteItemWSResponse deleteItem(com.seda.cart.webservices.dati.DeleteItemWSRequest arg0) throws java.rmi.RemoteException, com.seda.cart.webservices.srv.FaultType {
    	
		DeleteItemWSResponse delItemResponse = new DeleteItemWSResponse();
		delItemResponse.setResponseCode("00");
		delItemResponse.setResponseMessage("Operazione effettuata con successo!!");
		
    	try {			
    		//inizio LP PG200070 - 20200812
    		//CarrelloVirtualeBean service = new CarrelloVirtualeBean();
    		CarrelloVirtualeBean service = getFacadeService();
    		//fine LP PG200070 - 20200812

        	Item item = arg0.getItem();
        	// Creates WS request object
        	ItemDto itemDto = getItemDto(item);
        	
        	CartItemRequestDto itemRequestDto = new CartItemRequestDto();
        	
        	// Get the request URL for security check
			if (getWebServiceContext() == null) {
				itemRequestDto.setCallerURL("127.0.0.1");
			}
			else {
				itemRequestDto.setCallerURL(getWebServiceContext().showMessageContext());
			}
        	itemRequestDto.setItemDto(itemDto);
        	itemRequestDto.setCanalePagamento(arg0.getCanalePagamento());
        	itemRequestDto.setCodiceSocieta(arg0.getCodiceSocieta());
        	itemRequestDto.setCartSessionId(arg0.getCartSessionId());
        	itemRequestDto.setSecurityToken(arg0.getSecurityToken());
        	
        	CartResponseDto result = service.removeItem(itemRequestDto, dbSchemaCodSocieta);
        	
        	// Creates WS response object      	
        	Item[] itemListResponse = generateItemArrayFromResponse(result);

        	
        	delItemResponse.setCartSessionId(result.getCartSessionId());
        	delItemResponse.setSecurityToken(result.getSecurityToken());
        	delItemResponse.setItems(itemListResponse);
        	
        	//info("Request execute successfully");
        	
        	
		} 
		catch (CartFacadeException e) {
			
			error(e.getMessage());
			delItemResponse.setResponseCode("01");
			delItemResponse.setResponseMessage(e.getMessage());
			//throw new FaultType(e.getErrorCode(),e.getErrorDescription(),e.getSecurityToken());
		}
		catch (FacadeException e) {
			
			error(e.getMessage());
			delItemResponse.setResponseCode("02");
			delItemResponse.setResponseMessage(e.getMessage());
			//throw new FaultType(e.getErrorCode(),e.getErrorDescription(),arg0.getSecurityToken());
		}
		catch (Exception e) {
			error(e.getMessage());
			
			delItemResponse.setResponseCode("03");
			delItemResponse.setResponseMessage(e.getMessage());
			//throw new FaultType(505,e.getMessage(),arg0.getSecurityToken());
		}
		
		return delItemResponse;
    }

    public com.seda.cart.webservices.dati.UpdateItemWSResponse updateItem(com.seda.cart.webservices.dati.UpdateItemWSRequest arg0) throws java.rmi.RemoteException, com.seda.cart.webservices.srv.FaultType {
    	
    	UpdateItemWSResponse updateItemResponse = new UpdateItemWSResponse();
    	updateItemResponse.setResponseCode("00");
    	updateItemResponse.setResponseMessage("Operazione effettuata con successo!!");
    	
    	try {						
    		//inizio LP PG200070 - 20200812
    		//CarrelloVirtualeBean service = new CarrelloVirtualeBean();
    		CarrelloVirtualeBean service = getFacadeService();
    		//fine LP PG200070 - 20200812
        	
        	Item item = arg0.getItem();
        	// Creates WS request object
        	ItemDto itemDto = getItemDto(item);
        	
        	CartItemRequestDto itemRequestDto = new CartItemRequestDto();
        	
        	// Get the request URL for security check
			if (getWebServiceContext() == null) {
				itemRequestDto.setCallerURL("127.0.0.1");
			}
			else {
				itemRequestDto.setCallerURL(getWebServiceContext().showMessageContext());
			}
        	itemRequestDto.setItemDto(itemDto);
        	itemRequestDto.setCanalePagamento(arg0.getCanalePagamento());
        	itemRequestDto.setCartSessionId(arg0.getCartSessionId());
        	itemRequestDto.setCodiceSocieta(arg0.getCodiceSocieta());
        	itemRequestDto.setSecurityToken(arg0.getSecurityToken());
        	
        	CartResponseDto result = service.updateItem(itemRequestDto, dbSchemaCodSocieta);
        	
        	// Creates WS response object      	
        	// Creates WS response object      	
        	Item[] itemListResponse = generateItemArrayFromResponse(result);
        	
        	
        	updateItemResponse.setCartSessionId(result.getCartSessionId());
        	updateItemResponse.setSecurityToken(result.getSecurityToken());
        	updateItemResponse.setItems(itemListResponse);
        	
        	//info("Request execute successfully");
        	
        	
		} 
		catch (CartFacadeException e) {
			error(e.getMessage());
			updateItemResponse.setResponseCode("01");
			updateItemResponse.setResponseMessage(e.getMessage());
			//throw new FaultType(e.getErrorCode(),e.getErrorDescription(),e.getSecurityToken());
		}
		catch (FacadeException e) {
			error(e.getMessage());
			updateItemResponse.setResponseCode("02");
			updateItemResponse.setResponseMessage(e.getMessage());
			//throw new FaultType(e.getErrorCode(),e.getErrorDescription(),arg0.getSecurityToken());
		}
		catch (Exception e) {
			updateItemResponse.setResponseCode("03");
			updateItemResponse.setResponseMessage(e.getMessage());
			error(e.getMessage());
			//throw new FaultType(505,e.getMessage(),arg0.getSecurityToken());
		}
		
		return updateItemResponse;
    }

    public com.seda.cart.webservices.dati.PurgeCartWSResponse purgeCart(com.seda.cart.webservices.dati.PurgeCartWSRequest arg0) throws java.rmi.RemoteException, com.seda.cart.webservices.srv.FaultType {
    	
    	PurgeCartWSResponse purgeCartResponse = new PurgeCartWSResponse();
    	purgeCartResponse.setResponseCode("00");
    	purgeCartResponse.setResponseMessage("Operazione effettuata con successo!!");
    	
    	try {						
    		//inizio LP PG200070 - 20200812
    		//CarrelloVirtualeBean service = new CarrelloVirtualeBean();
    		CarrelloVirtualeBean service = getFacadeService();
    		//fine LP PG200070 - 20200812
        	       	
        	// Get the request URL
			//MessageContext context = MessageContext.getCurrentContext(); 
			//HttpServletRequest req = (HttpServletRequest) context.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
			 
        	service.purgeCart(dbSchemaCodSocieta);
        	
		} 
		catch (CartFacadeException e) {
			error(e.getMessage());
			purgeCartResponse.setResponseCode("01");
			purgeCartResponse.setResponseMessage(e.getMessage());
			//throw new FaultType(e.getErrorCode(),e.getErrorDescription(),e.getSecurityToken());
		}
		catch (FacadeException e) {
			error(e.getMessage());
			purgeCartResponse.setResponseCode("02");
			purgeCartResponse.setResponseMessage(e.getMessage());
			//throw new FaultType(e.getErrorCode(),e.getErrorDescription(),arg0.getSecurityToken());
		}
		catch (Exception e) {
			error(e.getMessage());
			purgeCartResponse.setResponseCode("03");
			purgeCartResponse.setResponseMessage(e.getMessage());
			//throw new FaultType(505,e.getMessage(),arg0.getSecurityToken());
		}
		
		return purgeCartResponse;
    }

	private Item[] generateItemArrayFromResponse(CartResponseDto result) {
		Item[] itemListResponse = new Item[result.getItemList().size()];   
		int itemIdx = 0;
		for (ItemDto itemDto : result.getItemList()) {
			ItemAttribute[] itemAttributeListResponse = new ItemAttribute[itemDto.getAttributeList().size()];
			int attrIdx = 0;
			for (ItemAttributeDto itemAttributeDto : itemDto.getAttributeList()) {
				ItemAttribute itemAttributeResponse = new ItemAttribute();
				itemAttributeResponse.setKey(itemAttributeDto.getKey());
				itemAttributeResponse.setValue(itemAttributeDto.getValue());
				itemAttributeListResponse[attrIdx++] = itemAttributeResponse;
			}
			Item item = new Item();
			item.setAttributeList(itemAttributeListResponse);
			item.setChiaveEnte(itemDto.getChiaveEnte());
			item.setCodiceImpostaServizio(itemDto.getCodiceImpostaServizio());       		
			item.setCodiceTipologiaServizio(itemDto.getCodiceTipologiaServizio());
			item.setCodiceUtente(itemDto.getCodiceUtente());
			item.setDescrizione(itemDto.getDescrizione());
			item.setIdItem(itemDto.getIdItem());
			item.setPrezzo(itemDto.getPrezzo());
			item.setQuantita(itemDto.getQuantita());
			item.setTipoBollettino(itemDto.getTipoBollettino());
			itemListResponse[itemIdx++] = item;
		}
		return itemListResponse;
	}

	private ItemDto getItemDto(Item item) {
		ItemDto itemDto = new ItemDto();
		if (item.getAttributeList() != null) {
				for (ItemAttribute itemAttribute : item.getAttributeList()) {
					ItemAttributeDto itemAttrDto = new ItemAttributeDto();
					itemAttrDto.setKey(itemAttribute.getKey());
					itemAttrDto.setValue(itemAttribute.getValue());
					itemDto.addAttribute(itemAttrDto);
				}
		}

		itemDto.setChiaveEnte(item.getChiaveEnte());
		itemDto.setCodiceImpostaServizio(item.getCodiceImpostaServizio());
		itemDto.setCodiceTipologiaServizio(item.getCodiceTipologiaServizio());
		itemDto.setCodiceUtente(item.getCodiceUtente());
		itemDto.setDescrizione(item.getDescrizione());
		itemDto.setIdItem(item.getIdItem());
		itemDto.setPrezzo(item.getPrezzo());
		itemDto.setQuantita(item.getQuantita());
		itemDto.setTipoBollettino(item.getTipoBollettino());
		return itemDto;
	}

}
