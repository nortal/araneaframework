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
import org.araneaframework.jsp.exception.MissingFormElementIdAraneaJspException;
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetUtil;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;


/**
 * Base form element label tag.
 * 
 * @author Oleg Mürk
 */
public class BaseFormElementLabelTag extends PresentationTag {
  protected FormWidget.ViewModel formViewModel;

  protected FormElement.ViewModel formElementViewModel;
  protected Control.ViewModel controlViewModel;
  protected String localizedLabel;
  protected String accessKeyId;  
  protected String derivedId;
  
  //Attributes
  
  protected String id;
  protected boolean showMandatory = true;
  protected boolean showColon = true;
  protected String accessKey;  

  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    // Get form data    
    formViewModel = (FormWidget.ViewModel)requireContextEntry(FormTag.FORM_VIEW_MODEL_KEY);
    FormWidget form = 
      (FormWidget)JspUtil.requireContextEntry(pageContext, FormTag.FORM_KEY);

    //In case the tag is in formElement tag
    derivedId = id;
    if (derivedId == null && getContextEntry(FormElementTag.ID_KEY) != null) 
      derivedId = (String) getContextEntry(FormElementTag.ID_KEY);

    if (derivedId == null) 
      throw new MissingFormElementIdAraneaJspException(this);

    formElementViewModel = 
      (FormElement.ViewModel) JspWidgetUtil.traverseToSubWidget(form, derivedId)._getViewable().getViewModel();   

    // Get control  
    controlViewModel = (formElementViewModel).getControl();
    localizedLabel = JspUtil.getResourceString(pageContext, formElementViewModel.getLabel());                  

    if (accessKeyId == null) {

      // If controlViewModel.getLabel() did not specify a resource, we 
      // assume that controlViewModel.getLabel() + ".access-key" will also 
      // _not_ specify a legal resource.
      accessKey = JspUtil.getResourceStringOrNull(pageContext,  formElementViewModel.getLabel() + ".access-key");
    }
    else {
      accessKey = JspUtil.getResourceStringOrNull(pageContext, accessKeyId);
    }
    if (accessKey != null && accessKey.length() != 1) accessKey = null;


    // Continue
    return EVAL_BODY_INCLUDE;    
  }


  /* ***********************************************************************************
   * Tag attributes
   * ***********************************************************************************/

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Element id." 
   */
  public void setId(String id) throws JspException {
    this.id = (String)evaluateNotNull("id", id, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Whether an asterisk is shown when the element is mandatory." 
   */
  public void setShowMandatory(String showMandatory) throws JspException {
    this.showMandatory = ((Boolean)(evaluateNotNull("showMandatory", showMandatory, Boolean.class))).booleanValue();
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Whether a colon (":") is draw after the label." 
   */
  public void setShowColon(String showColon) throws JspException {
    this.showColon = ((Boolean)(evaluateNotNull("showColumn", showColon, Boolean.class))).booleanValue();
  }

  /**
   * Set the id of the resource containing the access key for this label.
   * It may be null. In this case the default resource id is used: 
   *   &lt;label-id&gt;.access-key
   * (where label-id is the resource id containing the label for the element.
   *  if it exists).
   * If such resource exists and specifies a single character, the 
   * tag outputs an additional accesskey attribute for the HTML a &lt;label&gt; element.
   * If given resource does not exist, it's also ok. Noone will die. 
   */
  public void setAccessKeyId(String accessKeyId) throws JspException {
    this.accessKeyId = (String)evaluate("accessKeyId", accessKeyId, String.class);
  }
}
