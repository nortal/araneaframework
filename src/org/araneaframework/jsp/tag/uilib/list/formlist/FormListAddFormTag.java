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

package org.araneaframework.jsp.tag.uilib.list.formlist;

import java.io.Writer;
import org.araneaframework.jsp.tag.uilib.form.FormTag;

/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 * @jsp.tag
 *   name = "formListAddForm"
 *   body-content = "JSP"
 *   description = "The tag for rendering form for adding new row to an editable list. Makes available following page scope variables:
 *      <ul>
 *      <li><i>form</i> - the add-form <i>view model</i>.</li>
 *      <li><i>formId</i> - the add-form ID (not full ID!).</li>
 *      </ul>"
 */
public class FormListAddFormTag extends FormTag {

  @Override
  public int doStartTag(Writer out) throws Exception {
    String editableListId = (String) requireContextEntry(FormListTag.FORM_LIST_ID_KEY);
    this.id = editableListId + ".addForm";
    return super.doStartTag(out);
  }
}
