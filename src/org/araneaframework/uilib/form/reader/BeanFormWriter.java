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

package org.araneaframework.uilib.form.reader;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import org.araneaframework.backend.util.BeanMapper;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.GenericFormElement;
import org.araneaframework.uilib.form.data.Data;


/**
 * This class allows one to write Value Objects to forms.
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * 
 */
public class BeanFormWriter implements Serializable {
  protected BeanMapper beanMapper;

  /**
   * Creates the class initializing the Value Object class.
   * 
	 * @param voClass the Value Object class.
	 */
  public BeanFormWriter(Class voClass) {
    beanMapper = new BeanMapper(voClass);
  }

  /**
   * Writes the Value Object values to form where possible.
   * 
	 * @param form {@link FormWidget} to write to.
	 * @param vo Value Object to read from.
	 */
  public void writeFormBean(FormWidget form, Object vo) {
    List voFields = beanMapper.getBeanFields();

    for (Iterator i = voFields.iterator(); i.hasNext();) {
      String field = (String) i.next();
      GenericFormElement element = form.getElement(field);
      if (element != null) {
        if (element instanceof FormElement) {          
          Data data = ((FormElement) element).getData();
          if (data != null) {
            data.setValue(beanMapper.getBeanFieldValue(vo, field));
          }
        }
        else if (element instanceof FormWidget) {
          BeanFormWriter subVoWriter = new BeanFormWriter(beanMapper.getBeanFieldType(field));

          Object subVO = beanMapper.getBeanFieldValue(vo, field);
          
          if (subVO != null) {
            subVoWriter.writeFormBean((FormWidget) element, subVO);
          }
        }
      }
    }
  }
}
