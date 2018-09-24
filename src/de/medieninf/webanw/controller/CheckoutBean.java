package de.medieninf.webanw.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import de.medieninf.webanw.dellstore.Customer;
import de.medieninf.webanw.dellstore.DellStoreBean;
import de.medieninf.webanw.dellstore.InsufficientQuantityException;
import de.medieninf.webanw.dellstore.Order;
import de.medieninf.webanw.dellstore.OrderLine;
import de.medieninf.webanw.dellstore.Product;
import de.medieninf.webanw.util.SessionUtils;
@ManagedBean(name="checkout")
@RequestScoped
/**
 * Bean for managing shopping cart checkout action
 *
 */
public class CheckoutBean implements Serializable{

	private static final long serialVersionUID = 611246663340594648L;
	private DellStoreBean dellstore;
	private String checkOutResult;
	private List<OrderLine> items;
	
	public CheckoutBean(){
		
		dellstore = new DellStoreBean();
		checkOutResult = "";
		
	}
	
	public void setItems(List<OrderLine> i){
		
		this.items = i;
	}
	
	public List<OrderLine> getItems(){
		return this.items;
	}
	
	public String getCheckOutResult(){
		return this.checkOutResult;
	}
	/**
	 * Checkout shopping cart
	 * @return 
	 */
	public String checkout(CartBean cart){
		int i = cart.getTotalItems();
		this.items = cart.getItems();
		//TODO check quantity!
		String user = SessionUtils.getUserName();
		
		Customer customer = dellstore.getCustomer(user);
		System.out.println("CUSTOMER IN CART:");
		System.out.println(customer.toString());
		this.dellstore = new DellStoreBean();
		List<OrderLine> checkItems = new ArrayList<OrderLine>();
		
		try {
			for (OrderLine line : items){
				checkItems.add(makeLine(dellstore.getProduct(line.getProduct().getProductId()),line.getQuantity()));
			}
			dellstore.purchase(customer, checkItems);
			System.out.println("ITEMS CHECKOUT:");
			System.out.println(items.toString());
			checkOutResult = "checkout";
			items.clear();
			i = 0;
			
		} catch (InsufficientQuantityException | IllegalStateException e) {
			if(e instanceof InsufficientQuantityException){
				//set items in cart
				i = 0;
				for(OrderLine line : items){
					
					i += line.getQuantity();
				}
				FacesContext.getCurrentInstance().addMessage(
						user,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Nicht genügend Exemplare verfügbar!Oben aufgeführte Positionen wurden nicht bestellt!",
								"Please remove items from cart"));
				cart.setTotalItems(i);
			}else{
				FacesContext.getCurrentInstance().addMessage(
						user,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Bestandsänderung, bitte aktualisieren Sie den Warenkorb!",
								"Please remove items from cart"));
				
			}
			
			
			
		}finally{
			cart.setTotalItems(0);
			cart.setTotal(BigDecimal.ZERO);
			//SessionUtils.getSession().setAttribute("totalItems", totalItems);
		}
		
		return checkOutResult;
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
		l.setOrder(new Order());
		l.setVersion(1);

		System.out.println("IN MAKE LINE:");
		System.out.println(l.toString());
		return l;
	}

}
