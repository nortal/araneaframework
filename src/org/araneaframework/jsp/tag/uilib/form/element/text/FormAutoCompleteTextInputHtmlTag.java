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

package org.araneaframework.jsp.tag.uilib.form.element.text;

import org.apache.commons.lang.StringUtils;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.event.OnChangeEventListener;
import org.araneaframework.uilib.form.control.AutoCompleteTextControl;

/**
 * Standard text input form element tag with autocompletion.
 * 
 * @author Steven Jentson (steven@webmedia.ee)
 * @author Taimo Peelo (taimo@araneaframework.org)
 * 
 * @jsp.tag 
 *   name = "autoCompleteTextInput" 
 * 	 body-content = "JSP" 
 *   description = "Form text input field, represents UiLib &quot;AutoCompleteTextControl&quot;."
 */
public class FormAutoCompleteTextInputHtmlTag extends BaseFormTextInputHtmlTag {

  protected String divClass = "autocompletediv";

  protected String localDataVar;

  @Override
  protected int doEndTag(Writer out) throws Exception {
    assertControlType("AutoCompleteTextControl");

    AutoCompleteTextControl.ViewModel viewModel = ((AutoCompleteTextControl.ViewModel) this.controlViewModel);

    Map<String, String> attributes = new HashMap<String, String>();
    attributes.put("autocomplete", "off");
    if (viewModel.getMaxLength() != null) {
      attributes.put("maxlength", viewModel.getMaxLength().toString());
    }

    if (this.onChangePrecondition == null) {
      this.onChangePrecondition = "return ";
    } else if (!this.onChangePrecondition.endsWith(";")) {
      this.onChangePrecondition += " return ";
    } else {
      this.onChangePrecondition += " && ";
    }

    this.onChangePrecondition += "!$('ACdiv." + getFullFieldId() + "').visible();";

    boolean events = this.events;
    this.events = false;
    writeTextInput(out, "text", true, attributes);
    this.events = events;

    JspUtil.writeOpenStartTag(out, "div");
    JspUtil.writeAttribute(out, "id", "ACdiv." + getFullFieldId());
    JspUtil.writeAttribute(out, "class", this.divClass);
    JspUtil.writeAttribute(out, "style", "display:none;");
    JspUtil.writeCloseStartTag(out);
    JspUtil.writeEndTag(out, "div");

    JspUtil.writeStartTag_SS(out, "script type=\"text/javascript\"");
   	out.write(constructACRegistrationScript(viewModel));
   	JspUtil.writeEndTag(out, "script");

    super.doEndTag(out);
    return EVAL_PAGE;
  }

  protected String constructACRegistrationScript(AutoCompleteTextControl.ViewModel viewModel) {
    StringBuffer script = new StringBuffer("Aranea.Behaviour.doAutoCompleteInputSetup('");
    script.append(getFullFieldId());
    script.append("',");

    if (viewModel.isOnChangeEventRegistered()) {
      script.append("'");
      script.append(OnChangeEventListener.ON_CHANGE_EVENT);
      script.append("',");
    } else {
      script.append("null,");
    }

    if (this.updateRegions != null && this.updateRegions.length() > 0) {
      script.append("'");
      script.append(this.updateRegions);
      script.append("',");
    } else {
      script.append("null,");
    }

    script.append("{minChars: ");
    script.append(String.valueOf(viewModel.getMinCompletionLength()));
    script.append("},");

    if (this.localDataVar != null) {
      script.append("'").append(this.localDataVar).append("'");
    } else if (viewModel.getLocalData() == null || viewModel.getLocalData().isEmpty()) {
      script.append(viewModel.isDataProviderExists());
    } else {
      script.append("[\"").append(StringUtils.join(viewModel.getLocalData(), "\",\"")).append("\"]");
    }

    script.append(");");

    return script.toString();
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Class attribute assigned to &lt;DIV&gt; inside which suggestions are shown."
   */
  public void setDivClass(String divClass) {
    this.divClass = evaluate("divClass", divClass, String.class);
  }

  /**
   * @jsp.attribute
   * type = "java.lang.String"
   * required = "false"
   * description = "If using local data for this autocomplete, one can define the JS data array in a
   *        JSP page, and make the autocomplete use that data array by reference. Therefore, one can
   *        provide the variable name that points to an array."
   */
  public void setLocalDataVar(String localDataVar) {
    this.localDataVar = evaluate("localDataVar", localDataVar, String.class);
  }
}
