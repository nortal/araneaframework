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

package org.araneaframework.example.common.tags.uilib.form.element;

import java.io.Writer;
import org.araneaframework.jsp.tag.uilib.form.UiFormEnterKeyboardHandlerTag;
import org.araneaframework.jsp.tag.uilib.form.UiFormTag;
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
    this.showLabel = false;
    this.validateOnEvent = true;
    this.style = "text-decoration: none";
  }

  public int doStartTag(Writer out) throws Exception {
    addAttribute("style", getStyle());
    super.doStartTag(out);
    out.write("<button type=\"button\">" + UiUtil.getResourceString(pageContext, "button.search").toUpperCase(UiUtil.getLocalizationContext(pageContext).getLocale()) + "</button>");

    UiFormEnterKeyboardHandlerTag tag = new UiFormEnterKeyboardHandlerTag();
    tag.setFullElementId((String)requireContextEntry(UiFormTag.FORM_FULL_ID_KEY)+"."+id);
    registerSubtag(tag);
    executeStartSubtag(tag);
    executeEndTagAndUnregister(tag);
    
    return EVAL_BODY_INCLUDE;    
  }
}
