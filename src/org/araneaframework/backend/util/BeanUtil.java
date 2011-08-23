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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.araneaframework.core.exception.AraneaRuntimeException;
import org.araneaframework.core.util.Assert;
import org.araneaframework.core.util.ExceptionUtil;

/**
 * This class provides methods to manipulate Bean fields. Simple (e.g. 'name') as well as nested (e.g. 'location.city')
 * Bean fields are both supported. Empty nested properties are automatically attempted to be initiated.
 * <p>
 * When accessing field the following approach is used:
 * <ol>
 * <li>If there is public accessor in bean's class or any superclass, then use it.</li>
 * <li>If there is declared field with any access modifier in bean's class, then use it.</li>
 * </ol>
 * 
 * @author Rein Raudjärv (rein@araneaframework.org)
 * @author Nikita Salnikov-Tarnovski (nikem@webmedia.ee)
 * @author Martti Tamm (martti@araneaframework.org)
 * @see BeanMapper
 */
public class BeanUtil {

  /**
   * The prefix of a bean property getter method name.
   */
  public static final String GETTER_PREFIX = "get";

  /**
   * Another allowed prefix of a bean property getter method name.
   */
  public static final String GETTER_PREFIX2 = "is";

  /**
   * The prefix of a bean property setter method name.
   */
  public static final String SETTER_PREFIX = "set";

  /**
   * The delimiter that separates the components of a nested reference.
   */
  public static final String NESTED_DELIM = ".";

  /**
   * Provides a list of bean properties that can be read or written to by this <code>BeanUtil</code>. Only simple fields
   * (not nested) are returned.
   * 
   * @param beanClass the class implementing the Bean pattern.
   * @return <code>List&lt;String&gt;</code>- the <code>List</code> of Bean field names.
   */
  public static List<String> getProperties(Class<?> beanClass) {
    Assert.notNull(beanClass, "No bean class specified.");

    Set<String> result = new LinkedHashSet<String>();

    // Adding properties:
    for (PropertyDescriptor descriptor : PropertyUtils.getPropertyDescriptors(beanClass)) {
      result.add(descriptor.getName());
    }

    for (Field field : beanClass.getDeclaredFields()) {
      result.add(field.getName());
    }

    return new LinkedList<String>(result);
  }

  /**
   * Provides the value of <code>bean</code> <code>property</code> (identified by <code>property</code>). Returns
   * <code>null</code> if no bean specified or such method/field found.
   * 
   * @param bean The bean for which the property value must be returned.
   * @param property The name of bean property. May be nested.
   * @return The value of the bean property.
   */
  public static Object getPropertyValue(Object bean, String property) {
    assertData(bean, property);
    return getSimplePropertyValue(getLastBean(bean, property), getLastProperty(property));
  }

  /**
   * Sets the value of <code>bean</code> <code>property</code> (identified by <code>property</code>) to the given
   * <code>value</code>. If the property is a nested path and a nested property is <code>null</code>, an attempt is made
   * to initiate it.
   * 
   * @param bean Object for which the property value must be set.
   * @param property The name of bean property. May be nested.
   * @param value The value of the bean property must have.
   */
  public static void setPropertyValue(Object bean, String property, Object value) {
    setPropertyValue(getLastBean(bean, property), getLastProperty(property), value, true);
  }

  /**
   * Sets the value of <code>bean</code> <code>property</code> (identified by <code>property</code>) to the given
   * <code>value</code>. The <code>createMissingBeans</code> parameter controls whether nested <code>null</code>
   * properties should be initiated and set when traversing.
   * 
   * @param bean Object for which the property value must be set.
   * @param property The name of bean property. May be nested.
   * @param value The value of the bean property must have.
   * @param createMissingBeans A boolean indicating that, if property is nested, all null properties should be
   *          instantiated (by default: <code>true</code>).
   * @since 2.0
   */
  public static void setPropertyValue(Object bean, String property, Object value, boolean createMissingBeans) {
    assertData(bean, property);
    setSimplePropertyValue(getLastBean(bean, property), getLastProperty(property), value);
  }

  /**
   * Provides the type class of the given <code>bean</code> <code>property</code>.
   * 
   * @param beanClass Object class for which the property data type must be returned.
   * @param property The name of bean property. May be nested.
   * @return The data type of the bean property.
   */
  public static Class<?> getPropertyType(Class<?> beanClass, String property) {
    assertData(beanClass, property);
    beanClass = getLastBeanClass(beanClass, property);
    return getSimplePropertyType(beanClass, getLastProperty(property));
  }

