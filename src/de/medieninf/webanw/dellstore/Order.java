/*
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 * converted for standalone usage from JBoss sample implementation
 */

package de.medieninf.webanw.dellstore;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import javax.persistence.*;


@Entity
@Table(name="ORDERS")
public class Order implements Serializable {
	private static final long serialVersionUID = -2210176773418701660L;
	
	protected long orderId;
	protected int version;
	protected Date orderDate;
	protected Customer customer;
	protected BigDecimal netAmount;
	protected BigDecimal tax;
	protected BigDecimal totalAmount;
	protected List<OrderLine> orderLines;

	public Order() {
		orderDate = new Date();
		netAmount = BigDecimal.ZERO;
		tax = BigDecimal.ZERO;
		totalAmount = BigDecimal.ZERO;
		orderLines = new ArrayList<OrderLine>();
	}
	
    @Id
    @SequenceGenerator(name="OrderIdGen", sequenceName="orders_orderid_seq",
            allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OrderIdGen")
    @Column(name="ORDERID")
    public long getOrderId() {
        return orderId;
    }
    public void setOrderId(long id) {
        this.orderId = id;
    }
    
    @Version
    @Column(name="version")
    public int getVersion() {
    	return this.version;
    }
    public void setVersion(int version) {
    	this.version = version;
    }

    @Column(name="ORDERDATE", nullable=false)
    @Temporal(TemporalType.DATE)
    public Date getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(Date date) {
    	// What a complete horrible mess java.util.Date and related is. 
    	// Goal is to get rid of the time in the date, as this
    	// is not part of the database representation. I see
    	// not other way as to make a calendar instance
    	Calendar cal = Calendar.getInstance();
    	// of the date to be set
    	cal.setTime(date);
    	// in order to retrieve day, month, year
    	int day = cal.get(Calendar.DAY_OF_MONTH);
    	int month = cal.get(Calendar.MONTH);
    	int year = cal.get(Calendar.YEAR);
    	// then we need to alter everything such that 
    	// the finer details regarding the time vanish
    	cal.clear();
    	cal.set(year, month, day);
    	// the agony of using date has not yet ended, as there
    	// must be a way to alter date to a corresponding date object
    	date.setTime(cal.getTimeInMillis());
    	// please note, that we do change the value that has been
    	// given to us in order to store something, which is 
    	// really is a most horrible way to deal with data entrusted
    	// to us. Only the sheer obscenity and akwardness of the
    	// java.util.Date class is if no excuse then at least a reason
    	// to do so.
        this.orderDate = date;
    }

    @OneToMany(mappedBy="order", cascade=CascadeType.ALL)
    public List<OrderLine> getOrderLines() {
        return orderLines;
    }
    public void setOrderLines(List<OrderLine> lines) {
        this.orderLines = lines;
    }

    @ManyToOne
    @JoinColumn(name="CUSTOMERID")
    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    @Column(name="NETAMOUNT", nullable=false, precision=12, scale=2)
    public BigDecimal getNetAmount() {
        return netAmount;
    }
    public void setNetAmount(BigDecimal amount) {
        this.netAmount = amount;
    }

    @Column(name="TAX", nullable=false, precision=12, scale=2)
    public BigDecimal getTax() {
        return tax;
    }
    public void setTax(BigDecimal amount) {
        this.tax = amount;
    }

    @Column(name="TOTALAMOUNT", nullable=false, precision=12, scale=2)
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(BigDecimal amount) {
        this.totalAmount = amount;
    }
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((customer == null) ? 0 : customer.hashCode());
		result = prime * result
				+ ((netAmount == null) ? 0 : netAmount.hashCode());
		result = prime * result
				+ ((orderDate == null) ? 0 : orderDate.hashCode());
		result = prime * result + (int) (orderId ^ (orderId >>> 32));
		result = prime * result + ((tax == null) ? 0 : tax.hashCode());
		result = prime * result
				+ ((totalAmount == null) ? 0 : totalAmount.hashCode());
		result = prime * result + version;
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
		Order other = (Order) obj;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (netAmount == null) {
			if (other.netAmount != null)
				return false;
		} else if (!netAmount.equals(other.netAmount))
			return false;
		if (orderDate == null) {
			if (other.orderDate != null)
				return false;
		} else if (!orderDate.equals(other.orderDate))
			return false;
		if (orderId != other.orderId)
			return false;
		if (tax == null) {
			if (other.tax != null)
				return false;
		} else if (!tax.equals(other.tax))
			return false;
		if (totalAmount == null) {
			if (other.totalAmount != null)
				return false;
		} else if (!totalAmount.equals(other.totalAmount))
			return false;
		return true;
	}
	@Override
    public String toString() {
    	return ""+orderId+"[" + orderDate +", " + orderLines.size() + " items" + "]";
    }

}
