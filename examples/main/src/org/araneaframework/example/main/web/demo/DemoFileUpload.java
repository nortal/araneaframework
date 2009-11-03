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

package org.araneaframework.example.main.web.demo;

import java.util.ArrayList;
import java.util.List;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.http.service.FileDownloaderService;
import org.araneaframework.http.support.PopupWindowProperties;
import org.araneaframework.uilib.event.OnClickEventListener;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.control.FileUploadControl;
import org.araneaframework.uilib.form.control.SelectControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.FileInfoData;
import org.araneaframework.uilib.form.data.StringData;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;
import org.araneaframework.uilib.support.DisplayItem;
import org.araneaframework.uilib.support.FileInfo;

/**
 * Demonstrates usage of file upload control.
 * 
 * @author Lauri Tulmin
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoFileUpload extends TemplateBaseWidget {

  private FormWidget form;

  private ListWidget<FileInfo> uploadList;

  private List<FileInfo> files = new ArrayList<FileInfo>();

  @Override
  public void init() throws Exception {
    setViewSelector("demo/demoFileUpload");
    buildList();
    this.form = buildForm();
    addWidget("uploadForm", this.form);
  }

  private void buildList() throws Exception {
    this.uploadList = new ListWidget<FileInfo>();
    this.uploadList.setDataProvider(new FileListDataProvider());
    this.uploadList.addField("originalFilename", "#Original filename");
    this.uploadList.addField("size", "#File size");
    this.uploadList.addField("contentType", "#Content Type");
    this.uploadList.addEmptyField("dummy");
  }

  private FormWidget buildForm() throws Exception {
    final FormWidget result = new FormWidget();

    result.addElement("encodingTest", "#encodingTest", new TextControl(), new StringData(), false);

    SelectControl<DisplayItem> selectControl = new SelectControl<DisplayItem>(DisplayItem.class, "label", "value");
    selectControl.addItem(null, "- choose -");
    selectControl.addItem("1", "one");
    selectControl.addItem("2", "two");

    FormElement<DisplayItem, String> selectElement = result.addElement("select", "#Select", selectControl,
        new StringData(), true);
    selectElement.setValue("1");
    selectElement.setDisabled(true);

    result.addElement("file", "#File", new FileUploadControl(), new FileInfoData(), false);
    result.addElement("upload", "#Upload file", new ButtonControl(new FileUploadButtonListener()));
    return result;
  }

  public void handleEventSelectFile(String param) throws Exception {
    FileInfo selectedFile = this.uploadList.getRowFromRequestId(param);
    getMessageCtx().showInfoMessage("Popup window with download content should have opened. If it did not, please "
            + "relax your popup blocker settings.");
    getPopupCtx().open(new FileDownloaderService(selectedFile), new PopupWindowProperties(), null);
  }

  public void handleEventValidate() throws Exception {
    this.form.convertAndValidate();
  }

  private class FileListDataProvider extends MemoryBasedListDataProvider<FileInfo> {

    public FileListDataProvider() {
      super(FileInfo.class);
    }

    @Override
    public List<FileInfo> loadData() throws Exception {
      return DemoFileUpload.this.files;
    }
  }

  private class FileUploadButtonListener implements OnClickEventListener {

    public void onClick() throws Exception {
      form.getElementByFullName("file").convertAndValidate();
      FileInfo fileInfo = (FileInfo) form.getValueByFullName("file");
      if (fileInfo != null && fileInfo.isFilePresent()) {
        if (!files.isEmpty()) {
          DemoFileUpload.this.addWidget("uploadList", uploadList);
        }
        files.add(fileInfo);
        form.setValueByFullName("file", null);

        // refresh the list data
        uploadList.getDataProvider().refreshData();
      }
    }
  }

}
