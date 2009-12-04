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

package org.araneaframework.uilib.form.reader;

import org.araneaframework.core.Assert;

import java.io.Serializable;
import java.util.List;
import org.araneaframework.backend.util.BeanMapper;
import org.araneaframework.uilib.form.Data;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.GenericFormElement;

/**
 * This class allows one to write Value Objects to forms.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 */
public class BeanFormWriter<T> implements Serializable {

  protected BeanMapper<T> beanMapper;

  /**
   * Creates the class initializing the Value Object class.
   * 
   * @param voClass the Value Object class.
   */
  public BeanFormWriter(Class<T> voClass) {
    this.beanMapper = new BeanMapper<T>(voClass);
  }

  /**
   * Writes the Value Object values to form where possible.
   * 
   * @param form {@link FormWidget} to write to.
   * @param vo Value Object to read from.
   */
  @SuppressWarnings("unchecked")
  public void writeFormBean(FormWidget form, T vo) {
    List<String> voFields = this.beanMapper.getPropertyNames();

    for (String field : voFields) {
      GenericFormElement element = form.getElement(field);
      if (element != null) {
        if (element instanceof FormElement) {
          Data data = ((FormElement) element).getData();
          if (data != null) {
            data.setValue(this.beanMapper.getProperty(vo, field));
          }
        } else if (element instanceof FormWidget) {
          BeanFormWriter subVoWriter = getInstance(this.beanMapper.getPropertyType(field));
          Object subVO = this.beanMapper.getProperty(vo, field);
          if (subVO != null) {
            subVoWriter.writeFormBean((FormWidget) element, subVO);
          }
        }
      }
    }
  }

  public static <E> BeanFormWriter<E> getInstance(Class<E> clazz) {
    Assert.notNullParam(clazz, "clazz");
    return new BeanFormWriter<E>(clazz);
  }
}
