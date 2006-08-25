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

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.formlist.BaseFormListWidget;
import org.araneaframework.uilib.form.formlist.FormRow;
import org.araneaframework.uilib.form.formlist.FormRowHandler;

/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class MapFormRowHandlerDecorator implements FormRowHandler {

	protected FormRowHandler rowHandler;
	protected BaseFormListWidget rowsWidget;
	protected Map data;
	
	/**
	 * @param data
	 */
	public MapFormRowHandlerDecorator(Map data, BaseFormListWidget rowsWidget, FormRowHandler rowHandler) {
		this.rowHandler = rowHandler;
		this.rowsWidget = rowsWidget;
		this.data = data;
	}
	
	public Object getRowKey(Object row) {
		return rowHandler.getRowKey(row);
	}

	public void saveRows(Map rowForms) throws Exception {
		rowHandler.saveRows(rowForms);
		rowsWidget.setRows(new ArrayList(data.values()));
	}

	public void deleteRows(Set keys) throws Exception {
		rowHandler.deleteRows(keys);
		rowsWidget.setRows(new ArrayList(data.values()));
	}

	public void initFormRow(FormRow editableRow, Object row) throws Exception {
		rowHandler.initFormRow(editableRow, row);
	}

	public void initAddForm(FormWidget addForm) throws Exception {
		rowHandler.initAddForm(addForm);
	}	

	public void addRow(FormWidget rowForm) throws Exception {
		rowHandler.addRow(rowForm);
		rowsWidget.setRows(new ArrayList(data.values()));
	}

	public void openOrCloseRow(FormRow editableRow) throws Exception {
		rowHandler.openOrCloseRow(editableRow);
	}

}
