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

package org.araneaframework.jsp.tag.layout;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.util.JspUtil;

/**
 * Aranea's cell tag, represents one cell in a layout row.
 * In HTML this tag corresponds to column inside table row -- &lt;td&gt; with <code>class</code>, <code>colspan</code> and <code>rowspan</code> attributes.
 * 
 * @jsp.tag
 *   name = "cell"
 *   body-content = "JSP"
 *   description = "Represents a cell in layout."
 *
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class LayoutCellTag extends BaseLayoutCellTag {
  protected String cellTag = "td";
  protected String colspan;
  protected String rowspan;
  protected String width;
  protected String height;

  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    JspUtil.writeOpenStartTag(out, cellTag);
    writeCellAttributes(out);
    JspUtil.writeCloseStartTag(out);

    return EVAL_BODY_INCLUDE;
  }
  
  /** Overwrite if other attributes besides <code>styleclass</code> are needed for HTML table cell. */
  protected void writeCellAttributes(Writer out) throws Exception {
    addAttribute("class",  getStyleClass());
    addAttribute("style", getStyle());
    addAttribute("colspan", colspan);
    addAttribute("rowspan", rowspan);
    addAttribute("width", width);
    addAttribute("height", height);
	JspUtil.writeAttributes(out, attributes);
  }
  
  protected int doEndTag(Writer out) throws Exception {
    JspUtil.writeEndTag(out, cellTag);
    return super.doEndTag(out);
  }

  /* ***********************************************************************************
   * Tag attributes
   * ***********************************************************************************/
  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Colspan for this cell. Same as in HTML."
   */
  public void setColspan(String colspan) throws JspException {
    this.colspan = (String)evaluate("colspan", colspan, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Rowspan for this cell. Same as in HTML."
   */
  public void setRowspan(String rowspan) throws JspException {
    this.rowspan = (String)evaluate("rowspan", rowspan, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Width for this cell. Same as in HTML, deprecated."
   */
  public void setWidth(String width) throws JspException {
    this.width = (String)evaluate("width", width, String.class);
  }
  
  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Height for this cell. Same as in HTML, deprecated."
   */
  public void setHeight(String height) throws JspException {
    this.height = (String)evaluate("height", height, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Whether this cell is header cell, defaults to false. In HTML, tag is rendered with &lt;th&gt; or &lt;tr&gt;."
   */
  public void setHeaderCell(String headerCell) throws JspException {
    boolean isHeaderCell = ((Boolean)evaluate("headerCell", headerCell, Boolean.class)).booleanValue();
    this.cellTag = isHeaderCell ? "th" : "td";
  }
}
