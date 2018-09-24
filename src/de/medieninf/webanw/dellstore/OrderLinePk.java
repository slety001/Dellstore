package de.medieninf.webanw.dellstore;


import java.io.Serializable;

import javax.persistence.*;


@Embeddable
public class OrderLinePk implements Serializable {
	private static final long serialVersionUID = -733721951098296432L;

	protected long orderLineId;
	protected long orderId;

    public OrderLinePk() {
    }

    public long getOrderLineId() {
        return orderLineId;
    }
    public void setOrderLineId(long orderLineId) {
        this.orderLineId = orderLineId;
    }
    
    public long getOrderId() {
        return orderId;
    }
    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    @Override
    public int hashCode() {
        return (int) (orderId + orderLineId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) 
        	return false;
        if (!(obj instanceof OrderLinePk)) 
        	return false;
        OrderLinePk pk = (OrderLinePk) obj;
        return pk.orderId == orderId && pk.orderLineId == orderLineId;
    }
    
    @Override
    public String toString() {
    	return ""  + orderId + "-" + orderLineId;
    }
}
