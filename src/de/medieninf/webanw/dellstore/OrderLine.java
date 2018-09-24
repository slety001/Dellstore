/*
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 * converted for standalone usage from JBoss sample implementation
 */
package de.medieninf.webanw.dellstore;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;

@Entity
@Table(name="ORDERLINES")
public class OrderLine implements Serializable {
	private static final long serialVersionUID = 1319677748237036107L;

	protected OrderLinePk primaryKey; 
	protected int version;
	protected Order order;
	protected Product product;
	protected int quantity;
	protected Date orderDate;
     
    public OrderLine() {
        this.primaryKey = new OrderLinePk();
    }
	
    @EmbeddedId
    @AttributeOverrides({
    	@AttributeOverride(name="orderLineId", column = @Column(name = "orderlineid")),
    	@AttributeOverride(name="orderId", column = @Column(name = "orderid")),
    })
    public OrderLinePk getPrimaryKey() {
        return primaryKey;
    }
    public void setPrimaryKey(OrderLinePk pk) {
        primaryKey = pk;
    }
    
    @Version
    @Column(name="version")
    public int getVersion() {
    	return this.version;
    }
    public void setVersion(int version) {
    	this.version = version;
    }

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="orderid", updatable=false, insertable=false)
    public Order getOrder() {
    	return order;
    }
    public void setOrder(final Order order) {
    	this.order = order;
    	this.primaryKey.setOrderId(order.getOrderId());
    }
       
    @ManyToOne
    @JoinColumn(name="PROD_ID", unique=false, nullable=false)
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product=product;
    }

    @Column(name="QUANTITY", nullable=false)
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void addQuantity(int howmany) {
        quantity += howmany;
    }

    @Column(name="ORDERDATE", nullable=false)
    @Temporal(TemporalType.DATE)
    public Date getOrderDate() {
    	// I assume that that data is redundant, but we 
    	// manage it independantly anyway. Maybe there is 
    	// a reason why the order date of an orderline might
    	// be different from the order date of the order.
    	// let's assume so.
        return orderDate;
    }
    public void setOrderDate(Date date) {
    	// for info on that horrible code check comment in Order.setOrderDate
        this.orderDate = date;
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	int day = cal.get(Calendar.DAY_OF_MONTH);
    	int month = cal.get(Calendar.MONTH);
    	int year = cal.get(Calendar.YEAR);
    	cal.clear();
    	cal.set(year, month, day);
    	date.setTime(cal.getTimeInMillis());
        this.orderDate = date;
    }
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((orderDate == null) ? 0 : orderDate.hashCode());
		result = prime * result
				+ ((primaryKey == null) ? 0 : primaryKey.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + quantity;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderLine other = (OrderLine) obj;
		if (orderDate == null) {
			if (other.orderDate != null)
				return false;
		} else if (!orderDate.equals(other.orderDate))
			return false;
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		} else if (!primaryKey.equals(other.primaryKey))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (quantity != other.quantity)
			return false;
		return true;
	}
	
	@Override
    public String toString() {
    	String ret = getOrder().getOrderId() + "-" + getPrimaryKey().getOrderLineId();
    	ret += "-[";
    	ret += quantity + "*" + product.toString(); 
    	ret += "]";
    	return ret;
    }
    
}
