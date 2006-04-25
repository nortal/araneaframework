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

package org.araneaframework.template.tags.wizard;

import java.io.Writer;
import org.araneaframework.jsp.tag.UiBaseTag;
import org.araneaframework.jsp.util.UiWidgetUtil;
import org.araneaframework.template.framework.context.WizardContext;

/**
 * This tag displays wizard's navigation info.
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 * 
 * @jsp.tag
 *   name = "wizardHeader"
 *   body-content = "JSP"
 *   description = "Includes navigation info."
 */
public class WizardHeaderTag extends UiBaseTag {
  protected int doStartTag(Writer out) throws Exception {
	  WizardContext wizard = (WizardContext) UiWidgetUtil.getWidgetFromContext(null, pageContext);
	  
	  int index = wizard.getCurrentPageIndex();
	  int count = wizard.countPages();
	  
	  StringBuffer sb = new StringBuffer();
	  sb.append("Page ");
	  sb.append(index + 1);
	  sb.append(" of ");
	  sb.append(count);
	  
	  out.write(sb.toString());
	  
	  return SKIP_BODY;		
	}
}
