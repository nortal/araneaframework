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

package org.araneaframework.jsp.tag.uilib.form;

import java.io.Writer;



/**
 * Standard form element label tag. Renders localizable label bound to form element 
 * with HTML <code>&lt;span&gt;</code> and <code>&lt;label&gt;</code> tags.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "label"
 *   body-content = "JSP"
 *   description = "Represents localizable label that is bound to a form element."
 */
public class FormElementLabelHtmlTag extends BaseFormElementLabelTag {
  
  protected String mandatoryLabelClass = null;
  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);    
    FormSimpleLabelHtmlTag.writeLabel(
        out, 
        localizedLabel, 
        (formElementViewModel.isMandatory() && showMandatory), 
        getStyleClass(),
        derivedId,
        pageContext,
        showColon,
        accessKey, getStyle(), mandatoryLabelClass
    );

    return EVAL_BODY_INCLUDE;    
  }
  
  public void setMandatoryLabelClass(String mandatoryLabelClass) {
    this.mandatoryLabelClass = evaluate("mandatoryLabelClass", mandatoryLabelClass, String.class);
  }
}




