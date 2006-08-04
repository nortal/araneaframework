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

package org.araneaframework.jsp.tag.uilib.form;        

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.BaseTag;
import org.araneaframework.jsp.util.JspWidgetUtil;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;


/**
 * Specifies form element context for inner tags. 
 * Form element view model, id and value are made accessible to inner tags as EL variables.
 * 
 * @author Jevgeni Kabanov
 * 
 * @jsp.tag
 *   name = "formElement"
 *   body-content = "JSP"
 *   description = "UiLib form element tag.  <br/>                 
           Makes available following page scope variables: 
           <ul>
             <li><i>formElement</i> - UiLib form element view model.
             <li><i>formElementId</i> - UiLib form element id.
             <li><i>formElementValue</i> - UiLib form element value.
           </ul> "
 */
public class FormElementTag extends BaseTag {
  public static final String ID_KEY = "formElementId";
  public static final String VIEW_MODEL_KEY = "formElement";
  public static final String VALUE_KEY = "formElementValue";

  protected String id;
  protected FormElement.ViewModel formElementViewModel;

  public int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    // Get form data    
    FormWidget form = (FormWidget)requireContextEntry(FormTag.FORM_KEY);

    // Get form element
    formElementViewModel = 
      (FormElement.ViewModel) JspWidgetUtil.traverseToSubWidget(form, id)._getViewable().getViewModel();   

    // Store data
    addContextEntry(VIEW_MODEL_KEY, formElementViewModel);
    addContextEntry(ID_KEY, id);
    addContextEntry(VALUE_KEY, formElementViewModel.getValue());

    // Continue
    return EVAL_BODY_INCLUDE;
  }

  /* ***********************************************************************************
   * Tag attributes
   * ***********************************************************************************/

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "true"
   *   description = "UiLib form element id." 
   */
  public void setId(String id) throws JspException {
    this.id = (String)evaluateNotNull("id", id, String.class);
  }
}
