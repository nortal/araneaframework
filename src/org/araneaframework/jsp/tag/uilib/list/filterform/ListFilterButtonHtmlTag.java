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
import org.araneaframework.jsp.tag.uilib.form.FormEnterKeyboardHandlerHtmlTag;
import org.araneaframework.jsp.tag.uilib.form.FormTag;
import org.araneaframework.jsp.tag.uilib.form.element.FormLinkButtonHtmlTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.support.UiLibMessages;

/**
 * Tag that renders the {@link ListWidget}'s filter form filtering button.
 * Should be used inside list filter form (&lt;ui:listFilter&gt; tag).
 * Button uses label specified by {@link ListWidget}&mdash;{@link UiLibMessages#LIST_FILTER_BUTTON_LABEL}.
 * Also registers enter keyboard handler, so that ENTER key triggers filtering
 * from any filter form field.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "listFilterButton"
 *   body-content = "JSP"
 *   description = "Renders ListWidget's filter form filtering button. Should be used inside &lt;ui:listFilter&gt; tag. Button label identifier is UiLibMessages.LIST_FILTER_BUTTON_LABEL&mdash;'uilib.list.filter.button'"
 *
 * @since 1.0.3  
 */
public class ListFilterButtonHtmlTag extends FormLinkButtonHtmlTag {
  public static final String RENDER_BUTTON = "button";
  public static final String RENDER_INPUT = "input";
  public static final String RENDER_EMPTY = "empty";
	
  protected String renderMode;

  public ListFilterButtonHtmlTag() {
    this.id = ListWidget.FILTER_BUTTON_ID;
    this.showLabel = false;
    this.validateOnEvent = true;
    this.style = "text-decoration: none";
    this.renderMode = ListFilterButtonHtmlTag.RENDER_BUTTON;
  }

  public int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    if (!renderMode.equals(ListFilterButtonHtmlTag.RENDER_EMPTY))
      writeNonEmpty(out);
    registerEnterKeyboardHandler();
    
    return EVAL_BODY_INCLUDE;    
  }

  protected void registerEnterKeyboardHandler() throws JspException {
    FormEnterKeyboardHandlerHtmlTag tag = new FormEnterKeyboardHandlerHtmlTag();
    tag.setFullElementId((String)requireContextEntry(FormTag.FORM_FULL_ID_KEY)+"."+id);
    registerSubtag(tag);
    executeStartSubtag(tag);
    executeEndTagAndUnregister(tag);
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
   *   description = "Possible values are 'button', 'input' &mdash; filter button is rendered with corresponding HTML tags, or 'empty' in which case JSP author must provide suitable content for this tag by themself (with an image, for example). Default rendermode is 'button'." 
   */
  public void setRenderMode(String renderMode) throws JspException {
    String tmpMode = (String) evaluate("renderMode", renderMode, String.class);

    if (!(RENDER_BUTTON.equals(tmpMode) || RENDER_INPUT.equals(tmpMode)
        || RENDER_EMPTY.equals(tmpMode))) {
      throw new AraneaJspException("<ui:listFilterButton> 'renderMode' "
          + "attribute must be '" + RENDER_BUTTON + "' or '" + RENDER_INPUT
          + "' or '" + RENDER_EMPTY + "'");
    }

    this.renderMode = tmpMode;
  }
}
