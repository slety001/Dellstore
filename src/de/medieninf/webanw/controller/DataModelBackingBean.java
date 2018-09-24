package de.medieninf.webanw.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import de.medieninf.webanw.dellstore.Customer;
import de.medieninf.webanw.dellstore.DellStoreBean;
import de.medieninf.webanw.persist.PersistenzDieZweite;
import de.medieninf.webanw.util.Pager;

@ManagedBean(name = "dmbb")
@ApplicationScoped
public class DataModelBackingBean implements Serializable {
	private static final long serialVersionUID = 2576873619909065843L;

	private DellStoreBean dellstore;
	private PersistenzDieZweite otherpersist;
	private Pager pager;
	private List<Customer> lbn; // NameBean list. <Customer>.
	private List<Customer> searchlbn;
	transient private DataModel<Customer> custModel; // not serializable

	public DataModelBackingBean() {
		
		dellstore = new DellStoreBean();
		otherpersist = new PersistenzDieZweite();
		pager = new Pager();
		lbn = retrieveData();  
		searchlbn = null;
		custModel = new ListDataModel<Customer>();
		custModel.setWrappedData(lbn);
		pager.setSize(lbn.size());
	}

	/*
	 * Laden von der Liste, die bei der Kundenanzeigen geladen wird
	 */
	
	public List<Customer> retrieveData() {
		return dellstore.searchCustomers(null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, 0);
	}
	
	/*
	 * Auswahl von Liste, die in DataModel geladen wird
	 */
	
	public List<Customer> getListe() {
		if (searchlbn == null){
			return lbn;
		}else{
			return searchlbn;
		}
	}
	
	/*
	 * Löschen der searchListe
	 */
	
	public void makeSearchLbnDead(){
		custModel.setWrappedData(lbn);
		pager.setSize(lbn.size());
		searchlbn = null;
	}

	public DataModel<Customer> getCustModel() {
		return custModel;
	}

	/*
	 * Setzen von der searchListe, die bei der Kundensuche in den DataModel geladen wird
	 */
	
	public void setSearch(SearchCustomer searchCust) {
		searchlbn = this.otherpersist.searchCustomers(searchCust.getUserName().toUpperCase(),
				searchCust.getFirstName().toUpperCase(), searchCust.getLastName().toUpperCase(),
				searchCust.getAddress1().toUpperCase(), ((searchCust.getAddress2() == null) ? null : searchCust.getAddress2().toUpperCase()),
				searchCust.getCity().toUpperCase(), searchCust.getState().toUpperCase(),
				((searchCust.getZip() == null) ? null : Integer.valueOf(searchCust.getZip())),
				searchCust.getCountry().toUpperCase(),
				((searchCust.getRegion() == null) ? null : Integer.valueOf(searchCust.getRegion())),
				searchCust.getEmail().toUpperCase(), searchCust.getPhone().toUpperCase(),
				(searchCust.getCreditCardType() == null) ? null : Integer.valueOf(searchCust.getCreditCardType()),
				searchCust.getCreditCard().toUpperCase(), searchCust.getCreditCardExpiration().toUpperCase(),
				((searchCust.getAge() == null) ? null : Integer.valueOf(searchCust.getAge())),
				((searchCust.getIncome() == null) ? null : Integer.valueOf(searchCust.getIncome())),
				searchCust.getGender().toUpperCase(), 0, 0, "customerId", true);
		custModel.setWrappedData(searchlbn);
		pager.setSize(searchlbn.size());
	}

	public Pager getPager() {
		return pager;
	}
}
