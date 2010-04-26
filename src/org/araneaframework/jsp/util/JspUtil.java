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

package org.araneaframework.jsp.util;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.Environment;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.framework.SystemFormContext;
import org.araneaframework.framework.OverlayContext.OverlayActivityMarkerContext;
import org.araneaframework.http.JspContext;
import org.araneaframework.http.util.EnvironmentUtil;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.jsp.UiEvent;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;
import org.araneaframework.jsp.tag.basic.ElementHtmlTag;
import org.araneaframework.jsp.tag.layout.support.CellClassProvider;
import org.araneaframework.jsp.tag.layout.support.RowClassProvider;
import org.araneaframework.jsp.tag.uilib.form.FormElementTag;
import org.araneaframework.jsp.tag.uilib.form.FormTag;
import org.araneaframework.jsp.tag.uilib.list.BaseListRowsTag;
import org.araneaframework.jsp.tag.uilib.list.ListTag;
import org.araneaframework.jsp.tag.uilib.list.formlist.FormListTag;

/**
 * UI common utilities.
 * 
 * @author Oleg Mürk
 */
public class JspUtil {

  private static final Map<String, String> ATTR_ERROR = new HashMap<String, String>();

  static {
    ATTR_ERROR.put(AttributedTagInterface.ATTRIBUTED_TAG_KEY, null);
    ATTR_ERROR.put(FormListTag.FORM_LIST_ID_KEY, "&lt;ui:formList&gt; tag expected, but not found!");
    ATTR_ERROR.put(FormListTag.FORM_LIST_VIEW_MODEL_KEY, "&lt;ui:formList&gt; tag expected, but not found!");
    ATTR_ERROR.put(ElementHtmlTag.KEY, "&lt;ui:element&gt; tag expected, but not found! Probably this is an attempt to use &lt;ui:elementContent&gt; or &lt;ui:attribute&gt; outside &lt;ui:element&gt; tag.");
    ATTR_ERROR.put(FormElementTag.ID_KEY, "&lt;ui:formElement&gt; tag expected, but not found!  Make sure that form element and control tags either have an 'id' or are used inside &lt;ui:formElement&gt; tag.");
    ATTR_ERROR.put(FormTag.FORM_VIEW_MODEL_KEY, "&lt;ui:form&gt; tag expected, but not found! Make sure form element and control tags are used inside &lt;ui:form&gt; tag.");
    ATTR_ERROR.put(FormTag.FORM_FULL_ID_KEY, "&lt;ui:form&gt; tag expected, but not found! Make sure form element and control tags are used inside &lt;ui:form&gt; tag.");		
    ATTR_ERROR.put(FormTag.FORM_KEY, "&lt;ui:form&gt; tag expected, but not found! Make sure form element and control tags are used inside &lt;ui:form&gt; tag.");
    ATTR_ERROR.put(RowClassProvider.KEY, "&lt;ui:layout&gt; tag expected, but not found! Make sure that row tags are used inside &lt;ui:layout&gt; tag.");
    ATTR_ERROR.put(CellClassProvider.KEY, "&lt;ui:layout&gt; or &lt;ui:row&gt; expected, but not found! Make sure that row and cell tags are inside inside &lt;ui:layout&gt; tag.");
    ATTR_ERROR.put(ListTag.LIST_VIEW_MODEL_KEY, "&lt;ui:list&gt; tag expected, but not found! Make sure list tags is used inside &lt;ui:list&gt; tag.");
    ATTR_ERROR.put(ListTag.LIST_ID_KEY, "&lt;ui:list&gt; tag expected, but not found!  Make sure list tags is used inside &lt;ui:list&gt; tag.");
    ATTR_ERROR.put(BaseListRowsTag.ROW_REQUEST_ID_KEY, "&lt;ui:listRows&gt; or another list rows tag expected, but not found!");	
  }

  /**
   * Includes JSP page at given path.
   */
  public static void include(PageContext pageContext, String path) throws ServletException, IOException {
    // starting with '/' is absolute path (may add prefix), otherwise path is relative (unchanged).
    pageContext.include(path);
  }

