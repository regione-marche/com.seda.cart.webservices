/*
package com.seda.cart.webservices.config;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public enum PropKeys {
	DEFAULT_NODE,
	JNDI_CONTEXT,
	JNDI_PROVIDER
	;
	
    private static ResourceBundle rb;

    public String format( Object... args ) {
        synchronized(PropKeys.class) {
            if(rb==null)
            	rb = ResourceBundle.getBundle(PropKeys.class.getName());
            return MessageFormat.format(rb.getString(name()),args);
        }
    }
}
*/