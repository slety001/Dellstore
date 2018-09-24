package de.medieninf.webanw.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import de.medieninf.webanw.dellstore.Customer;
import de.medieninf.webanw.dellstore.DellStoreBean;
import de.medieninf.webanw.dellstore.Order;
import de.medieninf.webanw.persist.PersistenzDieZweite;
import de.medieninf.webanw.util.Pager;
@ManagedBean(name="orders")
@SessionScoped
public class Bestellungen implements Serializable {
	
	private static final long serialVersionUID = 4277133979047105535L;
	private List<Order> orders;
	private PersistenzDieZweite otherpersist;
	private DellStoreBean dellstore;
	private Pager pager;
	private String orderBy;
	private boolean ascending;
	private String orderByOld;
	private List<Order> custOrder;
	private Order custOrderDetails;
	private double totalPreis;
	
	public Bestellungen(){
		otherpersist = new PersistenzDieZweite();
		dellstore = new DellStoreBean();
		orders = new ArrayList<Order>();
		pager = new Pager();
		orderBy = "orderId";
		orderByOld = "orderId";
		ascending = true;
		totalPreis = 0.0;
		custOrderDetails = null;
	}	
	public List<Order> getCustOrder(){
		
		return custOrder;
	}
	
	public void setCustOrder(Customer c){
		//TODO Bestellungen für Kunden bekommen, evtl wie 1. Abgabe mit view in DB
		//und über persistdiezweite
		this.custOrder = null;
	}
	
	/**
	 * Stosst die DB-Abfrage an und holt alle Bestellungen aus der Datenbank
	 * @return orders - ArrayList mit Order-Objekten
	 */
	public List<Order> getAllorders() {
		pager.setSize(getOrderscount());
		orders = this.otherpersist.getOrders(pager.getNoRows(), pager.getFirst(), orderBy, ascending);		
		return orders;
	}
	
	/**
	 * gibt die Anzahl der Zeilen einer DB-Abfrage zurueck
	 * @return
	 */
	public int getOrderscount(){
		int count = this.otherpersist.getCount("Order");
		return count;
	}
	
	public Order getCustOrderDetails() {
		return custOrderDetails;
	}
	
	public void setCustOrderDetails(Order custOrderDetails) {
		this.custOrderDetails = custOrderDetails;
	}
	
	public double getTotalPreis() {
		return totalPreis;
	}
	
	public void setTotalPreis(double preis) {
		this.totalPreis += preis;
	}
	
	public double totalPreis(double stueckpreis, int menge) {
		double p = stueckpreis * menge;
		setTotalPreis(p);
		return p;
	}
	
	/**
	 * Holt ein Orderobjekt aus dem ActionEvent, falls eine Bestellung in Kundendetails angeklickt wurde.
	 * Rueckgabe navigiert zu orderDetails.xhtml
	 * @param ae
	 * @return "bestellungsdetails" falls erfollgreich geholt, "error" falls nicht.
	 */
	public String orderDetails(ActionEvent ae){
		this.setCustOrderDetails((Order)ae.getComponent().getAttributes().get("custOrderDetails"));
		return (custOrderDetails != null ? "bestellungsdetails" : "error");		
	}

	/**
	 * Falls in Bestellungsdetailsübersicht Button "Zurueck" geklickt wurde, gibt 
	 * "backToCustomerDetailsView" zurueck. Dadurch wird zu Kundendetails navigiert.
	 * @return "backToCustomerDetailsView"
	 */
	public String backToCustomerDetailsView(){
		totalPreis = 0.0;
		return "backToCustomerDetailsView";
	}
	
	
	public Pager getPager(){
		return pager;
	}
	
	/**
	 * Setzt Attribute, die fuer das Sortieren waehrend der DB-Abfrage benoetigt werden.
	 * @param e
	 */
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
	
	/**
	 * Wird angestossen, wenn man in der Navigationsleiste auf Bestellungen klickt.
	 * Setzt bestimmte Parameter auf Initialwert
	 * @return "bestellungen" - dient zu Navigation zu Bestellungsuebersicht
	 */
	public String select(){
		totalPreis = 0.0;
		pager = new Pager();
		orderBy = "orderId";
		orderByOld = "orderId";
		ascending = true;
		return "bestellungen";
	}
}

