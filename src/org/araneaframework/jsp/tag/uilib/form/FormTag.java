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

package org.araneaframework.jsp.tag.uilib.form;

import java.io.Writer;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.uilib.BaseWidgetTag;
import org.araneaframework.uilib.form.FormWidget;

/**
 * {@link org.araneaframework.uilib.form.FormWidget} tag. Specifies form context for inner tags, makes
 * {@link org.araneaframework.uilib.form.FormWidget.ViewModel} and widget id accessible to inner tags as EL variables.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *  name = "form"
 *  body-content = "JSP"
 *  description = "Form tag makes available following page scope variables:
 *          <ul>
 *          <li><i>form</i> - the form widget <i>view model</i>.</li>
 *          <li><i>formId</i> - the form widget ID (not full ID, just widget ID).</li>
 *          <li><i>formFullId</i> - the form widget full ID.</li>
 *          </ul>"
 */
public class FormTag extends BaseWidgetTag {

  public final static String FORM_FULL_ID_KEY = "formFullId";

  public final static String FORM_ID_KEY = "formId";

  public final static String FORM_VIEW_MODEL_KEY = "form";

  public final static String FORM_KEY = "org.araneaframework.jsp.tag.uilib.form.FormTag.FORM";

  protected FormWidget.ViewModel formViewModel;

  @Override
  public int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    // Get form data
    try {
      this.formViewModel = (FormWidget.ViewModel) this.viewModel;
    } catch (ClassCastException e) {
      throw new AraneaJspException("Could not acquire form view model. <ui:form> should have an ID specified or "
          + "should be in context of real FormWidget.", e);
    }

    // Set variables
    addContextEntry(FORM_FULL_ID_KEY, this.fullId);
    addContextEntry(FORM_ID_KEY, this.id);
    addContextEntry(FORM_VIEW_MODEL_KEY, this.formViewModel);
    addContextEntry(FORM_KEY, this.widget);

    // Continue
    return EVAL_BODY_INCLUDE;
  }

  @Override
  public void doFinally() {
    super.doFinally();
    this.formViewModel = null;
  }
}
