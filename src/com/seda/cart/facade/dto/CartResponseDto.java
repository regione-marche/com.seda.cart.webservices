package com.seda.cart.facade.dto;

import java.io.Serializable;
import java.util.List;

public class CartResponseDto implements Serializable {
	
 	private static final long serialVersionUID = 1L;
	private String cartSessionId;
    private String securityToken;
    private String responseCode;
	private List<ItemDto> itemList;

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

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public List<ItemDto> getItemList() {
		return itemList;
	}

	public void setItemList(List<ItemDto> itemList) {
		this.itemList = itemList;
	}

    @Override
    public String toString() {
        return "CartResponseDto{" +
                "cartSessionId='" + cartSessionId + '\'' +
                ", securityToken='" + securityToken + '\'' +
                ", responseCode='" + responseCode + '\'' +
                ", itemList=" + itemList +
                '}';
    }
	
}
