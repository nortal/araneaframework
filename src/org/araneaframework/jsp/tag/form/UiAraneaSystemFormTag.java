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

import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.araneaframework.OutputData;
import org.araneaframework.core.StandardWidget;
import org.araneaframework.framework.container.StandardWidgetContainerWidget;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.servlet.core.StandardServletServiceAdapterComponent;
import org.araneaframework.servlet.filter.StandardJspFilterService;
import org.araneaframework.servlet.util.ClientStateUtil;


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
public class UiAraneaSystemFormTag extends UiSystemFormTag {  
  private OutputData output;
  private StandardJspFilterService.JspConfiguration config;
  
  protected int doStartTag(Writer out) throws Exception {
    output = 
      (OutputData) pageContext.getRequest().getAttribute(
          StandardServletServiceAdapterComponent.OUTPUT_DATA_REQUEST_ATTRIBUTE);

    config = 
      (StandardJspFilterService.JspConfiguration) output.getAttribute(
          StandardJspFilterService.JSP_CONFIGURATION_KEY);

    super.doStartTag(out);
    
    // Hidden fields: preset
    Map state = (Map)output.getAttribute(ClientStateUtil.SYSTEM_FORM_STATE);
    if (state != null) {
	    for (Iterator iter = state.entrySet().iterator(); iter.hasNext();) {
			Entry element = (Entry) iter.next();
	        UiUtil.writeHiddenInputElement(out, (String)element.getKey(), (String)element.getValue());
		}
    }
    
    // Hidden fields: to be set
    UiUtil.writeHiddenInputElement(out, StandardWidget.EVENT_HANDLER_ID_KEY, "");
    UiUtil.writeHiddenInputElement(out, StandardWidgetContainerWidget.EVENT_PATH_KEY, "");
    UiUtil.writeHiddenInputElement(out, StandardWidget.EVENT_PARAMETER_KEY, "");

    // Continue
    return EVAL_BODY_INCLUDE;
  }
  
  /* ***********************************************************************************
   * Implementation of SystemForm abstract methods
   * ***********************************************************************************/

  protected String getAcceptCharset() {
    return config.getSubmitCharset();
  }

  protected String getFormAction() {
    return ((HttpServletRequest) pageContext.getRequest()).getContextPath() + ((HttpServletRequest) pageContext.getRequest()).getServletPath();
  }
}
