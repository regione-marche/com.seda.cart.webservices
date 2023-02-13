package com.seda.cart.facade.model;

public class ItemSessionKey {
    private String codiceSocieta;
    private String codiceUtente;

    public ItemSessionKey(String codiceSocieta, String codiceUtente) {
        this.codiceSocieta = codiceSocieta;
        this.codiceUtente = codiceUtente;
    }

    public String getCodiceSocieta() {
        return codiceSocieta;
    }

    public void setCodiceSocieta(String codiceSocieta) {
        this.codiceSocieta = codiceSocieta;
    }

    public String getCodiceUtente() {
        return codiceUtente;
    }

    public void setCodiceUtente(String codiceUtente) {
        this.codiceUtente = codiceUtente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemSessionKey that = (ItemSessionKey) o;

        if (codiceSocieta != null ? !codiceSocieta.equals(that.codiceSocieta) : that.codiceSocieta != null) return false;
        if (codiceUtente != null ? !codiceUtente.equals(that.codiceUtente) : that.codiceUtente != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = codiceSocieta != null ? codiceSocieta.hashCode() : 0;
        result = 31 * result + (codiceUtente != null ? codiceUtente.hashCode() : 0);
        return result;
    }

}
