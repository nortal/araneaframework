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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.UiPresentationTag;
import org.araneaframework.jsp.tag.aranea.UiAraneaRootTag;
import org.araneaframework.jsp.tag.form.UiSystemFormTag;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.jsp.util.UiWidgetUtil;
import org.araneaframework.servlet.ServletOutputData;

/**
 * Button base tag.
 * 
 * @author Oleg Mürk
 */
public class UiSimpleButtonBaseTag extends UiPresentationTag {
  protected String id;
  protected String labelId;   
  protected String systemFormId;
  protected String contextWidgetId;
  protected String localizedLabel;
  protected String onClickPrecondition = "return true;";

  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    systemFormId = (String) requireContextEntry(UiSystemFormTag.ID_KEY);

    if (labelId != null)
      localizedLabel = UiUtil.getResourceString(pageContext, labelId);

    contextWidgetId = UiWidgetUtil.getContextWidgetFullId(pageContext);

    return EVAL_BODY_INCLUDE;    
  }

  /* ***********************************************************************************
   * Tag attributes
   * ***********************************************************************************/

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Button id, allows to access button from JavaScript." 
   */
  public void setId(String id) throws JspException {
    this.id = (String)evaluate("id", id, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Id of button label." 
   */
  public void setLabelId(String labelId) throws JspException {
    this.labelId = (String)evaluate("labelId", labelId, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Precondition for deciding whether go to server side or not." 
   */
  public void setOnClickPrecondition(String onClickPrecondition) throws JspException {
    this.onClickPrecondition = (String) evaluate("onChangePrecondition", onClickPrecondition, String.class);
  }
  
  public StringBuffer getRequestURL() throws JspException {
    ServletOutputData output = (ServletOutputData) requireContextEntry(UiAraneaRootTag.OUTPUT_DATA_KEY);
    StringBuffer sb = ((HttpServletRequest)output.getRequest()).getRequestURL();
    return sb;
  }
}




