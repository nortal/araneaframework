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

package org.araneaframework.jsp.tag.form;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.jsp.JspException;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.framework.SystemFormContext;
import org.araneaframework.http.ClientStateContext;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.http.JspContext;
import org.araneaframework.jsp.util.JspUtil;


/**
 * System form tag. System form maps into HTML form. 
 * Canonical use in Aranea - surrounding all body elements. 
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "systemForm"
 *   description = "Puts an HTML <i>form</i> tag with parameters needed by Aranea."
 */
public class AraneaSystemFormHtmlTag extends BaseSystemFormHtmlTag {  
  private JspContext config;
  
  protected int doStartTag(Writer out) throws Exception {
    config = (JspContext) getEnvironment().requireEntry(JspContext.class);

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
  
  protected int doEndTag(Writer out) throws Exception {
    ClientStateContext ctx= (ClientStateContext) getEnvironment().getEntry(ClientStateContext.class);
    ctx.addSystemFormState();
    writeSystemFormFields(out);
    return super.doEndTag(out);
  }

  private void writeSystemFormFields(Writer out) throws JspException, IOException {
    SystemFormContext systemFormContext = (SystemFormContext) getEnvironment().requireEntry(SystemFormContext.class);
    for (Iterator i = systemFormContext.getFields().entrySet().iterator(); i.hasNext(); ) {
      Map.Entry entry = (Map.Entry) i.next();
      JspUtil.writeHiddenInputElement(out, (String) entry.getKey(), (String) entry.getValue());
    }
  }
  
  /* ***********************************************************************************
   * Implementation of SystemForm abstract methods
   * ***********************************************************************************/

  protected String getAcceptCharset() {
    return config.getSubmitCharset();
  }

  protected String getFormAction() throws JspException {
    return ((HttpInputData) getOutputData().getInputData()).getContainerURL();
  }
}
