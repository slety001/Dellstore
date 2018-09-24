package de.medieninf.webanw.controller;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;

import de.medieninf.webanw.dellstore.Customer;
import de.medieninf.webanw.dellstore.DellStoreBean;
import de.medieninf.webanw.persist.PersistenzDieZweite;
import de.medieninf.webanw.util.Pager;

@ManagedBean(name="kunden")
@SessionScoped
public class Kunden implements Serializable {

	private static final long serialVersionUID = -792480712111545537L;
	
	private List<Customer> customers;
	private PersistenzDieZweite otherpersist;
	private transient DellStoreBean dellstore;
	private Customer searchcustomer;
	private Customer editCustomer;
	private Pager pager;
	private String orderBy;
	private String orderByOld;
	private Customer selectedCust;
	private Customer currentCopy;
	private boolean ascending;
	private boolean search = false;
	private boolean edit;
	private boolean isActive = false;
	private DataModelBackingBean beanchen;
	private Method method;
	private SearchCustomer searchCust;

	public Kunden() {
		pager = new Pager();
		otherpersist = new PersistenzDieZweite();
		dellstore = new DellStoreBean();
		searchcustomer = new Customer();
		editCustomer = new Customer();
		orderBy = "getCustomerId";
		orderByOld = "getCustomerId";
		ascending = true;
		//customers = retrieveCustomersData();
		selectedCust = null;
		edit = false;
		beanchen = new DataModelBackingBean();
		searchCust = new SearchCustomer();
	}
	
	
	public void setSelectedCust(long customerId){
		
		this.selectedCust = dellstore.getCustomer(customerId);
	}
	public Customer getEditCustomer(){
		
		return this.editCustomer;
	}
	public Customer getSelectedCust(){
		
		return this.selectedCust;
	}
	public boolean getIsActive(){
		
		return this.isActive;
	}
	public void setCurrentCopy(Customer c){
		
		this.currentCopy = c;
	}
	
	public Customer getCurrentCopy(){
		
		return this.currentCopy;
	}
	
	/**
	 * Shows customer details for selected customer
	 * when "Benutzername" is clicked
	 * @param id
	 * @return
	 */
	public String details(ActionEvent ae){
		
		//Customer aus DataModel der Tabelle
		this.setSelectedCust((long)ae.getComponent().getAttributes().get("user"));
		
		/*Kopie mit Änderungen (gleiche Version wie Ausgangsdaten!)
		 *wird in update geprüft, wenn nicht gleich dann sind zwischenzeitlich Änderungen gemacht worden!
		 */
		currentCopy = new Customer(selectedCust);
		currentCopy.setVersion(selectedCust.getVersion());
		return (selectedCust != null ? "details" : "error");
	}
	
	public boolean getEdit(){
		return this.edit;
	}
	
	public void setEdit(boolean val){
		
		edit= val;
	}
	
	/**
	 * toggles edit user details
	 */
	public void makeEdit(){
		
		edit = !edit;
	}
	
	/**
	 * Navigation case for 'Zurück' Button at userdetails
	 * to switch edit to false
	 * @return
	 */
	public String backToView(){
		
		this.setEdit(false);
		return "back";
	}
	/*public List<Customer> getCustomers() {		
		return this.customers;
	}*/
	
	/*public List<Customer> retrieveCustomersData(){
		pager.setSize(getCustomerscount());
		customers = this.otherpersist.getCustomers(pager.getNoRows(), pager.getFirst(), orderBy, ascending);
		return customers;
	}*/

	/*public int getCustomerscount() {
		int count = this.otherpersist.getCount("Customer");
		return count;
	}*/
	
	/*public void browse(ActionEvent ae){
		UIComponent cmdBrowse = ae.getComponent();
		FacesContext fcBrowse = FacesContext.getCurrentInstance();
		String str = cmdBrowse.getId();
		switch(str){
			case "start": 
				pager.start();
				break;
			case "vor":
				pager.vor();
				break;
			case "zurueck":
				pager.zurueck();
				break;
			case "ende":
				pager.ende();
				break;
			default:
				pager.start();
				break;
		}
		if(search){
			search();
		}else{
			customers = retrieveCustomersData();
		}
	}*/

	public Pager getPager() {
		return pager;
	}

	public void sort(ActionEvent e) {
		UIComponent cmd = e.getComponent();
		FacesContext fc = FacesContext.getCurrentInstance();
		orderBy = cmd.getId();
		beanchen.getPager().initFirst();
		if (orderBy.equals(orderByOld)) {
			ascending = (ascending) ? false : true;
		} else {
			ascending = true;
			orderByOld = orderBy;
		}
		sortBy(orderBy, ascending);
	}
	
	/*
	 * Sortieren von der Liste von Kuden
	 */
	
