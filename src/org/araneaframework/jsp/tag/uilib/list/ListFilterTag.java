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

package org.araneaframework.jsp.tag.uilib.list;

import org.araneaframework.Path;

import java.io.Writer;
import org.araneaframework.jsp.tag.BaseTag;
import org.araneaframework.jsp.tag.uilib.form.FormTag;
import org.araneaframework.uilib.list.ListWidget;

/**
 * List widget filter tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *  name = "listFilter"
 *  body-content = "JSP"
 *  description = "Represents UiLib list filter. Introduces an implicit UiLib form, so one can place form elements under it."
 */
public class ListFilterTag extends BaseTag {

  protected FormTag formTag;

  @Override
  public int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    // Get list data
    String listId = (String) requireContextEntry(ListTag.LIST_ID_KEY);

    // Include form tag
    this.formTag = registerSubtag(new FormTag());
    this.formTag.setId(listId + Path.SEPARATOR + ListWidget.FILTER_FORM_NAME);
    executeStartSubtag(registerSubtag(this.formTag));

    // Continue
    return EVAL_BODY_INCLUDE;
  }

  @Override
  public int doEndTag(Writer out) throws Exception {
    executeEndSubtag(registerSubtag(formTag));
    unregisterSubtag(registerSubtag(formTag));
    // Continue
    return super.doEndTag(out);
  }
}
