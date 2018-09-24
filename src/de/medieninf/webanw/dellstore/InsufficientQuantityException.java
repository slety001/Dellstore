/*
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 * converted for standalone usage from JBoss sample implementation
 */

package de.medieninf.webanw.dellstore;

import java.util.List;


public class InsufficientQuantityException extends Exception {
	private static final long serialVersionUID = -2253514731305758476L;
	
	protected List<Product> products = null;

    public InsufficientQuantityException(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }
}
