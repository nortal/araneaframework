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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public class NestedBeanUtil {
	
    /**
     * The delimiter that separates the components of a nested reference.
     */
    public static final char NESTED_DELIM = '.';
    
    public static List getFields(Object bean) {
		if (bean == null) {
			throw new RuntimeException("No bean specified (use bean type as argument)");
		}
    	return getFields(bean.getClass());
    }
	
	public static List getFields(Class beanType) {
		if (beanType == null) {
			throw new RuntimeException("No bean class specified");
		}
		
		List result = new ArrayList();
		
		Method[] methods = beanType.getMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			// Checking that method may be a valid getter method
			if (Modifier.isPublic(method.getModifiers())
					&& (method.getParameterTypes().length == 0)
					&& !(method.getReturnType().isAssignableFrom(Void.class))) {
				if (method.getName().startsWith("get") && !"getClass".equals(method.getName())) {
					//Adding the field...
					result.add(method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4));
				}
				else if (method.getName().startsWith("is") && (Boolean.class.equals(method.getReturnType()) || boolean.class.equals(method.getReturnType()))) {
					//Adding the field...
					result.add(method.getName().substring(2, 3).toLowerCase() + method.getName().substring(3));					
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Returns null if no bean specified or such method found.
	 */
	private static Object getSimpleFieldValue(Object bean, String fieldName) {
		if (bean == null) {
			return null;
		}
		if (fieldName == null) {
			throw new RuntimeException("No field name specified");
		}

		Object result = null;
		try {
			Method getter = getSimpleReadMethod(bean.getClass(), fieldName);
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
	 * Returns null if no bean specified or such method found.
	 */
	public static Object getFieldValue(Object bean, String fieldName) {
		if (bean == null) {
			return null;
		}
		if (fieldName == null) {
			throw new RuntimeException("No field name specified");
		}
		
		String[] fieldNames = StringUtils.split(fieldName, NESTED_DELIM);
		Object subValue = bean;
		for (int i = 0; i < fieldNames.length && subValue != null; i++) {
			subValue = getSimpleFieldValue(subValue, fieldNames[i]);
		}
		return subValue;
	}	

	/**
	 * Nothing will happen if no bean specified or such method found.
	 */
	private static void setSimpleFieldValue(Object bean, String fieldName, Object value) {
		if (bean == null) {
			return;
		}
		if (fieldName == null) {
			throw new RuntimeException("No field name specified");
		}
		
		try {
			Method setter = getSimpleWriteMethod(bean.getClass(), fieldName);
			if (setter != null) {
				setter.invoke(bean, new Object[] { value });
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
	 * Nothing will happen if no bean specified or such method found.
	 */
	public static void setFieldValue(Object bean, String fieldName, Object value) {
		if (bean == null) {
			return;
		}
		if (fieldName == null) {
			throw new RuntimeException("No field name specified");
		}
		
		String[] fieldNames = StringUtils.split(fieldName, NESTED_DELIM);
		Object subBean = bean;
		for (int i = 0; i < fieldNames.length - 1 && subBean != null; i++) {
			subBean = getSimpleFieldValue(subBean, fieldNames[i]);
		}
		if (subBean != null) {
			setSimpleFieldValue(subBean, fieldNames[fieldNames.length-1], value);
		}
	}
	
	/**
	 * Missing beans in sub-fields are created automatically.
	 * 
	 * Nothing will happen if no bean specified or such method found.
	 */	
	public static void fillFieldValue(Object bean, String fieldName, Object value) {
		if (bean == null) {
			return;
		}
		if (fieldName == null) {
			throw new RuntimeException("No field name specified");
		}
		
		String[] fieldNames = StringUtils.split(fieldName, NESTED_DELIM);
		Object subBean = bean;
		for (int i = 0; i < fieldNames.length - 1 && subBean != null; i++) {
			Object tmp = getSimpleFieldValue(subBean, fieldNames[i]);
			if (tmp == null) {
				tmp = newInstance(getSimpleFieldType(subBean.getClass(), fieldNames[i]));
				setSimpleFieldValue(subBean, fieldNames[i], tmp);
				subBean = tmp;
			}
		}
		setSimpleFieldValue(subBean, fieldNames[fieldNames.length-1], value);
	}
    
	/**
	 * Null is returned if no such method found.
	 */
	private static Class getSimpleFieldType(Class beanType, String fieldName) {
		if (beanType == null) {
			throw new RuntimeException("No bean class specified");
		}
		if (fieldName == null) {
			throw new RuntimeException("No field name specified");
		}
		
		Class result = null;
		Method getter = getSimpleReadMethod(beanType, fieldName);
		if (getter != null) {
			result = getter.getReturnType();
		}
		return result;
	}	

    public static Class getFieldType(Object bean, String fieldName) {
		if (bean == null) {
			throw new RuntimeException("No bean specified (use bean type as argument)");
		}
    	return getFieldType(bean.getClass(), fieldName);
    }
	
	/**
	 * Null is returned if no such method found.
	 */
	public static Class getFieldType(Class beanType, String fieldName) {
		if (beanType == null) {
			throw new RuntimeException("No bean class specified");
		}
		if (fieldName == null) {
			throw new RuntimeException("No field name specified");
		}		
		
		String[] fieldNames = StringUtils.split(fieldName, NESTED_DELIM);
		Class subBeanType = beanType;
		for (int i = 0; i < fieldNames.length && subBeanType != null; i++) {
			subBeanType = getSimpleFieldType(subBeanType, fieldNames[i]);
		}
		return subBeanType;
	}
	
    public static boolean isReadable(Object bean, String fieldName) {
		if (bean == null) {
			throw new RuntimeException("No bean specified (use bean type as argument)");
		}
    	return isReadable(bean.getClass(), fieldName);
    }	
	
	public static boolean isReadable(Class beanType, String fieldName) {
		return (getReadMethod(beanType, fieldName) != null);
	}
	
    public static boolean isWritable(Object bean, String fieldName) {
		if (bean == null) {
			throw new RuntimeException("No bean specified (use bean type as argument)");
		}
    	return isWritable(bean.getClass(), fieldName);
    }	
	
	public static boolean isWritable(Class beanType, String fieldName) {
		return (getWriteMethod(beanType, fieldName) != null);
	}

	/**
	 * Null is returned if no such method found.
	 */
	private static Method getSimpleWriteMethod(Class beanType, String fieldName) {
		if (beanType == null) {
			throw new RuntimeException("No bean class specified");
		}
		if (fieldName == null) {
			throw new RuntimeException("No field name specified");
		}
		
		String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		try {
			return beanType.getMethod(setterName, new Class[] { getSimpleFieldType(beanType, fieldName)});
		}
		catch (NoSuchMethodException e) {
			// There is not 'set' method for this field
		}
		
		return null;
	}
	
    public static Method getWriteMethod(Object bean, String fieldName) {
		if (bean == null) {
			throw new RuntimeException("No bean specified (use bean type as argument)");
		}
    	return getWriteMethod(bean.getClass(), fieldName);
    }	
	
	/**
	 * Null is returned if no such method found.
	 */
	public static Method getWriteMethod(Class beanType, String fieldName) {
		if (beanType == null) {
			throw new RuntimeException("No bean class specified");
		}
		if (fieldName == null) {
			throw new RuntimeException("No field name specified");
		}
		
		String[] fieldNames = StringUtils.split(fieldName, NESTED_DELIM);
		Class subBeanType = beanType;
		for (int i = 0; i < fieldNames.length - 1 && subBeanType != null; i++) {
			subBeanType = getSimpleFieldType(subBeanType, fieldNames[i]);
		}
		if (subBeanType != null) {
			return getSimpleWriteMethod(subBeanType, fieldNames[fieldNames.length-1]);
		}
		return null;
	}	
	
	/**
	 * Null is returned if no such method found.
	 */
	private static Method getSimpleReadMethod(Class beanType, String fieldName) {
		if (beanType == null) {
			throw new RuntimeException("No bean class specified");
		}
		if (fieldName == null) {
			throw new RuntimeException("No field name specified");
		}	
		
		String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		try {
			return beanType.getMethod(getterName, null);
		}
		catch (NoSuchMethodException e) {
			// There is not 'get' method for this field
		}
		
		getterName = "is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		try {
			return beanType.getMethod(getterName, null);
		}
		catch (NoSuchMethodException e) {
			// There is not 'is' method for this field
		}
		
		return null;
	}
	
    public static Method getReadMethod(Object bean, String fieldName) {
		if (bean == null) {
			throw new RuntimeException("No bean specified (use bean type as argument)");
		}
    	return getReadMethod(bean.getClass(), fieldName);
    }
	
	/**
	 * Null is returned if no such method found.
	 */
	public static Method getReadMethod(Class beanType, String fieldName) {
		if (beanType == null) {
			throw new RuntimeException("No bean class specified");
		}
		if (fieldName == null) {
			throw new RuntimeException("No field name specified");
		}
		
		String[] fieldNames = StringUtils.split(fieldName, NESTED_DELIM);
		Class subBeanType = beanType;
		for (int i = 0; i < fieldNames.length - 1 && subBeanType != null; i++) {
			subBeanType = getSimpleFieldType(subBeanType, fieldNames[i]);
		}
		if (subBeanType != null) {
			return getSimpleReadMethod(subBeanType, fieldNames[fieldNames.length-1]);
		}
		return null;
	}
	
    public static Object newInstance(Object bean) {
		if (bean == null) {
			throw new RuntimeException("No bean specified (use bean type as argument)");
		}
    	return newInstance(bean.getClass());
    }	
	
	public static Object newInstance(Class beanType) {
		if (beanType == null) {
			throw new RuntimeException("No bean class specified");
		}

		Object result;
		try {
			result = beanType.newInstance();
		}
		catch (InstantiationException e) {
			throw new NestableRuntimeException("Could not create an instance of class '" + beanType + "'", e);
		}
		catch (IllegalAccessException e) {
			throw new NestableRuntimeException("Could not create an instance of class '" + beanType + "'", e);
		}
		return result;
	}
	
	public static void copy(Object to, Object from) {
		if (from == null || to == null) {
			throw new NullPointerException("You cannot convert a Bean to null or vice versa");
		}
		
		List fromVoFields = getFields(from.getClass());
		for (Iterator i = fromVoFields.iterator(); i.hasNext();) {
			String field = (String) i.next();
			Class toFieldType = getSimpleFieldType(to.getClass(), field);
			Class fromFieldType = getSimpleFieldType(from.getClass(), field);
			if (isWritable(to.getClass(), field) && toFieldType.isAssignableFrom(fromFieldType)) {
				setSimpleFieldValue(to, field, getSimpleFieldValue(from, field));
			}
		}
	}
	
	public static Object copy(Class toType, Object from) {
		if (from == null || toType == null) {
			throw new NullPointerException("You cannot convert a Bean to null or vice versa");
		}
		Object result = newInstance(toType.getClass());
		copy(result, from);
		return result;
	}
	
	public static Object clone(Object bean) {
		if (bean == null) {
			throw new RuntimeException("No bean specified");
		}
		return copy(bean.getClass(), bean);
	}
	
    public static boolean isBean(Object bean) {
		if (bean == null) {
			throw new RuntimeException("No bean specified (use bean type as argument)");
		}
    	return isBean(bean.getClass());
    }		

	public static boolean isBean(Class beanType) {
		return (getFields(beanType).size() != 0);
	}
}
