package com.example.j2eeapp.services.impl;

import java.util.ArrayList;
import java.util.Collection;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.component.inputtext.InputText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.j2eeapp.dao.UserDao;
import com.example.j2eeapp.domain.UserEntity;
import com.example.j2eeapp.services.UserService;

@Component
public class UserServiceImpl implements UserService, UserDetailsService {

	@Autowired
	private UserDao userDao;

	/**
	 * Create user - persist to database
	 * 
	 * @param userEntity
	 * @return true if success
	 */
	public boolean createUser(UserEntity userEntity) {

		if (!userDao.checkAvailable(userEntity.getUserName())) {
			FacesMessage message = constructErrorMessage(
					String.format("User Name '%s' is not available",
							userEntity.getUserName()), null);
			getFacesContext().addMessage(null, message);

			return false;
		}
		try {
			userDao.save(userEntity);
		} catch (Exception e) {
			FacesMessage message = constructInfoMessage(e.getMessage(), null);
			getFacesContext().addMessage(null, message);
			return false;
		}
		return true;
	}

	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException {

		UserEntity user = userDao.loadUserByUserName(userName);

		if (user == null) {
			throw new UsernameNotFoundException(String.format(
					"No such user with name provided '%s'", userName));
		}

		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

		User userDetails = new User(user.getUserName(), user.getPassword(),
				authorities);

		return userDetails;
	}

	protected FacesMessage constructErrorMessage(String message, String detail) {
		return new FacesMessage(FacesMessage.SEVERITY_ERROR, message, detail);
	}

	protected FacesMessage constructInfoMessage(String message, String detail) {
		return new FacesMessage(FacesMessage.SEVERITY_INFO, message, detail);
	}

	protected FacesMessage constructFatalMessage(String message, String detail) {
		return new FacesMessage(FacesMessage.SEVERITY_FATAL, message, detail);
	}

	protected FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	/**
	 * Check user name availability. UI ajax use.
	 * 
	 * @param ajax
	 *            event
	 * @return
	 */

	public boolean checkAvailable(AjaxBehaviorEvent event) {

		InputText inputText = (InputText) event.getSource();
		String value = (String) inputText.getValue();

		boolean available = userDao.checkAvailable(value);

		if (!available) {
			FacesMessage message = constructErrorMessage(null,
					String.format("User Name '%s' is not available", value));
			getFacesContext().addMessage(event.getComponent().getClientId(),
					message);
		} else {
			FacesMessage message = constructInfoMessage(null,
					String.format("User Name '%s' is available", value));
			getFacesContext().addMessage(event.getComponent().getClientId(),
					message);
		}

		return available;
	}

	/**
	 * Retrieves full User record from database by user name
	 * 
	 * @param userName
	 * @return UserEntity
	 */
	public UserEntity loadUserEntityByUsername(String userName) {
		return userDao.loadUserByUserName(userName);
	}

}
