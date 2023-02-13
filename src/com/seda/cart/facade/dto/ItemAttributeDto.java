package com.seda.cart.facade.dto;

import java.io.Serializable;

import com.seda.payer.core.bean.ItemAttribute;

public class ItemAttributeDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String key;
    private String value;

    public ItemAttributeDto() {
    }

    public ItemAttributeDto(String key, String value) {
        this.key = key;
        this.value = value;
    }
    
    public ItemAttributeDto(ItemAttribute itemAttribute) {
    	setKey(itemAttribute.getKey());
    	setValue(itemAttribute.getValue());
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

	public ItemAttribute getItemAttribute() {
		ItemAttribute itemAttribute = new ItemAttribute();
		itemAttribute.setKey(key);
		itemAttribute.setValue(value);
		return itemAttribute;
	}
	
    @Override
    public String toString() {
        return "ItemAttributeDto{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
 
    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      ItemAttributeDto that = (ItemAttributeDto) o;

      if (key != null ? !key.equals(that.key) : that.key != null) {
        return false;
      }
      if (value != null ? !value.equals(that.value) : that.value != null) {
        return false;
      }

      return true;
    }
 
    @Override
    public int hashCode() {
      int result = key != null ? key.hashCode() : 0;
      result = 31 * result + (value != null ? value.hashCode() : 0);
      return result;
    }

}
