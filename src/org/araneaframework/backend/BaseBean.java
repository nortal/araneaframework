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

package org.araneaframework.backend;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.backend.util.BeanMapper;

/**
 * Base class for value objects. Implements some general properties and methods.
 * 
 * @author Jevgeni Kabanov
 * @since 1.4.1.20
 */
public abstract class BaseBean implements Serializable, Cloneable {

	private static final Log log = LogFactory.getLog(BaseBean.class);

	//*******************************************************************
	// FIELDS
	//*******************************************************************

	/**
	 * Holds names of fields which are marked as changed. Is used for update/insert operations.
	 */
	private Set changes = new HashSet();

	/**
	 * Private VoMapper, used for <code>toString</code> and <code>equals</code> methods.
	 */
	private BeanMapper beanMapper;

	//*******************************************************************
	// PUBLIC METHODS
	//*******************************************************************

	/**
	 * Creates new instance of the GeneralVO.
	 */
	protected BaseBean() {
		beanMapper = new BeanMapper(getClass());
	}

	/**
	 * Overrides default <code>toString</code> method.
	 * <P/>
	 * 
	 * @return <code>java.lang.String</code> representation of this value object.
	 */
	public String toString() {
		StringBuffer result = new StringBuffer();
		List voFields = beanMapper.getFields();
		for (Iterator i = voFields.iterator(); i.hasNext();) {
			String field = (String) i.next();
			result.append(field);
			result.append("=");
			result.append("" + beanMapper.getFieldValue(this, field));
			result.append("; ");
		}
		return result.toString();
	}

	/**
	 * Compares Value Object for equality by their fields.
	 * 
	 * @param otherVo
	 *          Value Object to compare to.
	 * @return <code>boolean</code>- if Value Object are equal.
	 */
	public boolean equals(Object otherVo) {
		//In case other VO is null, or of other class.
		if (otherVo == null || (!this.getClass().equals(otherVo.getClass()))) {
			return false;
		}

		//Otherwise compare all fields
		boolean result = true;
		List voFields = beanMapper.getFields();
		for (Iterator i = voFields.iterator(); i.hasNext() && result;) {
			String field = (String) i.next();
			result = valuesEqual(beanMapper.getFieldValue(this, field), beanMapper.getFieldValue(otherVo, field));
		}
		return result;
	}

	/**
	 * Implements hash using Value Object fields.
	 */
	public int hashCode() {
		int result = 17;
		List voFields = beanMapper.getFields();
		for (Iterator i = voFields.iterator(); i.hasNext();) {
			String field = (String) i.next();
			result = 37 * result + beanMapper.getFieldValue(this, field).hashCode();
		}
		return result;
	}

	/**
	 * Overrides default <code>clone()</code> operation.
	 * <P/>
	 * 
	 * @return clone of this object.
	 */
	public Object clone() {
		try {
			return super.clone();
		}
		catch (CloneNotSupportedException e) {
			log.warn("BaseVO threw CloneNotSupportedException, check that everything is defined Cloneable");
			return null;
		}
	}

	//*******************************************************************
	// LEGACY PUBLIC METHODS
	//*******************************************************************

	/**
	 * Getter for property changes.
	 * <P/>
	 * 
	 * @return Value of property changes.
	 */
	public Set getChanges() {
		return changes;
	}

	/**
	 * Marks field of the value object as changed eg this field was changed after loading it from the database.
	 * <P/>
	 * Useful when value objects are used for insert/update procedures written in PL/SQL instead of EJB methods.
	 * <P/>
	 * 
	 * @param name
	 *          name of the VO-s field which is being changed.
	 * @return <code>true</code> if such field does exist and was marked as chaned, <code>false</code> otherwise.
	 */
	public boolean addChange(String name) {
		log.debug("Adding changed field = " + name);
		try {
			this.getClass().getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1), (Class[])null);
			changes.add(name);
			return true;
		}
		catch (NoSuchMethodException ex) {
			log.debug(ex);
			return false;
		}
	}

	/**
	 * Changes all fields on this <i>value object</i> as changed.
	 */
	public void changeAll() {
		List voFields = beanMapper.getFields();
		for (Iterator i = voFields.iterator(); i.hasNext();) {
			String field = (String) i.next();
			addChange(field);
		}
	}

	/**
	 * Marks all fields as unchanged.
	 */
	public void clearChanges() {
		changes = new HashSet();
	}

	//*******************************************************************
	// PRIVATE HELPER METHODS
	//*******************************************************************

	/**
	 * Checks for object equality.
	 */
	private boolean valuesEqual(Object value1, Object value2) {
		return (value1 == null) ? value2 == null : value1.equals(value2);
	}
}
