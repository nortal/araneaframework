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

package org.araneaframework.jsp.tag.updateregion;

import java.io.Writer;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.http.UpdateRegionContext;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.BaseTag;
import org.araneaframework.jsp.util.JspUtil;

/**
 * Base tag for tags that allow defining updatable regions within HTML page.
 * 
 * @see org.araneaframework.jsp.tag.updateregion.UpdateRegionHtmlTag
 * @see org.araneaframework.jsp.tag.updateregion.UpdateRegionRowsHtmlTag
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class BaseUpdateRegionTag extends BaseTag {

  protected String id;

  protected String globalId;

  protected String fullId;

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    if (this.id == null && this.globalId == null) {
      throw new AraneaJspException("'id' or 'globalId' is required!");
    }

    String contextWidgetId = getContextWidgetFullId();

    this.fullId = this.globalId;

    if (this.fullId == null) {
      this.fullId = contextWidgetId.length() > 0 ? (contextWidgetId + "." + this.id) : this.id;
    }

    String uiWidgetId = ((ApplicationWidget) JspUtil.requireContextEntry(this.pageContext, ServletUtil.UIWIDGET_KEY))
        .getScope().toString();
    UpdateRegionContext updateRegionContext = getEnvironment().requireEntry(UpdateRegionContext.class);
    updateRegionContext.addDocumentRegion(this.fullId, uiWidgetId);

    return EVAL_BODY_INCLUDE;
  }

  /**
   * @jsp.attribute type = "java.lang.String" required = "false" description = "Local id of the update region."
   */
  public void setId(String id) {
    this.id = evaluate("id", id, String.class);
  }

  /**
   * @jsp.attribute type = "java.lang.String" required = "false" description = "Global id of the update region."
   */
  public void setGlobalId(String globalId) {
    this.globalId = evaluate("globalId", globalId, String.class);
  }
}