  /**
   * Provides whether this <code>BeanUtil</code> can read the given <code>bean</code> <code>property</code>.
   * 
   * @param beanClass Bean class for which the property must be resolved.
   * @param property The name of bean property. May be nested.
   * @return Whether the bean property is readable.
   */
  public static boolean isReadableProperty(Class<?> beanClass, String property) {
    assertData(beanClass, property);

    boolean readable = false;

    while (StringUtils.isNotEmpty(property)) {
      String simpleProperty = StringUtils.substringBefore(property, NESTED_DELIM);
      readable = hasReadablePropertyMethod(beanClass, simpleProperty)
          || hasReadablePropertyField(beanClass, simpleProperty);
      if (!readable) {
        break;
      }
      beanClass = getPropertyType(beanClass, simpleProperty);
      property = StringUtils.substringAfter(property, NESTED_DELIM);
    }

    return readable;
  }

  /**
   * Provides whether this <code>BeanUtil</code> can write to the given <code>bean</code> <code>property</code>.
   * 
   * @param beanClass Object class for which the property must be resolved.
   * @param property The name of bean property. May be nested.
   * @return Whether the bean property is writable.
   */
  public static boolean isWritableProperty(Class<?> beanClass, String property) {
    assertData(beanClass, property);

    boolean writable = false;

    while (StringUtils.isNotEmpty(property)) {
      String simpleProperty = StringUtils.substringBefore(property, NESTED_DELIM);
      writable = hasWriteablePropertyMethod(beanClass, simpleProperty)
          || hasWriteablePropertyField(beanClass, simpleProperty);
      if (!writable) {
        break;
      }
      beanClass = getPropertyType(beanClass, simpleProperty);
      property = StringUtils.substringAfter(property, NESTED_DELIM);
    }

    return writable;
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
   * @param beanClass the class implementing the Bean pattern.
   * @return new instance of the Bean type.
   */
  public static <T> T newInstance(Class<T> beanClass) {
    Assert.notNullParam(beanClass, "beanClass");

    T result;
    try {
      result = beanClass.newInstance();
    } catch (InstantiationException e) {
      throw new NestableRuntimeException("Could not create an instance of class '" + beanClass + "'", e);
    } catch (IllegalAccessException e) {
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
   * <code>from</code> Bean fields that are not supported by <code>to</code> are ignored.
   * </p>
   * 
   * @param from <code>Bean</code> from which to convert.
   * @param to <code>Bean</code> to which to convert.
   * @return <code>to</code> with <codefrom</code> values
   * @see #copy(Object, Class)
   */
  public static Object copy(Object from, Object to) {
    Assert.notNull(from, "BeanUtil.copy() cannot accept NULL argument 'from'.");
    Assert.notNull(to, "BeanUtil.copy() cannot accept NULL arguments 'to'.");
    try {
      PropertyUtils.copyProperties(from, to);
    } catch (Exception e) {
      ExceptionUtil.uncheckException("Exception while copying values from one bean to another.", e);
    }
    return to;
  }

  /**
   * Creates a new instance of Class <code>toType</code> and sets its field values to be the same as given
   * <code>from</code> Object. Only fields with same names that exist in both <code>from</code> object and
   * <code>toType</code> class are affected.
   * 
   * @param from <code>Bean</code> from which to read field values.
   * @param toType <code>Class</code> which object instance to create.
   * @return new instance of <code>toType</code> with <code>from</code> values
   * @see #copy(Object, Object)
   * @see #clone()
   */
  public static Object copy(Object from, Class<?> toType) {
    Assert.isTrue(from != null && toType != null, "You cannot convert a Bean to null or vice versa");
    return copy(from, newInstance(toType));
  }

  /**
   * Clones <code>bean</code> by copying its fields values (references) to a new instance of the same type.
   * 
   * @param bean bean Object, which value to set.
   * @return new instance of <code>bean</code> type with same fields values (references)
   * @see #copy(Object, Object)
   * @see #copy(Object, Class)
   */
  public static Object clone(Object bean) {
    Assert.notNullParam(bean, "bean");
    return copy(bean, bean.getClass());
  }

  // *********************************************************
  // PRIVATE METHODS FOR MANIPULATING PROPERTIES OF ANY KIND
  // *********************************************************

  private static Object getSimplePropertyValue(Object bean, String property) {
    Object result = null;

    if (hasReadablePropertyMethod(bean.getClass(), property)) {

      try {
        result = PropertyUtils.getSimpleProperty(bean, property);
      } catch (Exception e) {
        ExceptionUtil.uncheckException("Exception while retrieving the value of [" + getTargetDesc(bean, property)
            + "] using a getter method.", e);
      }

    } else if (hasReadablePropertyField(bean.getClass(), property)) {
      result = getFieldValue(bean, property);
    }
    return result;
  }

  private static void setSimplePropertyValue(Object bean, String property, Object value) {
    if (hasWriteablePropertyMethod(bean.getClass(), property)) {

      try {
        PropertyUtils.setSimpleProperty(bean, property, value);
      } catch (Exception e) {
        ExceptionUtil.uncheckException("Exception while setting the value of [" + getTargetDesc(bean, property)
            + "] to [" + getValueDesc(value) + "] using a setter method.", e);
      }

    } else {
      setFieldValue(bean, property, value);
    }
  }

  private static Class<?> getSimplePropertyType(Class<?> beanClass, String property) {
    Class<?> result = getPropertyTypeByMethod(beanClass, property);
    if (result == null) {
      result = getFieldType(beanClass, property);
    }
    return result;
  }

  // If the property is nested, processes sub-properties until the the property is a simple property.
  private static Object getLastBean(Object bean, String property) {
    while (StringUtils.contains(property, NESTED_DELIM)) {
      String simpleProperty = StringUtils.substringBefore(property, NESTED_DELIM);
      bean = instantiateIfNull(bean, simpleProperty);
      property = StringUtils.substringAfter(property, NESTED_DELIM);
    }
    return bean;
  }

  // If the property is nested, processes sub-properties until the the property is a simple property.
  private static Class<?> getLastBeanClass(Class<?> beanClass, String property) {
    while (StringUtils.contains(property, NESTED_DELIM)) {
      String simpleProperty = StringUtils.substringBefore(property, NESTED_DELIM);
      Class<?> tmpClass = getSimplePropertyType(beanClass, simpleProperty);
      Assert.notNull(tmpClass, "Unable to resolve property '" + simpleProperty + "' of '" + beanClass.getName() + "'!");
      beanClass = tmpClass;
      property = StringUtils.substringAfter(property, NESTED_DELIM);
    }
    return beanClass;
  }

  // Provides the last property, if the property is nested. Otherwise returns the original.
  private static String getLastProperty(String property) {
    return StringUtils.contains(property, NESTED_DELIM) ? StringUtils.substringAfterLast(property, NESTED_DELIM)
        : property;
  }

  // Instantiates and sets the bean property, if it is null.
  private static Object instantiateIfNull(Object bean, String property) {
    Object result = getPropertyValue(bean, property);

    if (result == null) {
      if (!isWritableProperty(bean.getClass(), property)) {
        throw new AraneaRuntimeException("Cannot instatiate an object as a value for property because the property is "
            + "read-only [" + getTargetDesc(bean, property) + "].");
      }

      Class<?> type = getPropertyType(bean.getClass(), property);

      if (type != null) {
        result = newInstance(type);

        if (result != null) {
          setSimplePropertyValue(bean, property, result);
        } else {
          throw new AraneaRuntimeException("Was trying to instantiate class '" + type.getSimpleName()
              + "' to set it as a value for [" + getTargetDesc(bean, property)
              + "] but some how could not. Make sure, it has default constructor.");
        }
      } else {
        throw new AraneaRuntimeException("For some reason, could not resolve the data type for property [" + property
            + "] of class [" + getTargetDesc(bean, property) + "].");
      }
    }
    return result;
  }

  // *******************************************************************
  // PRIVATE METHODS FOR PROPERTIES THAT ARE ACCESSIBLE THROUGH METHODS
  // *******************************************************************

  private static Class<?> getPropertyMethodType(Method method) {
    String name = method == null ? null : method.getName();
    if (name == null || method == null) {
      return null;
    } else if (StringUtils.startsWith(name, SETTER_PREFIX)) {
      return method.getParameterTypes().length == 1 ? method.getParameterTypes()[0] : null;
    } else if (StringUtils.startsWith(name, GETTER_PREFIX)) {
      return method.getReturnType();
    } else if (StringUtils.startsWith(name, GETTER_PREFIX2)) {
      Assert.isTrue(boolean.class.equals(method.getReturnType()), "The return type of " + method + " is not boolean!");
      return method.getReturnType();
    }
    return null;
  }

  private static Class<?> getPropertyTypeByMethod(Class<?> beanClass, String property) {
    Method method = getMethodByProperty(beanClass, property);
    Class<?> result = null;

    if (method != null) {
      result = getPropertyMethodType(method);
    }
    return result;
  }

  private static Method getPropertyGetter(Class<?> clazz, String property) {
    property = StringUtils.capitalize(property);
    Method method = getMethodByName(clazz, GETTER_PREFIX + property, 0);
    if (method == null) {
      method = getMethodByName(clazz, GETTER_PREFIX2 + property, 0);
    }
    return method;
  }

  private static Method getPropertySetter(Class<?> clazz, String property) {
    property = StringUtils.capitalize(property);
    return getMethodByName(clazz, SETTER_PREFIX + property, 1);
  }

  private static Method getMethodByProperty(Class<?> clazz, String property) {
    Method method = getPropertyGetter(clazz, property);
    if (method == null) {
      method = getPropertySetter(clazz, property);
    }
    return method;
  }

  private static Method getMethodByName(Class<?> clazz, String methodName, int paramCount) {
    for (Method method : clazz.getMethods()) {
      if (ObjectUtils.equals(methodName, method.getName()) && method.getParameterTypes().length == paramCount) {
        return method;
      }
    }
    return null;
  }

  private static boolean hasReadablePropertyMethod(Class<?> beanClass, String property) {
    return getPropertyGetter(beanClass, property) != null;
  }

  private static boolean hasWriteablePropertyMethod(Class<?> beanClass, String property) {
    return getPropertySetter(beanClass, property) != null;
  }

  // *******************************************************************************
  // PRIVATE METHODS FOR PROPERTIES THAT ARE ACCESSIBLE THROUGH CLASS-LEVEL FIELDS
  // *******************************************************************************

  // Returns the field with the given name, or null.
  private static Field getField(Class<?> bean, String fieldName) {
    Assert.notNullParam(bean, "bean");
    Assert.notNullParam(fieldName, "fieldName");
    try {
      Field field = bean.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field;
    } catch (NoSuchFieldException e) {
      return null;
    }
  }

  // Returns the field type of the field with the given name, or null.
  private static Class<?> getFieldType(Class<?> bean, String fieldName) {
    Field f = getField(bean, fieldName);
    return f == null ? null : f.getType();
  }

  // Returns the field value of the field with the given name, or null.
  private static Object getFieldValue(Object bean, String fieldName) {
    Field f = getField(bean.getClass(), fieldName);
    try {
      return f == null ? null : f.get(bean);
    } catch (Exception e) {
      return null;
    }
  }

  // Sets the field value. If field is not found, nothing happens.
  private static void setFieldValue(Object bean, String fieldName, Object value) {
    Field f = getField(bean.getClass(), fieldName);
    if (f != null) {
      if (!f.isAccessible()) {
        f.setAccessible(true);
      }

      try {
        f.set(bean, value);
      } catch (Exception e) {}
    }
  }

  private static boolean hasReadablePropertyField(Class<?> beanClass, String property) {
    return getField(beanClass, property) != null;
  }

  private static boolean hasWriteablePropertyField(Class<?> beanClass, String property) {
    return hasReadablePropertyField(beanClass, property); // Same logic as in hasReadablePropertyField().
  }

  // Validation that is used by public methods that take a bean and its property as its parameters.
  private static void assertData(Object bean, String property) {
    Assert.notNull(bean, "No bean is specified.");
    Assert.notEmpty(property, "No property is specified.");
  }

  // Used with exceptions: returns "BeanClass.property".
  private static String getTargetDesc(Object bean, String property) {
    return bean.getClass().getSimpleName() + NESTED_DELIM + property;
  }

  // Used with exceptions: returns ["null"|"ValueClass"].
  private static String getValueDesc(Object value) {
    return value == null ? "null" : value.getClass().getSimpleName();
  }
}
