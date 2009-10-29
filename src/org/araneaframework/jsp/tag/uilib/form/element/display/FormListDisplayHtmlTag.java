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

package org.araneaframework.jsp.tag.uilib.form.element.display;

import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import org.araneaframework.jsp.util.JspUtil;

/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 * @jsp.tag
 *   name = "listDisplay"
 *   body-content = "empty"
 *   description = "Display element value as list of strings, 
          represents DisplayControl and requires that element value would be of type Collection."
 */
public class FormListDisplayHtmlTag extends BaseFormListDisplayHtmlTag {
    
  /**
   */
  @Override
  protected int doEndTag(Writer out) throws Exception {    
    
    if (displayControlViewModel.getValue() != null) {
      for (Iterator<?> i = ((List<?>) displayControlViewModel.getValue()).iterator(); i.hasNext(); ) { 
      	JspUtil.writeEscaped(out, i.next().toString());
        
        if (i.hasNext()) 
          writeSeparator(out);
      }
    }
    
    super.doEndTag(out);    
    return EVAL_PAGE;
  }
}
