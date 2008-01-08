package org.araneaframework.example.main.business.data;

import java.util.List;
import org.araneaframework.example.main.business.model.GeneralMO;

public interface IGeneralDAO {

	/**
	 * Reads an object with specified class and Id. Returned object can be casted
	 * into specified class afterwards.
	 * 
	 * @param clazz
	 *          object's class.
	 * @param id
	 *          object's Id.
	 * @return object with the specified Id and class.
	 */
	public abstract GeneralMO getById(Class clazz, Long id);

	/**
	 * Reads all objects with specified class. Returned objects can be casted into
	 * specified class afterwards.
	 * 
	 * @param clazz
	 *          objects' class.
	 * @return all objects with the specified class.
	 */
	public abstract List getAll(Class clazz);

	/**
	 * Stores a new object and returns its Id.
	 * 
	 * @param object
	 *          object.
	 * @return object's Id.
	 */
	public abstract Long add(GeneralMO object);

	/**
	 * Stores an existing object.
	 * 
	 * @param object
	 *          object.
	 */
	public abstract void edit(GeneralMO object);

	/**
	 * Removes an object with specified class and Id.
	 * 
	 * @param clazz
	 *          object's class.
	 * @param id
	 *          object's Id.
	 */
	public abstract void remove(Class clazz, Long id);

}