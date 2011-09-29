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
import org.apache.commons.lang.StringUtils;
import org.araneaframework.http.WindowFocusPositionContext;
import org.araneaframework.jsp.tag.BaseTag;
/**
 * Tag that registers functions dealing with focus position setting.
 * @author Maksim Boiko <mailto:max@webmedia.ee>
 * 
 * @jsp.tag
 *   name = "registerFocus"
 *   body-content = "empty"
 *   description = "Registers functions dealing with focus position setting".
 */
public class FocusRegistrationHtmlTag extends BaseTag {

  @Override
  protected int doEndTag(Writer out) throws Exception {
    WindowFocusPositionContext focusContext = getEnvironment().getEntry(WindowFocusPositionContext.class);

    if (focusContext != null) {
      setFocus(out, focusContext);
    }

    return EVAL_PAGE;
  }

  protected void setFocus(Writer out, WindowFocusPositionContext focusContext) throws Exception {
    if(StringUtils.isEmpty(focusContext.getFocusedElement())) {
      return;
    }
    out.write("<script type=\"text/javascript\">" +
    		"var elementToFocus = document.getElementById('");
    out.write(focusContext.getFocusedElement());
    out.write("');");
//    out.write("alert('"+focusContext.getFocusedElement()+"');");
    out.write("if(elementToFocus) {elementToFocus.focus();}");
    out.write("</script>");
  }
}
