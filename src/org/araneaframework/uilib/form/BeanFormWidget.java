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

import org.araneaframework.backend.util.BeanMapper;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.uilib.form.reader.BeanFormReader;
import org.araneaframework.uilib.form.reader.BeanFormWriter;

public class BeanFormWidget extends FormWidget {
  private BeanMapper beanMapper;
  private Class beanClass;
  
  public BeanFormWidget(Class beanClass) {
    this.beanClass = beanClass;
    this.beanMapper = new BeanMapper(beanClass);
  }
  
  private Data inferDataType(String fieldId) {
    if (!beanMapper.fieldExists(fieldId))
      throw new AraneaRuntimeException("Could not infer type for bean field '" + fieldId + "'!");

    return new Data(beanMapper.getBeanFieldType(fieldId));
  }
  
  public BeanFormWidget addBeanSubForm(String id) throws Exception {
    if (!beanMapper.fieldExists(id))
      throw new AraneaRuntimeException("Could not infer type for bean subform '" + id + "'!");

    BeanFormWidget result = new BeanFormWidget(beanMapper.getBeanFieldType(id));
    addElement(id, result);
    return result;
  }
  
  public FormElement addBeanElement(String elementName, String labelId, Control control, boolean mandatory) throws Exception {
    return super.addElement(elementName, labelId, control, inferDataType(elementName), mandatory);
  }  
  
  public FormElement addBeanElement(String elementName, String labelId, Control control, Object initialValue, boolean mandatory) throws Exception {
    return super.addElement(elementName, labelId, control, inferDataType(elementName), initialValue, mandatory);
  }
  
  public Object readBean(Object bean) {
    BeanFormReader reader = new BeanFormReader(this);
    reader.readFormBean(bean);
    return bean;
  }

  public void writeBean(Object bean) {
    BeanFormWriter writer = new BeanFormWriter(beanClass);
    writer.writeFormBean(this, bean);
  }
  
  
}
