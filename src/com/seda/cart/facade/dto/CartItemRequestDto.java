package com.seda.cart.facade.dto;

public class CartItemRequestDto extends CartRequestDto {
	
	private static final long serialVersionUID = 1L;

	ItemDto itemDto;

	public ItemDto getItemDto() {
		return itemDto;
	}

	public void setItemDto(ItemDto itemDto) {
		this.itemDto = itemDto;
	}

}
