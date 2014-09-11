package com.example.j2eeapp.uiutils;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

/**
 * Helper util to assist in user interface
 * @author hiren
 *
 */
public class UIUtils implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8045722584610432218L;

	private int viewLoadCount = 0;
	public void greetOnViewLoad(ComponentSystemEvent event){
		
		FacesContext context = FacesContext.getCurrentInstance();
		
		if(viewLoadCount < 1 && !context.isPostback()){
			
			String firstName = (String) event.getComponent().getAttributes().get("firstName");
			String lastName = (String) event.getComponent().getAttributes().get("lastName");
			
			FacesMessage message = new  FacesMessage(String.format("Welcome to your account %s %s", firstName, lastName));
		    context.addMessage("growlMessages", message);
		
		    viewLoadCount++;
		}
		
	}
}
