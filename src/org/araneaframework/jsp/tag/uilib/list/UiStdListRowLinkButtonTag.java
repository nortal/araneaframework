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

package org.araneaframework.jsp.tag.uilib.list;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.jsp.JspException;
import org.araneaframework.OutputData;
import org.araneaframework.core.StandardWidget;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.container.StandardWidgetContainerWidget;
import org.araneaframework.jsp.tag.aranea.UiAraneaRootTag;
import org.araneaframework.jsp.tag.basic.UiAttributedTagInterface;
import org.araneaframework.jsp.util.UiStdWidgetCallUtil;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.servlet.ThreadCloningContext;
import org.araneaframework.servlet.util.ClientStateUtil;
import org.araneaframework.servlet.util.URLUtil;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 * 
 * @jsp.tag
 *   name = "listRowLinkButton"
 *   body-content = "JSP"
 *   description = "Represents a link with an onClick JavaScript event."
 */
public class UiStdListRowLinkButtonTag extends UiListRowButtonBaseTag {
	{
		baseStyleClass = "aranea-link-button";
	}

	protected int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);
		
	    Map parameters = getParameterMap();
	    StringBuffer url = getRequestURL();
	    
	    addContextEntry(UiAttributedTagInterface.HTML_ELEMENT_KEY, id);

		UiUtil.writeOpenStartTag(out, "a");
		UiUtil.writeAttribute(out, "id", id);
		UiUtil.writeAttribute(out, "class", getStyleClass());
		UiUtil.writeAttribute(out, "style", getStyle());
		UiUtil.writeAttribute(out, "border", "0");
		UiUtil.writeAttribute(out, "href", URLUtil.parametrizeURI(url.toString(), parameters));

		if (eventId != null)
			UiStdWidgetCallUtil.writeEventAttributeForEvent(
					pageContext,
					out, 
					"onclick", 
					systemFormId, 
					contextWidgetId, 
					eventId, 
					eventParam, 
					onClickPrecondition,
					updateRegionNames);      
		
		UiUtil.writeCloseStartTag_SS(out);    
		
		return EVAL_BODY_INCLUDE;    
	}    
	
	protected int doEndTag(Writer out) throws Exception {
		if (localizedLabel != null)
			UiUtil.writeEscaped(out, localizedLabel);
		UiUtil.writeEndTag(out, "a");
		
		return super.doEndTag(out);
	}
	
  protected Map getParameterMap() throws JspException {
    OutputData output = (OutputData) requireContextEntry(UiAraneaRootTag.OUTPUT_DATA_KEY);
    Map state = (Map)output.getAttribute(ClientStateUtil.SYSTEM_FORM_STATE);
    Object threadId = state.get(ThreadContext.THREAD_SERVICE_KEY);

    Map result = new HashMap();
    result.put(ThreadContext.THREAD_SERVICE_KEY, threadId);
    result.put(StandardWidgetContainerWidget.EVENT_PATH_KEY, contextWidgetId);
    result.put(StandardWidget.EVENT_HANDLER_ID_KEY, eventId);
    result.put(StandardWidget.EVENT_PARAMETER_KEY, eventParam);
    result.put(ThreadCloningContext.CLONING_REQUEST_KEY, "true");

    return result;
  }
}
