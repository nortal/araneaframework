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

package org.araneaframework.uilib.form;

import org.apache.commons.beanutils.PropertyUtils;
import org.araneaframework.backend.util.BeanMapper;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.uilib.form.reader.BeanFormReader;
import org.araneaframework.uilib.form.reader.BeanFormWriter;

public class BeanFormWidget<T> extends FormWidget {
//  private static final String[] primitiveTypes = new String[] {"int", "long", "short", "double", "float", "boolean", "byte", "char"};
  private BeanMapper<T> beanMapper;
  private Class<T> beanClass;
  private T bean;
  
  public BeanFormWidget(Class<T> beanClass, T bean) {
    this.beanClass = beanClass;
    this.beanMapper = new BeanMapper<T>(beanClass);
    this.bean = bean;
  }
  
  public BeanFormWidget(Class<T> beanClass) {
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
  
  public BeanFormWidget<?> addBeanSubForm(String id) {
    return addBeanSubForm(id, Object.class);
  }
  
  public <E> BeanFormWidget<E> addBeanSubForm(String id, Class<E> fieldType) {
    if (!beanMapper.isReadable(id))
      throw new AraneaRuntimeException("Could not infer type for bean subform '" + id + "'!");
    if(!beanMapper.getFieldType(id).isAssignableFrom(fieldType)){
      throw new AraneaRuntimeException("The field '" + id + "' has different type than the provided " + fieldType);
    }
    BeanFormWidget<E> result = null;
    try {
      result = new BeanFormWidget<E>(fieldType, (E)PropertyUtils.getProperty(bean, id));
    } catch (Exception e) {
      throw ExceptionUtil.uncheckException(e);
    }
    addElement(id, result);
    return result;
  }
  
  public <C,D> FormElement<C,D> addBeanElement(String elementName, String labelId, Control<C> control, boolean mandatory) {
    Object initialValue = null;
    try {
      initialValue = PropertyUtils.getProperty(bean, elementName);
    } catch (Exception e) {
      throw ExceptionUtil.uncheckException(e);
    } 
    return addBeanElement(elementName, labelId, control, (D)initialValue, mandatory);
  }  
  
  public <C,D> FormElement<C,D> addBeanElement(String elementName, String labelId, Control<C> control, D initialValue, boolean mandatory) {
    return super.addElement(elementName, labelId, control, Data.newInstance((Class<D>)initialValue.getClass()), initialValue, mandatory);
  }

  @Deprecated
  public T writeToBean(T bean) {
    BeanFormReader reader = new BeanFormReader(this);
    reader.readFormBean(bean);
    return bean;
  }
  
  @Deprecated
  public void readFromBean(T bean) {
    BeanFormWriter<T> writer = new BeanFormWriter<T>(beanClass);
    writer.writeFormBean(this, bean);
  }

  public void readFromBean() {
    BeanFormWriter<T> writer = new BeanFormWriter<T>(beanClass);
    writer.writeFormBean(this, bean);
  }
  
  public T writeToBean() {
    BeanFormReader reader = new BeanFormReader(this);
    reader.readFormBean(bean);
    return bean;
  }
  
  public T getBean() {
    return bean;
  }
  
  public Class<T> getBeanClass() {
    return beanClass;
  }  
  
}
