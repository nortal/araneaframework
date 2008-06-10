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

package org.araneaframework.jsp.tag.uilib.form.element.select;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementLabelTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.form.control.SelectControl;


/**
 * Standard form element label tag.
 * 
 * @author Marko Muts
 * 
 * @jsp.tag
 *   name = "radioSelectItemLabel"
 *   body-content = "JSP" 
 *   description = "Represents localizable label."
 */
public class FormRadioSelectItemLabelHtmlTag extends BaseFormElementLabelTag {
  protected String value;
  /** @since 1.1 */ 
  protected String radioId;

  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    // Type check
    //XXX: assertControlType("SelectControl");

    // Prepare  
    SelectControl.ViewModel viewModel = ((SelectControl.ViewModel)controlViewModel);

    writeLabel(out, viewModel.getLabelForValue(value));

    return EVAL_BODY_INCLUDE;    
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   rtexprvalue = "true"
   *   required = "false"
   *   description = "Select item value." 
   */
  public void setValue(String value) throws JspException  {
    this.value = (String)evaluateNotNull("value", value, String.class);
  }
  
  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   rtexprvalue = "true"
   *   required = "false"
   *   description = "The HTML id radio button to which this label belongs."
   * @since 1.1  
   */
  public void setRadioId(String radioId) throws JspException  {
    this.radioId = (String)evaluate("radioId", radioId, String.class);
  }
  
  /** @since 1.1 */
  public void writeLabel(Writer out, String label) throws Exception {
    JspUtil.writeOpenStartTag(out, "span");
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeCloseStartTag_SS(out);

    JspUtil.writeOpenStartTag(out, "label");
    JspUtil.writeAttribute(out, "for", radioId);
    JspUtil.writeCloseStartTag_SS(out);
    JspUtil.writeEscaped(out, label);
    JspUtil.writeEndTag_SS(out, "label");

    JspUtil.writeEndTag(out, "span");
  }
}
