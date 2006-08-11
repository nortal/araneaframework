package org.araneaframework.backend.util;

import java.lang.reflect.Method;

/**
 * This class provides a way to manipulate Bean fields and subfields.
 * 
 * @author <a href="mailto:rein@webmedia.ee">Rein Raudjärv</a>
 * 
 * @see org.araneaframework.backend.util.BaseBeanMapper
 */
public class BeanMapper extends BaseBeanMapper {
	
	//*******************************************************************
	// FIELDS
	//*******************************************************************	
	
	/**
	 * Whetther to create missing beans during writing bean subfields while the
	 * corresponding field of current bean has no sub-bean instance set. If set
	 * to True, during writing to subfield all sub-fields that corresponds to
	 * sub-beans are automaticallyinitialized with new instances of those beans.
	 * Otherwise a RuntimeException is thrown.
	 */
	private boolean createMissingBeans = false;
	
	//*********************************************************************
	//* PUBLIC METHODS
	//*********************************************************************	
	
	
	/**
	 * Initializes the BeanMapper with the given type.
	 * 
	 * @param beanClass
	 *          the class implementing the Bean pattern.
	 */
	public BeanMapper(Class beanClass) {
		super(beanClass);
	}
	
	/**
	 * Initializes the BeanMapper.
	 * 
	 * @param beanClass
	 *          the class implementing the Bean pattern.
	 * @param createMissingBeans
	 * 			whetther to create missing beans during writing bean subfields
	 * 			(default is false).
	 */
	public BeanMapper(Class beanClass, boolean createMissingBeans) {
		super(beanClass);
		this.createMissingBeans = createMissingBeans;		
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
		Object result;
		if (isRecursive(fieldName)) {
			Object subBean = super.getBeanFieldValue(bean, getThisField(fieldName));
			if (subBean == null) {
				return null;
			}
			result = getSubBeanMapper(fieldName).getBeanFieldValue(subBean, getSubfields(fieldName));
		} else {
			result = super.getBeanFieldValue(bean, fieldName);			
		}
		return result;
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
			if (this.createMissingBeans && subBean == null) {				
				subBean = BeanUtil.newInstance(super.getBeanFieldType(getThisField(fieldName)));
				super.setBeanFieldValue(bean, getThisField(fieldName), subBean); 
			}
			getSubBeanMapper(fieldName).setBeanFieldValue(subBean, getSubfields(fieldName), value);			
		} else {
			super.setBeanFieldValue(bean, fieldName, value);			
		}
	}
	
	//*********************************************************************
	//* PRIVATE HELPER METHODS
	//*********************************************************************
	
	/**
	 * Returns getter from field name.
	 */
	protected Method getGetterMethod(String fieldName) {
		Method result;
		if (isRecursive(fieldName)) {
			result = getSubBeanMapper(fieldName).getGetterMethod(getSubfields(fieldName));
		} else {
			result = super.getGetterMethod(fieldName);
		}
		return result;
	}
	
	/**
	 * Returns setter from field name.
	 */
	protected Method getSetterMethod(String fieldName) {
		Method result;
		if (isRecursive(fieldName)) {
			result = getSubBeanMapper(fieldName).getSetterMethod(getSubfields(fieldName));
		} else {
			return super.getSetterMethod(fieldName);			
		}
		return result;
	}
	
	private BaseBeanMapper getSubBeanMapper(String fieldName) {
		return new BeanMapper(getBeanFieldType(getThisField(fieldName)), this.createMissingBeans);
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
