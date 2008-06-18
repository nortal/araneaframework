/**
 * Copyright 2007 Webmedia Group Ltd.
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

package org.araneaframework.jsp.tag.uilib;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.jsp.tag.BaseTag;
import org.araneaframework.jsp.util.JspWidgetUtil;

public class BaseWidgetTag extends BaseTag {

  protected String id;
  protected String fullId;
  protected ApplicationWidget widget;
  protected ApplicationWidget.WidgetViewModel viewModel;

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "UiLib widget id." 
   */
  public void setId(String id) throws JspException {
    this.id = evaluateNotNull("id", id, String.class);
  }

  @Override
  public int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    if (id == null) {
      widget = getContextWidget();
    } else {
      widget = JspWidgetUtil.traverseToSubWidget(getContextWidget(), id);
    }
    fullId = widget.getScope().toString();
    viewModel = (ApplicationWidget.WidgetViewModel) widget._getViewable().getViewModel();

    // Continue
    return EVAL_BODY_INCLUDE;    
  }

  @Override
  public void doFinally() {
    super.doFinally();
    // to prevent memory leaks in containers where tags might live very long
    id = fullId = null;
    widget = null;
    viewModel = null;
  }

}
