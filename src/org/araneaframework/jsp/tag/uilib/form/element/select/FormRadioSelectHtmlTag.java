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

package org.araneaframework.jsp.tag.uilib.form.element.select;

import org.araneaframework.uilib.util.ConfigurationContextUtil;
import java.io.Writer;
import java.util.Iterator;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementHtmlTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.form.control.SelectControl;
import org.araneaframework.uilib.support.DisplayItem;


/**
 * Standard select form element tag.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 * @jsp.tag
 *   name = "radioSelect"
 *   body-content = "JSP"
 *   description = "Form radioselect buttons field, represents UiLib "SelectControl"."
 */
public class FormRadioSelectHtmlTag extends BaseFormElementHtmlTag {

  protected String type = "horizontal";

  protected boolean labelBefore = false;

  protected Boolean localizeDisplayItems;

  public FormRadioSelectHtmlTag() {
    setHasElementContextSpan(false);
  }

  public int doEndTag(Writer out) throws Exception {
    assertControlType("SelectControl");

    if (!"horizontal".equals(type) && !"vertical".equals(type)) {
      throw new AraneaJspException(
          "Attribute 'type' can be only either 'horizontal' or 'vertical'!");
    }

    // Prepare
    addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, null);

    SelectControl.ViewModel viewModel = (SelectControl.ViewModel) controlViewModel;
    FormRadioSelectItemLabelHtmlTag label = new FormRadioSelectItemLabelHtmlTag();

    for (Iterator i = viewModel.getSelectItems().iterator(); i.hasNext();) {
      DisplayItem displayItem = (DisplayItem) i.next();

      // set the corresponding HTML id for label and radiobutton so that
      // clicking on label
      // affects radiobutton selection
      String radioId = viewModel.getScope().toString() + displayItem.getValue();
      FormRadioSelectItemHtmlTag item = new FormRadioSelectItemHtmlTag();
      registerSubtag(item);
      item.setHtmlId(radioId);
      label.setRadioId(radioId);

      if (labelBefore) {
        writeLabel(label, derivedId, displayItem.getValue());
      }

      item.setId(derivedId);
      item.setValue(displayItem.getValue());
      item.setEvents(events ? "true" : "false");
      item.setValidateOnEvent(validateOnEvent ? "true" : "false");
      item.setStyleClass(getStyleClass());

      if (updateRegions != null) {
        item.setUpdateRegions(updateRegions);
      }

      if (globalUpdateRegions != null) {
        item.setGlobalUpdateRegions(globalUpdateRegions);
      }

      if (getStyle() != null) {
        item.setStyle(getStyle());
      }

      if (tabindex != null) {
        item.setTabindex(tabindex);
      }

      executeStartSubtag(item);
      executeEndTagAndUnregister(item);

      if (!labelBefore) {
        writeLabel(label, derivedId, displayItem.getValue());
      }

      if ("horizontal".equals(type)) {
        out.write("&nbsp;");
      } else if ("vertical".equals(type)) {
        JspUtil.writeStartEndTag(out, "br");
      }
    }
    super.doEndTag(out);
    return EVAL_PAGE;
  }

  /*
   * Tag attributes
   */
  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "The way the radio buttons will be rendered - can be either vertical or horizontal. By default horizontal."
   */
  public void setType(String type) throws JspException {
    this.type = (String) evaluate("type", type, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Boolean that controls whether label is before or after each radio button. False by default."
   */
  public void setLabelBefore(String labelBefore) throws JspException {
    this.labelBefore = ((Boolean) evaluateNotNull("labelBefore", labelBefore,
        Boolean.class)).booleanValue();
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Whether to localize display items. Provides a way to override ConfigurationContext.LOCALIZE_FIXED_CONTROL_DATA."
   * @since 1.2
   */
  public void setLocalizeDisplayItems(String localizeDisplayItems)
      throws JspException {
    this.localizeDisplayItems = (Boolean) evaluateNotNull(
        "localizeDisplayItems", localizeDisplayItems, Boolean.class);
  }

  protected void writeLabel(FormRadioSelectItemLabelHtmlTag label, String id,
      String value) throws JspException {
    registerSubtag(label);
    label.setId(id);
    label.setValue(evaluateLabel(value));
    executeStartSubtag(label);
    executeEndTagAndUnregister(label);
  }

  protected String evaluateLabel(String value) {
    if (this.localizeDisplayItems == null) {
      this.localizeDisplayItems = ConfigurationContextUtil
          .isLocalizeControlData(getEnvironment());
    }

    if (this.localizeDisplayItems.booleanValue()) {
      value = JspUtil.getResourceString(pageContext, value);
    }

    return value;
  }
}
