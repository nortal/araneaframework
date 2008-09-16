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

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.backend.util.BeanMapper;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.uilib.form.reader.BeanFormReader;
import org.araneaframework.uilib.form.reader.BeanFormWriter;

public class BeanFormWidget<T> extends FormWidget {
  
  private static final Log log = LogFactory.getLog(BaseApplicationWidget.class);
  
  private static final String[] primitiveTypes = new String[] {"int", "long", "short", "double", "float", "boolean", "byte", "char"};
  private BeanMapper beanMapper;
  private Class<T> beanClass;
  private T bean;
  
  @SuppressWarnings("unchecked")
  public BeanFormWidget(T bean) {
    this.bean = bean;
    this.beanClass = (Class<T>) bean.getClass();
    this.beanMapper = new BeanMapper(beanClass);
    _readFromBean();
  }
  
  private BeanFormWidget(Class<T> beanClass){
    try {
      this.bean = beanClass.newInstance();
      _readFromBean();
    } catch (InstantiationException e) {
      ExceptionUtil.uncheckException(e);
    } catch (IllegalAccessException e) {
      ExceptionUtil.uncheckException(e);
    }
    this.beanClass = beanClass;
    this.beanMapper = new BeanMapper(beanClass);
  }
  
  private Data inferDataType(String fieldId, boolean mandatory) {
    if (!beanMapper.isReadable(fieldId))
      throw new AraneaRuntimeException("Could not infer type for bean field '" + fieldId + "'!");

    Class type = beanMapper.getFieldType(fieldId);
    
    if (type.isPrimitive()) {
      if (!mandatory) 
        throw new AraneaRuntimeException("Form element '" + fieldId +"' corresponding to JavaBean's primitive-typed field was not specified as mandatory.");

      switch(ArrayUtils.indexOf(primitiveTypes, type.getName())) {
        case 0: { type = Integer.class; break; }
        case 1: { type = Long.class; break; }
        case 2: { type = Short.class; break; }
        case 3: { type = Double.class; break; }
        case 4: { type = Float.class; break; }
        case 5: { type = Boolean.class; break; }
        case 6: { type = Byte.class; break; }
        case 7: { type = Character.class; break; }
        default : throw new AraneaRuntimeException("Could not infer type for bean field '" + fieldId + "'!");
      }
    }
    
    return new Data(type);
  }
  
  /**
   * NB! The user of this method must take the full responsibility for type checking when using this method.
   */
  @SuppressWarnings("unchecked")
  public <V> BeanFormWidget<V> addBeanSubForm(String id) {
    if (!beanMapper.isReadable(id))
      throw new AraneaRuntimeException("Could not infer type for bean subform '" + id + "'!");

    BeanFormWidget<V> result = new BeanFormWidget<V>(beanMapper.getFieldType(id));
    addElement(id, result);
    return result;
  }
  
  public FormElement addBeanElement(String elementName, String labelId, Control control, boolean mandatory) {
    return super.addElement(elementName, labelId, control, inferDataType(elementName, mandatory), mandatory);
  }  
  
  public FormElement addBeanElement(String elementName, String labelId, Control control, Object initialValue, boolean mandatory) {
    return super.addElement(elementName, labelId, control, inferDataType(elementName, mandatory), initialValue, mandatory);
  }

  @Deprecated
  public T writeToBean(T bean) {
    BeanFormReader reader = new BeanFormReader(this);
    reader.readFormBean(bean);
    return bean;
  }
  
  public T writeToBean() {
    BeanFormReader reader = new BeanFormReader(this);
    reader.readFormBean(bean);
    return bean;
  }
  
  public void readFromBean(T bean) {
    if(this.bean == bean){
      log.warn("You are reading from the same bean that is already contained in this form!");
    }
    this.bean = bean;
    _readFromBean();
  }

  public void readFromBean() {
    _readFromBean();
  }
  
  private void _readFromBean() {
    BeanFormWriter writer = new BeanFormWriter(beanClass);
    writer.writeFormBean(this, bean);
  }
  
  public Class<T> getBeanClass() {
    return beanClass;
  }  
}
