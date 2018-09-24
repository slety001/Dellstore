package de.medieninf.webanw.persist;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import de.medieninf.webanw.dellstore.Customer;
import de.medieninf.webanw.dellstore.DellStoreBean;
import de.medieninf.webanw.dellstore.Order;
import de.medieninf.webanw.dellstore.Product;

public class PersistenzDieZweite {

	private final static String persistenceUnit = "dellstore";

    private EntityManagerFactory emf;
    private ThreadLocal<EntityManager> em;
    private ThreadLocal<EntityTransaction> tx;
	
	private Customer customer;
	private DellStoreBean dellstore;
	private List<Customer> customers;
	private int howmanyCustomer = -1;

	public PersistenzDieZweite() {
		dellstore = new DellStoreBean();
		emf = Persistence.createEntityManagerFactory(persistenceUnit);
        em = new ThreadLocal<EntityManager>();
        tx = new ThreadLocal<EntityTransaction>();
	}
	
	private void cEm() {
		EntityManager lem = em.get();
		if (lem != null && lem.isOpen()) 
			lem.close();
		lem = emf.createEntityManager();
		em.set(lem);
		tx.set(lem.getTransaction());
    }
    
	@SuppressWarnings("unchecked")
	public List<Customer> getCustomers(
			int limit,
		    int offset,
		    String orderBy,
		    boolean ascending) {
		cEm();
		StringBuffer queryString = new StringBuffer("SELECT c FROM Customer c ");
		queryString.append("ORDER BY c." + orderBy);
		queryString.append(ascending ? " ASC " : " DESC ");
    	Query query = em.get().createQuery(queryString.toString());
		query.setMaxResults(limit);
		query.setFirstResult(offset);
        List<Customer> customers = query.getResultList(); 
        return customers;
	}
	
	private Query searchCustomerQuery(
    		String projection, // Customer is just c
		    String userName,
		    String firstName,
		    String lastName,
		    String address1,
		    String address2,
		    String city,
		    String state,
		    Integer zip,
		    String country,
		    Integer region,
		    String email,
		    String phone,
		    Integer creditCardType,
		    String creditCard,
		    String creditCardExpiration,
		    Integer age,
		    Integer income,
		    String gender,
		    String orderBy,
		    boolean ascending
    		) {
    	StringBuffer queryString = new StringBuffer("SELECT " + projection + " FROM Customer c ");
    	String joinString = " WHERE ";
    	if (userName != null) {
        	queryString.append(joinString + "UPPER(c.userName) LIKE :userName ");
        	joinString = " AND ";
    	}
    	if (firstName != null) {
        	queryString.append(joinString + "UPPER(c.firstName) LIKE :firstName ");
        	joinString = " AND ";
    	}
    	if (lastName != null) {
        	queryString.append(joinString + "UPPER(c.lastName) LIKE :lastName ");
        	joinString = " AND ";    		
    	}
    	if (address1 != null){
        	queryString.append(joinString + "UPPER(c.address1) LIKE :address1 ");
        	joinString = " AND ";
    	}
    	if (address2 != null){
        	queryString.append(joinString + "UPPER(c.address2) LIKE :address2 ");
        	joinString = " AND ";
    	}
    	if (city != null){
        	queryString.append(joinString + "UPPER(c.city) LIKE :city ");
        	joinString = " AND ";
    	}
    	if (state != null){
        	queryString.append(joinString + "UPPER(c.state) LIKE :state ");
        	joinString = " AND ";
    	}
    	if (country != null){
        	queryString.append(joinString + "UPPER(c.country) LIKE :country ");
        	joinString = " AND ";
    	}
    	if (email != null){
        	queryString.append(joinString + "UPPER(c.email) LIKE :email ");
        	joinString = " AND ";
    	}
    	if (phone != null){
        	queryString.append(joinString + "UPPER(c.phone) LIKE :phone ");
        	joinString = " AND ";
    	}
    	if (creditCard != null){
        	queryString.append(joinString + "UPPER(c.creditCard) LIKE :creditCard ");
        	joinString = " AND ";
    	}
    	if (creditCardExpiration != null) {
        	queryString.append(joinString + "UPPER(c.creditCardExpiration) LIKE :creditCardExpiration ");
        	joinString = " AND ";
    	}
    	if (gender != null) {
        	queryString.append(joinString + "UPPER(c.gender) LIKE :gender ");
        	joinString = " AND ";
    	}
    	if (zip != null) {
    		queryString.append(joinString + "c.zip = :zip");
        	joinString = " AND ";
    	}
    	if (region != null) {
    		queryString.append(joinString + "c.region = :region");
        	joinString = " AND ";
    	}
    	if (creditCardType != null) {
    		queryString.append(joinString + "c.creditCardType = :creditCardType");
        	joinString = " AND ";
    	}
    	if (age != null) {
    		queryString.append(joinString + "c.age = :age");
        	joinString = " AND ";
    	}
    	if (income != null) {
    		queryString.append(joinString + "c.income = :income");
        	joinString = " AND ";
    	}    	
		queryString.append(" ORDER BY c." + orderBy);
		queryString.append(ascending ? " ASC " : " DESC ");
		System.out.println("queryString: "+queryString.toString());
       	Query query = em.get().createQuery(queryString.toString());
    	if (userName != null) {
           	query.setParameter("userName", "%" + userName + "%");
    	}
    	if (firstName != null) {
           	query.setParameter("firstName", "%" + firstName + "%");
    	}
    	if (lastName != null) {
           	query.setParameter("lastName", "%" + lastName + "%");
    	}
    	if (address1 != null){
           	query.setParameter("address1", "%" + address1 + "%");
    	}
    	if (address2 != null){
           	query.setParameter("address2", "%" + address2 + "%");
    	}
    	if (city != null){
          	query.setParameter("city", "%" + city + "%");
    	}
    	if (state != null){
          	query.setParameter("state", "%" + state + "%");    	
    	}
    	if (country != null){
          	query.setParameter("country", "%" + country + "%");
    	}
    	if (email != null){
          	query.setParameter("email", "%" + email + "%");
    	}
    	if (phone != null){
          	query.setParameter("phone", "%" + phone + "%");
    	}
    	if (creditCard != null){
           	query.setParameter("creditCard", "%" + creditCard + "%");
    	}
    	if (creditCardExpiration != null) {
           	query.setParameter("creditCardExpiration", "%" + creditCardExpiration + "%");
    	}
    	if (gender != null) {
           	query.setParameter("gender", "%" + gender + "%");
    	}
    	if (zip != null)
    		query.setParameter("zip", zip);
    	if (region != null)
           	query.setParameter("region", region);
    	if (creditCardType != null)
           	query.setParameter("creditCardType", creditCardType);
    	if (age != null)
           	query.setParameter("age", age);
    	if (income != null) 
    		query.setParameter("income", income);
       	return query;
    }
    
