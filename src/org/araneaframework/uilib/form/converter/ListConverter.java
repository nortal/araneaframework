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

package org.araneaframework.uilib.form.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.araneaframework.uilib.form.FormElementContext;


/**
 * This converter uses a contained converter to convert individual <code>List</code>
 * items thus converting the entire <code>List</code>.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class ListConverter extends BaseConverter {

  protected BaseConverter listItemConverter;

  /**
	 * Creates the converter initializing the contained converter.
	 * 
	 * @param listItemConverter the contained converter.
	 */
  public ListConverter(BaseConverter listItemConverter) {
    this.listItemConverter = listItemConverter;
  }

  /**
	 *  Converts the <code>List</code> using contained converter.
	 */
  public Object convertNotNull(Object data) {
    List result = new ArrayList();

    for (Iterator i = ((Collection) data).iterator(); i.hasNext();) {
      result.add(listItemConverter.convertNotNull(i.next()));
    }

    return result;
  }

  /**
	 *  Converts the <code>List</code> back using contained converter.
	 */
  public Object reverseConvertNotNull(Object data) {
    List result = new ArrayList();

    for (Iterator i = ((Collection) data).iterator(); i.hasNext();) {
      result.add(listItemConverter.reverseConvertNotNull(i.next()));
	  }
    return result;
  }

  public void setFormElementCtx(FormElementContext feCtx) {
    super.setFormElementCtx(feCtx);
    
    listItemConverter.setFormElementCtx(feCtx);
  }

  /**
	 *  Returns a <code>new ListConverter(listItemConverter)</code>.
	 */
  public BaseConverter newConverter() {
    return new ListConverter(listItemConverter.newConverter());
  }

}
