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

package org.araneaframework.jsp.tag.aranea;

import java.io.Writer;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import org.araneaframework.http.JspContext;
import org.araneaframework.jsp.tag.BaseTag;
import org.araneaframework.jsp.util.StringAdapterResourceBundle;

/**
 * Aranea JSP root tag, all other Aranea JSP tags must be used inside it. 
 * 
 * @jsp.tag
 *   name = "root"
 *   body-content = "JSP"
 */
public class AraneaRootTag extends BaseTag {
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    
    JspContext config = (JspContext) getEnvironment().requireEntry(JspContext.class);
    
    Config.set(
        pageContext, 
        Config.FMT_LOCALIZATION_CONTEXT, 
        new LocalizationContext(
            new StringAdapterResourceBundle(config.getCurrentBundle()),
            config.getCurrentLocale()
          ),
          PageContext.REQUEST_SCOPE           
        );
    
    return EVAL_BODY_INCLUDE;
  }
}
