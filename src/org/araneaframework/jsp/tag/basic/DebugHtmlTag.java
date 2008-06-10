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

package org.araneaframework.jsp.tag.basic;				

import java.io.Writer;
import java.util.Enumeration;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.BaseTag;
import org.araneaframework.jsp.util.JspUtil;


/**
 * Debug tag
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "debug"
 *   body-content = "JSP"
 *   description = "Outputs all attributes from the specified scope on screen. <br/>        
        Possible scopes are: 
        <ul>
          <li><i>application</i></li>
          <li><i>session</i></li>
          <li><i>request</i></li>
          <li><i>page</i></li>
        </ul>  
        The default scope is <i>page</i>."
 */

public class DebugHtmlTag extends BaseTag {
	public final static String APPLICATION_SCOPE = "application";
	public final static String SESSION_SCOPE = "session";
	public final static String REQUEST_SCOPE = "request";
	public final static String PAGE_SCOPE = "page";

	protected int scope = PageContext.PAGE_SCOPE;

	protected int doEndTag(Writer out) throws Exception {			
		// Output
		JspUtil.writeOpenStartTag(out, "table");
		JspUtil.writeAttribute(out, "border", "1");
		JspUtil.writeCloseStartTag(out);

		for(Enumeration i = pageContext.getAttributeNamesInScope(scope); i.hasMoreElements();) {
			String key = (String)i.nextElement();
			JspUtil.writeStartTag(out, "tr");
			JspUtil.writeStartTag(out, "td");
			JspUtil.writeEscaped(out, key);
			JspUtil.writeEndTag(out, "td");
			JspUtil.writeStartTag(out, "td");
			Object o = pageContext.getAttribute(key, scope);
			JspUtil.writeEscaped(out, o != null ? o.toString() : "<i>null</i>");
			JspUtil.writeEndTag(out, "td");
			JspUtil.writeEndTag(out, "tr");
		}

		JspUtil.writeEndTag(out, "table");    

		// Continue
		super.doEndTag(out);
		return EVAL_PAGE;      
	}
	
	/* ***********************************************************************************
	 * Tag attributes
	 * ***********************************************************************************/

	/**
	 *	@jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Attribute scope."
	 */
	public void setScope(String scope) throws JspException {
		String scopeString = (String)evaluateNotNull("scope", scope, String.class);

		if (APPLICATION_SCOPE.equals(scopeString))
			this.scope = PageContext.APPLICATION_SCOPE;
		else if (SESSION_SCOPE.equals(scopeString))
			this.scope = PageContext.SESSION_SCOPE;
		else if (REQUEST_SCOPE.equals(scopeString))
			this.scope = PageContext.REQUEST_SCOPE;
		else if (PAGE_SCOPE.equals(scopeString))
			this.scope = PageContext.PAGE_SCOPE;
		else
			throw new AraneaJspException("Wrong debug scope value '" + this.scope + "'");
	}
}
