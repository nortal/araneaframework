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

package org.araneaframework.jsp.tag;

import org.araneaframework.Path;

import javax.servlet.jsp.PageContext;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.core.Assert;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.http.UpdateRegionContext;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetUtil;

/**
 * Contains commons function to simplify tags logic.
 * 
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
 * @since 2.0
 */
public class AraneaJspFunctions {

  /**
   * Registers an update region using the given ID or global ID (either global or local with widget scope), and returns
   * the ID to be assigned to an element for which the content must be updated. The crucial part of an update region is
   * the ID of the element for which a <u>content</u> must be updated.
   * 
   * @param pageContext The page context is used for resolving the current widget ID and its environment.
   * @param id The update region ID. The region is not global and is updated on certain pages.
   * @param globalId The update region ID. The region is global and usually on every page.
   * @return The registered update region ID.
   */
  private static String registerUpdateRegion(PageContext pageContext, String id, String globalId) {
    Assert.isTrue(id != null || globalId != null, "'id' or 'globalId' is required!");

    String contextWidgetId = JspWidgetUtil.getContextWidgetFullId(pageContext);
    String fullId = globalId;

    if (fullId == null) {
      fullId = contextWidgetId.length() > 0 ? (contextWidgetId + Path.SEPARATOR + id) : id;
    }

    try {
      ApplicationWidget widget = (ApplicationWidget) JspUtil.requireContextEntry(pageContext, ServletUtil.UIWIDGET_KEY);
      String uiWidgetId = widget.getScope().toString();

      JspUtil.getEnvironment(pageContext).requireEntry(UpdateRegionContext.class).addDocumentRegion(fullId, uiWidgetId);
    } catch (Exception e) {
      ExceptionUtil.uncheckException(e);
    }

    return fullId;
  }

  /**
   * Returns an HTML code for marking the beginning of an update region. The crucial part of an update region is the ID
   * of the element for which a <u>content</u> must be updated.
   * 
   * @param pageContext The page context is used for resolving the current widget ID and its environment.
   * @param tag The tag will be the rendered element tag.
   * @param id The update region ID. The region is not global and is updated on certain pages.
   * @param globalId The update region ID. The region is global and usually on every page.
   * @return The HTML code as the beginning of the update region start.
   */
  public static String getUpdateRegionBegin(PageContext pageContext, String tag, String id, String globalId) {
    String finalId = registerUpdateRegion(pageContext, id, globalId);
    StringBuffer result = new StringBuffer('<').append(tag).append(" id=\"").append(finalId).append("\"><!--BEGIN:");
    return result.append(id).append("-->").toString();
  }

  /**
   * Returns an HTML code for marking the end of an update region. The crucial part of an update region is the ID of the
   * element for which a <u>content</u> must be updated.
   * 
   * @param pageContext The page context is used for resolving the current widget ID and its environment.
   * @param tag The tag will be the rendered element tag.
   * @param id The update region ID. The region is not global and is updated on certain pages.
   * @param globalId The update region ID. The region is global and usually on every page.
   * @return The HTML code as the beginning of the update region start.
   */
  public static String getUpdateRegionEnd(PageContext pageContext, String tag, String id, String globalId) {
    String finalId = registerUpdateRegion(pageContext, id, globalId);
    return new StringBuffer("</").append(tag).append("><!--END:").append(finalId).append("-->").toString();
  }
}
