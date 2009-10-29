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

package org.araneaframework.jsp.tag.presentation;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.util.JspUtil;

/**
 * Button base tag.
 * 
 * @author Oleg Mürk
 */
public class BaseButtonTag extends PresentationTag {

  private static final String RENDER_BUTTON = "button";

  private static final String RENDER_INPUT = "input";

  protected String id;

  protected String labelId;

  protected String onclick;

  protected String tabindex;

  protected String renderMode = RENDER_BUTTON;

  protected String getRenderMode() {
    return isInput() ? RENDER_INPUT : RENDER_BUTTON;
  }

  protected boolean isInput() {
    return this.renderMode.equals(RENDER_INPUT);
  }

  protected void writeButtonStartTag(Writer out) throws IOException {
    JspUtil.writeOpenStartTag(out, getRenderMode());
    if (isInput()) {
      JspUtil.writeAttribute(out, "type", "button");
      writeButtonLabel(out);
    }
  }

  protected void writeButtonCloseTag(Writer out, boolean endTag) throws IOException {
    if (!endTag) {
      if (isInput()) {
        JspUtil.writeCloseStartEndTag_SS(out);
      } else {
        JspUtil.writeCloseStartTag_SS(out);
      }
    } else if (!isInput()) {
      writeButtonLabel(out);
      JspUtil.writeEndTag(out, RENDER_BUTTON);
    }
  }

  private void writeButtonLabel(Writer out) throws IOException {
    if (!StringUtils.isEmpty(JspUtil.getResourceString(this.pageContext, this.labelId))) {
      String label = JspUtil.getResourceString(this.pageContext, this.labelId);
      if (isInput()) {
        JspUtil.writeAttribute(out, "value", label);
      } else {
        out.write(JspUtil.getResourceString(this.pageContext, label));
      }
    }
  }

  // Tag attributes

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Button id, allows to access button from JavaScript." 
   */
  public void setId(String id){
    this.id = evaluate("id", id, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Id of button label." 
   */
  public void setLabelId(String labelId){
    this.labelId = evaluate("labelId", labelId, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "true"
   *   description = "onClick Javascript action." 
   */
  public void setOnclick(String onclick){
    this.onclick = evaluate("onclick", onclick, String.class);
  }
  
  /**
   * @since 1.0.11
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "HTML tabindex for the button."
   */	
  public void setTabindex(String tabindex) throws JspException {
    this.tabindex = evaluateNotNull("tabindex", tabindex, String.class);
  }
  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Allowed values are (button | input) - the corresponding HTML tag will be used for rendering. Default is button."
   */
  public void setRenderMode(String renderMode) throws JspException {
    String tmpMode = evaluate("renderMode", renderMode, String.class);

    if (!RENDER_BUTTON.equals(tmpMode) && !RENDER_INPUT.equals(tmpMode)) {
      throw new AraneaJspException("<ui:basicButton> 'renderMode' attribute " + "must be '" + RENDER_BUTTON + "' or '"
          + RENDER_INPUT + "'");
    }

    this.renderMode = tmpMode;
  }
}
