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

package org.araneaframework.example.main.web.demo.advanced;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.http.service.FileDownloaderService;
import org.araneaframework.http.support.PopupWindowProperties;
import org.araneaframework.http.widget.FileDownloaderWidget.FileDownloadStreamCallback;
import org.araneaframework.uilib.event.OnClickEventListener;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.control.DefaultSelectControl;
import org.araneaframework.uilib.form.control.FileUploadControl;
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

  private static final String FILE_INPUT = "file";
  private static final String FILE_LIST = "uploadList";

  private FormWidget form;

  private ListWidget<FileInfo> uploadList;

  private List<FileInfo> files = new ArrayList<FileInfo>();

  @Override
  public void init() throws Exception {
    setViewSelector("demo/advanced/fileUpload");
    buildList();
    addWidget("uploadForm", this.form = buildForm());
  }

  private void buildList() throws Exception {
    this.uploadList = new ListWidget<FileInfo>();
    this.uploadList.setDataProvider(new FileListDataProvider());
    this.uploadList.addField("originalFilename", "fileUpload.originalName");
    this.uploadList.addField("size", "fileUpload.size");
    this.uploadList.addField("contentType", "fileUpload.contentType");
    this.uploadList.addEmptyField("dummy");
  }

  private FormWidget buildForm() throws Exception {
    final FormWidget result = new FormWidget();

    result.addElement("encodingTest", "fileUpload.encodingTest", new TextControl(), new StringData(), false);

    DefaultSelectControl selectControl = new DefaultSelectControl();
    selectControl.addItem("select.choose", null);
    selectControl.addItem("select.one", "1");
    selectControl.addItem("select.two", "2");

    FormElement<DisplayItem, String> selectElement = result.addElement("select", "fileUpload.select", selectControl,
        new StringData(), true);
    selectElement.setValue("1");
    selectElement.setDisabled(true);

    result.addElement(FILE_INPUT, "fileUpload.file", new FileUploadControl(), new FileInfoData(), false);
    result.addElement("upload", "fileUpload.upload", new ButtonControl(new FileUploadButtonListener()));
    return result;
  }

  public void handleEventSelectFile(String param) throws Exception {
    FileInfo selectedFile = this.uploadList.getRowFromRequestId(param);
    getMessageCtx().showInfoMessage("common.popupBlockMsg");
    DownloadFileProvider provider = new DownloadFileProvider(selectedFile);
    getPopupCtx().open(new FileDownloaderService(provider), new PopupWindowProperties(), null);
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
      // We only convert the the upload control:
      form.getElementByFullName(FILE_INPUT).convertAndValidate();

      FileInfo fileInfo = (FileInfo) form.getValueByFullName(FILE_INPUT);
      if (fileInfo != null && fileInfo.isFilePresent()) {
        if (files.isEmpty()) { // It means: is file is first? 
          DemoFileUpload.this.addWidget(FILE_LIST, uploadList);
        }

        files.add(fileInfo);
        form.setValueByFullName(FILE_INPUT, null);

        // refresh the list data
        uploadList.getDataProvider().refreshData();
      }
    }
  }

  private class DownloadFileProvider extends FileDownloadStreamCallback {

    private FileInfo file;

    public DownloadFileProvider(FileInfo file) {
      this.file = file;
    }

    @Override
    public InputStream getStreamToDownload() {
      return this.file.getFileStream();
    }

    @Override
    public String getContentType() {
      return this.file.getContentType();
    }

    @Override
    public int getLength() {
      return (int) this.file.getSize();
    }

    @Override
    public String getFileName() {
      return this.file.getFileName();
    }
  }
}
