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
@Table(name="CUSTOMERS")
public class Customer implements Serializable {
	private static final long serialVersionUID = 4966886043973182887L;

	public static String[] cctypes = {"MasterCard", "Visa", "Discover", "Amex", "Dell Preferred"};

    protected long   customerId;
    protected int    version;
    protected String userName;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String address1;
    protected String address2;
    protected String city;
    protected String state;
    protected int    zip;  
    protected String country;
    protected int    region;
    protected String email;
    protected String phone;
    protected int    creditCardType;
    protected String creditCard;
    protected String creditCardExpiration;
    protected int    age;
    protected long   income;
    protected String gender;
    protected List<Order> orders;

    public Customer() {
    	password = "password"; // default in all instances
    }

    public Customer(Customer other) {
        customerId = other.customerId;
        userName = other.userName;
        password = other.password;
        firstName = other.firstName;
        lastName = other.lastName;
        address1 = other.address1;
        address2 = other.address2;
        city = other.city;
        state = other.state;
        zip = other.zip;
        country = other.country;
        region = other.region;
        email = other.email;
        phone = other.phone;
        creditCardType = other.creditCardType;
        creditCard = other.creditCard;
        creditCardExpiration = other.creditCardExpiration;
        age = other.age;
        income = other.income;
        gender = other.gender;
    }
    
    @Id
    @SequenceGenerator(name="CustomerIdGen", sequenceName="customers_customerid_seq",
            allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CustomerIdGen")
    @Column(name="CUSTOMERID")
    public long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(long id) {
        this.customerId = id;
    }
    
    @Version
    @Column(name="version")
    public int getVersion() {
    	return this.version;
    }
    public void setVersion(int version) {
    	this.version = version;
    }

    @Column(name="USERNAME", unique=true, nullable=false, length=50)
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name="PASSWORD", nullable=false, length=50)
    public String getHashedPassword() {
        return password;
    }
    public void setHashedPassword(String password) {
        this.password = password;
    }

    @Column(name="FIRSTNAME", nullable=false, length=50)
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    @Column(name="LASTNAME", nullable=false, length=50)
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name="ADDRESS1", nullable=false, length=50)
    public String getAddress1() {
        return address1;
    }
    public void setAddress1(String address1) {
        this.address1 = address1;
    }
    
    @Column(name="ADDRESS2", length=50)
    public String getAddress2() {
        return address2;
    }
    public void setAddress2(String address2) {
        this.address2 = address2;
    }
    
    @Column(name="CITY", nullable=false, length=50)
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    
    @Column(name="STATE", length=50)
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    @Column(name="ZIP")
    public int getZip() {
        return zip;
    }
    public void setZip(int zip) {
        this.zip = zip;
    }

    @Column(name="COUNTRY", nullable=false, length=50)
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    @Column(name="REGION", nullable=false)
    public int getRegion() {
        return region;
    }
    public void setRegion(int region) {
        this.region = region;
    }

    @Column(name="EMAIL", length=50)
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name="PHONE", length=50)
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name="AGE")
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    @Column(name="INCOME")
    public long getIncome() {
        return income;
    }
    public void setIncome(long income) {
        this.income = income;
    }

    @Column(name="GENDER", length=1)
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    @Column(name="CREDITCARDTYPE")
    public int getCreditCardType() {
        return creditCardType;
    }
    public void setCreditCardType(int type) {
        this.creditCardType = type;
    }

    @Transient public String getCreditCardTypeString() {
        if (creditCardType<1 || creditCardType>cctypes.length) {
            return "";
        }
        return cctypes[creditCardType-1];
    }

    @Column(name="CREDITCARD", nullable=false, length=50)
    public String getCreditCard() {
        return creditCard;
    }
    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    @Column(name="CREDITCARDEXPIRATION", nullable=false, length=50)
    public String getCreditCardExpiration() {
        return creditCardExpiration;
    }
    public void setCreditCardExpiration(String creditCardExpiration) {
        this.creditCardExpiration = creditCardExpiration;
    }
    
    @OneToMany(mappedBy="customer")
    public List<Order> getOrders() {
    	return this.orders;
    }
    public void setOrders(List<Order> orders) {
    	this.orders = orders;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((address1 == null) ? 0 : address1.hashCode());
		result = prime * result
				+ ((address2 == null) ? 0 : address2.hashCode());
		result = prime * result + age;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result
				+ ((creditCard == null) ? 0 : creditCard.hashCode());
		result = prime
				* result
				+ ((creditCardExpiration == null) ? 0 : creditCardExpiration
						.hashCode());
		result = prime * result + creditCardType;
		result = prime * result + (int) (customerId ^ (customerId >>> 32));
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + (int) (income ^ (income >>> 32));
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + region;
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + zip;
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
		Customer other = (Customer) obj;
		if (address1 == null) {
			if (other.address1 != null)
				return false;
		} else if (!address1.equals(other.address1))
			return false;
		if (address2 == null) {
			if (other.address2 != null)
				return false;
		} else if (!address2.equals(other.address2))
			return false;
		if (age != other.age)
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (creditCard == null) {
			if (other.creditCard != null)
				return false;
		} else if (!creditCard.equals(other.creditCard))
			return false;
		if (creditCardExpiration == null) {
			if (other.creditCardExpiration != null)
				return false;
		} else if (!creditCardExpiration.equals(other.creditCardExpiration))
			return false;
		if (creditCardType != other.creditCardType)
			return false;
		if (customerId != other.customerId)
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (income != other.income)
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (region != other.region)
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		if (zip != other.zip)
			return false;
		return true;
	}

	@Override
    public String toString() {
    	return userName + "[" + firstName + " " + lastName + "]"; 
    }
    
}
