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
import org.araneaframework.jsp.tag.BaseTag;

/**
 * The content of this tag will be displayed when value acquired from <code>PageContext</code>
 * under the {org.araneaframework.jsp.tag.uilib.form.element.display.FormConditionalDisplayTag#CONDITION_KEY}
 * was true.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 * @jsp.tag
 *   name = "conditionTrue"
 *   body-content = "JSP"
 *   description = "The content of this tag will be displayed if the element value is TRUE."
 */
public class FormConditionTrueTag extends BaseTag {

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    Boolean condition = (Boolean) requireContextEntry(FormConditionalDisplayTag.CONDITION_KEY);
    return condition ? EVAL_BODY_INCLUDE : SKIP_BODY;
  }
}
