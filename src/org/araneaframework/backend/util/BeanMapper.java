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

package org.araneaframework.backend.util;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.log4j.Logger;

/**
 * This class provides a way to manipulate Value Object fields. This class
 * assumes that the class passed to constructor (<code>voClass</code>)
 * implements the Value Object pattern - that is to open it's fields using
 * getters and setters (read-only fields are permitted). The only names
 * permitted are those starting with "get", "is" and "set". Another requirement
 * is that Value Objects must have a constructor that doesn't take any
 * parameters.
 * 
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 * @since 1.4.1.20
 */
public class BeanMapper implements Serializable {
	private static Logger log = Logger.getLogger(BeanMapper.class);
	
	//*******************************************************************
	// FIELDS
	//*******************************************************************
	
	/**
	 * Holds the Value Object <code>Class</code>.
	 */
	private Class voClass;
	
	//*********************************************************************
	//* PUBLIC METHODS
	//*********************************************************************
	
	/**
	 * Initializes the VoMapper.
	 * 
	 * @param voClass
	 *          the class implementing the Value Object pattern.
	 */
	public BeanMapper(Class voClass) {
		this.voClass = voClass;
	}
	
	/**
	 * Returns <code>List&lt;String&gt;</code>- the <code>List</code> of VO
	 *         field names.
	 * @return <code>List&lt;String&gt;</code>- the <code>List</code> of VO
	 *         field names.
	 */
	public List getBeanFields() {
		List result = new ArrayList();
		
		Method[] voMethods = voClass.getMethods();
		for (int i = 0; i < voMethods.length; i++) {
			Method voMethod = voMethods[i];
			//Checking that method may be a valid getter method
			if (Modifier.isPublic(voMethod.getModifiers()) && (voMethod.getParameterTypes().length == 0) && !(voMethod.getReturnType().isAssignableFrom(Void.class))) {
				//Checking that it's a getter method, and it has a corresponding
				// setter method.
				if (voMethod.getName().startsWith("get") && !"getClass".equals(voMethod.getName())) {
					//Adding the field...
					result.add(voMethod.getName().substring(3, 4).toLowerCase() + voMethod.getName().substring(4));
				}
				else if (voMethod.getName().startsWith("is") && (Boolean.class.equals(voMethod.getReturnType()) || boolean.class.equals(voMethod.getReturnType()))) {
					//Adding the field...
					result.add(voMethod.getName().substring(2, 3).toLowerCase() + voMethod.getName().substring(3));
					
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Returns the value of VO field identified with name <code>field</code>
	 * for object <code>vo</code>
	 * 
	 * @param vo
	 *          Object, which value to return.
	 * @param field
	 *          The name of VO field.
	 * @return The value of the field.
	 */
	public Object getBeanFieldValue(Object vo, String fieldName) {
		String mainFieldName = fieldName;
		String subFields = null;
		if (fieldName.indexOf(".") != -1) {
			mainFieldName = fieldName.substring(0, fieldName.indexOf("."));
			subFields = fieldName.substring(mainFieldName.length() + 1);
			
			Object result = null;
			try {
				Method getter = getGetterMethod(mainFieldName);
				result = getter.invoke(vo, null);
				return new BeanMapper(getBeanFieldType(mainFieldName)).getBeanFieldValue(result, subFields);
			}
			catch (InvocationTargetException e) {
				throw new NestableRuntimeException("There was a problem getting field '" + fieldName + "' value", e);
			}
			catch (IllegalAccessException e) {
				throw new NestableRuntimeException("There was a problem getting field '" + fieldName + "' value", e);
			}
		}
		
		Object result = null;
		try {
			Method getter = getGetterMethod(fieldName);
			if (getter != null) {
				result = getter.invoke(vo, null);
			}
		}
		catch (InvocationTargetException e) {
			throw new NestableRuntimeException("There was a problem getting field '" + fieldName + "' value", e);
		}
		catch (IllegalAccessException e) {
			throw new NestableRuntimeException("There was a problem getting field '" + fieldName + "' value", e);
		}
		return result;
	}
	
	/**
	 * Sets the value of VO field identified by name <code>field</code> for
	 * object <code>vo</code>.
	 * 
	 * @param vo
	 *          vo Object, which value to set.
	 * @param field
	 *          The name of VO field.
	 * @param value
	 *          The new value of the field.
	 */
	public void setBeanFieldValue(Object vo, String fieldName, Object value) {
		try {
			Method setter = getSetterMethod(fieldName);
			if (setter != null) {
				setter.invoke(vo, new Object[] { value });
			}
		}
		catch (InvocationTargetException e) {
			throw new NestableRuntimeException("There was a problem setting field '" + fieldName + "' to value " + value, e);
		}
		catch (IllegalAccessException e) {
			throw new NestableRuntimeException("There was a problem setting field '" + fieldName + "' to value " + value, e);
		}
	}
	
	/**
	 * Returns type of VO field identified by name <code>field</code>.
	 * 
	 * @param field
	 *          The name of VO field.
	 * @return The type of the field.
	 */
	public Class getBeanFieldType(String fieldName) {
		Class result = null;
		Method getter = getGetterMethod(fieldName);
		if (getter != null) {
			result = getter.getReturnType();
		}
		return result;
	}
	
	/**
	 * Checks that the field identified by <code>fieldName</code> is a valid
	 * Value Object field.
	 * 
	 * @param fieldName
	 *          Value Object field name.
	 * @return if this field is in Value Object.
	 */
	public boolean fieldExists(String fieldName) {
		return (getGetterMethod(fieldName) != null);
	}
	
	/**
	 * Checks that the field identified by <code>fieldName</code> is a writable
	 * Value Object field.
	 * 
	 * @param fieldName
	 *          Value Object field name.
	 * @return if this field is in Value Object.
	 */
	public boolean fieldIsWritable(String fieldName) {
		return (getSetterMethod(fieldName) != null);
	}
	
	//*********************************************************************
	//* PRIVATE HELPER METHODS
	//*********************************************************************
	
	/**
	 * Returns getter from field name.
	 */
	private Method getGetterMethod(String fieldName) {
		String mainFieldName = fieldName;
		String subFields = null;
		if (fieldName.indexOf(".") != -1) {
			mainFieldName = fieldName.substring(0, fieldName.indexOf("."));
			subFields = fieldName.substring(mainFieldName.length() + 1);
		}
		
		String getterName = "get" + mainFieldName.substring(0, 1).toUpperCase() + mainFieldName.substring(1);
		try {
			Method method = voClass.getMethod(getterName, null);
			if (subFields != null) {
				return new BeanMapper(method.getReturnType()).getGetterMethod(subFields);
			}
			return method;
		}
		catch (NoSuchMethodException e) {
			//There is not 'get' method for this field
		}
		
		getterName = "is" + mainFieldName.substring(0, 1).toUpperCase() + mainFieldName.substring(1);
		try {
			Method method = voClass.getMethod(getterName, null);
			if (subFields != null) {
				return new BeanMapper(method.getReturnType()).getGetterMethod(subFields);
			}
			return method;
		}
		catch (NoSuchMethodException e) {
			//There is not 'is' method for this field
		}
		
		return null;
	}
	
	/**
	 * Returns setter from field name.
	 */
	private Method getSetterMethod(String fieldName) {
		
		String mainFieldName = fieldName;
		String subFields = null;
		if (fieldName.indexOf(".") != -1) {
			mainFieldName = fieldName.substring(0, fieldName.indexOf("."));
			subFields = fieldName.substring(mainFieldName.length() + 1);
			return new BeanMapper(getBeanFieldType(mainFieldName)).getSetterMethod(subFields);
		}
		
		String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		try {
			return voClass.getMethod(setterName, new Class[] { getBeanFieldType(fieldName)});
		}
		catch (NoSuchMethodException e) {
			//There is not 'set' method for this field
		}
		
		return null;
	}
	
	/**
	 * Returns whether the given object type is a Value Object type.
	 * @param objectType object type.
	 * @return whether the given object type is a Value Object type.
	 */
	public static boolean isBean(Class objectType) {
		BeanMapper beanMapper = new BeanMapper(objectType);
		return beanMapper.getBeanFields().size() != 0;
	}
}
