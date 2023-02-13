/**
 * 
 */
package com.seda.cart.facade.bean;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import com.seda.cart.facade.dto.CartItemRequestDto;
import com.seda.cart.facade.dto.CartRequestDto;
import com.seda.cart.facade.dto.CartResponseDto;
import com.seda.cart.facade.dto.CartSessionRequestDto;
import com.seda.cart.facade.dto.ItemDto;
import com.seda.cart.facade.exception.CartFacadeException;
import com.seda.cart.facade.exception.CartSecurityException;
import com.seda.cart.facade.exception.CartVerifyException;
import com.seda.cart.facade.exception.FacadeException;
import com.seda.cart.facade.exception.SessionLimitExceededException;
import com.seda.cart.facade.handler.BaseFacadeHandler;

import com.seda.cart.facade.model.ShoppingCartDto;
import com.seda.commons.properties.tree.PropertiesTree;

import com.seda.j2ee5.jndi.JndiProxyException;
import com.seda.payer.core.bean.AbilitaCanalePagamentoTipoServizioEnte;
import com.seda.payer.core.bean.AbilitaSistemiEsterniSecureSite;
import com.seda.payer.core.bean.CarrelloVirtuale;
import com.seda.payer.core.bean.ConfigSessioneCarrello;
import com.seda.payer.core.bean.Item;
import com.seda.payer.core.bean.ItemAttribute;
import com.seda.payer.core.dao.AbilitaCanalePagamentoTipoServizioEnteDao;
import com.seda.payer.core.dao.AbilitaSistemiEsterniSecureSiteDao;
import com.seda.payer.core.dao.CarrelloVirtualeDao;
import com.seda.payer.core.exception.DaoException;



public class CarrelloVirtualeBean extends BaseFacadeHandler {

	private static final long serialVersionUID = 1L;
	
	//inizio LP PG200070 - 20200812
	//public CarrelloVirtualeBean() throws Exception
	//{
	//	super();
	//}
	public CarrelloVirtualeBean(PropertiesTree propertiesTree) throws Exception {
		super(propertiesTree);
	}
	//fine LP PG200070 - 20200812
	
