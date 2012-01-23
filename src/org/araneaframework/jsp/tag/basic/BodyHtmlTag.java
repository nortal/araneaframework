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

package org.araneaframework.jsp.tag.basic;

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;
import org.araneaframework.framework.ConfirmationContext;
import org.araneaframework.framework.ExpiringServiceContext;
import org.araneaframework.http.util.EnvironmentUtil;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.tag.updateregion.UpdateRegionHtmlTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.util.ConfigurationUtil;

/**
 * Aranea HTML body tag.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * 
 * @jsp.tag
 *  name = "body"
 *  body-content = "JSP"
 *  description = "HTML BODY tag with some Aranea JSP additions."
 */
public class BodyHtmlTag extends PresentationTag {

  public static final String KEY = "org.araneaframework.jsp.tag.basic.BodyHtmlTag";

  protected String onload;

  protected String onunload;

  /**
   * HTML tag attribute.
   */
  protected String id;

  /**
   * HTML tag attribute.
   */
  protected String title;

  /**
   * HTML tag attribute.
   */
  protected String lang;

  /**
   * HTML tag attribute.
   */
  protected String dir;

  /**
   * Scripts registered by nested tags?
   */
  protected StringBuffer afterBodyEndScripts = null;

  @Override
  protected int doStartTag(Writer out) throws Exception {
    int result = super.doStartTag(out);

    addContextEntry(AttributedTagInterface.ATTRIBUTED_TAG_KEY, null);
    addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, null);
    addContextEntry(BodyHtmlTag.KEY, this);

