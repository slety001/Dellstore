/*
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 * converted and extended for standalone usage from JBoss sample implementation
 */

package de.medieninf.webanw.dellstore;

import java.util.*;


public interface DellStore {
	/**
	 * Retrieve all available categories.
	 * @return List of categories
	 */
	public List<Category> getCategories();
	
	/**
	 * Retrieve a category by its id.
	 * @param id
	 * @return category if available or null 
	 */
	public Category getCategory(int categoryId);

	/**
	 * Retrieve a category by its name.
	 * @param categoryName
	 * @return category if available or null 
	 */
	public Category getCategory(String categoryName);

	/**
	 * Create new or update category.
	 * Apply changes of a customer or creates new one.
	 * @param category to be created or updated
	 * @return updated or newly created category
	 */
	public Category update(Category category);

	/**
	 * Removes a category.
	 * @param category to be removed
	 */
	public void remove(Category category);
	
	/**
	 * Retrieve a customer by its username.
	 * @param userName Username
	 * @return Customer if available or null if there is no customer with name user
	 */
	public Customer getCustomer(String userName);
	
	/**
	 * Retrieve a customer by its customerId.
	 * @param customerId 
	 * @return Customer if available or null if there is no customer with respective id
	 */
	public Customer getCustomer(long customerId);	
	
	/**
	 * Searches customers according to properties of the customer. If a property
	 * is null then it is ignored while filtering the search results.
	 * For all String types a substring is searched (LIKE), for all integer types
	 * the match must fit exactly.
	 * @param userName   String
	 * @param firstName  String
	 * @param lastName   String
	 * @param address1   String
	 * @param address2   String
	 * @param city       String
	 * @param state      String
	 * @param zip        Integer
	 * @param country    String
	 * @param region     Integer
	 * @param email      String
	 * @param phone      String
	 * @param creditCardType       Integer
	 * @param creditCard     	   String
	 * @param creditCardExpiration String
	 * @param age       Integer
	 * @param income    Integer
	 * @param gender
	 * @param howmany Maximum number of customer to be returned
	 * @return List of customers matching the search criteria
	 */
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
		    int howmany);

	/**
	 * Counts the search result of customers according to properties as in searchCustomers
	 */
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
		    String gender);
	
	/**
	 * Apply changes to a customer or create new one.
	 * @param customer to be created or updated
	 * @return updated or newly created customer
	 */
	public Customer update(Customer customer);
	
	/**
	 * Deletes a customer. Must not have any orders associated.
	 * @param customer to be deleted
	 */
	public void remove(Customer customer);
		
	/**
	 * Retrieve a list of products a customer has recently ordered,
	 * not the related products as in the original example.
	 * @param customer The customer who has ordered the products
	 * @param howmany max. number of returned Products
	 * @return List of products the customer has ordered
	 */
	public List<Product> getRecentHistory(Customer customer, int howmany);

	/**
	 * Searches products according to properties of the product. If a property
	 * is null then its property is ignored while filtering the search results.
	 * For title and actor a substring is searched (LIKE).
	 * @param title Title of the film
	 * @param actor Actor in the film
	 * @param category Only products in that specific category
	 * @param orderBy order by either productId, title, actor, category, price, quantity, sales or not sort with null
	 * @param ascending order ascending if true, descending otherwise
	 * @param howmany maximal number of returned products
	 * @param start the first to display (beginning is zero) 
	 * @return List of products matching the search criteria
	 */
	public List<Product> searchProducts(String title, String actor, Category category, 
			String orderBy, boolean ascending, int howmany, int start);

	/**
	 * Counts the search result of products according to properties as in searchProducts
	 */
	public long countSearchProducts(String title, String actor, Category category);

	
	/**
	 * Retrieve a product by its id.
	 * @param id
	 * @return product if available or null 
	 */
	public Product getProduct(long productId);
	
	/**
	 * Apply changes to a product or create new one.
	 * @param product to be created or updated
	 * @return updated or newly created product
	 */
	public Product update(Product product);
	
	/**
	 * Gets orders of a specific product.
	 * @param Product product
	 * @param howmany maximal number of returned products
	 * @param start the first to display (beginning is zero) 
	 * @return List of orders of that product
	 */
	public List<Order> getOrders(Product product, int howmany, int start);
	
	/**
	 * Creates and order and updates the respective inventory information
	 * meaning decreasing the inventory (stock) and increasing sales.
	 * @param customer Customer that will take the order
	 * @param lines OrderLines for that order
	 * @return Order that has been created 
	 * @throws InsufficientQuantityException iff not enough inventory is available 
	 *         RuntimeException if one product appears in different order lines 
	 */
	public Order purchase(Customer customer, List<OrderLine> lines) 
		throws InsufficientQuantityException;

	/**
	 * Deletes an order and updates the respective inventory information,
	 * meaning increasing the inventory (stock) and decreasing the sales.
	 * The sequence to generate the order ids is not (and cannot be) affected 
	 * and stays increased even on successive purchase and deletes.
	 * @param order to be deleted
	 */
    public void remove(Order order);
 
    /**
     * Retrieves possible states for the country 'US'
     * @return
     */
    public List<String> getPossibleStates(); 
    
    /**
     * Closes the underlying connections and prepares for shutdown.
     * No further action allowed.
     */
    public void close();
    
}
