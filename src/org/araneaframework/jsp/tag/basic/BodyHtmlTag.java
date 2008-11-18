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

package org.araneaframework.jsp.tag.basic;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.servlet.jsp.JspException;
import org.araneaframework.framework.ConfirmationContext;
import org.araneaframework.framework.ExpiringServiceContext;
import org.araneaframework.http.util.EnvironmentUtil;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.ConfigurationContext;

/**
 * Aranea HTML body tag. 
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * 
 * @jsp.tag
 *   name = "body"
 *   body-content = "JSP"
 *   description = "HTML BODY tag with some Aranea JSP additions."
 */
public class BodyHtmlTag extends PresentationTag {
  public static final String ONLOAD = "_ap.onload()";
  public static final String ONUNLOAD = "_ap.onunload()";
  
  public static final String KEY = "org.araneaframework.jsp.tag.basic.BodyHtmlTag";
  
  protected String onload = ONLOAD;
  protected String onunload = ONUNLOAD;
  
  /** Another bunch of HTML attributes. */
  protected String id;
  protected String title;
  protected String lang;
  protected String dir;

  /** Scripts registered by nested tags. */
  protected StringBuffer afterBodyEndScripts = null;
  
  protected int doStartTag(Writer out) throws Exception {
    int r = super.doStartTag(out);

    addContextEntry(AttributedTagInterface.ATTRIBUTED_TAG_KEY, null);
    addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, null);
    addContextEntry(KEY, this);
    
