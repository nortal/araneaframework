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

package org.araneaframework.example.main.web.form;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.time.DateUtils;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.event.ProxyOnClickEventListener;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.constraint.RangeConstraint;
import org.araneaframework.uilib.form.control.BaseSelectControl;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.control.CheckboxControl;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.DateTimeControl;
import org.araneaframework.uilib.form.control.DefaultMultiSelectControl;
import org.araneaframework.uilib.form.control.DefaultSelectControl;
import org.araneaframework.uilib.form.control.FloatControl;
import org.araneaframework.uilib.form.control.NumberControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.control.TextareaControl;
import org.araneaframework.uilib.form.control.TimeControl;
import org.araneaframework.uilib.form.data.BigDecimalData;
import org.araneaframework.uilib.form.data.BooleanData;
import org.araneaframework.uilib.form.data.DateData;
import org.araneaframework.uilib.form.data.IntegerData;
import org.araneaframework.uilib.form.data.LongData;
import org.araneaframework.uilib.form.data.StringData;
import org.araneaframework.uilib.form.data.StringListData;
import org.araneaframework.uilib.list.util.FormUtil;
import org.araneaframework.uilib.support.TextType;

/**
 * Simple form component with all possible form controls and data types. The data is not mapped to beans.
 * <p>
 * This demo intends to show how forms are constructed in Aranea. The signature of the method most commonly used here is
 * following:
 * <pre>
 * form.addElement(String id, String labelId, Control control, [Data data[, Object initialValue], boolean mandatory])
 * </pre>
 * Notes:
 * <ol>
 * <li>Every element must have unique ID.
 * <li>Every element should have a label (use "#" for empty label), which is translated according to locale.
 * <li>Every element must have a Control, which specifies input type.
 * <li>An element can specify Data, which specifies target value type for element (Controls use fixed data types).
 * <li>When data is not provided, the form element <b>cannot</b> have a value (value is always null), e.g. a button.
 * <li>When initial value is not provided, the form element will have initial value of null.
 * <li>When mandatory is not set, the form element won't be mandatory (defaults to false).
 * </ol>
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 */
public class SimpleFormWidget extends TemplateBaseWidget {

  public static final String[] CARS_FRANCE = { "Citroën", "Peugeot", "Renault" };

  public static final String[] CARS_GERMANY = { "Audi", "Mercedes-Benz", "Volkswagen" };

  public static final String[] CARS_JAPAN = { "Mitsubishi", "Suzuki", "Toyota" };

  public static final String[] CARS_USA = { "Chrysler", "Ford", "Jeep" };

  protected FormWidget simpleForm = new FormWidget();

  /**
   * Builds a simple form widget with elements and initializes this widget.
   */
  @Override
  protected void init() {
    setViewSelector("form/simpleForm");

    // Let's add elements to the form.

    // Form elements can be created and modified before adding them to the form.
    // However, mostly we use shorter approach as with other elements in here.
    FormElement<String, String> el = this.simpleForm.createElement("form.text", new TextControl(0L, 255L),
        new StringData());
    this.simpleForm.addElement("textbox1", el);

    // Following shows that Control and Data are not always related: here we gather a number from text input and convert
    // it to number. However, programmers must be sure that input value, as accepted by control, can always be converted
    // to the target value (Data) type.

    this.simpleForm.addElement("textbox2", "form.textNumber", new TextControl(TextType.NUMBER_ONLY), new LongData());
    this.simpleForm.addElement("textbox3", "form.textEmail", new TextControl(TextType.EMAIL), new StringData());

    // Textarea inputs. As with TextControl, here we also specify lower (0) and upper (2000) bounds for input length.
    this.simpleForm.addElement("textarea", "form.textarea", new TextareaControl(0L, 2000L), new StringData());
    this.simpleForm.addElement("richTextarea", "form.richTextarea", new TextareaControl(0L, 2000L), new StringData());

    this.simpleForm.addElement("dateTime", "form.dateTime", new DateTimeControl(), new DateData());
    this.simpleForm.addElement("date", "form.date", new DateControl(), new DateData());
    this.simpleForm.addElement("time", "form.time", new TimeControl(), new DateData());

    this.simpleForm.addElement("intNumber", "form.integer", new NumberControl(), new IntegerData());
    this.simpleForm.addElement("floatNumber", "form.float", new FloatControl(), new BigDecimalData());

    this.simpleForm.addElement("checkbox", "form.check", new CheckboxControl(), new BooleanData());

    this.simpleForm.addElement("select", "form.select", createSimpleSelect(false), new StringData());
    this.simpleForm.addElement("selectRadio", "form.selectRadio", createSimpleSelect(true), new StringData());

    // Note that multi-selects return a collection of selected values, so StringListData is used instead of StringData.
    this.simpleForm.addElement("multiselect", "form.multiselect", createMultiSelect(), new StringListData());
    this.simpleForm.addElement("multiselectChecks", "form.multiselectChecks", createMultiSelect(), new StringListData());

    // Now let's create range inputs, where both values are optional.

    // End date value must be greater or equal to start date value.
    FormUtil.addDateTimeRangeElements(this.simpleForm, "dateTimeRange", "Start", "End", "form.dateTimeRange", true);
    FormUtil.addDateRangeElements(this.simpleForm, "dateRange", "Start", "End", "form.dateRange", true);
    FormUtil.addTimeRangeElements(this.simpleForm, "timeRange", "Start", "End", "form.timeRange", true);

    // Second integer value must be greater or equal to the first integer value.
    FormElement<BigInteger, Integer> intStart = this.simpleForm.addElement("intRangeStart", "form.numberRange",
        new NumberControl(), new IntegerData());
    FormElement<BigInteger, Integer> intEnd = this.simpleForm.addElement("intRangeEnd", "form.numberRange",
        new NumberControl(), new IntegerData());
    intEnd.setConstraint(new RangeConstraint<BigInteger, Integer>(intStart, intEnd, true));

    // Second float value must be greater or equal to the first float value.
    FormElement<BigDecimal, BigDecimal> floatStart = this.simpleForm.addElement("floatRangeStart", "form.floatRange",
        new FloatControl(), new BigDecimalData());
    FormElement<BigDecimal, BigDecimal> floatEnd = this.simpleForm.addElement("floatRangeEnd", "form.floatRange",
        new FloatControl(), new BigDecimalData());
    floatEnd.setConstraint(new RangeConstraint<BigDecimal, BigDecimal>(floatStart, floatEnd, true));

    // Now we add a button, that is also Control, to fill form elements with values.
    // We must attach an onClick listener for buttons: this listener calls handleEventFillValues() of this widget.
    ButtonControl fillButton = new ButtonControl(new ProxyOnClickEventListener(this, "fillValues"));
    // As the button does not hold any value, Data will be omitted (null).
    this.simpleForm.addElement("fillBtn", "form.fill", fillButton);

    // Add another button to form to enable validating the form.
    ButtonControl validateButton = new ButtonControl(new ProxyOnClickEventListener(this, "validateValues"));
    this.simpleForm.addElement("validateBtn", "form.validate", validateButton);

    // The usual, add the created widget to main widget.
    addWidget("simpleForm", this.simpleForm);
  }

