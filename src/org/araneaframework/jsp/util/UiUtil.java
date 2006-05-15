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

package org.araneaframework.jsp.util;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import org.araneaframework.jsp.UiException;
import org.araneaframework.jsp.container.UiWidgetContainer;
import org.araneaframework.jsp.tag.UiPresentationTag;
import org.araneaframework.jsp.tag.basic.UiAttributedTagInterface;
import org.araneaframework.jsp.tag.basic.UiElementTag;
import org.araneaframework.jsp.tag.form.UiSystemFormTag;
import org.araneaframework.jsp.tag.layout.CellClassProvider;
import org.araneaframework.jsp.tag.layout.RowClassProvider;
import org.araneaframework.jsp.tag.uilib.form.UiFormElementTag;
import org.araneaframework.jsp.tag.uilib.form.UiFormTag;
import org.araneaframework.jsp.tag.uilib.list.UiListRowsTag;
import org.araneaframework.jsp.tag.uilib.list.UiListTag;
import org.araneaframework.jsp.tag.uilib.list.formlist.UiFormListTag;

/**
 * UI common utilities.
 * 
 * @author Oleg Mürk
 */
public class UiUtil {
  private static final Map attributeErrorMap = new HashMap();  
  static {
    attributeErrorMap.put(UiAttributedTagInterface.ATTRIBUTED_TAG_KEY, null);
    attributeErrorMap.put(UiPresentationTag.ATTRIBUTED_TAG_KEY, null);

    attributeErrorMap.put(UiFormListTag.FORM_LIST_ID_KEY, "<ui:formList> tag expected, but not found!");
    attributeErrorMap.put(UiFormListTag.FORM_LIST_VIEW_MODEL_KEY, "<ui:formList> tag expected, but not found!");

    attributeErrorMap.put(UiElementTag.KEY, "<ui:element> tag expected, but not found! Probably this is an attempt to use <ui:elementContent> or <ui:attribute> outside <ui:element> tag.");

    attributeErrorMap.put(UiFormElementTag.ID_KEY, "<ui:formElement> tag expected, but not found!  Make sure that form element and control tags either have an 'id' or are used inside <ui:formElement> tag.");

    attributeErrorMap.put(UiFormTag.FORM_SCOPED_FULL_ID_KEY, "<ui:form> tag expected, but not found! Make sure form element and control tags are used inside <ui:form> tag.");
    attributeErrorMap.put(UiFormTag.FORM_VIEW_MODEL_KEY, "<ui:form> tag expected, but not found! Make sure form element and control tags are used inside <ui:form> tag.");
    attributeErrorMap.put(UiFormTag.FORM_FULL_ID_KEY, "<ui:form> tag expected, but not found! Make sure form element and control tags are used inside <ui:form> tag.");		
    attributeErrorMap.put(UiFormTag.FORM_KEY, "<ui:form> tag expected, but not found! Make sure form element and control tags are used inside <ui:form> tag.");

    attributeErrorMap.put(RowClassProvider.KEY, "<ui:layout> tag expected, but not found! Make sure that row tags are used inside <ui:layout> tag.");
    attributeErrorMap.put(CellClassProvider.KEY, "<ui:layout> or <ui:row> expected, but not found! Make sure that row and cell tags are inside inside <ui:layout> tag.");

    attributeErrorMap.put(UiListTag.LIST_VIEW_MODEL_KEY, "<ui:list> tag expected, but not found! Make sure list tags is used inside <ui:list> tag.");
    attributeErrorMap.put(UiListTag.LIST_ID_KEY, "<ui:list> tag expected, but not found!  Make sure list tags is used inside <ui:list> tag.");
    attributeErrorMap.put(UiListRowsTag.ROW_REQUEST_ID_KEY, "<ui:listRows> or another list rows tag expected, but not found!");	
    attributeErrorMap.put(UiSystemFormTag.ID_KEY, "<ui:systemForm> tag expected, but not found! Make sure your tags are surrounded by <ui:systemForm>.");
    attributeErrorMap.put(UiWidgetContainer.REQUEST_CONTEXT_KEY, "<ui:viewPort> or another widget container tag expected, but not found!");
  }

  /**
   * Includes JSP page at given path.
   */
  public static void include(PageContext pageContext, String path) throws ServletException, IOException {
	// starting with '/' is absolute path (may add prefix), otherwise path is relative (unchanged).
    pageContext.include(path.startsWith("/") ? "/content" + path : path);
  }
  
  /**
   * Get resource string for given id.
   * Throws MissingResourceException when given resource not found.
   */
  public static String getResourceString(PageContext pageContext, String id) {
    return getLocalizationContext(pageContext).getResourceBundle().getString(id);
  }
  
