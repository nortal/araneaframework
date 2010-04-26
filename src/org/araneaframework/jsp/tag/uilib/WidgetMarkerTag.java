/*
 * Copyright 2007 Webmedia Group Ltd.
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

package org.araneaframework.jsp.tag.uilib;

import java.io.Writer;
import org.araneaframework.jsp.AraneaAttributes;
import org.araneaframework.jsp.util.JspUtil;

/**
 * Adds a marker to the page which is used to identify the widget to which the content belongs.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 * @jsp.tag
 *   name = "widgetMarker"
 *   body-content = "JSP"
 *   description = "Adds a marker to the page which can be used to identify the widget to which this tags content belongs. This tag
 *   can be used as many times as needed (content of one widget might be in several unconnected pieces)."
 */
public class WidgetMarkerTag extends BaseWidgetTag {

  public static final String MARKERCLASS = "widgetMarker";

  protected String tag = "div";

  @Override
  public int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    JspUtil.writeOpenStartTag(out, this.tag);
    JspUtil.writeAttribute(out, "class", MARKERCLASS);
    JspUtil.writeAttribute(out, AraneaAttributes.WIDGET_ID, this.widget.getScope().toString());
    JspUtil.writeCloseStartTag(out);

    return EVAL_BODY_INCLUDE;
  }

  @Override
  protected int doEndTag(Writer out) throws Exception {
    JspUtil.writeEndTag(out, this.tag);
    return super.doEndTag(out);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Widget marker HTML tag (default is &lt;div&gt;). Content will be rendered inside that tag."
   */
  public void setTag(String tag) throws Exception {
    this.tag = evaluateNotNull("tag", tag, String.class);
  }
}
