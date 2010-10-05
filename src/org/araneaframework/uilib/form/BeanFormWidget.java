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

package org.araneaframework.uilib.form;

import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.backend.util.BeanMapper;
import org.araneaframework.backend.util.BeanUtil;
import org.araneaframework.core.Assert;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.uilib.form.reader.BeanFormReader;
import org.araneaframework.uilib.form.reader.BeanFormWriter;

public class BeanFormWidget<T> extends FormWidget {

  protected BeanMapper<T> beanMapper;

  protected Class<T> beanClass;

  protected T bean;

  /**
   * Holds information whether readFromBean(*) has been called before this widget is initialized. When this variable is
   * still <code>false</code> by the time {@link #init()} occurs, the {@link #readFromBean()} method will be called
   * automatically. This makes using <code>BeanFormWidget</code> more convenient when the bean object is provided to the
   * constructor (by not having to call the read method manually). However, it also prevents writing over form data when
   * a programmer has already called {@link #readFromBean(Object)}.
   * 
   * @since 2.0
   */
  protected boolean beanReadBeforeInit;

  public BeanFormWidget(Class<T> beanClass, T bean) {
    Assert.notNullParam(beanClass, "beanClass");
    Assert.notNullParam(bean, "bean");

    this.beanClass = beanClass;
    this.beanMapper = new BeanMapper<T>(beanClass);
    this.bean = bean;
  }

  /**
   * Initiates a new <code>BeanFormWidget</code> using the given <code>beanClass</code> to analyze the bean and to 
   * @param beanClass
   */
  public BeanFormWidget(Class<T> beanClass) {
    Assert.notNullParam(beanClass, "beanClass");

    this.beanClass = beanClass;
    this.beanMapper = new BeanMapper<T>(beanClass);
    try {
      this.bean = beanClass.newInstance();
    } catch (Exception e) {
      throw ExceptionUtil.uncheckException(e);
    }
  }

  @Override
  protected void init() throws Exception {
    super.init();
    if (!this.beanReadBeforeInit) {
      readFromBean();
    }
  }

  /**
   * Adds a bean-sub-form with given path. Since this class is BeanFormWidget, the path must map to bean properties and
   * property type is evaluated and used as the type of the bean-sub-form.
   * <p>
   * Since Aranea 2.0, the given path may be nested where path elements (separated by dots) are used for bean-sub-form
   * creation or for adding new sub-forms with the path element as the sub-form ID.
   * 
   * @param path The (simple or nested) path of bean-sub-form to add where path must also correspond to bean properties.
   *          Nested path has dots separating sub-form IDs in the order they will be created (the second sub-form will
   *          be the sub-form of the first sub-form, etc).
   * @return The last created sub-form, or the current bean (when the path is empty).
   * @see #addSubForm(String)
   * @see #addSimpleBeanSubForm(String)
   */
  public BeanFormWidget<?> addBeanSubForm(String path) {
    BeanFormWidget<?> result = this;

    if (!StringUtils.isEmpty(path)) {
      StringTokenizer tokens = new StringTokenizer(path, BeanUtil.NESTED_DELIM);

      while (tokens.hasMoreTokens()) {
        String subFormId = tokens.nextToken();
        BeanFormWidget<?> subForm = (BeanFormWidget<?>) result.getSubFormByFullName(subFormId);
        result = subForm != null ? subForm : result.addSimpleBeanSubForm(subFormId);
      }
    }

    return result;
  }

  /**
   * Adds a bean sub form with the given <code>id</code> and with given <code>dataType</code>. Does not support nested
   * fields! Also, the <code>id</code> must match a property of the bean this bean-form-widget represents.
   * 
   * @param id The ID the sub-form will have.
   * @param dataType The type the sub-form represents when converted to the target bean.
   * @return The created sub-form.
   * @deprecated Use {@link #addSimpleBeanSubForm(String, Class)} instead.
   */
  @Deprecated
  public <E> BeanFormWidget<E> addBeanSubForm(String id, Class<E> dataType) {
    return addSimpleBeanSubForm(id, dataType);
  }

