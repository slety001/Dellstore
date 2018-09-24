package de.medieninf.webanw.util;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value="digitsvalid")
public class DigitsValidator implements Validator, StateHolder, Serializable{

	@Override
	public boolean isTransient() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void restoreState(FacesContext arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object saveState(FacesContext arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTransient(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException {
		Integer zahl;
		String uiName;
		String uiId = arg1.getId();
		switch(uiId){
			case "zip": 
				uiName = "PLZ";
				break;
			case "region": 
				uiName = "Region";
				break;
			case "cct":
				uiName = "Kreditkartentyp";
				break;
			case "age":
				uiName = "Alter";
				break;
			case "income":
				uiName = "Einkommen";
				break;
			default:
				uiName = "";
				break;
		}
		try{
			String str = arg2.toString();
			zahl = Integer.valueOf(str);
		}catch(ClassCastException e){
			FacesMessage msg = new FacesMessage(uiName + " darf nur aus Ziffern bestehen ");
			throw new ValidatorException(msg);
		}catch(NumberFormatException e){
			FacesMessage msg = new FacesMessage(uiName + " darf nur aus Ziffern bestehen ");
			throw new ValidatorException(msg);
		}
		if(zahl < 1){
			FacesMessage msg = new FacesMessage(uiName + " darf nicht negativ sein");
			throw new ValidatorException(msg);
		}
		
	}

}
