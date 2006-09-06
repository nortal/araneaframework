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
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * This class provides methods to manipulate Bean fields.
 * 
 * Simple (e.g. 'name') as well as nested (e.g. 'location.city') Bean fields
 * are both supported.
 * 
 * To propagate an empty Bean by nested fields,
 * {@link #fillFieldValue(Object, String, Object)} method is recommended
 * instead of {@link #setFieldValue(Object, String, Object)} to create
 * missing Beans automatically.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 * 
 * @see BeanMapper
 */
public class BeanUtil {
	
    /**
     * The delimiter that separates the components of a nested reference.
     */
    public static final char NESTED_DELIM = '.';
    
	/**
	 * Returns <code>List&lt;String&gt;</code>- the <code>List</code> of Bean
	 * field names.
	 * <p>        
	 * Only simple fields (not nested) are returned.
	 * </p>
	 * 
	 * @param beanClass
	 *         the class implementing the Bean pattern.
	 * @return <code>List&lt;String&gt;</code>- the <code>List</code> of Bean
	 *         field names.
	 */
	public static List getFields(Class beanClass) {
    	Validate.notNull(beanClass, "No bean class specified");
		
		List result = new ArrayList();
		
		Method[] methods = beanClass.getMethods();
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
	 * Returns the value of Bean field identified with name <code>field</code>
	 * for object <code>bean</code>.
	 * <p>
	 * Returns null if no bean specified or such method found.
	 * </p>
	 * <p>
	 * Only simple fields are supported.
	 * Use {@link #getFieldValue(Object, String)} for nested fields.
	 * </p>
	 * 
	 * @param bean
	 *          Object, which value to return.
	 * @param field
	 *          The name of VO field.
	 * @return The value of the field.
	 */
	private static Object getSimpleFieldValue(Object bean, String field) {
    	Validate.notNull(field, "No field name specified");
		if (bean == null) {
			return null;
		}
		
		Object result = null;
		try {
			Method getter = getSimpleReadMethod(bean.getClass(), field);
			if (getter != null) {
				result = getter.invoke(bean, null);
			}
		}
		catch (InvocationTargetException e) {
			throw new NestableRuntimeException("There was a problem getting field '" + field + "' value", e);
		}
		catch (IllegalAccessException e) {
			throw new NestableRuntimeException("There was a problem getting field '" + field + "' value", e);
		}
		return result;
	}
	
	/**
	 * Returns the value of Bean field identified with name <code>field</code>
	 * for object <code>bean</code>.
	 * <p>
	 * Returns null if no bean specified or such method found.
	 * </p>
	 * 
	 * @param bean
	 *          Object, which value to return.
	 * @param field
	 *          The name of VO field.
	 * @return The value of the field.
	 */
	public static Object getFieldValue(Object bean, String field) {
    	Validate.notNull(field, "No field name specified");
		if (bean == null) {
			return null;
		}
		
		String[] fields = StringUtils.split(field, NESTED_DELIM);
		Object subValue = bean;
		for (int i = 0; i < fields.length && subValue != null; i++) {
			subValue = getSimpleFieldValue(subValue, fields[i]);
		}
		return subValue;
	}

	/**
	 * Sets the value of Bean field identified by name <code>field</code> for
	 * object <code>bean</code>.
	 * <p>
	 * Nothing happens if no bean specified, one of its sub-field is null or
	 * no such method found.
	 * </p>
	 * <p>
	 * Only simple fields are supported.
	 * Use {@link #setFieldValue(Object, String, Object)} for nested fields.
	 * </p> 
	 * 
	 * @param bean
	 *          bean Object, which value to set.
	 * @param field
	 *          The name of Bean field.
	 * @param value
	 *          The new value of the field.
	 *          
	 * @see #fillFieldValue(Object, String, Object)
	 */
	private static void setSimpleFieldValue(Object bean, String field, Object value) {
    	Validate.notNull(field, "No field name specified");
		if (bean == null) {
			return;
		}
		
		try {
			Method setter = getSimpleWriteMethod(bean.getClass(), field);
			if (setter != null) {
				setter.invoke(bean, new Object[] { value });
			}
		}
		catch (InvocationTargetException e) {
			throw new NestableRuntimeException("There was a problem setting field '" + field + "' to value " + value, e);
		}
		catch (IllegalAccessException e) {
			throw new NestableRuntimeException("There was a problem setting field '" + field + "' to value " + value, e);
		}
	}
	
	/**
	 * Sets the value of Bean field identified by name <code>field</code> for
	 * object <code>bean</code>.
	 * <p>
	 * Nothing happens if no bean specified, one of its sub-field is null or
	 * no such method found.
	 * </p>
	 * <p>
	 * If one of the sub-fields (not the last one) is null, they are not
	 * automatically propagated. In order for this, use
	 * {@link #fillFieldValue(Object, String, Object)} method.
	 * </p>
	 * 
	 * @param bean
	 *          bean Object, which value to set.
	 * @param field
	 *          The name of Bean field.
	 * @param value
	 *          The new value of the field.
	 *          
	 * @see #fillFieldValue(Object, String, Object)
	 */
	public static void setFieldValue(Object bean, String field, Object value) {
    	Validate.notNull(field, "No field name specified");
		if (bean == null) {
			return;
		}
		
		String[] fields = StringUtils.split(field, NESTED_DELIM);
		Object subBean = bean;
		for (int i = 0; i < fields.length - 1 && subBean != null; i++) {
			subBean = getSimpleFieldValue(subBean, fields[i]);
		}
		if (subBean != null) {
			setSimpleFieldValue(subBean, fields[fields.length-1], value);
		}
	}
	
	/**
	 * Sets the value of Bean field identified by name <code>field</code> for
	 * object <code>bean</code>.
	 * <p>
	 * Nothing happens if no bean specified or such method found.
	 * </p>
	 * This method is identical to
	 * {@link #setFieldValue(Object, String, Object)} except that mssing beans
	 * in sub-fields (not the last one) of bean Object are created
	 * automatically.
	 * 
	 * @param bean
	 *          bean Object, which value to set.
	 * @param field
	 *          The name of Bean field.
	 * @param value
	 *          The new value of the field.
	 *          
	 * @see #setFieldValue(Object, String, Object)
	 */	
	public static void fillFieldValue(Object bean, String field, Object value) {
    	Validate.notNull(field, "No field name specified");
		if (bean == null) {
			return;
		}
		
		String[] fields = StringUtils.split(field, NESTED_DELIM);
		Object subBean = bean;
		for (int i = 0; i < fields.length - 1 && subBean != null; i++) {
			Object tmp = getSimpleFieldValue(subBean, fields[i]);
			if (tmp == null) {
				tmp = newInstance(getSimpleFieldType(subBean.getClass(), fields[i]));
				setSimpleFieldValue(subBean, fields[i], tmp);
			}
			subBean = tmp;
		}
		setSimpleFieldValue(subBean, fields[fields.length-1], value);
	}
    
	/**
	 * Returns type of Bean field identified by name <code>field</code>.
	 * <p>
	 * Null is returned if no such method found.
	 * </p>
	 * <p>
	 * Only simple fields are supported.
	 * Use {@link #getFieldType(Class, String)} for nested fields.
	 * </p>
	 *  
	 * @param beanClass
	 *         the class implementing the Bean pattern.
	 * @param field
	 *          The name of Bean field.
	 * @return The type of the field.
	 */
	private static Class getSimpleFieldType(Class beanClass, String field) {
    	Validate.notNull(beanClass, "No bean class specified");
    	Validate.notNull(field, "No field name specified");
		
		Class result = null;
		Method getter = getSimpleReadMethod(beanClass, field);
		if (getter != null) {
			result = getter.getReturnType();
		}
		return result;
	}
	
	/**
	 * Returns type of Bean field identified by name <code>field</code>.
	 * <p>
	 * Null is returned if no such method found.
	 * </p>
	 * 
	 * @param beanClass
	 *         the class implementing the Bean pattern.
	 * @param field
	 *          The name of Bean field.
	 * @return The type of the field.
	 */
	public static Class getFieldType(Class beanClass, String field) {
    	Validate.notNull(beanClass, "No bean class specified");
    	Validate.notNull(field, "No field name specified");
		
		String[] fields = StringUtils.split(field, NESTED_DELIM);
		Class subBeanType = beanClass;
		for (int i = 0; i < fields.length && subBeanType != null; i++) {
			subBeanType = getSimpleFieldType(subBeanType, fields[i]);
		}
		return subBeanType;
	}
	
	/**
	 * Checks that the field identified by <code>field</code> is a valid
	 * Bean field (can be read-only).
	 * <p>
	 * To enable reading the field, the spcfified <code>beanClass</code> must
	 * have getter (field's name starts with <code>get</code> or
	 * <code>is</code>) for this field.
	 * </p>
	 * 
	 * @param beanClass
	 *         the class implementing the Bean pattern.
	 * @param field
	 *          Bean field name.
	 * @return if this field is in Bean.
	 */	
	public static boolean isReadable(Class beanClass, String field) {
		return (getReadMethod(beanClass, field) != null);
	}
	
	/**
	 * Checks that the field identified by <code>field</code> is a writable
	 * Bean field.
	 * <p>
	 * To enable writing the field, the spcfified <code>beanClass</code> must
	 * have setter (field's name starts with <code>set</code>) for this field.
	 * </p>
	 * 
	 * @param beanClass
	 *         the class implementing the Bean pattern.
	 * @param field
	 *          Bean field name.
	 * @return if this field is in Bean.
	 */
	public static boolean isWritable(Class beanClass, String field) {
		return (getWriteMethod(beanClass, field) != null);
	}

	/**
	 * Returns write method (setter) for the field.
	 * <p>
	 * Null is returned if no such method found.
	 * </p>
	 * <p>
	 * Only simple fields are supported.
	 * Use {@link #getWriteMethod(Class, String)} for nested fields.
	 * </p> 
	 * 
	 * @param beanClass
	 *         the class implementing the Bean pattern.
	 * @param field
	 *          Bean field name.
	 * @return write method (setter) for the field.
	 */	
	private static Method getSimpleWriteMethod(Class beanClass, String field) {
    	Validate.notNull(beanClass, "No bean class specified");
    	Validate.notNull(field, "No field name specified");
		
		String setterName = "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
		try {
			return beanClass.getMethod(setterName, new Class[] { getSimpleFieldType(beanClass, field)});
		}
		catch (NoSuchMethodException e) {
			// There is no 'set' method for this field
		}
		
		return null;
	}
	
	/**
	 * Returns write method (setter) for the field.
	 * <p>
	 * Null is returned if no such method found.
	 * </p>
	 * 
	 * @param beanClass
	 *         the class implementing the Bean pattern.
	 * @param field
	 *          Bean field name.
	 * @return write method (setter) for the field.
	 */
	public static Method getWriteMethod(Class beanClass, String field) {
    	Validate.notNull(beanClass, "No bean class specified");
    	Validate.notNull(field, "No field name specified");
		
		String[] fields = StringUtils.split(field, NESTED_DELIM);
		Class subBeanType = beanClass;
		for (int i = 0; i < fields.length - 1 && subBeanType != null; i++) {
			subBeanType = getSimpleFieldType(subBeanType, fields[i]);
		}
		if (subBeanType != null) {
			return getSimpleWriteMethod(subBeanType, fields[fields.length-1]);
		}
		return null;
	}	
	
	/**
	 * Returns read method (getter) for the field.
	 * <p>
	 * Null is returned if no such method found.
	 * </p>
	 * <p>
	 * Only simple fields are supported.
	 * Use {@link #getWriteMethod(Class, String)} for nested fields.
	 * </p>
	 *  
	 * @param beanClass
	 *         the class implementing the Bean pattern.
	 * @param field
	 *          Bean field name.
	 * @return read method (getter) for the field.
	 */
	private static Method getSimpleReadMethod(Class beanClass, String field) {
    	Validate.notNull(beanClass, "No bean class specified");
    	Validate.notNull(field, "No field name specified");
		
		String getterName = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
		try {
			return beanClass.getMethod(getterName, null);
		}
		catch (NoSuchMethodException e) {
			// There is no 'get' method for this field
		}
		
		getterName = "is" + field.substring(0, 1).toUpperCase() + field.substring(1);
		try {
			return beanClass.getMethod(getterName, null);
		}
		catch (NoSuchMethodException e) {
			// There is no 'is' method for this field
		}
		
		return null;
	}
	
	/**
	 * Returns read method (getter) for the field.
	 * <p>
	 * Null is returned if no such method found.
	 * </p>
	 * 
	 * @param beanClass
	 *         the class implementing the Bean pattern.
	 * @param field
	 *          Bean field name.
	 * @return read method (getter) for the field.
	 */
	public static Method getReadMethod(Class beanClass, String field) {
    	Validate.notNull(beanClass, "No bean class specified");
    	Validate.notNull(field, "No field name specified");
		
		String[] fields = StringUtils.split(field, NESTED_DELIM);
		Class subBeanType = beanClass;
		for (int i = 0; i < fields.length - 1 && subBeanType != null; i++) {
			subBeanType = getSimpleFieldType(subBeanType, fields[i]);
		}
		if (subBeanType != null) {
			return getSimpleReadMethod(subBeanType, fields[fields.length-1]);
		}
		return null;
	}
	
	/**
	 * Creates new instance of the specified <code>beanClass</code>.
	 * <p>
	 * In order to be Bean type, it must have a constructor without arguments. 
	 * <p>
	 * <p>
	 * If creating the new instance fails, a RuntimeException is thrown. 
	 * </p>
	 * 
	 * @param beanClass
	 *         the class implementing the Bean pattern.
	 * @return new instance of the Bean type.
	 */
	public static Object newInstance(Class beanClass) {
    	Validate.notNull(beanClass, "No bean class specified");

		Object result;
		try {
			result = beanClass.newInstance();
		}
		catch (InstantiationException e) {
			throw new NestableRuntimeException("Could not create an instance of class '" + beanClass + "'", e);
		}
		catch (IllegalAccessException e) {
			throw new NestableRuntimeException("Could not create an instance of class '" + beanClass + "'", e);
		}
		return result;
	}
	
	/**
	 * Sets all the fields with same names to same values.
	 * <p> 
	 * NB! the values are references (there'is no deep copy made)!
	 * </p>
	 * <p>
	 * <code>from</code> Bean fields that are not supported by <code>to</code>
	 * are ignored. 
	 * </p>
	 * 
	 * @param from
	 *          <code>Bean</code> from which to convert.
	 * @param to
	 *          <code>Bean</code> to which to convert.
	 * @return <code>to</code> with <codefrom</code> values
	 * 
	 * @see #copy(Object, Class)
	 */
	public static Object copy(Object from, Object to) {
    	Validate.isTrue(from != null && to != null, "You cannot convert a Bean to null or vice versa");		
		
		List fromVoFields = getFields(from.getClass());
		for (Iterator i = fromVoFields.iterator(); i.hasNext();) {
			String field = (String) i.next();
			Class toFieldType = getSimpleFieldType(to.getClass(), field);
			Class fromFieldType = getSimpleFieldType(from.getClass(), field);
			if (isWritable(to.getClass(), field) && toFieldType.isAssignableFrom(fromFieldType)) {
				setSimpleFieldValue(to, field, getSimpleFieldValue(from, field));
			}
		}
		return to;
	}
	
	/**
	 * Creates a new instance of Class <code>toType</code> and sets its field values to be
	 * the same as given <code>from</code> Object. Only fields with same names that exist in
	 * both <code>from</code> object and <code>toType</code> class are affected.
	 * 
	 * @param from
	 *          <code>Bean</code> from which to read field values.
	 * @param toType
	 *          <code>Class</code> which object instance to create.
	 * @return new instance of <code>toType</code> with <code>from</code> values
	 * 
	 * @see #copy(Object, Object)
	 * @see #clone() 
	 */
	public static Object copy(Object from, Class toType) {
    	Validate.isTrue(from != null && from != null, "You cannot convert a Bean to null or vice versa");		
		return copy(from, newInstance(toType));
	}
	
	/**
	 * Clones <code>bean</code> by copying its fields values (references) to a
	 * new instance of the same type.
	 * 
	 * @param bean
	 *          bean Object, which value to set.
	 * @return new instance of <code>bean</code> type with same fields values
	 * (references)
	 * 
	 * @see #copy(Object, Object)
	 * @see #copy(Object, Class)
	 */
	public static Object clone(Object bean) {
    	Validate.notNull(bean, "No bean specified");		
		return copy(bean, bean.getClass());
	}

	/**
	 * Returns whether the given object type is a Bean type.
	 * 
	 * @param clazz
	 *         the class.
	 * @return whether the given object type is a Bean type.
	 */
	public static boolean isBean(Class clazz) {
		return (getFields(clazz).size() != 0);
	}
}