  /**
   * Adds a bean sub form with the given <code>id</code>. Does not support nested fields! Also, the <code>id</code> must
   * match a property of the bean this bean-form-widget represents. The property type for sub-form will be automatically
   * resolved using the target bean.
   * 
   * @param id The ID the sub-form will have.
   * @return The created sub-form.
   * @since 2.0
   */
  public BeanFormWidget<?> addSimpleBeanSubForm(String id) {
    return addSimpleBeanSubForm(id, this.beanMapper.getPropertyType(id));
  }

  /**
   * Adds a bean-sub-form with the given <code>id</code> and with given <code>dataType</code>. Does not support nested
   * fields! Also, the <code>id</code> must match a property of the bean this bean-form-widget represents. The
   * <code>dataType</code> must be compatible with the property type.
   * 
   * @param <E> The type of the class that will be also the type of the <code>BeanFormWidget</code>.
   * @param id The ID the sub-form will have.
   * @param dataType The type the sub-form represents when converted to the target bean.
   * @return The created sub-form.
   * @since 2.0
   */
  public <E> BeanFormWidget<E> addSimpleBeanSubForm(String id, Class<E> dataType) {
    Assert.notNullParam(dataType, "fieldType");
    Assert.isTrue(StringUtils.isEmpty(id) || !StringUtils.contains(id, BeanUtil.NESTED_DELIM), "The path ['" + id
        + "'] may not be a nested path!");

    Class<?> realType = this.beanMapper.getPropertyType(id);
    Assert.notNull(realType, "The bean property '" + id + "' was not found!");
    Assert.isTrue(realType.isAssignableFrom(dataType), "The bean property '" + id + "' type [" + realType.getName()
        + "] cannot hold value of given type [" + dataType.getName() + "]!");

    BeanFormWidget<E> result = null;

    if (!StringUtils.isEmpty(id)) {
      result = new BeanFormWidget<E>(dataType);
      addElement(id, result);
    }

    return result;
  }

  public <C, D> FormElement<C, D> addBeanElement(String elementName, String labelId, Control<C> control) {
    return addBeanElement(elementName, labelId, control, false);
  }

  @SuppressWarnings("unchecked")
  public <C, D> FormElement<C, D> addBeanElement(String elementName, String labelId, Control<C> control,
      boolean mandatory) {
    Class<?> type = BeanUtil.getPropertyType(this.beanClass, elementName);
    Assert.notNull(type, "Could not resolve the type of property '" + elementName + "' in class " + this.beanClass);

    return addElement(elementName, labelId, control, new Data(type), mandatory);
  }

  @SuppressWarnings("unchecked")
  public <C, D> FormElement<C, D> addBeanElement(String elementName, String labelId, Control<C> control,
      D initialValue, boolean mandatory) {
    if (initialValue == null) {
      return addBeanElement(elementName, labelId, control, mandatory);
    } else {
      return addElement(elementName, labelId, control, Data.newInstance((Class<D>) initialValue.getClass()),
          initialValue, mandatory);
    }
  }

  /**
   * Writes data from given <code>bean</code> to the underlying form.
   * 
   * @param bean An instance of <code>bean</code>.
   */
  public void readFromBean(T bean) {
    BeanFormWriter<T> writer = new BeanFormWriter<T>(this.beanClass);
    writer.writeFormBean(this, bean);
    this.beanReadBeforeInit = true;
  }

  public void readFromBean() {
    readFromBean(this.bean);
  }

  public T writeToBean() {
    BeanFormReader reader = new BeanFormReader(this);
    reader.readFormBean(this.bean);
    return this.bean;
  }

  /**
   * Writes form data to given bean. Prefer using {@link #writeToBean()} instead, when possible, because it makes use of
   * the existing bean object that was provided to this form widget.
   * 
   * @param bean An instance of bean where form values will be written.
   * @return The same bean as provided for input.
   */
  public T writeToBean(T bean) {
    BeanFormReader reader = new BeanFormReader(this);
    reader.readFormBean(bean);
    return bean;
  }

  /**
   * Provides the bean that was provided for input. Note that the bean may have different property values compared to
   * the state when the form was initialized, because when {@link #writeToBean()} is used, it updates the bean
   * properties with values from the form.
   * 
   * @return The bean object that was used to initialize this form widget.
   */
  public T getBean() {
    return this.bean;
  }

  /**
   * Provides the bean class that this form widget is using for resolving bean elements.
   * 
   * @return The bean class that this form widget is using.
   */
  public Class<T> getBeanClass() {
    return this.beanClass;
  }

}
