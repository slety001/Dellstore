package de.medieninf.webanw.util;

import java.io.Serializable;

public class Pager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9147839806602147007L;
	private int first;
	private int size;
	private int noRows;
	
	public Pager(){
		first = 0;
		size = 0;
		noRows = 10;
	}
	
	public int getFirst(){
		return first;
	}
	
	public void initFirst(){
		this.first = 0;
	}
	
	public int getSize(){
		return size;
	}
	
	public void setSize(int size){
		this.size = size;
		//this.first = 0;
	}
	
	public int getNoRows(){
		return noRows;
	}
	
	public void setNoRows(int noRows){
		this.noRows = noRows;
	}
	
	public String start(){
		first = 0;
		System.out.println("first "+first);
		return null;
	}
	
	public String vor(){
		first += noRows;
		if(first > size - noRows){
			first = size - noRows;
		}
		System.out.println("first "+first);
		//FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		return null;
	}
	
	public String zurueck(){
		first -= noRows;
		if (first < 0) first = 0;
		return null;
	}
	
	public String ende(){
		first = size - noRows;
		return null;
	}
}