  /**
   * Get resource string for given id.
   * Return null when given string not found
   */
  public static String getResourceStringOrNull(PageContext pageContext, String id) {
  	try {
  		return getLocalizationContext(pageContext).getResourceBundle().getString(id);
  	}
  	catch(MissingResourceException e){
  		return null;
  	}
  }
  
  
  public static LocalizationContext getLocalizationContext(PageContext pageContext) {
  	return (LocalizationContext)Config.get(pageContext, Config.FMT_LOCALIZATION_CONTEXT, PageContext.REQUEST_SCOPE);
  }
  
  //
  // XHTML output methods
  //

  /**
   * Writes opening of start tag
   */
  public static void writeOpenStartTag(Writer out, String tag) throws IOException {
  	out.write("<");
  	out.write(tag);
  }
  
  /**
   * Writes closing of start tag
   */
  public static void writeCloseStartTag(Writer out) throws IOException {
    out.write(">");
    out.write("\n");
  }

  /**
   * Writes closing of start tag. Space sensitive.
   */
  public static void writeCloseStartTag_SS(Writer out) throws IOException {
    out.write(">");   
  }

  /**
   * Writes closing of start tag that is also and end tag. 
   */
  public static void writeCloseStartEndTag(Writer out) throws IOException {
    out.write("/>");
    out.write("\n");
  }

  /**
   * Writes closing of start tag that is also and end tag. Space sensitive. 
   */
  public static void writeCloseStartEndTag_SS(Writer out) throws IOException {
    out.write("/>");
  }

  /**
   * Writes start tag.
   */
  public static void writeStartTag(Writer out, String tag) throws IOException {
    out.write("<");
    out.write(tag);
    out.write(">");
    out.write("\n");
  }
    
  /**
   * Writes end tag. Space sensitive.
   */
  public static void writeStartTag_SS(Writer out, String tag) throws IOException {
    out.write("<");
    out.write(tag);
    out.write(">");
  }
    
  /**
   * Writes start tag that is also and end tag.
   */
  public static void writeStartEndTag(Writer out, String tag) throws IOException {
    out.write("<");
    out.write(tag);
    out.write("/>");    
    out.write("\n");
  }
    
  /**
   * Writes start tag that is also and end tag. Space sensitive.
   */
  public static void writeStartEndTag_SS(Writer out, String tag) throws IOException {
    out.write("<");
    out.write(tag);
    out.write("/>");
  }
  
  /**
   * Writes end tag.
   */
  public static void writeEndTag(Writer out, String tag) throws IOException {
    out.write("</");
    out.write(tag);
    out.write(">");
    out.write("\n");
  } 
  /**
   * Writes end tag. Space sensitive.
   */
  public static void writeEndTag_SS(Writer out, String tag) throws IOException {
    out.write("</");
    out.write(tag);
    out.write(">");
  }

  
  /**
   * Writes out attributes contained in the map.
   * If map is <code>null</code>, writes nothing.
   */
  public static void writeAttributes(Writer out, Map attributes) throws IOException {
    if (attributes == null) return;
    
    for(Iterator i = attributes.keySet().iterator(); i.hasNext();) {
      String name = (String)i.next();
      String value = attributes.get(name).toString();
      
      UiUtil.writeAttribute(out, name, value);
    }    
  }


  /**
   * Writers attribute of form ' ATTR_NAME="value"'.
   * If value is <code>null</code>, writes nothing.
   * If escape is set to true HTML escaping takes place on 
   * the value (&lt;, &gt;, &quot;, &amp; get replaced with
   * the entities).
   */
  public static void writeAttribute(Writer out, String name, Object value, boolean escape) throws IOException {
    if (value == null) return;
     
    out.write(" ");
    out.write(name);
    out.write("=\"");
    if (escape) 
    	writeEscapedAttribute(out, value.toString());
    else
    	out.write(value.toString());
    out.write("\"");
  }
  
  public static void writeAttribute(Writer out, String name, Object value) throws IOException {
  	writeAttribute(out, name, value, true);
  }

  /**
   * Writes opening of attribute.
   */
  public static void writeOpenAttribute(Writer out, String name) throws IOException {
    out.write(" ");
    out.write(name);
    out.write("=\"");
  }
  
  /**
   * Writes closing of attribute.
   */
  public static void writeCloseAttribute(Writer out) throws IOException {
    out.write("\"");
  }

  /**
   * Writes script string.
   * Equivalent to <code>writeScriptString(out, value, true)</code>
   */  
  public static void writeScriptString(Writer out, String value) throws IOException {
    out.write("'");
    writeEscapedScriptString(out, value, true);
    out.write("'");
  }

