package org.araneaframework.backend.util;

import java.util.List;

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
public interface GeneralBeanMapper {

	/**
	 * Returns <code>List&lt;String&gt;</code>- the <code>List</code> of VO
	 *         field names.
	 * @return <code>List&lt;String&gt;</code>- the <code>List</code> of VO
	 *         field names.
	 */
	List getBeanFields();

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
	Object getBeanFieldValue(Object vo, String fieldName);

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
	void setBeanFieldValue(Object vo, String fieldName, Object value);

	/**
	 * Returns type of VO field identified by name <code>field</code>.
	 * 
	 * @param field
	 *          The name of VO field.
	 * @return The type of the field.
	 */
	Class getBeanFieldType(String fieldName);

	/**
	 * Checks that the field identified by <code>fieldName</code> is a valid
	 * Value Object field.
	 * 
	 * @param fieldName
	 *          Value Object field name.
	 * @return if this field is in Value Object.
	 */
	boolean fieldExists(String fieldName);

	/**
	 * Checks that the field identified by <code>fieldName</code> is a writable
	 * Value Object field.
	 * 
	 * @param fieldName
	 *          Value Object field name.
	 * @return if this field is in Value Object.
	 */
	boolean fieldIsWritable(String fieldName);

}