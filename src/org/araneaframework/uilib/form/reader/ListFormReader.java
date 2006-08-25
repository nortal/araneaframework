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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.formlist.BaseFormListWidget;
import org.araneaframework.uilib.form.formlist.BeanFormListWidget;
import org.araneaframework.uilib.form.formlist.FormRow;


/**
 * This class allows one to read <code>List</code> s from
 * {@link org.araneaframework.uilib.form.formlist.FormListWidget}s.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class ListFormReader {

  protected BaseFormListWidget formList;
  protected Class beanClass;

  /**
   * Creates the class, initializing the composite element to read from.
   * 
   * @param formList the composite element to read from.
   */
  public ListFormReader(BaseFormListWidget formList) {
    this.formList = formList;
    
    if (formList instanceof BeanFormListWidget) {
      beanClass = ((BeanFormListWidget) formList).getBeanClass();
    }
  }

  /**
   * Returns <code>List></code> with values read from the form list where possible.
   * 
   * @return <code>List></code> with values read from the form list swhere possible.
   */
  public List getList() {
    List result = new ArrayList();

    for (Iterator i = formList.getFormRows().values().iterator(); i.hasNext();) {
    	FormRow formRow = (FormRow) i.next();

      FormWidget element = formRow.getForm();

      if (beanClass != null) {
        BeanFormReader subReader = new BeanFormReader((FormWidget) element);
        result.add(subReader.getBean(beanClass));
      }
      else {
        MapFormReader subReader = new MapFormReader((FormWidget) element);
        result.add(subReader.getMap());
      }
    }

    return result;
  }
}
