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

package org.araneaframework.uilib.list.formlist;

import java.util.Map;
import java.util.Set;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.list.RowHandler;

/**
 * This class represents the callback interface provided by the programmer to the {@link org.araneaframework.uilib.list.formlist.FormListWidget}. 
 * 
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public interface FormRowHandler extends RowHandler {
  /**
   * Should initialize the editable row building the form and setting the correct open/close status.
   * 
   * @param formRow editable row.
   * @param row row object.
   */
  public void initFormRow(FormRow formRow, Object row) throws Exception;    
  
  /**
   * Should initialize the form that will be used to add new rows.
   * 
   * @param addForm form that will be used to add new rows.
   */
  public void initAddForm(FormWidget addForm) throws Exception;
  
  /**
   * This method is called when a new row should be added. The row data should be read from the supplied form.
   * 
   * @param addForm the form that will be used to add new rows.
   */
  public void addRow(FormWidget addForm) throws Exception;
  
  /**
   * This method is called when the specified form rows should be saved.
   * 
   * @param formRows <code>Map&lt;Object key, EditableRow</code>. 
   */
  public void saveRows(Map formRows) throws Exception;
  
  /**
   * This method is called when the rows specified by the supplied set of keys should be deleted. 
   * 
   * @param keys row keys.
   */
  public void deleteRows(Set keys) throws Exception;
  
  /**
   * This method is called when the supplied row has been opened or closed.
   * @param formRow editable row.
   */
  public void openOrCloseRow(FormRow formRow) throws Exception;
}
