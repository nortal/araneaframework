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

package org.araneaframework.jsp.tag.presentation;  

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.util.JspUtil;

/**
 * Base tag for simple button which has label but not much more.
 * 
 * @author Oleg Mürk
 */
public class BaseSimpleButtonTag extends PresentationTag {
  protected String id;
  protected String labelId;   
  protected String contextWidgetId;
  protected String localizedLabel;
  protected String onClickPrecondition;
  protected String tabindex;

  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    if (labelId != null)
      localizedLabel = JspUtil.getResourceString(pageContext, labelId);

    contextWidgetId = getContextWidgetFullId();

    return EVAL_BODY_INCLUDE;    
  }

  /* ***********************************************************************************
   * Tag attributes
   * ***********************************************************************************/

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   rtexprvalue = "true"
   *   required = "false"
   *   description = "Button id, allows to access button from JavaScript." 
   */
  public void setId(String id) throws JspException {
    this.id = (String)evaluate("id", id, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   rtexprvalue = "true"
   *   required = "false"
   *   description = "Id of button label." 
   */
  public void setLabelId(String labelId) throws JspException {
    this.labelId = (String)evaluate("labelId", labelId, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   rtexprvalue = "true"
   *   required = "false"
   *   description = "Precondition for deciding whether go to server side or not." 
   */
  public void setOnClickPrecondition(String onClickPrecondition) throws JspException {
    this.onClickPrecondition = (String) evaluate("onClickPrecondition", onClickPrecondition, String.class);
  }
  
  /**
   * @since 1.0.11
   * @jsp.attribute
   *   type = "java.lang.String"
   *   rtexprvalue = "true"
   *   required = "false"
   *   description = "HTML tabindex for the element."
   */	
  public void setTabindex(String tabindex) throws JspException {
    this.tabindex = (String)evaluateNotNull("tabindex", tabindex, String.class);
  }
}