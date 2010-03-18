/*
 * Copyright 2006 Webmedia Group Ltd. Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package org.araneaframework.jsp.tag.uilib.form;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.apache.commons.lang.StringEscapeUtils;
import org.araneaframework.Path;
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.util.JspStringUtil;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetUtil;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;

/**
 * Standard simple label tag.
 * 
 * @author Oleg Mürk
 * @jsp.tag name = "simpleLabel" body-content = "JSP" description = "Represents localizable label."
 */
public class FormSimpleLabelHtmlTag extends PresentationTag {
  /** @since 1.1 */
  public static final String LABEL_SPAN_PREFIX = "label-";

  public final static String ASTERISK_STYLE = "aranea-label-asterisk";

  protected boolean showColon = true;
  protected String labelId;
  protected boolean mandatory = false;
  protected String forElementId;
  protected String accessKeyId;

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    if (accessKeyId == null)
      accessKeyId = labelId + ".access-key";
    String accessKey = JspUtil.getResourceStringOrNull(pageContext, accessKeyId);
    writeLabel(out,
               JspUtil.getResourceString(pageContext, labelId),
               mandatory,
               getStyleClass(),
               forElementId,
               pageContext,
               showColon,
               accessKey,
               getStyle());

    return EVAL_BODY_INCLUDE;
  }

  /* ***********************************************************************************
   * Tag attributes *********************************************************************************
   */

  /**
   * @jsp.attribute type = "java.lang.String" required = "true" description = "Label id."
   */
  public void setLabelId(String labelId) throws JspException {
    this.labelId = evaluateNotNull("labelId", labelId, String.class);
  }

  /**
   * @jsp.attribute type = "java.lang.String" required = "false" description =
   *                "Whether the label should display the asterisk, <code>false<code> by default."
   */
  public void setShowMandatory(String mandatory) throws JspException {
    this.mandatory = ((evaluateNotNull("mandatory", mandatory, Boolean.class))).booleanValue();
  }

  /**
   * @jsp.attribute type = "java.lang.String" required = "false" description =
   *                "Whether a colon (&quot;:&quot;) is shown after the label."
   */
  public void setShowColon(String showColon) throws JspException {
    this.showColon = ((evaluateNotNull("showColumn", showColon, Boolean.class))).booleanValue();
  }

  /**
   * @jsp.attribute type = "java.lang.String" required = "false" description =
   *                "ID of the form element for which the label is created"
   */
  public void setFor(String elementName) {
    this.forElementId = evaluate("for", elementName, String.class);
  }

  /**
   * Sets the resource id containing the access key for this label. This attribute is optional, if it is not specified,
   * the resource id is constructed as &lt;label-id&gt;.access-key. When this resource exists and contains a single
   * character, this character is used as an access key for the label. Otherwise no access key is used.
   */
  public void setAccessKeyId(String accessKeyId) {
    this.accessKeyId = evaluate("accessKeyId", accessKeyId, String.class);
  }

  /* ***********************************************************************************
   * STATIC label writing functions *********************************************************************************
   */

  /**
   * @deprecated Use {@link #writeLabel(Writer,String,boolean,String,String,PageContext,boolean,String,String)} instead
   */
  @Deprecated
  public static void writeLabel(Writer out,
                                String label,
                                boolean mandatory,
                                String styleClass,
                                String formElementId,
                                PageContext pageContext,
                                boolean showColon,
                                String accessKey) throws Exception {
    writeLabel(out, label, mandatory, styleClass, formElementId, pageContext, showColon, accessKey, null);
  }

  public static void writeLabel(Writer out,
                                String label,
                                boolean mandatory,
                                String styleClass,
                                String formElementId,
                                PageContext pageContext,
                                boolean showColon,
                                String accessKey,
                                String style) throws Exception {
    writeLabel(out, label, mandatory, styleClass, formElementId, pageContext, showColon, accessKey, styleClass, null);
  }

  public static void writeLabel(Writer out,
                                String label,
                                boolean mandatory,
                                String styleClass,
                                String formElementId,
                                PageContext pageContext,
                                boolean showColon,
                                String accessKey,
                                String style,
                                String mandatoryLabelClass) throws Exception {

    // Allow accessKey only if it is one-character long
    if (accessKey != null && accessKey.length() != 1)
      accessKey = null;

    String fullFormElementId = null; // Need this for the "for" attribute of the <label> tag

    if (formElementId != null) {
      // Find surrounding form's id
      String formId = (String) JspUtil.requireContextEntry(pageContext, FormTag.FORM_FULL_ID_KEY);
      // XXX: even if formElementContext JS should be invoked when writing label, formelement should not be marked as
      // present
      BaseFormElementHtmlTag.writeFormElementContextOpen(out,
                                                         formId,
                                                         formElementId,
                                                         false,
                                                         pageContext,
                                                         LABEL_SPAN_PREFIX);
      fullFormElementId = formId + Path.SEPARATOR + formElementId;
    }

    if (mandatory) {
      JspUtil.writeOpenStartTag(out, "span");
      JspUtil.writeAttribute(out, "class", mandatoryLabelClass != null ? mandatoryLabelClass : ASTERISK_STYLE);
      JspUtil.writeCloseStartTag_SS(out);
      out.write("*&nbsp;");
      JspUtil.writeEndTag_SS(out, "span");
    }

    // Write <span class=..>
    JspUtil.writeOpenStartTag(out, "span");
    JspUtil.writeAttribute(out, "class", styleClass);
    JspUtil.writeAttribute(out, "style", style);
    JspUtil.writeCloseStartTag_SS(out);

    // Write <label ...>
    JspUtil.writeOpenStartTag(out, "label");
    if (fullFormElementId != null)
      JspUtil.writeAttribute(out, "for", fullFormElementId);
    if (accessKey != null)
      JspUtil.writeAttribute(out, "accesskey", accessKey);
    JspUtil.writeCloseStartTag_SS(out);

    // Write label itself, underlining the accesskey if there is one
    String escapedLabel = StringEscapeUtils.escapeHtml(label);
    out.write(JspStringUtil.underlineAccessKey(escapedLabel, accessKey));
    if (showColon)
      out.write(":");

    // Close </label></span>
    JspUtil.writeEndTag_SS(out, "label");
    JspUtil.writeEndTag(out, "span");

    if (formElementId != null) {
      BaseFormElementHtmlTag.writeFormElementContextClose(out);
    }

    if (fullFormElementId != null) {
      FormWidget formWidget = (FormWidget) JspUtil.requireContextEntry(pageContext, FormTag.FORM_KEY);
      FormElement<?, ?> f = (FormElement<?, ?>) JspWidgetUtil.traverseToSubWidget(formWidget, formElementId);
      BaseFormElementHtmlTag.writeFormElementValidityMarkers(out, f.isValid(), LABEL_SPAN_PREFIX + fullFormElementId);
    }
  }

  public static void writeSelectLabel(Writer out, String label, String styleClass) throws IOException {
    JspUtil.writeOpenStartTag(out, "span");
    JspUtil.writeAttribute(out, "class", styleClass);
    JspUtil.writeCloseStartTag_SS(out);
    JspUtil.writeEscaped(out, label);
    JspUtil.writeEndTag(out, "span");
  }
}
