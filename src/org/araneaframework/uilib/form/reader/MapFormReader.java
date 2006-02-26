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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.GenericFormElement;
import org.araneaframework.uilib.form.data.Data;


/**
 * This class allows one to read <code>Map</code> s from
 * {@link org.araneaframework.uilib.form.FormWidget}s.
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * 
 */
public class MapFormReader {

  protected FormWidget compositeFormElement;

  /**
   * Creates the class, initializing the composite element to read from.
   * 
   * @param compositeFormElement the composite element to read from.
   */
  public MapFormReader(FormWidget compositeFormElement) {
    this.compositeFormElement = compositeFormElement;
  }

  /**
   * Returns <code>Map></code> with values read from the form where possible.
   * 
   * @return <code>Map></code> with values read from the form where possible.
   */
  public Map getMap() {
    Map result = new HashMap();

    for (Iterator i = compositeFormElement.getElements().entrySet().iterator(); i.hasNext();) {
    	Map.Entry entry = (Map.Entry) i.next();
    	
      GenericFormElement element = (GenericFormElement) entry.getValue();
      String elementId = (String) entry.getKey();
      
      if (element instanceof FormElement) {
        Data data = ((FormElement) element).getData();
        if (data != null) {
          result.put(elementId, data.getValue());
        }
      }
      else if (element instanceof FormWidget) {
        MapFormReader subMapReader = new MapFormReader((FormWidget) element);
        result.put(elementId, subMapReader.getMap());
      }
    }

    return result;
  }
}
