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

package org.araneaframework.jsp.tag.uilib.form.element.text;

import java.io.Writer;
import org.araneaframework.jsp.tag.uilib.form.BaseFormSimpleElementDisplayHtmlTag;

/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 * @jsp.tag
 *   name = "floatInputDisplay"
 *   body-content = "JSP"
 *   description = "Form float display field, represents UiLib "FloatControl"."
 */
public class FormFloatInputDisplayHtmlTag extends BaseFormSimpleElementDisplayHtmlTag {
  {
    baseStyleClass = "aranea-float-display";
  }

  @Override
  protected int doEndTag(Writer out) throws Exception {
    assertControlType("FloatControl");
    return super.doEndTag(out);
  }
}
