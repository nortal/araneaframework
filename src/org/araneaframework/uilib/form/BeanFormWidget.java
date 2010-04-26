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

  private BeanMapper<T> beanMapper;

  private Class<T> beanClass;

  private T bean;

  public BeanFormWidget(Class<T> beanClass, T bean) {
    Assert.notNullParam(beanClass, "beanClass");
    Assert.notNullParam(bean, "bean");

    this.beanClass = beanClass;
    this.beanMapper = new BeanMapper<T>(beanClass);
    this.bean = bean;
  }

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
    readFromBean();
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
   * @return If path is empty or simple (not nested) then the current bean form widget is returned. Otherwise, the last
   *         created bean-sub-form widget is returned.
   * @see #addSubForm(String)
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
    Data<D> data = new Data(BeanUtil.getPropertyType(this.beanClass, elementName));
    return addElement(elementName, labelId, control, data, mandatory);
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
   * Writes form data to given bean. Deprecated in favor of {@link #writeToBean()}.
   * 
   * @param bean An instance of bean where form values will be written.
   * @return The same bean as provided for input.
   */
  @Deprecated
  public T writeToBean(T bean) {
    BeanFormReader reader = new BeanFormReader(this);
    reader.readFormBean(bean);
    return bean;
  }

  /**
   * Writes data from given <code>bean</code> to the underlying form.
   * 
   * @param bean An instance of <code>bean</code>.
   */
  public void readFromBean(T bean) {
    BeanFormWriter<T> writer = new BeanFormWriter<T>(this.beanClass);
    writer.writeFormBean(this, bean);
  }

  public void readFromBean() {
    readFromBean(this.bean);
  }

  public T writeToBean() {
    BeanFormReader reader = new BeanFormReader(this);
    reader.readFormBean(this.bean);
    return this.bean;
  }

  public T getBean() {
    return this.bean;
  }

  public Class<T> getBeanClass() {
    return this.beanClass;
  }

}
