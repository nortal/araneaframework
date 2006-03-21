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

package org.araneaframework.example.main.web.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.util.DataDTO;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.control.CheckboxControl;
import org.araneaframework.uilib.list.EditableBeanListWidget;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;
import org.araneaframework.uilib.list.formlist.FormRow;
import org.araneaframework.uilib.list.formlist.adapters.MemoryBasedListFormRowHandlerDecorator;
import org.araneaframework.uilib.list.formlist.adapters.ValidOnlyIndividualFormRowHandler;


/**
 * This is an example of component with a single list.
 */
public class DemoCheckboxList extends TemplateBaseWidget {
	private EditableBeanListWidget checkList;
	private Map data = new HashMap();

	{
		Random rnd = new Random();

		for (int i = 0; i < 100; i += 3) {
			//Just making the initial data
			//In reality it should have been read from the database
			data.put(new Long(i + 1), new DataDTO(new Long(i + 1), Boolean.TRUE, new Long(rnd.nextLong() % 100), "Strange"));
			data.put(new Long(i + 2), new DataDTO(new Long(i + 2), Boolean.FALSE, new Long(rnd.nextLong() % 100), "Peculiar"));
			data.put(new Long(i + 3), new DataDTO(new Long(i + 3), Boolean.TRUE, new Long(rnd.nextLong() % 100), "Queer"));
		}
	}

	public void init() throws Exception {
		super.init();

		addGlobalEventListener(new ProxyEventListener(this));
		setViewSelector("demo/demoCheckboxList");
		
		MemoryBasedListDataProvider listDataProvider = new DemoCheckboxListDataProvider();

	    checkList = new EditableBeanListWidget(DataDTO.class);
	    checkList.setFormRowHandler(new DemoCheckboxListRowHandler());
	    checkList.setListDataProvider(listDataProvider);
	    
	    checkList.addListColumn("booleanField", "#Boolean");
	    checkList.addListColumn("stringField", "#String");
	    checkList.addListColumn("longField", "#Long");
		
		checkList.getFormList().setFormRowHandler(
				new MemoryBasedListFormRowHandlerDecorator(
					listDataProvider, 
					checkList.getFormList().getFormRowHandler()));
		
		checkList.setInitialOrder("longField", false);

		addWidget("checkList", checkList);
	}

	public void handleEventSave(String parameter) throws Exception {
		checkList.getFormList().saveCurrentRows();
	}
	
	public void handleEventReturn(String parameter) throws Exception {
		getFlowCtx().cancel();
	}

	public class DemoCheckboxListRowHandler extends ValidOnlyIndividualFormRowHandler {
		public Object getRowKey(Object row) {
			return ((DataDTO) row).getId();
		}

		public void saveValidRow(FormRow editableRow) {
			DataDTO rowData = (DataDTO) data.get(editableRow.getRowKey());
			rowData.setBooleanField((Boolean) editableRow.getRowForm().getValueByFullName("booleanField"));
		}

		public void initFormRow(FormRow editableRow, Object row) throws Exception {
			((BeanFormWidget)editableRow.getRowForm()).addBeanElement("booleanField", "#Boolean field", new CheckboxControl(), true);
			((BeanFormWidget)editableRow.getRowForm()).writeBean(row);
		}
	}

	public class DemoCheckboxListDataProvider extends MemoryBasedListDataProvider {
		public DemoCheckboxListDataProvider() {
			super(DataDTO.class);
		}

		public List loadData() throws Exception {
			return new ArrayList(data.values());
		}
	}
}
