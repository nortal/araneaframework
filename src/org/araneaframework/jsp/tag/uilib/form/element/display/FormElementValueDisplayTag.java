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

package org.araneaframework.jsp.tag.uilib.form.element.display;

import java.io.Writer;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementDisplayTag;
import org.araneaframework.uilib.form.control.DisplayControl;

/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 * @jsp.tag
 *  name = "valueDisplay"
 *  body-content = "JSP"
 *  description = "Puts the element value in page scope variable, represents UiLib 'DisplayControl'."
 */
public class FormElementValueDisplayTag extends BaseFormElementDisplayTag {

  protected String var;

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "true"
   *    description = "Name of the page scope variable to put the element value into."
   */
  public void setVar(String var) {
    this.var = var;
  }

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    assertControlType("DisplayControl");

    DisplayControl.ViewModel viewModel = (DisplayControl.ViewModel) this.controlViewModel;

    // Store data
    addContextEntry(this.var, viewModel.getValue());

    return EVAL_BODY_INCLUDE;
  }

}
