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
import org.araneaframework.jsp.tag.aranea.AraneaRootTag;
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;
import org.araneaframework.jsp.util.JspWidgetCallUtil;
import org.araneaframework.jsp.util.JspUtil;
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
public class ListRowLinkButtonHtmlTag extends BaseListRowButtonTag {
	{
		baseStyleClass = "aranea-link-button";
	}

	protected int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);
		
	    Map parameters = getParameterMap();
	    StringBuffer url = getRequestURL();
	    
	    addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, id);

		JspUtil.writeOpenStartTag(out, "a");
		JspUtil.writeAttribute(out, "id", id);
		JspUtil.writeAttribute(out, "class", getStyleClass());
		JspUtil.writeAttribute(out, "style", getStyle());
		JspUtil.writeAttribute(out, "border", "0");
		JspUtil.writeAttribute(out, "href", URLUtil.parametrizeURI(url.toString(), parameters));

		if (eventId != null)
			JspWidgetCallUtil.writeEventAttributeForEvent(
					pageContext,
					out, 
					"onclick", 
					systemFormId, 
					contextWidgetId, 
					eventId, 
					eventParam, 
					onClickPrecondition,
					updateRegionNames);      
		
		JspUtil.writeCloseStartTag_SS(out);    
		
		return EVAL_BODY_INCLUDE;    
	}    
	
	protected int doEndTag(Writer out) throws Exception {
		if (localizedLabel != null)
			JspUtil.writeEscaped(out, localizedLabel);
		JspUtil.writeEndTag(out, "a");
		
		return super.doEndTag(out);
	}
	
  protected Map getParameterMap() throws JspException {
    OutputData output = (OutputData) requireContextEntry(AraneaRootTag.OUTPUT_DATA_KEY);
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
