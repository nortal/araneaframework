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
import java.util.List;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.http.PopupWindowContext;
import org.araneaframework.http.service.FileDownloaderService;
import org.araneaframework.http.support.PopupWindowProperties;
import org.araneaframework.uilib.event.OnChangeEventListener;
import org.araneaframework.uilib.event.OnClickEventListener;
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
 * @author Lauri Tulmin
 * @author Taimo Peelo (taimo@araneaframework.org) 
 */
public class DemoFileUpload extends TemplateBaseWidget {
	  private static final long serialVersionUID = 1L;
  private FormWidget form;
	private ListWidget uploadList;

	private List files = new ArrayList();

	public void init() throws Exception {
		super.init();

		setViewSelector("demo/demoFileUpload");

		buildList();

		form = buildForm();
		addWidget("uploadForm", form);
	}

	private void buildList() throws Exception {
		uploadList = new ListWidget();
		addWidget("uploadForm", uploadList);
		uploadList.setDataProvider(new FileListDataProvider());
		uploadList.addField("originalFilename", "#Original filename");
		uploadList.addField("size", "#File size");
		uploadList.addField("contentType", "#Content Type");
		uploadList.addField("dummy", null, false);
	}
	
	private FormWidget buildForm() throws Exception {
		final FormWidget result = new FormWidget();
		
		result.addElement("encodingTest", "#encodingTest", new TextControl(), new StringData(), false);

		SelectControl select = new SelectControl();
		select.addOnChangeEventListener(new OnChangeEventListener() {
			      private static final long serialVersionUID = 1L;

      public void onChange() throws Exception {
			}
		});

		select.addItem(new DisplayItem(null, "- choose -"));
		select.addItem(new DisplayItem("1", "one"));
		select.addItem(new DisplayItem("2", "two"));

		result.addElement("select", "#Select", select, new StringData(), true);
		result.addElement("file", "#File", new FileUploadControl(),
				new FileInfoData(), false);

		ButtonControl button = new ButtonControl();
		button.addOnClickEventListener(new FileUploadButtonListener());
		result.addElement("upload", "#Upload file", button, null, false);
		
		return result;
	}

	public void handleEventSelectFile(String param) throws Exception {
		FileInfo selectedFile = (FileInfo) uploadList.getRowFromRequestId(param);
		
		getMessageCtx().showInfoMessage("Popup window with download content should have opened. If it did not, please relax your popup blocker settings.");
		FileDownloaderService service = new FileDownloaderService(selectedFile);
		
		PopupWindowContext popupContext = (PopupWindowContext) getEnvironment().getEntry(PopupWindowContext.class);
		PopupWindowProperties p = new PopupWindowProperties();
		popupContext.open(service, p, null);
	}

	// INNER CLASSES
	
	private class FileListDataProvider extends MemoryBasedListDataProvider {
		    private static final long serialVersionUID = 1L;

    public FileListDataProvider() {
			super(FileInfo.class);
		}

		public List loadData() throws Exception {
			return files;
		}
	}
	
	private class FileUploadButtonListener implements OnClickEventListener {
		    private static final long serialVersionUID = 1L;

    public void onClick() throws Exception {
			form.getElementByFullName("file").convertAndValidate();
			FileInfo fileInfo = (FileInfo) form.getValueByFullName("file");
			if (fileInfo != null && !fileInfo.getOriginalFilename().trim().equals("") && fileInfo.getSize() > 0)  {
				if (files.size() == 0) {
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