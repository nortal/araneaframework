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

import org.araneaframework.http.core.FileDownloadActionListener;

import org.apache.commons.lang.StringUtils;

import org.araneaframework.InputData;
import org.araneaframework.OutputData;

import java.util.ArrayList;
import java.util.List;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.http.service.FileDownloaderService;
import org.araneaframework.http.support.PopupWindowProperties;
import org.araneaframework.uilib.event.OnChangeEventListener;
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

  private static final long serialVersionUID = 1L;

  private FormWidget form;

  private ListWidget uploadList;

  private List files = new ArrayList();

  public void init() throws Exception {
    setViewSelector("demo/demoFileUpload");
    addWidget("uploadForm", buildForm());
    buildList();
    addActionListener("download", new FileDownloadActionListener(getPopupCtx(),
        new DemoFileHandler()));
  }

  private void buildList() throws Exception {
    this.uploadList = new ListWidget();
    this.uploadList.setDataProvider(new FileListDataProvider());
    this.uploadList.addField("originalFilename", "#Original filename");
    this.uploadList.addField("size", "#File size");
    this.uploadList.addField("contentType", "#Content Type");
    this.uploadList.addEmptyField("dummy", null);
  }

  private FormWidget buildForm() throws Exception {
    this.form = new FormWidget();
    this.form.addElement("encodingTest", "#encodingTest", new TextControl(), new StringData(), false);

    SelectControl selectControl = new SelectControl();
    selectControl.addOnChangeEventListener(new OnChangeEventListener() {

      public void onChange() throws Exception {}
    });

    selectControl.addItem(new DisplayItem(null, "- choose -"));
    selectControl.addItem(new DisplayItem("1", "one"));
    selectControl.addItem(new DisplayItem("2", "two"));

    this.form.addElement("select", "#Select", selectControl, new StringData(), true);
    FormElement selectElement = (FormElement) this.form.getElement("select");
    selectElement.setValue("1");
    selectElement.setDisabled(true);

    this.form.addElement("file", "#File", new FileUploadControl(), new FileInfoData(), false);

    ButtonControl button = new ButtonControl();
    button.addOnClickEventListener(new FileUploadButtonListener());
    this.form.addElement("upload", "#Upload file", button, null, false);

    return this.form;
  }

  public void handleEventSelectFile(String param) throws Exception {
    FileInfo selectedFile = (FileInfo) this.uploadList.getRowFromRequestId(param);

    getMessageCtx().showInfoMessage(
        "Popup window with download content should have opened. "
            + "If it did not, please relax your popup blocker settings.");

    FileDownloaderService service = new FileDownloaderService(selectedFile);
    service.setContentDispositionInline(false);

    PopupWindowProperties p = new PopupWindowProperties();
    getPopupCtx().open(service, p, null);
  }

  public void handleEventValidate() throws Exception {
    this.form.convertAndValidate();
  }

  // INNER CLASSES

  private class FileListDataProvider extends MemoryBasedListDataProvider {

    public FileListDataProvider() {
      super(FileInfo.class);
    }

    public List loadData() throws Exception {
      return files;
    }
  }

  private class FileUploadButtonListener implements OnClickEventListener {

    public void onClick() throws Exception {
      form.getElementByFullName("file").convertAndValidate();
      FileInfo fileInfo = (FileInfo) form.getValueByFullName("file");

      if (fileInfo != null && !StringUtils.isBlank(fileInfo.getFileName())
          && fileInfo.getSize() > 0) {

        if (files.isEmpty()) {
          DemoFileUpload.this.addWidget("uploadList", uploadList);
        }

        files.add(fileInfo);
        form.setValueByFullName("file", null);

        // refresh the list data
        uploadList.getDataProvider().refreshData();
      }
    }
  }

  private class DemoFileHandler extends FileDownloadActionListener.FileDownloadHandler {

    protected void prepareServiceData(String actionId, String actionParam, InputData input,
        OutputData output) {

      if (actionParam != null) {
        FileInfo file = (FileInfo) uploadList.getRowFromRequestId(actionParam);
        this.readData(file);
      }
    }
  }

}
