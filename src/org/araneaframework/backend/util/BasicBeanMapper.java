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

/**
 * This class provides a way to manipulate Bean fields. This class
 * assumes that the class passed to constructor (<code>BeanClass</code>)
 * implements the Bean pattern - that is to open it's fields using
 * getters and setters (read-only fields are permitted). The only names
 * permitted are those starting with "get", "is" and "set". Another requirement
 * is that Beans must have a constructor that doesn't take any
 * parameters.
 * 
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 * @since 1.4.1.20
 */
public class BasicBeanMapper implements Serializable, GeneralBeanMapper {
	
	//*******************************************************************
	// FIELDS
	//*******************************************************************
	
	/**
	 * Holds the Bean <code>Class</code>.
	 */
	private Class beanClass;
	
	//*********************************************************************
	//* PUBLIC METHODS
	//*********************************************************************
	
	/**
	 * Initializes the BeanMapper.
	 * 
	 * @param beanClass
	 *          the class implementing the Bean pattern.
	 */
	public BasicBeanMapper(Class beanClass) {
		this.beanClass = beanClass;
	}
	
	/**
	 * Returns <code>List&lt;String&gt;</code>- the <code>List</code> of Bean
	 *         field names.
	 * @return <code>List&lt;String&gt;</code>- the <code>List</code> of Bean
	 *         field names.
	 */
	public List getBeanFields() {
		List result = new ArrayList();
		
		Method[] BeanMethods = this.beanClass.getMethods();
		for (int i = 0; i < BeanMethods.length; i++) {
			Method BeanMethod = BeanMethods[i];
			//Checking that method may be a valid getter method
			if (Modifier.isPublic(BeanMethod.getModifiers()) && (BeanMethod.getParameterTypes().length == 0) && !(BeanMethod.getReturnType().isAssignableFrom(Void.class))) {
				//Checking that it's a getter method, and it has a corresponding
				// setter method.
				if (BeanMethod.getName().startsWith("get") && !"getClass".equals(BeanMethod.getName())) {
					//Adding the field...
					result.add(BeanMethod.getName().substring(3, 4).toLowerCase() + BeanMethod.getName().substring(4));
				}
				else if (BeanMethod.getName().startsWith("is") && (Boolean.class.equals(BeanMethod.getReturnType()) || boolean.class.equals(BeanMethod.getReturnType()))) {
					//Adding the field...
					result.add(BeanMethod.getName().substring(2, 3).toLowerCase() + BeanMethod.getName().substring(3));
					
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Returns the value of Bean field identified with name <code>field</code>
	 * for object <code>Bean</code>
	 * 
	 * @param bean
	 *          Object, which value to return.
	 * @param field
	 *          The name of Bean field.
	 * @return The value of the field.
	 */
	public Object getBeanFieldValue(Object bean, String fieldName) {
		Object result = null;
		try {
			Method getter = getGetterMethod(fieldName);
			if (getter != null) {
				result = getter.invoke(bean, null);
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
	 * Sets the value of Bean field identified by name <code>field</code> for
	 * object <code>Bean</code>.
	 * 
	 * @param Bean
	 *          Bean Object, which value to set.
	 * @param field
	 *          The name of Bean field.
	 * @param value
	 *          The new value of the field.
	 */
	public void setBeanFieldValue(Object Bean, String fieldName, Object value) {
		try {
			Method setter = getSetterMethod(fieldName);
			if (setter != null) {
				setter.invoke(Bean, new Object[] { value });
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
	 * Returns type of Bean field identified by name <code>field</code>.
	 * 
	 * @param field
	 *          The name of Bean field.
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
	 * Bean field.
	 * 
	 * @param fieldName
	 *          Bean field name.
	 * @return if this field is in Bean.
	 */
	public boolean fieldExists(String fieldName) {
		return (getGetterMethod(fieldName) != null);
	}
	
	/**
	 * Checks that the field identified by <code>fieldName</code> is a writable
	 * Bean field.
	 * 
	 * @param fieldName
	 *          Bean field name.
	 * @return if this field is in Bean.
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
	protected Method getGetterMethod(String fieldName) {
		String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		try {
			return this.beanClass.getMethod(getterName, null);
		}
		catch (NoSuchMethodException e) {
			//There is not 'get' method for this field
		}
		
		getterName = "is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		try {
			return this.beanClass.getMethod(getterName, null);
		}
		catch (NoSuchMethodException e) {
			//There is not 'is' method for this field
		}
		
		return null;
	}
	
	/**
	 * Returns setter from field name.
	 */
	protected Method getSetterMethod(String fieldName) {
		String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		try {
			return this.beanClass.getMethod(setterName, new Class[] { getBeanFieldType(fieldName)});
		}
		catch (NoSuchMethodException e) {
			//There is not 'set' method for this field
		}
		
		return null;
	}
	
	/**
	 * Returns whether the given object type is a Bean type.
	 * @param objectType object type.
	 * @return whether the given object type is a Bean type.
	 */
	public static boolean isBean(Class objectType) {
		GeneralBeanMapper beanMapper = new BasicBeanMapper(objectType);
		return beanMapper.getBeanFields().size() != 0;
	}
}