	@SuppressWarnings("unchecked")
	public List<Customer> searchCustomers(
		    String userName,
		    String firstName,
		    String lastName,
		    String address1,
		    String address2,
		    String city,
		    String state,
		    Integer zip,
		    String country,
		    Integer region,
		    String email,
		    String phone,
		    Integer creditCardType,
		    String creditCard,
		    String creditCardExpiration,
		    Integer age,
		    Integer income,
		    String gender,
		    int howmany,
		    int start,
		    String orderBy,
		    boolean ascending) {
		cEm();
		Query query = searchCustomerQuery("c", userName, firstName,lastName,
				address1, address2, city, state, zip, country, region,
				email, phone, creditCardType, creditCard, creditCardExpiration,
				age, income, gender, orderBy, ascending);
		query.setMaxResults(howmany);
		query.setFirstResult(start);
		System.out.println("query: "+query.toString());
        List<Customer> customers = query.getResultList(); 
        //System.out.println("query: "+userName);
        return customers;
	}
	
	public int getCount(String tablename) {
		cEm();
		StringBuffer queryString = new StringBuffer("SELECT COUNT(c) FROM " + tablename + " c ");
    	Query query = em.get().createQuery(queryString.toString());
		return ((Long) query.getSingleResult()).intValue();
	}
	
	@SuppressWarnings("unchecked")
	public List<Order> getOrders(
			int limit,
		    int offset,
		    String orderBy,
		    boolean ascending) {
		cEm();
		StringBuffer queryString = new StringBuffer("SELECT o FROM Order o ");
		queryString.append("ORDER BY o." + orderBy);
		queryString.append(ascending ? " ASC " : " DESC ");
    	Query query = em.get().createQuery(queryString.toString());
		query.setMaxResults(limit);
		query.setFirstResult(offset);
        List<Order> orders = query.getResultList(); 
        return orders;
	}
	
	@SuppressWarnings("unchecked")
	public List<Product> getProducts(
			int limit,
		    int offset,
		    String orderBy,
		    boolean ascending) {
		cEm();
		StringBuffer queryString = new StringBuffer("SELECT p FROM Product p ");
		queryString.append("ORDER BY p." + orderBy);
		queryString.append(ascending ? " ASC " : " DESC ");
    	Query query = em.get().createQuery(queryString.toString());
		query.setMaxResults(limit);
		query.setFirstResult(offset);
        List<Product> products = query.getResultList(); 
        return products;
	}
	
//	public void forUpdate(long prod_id){
//		cEm();
//		StringBuffer queryString = new StringBuffer("SELECT quan_in_stock FROM inventory WHERE"+ prod_id +"FOR UPDATE;");
//		Query query = em.get().createQuery(queryString.toString());
//	}
	
	public void close() {
		if (emf.isOpen()) {
			emf.close();
		}
		emf = null;
		em = null;
		tx = null;
	}
}
