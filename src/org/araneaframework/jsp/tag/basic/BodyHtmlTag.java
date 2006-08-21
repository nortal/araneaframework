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

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.util.JspUtil;

/**
 * Aranea HTML body tag. 
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * 
 * @jsp.tag
 *   name = "body"
 *   body-content = "JSP"
 *   description = "HTML BODY tag with some Aranea JSP additions."
 */
public class BodyHtmlTag extends PresentationTag {
  public static final String ONLOAD = "_ap.onload()";
  public static final String ONUNLOAD = "_ap.onunload()";
  
  protected String onload = ONLOAD;
  protected String onunload = ONUNLOAD;
  
  protected int doStartTag(Writer out) throws Exception {
    int r = super.doStartTag(out);

    addContextEntry(PresentationTag.ATTRIBUTED_TAG_KEY, null);
    
    JspUtil.writeOpenStartTag(out, "body");
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "onload", onload);
    JspUtil.writeAttribute(out, "onunload", onunload);
    JspUtil.writeCloseStartTag_SS(out);
    
    writeBodyStartScripts(out);
    
    return r;
  }

  protected int doEndTag(Writer out) throws Exception {
    JspUtil.writeEndTag(out, "body");
    return super.doEndTag(out);
  }
  
  protected void writeBodyStartScripts(Writer out) throws Exception {
    String servletUrl =
        ServletUtil.getInputData(pageContext.getRequest()).getContainerURL(); 
	  
    JspUtil.writeOpenStartTag(out, "script");
    JspUtil.writeAttribute(out, "type", "text/javascript");
    JspUtil.writeCloseStartTag_SS(out);
    
    out.write("getActiveAraneaPage().setServletURL('");
    out.write(servletUrl);
    out.write("');");

    JspUtil.writeEndTag(out, "script");
  }

  /* ***********************************************************************************
   * Tag attributes
   * ***********************************************************************************/

  /**
   * @jsp.attribute
   * type = "java.lang.String"
   * required = "false"
   * description = "Overwrite the standard Aranea JSP HTML body onload event. Use with caution."
   */
  public void setOnload(String onload) throws JspException {
    this.onload = (String) evaluate("onload", onload, String.class);
  }
  
  /**
   * @jsp.attribute
   * type = "java.lang.String"
   * required = "false"
   * description = "Overwrite the standard Aranea JSP HTML body onunload event. Use with caution."
   */
  public void setOnUnload(String onunload) throws JspException {
    this.onunload = (String) evaluate("onunload", onunload, String.class);
  }
}
