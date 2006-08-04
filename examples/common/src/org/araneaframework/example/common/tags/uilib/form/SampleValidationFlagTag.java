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

package org.araneaframework.example.common.tags.uilib.form;

import java.io.Writer;
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementHtmlTag;
import org.araneaframework.jsp.util.JspUtil;



/**
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 */
public class SampleValidationFlagTag extends BaseFormElementHtmlTag {
  
  /**
   */
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    
    String imgId = "img_"+formFullId+"."+derivedId;
    addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, imgId);

    JspUtil.writeOpenStartTag(out, "img");
    JspUtil.writeAttribute(out, "id", imgId);
    if (formElementViewModel.isValid())
      JspUtil.writeAttribute(out, "style", "visibility: hidden;");
    else
      JspUtil.writeAttribute(out, "style", "visibility: visible;");
    JspUtil.writeAttribute(out, "src", "gfx/uistd_validation_error.gif");
    JspUtil.writeCloseStartEndTag_SS(out);   
    
    return SKIP_BODY;
  }
}
