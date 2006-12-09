/**
 * Copyright 2006 Webmedia Group Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
**/

package org.araneaframework.example.main.business.data;

import java.util.List;
import org.araneaframework.example.main.business.model.GeneralMO;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * This is general data access object. It can retrieve all objects by class, one
 * object by Id and class, add or edit an object or remove an object by it's Id
 * and class.
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class GeneralDAO extends HibernateDaoSupport {

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
	public GeneralMO getById(Class clazz, Long id) {
		return (GeneralMO) getHibernateTemplate().get(clazz, id);
	}

	/**
	 * Reads all objects with specified class. Returned objects can be casted into
	 * specified class afterwards.
	 * 
	 * @param clazz
	 *          objects' class.
	 * @return all objects with the specified class.
	 */
	public List getAll(Class clazz) {
		return getHibernateTemplate().find("from " + clazz.getName());
	}

	/**
	 * Stores a new object and returns its Id.
	 * 
	 * @param object
	 *          object.
	 * @return object's Id.
	 */
	public Long add(GeneralMO object) {
		getHibernateTemplate().save(object);
		return object.getId();
	}

	/**
	 * Stores an existing object.
	 * 
	 * @param object
	 *          object.
	 */
	public void edit(GeneralMO object) {
		getHibernateTemplate().update(object);
	}

	/**
	 * Removes an object with specified class and Id.
	 * 
	 * @param clazz
	 *          object's class.
	 * @param id
	 *          object's Id.
	 */
	public void remove(Class clazz, Long id) {
		getHibernateTemplate().delete(getById(clazz, id));
	}
}
