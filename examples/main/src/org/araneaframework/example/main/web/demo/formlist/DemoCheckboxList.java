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

package org.araneaframework.example.main.web.demo.formlist;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.util.DataDTO;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.control.CheckboxControl;
import org.araneaframework.uilib.form.formlist.FormRow;
import org.araneaframework.uilib.form.formlist.adapter.ValidOnlyIndividualFormRowHandler;
import org.araneaframework.uilib.list.EditableBeanListWidget;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;

/**
 * This is an example of component with a single list.
 */
public class DemoCheckboxList extends TemplateBaseWidget {

  private EditableBeanListWidget<Long, DataDTO> checkList;

  private Map<Long, DataDTO> data = new HashMap<Long, DataDTO>();

  public DemoCheckboxList() {
    Random rnd = new Random();

    // Just making the initial data. In reality it should have been read from the database:
    for (int i = 0; i < 100; i += 3) {
      this.data.put(i + 1L, new DataDTO(i + 1L, true, rnd.nextLong() % 100L, "Strange"));
      this.data.put(i + 2L, new DataDTO(i + 2L, false, rnd.nextLong() % 100L, "Peculiar"));
      this.data.put(i + 3L, new DataDTO(i + 3L, true, rnd.nextLong() % 100L, "Queer"));
    }
  }

  @Override
  public void init() throws Exception {
    setViewSelector("demo/demoCheckboxList");

    this.checkList = new EditableBeanListWidget<Long, DataDTO>(new DemoCheckboxListRowHandler(), DataDTO.class);
    this.checkList.setDataProvider(new DemoCheckboxListDataProvider());
    addWidget("checkList", checkList);

    this.checkList.addField("booleanField", "#Boolean");
    this.checkList.addField("stringField", "#String");
    this.checkList.addField("longField", "#Long");
    this.checkList.setInitialOrder("longField", false);
  }

  public void handleEventSave() {
    this.checkList.getFormList().saveCurrentRows();
  }

  public void handleEventReturn() {
    getFlowCtx().cancel();
  }

  public class DemoCheckboxListRowHandler extends ValidOnlyIndividualFormRowHandler<Long, DataDTO> {

    public Long getRowKey(DataDTO row) {
      return row.getId();
    }

    @Override
    public void saveValidRow(FormRow<Long, DataDTO> editableRow) {
      DataDTO rowData = editableRow.getRow();
      rowData.setBooleanField((Boolean) editableRow.getForm().getValueByFullName("booleanField"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initFormRow(FormRow<Long, DataDTO> editableRow, DataDTO row) throws Exception {
      BeanFormWidget<DataDTO> form = (BeanFormWidget<DataDTO>) editableRow.getForm();
      form.addBeanElement("booleanField", "#Boolean field", new CheckboxControl(), true);
      form.readFromBean(editableRow.getRow());
    }
  }

  public class DemoCheckboxListDataProvider extends MemoryBasedListDataProvider<DataDTO> {

    public DemoCheckboxListDataProvider() {
      super(DataDTO.class);
    }

    @Override
    public List<DataDTO> loadData() throws Exception {
      return new LinkedList<DataDTO>(DemoCheckboxList.this.data.values());
    }
  }
}
