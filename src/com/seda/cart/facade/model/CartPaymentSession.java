package com.seda.cart.facade.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.seda.cart.facade.dto.ItemDto;

public class CartPaymentSession {

	List<ItemDto> items = new ArrayList<ItemDto>();

    public void addItem(ItemDto item) {
        items.add(item);
    }

    public void removeItem(ItemDto item) {
        items.remove(item);
    }

    public boolean contains(ItemDto itemDto) {
        return items.contains(itemDto);
    }

    public List<ItemDto> getItems() {
        return Collections.unmodifiableList(items);
    }

}