    JspUtil.writeOpenStartTag(out, "body");
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "onload", this.onload);
    JspUtil.writeAttribute(out, "onunload", this.onunload);

    JspUtil.writeAttribute(out, "id", this.id);
    JspUtil.writeAttribute(out, "title", this.title);
    JspUtil.writeAttribute(out, "lang", this.lang);
    JspUtil.writeAttribute(out, "dir", this.dir);

    JspUtil.writeCloseStartTag_SS(out);

    UpdateRegionHtmlTag updateRegionHtmlTag = new UpdateRegionHtmlTag();
    registerSubtag(updateRegionHtmlTag);
    updateRegionHtmlTag.setPageContext(pageContext);
    updateRegionHtmlTag.setGlobalId("araBodyScripts");
    executeStartSubtag(updateRegionHtmlTag);
    writeAfterBodyStartScripts(out);
    executeEndSubtag(updateRegionHtmlTag);
    
    return result;
  }

  @Override
  protected int doEndTag(Writer out) throws Exception {
    JspUtil.writeEndTag(out, "body");
    return super.doEndTag(out);
  }

  /* ***********************************************************************************
   * Methods for outputting scripts after opening and closing of HTML <body> tag.
   * *********************************************************************************
   */

  /**
   * Writes the scripts immediately following the opening of &lt;body&gt; tag.
   * 
   * @param out The writer of the rendered page.
   * @throws Exception Any exception that may occur.
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

  /**
   * Writes the script that causes the confirmation message to be shown.
   * 
   * @param out The writer of the rendered page.
   * @throws Exception Any exception that may occur.
   * @since 1.1
   */
  protected void writeConfirmationScript(Writer out) throws Exception {
    ConfirmationContext ctx = getEnvironment().getEntry(ConfirmationContext.class);
    if (ctx != null) {
      if (ctx.getConfirmationMessage() != null) {
        out.write("document.observe('aranea:loaded', Aranea.UI.confirmFlowEvent.curry('");
        out.write(ctx.getConfirmationMessage());
        out.write("'));");
      }
    }
  }

  /**
   * Writes scripts that register client-side keepalive events for server-side expiring services.
   * 
   * @param out The writer of the rendered page.
   * @throws IOException Any exception that may occur.
   */
  protected void writeKeepAliveRegistrationScripts(Writer out) throws IOException {
    ExpiringServiceContext expiringServiceContext = EnvironmentUtil.getExpiringServiceContext(getEnvironment());
    if (expiringServiceContext == null) {
      return;
    }
    Map<String, Long> expiringServiceMap = expiringServiceContext.getServiceTTLMap();
    if (expiringServiceMap != null && !expiringServiceMap.isEmpty()) { // there are some expiring services
      for (Map.Entry<String, Long> entry : expiringServiceMap.entrySet()) {

        // TODO: KeepAlives are just invoked a little (4 seconds) more often from client side,
        // than specified in configuration, there could be a better way.

        String serviceTTL = Long.toString(entry.getValue() - 4000);
        String topServiceId = EnvironmentUtil.getTopServiceId(getEnvironment());
        String threadServiceId = EnvironmentUtil.getThreadServiceId(getEnvironment());

        String sTop = topServiceId == null ? "null" : "'" + topServiceId.toString() + "'";
        String sThread = threadServiceId == null ? "null" : "'" + threadServiceId.toString() + "'";

        out.write("\nAranea.Page.addKeepAlive(Aranea.Page.getDefaultKeepAlive(");
        out.write(sTop);
        out.write(",");
        out.write(sThread);
        out.write(",'");
        out.write(entry.getKey());
        out.write("'),");
        out.write(serviceTTL);
        out.write(");\n");
      }
    }
  }

  /**
   * Writes script that makes client-side aware of container URL.
   * 
   * @param out The writer of the rendered page.
   * @throws IOException Any exception that may occur.
   */
  protected void writeServletURLScript(Writer out) throws IOException {
    String servletUrl = ServletUtil.getInputData(this.pageContext.getRequest()).getContainerURL();

    String encodedServletUrl = ServletUtil.getOutputData(this.pageContext.getRequest()).encodeURL(servletUrl);

    out.write("Aranea.Data.servletURL='");
    out.write(servletUrl);
    out.write("';");

    if (!servletUrl.equals(encodedServletUrl)) {
      String urlSuffix = encodedServletUrl.substring(servletUrl.length());
      out.write("Object.extend(Aranea.Page,{encodeURL:function(url){return(url+'");
      out.write(urlSuffix);
      out.write("')}});");
    }
  }

  /**
   * Writes script that makes client-side aware of server-side locale.
   * 
   * @param out The writer of the rendered page.
   * @throws IOException Any exception that may occur.
   */
  protected void writeLocaleScript(Writer out) throws IOException {
    Locale locale = getLocalizationContext().getLocale();
    out.write("Object.extend(Aranea.Data.locale,{lang:'");
    out.write(locale.getLanguage());
    out.write("',country:'");
    out.write(locale.getCountry());
    out.write("'});");
  }

  /**
   * Writes script that sets the whether Uilib {@link org.araneaframework.uilib.form.FormWidget}'s should be validated
   * seamlessly on the background with the actions or not.
   * 
   * @param out The writer of the rendered page.
   * @throws IOException Any exception that may occur.
   * @see ConfigurationContext#BACKGROUND_FORM_VALIDATION
   * @since 1.1
   */
  protected void writeAjaxValidationScript(Writer out) throws IOException {
    boolean validationEnabled = ConfigurationUtil.isBackgroundFormValidationEnabled(getEnvironment());
    out.write("Aranea.Data.backgroundValidation=" + validationEnabled + ";");
  }

  /**
   * Writes the scripts immediately following the closing of &lt;body&gt; tag.
   * 
   * @param out The writer of the rendered page.
   * @throws Exception Any exception that may occur.
   */
  protected void writeAfterBodyEndScripts(Writer out) throws Exception {
    JspUtil.writeOpenStartTag(out, "script");
    JspUtil.writeAttribute(out, "type", "text/javascript");
    JspUtil.writeCloseStartTag_SS(out);

    writeAdditionalAfterBodyEndScripts(out);

    JspUtil.writeEndTag(out, "script");
  }

  /**
   * Called before closing the script tag immediately following the HTML &lt;body&gt; start, use for additional
   * client-side page (Aranea.Page) initialization.
   * 
   * @param out The writer of the rendered page.
   * @throws Exception Any exception that may occur.
   */
  protected void writeAdditionalAfterBodyStartScripts(Writer out) throws Exception {}

  /**
   * Called before closing the script tag immediately following the HTML &lt;body&gt; start, use for additional
   * client-side page (Aranea.Page) initialization.
   * 
   * @param out The writer of the rendered page.
   * @throws Exception Any exception that may occur.
   */
  protected void writeAdditionalAfterBodyEndScripts(Writer out) throws Exception {
    out.write(this.afterBodyEndScripts.toString());
  }

  /**
   * Nested tags should get surrounding body tag from <code>PageContext</code> and register their body end scripts with
   * this method.
   * 
   * @param script The script to add after the BODY end-tag.
   */
  public void addAfterBodyEndScript(String script) {
    if (this.afterBodyEndScripts == null) {
      this.afterBodyEndScripts = new StringBuffer();
    }
    this.afterBodyEndScripts.append(script);
  }

  // Tag attributes

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Overwrite the standard Aranea JSP HTML body onload event. Use with caution."
   */
  public void setOnload(String onload) {
    this.onload = evaluate("onload", onload, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Overwrite the standard Aranea JSP HTML body onunload event. Use with caution."
   */
  public void setOnunload(String onunload) {
    this.onunload = evaluate("onunload", onunload, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Text direction."
   */
  public void setDir(String dir) {
    this.dir = dir;
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "HTML body id"
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Language information."
   */
  public void setLang(String lang) {
    this.lang = lang;
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Title."
   */
  public void setTitle(String title) {
    this.title = title;
  }
}
