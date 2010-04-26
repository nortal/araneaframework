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

package org.araneaframework.jsp.tag.form;

import java.io.Writer;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.BaseTag;
import org.araneaframework.jsp.util.JspUtil;

/**
 * Abstract base class for systemForm tags. System form maps into HTML form.
 * 
 * @author Oleg Mürk
 */
public abstract class BaseSystemFormHtmlTag extends BaseTag {

  public final static String GET_METHOD = "get";

  public final static String POST_METHOD = "post";

  private String id;

  protected String derivedId;

  protected String method;

  protected String enctype;

  protected String styleClass;

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    // Compute new id for systemForm.
    this.derivedId = this.id == null ? "systemForm" : this.id;

    String formAction = getFormAction();

    if (StringUtils.isBlank(formAction)) {
      formAction = null;
    } else {
      formAction = ((HttpServletResponse) this.pageContext.getResponse()).encodeURL(formAction);
    }

    // Write form
    JspUtil.writeOpenStartTag(out, "form");
    JspUtil.writeAttribute(out, "id", this.derivedId);
    JspUtil.writeAttribute(out, "name", this.derivedId);
    JspUtil.writeAttribute(out, "method", this.method);
    JspUtil.writeAttribute(out, "enctype", this.enctype);
    JspUtil.writeAttribute(out, "accept-charset", getAcceptCharset());
    JspUtil.writeAttribute(out, "action", formAction);
    JspUtil.writeAttribute(out, "class", this.styleClass);
    JspUtil.writeAttribute(out, "style", "margin: 0px");
    JspUtil.writeAttribute(out, "onsubmit", "return false;");
    JspUtil.writeAttribute(out, "arn-systemForm", "true");
    JspUtil.writeCloseStartTag(out);

    return EVAL_BODY_INCLUDE;
  }

  @Override
  protected int doEndTag(Writer out) throws Exception {
    JspUtil.writeEndTag(out, "form");

    // Continue
    super.doEndTag(out);
    return EVAL_PAGE;
  }

  /**
   * Provides the value for the "accept-charset" attribute. May return <code>null</code>.
   *  
   * @return The value for the "accept-charset" attribute or <code>null</code>.
   */
  protected abstract String getAcceptCharset();

  /**
   * Provides the value for the "action" attribute. May return <code>null</code>. The implemented method must return the
   * URL as-is. It will be encoded and escaped when written. Null, empty, and blank strings will be ignored. 
   *  
   * @return The value for the "action" attribute or <code>null</code>.
   */
  protected abstract String getFormAction();

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Form name, auto-generated as 'systemForm', if omitted."
   */
  public void setId(String id) {
    this.id = evaluate("id", id, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "true"
   *    description = "Submitting method: GET or POST."
   */
  public void setMethod(String method) throws JspException {
    this.method = evaluateNotNull("method", method, String.class).toLowerCase();

    if (!StringUtils.equalsIgnoreCase(this.method, GET_METHOD)
        && !StringUtils.equalsIgnoreCase(this.method, POST_METHOD)) {
      throw new AraneaJspException("Wrong form method value '" + method + "'");
    }
  }

  /**
   * @jsp.attribute type = "java.lang.String" required = "false" description = "Form data encoding type."
   */
  public void setEnctype(String enctype) {
    this.enctype = evaluate("enctype", enctype, String.class);
  }

  /**
   * @jsp.attribute type = "java.lang.String" required = "false" description = "CSS class for tag"
   */
  public void setStyleClass(String styleClass) {
    this.styleClass = evaluate("styleClass", styleClass, String.class);
  }
}