  /**
   * A test action, invoked when button is pressed. It adds the values of formelements to message context, and they end
   * up at the top of user screen at the end of the request.
   */
  public void handleEventFillValues() {
    this.simpleForm.setValueByFullName("textbox1", "Hello, Aranea!");
    this.simpleForm.setValueByFullName("textbox2", Long.MAX_VALUE);
    this.simpleForm.setValueByFullName("textbox3", "noreply@araneaframework.org");
    this.simpleForm.setValueByFullName("textarea", "Textarea can\n  contain\n    multiple\n      lines :)");
    this.simpleForm.setValueByFullName("richTextarea", "<p><strong>HTML</strong> <em>rules!</em>!</p>");
    this.simpleForm.setValueByFullName("dateTime", new Date());
    this.simpleForm.setValueByFullName("dateTimeRangeStart", new Date());
    this.simpleForm.setValueByFullName("dateTimeRangeEnd", DateUtils.addDays(new Date(), 7));
    this.simpleForm.setValueByFullName("date", new Date());
    this.simpleForm.setValueByFullName("dateRangeStart", new Date());
    this.simpleForm.setValueByFullName("dateRangeEnd", DateUtils.addDays(new Date(), 7));
    this.simpleForm.setValueByFullName("time", new Date());
    this.simpleForm.setValueByFullName("timeRangeStart", new Date());
    this.simpleForm.setValueByFullName("timeRangeEnd", DateUtils.addMinutes(new Date(), 65));
    this.simpleForm.setValueByFullName("intNumber", Integer.MAX_VALUE);
    this.simpleForm.setValueByFullName("intRangeStart", Integer.MIN_VALUE);
    this.simpleForm.setValueByFullName("intRangeEnd", Integer.MAX_VALUE);
    this.simpleForm.setValueByFullName("floatNumber", new BigDecimal("2.71828182"));
    this.simpleForm.setValueByFullName("floatRangeStart", new BigDecimal("1.61803399"));
    this.simpleForm.setValueByFullName("floatRangeEnd", new BigDecimal("3.14159265"));
    this.simpleForm.setValueByFullName("checkbox", true);
    this.simpleForm.setValueByFullName("select", CARS_GERMANY[2].toLowerCase());
    this.simpleForm.setValueByFullName("selectRadio", CARS_FRANCE[1].toLowerCase());
    this.simpleForm.setValueByFullName("multiselect", getCarValues(CARS_JAPAN));
    this.simpleForm.setValueByFullName("multiselectChecks", getCarValues(CARS_USA));
  }

  public void handleEventValidateValues() {
    if (this.simpleForm.convertAndValidate()) {
      getMessageCtx().showInfoMessage("form.msg.valid");
    }
  }

  public static DefaultSelectControl createSimpleSelect(boolean radio) {
    DefaultSelectControl result = new DefaultSelectControl();
    if (!radio) {
      result.addItem("- Choose -", null);
    }
    addCars(result);
    return result;
  }

  public static DefaultMultiSelectControl createMultiSelect() {
    DefaultMultiSelectControl result = new DefaultMultiSelectControl();
    addCars(result);
    return result;
  }

  private static void addCars(BaseSelectControl<?, ?> control) {
    for (String car : CARS_FRANCE) {
      control.addItem(car, car.toLowerCase(), "From France");
    }
    for (String car : CARS_GERMANY) {
      control.addItem(car, car.toLowerCase(), "From Germany");
    }
    for (String car : CARS_JAPAN) {
      control.addItem(car, car.toLowerCase(), "From Japan");
    }
    for (String car : CARS_USA) {
      control.addItem(car, car.toLowerCase(), "From USA");
    }
  }

  public static List<String> getCarValues(String[] cars) {
    String[] results = new String[cars.length];
    for (int i = 0; i < cars.length; i++) {
      results[i] = cars[i].toLowerCase();
    }
    return Arrays.asList(results);
  }
}
