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

import java.io.Writer;
import java.util.Iterator;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementHtmlTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.form.control.MultiSelectControl;
import org.araneaframework.uilib.support.DisplayItem;


/**
 * Standard select form element tag.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 * @jsp.tag
 *   name = "checkboxMultiSelect"
 *   body-content = "JSP"
 *   description = "Form multiselect checkbox field, represents UiLib 'MultiSelectControl'."
 */
public class FormCheckboxMultiSelectHtmlTag extends BaseFormElementHtmlTag {
  protected String type = "horizontal";
  protected boolean labelBefore = false;

  public FormCheckboxMultiSelectHtmlTag() {
    setHasElementContextSpan(false);
  }
  
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, null);
    return EVAL_BODY_INCLUDE;
  }


  public int doEndTag(Writer out) throws Exception {
    assertControlType("MultiSelectControl");    

    if (!"horizontal".equals(type) && !"vertical".equals(type))
      throw new AraneaJspException("Attribute 'type' can be only either 'horizontal' or 'vertical'!");

    // Prepare
    MultiSelectControl.ViewModel viewModel = ((MultiSelectControl.ViewModel)controlViewModel);

    FormCheckboxMultiSelectItemLabelHtmlTag label = new FormCheckboxMultiSelectItemLabelHtmlTag();
    FormCheckboxMultiSelectItemHtmlTag item = new FormCheckboxMultiSelectItemHtmlTag();

    for (Iterator i = viewModel.getSelectItems().iterator(); i.hasNext();) {
      DisplayItem displayItem = (DisplayItem) i.next();
      
      // set the corresponding HTML id for label and checkbox so that clicking on label
      // sets the checkbox value too
      String checkboxId = viewModel.getScope().toString() + displayItem.getValue();
      item.setHtmlId(checkboxId);
      label.setCheckboxId(checkboxId);

      if (labelBefore) writeLabel(label, derivedId, displayItem.getValue());

      registerSubtag(item);

      item.setId(derivedId);
      item.setValue(displayItem.getValue());
      item.setEvents(events ? "true" : "false");
      item.setValidateOnEvent(validateOnEvent ? "true" : "false");

      if(updateRegions != null)
        item.setUpdateRegions(updateRegions);
      if(globalUpdateRegions != null)
        item.setGlobalUpdateRegions(globalUpdateRegions);
      item.setStyleClass(getStyleClass());
      if (getStyle() != null) item.setStyle(getStyle());

      if(tabindex != null)
        item.setTabindex(tabindex);        

      executeStartSubtag(item);
      executeEndTagAndUnregister(item);

      if (!labelBefore) writeLabel(label, derivedId, displayItem.getValue());

      if ("horizontal".equals(type))
        out.write("&nbsp;");
      else if ("vertical".equals(type))
        JspUtil.writeStartEndTag(out, "br");
    }

    super.doEndTag(out);
    return EVAL_PAGE;  
  }


  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   rtexprvalue = "true"
   *   required = "false"
   *   description = "The way the checkboxes will be rendered - can be either "vertical" or "horizontal". By default "horizontal"." 
   */
  public void setType(String type) throws JspException {
    this.type = (String)evaluate("type", type, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   rtexprvalue = "true"
   *   required = "false"
   *   description = "Boolean that controls whether label is before or after each checkbox. False by default." 
   */
  public void setLabelBefore(String labelBefore) throws JspException {
    this.labelBefore = ((Boolean) evaluateNotNull("labelBefore", labelBefore, Boolean.class)).booleanValue();
  }

  protected void writeLabel(FormCheckboxMultiSelectItemLabelHtmlTag label, String id, String value) throws JspException {
    registerSubtag(label);
    label.setId(id);
    label.setValue(value);
    executeStartSubtag(label);
    executeEndTagAndUnregister(label);
  }
}
