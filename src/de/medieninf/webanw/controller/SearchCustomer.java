package de.medieninf.webanw.controller;

import de.medieninf.webanw.dellstore.Customer;

public class SearchCustomer {
	
	private String version;
	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zip;  
	private String country;
	private String region;
	private String email;
	private String phone;
	private String creditCardType;
	private String creditCard;
	private String creditCardExpiration;
	private String age;
	private String income;
	private String gender;

	public SearchCustomer(){
		userName = null;
        password = null;
        firstName = null;
        lastName = null;
        address1 = null;
        address2 = null;
        city = null;
        state = null;
        zip = null;
        country = null;
        region = null;
        email = null;
        phone = null;
        creditCardType = null;
        creditCard = null;
        creditCardExpiration = null;
        age = null;
        income = null;
        gender = null;
	}
	
    public String getUserName() {
    	return userName;
    }
    public void setUserName(String userName) {
    	this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
    	this.firstName=firstName;
    }
    
    public String getLastName() {
    	return lastName;
    }

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		if(address2==null){
			return address2;
		}
		if (address2.equals("")){
			return null;
		}
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		if(zip==null){
			return zip;
		}
		if (zip.equals("")){
			return null;
		}
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRegion() {
		if(region==null){
			return region;
		}
		if (region.equals("")){
			return null;
		}
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCreditCardType() {
		if(creditCardType==null){
			return creditCardType;
		}
		if (creditCardType.equals("")){
			return null;
		}
		return creditCardType;
	}

	public void setCreditCardType(String creditCardType) {
		this.creditCardType = creditCardType;
	}

	public String getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}

	public String getCreditCardExpiration() {
		return creditCardExpiration;
	}

	public void setCreditCardExpiration(String creditCardExpiration) {
		this.creditCardExpiration = creditCardExpiration;
	}

	public String getAge() {
		if(age==null){
			return age;
		}
		if (age.equals("")){
			return null;
		}
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getIncome() {
		if(income==null){
			return income;
		}
		if (income.equals("")){
			return null;
		}
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
    
}

