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

package org.araneaframework.jsp.tag.entity;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.BaseTag;


/**
 * Entity tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "entity"
 *   body-content = "empty"
 *   description = "Represents an HTML entity, for instance <i>&amp;nbsp;</i>."
 */
public class EntityHtmlTag extends BaseTag {
  protected String code = null;
  protected long count = 1;
  
  public EntityHtmlTag() {}
  public EntityHtmlTag(String code) {
	  this.code = code;
  }
  
  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    
    for(long i = 0; i < count; i++) {
      out.write("&");
      out.write(code);
      out.write(";");      
    }    
    
    // Continue
    return EVAL_BODY_INCLUDE;    
  }

   /* ***********************************************************************************
   * Tag attributes
   * ********************************************************************************* */

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "HTML entity code, e.g. <i>nbsp</i> or <i>#012</i>." 
   */
  public void setCode(String code) throws JspException {
    this.code = evaluateNotNull("code", code, String.class);  
  }
  
  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Number of times to repeat the entity." 
   */
  public void setCount(String count){
    this.count = (evaluate("count", count, Long.class)).longValue();  
  }
}
