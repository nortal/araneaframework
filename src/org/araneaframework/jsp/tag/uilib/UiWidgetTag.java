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

package org.araneaframework.jsp.tag.uilib;				

import java.io.Writer;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.araneaframework.core.Custom;
import org.araneaframework.jsp.UiException;
import org.araneaframework.jsp.container.UiWidgetContainer;
import org.araneaframework.jsp.tag.UiBaseTag;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.jsp.util.UiWidgetUtil;


/**
 * Widget base tag.
 * 
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 * 
 * @jsp.tag
 *   name = "widget"
 *   body-content = "JSP"
 *   description = "UiLib widget tag. <br/> 
           Makes available following page scope variables: 
           <ul>
             <li><i>widget</i> - UiLib widget view model.
           </ul> "
 */
public class UiWidgetTag extends UiBaseTag {
	/**
	 * Widget full dot-separated identifier starting from container (e.g. component).
	 */
	public final static String FULL_ID_KEY_REQUEST = "widgetFullId";
	/**
	 * Widget full dot-separated identifier with added unique container identifier prefix (e.g. component id).
	 */
	public final static String SCOPED_FULL_ID_KEY_REQUEST = "widgetScopedFullId";    
	/**
	 * Widget view model.
	 */
	public final static String VIEW_MODEL_KEY_REQUEST = "widget";  
	  
  protected String id;
  protected String fullId;
  protected String scopedFullId;  
  protected Custom.CustomWidget widget;
  protected Custom.WidgetViewModel viewModel;
  
  protected UiWidgetContainer container;
  
	//
  // Attributes
  //
  
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "UiLib widget id." 
	 */
	public void setId(String id) throws JspException {
		this.id = (String)evaluateNotNull("id", id, String.class);
	}

  
  //
  // Implementation
  //
  
  public int before(Writer out) throws Exception {
		super.before(out);
				
        container = (UiWidgetContainer) readAttribute(UiWidgetContainer.REQUEST_CONTEXT_KEY, PageContext.REQUEST_SCOPE);
        
		// Get data
        widget = UiWidgetUtil.getWidgetFromContext(id, pageContext);
        viewModel = (Custom.WidgetViewModel) widget._getViewable().getViewModel();
		fullId = UiWidgetUtil.getWidgetFullIdFromContext(id, pageContext);		
		
		if (fullId == null) 
			throw new UiException("Widget must have an id!");				
        
        scopedFullId = container.scopeWidgetFullId(pageContext, fullId);
		
		// Set variables
		pushAttribute(FULL_ID_KEY_REQUEST, fullId, PageContext.REQUEST_SCOPE);
		pushAttribute(SCOPED_FULL_ID_KEY_REQUEST, scopedFullId, PageContext.REQUEST_SCOPE);		
		pushAttribute(VIEW_MODEL_KEY_REQUEST, viewModel, PageContext.REQUEST_SCOPE);
		writeJavascript(out);
		
		// Continue
	  return EVAL_BODY_INCLUDE;		
	}
  
  public void init() {
    super.init();
    id = null;
    fullId = null;
    viewModel = null;
    widget = null;
  }

  /**
   * Write javascript that accompanies this tag.
   */
  protected void writeJavascript(Writer out) throws Exception{
    UiUtil.writeStartTag(out, "script");
    out.write("uiWidgetContext(");
    UiUtil.writeScriptString(out, scopedFullId);
    out.write(");\n");
    UiUtil.writeEndTag(out, "script");
  }
}
