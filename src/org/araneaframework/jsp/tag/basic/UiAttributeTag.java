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

package org.araneaframework.jsp.tag.basic;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.UiBaseTag;
import org.araneaframework.jsp.tag.UiPresentationTag;


/**
 * Attribute tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "attribute"
 *   body-content = "empty"
 *   description = "Defines an attribute of the containing element."
 */
public class UiAttributeTag extends UiBaseTag {
  protected String name;
  protected String value;
  
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    
    String elementKey = (String)getContextEntry(UiAttributedTagInterface.HTML_ELEMENT_KEY);
    if (elementKey != null) {
    	  writeAttributeScript(out, elementKey);
    	  return SKIP_BODY;
    }
    
    UiAttributedTagInterface attributedTag = (UiAttributedTagInterface)requireContextEntry(UiPresentationTag.ATTRIBUTED_TAG_KEY);
    attributedTag.addAttribute(name, value);

    return SKIP_BODY;
  }

  public void writeAttributeScript(Writer out, String elementKey) throws IOException {
    out.write("<script type=\"text/javascript\">");
    out.write("setElementAttr(\"");
    out.write(elementKey);
	out.write("\", \"");
	out.write(name);
	out.write("\", \"");
	out.write(value);
	out.write("\");");
    out.write("</script>");
  }
  
  /* ***********************************************************************************
   * Tag attributes
   * ***********************************************************************************/
 
  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "true" 
   *   description = "Attribute name."
   */
  public void setName(String name) throws JspException {
    this.name = (String)evaluateNotNull("name", name, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "true"
   *   description = "Attribute value." 
   */
  public void setValue(String value) throws JspException {
    this.value = (String)evaluate("value", value, String.class);
  }
}
