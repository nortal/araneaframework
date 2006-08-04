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

package org.araneaframework.jsp.tag.uilib.form.element;

import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.araneaframework.core.util.ClassLoaderUtil;
import org.araneaframework.jsp.exception.MissingFormElementIdAraneaJspException;
import org.araneaframework.jsp.support.FormElementViewSelector;
import org.araneaframework.jsp.support.TagInfo;
import org.araneaframework.jsp.tag.BaseTag;
import org.araneaframework.jsp.tag.uilib.form.FormElementTag;
import org.araneaframework.jsp.tag.uilib.form.FormElementTagInterface;
import org.araneaframework.jsp.tag.uilib.form.FormTag;
import org.araneaframework.jsp.util.JspWidgetCallUtil;
import org.araneaframework.jsp.util.JspWidgetUtil;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;


/**
 * Automatic form element tag. Chooses the tag to draw the control based on the information supplied in the 
 * component.
 * 
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 * 
 * @jsp.tag
 *   name = "automaticFormElement"
 *   body-content = "JSP"
 *   description = "Automatic form element which dynamically draws correct form element tag."
 */
public class AutomaticTagFormElementTag extends BaseTag {
  protected String derivedId;
  private String id;
  protected String events;
  protected String validate;
  protected String validateOnEvent;
  protected String tabindex;
  protected String styleClass;
  protected String updateRegions;
  protected String globalUpdateRegions;    

  protected FormWidget.ViewModel formViewModel;

  protected FormElement.ViewModel formElementViewModel;
  protected Control.ViewModel controlViewModel;  

  protected FormElementTagInterface controlTag;

  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);


    formViewModel = (FormWidget.ViewModel)requireContextEntry(FormTag.FORM_VIEW_MODEL_KEY);
    FormWidget form = (FormWidget)requireContextEntry(FormTag.FORM_KEY);

    //In case the tag is in formElement tag
    derivedId = id;
    if (derivedId == null && getContextEntry(FormElementTag.ID_KEY) != null) 
    	derivedId = (String) getContextEntry(FormElementTag.ID_KEY);
    if (derivedId == null) throw new MissingFormElementIdAraneaJspException(this);
    formElementViewModel = 
      (FormElement.ViewModel) JspWidgetUtil.traverseToSubWidget(form, derivedId)._getViewable().getViewModel();   

    // Get control  
    controlViewModel = formElementViewModel.getControl();

    FormElementViewSelector viewSelector = 
      (FormElementViewSelector) formElementViewModel.getProperties().get(
          FormElementViewSelector.FORM_ELEMENT_VIEW_SELECTOR_PROPERTY);

    if(viewSelector == null)
      throw new JspException("The form element view selector was not passed!.");
    
    Map tagMapping = JspWidgetCallUtil.getContainer(pageContext).getTagMapping(pageContext, viewSelector.getUri());
    
    if(tagMapping == null)
      throw new JspException("The tag mapping was not found!.");

    TagInfo tagInfo = (TagInfo) tagMapping.get(viewSelector.getTag());

    if(tagInfo == null)
      throw new JspException("Unexistant tag was passed to form element view selector!.");

    controlTag = (FormElementTagInterface)ClassLoaderUtil.loadClass(tagInfo.getTagClassName()).newInstance();
    Class tagClass = controlTag.getClass();

    registerSubtag(controlTag);

    initTagAttributes(tagClass, controlTag, viewSelector.getAttributes());

    controlTag.setId(derivedId);
    if(events != null)
      controlTag.setEvents(events);
    if(validate != null)
      controlTag.setValidate(validate);
    if(validateOnEvent != null)
      controlTag.setValidateOnEvent(validateOnEvent);
    if(styleClass != null)
      controlTag.setStyleClass(styleClass);
    if(tabindex != null)
      controlTag.setTabindex(tabindex);
    if(updateRegions != null)
      controlTag.setUpdateRegions(updateRegions);
    if(globalUpdateRegions != null)
      controlTag.setGlobalUpdateRegions(globalUpdateRegions);    

    executeStartSubtag(controlTag);

    return EVAL_BODY_INCLUDE;
  }


  protected int doEndTag(Writer out) throws Exception {
    executeEndSubtag(controlTag);
    unregisterSubtag(controlTag);

    return super.doEndTag(out);    
  }

  /* ***********************************************************************************
   * Tag attributes
   * ***********************************************************************************/

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Element id, can also be inherited." 
   */
  public void setId(String id) throws JspException {
    this.id = (String)evaluateNotNull("id", id, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Whether the element will send the events that are registered by server-side (by default "true")." 
   */
  public void setEvents(String events) throws JspException {
    this.events = events; 
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Whether the element will be validated on the client-side when the form is submitted (by default "true")." 
   */
  public void setValidate(String validate) throws JspException {
    this.validate = validate; 
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Whether the form will be validated on the client-side when the element generates an event (by default "false")." 
   */
  public void setValidateOnEvent(String validateOnEvent) throws JspException {
    this.validateOnEvent = validateOnEvent; 
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Element tabindex." 
   */
  public void setTabindex(String tabindex) throws JspException {
    this.tabindex = tabindex;
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "CSS class without prefix of the dynamically selected tag." 
   */
  public void setStyleClass(String styleClass) throws JspException {
    this.styleClass = styleClass;
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Enumerates the regions of markup to be updated in this widget scope. Please see <code><ui:updateRegion></code> for details." 
   */
  public void setUpdateRegions(String updateRegions) throws JspException {
    this.updateRegions = updateRegions;
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Enumerates the regions of markup to be updated globally. Please see <code><ui:updateRegion></code> for details." 
   */
  public void setGlobalUpdateRegions(String globalUpdateRegions) throws JspException {
    this.globalUpdateRegions = globalUpdateRegions;
  }    

  protected void initTagAttributes(Class tagClass, Object tag, Map attributes) throws Exception {
    for (Iterator i = attributes.entrySet().iterator(); i.hasNext();) {
      Map.Entry entry = (Map.Entry) i.next();

      String attributeName = (String) entry.getKey();
      String setterMethodName = "set" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);

      Method setter = tagClass.getMethod(setterMethodName, new Class[] {entry.getValue().getClass()});
      setter.invoke(tag, new Object[] {entry.getValue()});      
    }
  }
}
