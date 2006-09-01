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

package org.araneaframework.uilib.form.formlist.adapters;

import java.util.Map;
import java.util.Set;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.formlist.FormRow;
import org.araneaframework.uilib.form.formlist.FormRowHandler;

/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public abstract class DefaultFormRowHandler implements FormRowHandler {

	public abstract Object getRowKey(Object row);

	public void initFormRow(FormRow editableRow, Object row) throws Exception {}
	public void initAddForm(FormWidget addForm) throws Exception {}
	
	public void addRow(FormWidget rowForm) throws Exception {}	
	public void saveRows(Map editableRows) throws Exception {}
	public void deleteRows(Set keys) throws Exception {}

	/**
	 * Opens a closed row or resets and closes open row. 
	 * @see FormRowHandler#openOrCloseRow(FormRow)
	 */
	public void openOrCloseRow(FormRow editableRow) throws Exception {
		if (editableRow.isOpen()) {
			editableRow.reset();
		}
 		editableRow.setOpen(!editableRow.isOpen());
	}

}
