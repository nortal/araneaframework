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

package org.araneaframework.jsp.tag.basic;

import java.io.Writer;
import org.araneaframework.jsp.tag.BaseTag;
import org.araneaframework.jsp.util.JspUtil;

/**
 * Load event tag.
 * 
 * @author Maksim Boiko (max@webmedia.ee)
 * 
 * @jsp.tag
 *   name = "onLoadEvent"
 *   body-content = "empty"
 *   description = "Registers events on body load."
 */
public class OnLoadEventHtmlTag extends BaseTag{
  protected String event = "return true;";

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    JspUtil.writeStartTag_SS(out, "script");
    out.write("document.observe('aranea:loaded',function(){");
    JspUtil.writeEscapedAttribute(out, this.event);
    out.write("});");
    JspUtil.writeEndTag(out, "script");
    return SKIP_BODY;
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "true"
   *   description = "Event to register."
   */
  public void setEvent(String event){
    this.event = evaluate("event", event, String.class);
  }
}
