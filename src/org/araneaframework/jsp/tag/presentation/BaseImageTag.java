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
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.PresentationTag;

/**
 * UI image tag.
 * 
 * @author Oleg Mürk
 */
public abstract class BaseImageTag extends PresentationTag {
  // Usual HTML <img> tag attributes	
  protected String code, src, width, height, alt, title;

  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    if (code != null) {
      Info info = getImageInfo(code);
      if (info == null)
        throw new AraneaJspException("Missing image description with code '" + code + "'");
      this.src = info.src;
      this.width = info.width;
      this.height = info.height;
    }

    return EVAL_BODY_INCLUDE;    
  }

  /**
   * Image info class.
   */  
  protected static class Info {    
    public Info(String src, String width, String height) {
      this.src = src;
      this.width = width;
      this.height = height;
      this.alt = null;
    }    
    public Info(String src, String width, String height, String alt) {
      this.src = src;
      this.width = width;
      this.height = height;
      this.alt = alt;
    }

    public String src;
    public String width;
    public String height;
    public String alt;
  }

  /* ***********************************************************************************
   * Tag attributes
   * ***********************************************************************************/
  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   rtexprvalue = "true"
   *   required = "false"
   *   description = "Image code." 
   */
  public void setCode(String code) throws JspException {
    this.code = (String)evaluate("code", code, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   rtexprvalue = "true"
   *   required = "false"
   *   description = "Image src." 
   */
  public void setSrc(String src) throws JspException {
    this.src = (String)evaluate("src", src, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   rtexprvalue = "true"
   *   required = "false"
   *   description = "Image width" 
   */

  public void setWidth(String width) throws JspException {
    this.width = (String)evaluate("width", width, String.class);
  }  

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   rtexprvalue = "true"
   *   required = "false"
   *   description = "Image height." 
   */
  public void setHeight(String height) throws JspException {
    this.height = (String)evaluate("height", height, String.class);
  }  


  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   rtexprvalue = "true"
   *   required = "false"
   *   description = "Image alternate text." 
   */
  public void setAlt(String alt) throws JspException {
    this.alt = (String)evaluate("alt", alt, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   rtexprvalue = "true"
   *   required = "false"
   *   description = "Image title" 
   */
  public void setTitle(String title) throws JspException {
    this.title = (String)evaluate("title", title, String.class);
  }


  /* ***********************************************************************************
   * ABSTRACT METHODS
   * ***********************************************************************************/
  protected abstract Info getImageInfo(String code); 
}
