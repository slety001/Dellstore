package de.medieninf.webanw.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import de.medieninf.webanw.dellstore.Customer;
import de.medieninf.webanw.dellstore.DellStoreBean;
import de.medieninf.webanw.dellstore.Order;
import de.medieninf.webanw.dellstore.Product;
import de.medieninf.webanw.persist.PersistenzDieZweite;

import javax.servlet.http.HttpSession;

import de.medieninf.webanw.util.SessionUtils;
@ManagedBean(name="shopbean")
@SessionScoped
public class ShopBean implements Serializable {
	
	//TODO remove unused variables??? Are they needed?
	private static final long serialVersionUID = 7832816757126988249L;
	private static final String HELLO = "Herzlich willkommen im DVD-Shop, ";
	private String user;
	private Customer customer;
	private DellStoreBean dellstore;
	private List<Customer> customers;
	private List<Order> orders;
	private List<Product> products;
	private Product product;
	private int quantSelect;
	private PersistenzDieZweite otherpersist;
	private CartBean cart;
	
	public ShopBean() {

		user = "Gast";
		customer = new Customer();
		dellstore = new DellStoreBean();
		otherpersist = new PersistenzDieZweite();
		customers = new ArrayList<Customer>();
		quantSelect = 0;
		cart = new CartBean();
	}

	public void setUser(String user) {

		this.user = user;
	}

	public String getUser() {
		return this.user;
	}
	
	public String getGreeting(){
		
		HttpSession session = SessionUtils.getSession();
		if(session.getAttribute("username") != null){
			
			setUser(SessionUtils.getUserName());
		}
		return HELLO + " "  + user + "!";
	}
	
	public List<Product> getAllproducts() {
		products = this.otherpersist.getProducts(10, 0, "productId", true);		
		return products;
	}
	
	/*public List<Order> getAllorders() {
		if(customers.size()==0){
			customers = getCustomers();
		}
		//List<Customer> customers = getCustomers();
		List<Order> orders = new ArrayList<Order>();
		for(Customer cust : customers){
			orders.addAll(cust.getOrders());
		}
		Collections.sort(orders, new Comparator<Order>(){
			
			public int compare(Order o1, Order o2){
				return new Long(o1.getOrderId()).compareTo(new Long(o2.getOrderId()));
			}
		});
		
		return orders;
	}*/
	
	public void setProduct(int id){
		
	}
	
	public Product getProduct(){
		
		return this.product;
	}
	
	public int getQuantSelect(){
		
		return this.quantSelect;
	}
	
	public void setQuantSelect(int quant){
		
		this.quantSelect = quant;
	}
}
