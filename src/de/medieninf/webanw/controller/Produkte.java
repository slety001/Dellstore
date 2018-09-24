package de.medieninf.webanw.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import de.medieninf.webanw.dellstore.DellStoreBean;
import de.medieninf.webanw.dellstore.Product;
import de.medieninf.webanw.persist.PersistenzDieZweite;
import de.medieninf.webanw.util.Pager;
@ManagedBean(name="produkte")
@SessionScoped
public class Produkte implements Serializable {
	
	private static final long serialVersionUID = -5919950228317216298L;
	private List<Product> products;
	private PersistenzDieZweite otherpersist;
	private DellStoreBean dellstore;
	private Pager pager;
	private String orderBy;
	private String orderByOld;
	private boolean ascending;
	private Product selectedProd;
	private int selQuant;
	
	public Produkte(){
		otherpersist = new PersistenzDieZweite();
		dellstore = new DellStoreBean();
		products = new ArrayList<Product>();
		pager = new Pager();
		orderBy = "productId";
		orderByOld = "productId";
		ascending = true;
		selQuant = 0;
	}	
	
	public List<Product> getAllproducts() {
		pager.setSize(getProductscount());
		products = this.otherpersist.getProducts(pager.getNoRows(), pager.getFirst(), orderBy, ascending);		
		return products;
	}
	
	public int getProductscount(){
		int count = this.otherpersist.getCount("Product");
		return count;
	}

	public Pager getPager(){
		return pager;
	}
	
	public void sort(ActionEvent e){
		UIComponent cmd = e.getComponent();
		FacesContext fc = FacesContext.getCurrentInstance();
		orderBy = cmd.getId();
		pager.initFirst();
		if(orderBy.equals(orderByOld)){
			ascending = (ascending) ? false : true;
		}else{
			ascending = true;
			orderByOld = orderBy;
		}
	}

	public String select(){
		pager = new Pager();
		orderBy = "productId";
		orderByOld = "productId";
		ascending = true;
		return "produkte";
	}
	
	/**
	 * sets selected product id from products overview
	 * @param id
	 */
	public void setSelectedProd(long id){
		
		this.selectedProd = dellstore.getProduct(id);
	}
	
	/**
	 * returns selected product for product details
	 * @return selected product
	 */
	public Product getSelectedProd(){
		return this.selectedProd;
	}
	
	/**
	 * navigation case for product details
	 * only if selected prod id could be loaded
	 * @param id
	 * @return
	 */
	public String details(long id){
		
		this.setSelectedProd(id);
		
		 return (selectedProd != null ? "details" : "error");
	}
	
	/**
	 * quantity of selected product in stock
	 * @return stock quantity
	 */
	public String getStock(){
		
		return String.valueOf(selectedProd.getQuantity());
	}
	/**
	 * List mapping for output in DataTable
	 * @return
	 */
	public List<Product> getListSelected(){
		
		List<Product> sel = new ArrayList<Product>();
		sel.add(selectedProd);
		
		return sel;
	}
}