  /**
   * Writes given String properly formatted for javascript in single quotes.
   * @param escapeEntities set it to true if you want to escape XML entities like &amp;amp;, &lt;lt; etc.
   * @see #writeEscapedScriptString
   */
  public static void writeScriptString(Writer out, String value, boolean escapeEntities) throws IOException{
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
   * 
   * @throws UiException if both value and expression are specified
   */  
  public static void writeScriptString_rt(Writer out, String value, String expression) throws IOException, UiException {
    if (value != null && expression != null)
      throw new UiException("String value and run-time expression should not be specified at the same time"); 
    
    if (expression != null) {
      writeEscaped(out, expression);
    }
    else {
      out.write("'");    
      writeEscapedScriptString(out, value, true);
      out.write("'");
    }
  }  

  /**
   * Writes out escaped string. <code>null</code> values are omitted. 
   */
  public static void writeEscaped(Writer out, String value) throws IOException {
    if (value == null) return;
    
    for(int i = 0; i < value.length(); i++) {
      char c = value.charAt(i);  
      writeEscaped(out, c);
    }
  }
  
  /**
   * Writes out escaped character.
   */
  public static void writeEscaped(Writer out, char value) throws IOException {
    switch (value) {
      case '<': out.write("&lt;"); break;
      case '>': out.write("&gt;"); break;
      case '&': out.write("&amp;"); break;
      case '"': out.write("&quot;"); break;
      default:
        out.write(value);
    }     
  }
  
  /**
   * Writes out escaped attribute string. <code>null</code> values are omitted. 
   */
  public static void writeEscapedAttribute(Writer out, String value) throws IOException {
    if (value == null) return;
    
    for(int i = 0; i < value.length(); i++) {
      char c = value.charAt(i);
      switch (c) {
        case '<': out.write("&lt;"); break;
        case '>': out.write("&gt;"); break;
        case '&': out.write("&amp;"); break;
        case '"': out.write("&quot;"); break;
        case '\n': out.write("&xA;"); break;        
        default:
          out.write(c);
      }     
    }
  }
  
  /**
   * Writes out escaped script string (can be used also in attributes). <code>null</code> values are omitted.
   * @param escapeEntities - when this is true, standard XML entities are used to escape
   *        greater-than, less-than, ampersand and double-quotes. Otherwise, these symbols are
   *        left as they are. Set this attribute to true, if you write javascript inside an attribute,
   *        and set it to false if you write javascript inside a &lt;script&gt; tag. 
   */
  public static void writeEscapedScriptString(Writer out, String value, boolean escapeEntities) throws IOException {
    if (value == null) return;
        
    for(int i = 0; i < value.length(); i++) {
      char c = value.charAt(i);
      switch (c) {
        case '<': out.write(escapeEntities ? "&lt;" : "<"); break;
        case '>': out.write(escapeEntities ? "&gt;" : ">"); break;
        case '&': out.write(escapeEntities ? "&amp;": "&"); break;
        case '"': out.write(escapeEntities ? "&quot;": "\""); break;
        case '\'': out.write("\\'"); break;
        case '\n': out.write("\\n"); break;        
        default:
          out.write(c);
      }     
    }
  }
  
  /**
   * Writes out hidden html input element with give name and value.
   * 
   * @author Nikita Salnikov-Tarnovski
   */ 
  public static void writeHiddenInputElement(Writer out, String name, String value) throws IOException {
    UiUtil.writeOpenStartTag(out, "input");
    UiUtil.writeAttribute(out, "name", name);
    UiUtil.writeAttribute(out, "type", "hidden");
    UiUtil.writeAttribute(out, "value", value);
    UiUtil.writeCloseStartEndTag(out);
  }
  
  
  /**
   * Parses multi-valued attribute, where attributes are separated by commas.
   * Empty attribute values are allowed, they are specified by including whitespace
   * between commas: "first, ,third".
   * @return List&lt;String&gt; containing attribute values. 
   */
  public static List parseMultiValuedAttribute(String attribute) {
    List result = new ArrayList();

    if (attribute != null && !"".equals(attribute.trim())) {
      StringTokenizer tokens = new StringTokenizer(attribute, ",");
      while (tokens.hasMoreTokens())
        result.add(tokens.nextToken().trim());
    }

    return result;
  }
  
  // -------------- Operations with PageContext ------------------- //
  
  /**
   * Read attribute value in given scope and ensure that it is defined.  
   */
  public static Object requireContextEntry(PageContext pageContext, String key) throws JspException {
    Object value = pageContext.getAttribute(key, PageContext.REQUEST_SCOPE);
    if (value == null) {
      StringBuffer message = new StringBuffer();
      String errMsg = (String)attributeErrorMap.get(key);      
      if (errMsg != null) 
        message.append(errMsg + " (");
      message.append("Missing attribute '" + key + "' in ");
      message.append("'PageContext.REQUEST_SCOPE'");
      message.append(" scope");
      if (errMsg != null) 
        message.append(")");
      throw new UiException(message.toString());
    }
    else
      return value;
  }
}