  /**
   * Get resource string for given id. Throws MissingResourceException when given resource not found.
   */
  public static String getResourceString(PageContext pageContext, String id) {
    return getLocalizationContext(pageContext).getResourceBundle().getString(id);
  }

  /**
   * Get resource string for given id. Return null when given string not found
   */
  public static String getResourceStringOrNull(PageContext pageContext, String id) {
    try {
      return getLocalizationContext(pageContext).getResourceBundle().getString(id);
    } catch (MissingResourceException e) {
      return null;
    }
  }

  public static LocalizationContext getLocalizationContext(javax.servlet.jsp.JspContext jspContext) {
    return (LocalizationContext) getPageContext(jspContext).getRequest().getAttribute(
        ServletUtil.LOCALIZATION_CONTEXT_KEY);
  }

  /**
   * Writes opening of start tag: "<code>&lt;tag</code>".
   */
  public static void writeOpenStartTag(Writer out, String tag) throws IOException {
    out.write("<");
    out.write(tag);
  }

  /**
   * Writes closing of start tag: "<code>&gt;\n</code>".
   */
  public static void writeCloseStartTag(Writer out) throws IOException {
    out.write(">");
    out.write("\n");
  }

  /**
   * Writes closing of start tag (space sensitive): "<code>&gt;</code>".
   */
  public static void writeCloseStartTag_SS(Writer out) throws IOException {
    out.write(">");
  }

  /**
   * Writes closing of start tag that is also and end tag: "<code>/&gt;\n</code>".
   */
  public static void writeCloseStartEndTag(Writer out) throws IOException {
    out.write("/>");
    out.write("\n");
  }

  /**
   * Writes closing of start tag that is also and end tag (space sensitive):  "<code>/&gt;</code>".
   */
  public static void writeCloseStartEndTag_SS(Writer out) throws IOException {
    out.write("/>");
  }

  /**
   * Writes start tag: "<code>&lt;tag&gt;\n</code>".
   */
  public static void writeStartTag(Writer out, String tag) throws IOException {
    out.write("<");
    out.write(tag);
    out.write(">");
    out.write("\n");
  }

  /**
   * Writes end tag (space sensitive): "<code>&lt;tag&gt;</code>".
   */
  public static void writeStartTag_SS(Writer out, String tag) throws IOException {
    out.write("<");
    out.write(tag);
    out.write(">");
  }

  /**
   * Writes start tag that is also and end tag: "<code>&lt;tag/&gt;\n</code>".
   */
  public static void writeStartEndTag(Writer out, String tag) throws IOException {
    out.write("<");
    out.write(tag);
    out.write("/>");
    out.write("\n");
  }

  /**
   * Writes start tag that is also and end tag (space sensitive): "<code>&lt;tag/&gt;</code>".
   */
  public static void writeStartEndTag_SS(Writer out, String tag) throws IOException {
    out.write("<");
    out.write(tag);
    out.write("/>");
  }

  /**
   * Writes end tag: "<code>&lt;/tag&gt;\n</code>".
   */
  public static void writeEndTag(Writer out, String tag) throws IOException {
    out.write("</");
    out.write(tag);
    out.write(">");
    out.write("\n");
  }

  /**
   * Writes end tag (space sensitive): "<code>&lt;/tag&gt;</code>".
   */
  public static void writeEndTag_SS(Writer out, String tag) throws IOException {
    out.write("</");
    out.write(tag);
    out.write(">");
  }

  /**
   * Writes out attributes contained in the Map&lt;attributeName, attributeValue&gt;. If map is <code>null</code>,
   * writes nothing.
   */
  public static void writeAttributes(Writer out, Map<String, Object> attributes) throws IOException {
    if (attributes != null) {
      for (Map.Entry<String, Object> entry : attributes.entrySet()) {
        writeAttribute(out, entry.getKey(), entry.getValue());
      }
    }
  }

  public static void writeAttribute(Writer out, String name, Object value) throws IOException {
    writeAttribute(out, name, value, true);
  }

