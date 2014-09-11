package com.example.j2eeapp.commons.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Generic interface for Data Access objects. To be extended or implementes.
 * contains common persistence methods
 * @author hiren
 *
 */


public interface GenericDao<T, ID extends Serializable> {

	T save(T entity);
	T update(T entity);
	void delete(T entity);
	T findById(ID id);
	List<T> findAll();
	void flush();
	
}
