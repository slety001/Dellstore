/*
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 * converted for standalone usage from JBoss sample implementation
 * Merged inventory information (attributes quantity and sales) 
 */

package de.medieninf.webanw.dellstore;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;


@Entity
@Table(name="PRODUCTS")
@SecondaryTable(name="INVENTORY")
public class Product implements Serializable {
	private static final long serialVersionUID = 1268683790692531949L;

	protected long productId;
	protected int version;
	protected String title;
	protected String actor;
	protected BigDecimal price;
	protected int quantity; // from inventory
	protected int sales; // from inventory

	protected Category  category;
	protected Product   relatedProduct;

	@Id
    @SequenceGenerator(name="ProductIdGen", sequenceName="products_prod_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ProductIdGen")
	@Column(name="PROD_ID")
	public long getProductId() {
		return productId;
	}
	public void setProductId(long id) {
		this.productId = id;
	}
	
	@Version
	@Column(name="version")
	public int getVersion() {
		return this.version;
	}
	public void setVersion(int version) {
		this.version = version;
	}

	/*
	@OneToOne(fetch=FetchType.LAZY,mappedBy="product",
			  cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	public Inventory getInventory() {
		return inventory;
	}
	public void setInventory(Inventory inventory) {
	    this.inventory = inventory;
	}
	*/

	@ManyToOne
	@JoinColumn(name="CATEGORY",nullable=false)
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}

	@Column(name="TITLE",nullable=false,length=50)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name="ACTOR",nullable=false,length=50)
	public String getActor() {
		return actor;
	}
	public void setActor(String actor) {
	    this.actor = actor;
	}

	@Column(name="PRICE",nullable=false,precision=12,scale=2)
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price=price;
	}

	@Column(name="quan_in_stock", table="INVENTORY")
	public int getQuantity() { 
		return quantity; 
	}
	public void setQuantity(int quantity) { 
		this.quantity = quantity; 
	}

	@Column(name="sales", table="INVENTORY")
	public int getSales() { 
		return sales; 
	}
	public void setSales(int sales) { 
		this.sales = sales; 
	}
	
    public boolean order(int howmany) {
        if (howmany > quantity) {
            return false;
        }
        quantity -= howmany;
        sales += howmany;
        return true;
    }
    
    public void cancelOrder(int howmany) {
    	quantity += howmany;
    	sales -= howmany;
    }
	
	@ManyToOne
	@JoinColumn(name="COMMON_PROD_ID")
	public Product getRelatedProduct() {
		return relatedProduct;
	}
	public void setRelatedProduct(Product related) {
		this.relatedProduct=related;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actor == null) ? 0 : actor.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + (int) (productId ^ (productId >>> 32));
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Product other = (Product) obj;
		if (actor == null) {
			if (other.actor != null)
				return false;
		} else if (!actor.equals(other.actor))
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (productId != other.productId)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return productId + ":" + title + "," + actor;
	}
}