  /**
   * Writers attribute of form ' attrName"value"'. If value is <code>null</code>, writes nothing. If <code>escape</code>
   * is set to <code>true</code>, HTML escaping takes place on the value (&lt;, &gt;, &quot;, &amp; get replaced with
   * the entities).
   */
  public static void writeAttribute(Writer out, String name, Object value, boolean escape) throws IOException {
    if (value != null && StringUtils.isNotBlank(name) && StringUtils.isNotBlank(value.toString())) {
      out.write(" ");
      out.write(name);
      out.write("=\"");
      if (escape) {
        writeEscapedAttribute(out, value.toString());
      } else {
        out.write(value.toString());
      }
      out.write("\"");
    }
  }

  /**
   * Writes opening of attribute: '<code> attrName="</code>'.
   */
  public static void writeOpenAttribute(Writer out, String name) throws IOException {
    out.write(" ");
    out.write(name);
    out.write("=\"");
  }

  /**
   * Writes closing of attribute: '<code>"</code>'.
   */
  public static void writeCloseAttribute(Writer out) throws IOException {
    out.write("\"");
  }

  /**
   * Writes script string. Equivalent to <code>writeScriptString(out, value, true)</code>
   */
  public static void writeScriptString(Writer out, String value) throws IOException {
    writeScriptString(out, value, true);
  }

  /**
   * Writes given String properly formatted for javascript in single quotes.
   * 
   * @param escapeEntities set it to true if you want to escape XML entities like &amp;amp;, &lt;lt; etc.
   * @see #writeEscapedScriptString(Writer, String, boolean)
   */
  public static void writeScriptString(Writer out, String value, boolean escapeEntities) throws IOException {
    out.write("'");
    writeEscapedScriptString(out, value, escapeEntities);
    out.write("'");
  }

  /**
   * Writes script string or expression evaluating to a string.
   * 
   * @param out
   * @param value string value
   * @param expression expression
   * @throws AraneaJspException if both value and expression are specified
   */
  public static void writeScriptString_rt(Writer out, String value, String expression) throws IOException,
      AraneaJspException {
    if (value != null && expression != null) {
      throw new AraneaJspException("String value and run-time expression should not be specified at the same time");
    }

    if (expression != null) {
      writeEscaped(out, expression);
    } else {
      out.write("'");
      writeEscapedScriptString(out, value, true);
      out.write("'");
    }
  }

  /**
   * Writes out escaped string. <code>null</code> values are omitted.
   */
  public static void writeEscaped(Writer out, String value) throws IOException {
    if (value != null) {
      out.write(StringEscapeUtils.escapeHtml(value));
    }
  }

  /**
   * Writes out escaped attribute string. <code>null</code> values are omitted.
   */
  public static void writeEscapedAttribute(Writer out, String value) throws IOException {
    StringEscapeUtils.escapeHtml(out, value);
  }

  /**
   * Writes out escaped script string (can be used also in attributes). <code>null</code> values are omitted. When
   * <code>escapeEntities</code> is <code>true</code>, standard XML entities are escaped from greater-than, less-than,
   * ampersand and double-quotes symbols. Otherwise, these symbols are left as they are. Set <code>escapeEntities</code>
   * to <code>true</code>, if you write javascript inside an attribute, and set it to false if you write javascript
   * inside a &lt;script&gt; tag.
   * 
   * @param escapeEntities Whether entities in <code>value</code> should be escaped.
   */
  public static void writeEscapedScriptString(Writer out, String value, boolean escapeEntities) {
    try {
      StringEscapeUtils.escapeHtml(out, StringEscapeUtils.escapeJavaScript(value));
    } catch (Exception e) {
      ExceptionUtil.uncheckException(e);
    }
  }

  public static void writeEventAttributes(Writer out, UiEvent event) throws IOException {
    out.write(" ");
    out.write(event.getEventAttributes().toString());
    out.write(" ");
  }

