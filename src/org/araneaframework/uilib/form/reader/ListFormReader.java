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
**/

package org.araneaframework.uilib.form.reader;

import java.util.LinkedList;
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
 */
public class ListFormReader<K, V> {

  protected BaseFormListWidget<K, V> formList;

  protected Class<V> beanClass;

  /**
   * Creates the class, initializing the composite element to read from.
   * 
   * @param formList The composite element to read from.
   */
  public ListFormReader(BaseFormListWidget<K, V> formList) {
    this.formList = formList;
    if (formList instanceof BeanFormListWidget<?, ?>) {
      this.beanClass = ((BeanFormListWidget<K, V>) formList).getBeanClass();
    }
  }

  /**
   * Provides a <code>List></code> of values read from the list rows forms. The values are retrieved as beans of the
   * class that was defined in the constructor. If the bean class was not defined in the constructor, the values are
   * stored as map entries.
   * 
   * @return <code>List></code> with values (beans or maps) read from the form list rows forms.
   */
  public List<Object> getList() {
    List<Object> result = new LinkedList<Object>();

    for (FormRow<K, V> formRow : this.formList.getFormRows().values()) {
      FormWidget form = formRow.getForm();
      if (this.beanClass != null) {
        BeanFormReader subReader = new BeanFormReader(form);
        result.add(subReader.getBean(beanClass));
      } else {
        MapFormReader subReader = new MapFormReader(form);
        result.add(subReader.getMap());
      }
    }

    return result;
  }
}
