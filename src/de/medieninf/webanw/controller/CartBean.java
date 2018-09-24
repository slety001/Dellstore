package de.medieninf.webanw.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import de.medieninf.webanw.dellstore.Customer;
import de.medieninf.webanw.dellstore.Order;
import de.medieninf.webanw.dellstore.OrderLine;
import de.medieninf.webanw.dellstore.Product;

@ManagedBean(name="cart")
@SessionScoped
/**
 * Bean for managing shopping cart
 *
 */
public class CartBean implements Serializable {

	private static final long serialVersionUID = -8166356190578817731L;
	private List<OrderLine> items;
	private Customer customer;
	private Order order;
	private int selQuant;
	private String checkOutResult = "";
	private int totalItems;
	private BigDecimal total;

	public CartBean() {
		
		order = new Order();
		items = new ArrayList<OrderLine>();
		selQuant = 0;
		totalItems = 0;
		total = BigDecimal.ZERO;
	}
	
	@PostConstruct
	public void init(){
		
		this.setCheckOutResult("");
		
	}
	public void onload(){
		this.checkOutResult = "";
	}
	public void setTotal(BigDecimal t){
		
		this.total = t;
	}
	
	public BigDecimal getTotal(){
		
		return this.total;
	}
	public void setTotalItems(int quant){
		
		this.totalItems = quant;
		
	}
	
	public int getTotalItems(){
		
		return this.totalItems;
	}
	public List<OrderLine> getItems() {
		return items;
	}

	public void setItems(List<OrderLine> items) {
		this.items = items;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setSelQuant(int quant) {

		this.selQuant = quant;
	}

	public int getSelQuant() {

		return this.selQuant;
	}
	
	public String getCheckOutResult(){
		return this.checkOutResult;
	}
	
	public void setCheckOutResult(String r){
		
		this.checkOutResult = r;
	}
	/**
	 * Add item (OrderLine) to cart. If item already in cart, update quantity
	 * 
	 * @param line
	 */
	public String addItem(Product prod, int quant) {

		// make OrderLine from Product
		OrderLine line = makeLine(prod, quant);
		if(quant > prod.getQuantity()){
			FacesContext.getCurrentInstance().addMessage("",new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Nicht genügend Exemplare verfügbar!",
							"Please remove items from cart"));
			return "error";
		}
		boolean valid = true;
		System.out.println("ITEM QUANT:");
		System.out.println(quant);
		for (OrderLine item : items) {

			if (item.getProduct().getProductId() == prod.getProductId()) {
				totalItems += quant;
				item.addQuantity(quant);
				System.out.println("adding Items old");
				
				valid = false;
				break;
			}
		}

		if (valid) {
			items.add(line);
			System.out.println("adding Items new");
			totalItems += line.getQuantity();
		}
		//add to total
		BigDecimal itemCost  = prod.getPrice().multiply(new BigDecimal(quant));
        total = total.add(itemCost);
		this.setSelQuant(0);
		System.out.println(items.toString());
		return "added";
	}
	/**
	 * Generates orderline from given product p with quantity q
	 * @param p Product
	 * @param q Quantity
	 * @return orderline
	 */
	private OrderLine makeLine(Product p, int q) {

		OrderLine l = new OrderLine();
		l.setProduct(p);
		l.setQuantity(q);
		l.setOrderDate(new Date());
		l.setOrder(order);
		l.setVersion(1);

		System.out.println("IN MAKE LINE:");
		System.out.println(l.toString());
		return l;
	}
	
	/**
	 * removes selected OrderLine from cart
	 * @return
	 */
	public String remove(Product p){
		
		List<OrderLine> removeItems = new ArrayList<OrderLine>();
		for(OrderLine line : items){
			
			if(line.getProduct().getProductId() == p.getProductId()){
				BigDecimal itemCost  = p.getPrice().multiply(new BigDecimal(line.getQuantity()));
				total = total.subtract(itemCost);
				removeItems.add(line);
				totalItems -= line.getQuantity();
			}
		}
		
		items.removeAll(removeItems);		        		
		return "removed";
	}
	
	public void resetBadge(){
		totalItems = 0;
	}
	
	
}
