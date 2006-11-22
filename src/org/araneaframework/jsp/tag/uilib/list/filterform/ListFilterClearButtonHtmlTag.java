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

package org.araneaframework.jsp.tag.uilib.list.filterform;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.uilib.form.element.FormLinkButtonHtmlTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.support.UiLibMessages;


/**
 * Tag that renders the {@link ListWidget}'s filter form clearance button.
 * Should be used inside list filter form (&lt;ui:listFilter&gt; tag).
 * Button uses label specified by {@link ListWidget}&mdash;{@link UiLibMessages#LIST_FILTER_CLEAR_BUTTON_LABEL}.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "clearFilterButton"
 *   body-content = "JSP"
 *   description = "Renders ListWidget's filter form clearance button. Should be used inside &lt;ui:listFilter&gt; tag. Button label identifier is UiLibMessages.LIST_FILTER_CLEAR_BUTTON_LABEL&mdash;'uilib.list.filter.clear.button'"
 */
public class ListFilterClearButtonHtmlTag extends FormLinkButtonHtmlTag {
  public static final String RENDER_BUTTON = "button";
  public static final String RENDER_INPUT = "input";
  public static final String RENDER_EMPTY = "empty";
  
  protected String renderMode;
	  
  public ListFilterClearButtonHtmlTag() {
    this.id = ListWidget.FILTER_RESET_BUTTON_ID;
    this.showLabel = false;
    this.validateOnEvent = true;
    this.style = "text-decoration: none";
    this.renderMode = ListFilterClearButtonHtmlTag.RENDER_BUTTON;
  }

  public int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    
    if (!renderMode.equals(ListFilterButtonHtmlTag.RENDER_EMPTY))
      writeNonEmpty(out);

    return EVAL_BODY_INCLUDE;    
  }
  
  protected void writeNonEmpty(Writer out) throws Exception {
    JspUtil.writeOpenStartTag(out, renderMode);
    // type must be 'button' for both <button> and <input>
    JspUtil.writeAttribute(out, "type", "button");

    if (renderMode.equals(ListFilterButtonHtmlTag.RENDER_INPUT)) {
      JspUtil.writeAttribute(out, "value", localizedLabel);
      JspUtil.writeCloseStartEndTag_SS(out);
    } else {
      JspUtil.writeCloseStartTag_SS(out);
      out.write(localizedLabel);
      JspUtil.writeEndTag_SS(out, renderMode);
    }
  }
  
  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Possible values are 'button', 'input' &mdash; clearance button is rendered with corresponding HTML tags, or 'empty' in which case JSP author must provide suitable content for this tag by themself(with an image, for example). Default rendermode is 'button'." 
   */
  public void setRenderMode(String renderMode) throws JspException {
	  if (!(renderMode.equals(ListFilterClearButtonHtmlTag.RENDER_BUTTON) || renderMode.equals(ListFilterClearButtonHtmlTag.RENDER_INPUT) || renderMode.equals(ListFilterButtonHtmlTag.RENDER_EMPTY)))
	      throw new AraneaJspException("<ui:clearFilterButton> 'renderMode' attribute must be '" + ListFilterClearButtonHtmlTag.RENDER_BUTTON + "' or '"+ ListFilterClearButtonHtmlTag.RENDER_INPUT+"'" + "' or '"+ ListFilterClearButtonHtmlTag.RENDER_EMPTY+"'");
      this.renderMode = (String) evaluate("renderMode", renderMode, String.class);
  }
}