	public void sortBy(String orderBy, boolean ascending){
		Collections.sort(beanchen.getListe(), new Comparator<Customer>(){				
			public int compare(Customer o1, Customer o2){
				
				try {
					method = o1.getClass().getMethod(orderBy);
				} catch (NoSuchMethodException | SecurityException e1) {
					e1.printStackTrace();
				}
				try {
					method = o2.getClass().getMethod(orderBy);
				} catch (NoSuchMethodException | SecurityException e1) {
					e1.printStackTrace();
				}
				try {
					if(orderBy.equals("getCustomerId")){
						if(ascending){
							return ((Long)(method.invoke(o1))).compareTo((Long)(method.invoke(o2)));
						}else{
							return ((Long)(method.invoke(o2))).compareTo((Long)(method.invoke(o1)));
						}
					}else if(orderBy.equals("getAge")){
						if(ascending){
							return ((Integer)(method.invoke(o1))).compareTo((Integer)(method.invoke(o2)));
						}else{
							return ((Integer)(method.invoke(o2))).compareTo((Integer)(method.invoke(o1)));
						}
					}else{
						if(ascending){
							return (String.valueOf(method.invoke(o1))).compareTo(String.valueOf(method.invoke(o2)));
						}else{
							return (String.valueOf(method.invoke(o2))).compareTo(String.valueOf(method.invoke(o1)));
						}
					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassCastException  e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return 0;
			}
		});
	}

	/*
	 * Suchen in der Liste von Kunden
	 */
	
	public void search() {
		isActive = true;
		search = true;
		orderBy = "getCustomerId";
		orderByOld = "getCustomerId";
		beanchen.getPager().initFirst();
		ascending = true;
		/*customers = this.dellstore.searchCustomers(searchCust.getUserName().toUpperCase(), searchCust.getFirstName().toUpperCase(),
				searchCust.getLastName().toUpperCase(), searchCust.getAddress1().toUpperCase(), searchCust.getAddress2().toUpperCase(),
				searchCust.getCity().toUpperCase(), searchCust.getState().toUpperCase(), Integer.valueOf(searchCust.getZip()),
				searchCust.getCountry().toUpperCase(), Integer.valueOf(searchCust.getRegion()), searchCust.getEmail().toUpperCase(),
				searchCust.getPhone().toUpperCase(), Integer.valueOf(searchCust.getCreditCardType()), searchCust.getCreditCard().toUpperCase(),
				searchCust.getCreditCardExpiration().toUpperCase(), Integer.valueOf(searchCust.getAge()), Integer.valueOf(searchCust.getIncome()),
				searchCust.getGender().toUpperCase(), 200);*/
		beanchen.setSearch(searchCust);
		
	}

	public SearchCustomer getSearchCust() {
		if(searchCust == null){
			searchCust = new SearchCustomer();
		}
		return searchCust;
	}


	public void setSearchCust(SearchCustomer searchCust) {
		this.searchCust = searchCust;
	}

	/*
	 * Ausgabe der Liste von Kunden und leoschen von searchListe
	 */
	
	public String select() {
		beanchen.makeSearchLbnDead();
		//pager = new Pager();
		orderBy = "getCustomerId";
		orderByOld = "getCustomerId";
		ascending = true;
		searchCust = null;
		search = false;
		sortBy(orderBy, ascending);
		beanchen.getPager().initFirst();
		//customers = retrieveCustomersData();
		return "kunden";
	}
	
	public DataModel<Customer> getBeanchen(){
		return beanchen.getCustModel();
	}
	
	public Pager getBeanchenPager(){
		return beanchen.getPager();
	}

	public Customer getSearchcustomer() {
		if(searchcustomer == null){
			searchcustomer = new Customer();
		}
		//System.out.println("getSearchcustomer(): "+searchcustomer.getAge());
		return this.searchcustomer;
	}
	/**
	 * sends edited customer data to database 
	 * and checks it
	 * @return kundenDetail
	 * @throws Exception
	 */
	public String sendEdit()throws Exception{
		
		Customer newCust = null;
			try {
				newCust = dellstore.update(currentCopy);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(e.getLocalizedMessage()));
			}
		if(newCust != null){
			
			
			
			for(Customer cust : beanchen.getListe()){
				
				if( cust.getCustomerId() == newCust.getCustomerId()){
					cust.setAddress1(newCust.getAddress1());
					cust.setAddress2(newCust.getAddress2());
					cust.setAge(newCust.getAge());
					cust.setCity(newCust.getCity());
					cust.setCountry(newCust.getCountry());
					cust.setCreditCard(newCust.getCreditCard());
					cust.setCreditCardExpiration(newCust.getCreditCardExpiration());
					cust.setCreditCardType(newCust.getCreditCardType());
					cust.setEmail(newCust.getEmail());
					cust.setFirstName(newCust.getFirstName());
					cust.setGender(newCust.getGender());
					cust.setHashedPassword(newCust.getHashedPassword());
					cust.setIncome(newCust.getIncome());
					cust.setLastName(newCust.getLastName());
					cust.setOrders(newCust.getOrders());
					cust.setPhone(newCust.getPhone());
					cust.setRegion(newCust.getRegion());
					cust.setState(newCust.getState());
					cust.setZip(newCust.getZip());
					cust.setVersion(newCust.getVersion());
				}
			}
			
		}else{
			
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage msg =new FacesMessage("Objekt nicht gültig");
			fc.addMessage("kundenDetail", msg);
		}
		this.makeEdit();
		return "kundenDetail";
	}
}
