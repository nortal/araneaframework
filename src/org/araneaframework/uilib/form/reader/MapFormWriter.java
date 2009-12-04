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

import java.io.Serializable;
import java.util.Map;
import org.araneaframework.uilib.form.Data;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.GenericFormElement;

/**
 * This class allows one to write <code>Map</code>s to forms.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 */
public class MapFormWriter implements Serializable {

  /**
   * Writes the Value Object values to form where possible.
   * 
   * @param form {@link FormWidget}to write to.
   * @param map <code>Map</code> to read from.
   */
  @SuppressWarnings("unchecked")
  public void writeForm(FormWidget form, Map<?, ?> map) {

    for (Map.Entry entry : map.entrySet()) {
      String key = (String) entry.getKey();
      GenericFormElement element = form.getElement(key);

      if (element != null) {
        if (element instanceof FormElement) {
          Data data = ((FormElement) element).getData();
          if (data != null) {
            data.setValue(entry.getValue());
          }
        } else if (element instanceof FormWidget) {
          MapFormWriter subMapWriter = new MapFormWriter();

          Map subMap = (Map) entry.getValue();

          if (subMap != null) {
            subMapWriter.writeForm((FormWidget) element, subMap);
          }
        }
      }
    }
  }
}
