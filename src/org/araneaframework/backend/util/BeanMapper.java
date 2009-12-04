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

import org.araneaframework.core.Assert;

import java.io.Serializable;
import java.util.List;

/**
 * This class provides a way to manipulate Bean properties. This class assumes that the class passed to constructor (
 * <code>BeanClass</code>) implements the Bean pattern - that is to open it's properties using getters and setters
 * (read-only properties are permitted). The only names permitted are those starting with "get", "is" and "set". Another
 * requirement is that beans must have a constructor that doesn't take any parameters.
 * 
 * @author Rein Raudjärv (rein@araneaframework.org)
 * @author Martti Tamm (martti@araneaframework.org)
 * @see BeanUtil
 */
public class BeanMapper<T> implements Serializable {

  /**
   * Holds the bean <code>Class</code>.
   */
  private Class<T> beanClass;

  /**
   * Optionally, holds the bean that is manipulated.
   * 
   * @since 2.0
   */
  private T bean;

  /**
   * Whether to create missing beans during writing bean sub-properties.
   */
  private boolean createMissingBeans = false;

  /**
   * Initializes the BeanMapper.
   * 
   * @param beanClass the class implementing the Bean pattern.
   */
  public BeanMapper(Class<T> beanClass) {
    Assert.notNullParam(this, beanClass, "beanClass");
    this.beanClass = beanClass;
  }

  /**
   * Initializes the mapper taking a value which is used to resolve target class. The class of the value is taken.
   * 
   * @param bean The value to use for class resolving. The value must not be <code>null</code>.
   * @since 2.0
   */
  @SuppressWarnings("unchecked")
  public BeanMapper(T bean) {
    Assert.notNullParam(this, bean, "bean");
    this.beanClass = (Class<T>) bean.getClass();
    this.bean = bean;
  }

  /**
   * Initializes the BeanMapper.
   * 
   * @param beanClass the class implementing the Bean pattern.
   * @param createMissingBeans Whether to create missing beans during writing bean sub-properties (default is false).
   */
  public BeanMapper(Class<T> beanClass, boolean createMissingBeans) {
    this(beanClass);
    this.createMissingBeans = createMissingBeans;
  }

  /**
   * Initializes the BeanMapper.
   * 
   * @param bean The bean that is mapped.
   * @param createMissingBeans Whether to create missing beans during writing bean sub-properties (default is false).
   * @since 2.0
   */
  public BeanMapper(T bean, boolean createMissingBeans) {
    this(bean);
    this.createMissingBeans = createMissingBeans;
  }

  /**
   * Provides a list of names belonging to bean properties.
   * 
   * @return A list of names belonging to bean properties.
   */
  public List<String> getPropertyNames() {
    return BeanUtil.getProperties(this.beanClass);
  }

  /**
   * Provides the value of the <code>bean</code> <code>property</code>. The must be provided through the constructor.
   * 
   * @param property The name of bean property.
   * @return The value of the property.
   * @since 2.0
   */
  public Object getProperty(String property) {
    return getProperty(this.bean, property);
  }

  /**
   * Provides the value of the <code>bean</code> <code>property</code>.
   * 
   * @param bean The object for which to return the property value.
   * @param property The name of bean property.
   * @return The value of the property.
   */
  public Object getProperty(Object bean, String property) {
    return BeanUtil.getPropertyValue(bean, property);
  }

  /**
   * Sets the value of the <code>bean</code> <code>property</code> to the given value. The must be provided through the
   * constructor.
   * 
   * @param property The name of the bean property.
   * @param value The new value of the property.
   * @since 2.0
   */
  public void setProperty(String property, Object value) {
    setProperty(this.bean, property, value);
  }

  /**
   * Sets the value of the <code>bean</code> <code>property</code> to the given value.
   * 
   * @param bean The bean object for which the property must to set.
   * @param property The name of the bean property.
   * @param value The new value of the property.
   */
  public void setProperty(Object bean, String property, Object value) {
    BeanUtil.setPropertyValue(bean, property, value, this.createMissingBeans);
  }

  /**
   * Returns type of the bean property using the <code>propertyName</code>.
   * 
   * @param propertyName The name of the bean property.
   * @return The type of the bean property.
   */
  public Class<?> getPropertyType(String propertyName) {
    return BeanUtil.getPropertyType(this.beanClass, propertyName);
  }

  /**
   * Checks that the property (identified by <code>propertyName</code>) is readable.
   * 
   * @param propertyName The name of the bean property.
   * @return <code>true</code>, if the bean property is readable.
   */
  public boolean isReadable(String propertyName) {
    return BeanUtil.isReadableProperty(this.beanClass, propertyName);
  }

  /**
   * Checks that the property (identified by <code>propertyName</code>) is a writable.
   * 
   * @param propertyName The name of the bean property.
   * @return <code>true</code>, if the bean property is writable.
   */
  public boolean isWritable(String propertyName) {
    return BeanUtil.isWritableProperty(this.beanClass, propertyName);
  }
}
