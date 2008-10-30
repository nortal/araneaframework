/*
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
 */

package org.araneaframework.backend.util;

import java.io.Serializable;
import java.util.List;

/**
 * This class provides a way to manipulate Bean fields. This class assumes that
 * the class passed to constructor (<code>BeanClass</code>) implements the
 * Bean pattern - that is to open it's fields using getters and setters
 * (read-only fields are permitted). The only names permitted are those starting
 * with "get", "is" and "set". Another requirement is that Beans must have a
 * constructor that doesn't take any parameters.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 * @see BeanUtil
 */
public class BeanMapper implements Serializable {

  private static final long serialVersionUID = 1L;

  // *******************************************************************
  // FIELDS
  // *******************************************************************

  /**
   * Holds the Bean <code>Class</code>.
   */
  private Class beanClass;

  /**
   * Whether to create missing beans during writing bean subfields.
   */
  private boolean createMissingBeans = false;

  // *********************************************************************
  // * PUBLIC METHODS
  // *********************************************************************

  /**
   * Initializes the BeanMapper.
   * 
   * @param beanClass the class implementing the Bean pattern.
   */
  public BeanMapper(Class beanClass) {
    this.beanClass = beanClass;
  }

  /**
   * Initializes the BeanMapper.
   * 
   * @param beanClass the class implementing the Bean pattern.
   * @param createMissingBeans whetther to create missing beans during writing
   *            bean subfields (default is false).
   */
  public BeanMapper(Class beanClass, boolean createMissingBeans) {
    this(beanClass);
    this.createMissingBeans = createMissingBeans;
  }

  /**
   * Returns <code>List&lt;String&gt;</code>- the <code>List</code> of Bean
   * field names.
   * 
   * @return <code>List&lt;String&gt;</code>- the <code>List</code> of Bean
   *         field names.
   */
  public List getFields() {
    return BeanUtil.getFields(beanClass);
  }

  /**
   * Returns the value of Bean field identified with name <code>field</code>
   * for object <code>bean</code>
   * 
   * @param bean Object, which value to return.
   * @param fieldName The name of Bean field.
   * @return The value of the field.
   */
  public Object getFieldValue(Object bean, String fieldName) {
    return BeanUtil.getFieldValue(bean, fieldName);
  }

  /**
   * Sets the value of Bean field identified by name <code>field</code> for
   * object <code>bean</code>.
   * 
   * @param bean bean Object, which value to set.
   * @param fieldName The name of Bean field.
   * @param value The new value of the field.
   */
  public void setFieldValue(Object bean, String fieldName, Object value) {
    if (createMissingBeans) {
      BeanUtil.fillFieldValue(bean, fieldName, value);
    } else {
      BeanUtil.setFieldValue(bean, fieldName, value);
    }
  }

  /**
   * Returns type of Bean field identified by name <code>field</code>.
   * 
   * @param fieldName The name of Bean field.
   * @return The type of the field.
   */
  public Class getFieldType(String fieldName) {
    return BeanUtil.getFieldType(beanClass, fieldName);
  }

  /**
   * Checks that the field identified by <code>fieldName</code> is a readable
   * Bean field.
   * 
   * @param fieldName Bean field name.
   * @return if this field is in Bean.
   */
  public boolean isReadable(String fieldName) {
    return BeanUtil.isReadable(beanClass, fieldName);
  }

  /**
   * Checks that the field identified by <code>fieldName</code> is a writable
   * Bean field.
   * 
   * @param fieldName Bean field name.
   * @return if this field is in Bean.
   */
  public boolean isWritable(String fieldName) {
    return BeanUtil.isWritable(beanClass, fieldName);
  }
}
