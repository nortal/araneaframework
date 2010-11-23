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

package org.araneaframework.jsp.tag.form;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.framework.SystemFormContext;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.http.JspContext;
import org.araneaframework.jsp.util.JspUtil;

/**
 * System form tag. System form maps into HTML form. Canonical use in Aranea - surrounding all body elements.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag name = "systemForm" description = "Puts an HTML <i>form</i> tag with parameters needed by Aranea."
 */
public class AraneaSystemFormHtmlTag extends BaseSystemFormHtmlTag {

  private JspContext config;

  @Override
  protected int doStartTag(Writer out) throws Exception {
    this.config = getEnvironment().requireEntry(JspContext.class);

    super.doStartTag(out);

    // Hidden fields: preset
    writeSystemFormFields(out);

    // Hidden fields: to be set
    JspUtil.writeHiddenInputElement(out, ApplicationWidget.EVENT_HANDLER_ID_KEY, "");
    JspUtil.writeHiddenInputElement(out, ApplicationWidget.EVENT_PATH_KEY, "");
    JspUtil.writeHiddenInputElement(out, ApplicationWidget.EVENT_PARAMETER_KEY, "");

    // Continue
    return EVAL_BODY_INCLUDE;
  }

  private void writeSystemFormFields(Writer out) throws IOException {
    SystemFormContext systemFormContext = getEnvironment().requireEntry(SystemFormContext.class);
    for (Map.Entry<String, String> element : systemFormContext.getFields().entrySet()) {
      JspUtil.writeHiddenInputElement(out, element.getKey(), element.getValue());
    }
  }

  @Override
  protected String getAcceptCharset() {
    return this.config.getSubmitCharset();
  }

  @Override
  protected String getFormAction() {
    return ((HttpInputData) getOutputData().getInputData()).getContainerURL();
  }
}
