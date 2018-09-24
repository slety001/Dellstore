/*
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package de.medieninf.webanw.dellstore;

import java.util.*;
import static org.junit.Assert.*;
import org.junit.*;

public class TestDellStoreBean {
	final static public boolean MANY_TESTS=true; // if true then slower tests as well
	
	private DellStoreBean dellstore;
	
	@Before
	public void setUp() throws Exception {
		dellstore = new DellStoreBean();
	}

	private boolean containsCategoryName(Category category, String[] categories) {
		for (String categoryName: categories) {
			if (categoryName.equals(category.getCategoryName())) {
				return true;
			}
		}
		return false;
	}
	
	@Test
	public void testNothing() {
		// in order to see initial setup timing
	}
	
	@Test
	public void testInitJPA() {
		dellstore.getCustomer(1);  // minimal effort; just setup
	}
	
	@Test
	public void testGetCategories() {
		String[] categoryNames = { "Action", "Animation", "Children", "Classics",
				"Comedy", "Documentary", "Drama", "Family", "Foreign", "Games", 
				"Horror", "Music", "New", "Sci-Fi", "Sports", "Travel",};
		List<Category> lcategories = dellstore.getCategories();
		int noCategories = lcategories.size();
		assertEquals(categoryNames.length, lcategories.size());
		for (Category category: lcategories) {
			assertTrue(containsCategoryName(category, categoryNames));
		}
		Category category = new Category();
		String categoryName = "Telekolleg/Lehrfilm";
		category.setCategoryName(categoryName);
		category = dellstore.update(category);
		int categoryId = category.getCategoryId();
		Category retrievedCategory = dellstore.getCategory(categoryId);
		assertEquals(category, retrievedCategory);
		retrievedCategory = dellstore.getCategory(categoryName);
		assertEquals(category, retrievedCategory);
		lcategories = dellstore.getCategories();
		assertEquals(noCategories+1, lcategories.size());
		category.setCategoryName("new Name");
		category = dellstore.update(category);
		assertEquals(2, category.getVersion());
		dellstore.remove(category);
		lcategories = dellstore.getCategories();
		assertEquals(noCategories, lcategories.size());
	}

	@Test
	public void testGetCustomer() {
		String cname = "user1";
		Customer customer = dellstore.getCustomer(cname);
		assertEquals(cname, customer.getUserName());
		assertEquals(1l, customer.getCustomerId());
		assertEquals("VKUUXF", customer.getFirstName());
		assertEquals("ITHOMQJNYX", customer.getLastName());
		long customerids[] = {2,333, 2007, 9999, 10001, 19999, 20000 };
		for (long customerid : customerids) {
			customer = dellstore.getCustomer("user"+customerid);
			assertEquals(customerid, customer.getCustomerId());
		}
		assertNull(dellstore.getCustomer("user20001"));
	}

	@Test
	public void testGetCustomerById() {
		long cid = 1;
		Customer customer = dellstore.getCustomer(cid);
		assertEquals(cid, customer.getCustomerId());
		assertEquals("user1", customer.getUserName());
		assertEquals("VKUUXF", customer.getFirstName());
		assertEquals("ITHOMQJNYX", customer.getLastName());
		long customerids[] = {2,333, 2007, 9999, 10001, 19999, 20000 };
		for (long customerid : customerids) {
			customer = dellstore.getCustomer(customerid);
			assertEquals("user" + customerid, customer.getUserName());
		}
		assertNull(dellstore.getCustomer(20001));
	}

	public Customer newCustomer() {
		Customer customer = new Customer();
		String username = "Susi0815";
		customer.setUserName(username);
		customer.setFirstName("Susi");
		customer.setLastName("Sinnlos");
		customer.setAddress1("NowhereRoad 1");
		customer.setAddress2("1st floor (not ground floor)");
		customer.setAge(18);
		customer.setCity("Smalltown");
		customer.setCountry("Neverland");
		customer.setCreditCard(Customer.cctypes[0]);
		customer.setCreditCardExpiration("08/09");
		customer.setEmail("susi.sinnlos@example.org");
		customer.setGender("F");
		customer.setIncome(12345);
		customer.setPhone("555-12345");
		customer.setRegion(12);
		customer.setState("Stateland");
		customer.setZip(55555);
		return customer;
	}
	
	@Test
	public void testCreateCustomer() {
		Customer customer = newCustomer();
		String username = customer.getUserName();
		customer = dellstore.update(customer);
		Customer retrievedCustomer = dellstore.getCustomer(username);
		assertEquals(customer, retrievedCustomer);
		dellstore.remove(retrievedCustomer); // cleanup
		retrievedCustomer = dellstore.getCustomer(username);
		assertEquals(null, retrievedCustomer);
	}

	@Test
	public void testDeleteCustomer() {
		Customer customer = newCustomer();
		String username = customer.getUserName();
		customer = dellstore.update(customer);
		Customer retrievedCustomer = dellstore.getCustomer(username);
		assertEquals(customer, retrievedCustomer);
		dellstore.remove(retrievedCustomer); // cleanup
		//ToDo 
		// create a customer with orders and delete customer
	}
	
	@Test
	public void testGetOrders() {
		if (MANY_TESTS) {
			List<Order> allOrders = dellstore.getOrders(null, Integer.MAX_VALUE, 0);
			assertEquals(12000, allOrders.size());
		}
		Customer customer = dellstore.getCustomer(19887);
		List<Order> orders = customer.getOrders();
		assertEquals(6, orders.size());
		List<Long> orderids = new ArrayList<Long>();
		orderids.add(new Long(2721));
		orderids.add(new Long(4026));
		orderids.add(new Long(5581));
		orderids.add(new Long(7124));
		orderids.add(new Long(10020));
		orderids.add(new Long(11863));
		for (Order o: orders) {
			assertTrue(orderids.contains(o.getOrderId()));
		}	
	}

	@Test
	public void testSearchProducts() {
		List<Product> products;		
		String titlePart = "ACE";
		assertEquals(1045, dellstore.countSearchProducts(titlePart, null, null));
		if (MANY_TESTS) {
			products = dellstore.searchProducts(titlePart, null, null, null, true, Integer.MAX_VALUE, 0);
			assertEquals(1045, products.size());
		}
		products = dellstore.searchProducts(titlePart, null, null, null, true, 10, 1);
		assertEquals(10, products.size());
		for (Product p : products) {
			assertTrue(p.getTitle().contains(titlePart.toUpperCase()));
		}
		String actorPart = "Pene";
		assertEquals(61, dellstore.countSearchProducts(null, actorPart, null));
		if (MANY_TESTS) {
			products = dellstore.searchProducts(null, actorPart, null, null, true, Integer.MAX_VALUE, 0);
			assertEquals(61, products.size());
		}
		products = dellstore.searchProducts(null, actorPart, null, null, true, 10, 1);
		assertEquals(10, products.size());
		for (Product p : products) {
			assertTrue(p.getActor().contains(actorPart.toUpperCase()));
		}
		Map<Integer, Integer> mCatNoProducts = new TreeMap<Integer, Integer>();
		mCatNoProducts.put(16, 620);
		mCatNoProducts.put(15, 625);
		mCatNoProducts.put(14, 622);		
		mCatNoProducts.put(13, 610);
		mCatNoProducts.put(12, 643);
		mCatNoProducts.put(11, 588);
		mCatNoProducts.put(10, 616);
		mCatNoProducts.put( 9, 647);
		mCatNoProducts.put( 8, 627);
		mCatNoProducts.put( 7, 596);
		mCatNoProducts.put( 6, 637);
		mCatNoProducts.put( 5, 630);
		mCatNoProducts.put( 4, 643);
		mCatNoProducts.put( 3, 628);
		mCatNoProducts.put( 2, 641);
		mCatNoProducts.put( 1, 627);
		if (MANY_TESTS) {
			for (Category category : dellstore.getCategories()) {
				if (category.getCategoryId() <= 3) { // takes too long otherwise
					products = dellstore.searchProducts(null,null, category,null, true, 1000, 0);
					assertEquals((Integer) mCatNoProducts.get(category.getCategoryId()), (Integer) products.size());
				}
			}
		}
	}
	
	@Test
	public void testUpdateCustomer() {
		Customer customer = dellstore.getCustomer(1);
		String firstName = customer.getFirstName();
		String newFirstName = "Susi";
		customer.setFirstName("Susi");
		customer = dellstore.update(customer);
		Customer againCustomer = dellstore.getCustomer(1);
		assertEquals(newFirstName, againCustomer.getFirstName());
		againCustomer.setFirstName(firstName);
		againCustomer = dellstore.update(againCustomer);
		Customer yetAgainCustomer = dellstore.getCustomer(1);
		assertEquals(firstName, yetAgainCustomer.getFirstName());
	}
	
	/**
	 * 
	 */
	@Test 
	public void testOrder() {
		// the first customer has no orders
		Customer customer = dellstore.getCustomer(1);
		List<Order> orders = customer.getOrders();
		assertEquals(0, orders.size());
		// Order every other ith product of the first 20 products i times 
		// and remove it again
		List<OrderLine> orderLines = new ArrayList<OrderLine>();
		for (int i=1; i < 20; i += 2) {
			OrderLine orderLine = new OrderLine();
			orderLine.setProduct(dellstore.getProduct(i));
			orderLine.setQuantity(i);
			orderLines.add(orderLine);
		}
		Order generatedOrder = null;
		try {
			generatedOrder = dellstore.purchase(customer, orderLines);
		} catch (InsufficientQuantityException e) {
			List<Product> failedOrderLines = e.getProducts();
			String s = "";
			for (Product product: failedOrderLines) {
				s += product.getProductId() + " ";
			}
			fail("InsufficientQuantityException:" + e.getMessage() + "failed products: " + s);
		}
		customer = dellstore.update(customer);
		orders = customer.getOrders();
		assertEquals(1, orders.size());
		assertTrue("otherwise not the only order", orders.size() == 1); 
		Order retrievedOrder = orders.get(0);
		assertEquals(generatedOrder.getCustomer(), retrievedOrder.getCustomer());
		assertEquals(generatedOrder.getNetAmount(), retrievedOrder.getNetAmount());
		assertEquals(generatedOrder.getOrderDate(), retrievedOrder.getOrderDate());
		assertEquals(generatedOrder.getOrderId(), retrievedOrder.getOrderId());
		assertEquals(generatedOrder.getTax(), retrievedOrder.getTax());
		assertEquals(generatedOrder.getTotalAmount(), retrievedOrder.getTotalAmount());
		List<OrderLine> lolg = generatedOrder.getOrderLines();
		List<OrderLine> lolr = retrievedOrder.getOrderLines();
		assertEquals(lolg.size(), lolr.size());
//		for (OrderLine ol: lolg) {
//			System.out.println("gen:" + ol.toString());
//		}
//		for (OrderLine ol: lolr) {
//			System.out.println("ret:" + ol.toString());
//		}
		for (OrderLine olg: lolg) {
			boolean hasIt = false;
			Product pg = olg.getProduct();
			for (OrderLine olr : lolr) {
				Product pr = olr.getProduct(); 
				if (pg.equals(pr)) {
					hasIt = true;
					assertEquals(olg.getQuantity(), olr.getQuantity());
				}
			}
			if (!hasIt) {
				fail(olg.toString() + " not in the other");
			}
		}
		assertEquals(generatedOrder, retrievedOrder);
		dellstore.remove(retrievedOrder);
		customer = dellstore.getCustomer(customer.getCustomerId());
		orders = customer.getOrders();
		assertEquals(0, orders.size());
		// fail an order with insufficient quantity
		orderLines = new ArrayList<OrderLine>();
		long toFail = 6;
		for (int i=1; i <= 10; i++) {
			OrderLine orderLine = new OrderLine();
			Product product = dellstore.getProduct(i);
			orderLine.setProduct(product);
			// the sixth product has a too high Quantity
			int quantity = product.getQuantity();
			if (i == toFail) {
				quantity += 10; // too much
			}
			orderLine.setQuantity(quantity);
			orderLines.add(orderLine);
		}
	// order.setOrderLines(orderLines);
		try {
			generatedOrder = dellstore.purchase(customer, orderLines);
			fail("Ordering worked but should have thrown InsufficientQuantityException");
		} catch (InsufficientQuantityException e) {
			List<Product> failedOrderLines = e.getProducts();
			assertEquals(1, failedOrderLines.size());
			Product product = failedOrderLines.get(0);
			assertEquals(toFail, product.getProductId());
		}		
	}
	
	@Test
	public void testOrderUpdateQuantity() {
		// the first customer has no orders
		int productId = 1;
		int toBuy = 3;
		Customer customer = dellstore.getCustomer(1);
		List<Order> orders = customer.getOrders();
		assertEquals(0, orders.size());
		List<OrderLine> lines = new ArrayList<OrderLine>();
		Product product = dellstore.getProduct(productId);
		int available = product.getQuantity();
		OrderLine orderLine = new OrderLine();		
		orderLine.setProduct(product);
		orderLine.setQuantity(toBuy);
		lines.add(orderLine);
		Order order=null;
		try {
			order = dellstore.purchase(customer, lines);
		} catch (Exception e) { // InsufficentQuantity or OptimisticLock
			if (order != null) {
				try  {
					dellstore.remove(order);
				} catch (Exception e2) {
					// whatever, did my best
				}
			}
			fail("Should be possible to order");
			return;
		}
		assertEquals(available-toBuy, product.getQuantity());
		try {
			dellstore.remove(order);
		} catch (Exception e) {
			// whatever, did my best
			fail("Should be possible to remove order at the end");
		}
		product = dellstore.getProduct(product.getProductId());
		assertEquals(available, product.getQuantity());
	}
	
	@Test
	public void testCustomerSearch() {
		List<Customer> customers;
		String userName = null;
		String firstName = null;
		String lastName = null;
		String address1 = null;
		String address2 = null;
		String city = null;
		String state = null;
		Integer zip = null;
		String country = null;
		Integer region = null;
		String email = null;
		String phone = null;
		Integer creditCardType = null;
		String creditCard = null;
		String creditCardExpiration = null;
		Integer age = null;
		Integer income = null;
		String gender = null;
		int howmany = Integer.MAX_VALUE;
		
		firstName = "DFE";
		customers = dellstore.searchCustomers(userName, firstName, lastName, address1, address2, 
				city, state, zip, country, region, email, phone, 
				creditCardType, creditCard, creditCardExpiration, age, income, gender, 
				howmany);
		assertEquals(7, customers.size());
		assertEquals(7, dellstore.countSearchCustomers(userName, firstName, lastName, address1, address2, 
				city, state, zip, country, region, email, phone, 
				creditCardType, creditCard, creditCardExpiration, age, income, gender));
		customers = dellstore.searchCustomers(userName, firstName, lastName, address1, address2, 
				city, state, zip, country, region, email, phone, 
				creditCardType, creditCard, creditCardExpiration, age, income, gender, 
				howmany);
		assertEquals(7, customers.size());
		firstName = "AB";
		lastName = "XY";
		customers = dellstore.searchCustomers(userName, firstName, lastName, address1, address2, 
				city, state, zip, country, region, email, phone, 
				creditCardType, creditCard, creditCardExpiration, age, income, gender, 
				howmany);
		assertEquals(3, customers.size());		
	}
	
	@Test
	public void testOrderSearch() {
		Product product = dellstore.getProduct(1);
		List<Order> orders = dellstore.getOrders(product, Integer.MAX_VALUE, 0);
		assertEquals(4, orders.size());
		product = dellstore.getProduct(2);
		orders = dellstore.getOrders(product, Integer.MAX_VALUE, 0);
		assertEquals(8, orders.size());
	}
	
	@Test
	public void testUpdateProduct() {
		Product product = dellstore.getProduct(1);
		String title = product.getTitle();
		String newTitle = "Tarzans Flucht durch die Abortspülung";
		product.setTitle(newTitle);
		product = dellstore.update(product);
		Product againProduct = dellstore.getProduct(1);
		assertEquals(newTitle, againProduct.getTitle());
		againProduct.setTitle(title);
		againProduct = dellstore.update(againProduct);
		Product yetAgainProduct= dellstore.getProduct(1);
		assertEquals(title, yetAgainProduct.getTitle());
	}
	
	@Test
	public void testGetAllCustomers() {
		int no_retrieve = 100;
		int no_retrieved = no_retrieve;
		if (MANY_TESTS) {
			no_retrieve = Integer.MAX_VALUE;
			no_retrieved = 20000;
		}
		List<Customer> customers = dellstore.searchCustomers(null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, no_retrieve);
		assertEquals(no_retrieved, customers.size()); // around .5 seconds for all		
	}
	
	@Test
	public void testGetAllProducts() {
		int no_retrieve = 100;
		int no_retrieved = no_retrieve;
		if (MANY_TESTS) {
			no_retrieve = Integer.MAX_VALUE;
			no_retrieved = 10000;
		}
		List<Product> products = dellstore.searchProducts(null, null, null, null, true, no_retrieve, 0);
		assertEquals(no_retrieved, products.size()); // around 1 second for all		
	}
}
