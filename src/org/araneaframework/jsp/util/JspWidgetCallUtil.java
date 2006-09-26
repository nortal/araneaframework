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

package org.araneaframework.jsp.util;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.araneaframework.jsp.container.UiWidgetContainer;

/**
 * Standard util for producing calls to UiLib widgets in various
 * container frameworks. 
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public abstract class JspWidgetCallUtil {
  public static final String SIMPLE_SUBMIT_FUNCTION = "return _ap.submit(this);";

  public static UiWidgetContainer getContainer(PageContext pageContext) throws JspException {
    return (UiWidgetContainer) JspUtil.requireContextEntry(pageContext, UiWidgetContainer.KEY);
  }

  /**
   * Write out form submit script for specified attribute of HTML element. Aranea custom HTML 
   * tag attributes (See {@link org.araneaframework.jsp.AraneaAttributes}) are expected to be
   * present for submit logic to work.
   * 
   * @param out 
   * @param attributeName HTML attribute name, ('onclick', 'onchange', ...)
   */
  public static void writeSubmitScriptForEvent(Writer out, String attributeName) throws IOException {
    JspUtil.writeOpenAttribute(out, attributeName);
    out.write(JspWidgetCallUtil.getSubmitScriptForEvent());
    JspUtil.writeCloseAttribute(out);
  }
  
  /** 
   * Returns simple submit script for HTML element. This should be used whenever HTML 
   * element has just one event handling attribute that causes form submit, but can also 
   * be used when submit event should always take the same attributes, regardless of 
   * the DOM event that activates the submit function.
   *  
   * @return {@link #SIMPLE_SUBMIT_FUNCTION} */
  public static String getSubmitScriptForEvent() {
    return SIMPLE_SUBMIT_FUNCTION;
  }
}
