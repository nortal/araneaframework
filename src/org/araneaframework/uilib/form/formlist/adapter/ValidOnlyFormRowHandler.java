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

package org.araneaframework.uilib.form.formlist.adapter;

import java.util.Map;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.formlist.FormListUtil;
import org.araneaframework.uilib.form.formlist.FormRow;

/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public abstract class ValidOnlyFormRowHandler extends DefaultFormRowHandler {

	@Override
  public final void addRow(FormWidget rowForm) throws Exception {
    if (rowForm.convertAndValidate())
      addValidRow(rowForm); 
  }	   
	@Override
  public final void saveRows(Map<Object, FormRow> formRows) throws Exception {
    if (FormListUtil.convertAndValidateRowForms(formRows))
      saveValidRows(formRows); 
  }
  
  public void addValidRow(FormWidget rowForm) throws Exception {}     
  public void saveValidRows(Map<Object, FormRow> formRows) throws Exception {} 
}
