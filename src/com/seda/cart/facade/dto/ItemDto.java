package com.seda.cart.facade.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.seda.payer.core.bean.Item;
import com.seda.payer.core.bean.ItemAttribute;

public class ItemDto implements Serializable {
    
	private static final long serialVersionUID = 1L;
	private List<ItemAttributeDto> attributeList;
	private String idItem;
    private String codiceUtente;
    private String chiaveEnte;
    private String codiceTipologiaServizio;
    private String codiceImpostaServizio;
    private String tipoBollettino;
    private String descrizione;
    private BigDecimal prezzo;
    private Integer quantita;

    public ItemDto() {
    	attributeList = new ArrayList<ItemAttributeDto>();
    } 
    
    public ItemDto(Item item) {
    	setIdItem(item.getIdItem());
    	//setCodiceSocieta(item.getCodiceSocieta());
    	setCodiceUtente(item.getCodiceUtente());
    	setChiaveEnte(item.getChiaveEnte());
    	setCodiceTipologiaServizio(item.getCodiceTipologiaServizio());
    	setCodiceImpostaServizio(item.getCodiceImpostaServizio());
    	setTipoBollettino(item.getTipoBollettino());
    	//setCanalePagamento(item.getCanalePagamento());
    	setDescrizione(item.getDescrizione());
    	setPrezzo(item.getPrezzo());
    	setQuantita(item.getQuantita());
    	attributeList = new ArrayList<ItemAttributeDto>();
    	for (ItemAttribute itemAttribute : item.getAttributeList()) {
    		attributeList.add(new ItemAttributeDto(itemAttribute));
		}
    } 
    
    public String getIdItem() {
		return idItem;
	}

	public void setIdItem(String idItem) {
		this.idItem = idItem;
	}

    public String getCodiceUtente() {
        return codiceUtente;
    }

    public void setCodiceUtente(String codiceUtente) {
        this.codiceUtente = codiceUtente;
    }

    public String getChiaveEnte() {
        return chiaveEnte;
    }

    public void setChiaveEnte(String chiaveEnte) {
        this.chiaveEnte = chiaveEnte;
    }

    public String getCodiceTipologiaServizio() {
        return codiceTipologiaServizio;
    }

    public void setCodiceTipologiaServizio(String codiceTipologiaServizio) {
        this.codiceTipologiaServizio = codiceTipologiaServizio;
    }

    public String getCodiceImpostaServizio() {
        return codiceImpostaServizio;
    }

    public void setCodiceImpostaServizio(String codiceImpostaServizio) {
        this.codiceImpostaServizio = codiceImpostaServizio;
    }

    public String getTipoBollettino() {
        return tipoBollettino;
    }

    public void setTipoBollettino(String tipoBollettino) {
        this.tipoBollettino = tipoBollettino;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

    public Integer getQuantita() {
		return quantita;
	}

	public void setQuantita(Integer quantita) {
		this.quantita = quantita;
	}

	public List<ItemAttributeDto> getAttributeList() {
        return Collections.unmodifiableList(attributeList);
    }

    public void setAttributeList(List<ItemAttributeDto> attributeList) {
        this.attributeList = attributeList;
    }

    public void addAttribute(ItemAttributeDto itemAttribute) {
        attributeList.add(itemAttribute);
    }

	public Item getItem() {
		Item item = new Item();
    	item.setIdItem(idItem);
    	//item.setCodiceSocieta(codiceSocieta);
    	item.setCodiceUtente(codiceUtente);
    	item.setChiaveEnte(chiaveEnte);
    	item.setCodiceTipologiaServizio(codiceTipologiaServizio);
    	item.setCodiceImpostaServizio(codiceImpostaServizio);
    	item.setTipoBollettino(tipoBollettino);
    	//item.setCanalePagamento(canalePagamento);
    	item.setDescrizione(descrizione);
    	item.setPrezzo(prezzo);
    	item.setQuantita(quantita);
    	for (ItemAttributeDto itemAttributeDto : getAttributeList()) {
    		ItemAttribute itemAttribute = itemAttributeDto.getItemAttribute();
    		itemAttribute.setIdItem(idItem);
			item.addAttribute(itemAttribute);
		}
		return item;
	}

	@Override
    public String toString() {
        return "ItemDto{" +
                "attributeList=" + attributeList +
                ", codiceUtente='" + codiceUtente + '\'' +
                ", chiaveEnte='" + chiaveEnte + '\'' +
                ", codiceTipologiaServizio='" + codiceTipologiaServizio + '\'' +
                ", codiceImpostaServizio='" + codiceImpostaServizio + '\'' +
                ", tipoBollettino='" + tipoBollettino + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", prezzo=" + prezzo +
                '}';
    }

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemDto item = (ItemDto) o;

        if (chiaveEnte != null ? !chiaveEnte.equals(item.getChiaveEnte()) : item.getChiaveEnte() != null) return false;
        if (codiceImpostaServizio != null ? !codiceImpostaServizio.equals(item.getCodiceImpostaServizio()) : item.getCodiceImpostaServizio() != null)
            return false;
        if (codiceTipologiaServizio != null ? !codiceTipologiaServizio.equals(item.getCodiceTipologiaServizio()) : item.getCodiceTipologiaServizio() != null)
            return false;
        if (codiceUtente != null ? !codiceUtente.equals(item.getCodiceUtente()) : item.getCodiceUtente() != null) return false;
        if (descrizione != null ? !descrizione.equals(item.getDescrizione()) : item.getDescrizione() != null) return false;
        if (prezzo != null ? !prezzo.equals(item.getPrezzo()) : item.getPrezzo() != null) return false;
        if (tipoBollettino != null ? !tipoBollettino.equals(item.getTipoBollettino()) : item.getTipoBollettino() != null)
            return false;
        if (attributeList.size() != item.getAttributeList().size()) {
        	return false;
        }
        for (ItemAttributeDto itemAttributeDto : attributeList) {
			if (!item.getAttributeList().contains(itemAttributeDto)) {
				return false;
			}
 		}

        return true;
    }

    @Override
    public int hashCode() {
        int result = codiceUtente != null ? codiceUtente.hashCode() : 0;
        result = 31 * result + (chiaveEnte != null ? chiaveEnte.hashCode() : 0);
        result = 31 * result + (codiceTipologiaServizio != null ? codiceTipologiaServizio.hashCode() : 0);
        result = 31 * result + (codiceImpostaServizio != null ? codiceImpostaServizio.hashCode() : 0);
        result = 31 * result + (tipoBollettino != null ? tipoBollettino.hashCode() : 0);
        result = 31 * result + (descrizione != null ? descrizione.hashCode() : 0);
        result = 31 * result + (prezzo != null ? prezzo.hashCode() : 0);
        return result;
    }


}
