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

package org.araneaframework.jsp.tag.uilib.form.element;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementHtmlTag;
import org.araneaframework.jsp.util.JspStringUtil;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.form.control.ButtonControl;

/**
 * Standard button form element base tag.
 * 
 * @author Oleg Mürk
 */
public class BaseFormButtonTag extends BaseFormElementHtmlTag {

  private static final String RENDER_BUTTON = "button";

  private static final String RENDER_INPUT = "input";

  public static final String RENDER_EMPTY = "empty";

  protected boolean showLabel = true;

  protected String onClickPrecondition;

  protected ButtonControl.ViewModel viewModel;

  protected String renderMode = RENDER_BUTTON;

  public BaseFormButtonTag() {
    setHasElementContextSpan(false);
  }

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    // Type check
    assertControlType("ButtonControl");

    // Prepare
    this.viewModel = ((ButtonControl.ViewModel) this.controlViewModel);

    // Check whether access key was specified in the resources
    if (this.accessKey == null) {
      this.accessKey = JspUtil.getResourceStringOrNull(this.pageContext, this.controlViewModel.getLabel()
          + ".access-key");

      if (!StringUtils.isEmpty(this.accessKey)) {
        this.accessKey = null;
      }
    }

    // Continue
    return EVAL_BODY_INCLUDE;
  }

  protected String getRenderMode() {
    return isInput() ? RENDER_INPUT : RENDER_BUTTON;
  }

  protected boolean isInput() {
    return this.renderMode.equals(RENDER_INPUT);
  }

  protected boolean isLink() {
    return this.renderMode.equals(RENDER_EMPTY);
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
    if (this.showLabel) {
      String label = this.localizedLabel;
      if (this.accessKey != null) {
        label = JspStringUtil.underlineAccessKey(StringEscapeUtils.escapeHtml(this.localizedLabel), this.accessKey);
      }

      if (!StringUtils.isEmpty(this.localizedLabel)) {
        if (isInput()) {
          JspUtil.writeAttribute(out, "value", label);
        } else {
          out.write(label);
        }
      }
    }
  }

  // Attributes

  /**
   * @jsp.attribute type = "java.lang.String" required = "false" description = "Whether button shows its label."
   */
  public void setShowLabel(String showLabel) {
    this.showLabel = showLabel.equalsIgnoreCase("true");
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Precondition for deciding whether go to server side or not."
   */
  public void setOnClickPrecondition(String onClickPrecondition) {
    this.onClickPrecondition = evaluate("onClickPrecondition", onClickPrecondition, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Allowed values are (button | input) - the corresponding HTML tag will be used for rendering. Default is button."
   */
  public void setRenderMode(String renderMode) throws JspException {
    this.renderMode = evaluate("renderMode", renderMode, String.class);

    if (!isModeValid(this.renderMode)) {
      throw new AraneaJspException("'renderMode' attribute must be '" + RENDER_BUTTON + "' or '" + RENDER_INPUT
          + "' or '" + RENDER_EMPTY + "'.");
    }
  }

  private boolean isModeValid(String mode) {
    return RENDER_BUTTON.equals(mode) || RENDER_INPUT.equals(mode) || RENDER_EMPTY.equals(mode);
  }
}
