/*
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 * converted for standalone usage from JBoss sample implementation
 */
package de.medieninf.webanw.dellstore;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name="CATEGORIES")
public class Category implements Serializable {
	private static final long serialVersionUID = 6628295908058516805L;

	protected int           categoryId;   
	protected int           version;
	protected String        categoryName;
	protected List<Product> products;

    @Id
    @SequenceGenerator(name="CategoryIdGen", sequenceName="categories_category_seq",
            allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CategoryIdGen")
    @Column(name="CATEGORY")
    public int getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    
    @Version
    @Column(name="version")
    public int getVersion() {
    	return this.version;
    }
    public void setVersion(int version) {
    	this.version = version;
    }

    @Column(name="CATEGORYNAME", nullable=false, unique=true, length=50)
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    @OneToMany(mappedBy="category")
    public List<Product> getProducts() {
    	return this.products;
    }
    public void setProducts(List<Product> products) {
    	this.products = products;
    }
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + categoryId;
		result = prime * result
				+ ((categoryName == null) ? 0 : categoryName.hashCode());
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
		Category other = (Category) obj;
		if (categoryId != other.categoryId)
			return false;
		if (categoryName == null) {
			if (other.categoryName != null)
				return false;
		} else if (!categoryName.equals(other.categoryName))
			return false;
		return true;
	}
	
	@Override
    public String toString() {
    	return categoryName + "[" + categoryId + "]";
    }
}
