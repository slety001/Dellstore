package de.medieninf.webanw.controller;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import de.medieninf.webanw.controller.ShopBean;
import de.medieninf.webanw.dellstore.Customer;
import de.medieninf.webanw.dellstore.DellStore;
import de.medieninf.webanw.dellstore.DellStoreBean;
import de.medieninf.webanw.util.SessionUtils;

@ManagedBean(name="login")
@SessionScoped
public class Login implements Serializable {

	private static final long serialVersionUID = -8019753948284836851L;
	
	private HttpSession session;
	private String n;
	private int pwd;
	private String msg;
	private String user;
	private String output;
	
	public Login(){		
	}
	
	public int getPwd() {
		return pwd;
	}

	public void setPwd(int pwd) {
		this.pwd = pwd;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	//validate login
	public String validateUsernamePassword() {
		boolean valid = false;
		Customer customer;
		
		FacesContext fc = FacesContext.getCurrentInstance();
		DellStore dellstore = (DellStoreBean) fc.getApplication().evaluateExpressionGet(fc, "#{dellstore}", DellStore.class);
		customer = dellstore.getCustomer(user);
		
		//check pwd with given pwd
		if(customer != null){
			if(pwd==0){
				pwd=-1;
			}
			valid = customer.getCustomerId()==pwd ? true : false; 
		}
		
		
		if (valid) {
			session = SessionUtils.getSession();
			session.setAttribute("username", user);
			ShopBean shopbean = (ShopBean) fc.getApplication().evaluateExpressionGet(fc, "#{shopbean}", ShopBean.class);
			shopbean.setUser(user);
			return "index";
		} else {
			FacesContext.getCurrentInstance().addMessage(
					user,
					new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Incorrect Username and Password",
							"Please enter correct username and Password"));
			return "login";
		}
	}
	
	public void anmelden() {
	    output = "You submitted: " +" user: "+ user + " pass: " + pwd + " In session: "  + SessionUtils.getSession() + " username in session: "+ n;
	  }

	  public String getOutput() {
	    return output;
	  }

	  public void setOutput(String output) {
	    this.output = output;
	  }
	
	//logout event, invalidate session
	public String logout() {
		HttpSession session = SessionUtils.getSession();
		session.invalidate();
		return "login";
	}
}
