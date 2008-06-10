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
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.list.ListWidget;

/**
 * @jsp.tag
 *   name = "listRowRadioButton"
 *   body-content = "empty"
 *   display-name = "listRowRadioButton"
 *   description = "Represents a list row radio button. If you want, you can bind its state to the list row model object Boolean field value."
 * 
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
 * @since 1.1.3
 */
public class ListRowRadioButtonHtmlTag extends PresentationTag {

  /**
   * This will be used to create the value of the radio button of the list row
   * as following: <code>[listId].[SELECTION_SCOPE].[rowRequestId]</code>.
   * Note that the submitted radio button value will be later used to look up
   * the row.
   */
  public static final String SELECTION_SCOPE = ListWidget.LIST_RADIO_SCOPE;

  protected String labelId;

  protected boolean disabled;

  protected String onclick;

  protected String accesskey;

  protected String tabindex;

  protected boolean checked = false;

  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    JspUtil.writeOpenStartTag(out, "input");
    JspUtil.writeAttribute(out, "type", "radio");
    JspUtil.writeAttribute(out, "name", getRadioButtonName());
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttribute(out, "value", getRadioButtonValue());
    JspUtil.writeAttribute(out, "onclick", onclick);

    JspUtil.writeAttribute(out, "tabindex", tabindex);
    JspUtil.writeAttribute(out, "accessKey", accesskey);

    if (isSelected()) {
      JspUtil.writeAttribute(out, "checked", "checked");
    }

    if (disabled) {
      JspUtil.writeAttribute(out, "disabled", "disabled");
    }

    JspUtil.writeCloseStartEndTag(out);

    if (this.labelId != null) {
      JspUtil.writeOpenStartTag(out, "label");
      JspUtil.writeAttribute(out, "for", getRadioButtonName());
      JspUtil.writeCloseStartTag_SS(out);
      JspUtil.writeEscaped(out, JspUtil.getResourceString(pageContext,
          this.labelId));
      JspUtil.writeStartEndTag(out, "label");
    }

    return SKIP_BODY;
  }

  /**
   * Generates the radio button value. It is important that the list widget
   * could later understand it as a selected value.
   * 
   * @return The value that will be used for the radio button.
   * @throws JspException This method requires listId and rowRequestId entries
   *             from the context.
   */
  protected String getRadioButtonValue() throws JspException {
    String listId = (String) requireContextEntry(ListTag.LIST_FULL_ID_KEY);
    String rowRequestId = (String) requireContextEntry(BaseListRowsTag.ROW_REQUEST_ID_KEY);
    return listId + "." + SELECTION_SCOPE + "." + rowRequestId;
  }

  /**
   * Generates the radio button name that should be the same for all radio
   * buttons in one group. This implementation returns
   * <code>[listId].[SELECTION_SCOPE]</code>.
   * 
   * @return <code>[listId].[SELECTION_SCOPE]</code>
   * @throws JspException
   */
  protected String getRadioButtonName() throws JspException {
    return requireContextEntry(ListTag.LIST_FULL_ID_KEY) + "."
        + SELECTION_SCOPE;
  }

  /**
   * Decides whether the radio button should be rendered selected or not. It
   * will always be rendered selected when <code>checked</code> is
   * <code>true</code>. Otherwise, it checks using the list view model
   * whether row had been selected before.
   * 
   * @return A Boolean indicating whether the row should be rendered as
   *         selected.
   * @throws JspException This method need row request ID and list view model
   *             from the JSP context.
   */
  protected boolean isSelected() throws JspException {
    String rowRequestId = (String) requireContextEntry(BaseListRowsTag.ROW_REQUEST_ID_KEY);
    ListWidget.ViewModel viewModel = (ListWidget.ViewModel) requireContextEntry(ListTag.LIST_VIEW_MODEL_KEY);
    return this.checked || rowRequestId.equals(viewModel.getData().get(SELECTION_SCOPE));
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Specifies a custom label for the radio button."
   */
  public void setLabelId(String labelId) throws JspException {
    this.labelId = (String) evaluateNotNull("labelId", labelId, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.Boolean"
   *   required = "false"
   *   description = "Specifies whether the radio button should be rendered as disabled. Default is active state."
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
   *   description = "Specifies the initial value of the radio button. Default is unchecked."
   */
  public void setChecked(String checked) throws JspException {
    Boolean tempResult = (Boolean) evaluateNotNull("checked", checked, Boolean.class);
    this.checked = tempResult.booleanValue();
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "HTML tabindex for the radio button. This value must be a number between 0 and 32767."
   */   
  public void setTabindex(String tabindex) throws JspException {
    this.tabindex = (String) evaluateNotNull("tabindex", tabindex, String.class);
  }

}