    JspUtil.writeOpenStartTag(out, "body");
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "onload", onload);
    JspUtil.writeAttribute(out, "onunload", onunload);
    
    JspUtil.writeAttribute(out, "id", id);
    JspUtil.writeAttribute(out, "title", title);
    JspUtil.writeAttribute(out, "lang", lang);
    JspUtil.writeAttribute(out, "dir", dir);

    JspUtil.writeCloseStartTag_SS(out);
    
    writeAfterBodyStartScripts(out);
    
    return r;
  }

  protected int doEndTag(Writer out) throws Exception {
    JspUtil.writeEndTag(out, "body");
    return super.doEndTag(out);
  }
  
  /* ***********************************************************************************
   * Methods for outputting scripts after opening and closing of HTML <body> tag.
   * ***********************************************************************************/
  
  /**
   * Writes the scripts immediately following the opening of &lt;body&gt; tag.
   */
  protected void writeAfterBodyStartScripts(Writer out) throws Exception {
    JspUtil.writeOpenStartTag(out, "script");
    JspUtil.writeAttribute(out, "type", "text/javascript");
    JspUtil.writeCloseStartTag_SS(out);
    
    writeServletURLScript(out);
    writeLocaleScript(out);
    writeKeepAliveRegistrationScripts(out);
    writeAjaxValidationScript(out);
    writeConfirmationScript(out);

    writeAdditionalAfterBodyStartScripts(out);

    JspUtil.writeEndTag(out, "script");
  }

  /** @since 1.1 */
  protected void writeConfirmationScript(Writer out) throws Exception {
    ConfirmationContext ctx = (ConfirmationContext) getEnvironment().getEntry(ConfirmationContext.class);
    if (ctx == null) return;

    String message = ctx.getConfirmationMessage();
    if (message != null) {
      out.write("_ap.addClientLoadEvent(function(){ Aranea.UI.flowEventConfirm('" + message + "');});");
    }
  }

  /** Writes scripts that register client-side keepalive events for server-side expiring services. */
  protected void writeKeepAliveRegistrationScripts(Writer out) throws IOException {
    ExpiringServiceContext expiringServiceContext = EnvironmentUtil.getExpiringServiceContext(getEnvironment());
    if (expiringServiceContext == null)
      return;
	  Map expiringServiceMap = expiringServiceContext.getServiceTTLMap();
    if (expiringServiceMap != null && !expiringServiceMap.isEmpty()) { // there are some expiring services
      for (Iterator i = expiringServiceMap.entrySet().iterator(); i.hasNext();) {
        Map.Entry entry = (Map.Entry) i.next();
        Object keepAliveKey = "'"+ entry.getKey() + "'";
        // TODO: keepalives are just invoked a little (4 seconds) more often from client side,
        // than specified in configuration, there could be a better way.
        Long serviceTTL = new Long((((Long) entry.getValue()).longValue() - 4000));

        Object topServiceId = EnvironmentUtil.getTopServiceId(getEnvironment());
        Object threadServiceId = EnvironmentUtil.getThreadServiceId(getEnvironment());

        String sTop = topServiceId == null ? "null" : "'" + topServiceId.toString() + "'";
        String sThread = threadServiceId == null ? "null" : "'" + threadServiceId.toString() + "'";

        out.write("\n_ap.addKeepAlive(AraneaPage.getDefaultKeepAlive(" + 
        		sTop + "," + 
        		sThread + "," + keepAliveKey + ")," + serviceTTL.toString() + ");\n");
      }
    }
  }

  /** Writes script that makes client-side aware of container URL. */
  protected void writeServletURLScript(Writer out) throws IOException {
    String servletUrl =
        ServletUtil.getInputData(pageContext.getRequest()).getContainerURL();

    String encodedServletUrl = 
    	ServletUtil.getOutputData(pageContext.getRequest()).encodeURL(servletUrl);
    
    out.write("_ap.setServletURL('");
    out.write(servletUrl);
    out.write("');");
    
    if (!servletUrl.equals(encodedServletUrl)) {
      String urlSuffix = encodedServletUrl.substring(servletUrl.length());
      String function = "function(url) { return (url + '" + urlSuffix + "'); }";
      out.write("_ap.override('encodeURL'," + function + ");");
    }
  }

  /** Writes script that makes client-side aware of server-side locale. */
  protected void writeLocaleScript(Writer out) throws IOException {
	Locale locale = getLocalizationContext().getLocale();

    out.write("_ap.setLocale(new AraneaLocale('");
    out.write(locale.getLanguage());
    out.write("','");
    out.write(locale.getCountry());
    out.write("'));");
  }
  
  /** 
   * Writes script that sets the whether Uilib {@link org.araneaframework.uilib.form.FormWidget}'s should be validated
   * seamlessly on the background with the actions or not.
   * 
   * @see ConfigurationContext#BACKGROUND_FORM_VALIDATION
   * @since 1.1 */
  protected void writeAjaxValidationScript(Writer out) throws IOException {
    Boolean validationEnabled = (Boolean) getConfiguration().getEntry(ConfigurationContext.BACKGROUND_FORM_VALIDATION);
    out.write("_ap.setBackgroundValidation(" + String.valueOf(validationEnabled) +");");
  }

  /**
   * Writes the scripts immediately following the closing of &lt;body&gt; tag.
   */
  protected void writeAfterBodyEndScripts(Writer out) throws Exception {
    JspUtil.writeOpenStartTag(out, "script");
    JspUtil.writeAttribute(out, "type", "text/javascript");
    JspUtil.writeCloseStartTag_SS(out);
    
    writeAdditionalAfterBodyEndScripts(out);

    JspUtil.writeEndTag(out, "script");
  }
  
  /**
   * Called before closing the script tag immediately following the HTML &lt;body&gt; start, use for
   * additional client-side page (AraneaPage) initialization.
   */
  protected void writeAdditionalAfterBodyStartScripts(Writer out) throws Exception {}
  
  /**
   * Called before closing the script tag immediately following the HTML &lt;body&gt; start, use for
   * additional client-side page (AraneaPage) initialization.
   */
  protected void writeAdditionalAfterBodyEndScripts(Writer out) throws Exception {
    out.write(afterBodyEndScripts.toString());
  }
  
  /**
   * Nested tags should get surrounding body tag from <code>PageContext</code> and register
   * their body end scripts with this method.
   */
  public void addAfterBodyEndScript(String script) {
    if (afterBodyEndScripts == null)
      afterBodyEndScripts = new StringBuffer();
    afterBodyEndScripts.append(script);
  }

  /* ***********************************************************************************
   * Tag attributes
   * ***********************************************************************************/

  /**
   * @jsp.attribute
   * type = "java.lang.String"
   * required = "false"
   * description = "Overwrite the standard Aranea JSP HTML body onload event. Use with caution."
   */
  public void setOnload(String onload) throws JspException {
    this.onload = (String) evaluate("onload", onload, String.class);
  }
  
  /**
   * @jsp.attribute
   * type = "java.lang.String"
   * required = "false"
   * description = "Overwrite the standard Aranea JSP HTML body onunload event. Use with caution."
   */
  public void setOnunload(String onunload) throws JspException {
    this.onunload = (String) evaluate("onunload", onunload, String.class);
  }

  /**
   * @jsp.attribute
   * type = "java.lang.String"
   * required = "false"
   * description = "Text direction."
   */
  public void setDir(String dir) {
    this.dir = dir;
  }
  
  /**
   * @jsp.attribute
   * type = "java.lang.String"
   * required = "false"
   * description = "HTML body id"
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * @jsp.attribute
   * type = "java.lang.String"
   * required = "false"
   * description = "Language information."
   */
  public void setLang(String lang) {
    this.lang = lang;
  }

  /**
   * @jsp.attribute
   * type = "java.lang.String"
   * required = "false"
   * description = "Title."
   */
  public void setTitle(String title) {
    this.title = title;
  }
}
