package de.medieninf.webanw.util;

import java.util.Objects;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("estnconv")
public class EmptyStringToNullConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		//System.out.println("getAsObject: "+arg2);
		/*if(arg2.equals("")){
			System.out.println("arg2.equals vor: "+arg2);
			arg2=null;
			System.out.println("arg2.equals: "+arg2);
			return arg2;
		}else{
			System.out.println("arg2.equals: "+arg2);
			return arg2;
		}*/
		return arg2;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		String str = null;
		/*if(arg2 instanceof String){
			str = (String) arg2;
			System.out.println("STRING: "+str);
		}
		if(arg2 instanceof Long){
			str = String.valueOf((Long)arg2);
			System.out.println("LONG: "+str);
		}
		if(arg2 instanceof Integer){
			str = String.valueOf((Integer)arg2);
			System.out.println("INT: "+str);
		}*/
		str = Objects.toString(arg2, null);
		System.out.println("getAsString: "+arg2.toString());
		System.out.println("getAsString: "+str);
		if(str == null || str.equals("0"))return "";
		else return str;
	}

}
