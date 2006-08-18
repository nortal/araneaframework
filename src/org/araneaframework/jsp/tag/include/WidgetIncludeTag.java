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

package org.araneaframework.jsp.tag.include;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.OutputData;
import org.araneaframework.core.ApplicationComponent;
import org.araneaframework.jsp.tag.context.WidgetContextTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetUtil;

/**
 * Widget include tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "widgetInclude"
 *   body-content = "JSP"
 *   description = "The JSP specified by the path given in <i>page</i> is included as the widget with id specified in <i>id</i>."
 */
public class WidgetIncludeTag extends BaseIncludeTag {
	protected String widgetId;
	protected String page;
	
	public WidgetIncludeTag() {
		widgetId = null;
		page = null;
	}

	protected int doEndTag(Writer out) throws Exception {   
		ApplicationComponent.ApplicationWidget widget = JspWidgetUtil.getWidgetFromContext(widgetId, pageContext);
		
		WidgetContextTag widgetContextTag = new WidgetContextTag();
		
		registerSubtag(widgetContextTag);
		widgetContextTag.setId(widgetId);
		executeStartSubtag(widgetContextTag);
		
		OutputData output = getOutputData();
		
		try {
			if (page == null) {
				out.flush();
				widget._getWidget().render(output);
			}
			else {
				JspUtil.include(pageContext, "/widgets/" + page);
			}
		}
		finally {		
			executeEndTagAndUnregister(widgetContextTag);
		}
		
		super.doEndTag(out);
		return EVAL_PAGE;
	}	

	/* ***********************************************************************************
	 * Tag attributes
	 * ***********************************************************************************/

	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Widget id." 
	 */
	public void setId(String widgetId) throws JspException {
		this.widgetId = (String)evaluateNotNull("widgetId", widgetId, String.class);		
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Path to JSP." 
	 */
	public void setPage(String page) throws JspException {
		this.page = (String)evaluate("page", page, String.class);
	}
}
