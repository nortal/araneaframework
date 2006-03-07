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

import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.event.OnChangeEventListener;
import org.araneaframework.uilib.event.OnClickEventListener;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.control.FileUploadControl;
import org.araneaframework.uilib.form.control.SelectControl;
import org.araneaframework.uilib.form.data.FileInfoData;
import org.araneaframework.uilib.form.data.StringData;
import org.araneaframework.uilib.support.DisplayItem;
import org.araneaframework.uilib.support.FileInfo;

/**
 * @author Lauri Tulmin
 */
public class DemoFileUpload extends TemplateBaseWidget {
	private FormWidget editForm;

	private List files = new ArrayList();

	public void init() throws Exception {
		super.init();

		addGlobalEventListener(new ProxyEventListener(this));
		setViewSelector("demo/demoFileUpload");

		editForm = new FormWidget();

		SelectControl select = new SelectControl();
		select.addOnChangeEventListener(new OnChangeEventListener() {
			public void onChange() throws Exception {
			}
		});

		select.addItem(new DisplayItem(null, "- choose -"));
		select.addItem(new DisplayItem("1", "one"));
		select.addItem(new DisplayItem("2", "two"));

		editForm
				.addElement("select", "#Select", select, new StringData(), true);
		editForm.addElement("file", "#File", new FileUploadControl(),
				new FileInfoData(), false);

		ButtonControl button = new ButtonControl();
		button.addOnClickEventListener(new OnClickEventListener() {
			private static final long serialVersionUID = 0L;

			public void onClick() throws Exception {
				editForm.getElementByFullName("file").convertAndValidate();
				FileInfo fileInfo = (FileInfo) editForm
						.getValueByFullName("file");
				if (fileInfo != null) {
					files.add(fileInfo.getOriginalFilename());
					putViewData("files", files);
					editForm.setValueByFullName("file", null);
				}
			}
		});
		editForm.addElement("upload", "#Upload file", button, null, false);

		addWidget("editForm", editForm);
	}

	public void handleEventReturn(String param) throws Exception {
		getFlowCtx().cancel();
	}
}