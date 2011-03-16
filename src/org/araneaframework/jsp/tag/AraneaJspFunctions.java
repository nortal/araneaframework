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

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.PageContext;
import org.araneaframework.Environment;
import org.araneaframework.Path;
import org.araneaframework.Widget;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.core.Assert;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.framework.ConfirmationContext;
import org.araneaframework.framework.OverlayContext;
import org.araneaframework.http.UpdateRegionContext;
import org.araneaframework.http.util.EnvironmentUtil;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetUtil;
import org.araneaframework.uilib.util.ConfigurationUtil;

/**
 * Contains commons function to simplify tags logic.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 2.0
 */
public class AraneaJspFunctions {

  public static final String KEY_ATTRS = "attributes";

  public static final String KEY_ALL_ATTRS = "allAttributes";

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

  public static String evalCSSClass(String baseClass, String styleClass) {
    return PresentationTag.calculateStyleClass(baseClass, styleClass);
  }

  @SuppressWarnings("unchecked")
  public static Map<String, String> pushPreviousAttributes(JspContext jspContext) {
    LinkedList<Map<String, String>> allAttrs = (LinkedList<Map<String, String>>) jspContext.getAttribute(KEY_ALL_ATTRS,
        PageContext.REQUEST_SCOPE);

    if (allAttrs == null) {
      allAttrs = new LinkedList<Map<String, String>>();
      jspContext.setAttribute(KEY_ALL_ATTRS, allAttrs, PageContext.REQUEST_SCOPE);
    }

    Map<String, String> lastAttrs = (Map<String, String>) jspContext.getAttribute(KEY_ATTRS, PageContext.REQUEST_SCOPE);

    if (lastAttrs != null) {
      allAttrs.add(lastAttrs);
    }

    // The map for new attributes:
    return new LinkedHashMap<String, String>();
  }

  @SuppressWarnings("unchecked")
  public static Map<String, String> popPreviousAttributes(JspContext jspContext) {
    LinkedList<Map<String, String>> allAttrs = (LinkedList<Map<String, String>>) jspContext.getAttribute(KEY_ALL_ATTRS,
        PageContext.REQUEST_SCOPE);

    if (allAttrs == null) {
      throw new RuntimeException("The all attributes list shouldn't be empty! Make sure that pushPreviousAttributes() "
          + "was called before this method!");
    }

    // The map for new attributes:
    return allAttrs.isEmpty() ? null : allAttrs.getLast();
  }

  public static Map<String, String> addMapEntry(Map<String, String> map, String key, String value) {
    if (map == null) {
      throw new RuntimeException("The map must not be null! Make sure that pushPreviousAttributes() was called before "
          + "attributes!");
    }
    if (key != null) {
      map.put(key, value);
    }
    return map;
  }

  public static void setBodyAttributes(JspContext jspContext) {
    Environment env = JspUtil.getEnvironment(jspContext);
    String threadId = EnvironmentUtil.getThreadServiceId(env);
    String topServiceId = EnvironmentUtil.getTopServiceId(env);

    jspContext.setAttribute("bgvalidation", ConfigurationUtil.isBackgroundFormValidationEnabled(env));
    jspContext.setAttribute("confirmationMsg", getConfirmationMsg(env));
    jspContext.setAttribute("threadId", threadId == null ? "null" : "'" + threadId + "'");
    jspContext.setAttribute("topServiceId", topServiceId == null ? "null" : "'" + topServiceId + "'");
    jspContext.setAttribute("servletURL", JspUtil.getServletURL(jspContext));
    jspContext.setAttribute("encodedURL", JspUtil.getFormActionURL(jspContext));
    jspContext.setAttribute("locale", JspUtil.getLocalizationContext(jspContext).getLocale());
    jspContext.setAttribute("expiringServiceCtx", EnvironmentUtil.getExpiringServiceContext(env));
  }

  private static String getConfirmationMsg(Environment env) {
    ConfirmationContext confirmCtx = env.getEntry(ConfirmationContext.class);
    return confirmCtx != null ? confirmCtx.getConfirmationMessage() : null;
  }

  public static String getAcceptCharset(JspContext jspContext) {
    String charset = JspUtil.getConfiguration(jspContext).getSubmitCharset();
    return charset != null ? charset : "utf-8";
  }

  public static String getFormAction(JspContext jspContext) {
    return JspUtil.getFormActionURL(jspContext);
  }

  public static OverlayContext getOverlayContext(JspContext jspContext) {
    return JspUtil.getEnvironment(jspContext).getEntry(OverlayContext.class);
  }

  public static Set<Entry<String, String>> getHiddenFormFields(JspContext jspContext) {
    return JspUtil.getSystemFormContextEntries(jspContext);
  }

  public static boolean getFormBackgroundValidation(JspContext jspContext) {
    Widget w = (Widget) jspContext.getAttribute("widget", PageContext.REQUEST_SCOPE);
    return ConfigurationUtil.isBackgroundFormValidationEnabled(w.getEnvironment());
  }

}
