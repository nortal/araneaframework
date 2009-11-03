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

package org.araneaframework.jsp.tag.presentation;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.util.JspUtil;

/**
 * Base tag for simple button which has label but not much more.
 * 
 * @author Oleg Mürk
 */
public class BaseSimpleButtonTag extends PresentationTag {

  protected String id;

  protected String labelId;

  protected String contextWidgetId;

  protected String localizedLabel;

  protected String onClickPrecondition;

  protected String tabindex;

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    if (!StringUtils.isEmpty(this.labelId)) {
      this.localizedLabel = JspUtil.getResourceString(this.pageContext, this.labelId);
    }

    this.contextWidgetId = getContextWidgetFullId();

    return EVAL_BODY_INCLUDE;
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Button id, allows to access button from JavaScript."
   */
  public void setId(String id) {
    this.id = evaluate("id", id, String.class);
  }

  /**
   * @jsp.attribute type = "java.lang.String" required = "false" description = "Id of button label."
   */
  public void setLabelId(String labelId) {
    this.labelId = evaluate("labelId", labelId, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Precondition for deciding whether go to server side or not."
   */
  public void setOnClickPrecondition(String onClickPrecondition) {
    this.onClickPrecondition = evaluate("onClickPrecondition", onClickPrecondition, String.class);
  }

  /**
   * @since 1.0.11
   * @jsp.attribute type = "java.lang.String" required = "false" description = "HTML tabindex for the element."
   */
  public void setTabindex(String tabindex) throws JspException {
    this.tabindex = evaluateNotNull("tabindex", tabindex, String.class);
  }
}
