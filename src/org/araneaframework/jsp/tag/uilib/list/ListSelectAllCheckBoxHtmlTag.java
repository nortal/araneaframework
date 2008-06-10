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

package org.araneaframework.jsp.tag.uilib.list;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.util.JspUtil;

/**
 * @jsp.tag
 *   name = "listSelectAllCheckBox"
 *   body-content = "empty"
 *   display-name = "listSelectAllCheckBox"
 *   description = "Represents a check box that can select all check boxes in given list."
 * 
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
 * @since 1.1.3
 */
public class ListSelectAllCheckBoxHtmlTag extends PresentationTag {

  /**
   * The script that checks and unchecks check boxes in the list.
   */
  protected static final String SCRIPT_ON_CLICK = "return Aranea.UI.toggleListCheckBoxes(this);";

  protected boolean disabled;

  protected String onclick;

  protected String accesskey;

  protected String tabindex;

  protected boolean checked = false;

  public ListSelectAllCheckBoxHtmlTag() {
    this.baseStyleClass = "aranea-checkbox";
  }

  public int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    JspUtil.writeOpenStartTag(out, "input");
    JspUtil.writeAttribute(out, "type", "checkbox");
    JspUtil.writeAttribute(out, "id", getCheckBoxId());
    // No name attribute because it's not worth being submitted.
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    // No value attribute because it's not being submitted.
    JspUtil.writeAttribute(out, "onclick", getOnclickScript());

    JspUtil.writeAttribute(out, "tabindex", tabindex);
    JspUtil.writeAttribute(out, "accessKey", accesskey);

    if (checked) {
      JspUtil.writeAttribute(out, "checked", "checked");
    }

    if (disabled) {
      JspUtil.writeAttribute(out, "disabled", "disabled");
    }

    JspUtil.writeCloseStartEndTag(out);

    return SKIP_BODY;       
  }

  /**
   * Creates the onclick event script, including the onclick script that the
   * user specifies through attribute value.
   * 
   * @return The entire script for check box onclick event.
   */
  protected String getOnclickScript() {
    StringBuffer result = new StringBuffer();
    if (StringUtils.isNotBlank(this.onclick)) {
      result.append(this.onclick);
      result.append("; ");
    }
    result.append(SCRIPT_ON_CLICK);
    return result.toString();
  }

  /**
   * Creates the check box ID. Note that it is very important how the ID looks
   * like. It means that the ID of the row check box must begin with the ID
   * value of the select-all check box to make the JavaScript methods work.
   * 
   * @return The ID that will be used for the generated check box.
   * @throws JspException This method requires listId and rowRequestId entries
   *             from the context.
   */
  protected String getCheckBoxId() throws JspException {
    String listId = (String) requireContextEntry(ListTag.LIST_FULL_ID_KEY);         
    return listId + "." + ListRowCheckBoxHtmlTag.SELECTION_SCOPE;
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.Boolean"
   *   required = "false"
   *   description = "Specifies whether the check box should be rendered as disabled. Default is active state."
   */
  public void setDisabled(String disabled) throws JspException {
    Boolean tempResult = (Boolean) evaluateNotNull("disabled", disabled, Boolean.class);
    this.disabled = tempResult.booleanValue();
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Specifies custom <code>onclick</code> event. Default is none."
   */
  public void setOnclick(String onclick) throws JspException {
    this.onclick = (String) evaluateNotNull("onclick", onclick, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Specifies custom <code>acceskey</code> (defined by HTML). Default is none."
   */
  public void setAccessKey(String accessKey) throws JspException {
    this.accesskey = (String) evaluateNotNull("accessKey", accessKey, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.Boolean"
   *   required = "false"
   *   description = "Specifies the initial value of the check box. Default is unchecked."
   */
  public void setChecked(String checked) throws JspException {
    Boolean tempResult = (Boolean) evaluateNotNull("checked", checked, Boolean.class);
    this.checked = tempResult.booleanValue();
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "HTML tabindex for the check box."
   */   
  public void setTabindex(String tabindex) throws JspException {
    this.tabindex = (String) evaluateNotNull("tabindex", tabindex, String.class);
  }

}
