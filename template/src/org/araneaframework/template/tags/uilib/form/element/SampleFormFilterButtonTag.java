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

package org.araneaframework.template.tags.uilib.form.element;

import java.io.Writer;
import org.araneaframework.jsp.tag.uilib.form.element.UiStdFormLinkButtonTag;
import org.araneaframework.jsp.util.UiUtil;


/**
 * Standard search link button form element tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "filterButton"
 *   body-content = "JSP"
 */
public class SampleFormFilterButtonTag extends UiStdFormLinkButtonTag {

  public SampleFormFilterButtonTag() {
    this.id = "filter";
    this.styleClass = "simplelink";
    this.showLabel = false;
    this.validateOnEvent = true;
  }

  public int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    out.write("<button>" + UiUtil.getResourceString(pageContext, "button.search").toUpperCase(UiUtil.getLocalizationContext(pageContext).getLocale()) + "</button");
    return EVAL_BODY_INCLUDE;    
  }    
}
