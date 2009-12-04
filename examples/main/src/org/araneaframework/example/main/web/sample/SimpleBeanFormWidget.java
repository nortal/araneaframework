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

package org.araneaframework.example.main.web.sample;

import java.io.Serializable;
import java.math.BigDecimal;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.control.CheckboxControl;
import org.araneaframework.uilib.form.control.FloatControl;
import org.araneaframework.uilib.form.control.JodaDateControl;
import org.araneaframework.uilib.form.control.JodaDateTimeControl;
import org.araneaframework.uilib.form.control.JodaTimeControl;
import org.araneaframework.uilib.form.control.NumberControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.BooleanData;
import org.joda.time.DateTime;

/**
 * Simple bean form component. A form with one checkbox, one textbox,
 * three kinds of different timeinputs (DateInput, Timeinput and 
 * DateTimeInput), different primitive number inputs, and a button.
 * 
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
 * @since 1.1.3
 */
public class SimpleBeanFormWidget extends TemplateBaseWidget {

  private static final long serialVersionUID = 1L;

  private BeanFormWidget<FormDataModel> simpleForm;

  /**
   * Builds the form.
   */
  protected void init() throws Exception {
    setViewSelector("sample/simpleBeanForm");

    // Creation of new bean form bound to the instance of FormDataModel class:
    simpleForm = new BeanFormWidget<FormDataModel>(getDefaultValues());

    // Now that we have created a form, we will need to add form elements.
    // form elements consist of four basic things - label, Control that
    // implements form element functionality and Data holding values that form
    // element can have. Note that the first sample with FormWidget's
    // createElement method is not the way form elements are usually added to
    // the form, but rather emphasises the fact that everything you add to
    // FormWidget is a FormElement.

    // addBeanElement(String elementName, String labelId, Control control,
    // boolean mandatory);
    // 
    // Notice, we don't have to specify data type, because it can be read from
    // the bean field. Also, the primitive fields are always mandatory, because
    // they cannot accept null values.

    simpleForm.addElement("caseSensitive", "demo.beanForm.caseSensitive",
        new CheckboxControl(), new BooleanData(), false);

    simpleForm.setValueByFullName("caseSensitive", Boolean.TRUE);
    simpleForm.addBeanElement("searchString", "demo.beanForm.searchString",
        new TextControl(), false);
    simpleForm.addBeanElement("createdDateTime",
        "demo.beanForm.createdDateTime", new JodaDateTimeControl(), false);
    simpleForm.addBeanElement("createdTime", "demo.beanForm.createdTime",
        new JodaTimeControl(), false);
    simpleForm.addBeanElement("createdDate", "demo.beanForm.createdDate",
        new JodaDateControl(), false);
    simpleForm.addBeanElement("length", "demo.beanForm.length",
        new FloatControl(), false);

    // Primitive field mappings (notice the fields are mandatory, because they
    // cannot accept null values - Aranea would throw an exception):
    simpleForm.addBeanElement("siblingsCount", "demo.beanForm.siblingsCount",
        new NumberControl(), true);
    simpleForm.addBeanElement("peopleCount", "demo.beanForm.peopleCount",
        new NumberControl(), true);
    simpleForm.addBeanElement("weight", "demo.beanForm.weight",
        new FloatControl(), true);
    simpleForm.addBeanElement("preciseWeight", "demo.beanForm.preciseWeight",
        new FloatControl(), true);

    // We use a simple option of not defining the button here. Instead, we just
    // use a JSP tag that renders the button and also invokes the event.

    // The usual, add the created widget to main widget.
    addWidget("simpleForm", simpleForm);
  }

  /**
   * A test action, invoked when button is pressed. It adds the values of
   * formelements to message context, and they end up at the top of user screen
   * at the end of the request.
   */
  public void handleEventTestSimpleBeanForm() throws Exception {
    // if form is not invalid, do not try to show form element values
    // (error messages are added automatically to the messagecontext
    // though, user will not be without feedback)
    if (simpleForm.convertAndValidate()) {
      FormDataModel data = simpleForm.writeToBean();

      // We can display the result simply like that:
      getMessageCtx().showInfoMessage("Checkbox value is: " + data.isCaseSensitive());
      getMessageCtx().showInfoMessage("Textbox value is: " + data.getSearchString());
      getMessageCtx().showInfoMessage("DateTime value is: " + data.getCreatedDateTime());
      getMessageCtx().showInfoMessage("Time value is: " + data.getCreatedTime());
      getMessageCtx().showInfoMessage("Date value is: " + data.getCreatedDate());
      getMessageCtx().showInfoMessage("Number value is: " + data.getLength());
    }
  }

  private FormDataModel getDefaultValues(){
    FormDataModel formData = new FormDataModel();
    formData.setCaseSensitive(true);
    formData.setSearchString("Where am I?");
    formData.setCreatedDateTime(new DateTime());
    formData.setCreatedDate(new DateTime());
    formData.setCreatedTime(new DateTime());
    formData.setLength(new BigDecimal("12345.6789"));
    formData.setSiblingsCount(5);
    formData.setPeopleCount(66500000000L);
    formData.setWeight(77.8F);
    formData.setPreciseWeight(77.8989);
    return formData;
  }

  /**
   * The data model for our sample form.
   * 
   * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
   */
  public class FormDataModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean caseSensitive;

    private String searchString;

    private DateTime createdDateTime;

    private DateTime createdDate;

    private DateTime createdTime;

    private BigDecimal length;

    private int siblingsCount;

    private long peopleCount;

    private float weight;

    private double preciseWeight;

    public boolean isCaseSensitive() {
      return this.caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
      this.caseSensitive = caseSensitive;
    }

    public String getSearchString() {
      return this.searchString;
    }

    public void setSearchString(String searchString) {
      this.searchString = searchString;
    }

    public DateTime getCreatedDateTime() {
      return this.createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
      this.createdDateTime = createdDateTime;
    }

    public DateTime getCreatedDate() {
      return this.createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
      this.createdDate = createdDate;
    }

    public DateTime getCreatedTime() {
      return this.createdTime;
    }

    public void setCreatedTime(DateTime createdTime) {
      this.createdTime = createdTime;
    }

    public BigDecimal getLength() {
      return this.length;
    }

    public void setLength(BigDecimal length) {
      this.length = length;
    }

    public int getSiblingsCount() {
      return this.siblingsCount;
    }

    public void setSiblingsCount(int siblingsCount) {
      this.siblingsCount = siblingsCount;
    }

    public long getPeopleCount() {
      return this.peopleCount;
    }

    public void setPeopleCount(long peopleCount) {
      this.peopleCount = peopleCount;
    }

    public float getWeight() {
      return this.weight;
    }

    public void setWeight(float weight) {
      this.weight = weight;
    }

    public double getPreciseWeight() {
      return this.preciseWeight;
    }

    public void setPreciseWeight(double preciseWeight) {
      this.preciseWeight = preciseWeight;
    }

  }

}