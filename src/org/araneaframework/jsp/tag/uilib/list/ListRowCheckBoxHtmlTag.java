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

package org.araneaframework.jsp.tag.uilib.list;

import java.io.Writer;
import java.util.List;
import javax.servlet.jsp.JspException;
import org.araneaframework.Path;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.list.ListWidget;

/**
 * @jsp.tag
 *  name = "listRowCheckBox"
 *  body-content = "empty"
 *  display-name = "listRowCheckBox"
 *  description = "Represents a list row check box. If you want, you can bind its state to the list row model object Boolean field value."
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 1.1.3
 */
public class ListRowCheckBoxHtmlTag extends BaseListRowControlTag {

  /**
   * The value of the selected check box that will be returned with request.
   */
  public static final String LIST_CHECK_VALUE = ListWidget.LIST_CHECK_VALUE;

  /**
   * This will be used to create the ID of the check box of the list row as following:
   * <code>[listId].[SELECTION_SCOPE].[rowRequestId]</code>.
   */
  public static final String SELECTION_SCOPE = ListWidget.LIST_CHECK_SCOPE;

  protected String value;

  public ListRowCheckBoxHtmlTag() {
    this.baseStyleClass = "aranea-checkbox";
  }

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    String id = getCheckBoxId(false);

    JspUtil.writeOpenStartTag(out, "input");
    JspUtil.writeAttribute(out, "type", "checkbox");
    JspUtil.writeAttribute(out, "id", id);
    JspUtil.writeAttribute(out, "name", id);
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttribute(out, "value", this.value != null ? this.value : LIST_CHECK_VALUE);

    JspUtil.writeAttribute(out, "tabindex", this.tabindex);
    JspUtil.writeAttribute(out, "accessKey", this.accesskey);

    writeOnClickEvent(out);

    if (isChecked()) {
      JspUtil.writeAttribute(out, "checked", "checked");
    }

    if (this.disabled) {
      JspUtil.writeAttribute(out, "disabled", "disabled");
    }

    JspUtil.writeCloseStartEndTag(out);

    if (this.labelId != null) {
      JspUtil.writeOpenStartTag(out, "label");
      JspUtil.writeAttribute(out, "for", id);
      JspUtil.writeCloseStartTag_SS(out);
      JspUtil.writeEscaped(out, JspUtil.getResourceString(this.pageContext, this.labelId));
      JspUtil.writeEndTag(out, "label");
    }

    return SKIP_BODY;
  }

  /**
   * Creates the "onclick" event script, including the "onclick" script that the user specifies through attribute value.
   * 
   * @return The entire script for check box "onclick" event.
   */
  @Override
  protected String getOnclickScript() {
    String tmp = this.onClickEventId == null ? this.onclick : this.eventPrecondition;

    StringBuffer result = new StringBuffer();
    if (tmp != null) {
      result.append(tmp);
      result.append(" ");
    }

    try {
      result.append("return Aranea.UI.updateListSelectAll('");
      result.append(getCheckBoxId(true));
      result.append("');");
    } catch (JspException e) {}

    return result.toString();
  }

  /**
   * Creates the check box ID. Note that it is very important how the ID looks like. It means that the ID of the row
   * check box must begin with the ID value of the select-all check box to make the JavaScript methods work.
   * 
   * @param parent Whether the returned ID is the ID of the select-all-checkbox.
   * @return The ID that will be used for the generated check box.
   * @throws JspException This method requires listId and rowRequestId entries from the context.
   */
  protected String getCheckBoxId(boolean parent) throws JspException {
    String listId = (String) requireContextEntry(ListTag.LIST_FULL_ID_KEY);
    String rowRequestId = (String) requireContextEntry(BaseListRowsTag.ROW_REQUEST_ID_KEY);
    StringBuffer result = new StringBuffer(listId).append(Path.SEPARATOR + SELECTION_SCOPE);
    if (!parent) {
      result.append(Path.SEPARATOR).append(rowRequestId);
    }
    return result.toString();
  }

  /**
   * This method decides whether the check box should be checked or not. If the attribute <code>checked</code> is
   * <code>true</code> then it will always render it checked. Otherwise, it also looks from <code>viewData</code>
   * whether the row has been checked (by the user) before.
   * 
   * @return A Boolean indicating whether the check box should be rendered checked.
   * @throws JspException This method expects to have access to <code>ROW_KEY</code> and list view model in the JSP
   *           context.
   */
  @SuppressWarnings("unchecked")
  protected boolean isChecked() throws JspException {
    Object row = requireContextEntry(BaseListRowsTag.ROW_KEY);

    ListWidget<?>.ViewModel viewModel = (ListWidget.ViewModel) requireContextEntry(ListTag.LIST_VIEW_MODEL_KEY);

    List<?> checkedRows = List.class.cast(viewModel.getData().get(SELECTION_SCOPE));

    boolean prevChecked = checkedRows != null && checkedRows.contains(row);

    return this.checked || prevChecked;
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Allows a custom value for this check box (when it is submitted). Default value is  <code>selected</code>."
   */
  public void setValue(String value) throws JspException {
    this.value = evaluateNotNull("value", value, String.class);
  }

}
