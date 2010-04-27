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
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.uilib.BaseWidgetTag;
import org.araneaframework.jsp.tag.uilib.list.ListTag;
import org.araneaframework.uilib.form.formlist.FormListWidget;

/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 * @jsp.tag
 *   name = "formList"
 *   body-content = "JSP"
 *   description = "UiLib form list tag. <br/> 
           Makes available following page scope variables: 
           <ul>
             <li><i>formList</i> - UiLib editable list view model.</li>
             <li><i>formListId</i> - UiLib editable list id.</li>
           </ul> "
 */
public class FormListTag extends BaseWidgetTag {

  protected FormListWidget<?, ?>.ViewModel formListViewModel;

  public final static String FORM_LIST_ID_KEY = "formListId";

  public final static String FORM_LIST_VIEW_MODEL_KEY = "formList";

  @Override
  @SuppressWarnings("unchecked")
  public int doStartTag(Writer out) throws Exception {
    if (this.id == null) {
      this.id = (String) requireContextEntry(ListTag.LIST_ID_KEY) + ".formList";
    }

    super.doStartTag(out);

    try {
      this.formListViewModel = (FormListWidget.ViewModel) this.viewModel;
    } catch (ClassCastException e) {
      throw new AraneaJspException("Could not acquire form list view model. <ui:formList> should have id specified "
          + "or should be in context of real FormListWidget.", e);
    }

    addContextEntry(FORM_LIST_ID_KEY, this.id);
    addContextEntry(FORM_LIST_VIEW_MODEL_KEY, this.formListViewModel);
    return EVAL_BODY_INCLUDE;
  }

  // FINALLY - reset some fields to allow safe reuse from tag pool:

  @Override
  public void doFinally() {
    super.doFinally();
    this.id = null;
    this.formListViewModel = null;
  }
}
