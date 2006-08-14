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

import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.PresentationTag;

/**
 * Button base tag.
 * 
 * @author Oleg Mürk
 */
public class BaseButtonTag extends PresentationTag {
  protected String id = null;
  protected String labelId = null;
  protected String onclick = null;

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
   *   required = "true"
   *   description = "onClick Javascript action." 
   */
  public void setOnclick(String onclick) throws JspException {
    this.onclick = (String)evaluate("onclick", onclick, String.class);
  }
}