	public CartResponseDto addItemToCart(CartItemRequestDto request, String dbSchemaCodSocieta) throws FacadeException, SessionLimitExceededException {
		verifyCallerUrl(request, dbSchemaCodSocieta);
		verifyItem(request, dbSchemaCodSocieta);
		String cartId = request.getCartSessionId();
		logger.info("addItem: cartId=["+cartId+"] codice società=["+request.getCodiceSocieta()+"] canale pagamento=[" + request.getCanalePagamento() +"]");
		ItemDto itemDto = request.getItemDto();
		Item item = itemDto.getItem();
		ShoppingCartDto shoppingCartDto = null;
		//carico il vecchio valore per poterlo restituire in caso di eccezione
		String generatedToken= request.getSecurityToken();
		
		Connection conn=null;
		try {
			//recupero dal db il carrello
			if (cartId != null && cartId.length() > 0 )  {
				shoppingCartDto=loadShoppingCart(cartId, dbSchemaCodSocieta);
				CarrelloVirtualeFacadeHelper.verify(shoppingCartDto, request);
				if (!shoppingCartDto.isStateful()) {
					throw new FacadeException("Operazione non valida per carrello in modalità stateless");
				}
			}	
			//creo un nuovo carrello
			else {
				generatedToken = CarrelloVirtualeFacadeHelper.generateSecurityToken();
				shoppingCartDto = saveNewCart(request.getCodiceSocieta(), request.getCanalePagamento(), generatedToken, dbSchemaCodSocieta);
				cartId = shoppingCartDto.getShoppingCartId();
			}
			item.setIdCarrello(cartId);
			item.setCodiceSocieta(request.getCodiceSocieta());
			shoppingCartDto.addItem(itemDto);
			conn = getConnection(dbSchemaCodSocieta, true);
			CarrelloVirtualeDao dao = new CarrelloVirtualeDao(conn, getSchema(dbSchemaCodSocieta));
			generatedToken = CarrelloVirtualeFacadeHelper.generateSecurityToken();
			// aggiornamento del token
			String itemId = dao.addItem(item, generatedToken);
			itemDto.setIdItem(itemId);
			CartResponseDto response = CarrelloVirtualeFacadeHelper.generateResponse(cartId, generatedToken, shoppingCartDto.getItemsAsList());
			return response;
		} 
		catch (DaoException ex) {
			logger.error("addItemToCart failed, generic error due to: ", ex);
			throw new FacadeException("addItemToCart failed, generic error due to:" + ex.getMessage());
		}
		catch (JndiProxyException ex) {
			logger.error("addItemToCart failed, generic error due to: ", ex);
			throw new FacadeException("addItemToCart failed, generic error due to:" + ex.getMessage());
		}
		catch (SessionLimitExceededException ex) {
			logger.error("addItemToCart failed: ", ex);
			throw new SessionLimitExceededException(ex.getMessage(),ex.getSessionLimit());
		}
		finally{
			//inizio LP PG21XX04 Leak
    		//closeConnection(conn);
	    	try {
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
	}

	/** 
	 * @ejb.interface-method view-type="remote"
	 * @ejb.transaction type="NotSupported"
	 */
	public CartResponseDto getItemList(CartRequestDto request, String dbSchemaCodSocieta) throws FacadeException {
		verifyCallerUrl(request, dbSchemaCodSocieta);
		String cartId = request.getCartSessionId();
		logger.info("getItemList: cartId=["+cartId+"] codice società=["+request.getCodiceSocieta()+"] canale pagamento=[" + request.getCanalePagamento() +"]");
		ShoppingCartDto shoppingCartDto=loadShoppingCart(cartId, dbSchemaCodSocieta);
		CarrelloVirtualeFacadeHelper.verify(shoppingCartDto, request);
		String generatedToken = CarrelloVirtualeFacadeHelper.generateSecurityToken();
		Connection conn=null;
   		try {
   			conn=getConnection(dbSchemaCodSocieta, true);
			CarrelloVirtualeDao shoppingCartDao = new CarrelloVirtualeDao(conn, getSchema(dbSchemaCodSocieta));
			// aggiornamento del token
			shoppingCartDao.updateSecurityToken(cartId, generatedToken);
			CartResponseDto response = CarrelloVirtualeFacadeHelper.generateResponse(cartId, generatedToken, shoppingCartDto.getItemsAsList());
			return response;
		} 
		catch (DaoException ex) {
			logger.error("getItemList failed, generic error due to: ", ex);
			throw new FacadeException("getItemList failed, generic error due to:" + ex.getMessage());
		}
		catch (JndiProxyException ex) {
			logger.error("getItemList failed, generic error due to: ", ex);
			throw new FacadeException("getItemList failed, generic error due to:" + ex.getMessage());
		}
		finally{
			//inizio LP PG21XX04 Leak
    		//closeConnection(conn);
	    	try {
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
		
	}

	/** 
	 * @ejb.interface-method view-type="remote"
	 * @ejb.transaction type="NotSupported"
	 */
	public CartResponseDto deleteCart(CartRequestDto request, String dbSchemaCodSocieta) throws FacadeException {
		verifyCallerUrl(request, dbSchemaCodSocieta);
		String cartId = request.getCartSessionId();
		logger.info("deleteCart: cartId=["+cartId+"] codice società=["+request.getCodiceSocieta()+"] canale pagamento=[" + request.getCanalePagamento() +"]");
		ShoppingCartDto shoppingCartDto = getShoppingCart(cartId, dbSchemaCodSocieta);
		CarrelloVirtualeFacadeHelper.verify(shoppingCartDto, request);
		Connection conn=null;
   		try {
   			conn=getConnection(dbSchemaCodSocieta, true);
   			CarrelloVirtualeDao shoppingCartDao = new CarrelloVirtualeDao(conn, getSchema(dbSchemaCodSocieta));
			shoppingCartDao.doDelete(cartId);
			CartResponseDto response = CarrelloVirtualeFacadeHelper.generateResponse(cartId, null, shoppingCartDto.getItemsAsList());
			return response;
		} 
		catch (DaoException ex) {
			logger.error("getItemList failed, generic error due to: ", ex);
			throw new FacadeException("getItemList failed, generic error due to:" + ex.getMessage());
		}
		catch (JndiProxyException ex) {
			logger.error("getItemList failed, generic error due to: ", ex);
			throw new FacadeException("getItemList failed, generic error due to:" + ex.getMessage());
		}
		finally{
			//inizio LP PG21XX04 Leak
    		//closeConnection(conn);
	    	try {
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
		
	}
	/** 
	 * @ejb.interface-method view-type="remote"
	 * @ejb.transaction type="NotSupported"
	 */
	public CartResponseDto removeItem(CartItemRequestDto request, String dbSchemaCodSocieta) throws FacadeException {
		verifyCallerUrl(request, dbSchemaCodSocieta);
		String cartId = request.getCartSessionId();
		logger.info("removeItem: cartId=["+cartId+"] codice società=["+request.getCodiceSocieta()+"] canale pagamento=[" + request.getCanalePagamento() +"]");
		ShoppingCartDto shoppingCartDto = getShoppingCart(cartId, dbSchemaCodSocieta);
		if (!shoppingCartDto.isStateful()) {
			throw new FacadeException("Operazione non valida per carrello in modalità stateless");
		}
		CarrelloVirtualeFacadeHelper.verify(shoppingCartDto, request);
		String generatedToken = CarrelloVirtualeFacadeHelper.generateSecurityToken();
		Item item = request.getItemDto().getItem();
		item.setIdCarrello(request.getCartSessionId());
		Connection conn=null;
		try {
			// aggiornamento del token
			conn=getConnection(dbSchemaCodSocieta, true);
			new CarrelloVirtualeDao(conn, getSchema(dbSchemaCodSocieta)).deleteItem(item, generatedToken);
		} 
		catch (DaoException ex) {
			logger.error("removeItem failed, generic error due to: ", ex);
			throw new FacadeException("removeItem failed, generic error due to:" + ex.getMessage());
		}
		catch (JndiProxyException ex) {
			logger.error("removeItem failed, generic error due to: ", ex);
			throw new FacadeException("removeItem failed, generic error due to:" + ex.getMessage());
		}
		finally{
			//inizio LP PG21XX04 Leak
    		//closeConnection(conn);
	    	try {
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
		
		
		// carica dal DB il carrello modificato
		try {
			shoppingCartDto = loadShoppingCart(cartId, dbSchemaCodSocieta );
			return CarrelloVirtualeFacadeHelper.generateResponse(request.getCartSessionId(), generatedToken, shoppingCartDto.getItemsAsList());
		} 
		catch (Exception ex) {
			logger.error("removeItem failed, generic error due to: ", ex);
			throw new CartFacadeException("removeItem failed, generic error due to: " + ex.getMessage(), generatedToken);
		}
	}

	/** 
	 * 
	 * @ejb.interface-method view-type="remote"
	 * @ejb.transaction type="NotSupported"
	 */
	public CartResponseDto updateItem(CartItemRequestDto request, String dbSchemaCodSocieta) throws FacadeException {
		verifyCallerUrl(request, dbSchemaCodSocieta);
		String cartId = request.getCartSessionId();
		logger.info("updateItem: cartId=["+cartId+"] codice società=["+request.getCodiceSocieta()+"] canale pagamento=[" + request.getCanalePagamento() +"]");
		ShoppingCartDto shoppingCartDto = getShoppingCart(cartId, dbSchemaCodSocieta);
		if (!shoppingCartDto.isStateful()) {
			throw new FacadeException("Operazione non valida per carrello in modalità stateless");
		}
		CarrelloVirtualeFacadeHelper.verify(shoppingCartDto, request);
		String generatedToken = CarrelloVirtualeFacadeHelper.generateSecurityToken();
		Item item = request.getItemDto().getItem();
		item.setIdCarrello(request.getCartSessionId());
		Connection conn=null;
		try {
			conn=getConnection(dbSchemaCodSocieta, true);
			CarrelloVirtualeDao dao = new CarrelloVirtualeDao(conn, getSchema(dbSchemaCodSocieta));
			// aggiornamento del token
			dao.updateItem(item, generatedToken);
		} 
		catch (DaoException ex) {
			logger.error("updateItem failed, generic error due to: ", ex);
			throw new FacadeException("updateItem failed, generic error due to:" + ex.getMessage());
		}
		catch (JndiProxyException ex) {
			logger.error("updateItem failed, generic error due to: ", ex);
			throw new FacadeException("updateItem failed, generic error due to:" + ex.getMessage());
		}
		finally{
			//inizio LP PG21XX04 Leak
    		//closeConnection(conn);
	    	try {
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
		
		// carica dal DB il carrello modificato
		try {
			shoppingCartDto = loadShoppingCart(cartId, dbSchemaCodSocieta);
			return CarrelloVirtualeFacadeHelper.generateResponse(request.getCartSessionId(), generatedToken, shoppingCartDto.getItemsAsList());
		} 
		catch (Exception ex) {
			logger.error("updateItem failed, generic error due to: ", ex);
			throw new CartFacadeException("updateItem failed, generic error due to: " + ex.getMessage(), generatedToken);
		}
	}
	
	/** 
	 * @ejb.interface-method view-type="remote"
	 * @ejb.transaction type="NotSupported"
	 */
	public CartResponseDto getItemListForSession(CartSessionRequestDto request, String dbSchemaCodSocieta) throws FacadeException {
		verifyCallerUrl(request, dbSchemaCodSocieta);
		String cartId = request.getCartSessionId();
		String codiceUtente = request.getCodiceUtente();
		logger.info("getItemListForSession: cartId=["+cartId+"] codice società=["+request.getCodiceSocieta()+"] canale pagamento=[" + request.getCanalePagamento() +"] codice utente=[" + codiceUtente + "]");
		ShoppingCartDto shoppingCartDto=loadShoppingCart(cartId, dbSchemaCodSocieta);
		CarrelloVirtualeFacadeHelper.verify(shoppingCartDto, request);
		String generatedToken = CarrelloVirtualeFacadeHelper.generateSecurityToken();
   		Connection conn=null;
		try {
			conn=getConnection(dbSchemaCodSocieta, true);
   			CarrelloVirtualeDao shoppingCartDao = new CarrelloVirtualeDao(conn, getSchema(dbSchemaCodSocieta));
			// aggiornamento del token
			shoppingCartDao.updateSecurityToken(cartId, generatedToken);
			CartResponseDto response = CarrelloVirtualeFacadeHelper.generateResponse(cartId, generatedToken, shoppingCartDto.getItemsForSession(codiceUtente));
			return response;
		} 
		catch (DaoException ex) {
			logger.error("getItemList failed, generic error due to: ", ex);
			throw new FacadeException("getItemList failed, generic error due to:" + ex.getMessage());
		}
		catch (JndiProxyException ex) {
			logger.error("getItemList failed, generic error due to: ", ex);
			throw new FacadeException("getItemList failed, generic error due to:" + ex.getMessage());
		}finally{
			//inizio LP PG21XX04 Leak
    		//closeConnection(conn);
	    	try {
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
		
		
	}

	/** 
	 * 
	 * @ejb.interface-method view-type="remote"
	 * @ejb.transaction type="NotSupported"
	 */
	public void purgeCart(String dbSchemaCodSocieta) throws FacadeException {
    	
		Connection conn=null;
		try {
			conn=getConnection(dbSchemaCodSocieta, true);
    		CarrelloVirtualeDao dao = new CarrelloVirtualeDao(conn, getSchema(dbSchemaCodSocieta));
			dao.purgeCart();
    	}
		catch (DaoException ex) {
			logger.error("purgeCart failed, generic error due to: ", ex);
			throw new FacadeException("purgeCart failed, generic error due to:" + ex.getMessage());
		}
		catch (JndiProxyException ex) {
			logger.error("purgeCart failed, generic error due to: ", ex);
			throw new FacadeException("purgeCart failed, generic error due to:" + ex.getMessage());
		}
		finally{
			//inizio LP PG21XX04 Leak
    		//closeConnection(conn);
	    	try {
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
	}
	
	private ShoppingCartDto getShoppingCart(String cartSessionId, String dbSchemaCodSocieta)  throws FacadeException {
    	Connection conn=null;
		try { 
			if (cartSessionId == null || cartSessionId.length()==0) { 
				throw new FacadeException("Invalid cart session ID");
			}
			
			conn=getConnection(dbSchemaCodSocieta, true);
			CarrelloVirtuale shoppingCart = new CarrelloVirtualeDao(conn, getSchema(dbSchemaCodSocieta)).doDetail(cartSessionId);
			if (shoppingCart == null) {
				throw new FacadeException("Nessun carrello trovato con l'identificativo di sessione fornito ("+cartSessionId+")");
			}
			return new ShoppingCartDto(shoppingCart);
    	} 
		catch (DaoException ex) {
			logger.error("getShoppingCart failed, generic error due to: ", ex);
			throw new FacadeException("getShoppingCart failed, generic error due to:" + ex.getMessage());
		}
		catch (JndiProxyException ex) {
			logger.error("getShoppingCart failed, generic error due to: ", ex);
			throw new FacadeException("getShoppingCart failed, generic error due to:" + ex.getMessage());
		}
		finally{
			//inizio LP PG21XX04 Leak
    		//closeConnection(conn);
	    	try {
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
	}
	
	//carica dal db il carrello e tutti gli item contenuti
	private ShoppingCartDto loadShoppingCart(String shoppingCartId, String dbSchemaCodSocieta )  throws FacadeException {
	   	Connection conn=null;
		try { 
	   		conn=getConnection(dbSchemaCodSocieta, true);
			CarrelloVirtualeDao shoppingCartDao = new CarrelloVirtualeDao(conn, getSchema(dbSchemaCodSocieta));
	   		ShoppingCartDto shoppingCartDto = getShoppingCart(shoppingCartId, dbSchemaCodSocieta);
	   		List<Item> items = shoppingCartDao.getAllItems(shoppingCartId);
	   		for (Item item : items) {
	   			List<ItemAttribute> attributes = shoppingCartDao.getAllAttributes(item.getIdItem());
	   			item.setAttributeList(attributes);
	   			shoppingCartDto.addItem(new ItemDto(item));
			}
	   		return shoppingCartDto;
	   	}
		catch (DaoException ex) {
			logger.error("loadShoppingCart failed, generic error due to: ", ex);
			throw new FacadeException("loadShoppingCart failed, generic error due to:" + ex.getMessage());
		}
		catch (JndiProxyException ex) {
			logger.error("loadShoppingCart failed, generic error due to: ", ex);
			throw new FacadeException("loadShoppingCart failed, generic error due to:" + ex.getMessage());
		}
		finally{
			//inizio LP PG21XX04 Leak
    		//closeConnection(conn);
	    	try {
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
	}
	
	private ShoppingCartDto saveNewCart(String codiceSocieta, String canalePagamento, String generatedToken, String dbSchemaCodSocieta) throws FacadeException {
		Connection conn=null;
		ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
		logger.info("creazione nuovo carrello...");
		try {
			shoppingCartDto.setCodiceSocieta(codiceSocieta);
			shoppingCartDto.setCanalePagamento(canalePagamento);
			shoppingCartDto.setSecurityToken(generatedToken);
			conn=getConnection(dbSchemaCodSocieta, true);
			CarrelloVirtualeDao dao = new CarrelloVirtualeDao(conn,getSchema(dbSchemaCodSocieta));
			ConfigSessioneCarrello config = dao.getCartSessionConfig(codiceSocieta, canalePagamento);
			if (config == null) {
				logger.error("Nessuna configurazione di sessione trovata per codice società = " + codiceSocieta + " e canale pagamento = " + canalePagamento);
				throw new FacadeException("Nessuna configurazione di sessione trovata per codice società = " + codiceSocieta + " e canale pagamento = " + canalePagamento);
			}
			shoppingCartDto.setMaxSessions(config.getMaxSessions());
			shoppingCartDto.setStateful(config.isStateful());
			String cartId = dao.doSave(shoppingCartDto.getShoppingCart());
			shoppingCartDto.setShoppingCartId(cartId);
			logger.info("...nuovo carrello creato: id=["+cartId+"] numero max sessioni=["+shoppingCartDto.getMaxSessions()+"] stateful=["+shoppingCartDto.isStateful()+"]");
			return shoppingCartDto;
		}
		catch (DaoException ex) {
			logger.error("saveNewCart failed, generic error due to: ", ex);
			throw new FacadeException("saveNewCart failed, generic error due to:" + ex.getMessage());
		}
		catch (JndiProxyException ex) {
			logger.error("saveNewCart failed, generic error due to: ", ex);
			throw new FacadeException("saveNewCart failed, generic error due to:" + ex.getMessage());
		}
		finally{
			//inizio LP PG21XX04 Leak
    		//closeConnection(conn);
	    	try {
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
	}
	

	
	// controllo se è possibile aggiungere l'item al carrello
	private void verifyItem(CartItemRequestDto request, String dbSchemaCodSocieta) throws FacadeException {
		Connection conn=null;
		ItemDto item = request.getItemDto();
		logger.info("verifica abilitazioni per item: codice utente=[" + item.getCodiceUtente() + "] codice tipologia servizio=[" 
				+ item.getCodiceTipologiaServizio() + "] codice imposta servizio=[" + item.getCodiceImpostaServizio() + "] chiave ente=[" + item.getChiaveEnte() + "]");
		try {
			
			
			/*ImpostaServizioDao impostaServizioDao = new ImpostaServizioDao(getConnection(true), getSchema());
			ImpostaServizio bean = impostaServizioDao.doDetail(request.getCodiceSocieta(), item.getCodiceTipologiaServizio(), item.getCodiceImpostaServizio());
			if (bean == null) {
				throw new CartVerifyException("Impossibile aggiungere l'item al carrello. Servizio non abilitato per per il codice società indicato");
			}*/
			conn=getConnection(dbSchemaCodSocieta, true);
			AbilitaCanalePagamentoTipoServizioEnteDao abilitaDao = new AbilitaCanalePagamentoTipoServizioEnteDao(conn,getSchema(dbSchemaCodSocieta));
			AbilitaCanalePagamentoTipoServizioEnte abilita = abilitaDao.doDetail(request.getCodiceSocieta(), item.getCodiceUtente(), item.getChiaveEnte(), request.getCanalePagamento(), item.getCodiceTipologiaServizio());
			if (abilita == null  || !("Y".equalsIgnoreCase(abilita.getFlagAttivazione()))) {
				abilita = abilitaDao.doDetail(request.getCodiceSocieta(), item.getCodiceUtente(), item.getChiaveEnte(), "PSP", item.getCodiceTipologiaServizio());
				if (abilita == null  || !("Y".equalsIgnoreCase(abilita.getFlagAttivazione()))) {
					throw new CartVerifyException("Impossibile aggiungere l'item al carrello. Servizio non abilitato per il canale di pagamento indicato");
				}
			}				
		} 
		catch (JndiProxyException ex) {
			logger.error("isEnabled failed, generic error due to: ", ex);
			throw new FacadeException("verifyItem failed, generic error due to: " + ex.getMessage());
		} 
		catch (com.seda.payer.core.exception.DaoException ex) {
			logger.error("isEnabled failed, generic error due to: ", ex);
			throw new FacadeException("verifyItem failed, generic error due to: " + ex.getMessage());
		}
		finally{
			//inizio LP PG21XX04 Leak
    		//closeConnection(conn);
	    	try {
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
	}
	
	private void verifyCallerUrl(CartRequestDto request, String dbSchemaCodSocieta) throws FacadeException {
		Connection conn=null;
		logger.info("verifica URL chiamante ["+request.getCallerURL()+"]");
		logger.info("dbSchemaCodSocieta ["+dbSchemaCodSocieta+"]");
		try {
			conn=getConnection(dbSchemaCodSocieta, true);
			AbilitaSistemiEsterniSecureSiteDao dao = new AbilitaSistemiEsterniSecureSiteDao(conn, getSchema(dbSchemaCodSocieta));
			AbilitaSistemiEsterniSecureSite bean = dao.doDetail(request.getCallerURL(), "");
			if (bean == null || !("Y".equalsIgnoreCase(bean.getFlagAttivazione()))) {
				throw new CartSecurityException("Impossibile eseguire l'operazione. URL chiamante non abilitata", request.getSecurityToken(), request.getCallerURL());
			}
		}
		catch (JndiProxyException ex) {
			logger.error("verifyCallerUrl failed, generic error due to: ", ex);
			logger.info("verifyCallerUrl failed, generic error due to: " + ex);
			throw new FacadeException("verifyCallerUrl failed, generic error due to: " + ex.getMessage());
		} 
		catch (com.seda.payer.core.exception.DaoException ex) {
			logger.error("verifyCallerUrl failed, generic error due to: ", ex);
			logger.info("verifyCallerUrl failed, generic error due to: " + ex);
			throw new FacadeException("verifyCallerUrl failed, generic error due to: " + ex.getMessage());
		}
		catch (Throwable ex) {
			logger.error("verifyCallerUrl failed, generic error due to: ", ex);
			logger.info("verifyCallerUrl failed, generic error due to: " + ex);
			throw new FacadeException("verifyCallerUrl failed, generic error due to: " + ex.getMessage());
		}
		finally{
			//inizio LP PG21XX04 Leak
    		//closeConnection(conn);
	    	try {
	    		if(conn != null) {
	    			conn.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
			//fine LP PG21XX04 Leak
		}
		logger.info("verifyCallerUrl finito");
	}

	
}
