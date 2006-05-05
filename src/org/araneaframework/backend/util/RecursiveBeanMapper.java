package org.araneaframework.backend.util;

import java.lang.reflect.Method;
import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * This class provides a way to manipulate Bean fields and subfields.
 * 
 * @author <a href="mailto:rein@webmedia.ee>Rein Raudjärv</a>
 * 
 * @see org.araneaframework.backend.util.BasicBeanMapper
 */
public class RecursiveBeanMapper extends BasicBeanMapper {
	
	public static final boolean IGNORE_NULLS_BY_DEFAULT = false;
	
	//*******************************************************************
	// FIELDS
	//*******************************************************************	
	
	/**
	 * Whether to ignore errors during reading and writing bean
	 *          subfields while the corresponding field of current bean has no
	 *          sub-bean instance set. If set to True, failure of reading of
	 *          subfield returns Null. In addition, during writing to subfield 
	 *          all sub-fields that corresponds to sub-beans are automatically
	 *          initialized with new instances of those beans.
	 */
	private boolean ignoreNulls = IGNORE_NULLS_BY_DEFAULT;
	
	//*********************************************************************
	//* PUBLIC METHODS
	//*********************************************************************	
	
	/**
	 * Initializes the RecursiveBeanMapper.
	 * 
	 * @param beanClass
	 *          the class implementing the Bean pattern.
	 * @param ignoreNulls
	 *          whether to ignore errors during reading and writing bean
	 *          subfields while the corresponding field of current bean has no
	 *          sub-bean instance set. If set to True, failure of reading of
	 *          subfield returns Null. In addition, during writing to subfield 
	 *          all sub-fields that corresponds to sub-beans are automatically
	 *          initialized with new instances of those beans.
	 */
	public RecursiveBeanMapper(Class beanClass, boolean ignoreNulls) {
		super(beanClass);
		this.ignoreNulls = ignoreNulls;		
	}
	
	/**
	 * Returns the value of Bean field identified with name <code>field</code>
	 * for object <code>bean</code>
	 * 
	 * @param bean
	 *          Object, which value to return.
	 * @param field
	 *          The name of VO field.
	 * @return The value of the field.
	 */
	public Object getBeanFieldValue(Object bean, String fieldName) {
		if (isRecursive(fieldName)) {
			Object subBean = super.getBeanFieldValue(bean, getThisField(fieldName));
			if (this.ignoreNulls && subBean == null) {
				return null;
			}
			return getSubBeanMapper(fieldName).getBeanFieldValue(subBean, getSubfields(fieldName));
		}
		return super.getBeanFieldValue(bean, fieldName);
	}
	
	/**
	 * Sets the value of Bean field identified by name <code>field</code> for
	 * object <code>bean</code>.
	 * 
	 * @param bean
	 *          bean Object, which value to set.
	 * @param field
	 *          The name of Bean field.
	 * @param value
	 *          The new value of the field.
	 */
	public void setBeanFieldValue(Object bean, String fieldName, Object value) {
		if (isRecursive(fieldName)) {
			Object subBean = super.getBeanFieldValue(bean, getThisField(fieldName));
			if (this.ignoreNulls && subBean == null) {
				try {
					subBean = super.getBeanFieldType(getThisField(fieldName)).newInstance();
					super.setBeanFieldValue(bean, getThisField(fieldName), subBean); 
				} catch (InstantiationException e) {
					throw new NestableRuntimeException("There was a problem setting field '" + fieldName + "' to value " + value, e);
				} catch (IllegalAccessException e) {
					throw new NestableRuntimeException("There was a problem setting field '" + fieldName + "' to value " + value, e);
				}
			}
			getSubBeanMapper(fieldName).setBeanFieldValue(subBean, getSubfields(fieldName), value);
		}
		super.setBeanFieldValue(bean, fieldName, value);
	}
	
	//*********************************************************************
	//* PRIVATE HELPER METHODS
	//*********************************************************************
	
	/**
	 * Returns getter from field name.
	 */
	protected Method getGetterMethod(String fieldName) {
		if (isRecursive(fieldName)) {
			return getSubBeanMapper(fieldName).getGetterMethod(getSubfields(fieldName));
		}
		return super.getGetterMethod(fieldName);
	}
	
	/**
	 * Returns setter from field name.
	 */
	protected Method getSetterMethod(String fieldName) {
		if (isRecursive(fieldName)) {
			return getSubBeanMapper(fieldName).getSetterMethod(getSubfields(fieldName));
		}
		return super.getSetterMethod(fieldName);
	}
	
	private BasicBeanMapper getSubBeanMapper(String fieldName) {
		return new RecursiveBeanMapper(getBeanFieldType(getThisField(fieldName)), this.ignoreNulls);
	}
	
	private static boolean isRecursive(String fieldName) {
		return fieldName.indexOf(".") != -1;
	}
	
	private static String getThisField(String fieldName) {
		return fieldName.substring(0, fieldName.indexOf("."));
	}
	
	private static String getSubfields(String fieldName) {
		return fieldName.substring(fieldName.indexOf(".") + 1);
	}
}
