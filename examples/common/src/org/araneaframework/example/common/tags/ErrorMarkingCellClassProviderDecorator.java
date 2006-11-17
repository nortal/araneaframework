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

package org.araneaframework.example.common.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.araneaframework.jsp.tag.layout.support.CellClassProvider;
import org.araneaframework.jsp.tag.uilib.form.FormElementTag;
import org.araneaframework.uilib.form.FormElement;

/**
 * CellClassProvider that marks cells containing non-valid FormElements with "error" class.
 * This depends on <ui:formElement> tags used outside of cells:
 * <pre>
 *    &lt;ui:formElement id="someId"&gt;&lt;ui:cell&lt;...&gt;/ui:cell&lt;&gt;/ui:formElement&lt;
 * </pre>
 * 
 * and not
 * 
 * <pre>
 *    &lt;ui:cell&gt; &lt;ui:formElement id="someId"&gt;... &lt;/ui:formElement&gt;&lt;ui:cell&gt;
 * </pre>
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class ErrorMarkingCellClassProviderDecorator implements CellClassProvider {
  protected CellClassProvider superProvider;
  protected PageContext pageContext;

  public ErrorMarkingCellClassProviderDecorator(CellClassProvider superProvider, PageContext pageContext) {
    this.superProvider = superProvider;
    this.pageContext = pageContext;
  }

  public String getCellClass() throws JspException {
    FormElement.ViewModel formElementViewModel = (FormElement.ViewModel)pageContext.getAttribute(FormElementTag.VIEW_MODEL_KEY, PageContext.REQUEST_SCOPE);
    // superProvider.getCellClass() may only be called once, otherwise moves on to next cell's style
    String superClass = superProvider.getCellClass();

    if (formElementViewModel != null && !formElementViewModel.isValid()) {
      if (superClass != null)
        return superClass + " error";
      else
        return "error";
    }

    return superClass;
  }
}
