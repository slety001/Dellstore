/*
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package de.medieninf.webanw.dellstore;

import java.io.Serializable;
import java.util.*;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Simple backing bean to test JPA/Facelets integration
 */
public class TestBackingBean implements Serializable {
	private static final long serialVersionUID = 6135468435682892590L;

	private long customerId;
	private Customer customer;
	
	/**
	 * customerid is a property to select customers
	 */
	public long getCustomerId() { return customerId; }
	public void setCustomerId(long customerId) { 
		this.customerId = customerId;
		FacesContext fc = FacesContext.getCurrentInstance();
		DellStore dellstore = (DellStoreBean) fc.getApplication().evaluateExpressionGet(fc, "#{dellstore}", DellStore.class);
		if (dellstore == null) {
			fc.addMessage(null, new FacesMessage("cannot get dellstore"));
			return;
		}
		customer = dellstore.getCustomer(customerId);
	}

	/**
	 * pure output property to retrieve selected customer
	 */
	public Customer getCustomer() { 
		return customer;
	}	
	public List<Order> getOrdersFromCustomer() {
		if (customer == null)
			return null;
		List<Order> ret = customer.getOrders();
		return ret;
	}

	/**
	 * Convenience method to retrieve the number of order from the currently selected customer
	 */
	public int getNumberOfOrdersFromCustomer() {
		if (customer == null)
			return 0;
		return getOrdersFromCustomer().size();
	}
	
}
