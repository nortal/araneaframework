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

package org.araneaframework.example.common.tags.wizard;

import java.io.Writer;
import org.araneaframework.OutputData;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.jsp.tag.BaseTag;


/**
 * This tag includes wizard's current page.
 * It must be nested into WizardTag.
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 * 
 * @jsp.tag
 *   name = "wizardBody"
 *   body-content = "JSP"
 *   description = "Includes the current wizard page."
 */
public class WizardBodyTag extends BaseTag {
  protected int doStartTag(Writer out) throws Exception {
    ApplicationWidget widget = getContextWidget();

    OutputData output = getOutputData();

    try {
      hideGlobalContextEntries(pageContext);
      out.flush();
      widget._getWidget().render(output);
    } finally {
      restoreGlobalContextEntries(pageContext);
    }

    return SKIP_BODY;
  }
}