  /**
   * Writes out hidden HTML input element with give name and value.
   */
  public static void writeHiddenInputElement(Writer out, String name, String value) throws IOException {
    JspUtil.writeOpenStartTag(out, "input");
    JspUtil.writeAttribute(out, "name", name);
    JspUtil.writeAttribute(out, "type", "hidden");
    JspUtil.writeAttribute(out, "value", value);
    JspUtil.writeCloseStartEndTag(out);
  }

  /**
   * Parses multi-valued attribute, where attributes are separated by commas. Empty attribute values are allowed, they
   * are specified by including whitespace between commas: "first, ,third".
   * 
   * @param attribute The regions attribute containing multiple values separated commas.
   * @return List&lt;String&gt; containing attribute values.
   */
  public static List<String> parseMultiValuedAttribute(String attribute) {
    List<String> result = new LinkedList<String>();
    if (!StringUtils.isBlank(attribute)) {
      StringTokenizer tokens = new StringTokenizer(attribute, ",");
      while (tokens.hasMoreTokens()) {
        result.add(tokens.nextToken().trim());
      }
    }
    return result;
  }

  /**
   * Read attribute value from request scope.
   */
  public static Object getContextEntry(PageContext pageContext, String key) {
    return pageContext.getAttribute(key, PageContext.REQUEST_SCOPE);
  }

  /**
   * Read attribute value from request scope and ensure that it is defined.
   * 
   * @throws AraneaJspException if key is not present in given <code>PageContext</code>
   */
  public static Object requireContextEntry(PageContext pageContext, String key) throws JspException {
    Object value = pageContext.getAttribute(key, PageContext.REQUEST_SCOPE);
    if (value == null) {
      String errMsg = JspUtil.ATTR_ERROR.get(key);
      StringBuffer message = new StringBuffer();

      if (errMsg != null) {
        message.append(errMsg).append(" (");
      }

      message.append("Missing attribute '").append(key).append("' in 'PageContext.REQUEST_SCOPE' scope");

      if (errMsg != null) {
        message.append(")");
      }

      throw new AraneaJspException(message.toString());
    } else {
      return value;
    }
  }

  /**
   * Retrieves PageContext from a JSP context. This mean basically casting, but sometimes is simpler.
   * 
   * @param jspContext A regular JSP context.
   * @return A PageContext.
   */
  public static PageContext getPageContext(javax.servlet.jsp.JspContext jspContext) {
    return (PageContext) jspContext;
  }

  /**
   * Retrieves Aranea Environment form JSP context.
   * 
   * @param jspContext A regular JSP context.
   * @return An Environment object.
   * @since 1.2.2
   */
  public static Environment getEnvironment(javax.servlet.jsp.JspContext jspContext) {
    return ServletUtil.getEnvironment(getPageContext(jspContext).getRequest());
  }

  /**
   * Retrieves JSP configuration data context.
   * 
   * @param jspContext A regular JSP context.
   * @return JSP configuration context.
   * @since 1.2.2
   */
  public static JspContext getConfiguration(javax.servlet.jsp.JspContext jspContext) {
    return getEnvironment(jspContext).requireEntry(JspContext.class);
  }

  public static boolean hasOverlayMarker(javax.servlet.jsp.JspContext jspContext) {
    return getEnvironment(jspContext).getEntry(OverlayActivityMarkerContext.class) != null;
  }

  public static Set<Map.Entry<String, String>> getSystemFormContextEntries(javax.servlet.jsp.JspContext jspContext) {
    SystemFormContext systemFormContext = EnvironmentUtil.requireSystemFormContext(getEnvironment(jspContext));
    return systemFormContext.getFields().entrySet();
  }

  public static String getServletURL(javax.servlet.jsp.JspContext jspContext) {
    PageContext pageContext = getPageContext(jspContext);
    return ServletUtil.getInputData(pageContext.getRequest()).getContainerURL();
  }

  public static String getFormActionURL(javax.servlet.jsp.JspContext jspContext) {
    ServletRequest request = getPageContext(jspContext).getRequest();
    String servletUrl = ServletUtil.getInputData(request).getContainerURL();
    return ServletUtil.getOutputData(request).encodeURL(servletUrl);
  }
}