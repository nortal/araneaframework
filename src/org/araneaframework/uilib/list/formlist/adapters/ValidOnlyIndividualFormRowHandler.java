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

package org.araneaframework.uilib.list.formlist.adapters;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.araneaframework.uilib.list.formlist.FormRow;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public abstract class ValidOnlyIndividualFormRowHandler extends ValidOnlyFormRowHandler{
	public void saveValidRows(Map rowForms) throws Exception {
		for (Iterator i = rowForms.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			saveValidRow((FormRow) entry.getValue());
		}
	}
	
	public void deleteRows(Set keys) throws Exception {
		for (Iterator i = keys.iterator(); i.hasNext(); )
			deleteRow(i.next());
	}
  public final void saveRow(FormRow formRow) throws Exception  {}
  
	public void saveValidRow(FormRow formRow) throws Exception  {}
	public void deleteRow(Object key) throws Exception {}
}
