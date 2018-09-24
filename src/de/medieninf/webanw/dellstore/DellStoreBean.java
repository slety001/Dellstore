/*
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 * converted for standalone usage from JBoss sample implementation
 */
package de.medieninf.webanw.dellstore;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.eclipse.persistence.exceptions.DatabaseException;


public class DellStoreBean implements DellStore {
	private final static String persistenceUnit = "dellstore";

    private EntityManagerFactory emf;
    private ThreadLocal<EntityManager> em;
    private ThreadLocal<EntityTransaction> tx;
    
	public DellStoreBean() {
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

    @Override
	@SuppressWarnings("unchecked")
	public List<Category> getCategories() {
		cEm();
		String queryString = "SELECT c FROM Category c";
		Query query = em.get().createQuery(queryString);
		List<Category> categories = query.getResultList();
		return categories;
	}
    
    @Override
    public Category getCategory(int categoryId) {
    	try {
    		cEm();
    		String queryString = "SELECT c FROM Category c WHERE c.categoryId = :categoryId";
    		Query query = em.get().createQuery(queryString);
    		query.setParameter("categoryId", categoryId);
    		Category category = (Category) query.getSingleResult();
    		return category;
    	} catch (NoResultException e) {
    		return null;
    	}
    }
   
    @Override
    public Category getCategory(String categoryName) {
    	try {
    		cEm();
    		String queryString = "SELECT c FROM Category c WHERE c.categoryName = :categoryName";
    		Query query = em.get().createQuery(queryString);
    		query.setParameter("categoryName", categoryName);
    		Category category = (Category) query.getSingleResult();
    		return category;
    	} catch (NoResultException e) {
    		return null;
    	}
    }
    
    @Override
    public Category update(Category category) {
    	cEm();
    	tx.get().begin();
    	category = em.get().merge(category);
    	tx.get().commit();
    	return category;
    }

    @Override 
    public void remove(Category category) {
		cEm();
		tx.get().begin();
		category = em.get().merge(category);
		em.get().remove(category);
		tx.get().commit();
    }
    
    @Override
    public Customer getCustomer(String userName) {
		cEm();
    	try {
    		String queryString = "SELECT c FROM Customer c WHERE c.userName = :userName";
    		Query query = em.get().createQuery(queryString);
    		query.setParameter("userName",userName);
    		Customer customer = (Customer) query.getSingleResult();
    		return customer;
    	} catch (NoResultException e) {
    		return null;
    	}
    }

    @Override
    public Customer getCustomer(long customerId) {
		cEm();
    	try {
    		String queryString = "SELECT c FROM Customer c WHERE c.customerId = :customerId";
    		Query query = em.get().createQuery(queryString);
    		query.setParameter("customerId", customerId);
    		Customer c = (Customer) query.getSingleResult();
    		return c;
    	} catch (NoResultException e) {
    		return null;
    	}
    }
    
	@Override
	public Customer update(Customer customer) {
		cEm();
		tx.get().begin();
		customer = em.get().merge(customer);
	    tx.get().commit();
	    return customer;
	}
    
	@Override
    public void remove(Customer customer) {
		cEm();
		tx.get().begin();
		customer = em.get().merge(customer);
		em.get().remove(customer);
		tx.get().commit();
	}

	@Override
    public Product getProduct(long productId) {
		cEm();
    	String queryString = "Select p FROM Product p WHERE p.productId = :productId";
		Query query = em.get().createQuery(queryString);
		query.setParameter("productId", productId);
		Product product = (Product) query.getSingleResult();
    	return product;    	
    }
    
    @SuppressWarnings("unchecked")
	public List<Product> getRecentHistory(Customer customer, int howmany) {
		cEm();
		String queryString = "SELECT i.product FROM Order o JOIN o.orderLines i WHERE o.customer = :customer";
		// String queryString = "SELECT i.product AS p FROM Order o JOIN o.orderLines i JOIN FETCH i.product.relatedProduct WHERE o.customer = :customer order by i.lineId DESC";
		Query query = em.get().createQuery(queryString);
		query.setParameter("customer", customer);
		query.setMaxResults(howmany);
		List<Product> products = query.getResultList();
        return products;
	}

    private String[] orderbyproductok_array = {
   		"productId", "title", "actor", "category", "price", "quantity", "sales"
    };
    private List<String> orderbyproductok_list = new ArrayList<String>(Arrays.asList(orderbyproductok_array));
    private Query searchProductQuery(String projection, // p for product
    		String title, String actor, Category category, 
    		String orderBy, boolean ascending) {
    	title = (title == null) ? "%" : "%" + title.toUpperCase() + "%";    		
    	actor = (actor == null) ? "%" : "%" + actor.toUpperCase() + "%";
    	StringBuffer queryString = new StringBuffer("SELECT " + projection + " FROM Product p ");
    	String joinString = " WHERE ";
    	if (title != null) {
    		queryString.append(joinString + "p.title LIKE :title ");
    		joinString = " AND ";
    	}
    	if (actor != null) {
    		queryString.append(joinString + "p.actor LIKE :actor ");
    		joinString = " AND ";
    	}
    	if (category != null) {
    		queryString.append(joinString + "p.category = :category ");
    		joinString = " AND ";
    	}
    	if (orderBy != null) {
    		if (!orderbyproductok_list.contains(orderBy)) {
    			StringBuilder msg  = new StringBuilder();
    			msg.append("searchProductQuery: orderBy=" + orderBy + " not allowed\n");
    			msg.append("permissible are: ");
    			for (String ok : orderbyproductok_list) {
    				msg.append(ok + " ");
    			}
    			msg.append("\n");
    			throw new RuntimeException(msg.toString());
    		}
    		queryString.append(" ORDER BY p." + orderBy);
    		queryString.append(ascending ? " ASC " : " DESC ");
    	}
    	Query query = em.get().createQuery(queryString.toString());
    	if (title != null) {
    		query.setParameter("title", "%" + title + "%");
    	}
    	if (actor != null) {
    		query.setParameter("actor", "%" + actor + "%");
    	}
        if (category != null) {
        	query.setParameter("category", category);
        }
        return query;
    }
    
	@SuppressWarnings("unchecked")
	public List<Product> searchProducts(String title, String actor, Category category, 
			String orderBy, boolean ascending, 
			int howmany, int start) {
		cEm();
		Query query = searchProductQuery("p", title, actor, category, orderBy, ascending);
		query.setMaxResults(howmany);
		query.setFirstResult(start);
        List<Product> products = query.getResultList(); 
        return products;
	}
	
	public long countSearchProducts(String title, String actor, Category category) {
		cEm();
		Query query = searchProductQuery("COUNT(p)", title, actor, category, null, true);
		return ((Long) query.getSingleResult()).longValue();
	}    	

	@Override
	public Product update(Product product) {
		cEm();
		tx.get().begin();
		product = em.get().merge(product);
	    tx.get().commit();
	    return product;
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
		    String gender
    		) {
    	StringBuffer queryString = new StringBuffer("SELECT " + projection + " FROM Customer c ");
    	String joinString = " WHERE ";
    	if (userName != null) {
        	queryString.append(joinString + "c.userName LIKE :userName ");
        	joinString = " AND ";
    	}
    	if (firstName != null) {
        	queryString.append(joinString + "c.firstName LIKE :firstName ");
        	joinString = " AND ";
    	}
    	if (lastName != null) {
        	queryString.append(joinString + "c.lastName LIKE :lastName ");
        	joinString = " AND ";    		
    	}
    	if (address1 != null){
        	queryString.append(joinString + "c.address1 LIKE :address1 ");
        	joinString = " AND ";
    	}
    	if (address2 != null){
        	queryString.append(joinString + "c.address2 LIKE :address2 ");
        	joinString = " AND ";
    	}
    	if (city != null){
        	queryString.append(joinString + "c.city LIKE :city ");
        	joinString = " AND ";
    	}
    	if (state != null){
        	queryString.append(joinString + "c.state LIKE :state ");
        	joinString = " AND ";
    	}
    	if (country != null){
        	queryString.append(joinString + "c.country LIKE :country ");
        	joinString = " AND ";
    	}
    	if (email != null){
        	queryString.append(joinString + "c.email LIKE :email ");
        	joinString = " AND ";
    	}
    	if (phone != null){
        	queryString.append(joinString + "c.phone LIKE :phone ");
        	joinString = " AND ";
    	}
    	if (creditCard != null){
        	queryString.append(joinString + "c.creditCard LIKE :creditCard ");
        	joinString = " AND ";
    	}
    	if (creditCardExpiration != null) {
        	queryString.append(joinString + "c.creditCardExpiration LIKE :creditCardExpiration ");
        	joinString = " AND ";
    	}
    	if (gender != null) {
        	queryString.append(joinString + "c.gender LIKE :gender ");
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
	@Override
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
		    int howmany) {
		cEm();
		Query query = searchCustomerQuery("c", userName, firstName,lastName,
				address1, address2, city, state, zip, country, region,
				email, phone, creditCardType, creditCard, creditCardExpiration,
				age, income, gender);
		query.setMaxResults(howmany);
        List<Customer> customers = query.getResultList(); 
        return customers;
	}
	
	@Override
	public long countSearchCustomers(
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
		    String gender) {
		cEm();
		Query query = searchCustomerQuery("COUNT(c)", userName, firstName,lastName,
				address1, address2, city, state, zip, country, region,
				email, phone, creditCardType, creditCard, creditCardExpiration,
				age, income, gender);
		return ((Long) query.getSingleResult()).longValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Order> getOrders(Product product, int howmany, int start) {
		cEm();
    	StringBuffer queryString = new StringBuffer("SELECT DISTINCT o FROM Order o JOIN o.orderLines i");
    	String joinString = " WHERE ";
    	if (product != null) {
        	queryString.append(joinString + "i.product = :product");
    	}
       	Query query = em.get().createQuery(queryString.toString());
       	if (product != null) {
           	query.setParameter("product", product);       		
       	}
		query.setMaxResults(howmany);
		query.setFirstResult(start);
        List<Order> orders = query.getResultList(); 
        return orders;
	}    
	
	@Override
	public Order purchase(Customer customer, List<OrderLine> lines) 
			throws InsufficientQuantityException {
		cEm();	
		
		// Check whether products appear twice and throw execption
		Set<Long> productIds = new HashSet<Long>();
		for (OrderLine line: lines) {
			Long productId = line.getProduct().getProductId();
			if (productIds.contains(productId)) {
				throw new RuntimeException("Product with id " + productId + " appears more than once");
			}
			productIds.add(productId);
		}
		// ok - every product appears at most once
		
		// First an empty order
		Order order = new Order();
		order.setCustomer(customer);
		order.setOrderDate(new Date());
		try {
			tx.get().begin();
			em.get().persist(order);
			tx.get().commit();
		}  catch (OptimisticLockException e) {
			em.get().close();
			return null;
		}		
		// order added to database, ready to be filled with order lines
		// System.out.println("OrderId of dummy " + order.getOrderId());
		// needs to be cleaned up on failure		
		List<Product> errorProducts = new ArrayList<Product>();
		BigDecimal total = BigDecimal.ZERO;
		int orderLineId = 1;
		for (OrderLine line: lines) {
			Product product = line.getProduct();
			if (!product.order(line.getQuantity())) {
				errorProducts.add(product);
				// note that quantity has not been subtracted on erroneous products
	        }
			// System.out.println("setting Pk " + pk);
		}
	    if (errorProducts.size() > 0) {
	    	for (OrderLine line : lines) {
	    		Product product = line.getProduct();
	    		if (!errorProducts.contains(product)) {
	    			product.cancelOrder(line.getQuantity());
	    		}
	    	}
	    	order.setOrderLines(new ArrayList<OrderLine>());
	    	// should be save to remove order now
	    	try {
				tx.get().begin();
				em.get().remove(order);
				tx.get().commit();
			} catch (Exception ee) {
				System.err.println("cannot remove newly created order after unsuccessfully merging it with orderlines, really bad");
				throw new RuntimeException(ee);
			}
	    	throw new InsufficientQuantityException(errorProducts);
	    }
	    for (OrderLine line : lines) { // now assume it works - otherwise Optimisticlook later
			BigDecimal quantity = BigDecimal.valueOf(line.getQuantity());
			total = total.add(line.getProduct().getPrice().multiply(quantity));
			OrderLinePk pk = new OrderLinePk();
			pk.setOrderLineId(orderLineId++);
			pk.setOrderId(order.getOrderId());
			line.setOrder(order);
			line.setOrderDate(order.getOrderDate());
			line.setPrimaryKey(pk);
	    }
	    order.setOrderLines(lines);
	    order.setNetAmount(total);
	    order.setTax((order.getNetAmount().multiply(new BigDecimal(".0825"))));
	    order.setTotalAmount(order.getNetAmount().add(order.getTax()));
	    customer.getOrders().add(order);
		try {
			tx.get().begin();
			customer = em.get().merge(customer); // here orderlines should be merged
			for (OrderLine line : lines) {
				em.get().merge(line.getProduct());
			}
			tx.get().commit();
			em.get().close();
			return order;
		} catch (OptimisticLockException e) {
			try {
				tx.get().begin();
				em.get().remove(order);
				tx.get().commit();
			} catch (Exception ee) {
				System.err.println("cannot remove newly created order after unsuccessfully merging it with orderlines, really bad");
				throw new RuntimeException(ee);
			}
			em.get().close();
			return null;
		} catch (DatabaseException e) {
			em.get().close();
			return null;
		}		
	}

	@Override
    public void remove(Order order) {
		cEm();
		tx.get().begin();
		EntityManager emt = em.get();
		order = emt.merge(order);
		Customer customer = order.getCustomer();
		customer.getOrders().remove(order);
		List<OrderLine> orderLines = new ArrayList<OrderLine>(order.getOrderLines());
		for (OrderLine orderLine : orderLines) {
			orderLine.getProduct().cancelOrder(orderLine.getQuantity());
			emt.remove(orderLine);
			order.getOrderLines().remove(orderLine);
		}
		emt.remove(order);
		tx.get().commit();    	
    }

	private String[] possibleStates = {
		"AK", "AL", "AR", "AZ", "CA", "CO", "CT", "DC", "DE", "FL", 
		"GA", "HI", "IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA", 
		"MD", "ME", "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE", 
		"NH", "NJ", "NM", "NV", "NY", "OH", "OK", "OR", "PA", "RI", 
		"SC", "SD", "TN", "TX", "UT", "VA", "VT", "WA", "WI", "WV", "WY"	
	};
	@Override
	public List<String> getPossibleStates() {
		return new ArrayList<String>(Arrays.asList(possibleStates));
	}

	@Override
	public void close() {
		if (emf.isOpen()) {
			emf.close();
		}
		emf = null;
		em = null;
		tx = null;
	}
	
    /**
     * Some simple tests
     * @param args not used
     */
	public static void main(String[] args) {	
		DellStoreBean dellstore = new DellStoreBean();
		Customer customer = dellstore.getCustomer(19887);
		System.out.println(customer.getFirstName() + " " + customer.getLastName() + " hat " + 
				customer.getOrders().size() + " Bestellungen");
	}

}
