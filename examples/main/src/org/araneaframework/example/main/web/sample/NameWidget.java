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

package org.araneaframework.example.main.web.sample;

import org.araneaframework.uilib.core.BaseUIWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.StringData;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class NameWidget extends BaseUIWidget {
  private static final long serialVersionUID = 1L;
  private FormWidget form;
  
  protected void init() throws Exception {
	setViewSelector("sample/nameForm");

	form = new FormWidget();
    form.addElement("name", "#Enter your name", new TextControl(), new StringData(), true);
	addWidget("form", form);
  }

  public void handleEventReturn() throws Exception {
    if (form.convertAndValidate()) {
      getFlowCtx().finish(form.getValueByFullName("name"));
    }
  }
}
