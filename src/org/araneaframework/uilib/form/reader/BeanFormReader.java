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

import java.util.Iterator;
import java.util.Map;
import org.araneaframework.backend.util.BeanMapper;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.uilib.form.Data;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.GenericFormElement;


/**
 * This class allows one to read <code>Value Object</code> s from
 * {@link org.araneaframework.uilib.form.FormWidget}s.
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * 
 */
public class BeanFormReader {

  protected FormWidget compositeFormElement;
  
  /**
	 * Creates the class, initializing the composite element to read from.
	 * 
	 * @param compositeFormElement
	 *          the composite element to read from.
	 */
  public BeanFormReader(FormWidget compositeFormElement) {
    this.compositeFormElement = compositeFormElement;
  }

  /**
	 * Returns Value Object of the specified class with values read from the form
	 * where possible.
	 * 
	 * @param voClass
	 *          Value Object class.
	 * @return Value Object of the specified class with values read from the form
	 *         where possible.
	 */
  public Object getBean(Class voClass) {
    Object result = null;
    try {
      result = voClass.newInstance();
      readFormBean(result);
    }
    catch (InstantiationException e) {
      throw new AraneaRuntimeException("Could not read Value Object from form", e);
    }
    catch (IllegalAccessException e) {
      throw new AraneaRuntimeException("Could not read Value Object from form", e);
    }
    return result;
  }

  /**
	 * Writes values from form to Value Object where possible.
	 * 
	 * @param vo
	 *          Value Object to write to.
	 */
  public void readFormBean(Object vo) {
	  BeanMapper beanMapper = new BeanMapper(vo.getClass());
    for (Iterator i = compositeFormElement.getElements().entrySet().iterator(); i.hasNext();) {
    	Map.Entry entry = (Map.Entry) i.next();
    	
      GenericFormElement element = (GenericFormElement) entry.getValue();
      String elementId = (String) entry.getKey();
      
      if (element instanceof FormElement) {
        if (beanMapper.isWritable(elementId)) {
          Data data = ((FormElement) element).getData();
          if (data != null) {
            beanMapper.setFieldValue(vo, elementId, data.getValue());
          }
        }
      }
      else if (element instanceof FormWidget) {
        if (beanMapper.isWritable(elementId)) {
          BeanFormReader subVoReader = new BeanFormReader((FormWidget) element);
          beanMapper.setFieldValue(vo, elementId, subVoReader.getBean(beanMapper.getFieldType(elementId)));
        }
      }
    }

  }
}
