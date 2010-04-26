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

package org.araneaframework.tests;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import junit.framework.TestCase;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.core.StandardPath;
import org.araneaframework.http.core.StandardServletInputData;
import org.araneaframework.mock.MockHttpInputData;
import org.araneaframework.mock.MockOutputData;
import org.araneaframework.tests.mock.MockEnvironment;
import org.araneaframework.tests.util.RequestUtil;
import org.araneaframework.uilib.event.OnClickEventListener;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.Converter;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.GenericFormElement;
import org.araneaframework.uilib.form.constraint.ConstraintGroupHelper;
import org.araneaframework.uilib.form.constraint.GroupedConstraint;
import org.araneaframework.uilib.form.constraint.NotEmptyConstraint;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.control.CheckboxControl;
import org.araneaframework.uilib.form.control.DateTimeControl;
import org.araneaframework.uilib.form.control.DefaultSelectControl;
import org.araneaframework.uilib.form.control.NumberControl;
import org.araneaframework.uilib.form.control.StringArrayRequestControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.control.TextareaControl;
import org.araneaframework.uilib.form.converter.IdenticalConverter;
import org.araneaframework.uilib.form.converter.StringToDisplayItemConverter;
import org.araneaframework.uilib.form.converter.StringToNumberConverter;
import org.araneaframework.uilib.form.converter.TimestampToDateConverter;
import org.araneaframework.uilib.form.data.BooleanData;
import org.araneaframework.uilib.form.data.DateData;
import org.araneaframework.uilib.form.data.IntegerData;
import org.araneaframework.uilib.form.data.LongData;
import org.araneaframework.uilib.form.data.StringData;
import org.araneaframework.uilib.support.DisplayItem;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class FormTest extends TestCase {

  private static final Log LOG = LogFactory.getLog(FormTest.class);

  private boolean eventsWork = false;

  public FormTest(String name) {
    super(name);
  }

  private FormWidget makeUsualForm() throws Exception {
    // Creating form :-)
    FormWidget testForm = new FormWidget();

    // Adding elements to form
    testForm.addElement("myCheckBox", "my checkbox", new CheckboxControl(), new BooleanData(), true);
    testForm.addElement("myLongText", "my long text", new TextControl(), new LongData(), true);
    testForm.addElement("myDateTime", "my date and time", new DateTimeControl(), new DateData());
    testForm.addElement("myButton", "my button", new ButtonControl());

    // Adding a composite element
    testForm.addElement("hierarchyTest.myTextarea", "my text area", new TextareaControl(), new StringData(), true);

    // Filling in select control (which is under a composite element)
    FormElement<DisplayItem, String> mySelectElement = testForm.addElement("hierarchyTest.mySelect", "my drop down",
        new DefaultSelectControl(), new StringData(), true);

    DefaultSelectControl mySelect = (DefaultSelectControl) mySelectElement.getControl();
    mySelect.addItem("one", "1");
    mySelect.addItem("two", "2");
    mySelect.addItem("three", "3");
    mySelect.addItem("four", "4");

    testForm._getComponent().init(null, new MockEnvironment());

    return testForm;
  }

  /**
   * Tests converter picking
   */
  public void testConverters() throws Exception {
    FormWidget testForm = makeUsualForm();

    // Checking that converter is picked right
    testConverter(testForm, "myCheckBox", IdenticalConverter.class);
    testConverter(testForm, "myLongText", StringToNumberConverter.class);
    testConverter(testForm, "myDateTime", TimestampToDateConverter.class);
    testConverter(testForm, "hierarchyTest.mySelect", StringToDisplayItemConverter.class);
    testConverter(testForm, "hierarchyTest.myTextarea", IdenticalConverter.class);
  }

  private void testConverter(FormWidget form, String element, Class<?> targetConverter) {
    Converter<?, ?> converter = ((FormElement<?, ?>) form.getElementByFullName(element)).getConverter();
    assertTrue(converter != null && converter.getClass() == targetConverter);
  }

  /**
   * Tests value assigning.
   */
  public void testValueAssigning() throws Exception {
    FormWidget testForm = makeUsualForm();

    Date now = new Date(System.currentTimeMillis());

    // Setting initial form data
    setValue(testForm, "myCheckBox", true);
    setValue(testForm, "myLongText", 16L);
    setValue(testForm, "myDateTime", now);
    setValue(testForm, "hierarchyTest.mySelect", 12637L);
    setValue(testForm, "hierarchyTest.myTextarea", "MIB");

    // Checking that the data assigning works
    testData(testForm, "myCheckBox", true);
    testData(testForm, "myLongText", 16L);
    testData(testForm, "myDateTime", now);
    testData(testForm, "hierarchyTest.mySelect", 12637L);
    testData(testForm, "hierarchyTest.myTextarea", "MIB");
  }

  @SuppressWarnings("unchecked")
  private void setValue(FormWidget form, String element, Object value) {
    ((FormElement) form.getElementByFullName(element)).getData().setValue(value);
  }

  @SuppressWarnings("unchecked")
  private void testData(FormWidget form, String element, Object targetValue) {
    Object value = ((FormElement) form.getElementByFullName(element)).getData().getValue();
    assertTrue(ObjectUtils.equals(value, targetValue));
  }

  /**
   * Testing reading from valid request.
   */
  @SuppressWarnings("unchecked")
  public void testFormValidRequestReading() throws Exception {

    FormWidget testForm = makeUsualForm();

    MockHttpServletRequest validRequest = RequestUtil.markSubmitted(new MockHttpServletRequest());

    validRequest.addParameter("myCheckBox", (String) null);
    setRendered(testForm, "myCheckBox");

    validRequest.addParameter("myLongText", "108");
    setRendered(testForm, "myLongText");

    setRendered(testForm, "myDateTime");
    validRequest.addParameter("myDateTime.date", "11.10.2015");
    validRequest.addParameter("myDateTime.time", "01:01");

    setRendered(testForm, "hierarchyTest.myTextarea");
    validRequest.addParameter("hierarchyTest.myTextarea", "blah");

    setRendered(testForm, "hierarchyTest.mySelect");
    validRequest.addParameter("hierarchyTest.mySelect", "2");

    // Trying to read from a valid request
    StandardServletInputData input = new StandardServletInputData(validRequest);

    // Test in life-cycle order, without calling event and render.
    testForm._getWidget().update(input);
    assertTrue("Test form must be valid after reading from request", testForm.convertAndValidate());
    testForm._getWidget().render(new MockOutputData());

    Date reqDate = (new SimpleDateFormat("dd.MM.yyyy hh:mm")).parse("11.10.2015 01:01");

    // Checking that reading from request works

    assertEquals(testForm.getValueByFullName("myCheckBox"), Boolean.FALSE);
    assertEquals(testForm.getValueByFullName("myLongText"), 108L);
    assertEquals(testForm.getValueByFullName("myDateTime"), reqDate);
    assertEquals(testForm.getValueByFullName("hierarchyTest.mySelect"), "2");
    assertEquals(testForm.getValueByFullName("hierarchyTest.myTextarea"), "blah");

    StringArrayRequestControl.ViewModel vm1 = ((CheckboxControl) testForm.getControlByFullName("myCheckBox")).getViewModel();

    assertEquals(Boolean.valueOf(vm1.getSimpleValue()), Boolean.FALSE);

    TextControl.ViewModel vm2 = ((TextControl) testForm.getControlByFullName("myLongText")).getViewModel();

    assertEquals(Long.valueOf(vm2.getSimpleValue()), Long.valueOf(108L));
    assertEquals(getValue(testForm, "myCheckBox"), Boolean.FALSE);
    assertEquals(getValue(testForm, "myLongText"), 108L);
    assertEquals(getValue(testForm, "myDateTime"), reqDate);
    assertEquals(getValue(testForm, "hierarchyTest.mySelect"), "2");
    assertEquals(getValue(testForm, "hierarchyTest.myTextarea"), "blah");
  }

  private void setRendered(FormWidget form, String elementId) {
    ((FormElement<?, ?>) form.getElementByFullName(elementId)).rendered();
  }

  private Object getValue(FormWidget form, String elementId) {
    return ((FormElement<?, ?>) form.getElement(elementId)).getData().getValue();
  }

  /**
   * Testing reading from invalid request.
   */
  public void testFormInvalidRequestReading() throws Exception {
    FormWidget testForm = makeUsualForm();

    MockHttpServletRequest invalidRequest = RequestUtil.markSubmitted(new MockHttpServletRequest());

    invalidRequest.addParameter("myCheckBox", "ksjf");
    setRendered(testForm, "myCheckBox");

    setRendered(testForm, "myDateTime");
    invalidRequest.addParameter("myDateTime.date", "HA-HA");
    invalidRequest.addParameter("myDateTime.time", "BLAH");

    setRendered(testForm, "hierarchyTest.myTextarea");
    invalidRequest.addParameter("hierarchyTest.myTextarea", "");

    // Testing that invalid requests are read right
    StandardServletInputData input = new StandardServletInputData(invalidRequest);
    testForm._getWidget().update(input);

    assertTrue("Test form must not be valid after reading from request", !testForm.convertAndValidate());
  }

  /**
   * Testing reading from request with a mandatory element missing.
   */
  public void testFormMandatoryMissingRequestReading() throws Exception {

    FormWidget testForm = makeUsualForm();

    MockHttpServletRequest mandatoryMissingRequest = RequestUtil.markSubmitted(new MockHttpServletRequest());

    mandatoryMissingRequest.addParameter("myCheckBox", "true");
    mandatoryMissingRequest.addParameter("myLongText", "108");
    mandatoryMissingRequest.addParameter("myDateTime.date", "11.10.2015");
    mandatoryMissingRequest.addParameter("myDateTime.time", "01:01");
    mandatoryMissingRequest.addParameter("hierarchyTest.mySelect", "2");

    // Testing that mandatory items are processed right
    StandardServletInputData input = new StandardServletInputData(mandatoryMissingRequest);
    testForm._getWidget().update(input);

    assertTrue("Test form must not be valid after reading from request", !testForm.convertAndValidate());
  }

  /**
   * Testing reading from request with a not mandatory element missing.
   */
  public void testFormNotMandatoryMissingRequestReading() throws Exception {

    FormWidget testForm = makeUsualForm();

    MockHttpServletRequest notMandatoryMissingRequest = RequestUtil.markSubmitted(new MockHttpServletRequest());

    notMandatoryMissingRequest.addParameter("myCheckBox", (String) null);
    setRendered(testForm, "myCheckBox");

    notMandatoryMissingRequest.addParameter("myLongText", "108");
    setRendered(testForm, "myLongText");

    notMandatoryMissingRequest.addParameter("hierarchyTest.myTextarea", "blah");
    setRendered(testForm, "hierarchyTest.myTextarea");

    notMandatoryMissingRequest.addParameter("hierarchyTest.mySelect", "3");
    setRendered(testForm, "hierarchyTest.mySelect");

    StandardServletInputData input = new StandardServletInputData(notMandatoryMissingRequest);
    testForm._getWidget().update(input);

    assertTrue("Test form must be valid after reading from request", testForm.convertAndValidate());
  }

  /**
   * Testing reading from request with a grouped constraint set.
   */
  public void testFormActiveGroupedConstraintInvalidates() throws Exception {
    FormWidget testForm = makeUsualForm();

    MockHttpServletRequest notMandatoryMissingRequest = RequestUtil.markSubmitted(new MockHttpServletRequest());

    notMandatoryMissingRequest.addParameter("myCheckBox", "true");
    setRendered(testForm, "myCheckBox");

    notMandatoryMissingRequest.addParameter("myLongText", "108");
    setRendered(testForm, "myLongText");

    setRendered(testForm, "myDateTime");
    notMandatoryMissingRequest.addParameter("myDateTime", (String) null);

    notMandatoryMissingRequest.addParameter("hierarchyTest.myTextarea", "blah");
    setRendered(testForm, "hierarchyTest.myTextarea");
    notMandatoryMissingRequest.addParameter("hierarchyTest.mySelect", "2");
    setRendered(testForm, "hierarchyTest.mySelect");

    // create helper
    ConstraintGroupHelper groupHelper = new ConstraintGroupHelper();
    testForm.getElement("myDateTime").setConstraint(
        groupHelper.createGroupedConstraint(new NotEmptyConstraint<Object, Object>(), "active"));

    testForm._getWidget().update(new StandardServletInputData(notMandatoryMissingRequest));

    groupHelper.setActiveGroup("active");

    assertTrue("Test form must not be valid after reading from request", !testForm.convertAndValidate());
  }

  /**
   * Testing reading from request with a grouped constraint set.
   */
  public void testFormActiveGroupedConstraintValidates() throws Exception {

    FormWidget testForm = makeUsualForm();

    MockHttpServletRequest notMandatoryMissingRequest = RequestUtil.markSubmitted(new MockHttpServletRequest());

    notMandatoryMissingRequest.addParameter("myCheckBox", "true");
    setRendered(testForm, "myCheckBox");

    notMandatoryMissingRequest.addParameter("myLongText", "108");
    setRendered(testForm, "myLongText");

    setRendered(testForm, "myDateTime");
    notMandatoryMissingRequest.addParameter("myDateTime.date", "11.10.2015");
    notMandatoryMissingRequest.addParameter("myDateTime.time", "01:01");

    notMandatoryMissingRequest.addParameter("hierarchyTest.myTextarea", "blah");
    setRendered(testForm, "hierarchyTest.myTextarea");
    notMandatoryMissingRequest.addParameter("hierarchyTest.mySelect", "2");
    setRendered(testForm, "hierarchyTest.mySelect");

    // create helper
    ConstraintGroupHelper groupHelper = new ConstraintGroupHelper();
    GroupedConstraint groupedConstraint = (GroupedConstraint) groupHelper.createGroupedConstraint(
        new NotEmptyConstraint<Object, Object>(), "active");
    testForm.getElement("myDateTime").setConstraint(groupedConstraint);

    StandardServletInputData input = new StandardServletInputData(notMandatoryMissingRequest);
    testForm._getWidget().update(input);

    groupHelper.setActiveGroup("active");
    assertTrue("Test form must be valid after reading from request", testForm.convertAndValidate());
  }

  /**
   * Testing reading from request with a grouped constraint set.
   */
  public void testFormInactiveGroupedConstraintValidates() throws Exception {

    FormWidget testForm = makeUsualForm();

    MockHttpServletRequest notMandatoryMissingRequest = RequestUtil.markSubmitted(new MockHttpServletRequest());

    notMandatoryMissingRequest.addParameter("myCheckBox", "true");
    setRendered(testForm, "myCheckBox");

    notMandatoryMissingRequest.addParameter("myLongText", "108");
    setRendered(testForm, "myLongText");

    setRendered(testForm, "myDateTime");
    notMandatoryMissingRequest.addParameter("myDateTime.date", "11.10.2015");
    notMandatoryMissingRequest.addParameter("myDateTime.time", "01:01");

    notMandatoryMissingRequest.addParameter("hierarchyTest.myTextarea", "blah");
    setRendered(testForm, "hierarchyTest.myTextarea");
    notMandatoryMissingRequest.addParameter("hierarchyTest.mySelect", "2");
    setRendered(testForm, "hierarchyTest.mySelect");

    // create helper
    ConstraintGroupHelper groupHelper = new ConstraintGroupHelper();
    testForm.getElement("myDateTime").setConstraint(
        groupHelper.createGroupedConstraint(new NotEmptyConstraint<Object, Object>(), "active"));

    StandardServletInputData input = new StandardServletInputData(notMandatoryMissingRequest);
    testForm._getWidget().update(input);

    assertTrue("Test form must be valid after reading from request", testForm.convertAndValidate());
  }

  /**
   * Testing events.
   */
  public void testFormEventProcessing() throws Exception {
    FormWidget testForm = makeUsualForm();

    // Simple event
    setRendered(testForm, "myButton");
    ((ButtonControl) ((FormElement<?, ?>) testForm.getElement("myButton")).getControl())
        .addOnClickEventListener(new TestOnClickEventHandler());

    Map<String, String> data = new HashMap<String, String>();
    data.put(ApplicationWidget.EVENT_HANDLER_ID_KEY, "onClicked");
    MockHttpInputData input = new MockHttpInputData(data);

    ((FormElement<?, ?>) testForm.getElement("myButton"))._getWidget().update(input);
    testForm._getWidget().event(new StandardPath("myButton"), input);

    assertTrue("Event succeeded", this.eventsWork);
  }

  /**
   * TestCase: http://changelogic.araneaframework.org/index.php?event=Show_task&task_id=316&project_id=0 Disabled
   * &lt;select&gt; is created, initial value set. Request does not contain this select (disabled HTML elements are not
   * submitted). FormElement must be considered valid.
   */
  @SuppressWarnings("unchecked")
  public void testDisabledNonNullMandatorySelectValidates() throws Exception {
    FormWidget testForm = new FormWidget();

    DefaultSelectControl selectControl = new DefaultSelectControl();
    selectControl.addItem("- choose -", null);
    selectControl.addItem("one", "1");
    selectControl.addItem("two", "2");

    testForm.addElement("select", "#Select", selectControl, new StringData(), true);
    FormElement<?, String> selectElement = (FormElement<?, String>) testForm.getElement("select");
    selectElement.setValue("1");
    selectElement.setDisabled(true);
    selectElement.rendered();

    testForm._getComponent().init(null, new MockEnvironment());

    MockHttpServletRequest almostEmptyRequest = RequestUtil.markSubmitted(new MockHttpServletRequest());

    almostEmptyRequest.addParameter("dummyParam", "true");

    StandardServletInputData input = new StandardServletInputData(almostEmptyRequest);

    testForm._getWidget().update(input);

    testForm.getValueByFullName("select");
    assertTrue("Test form disabled select element with assigned value must be valid after reading from request.",
        testForm.convertAndValidate());
  }

  /** Test that form addElement methods accept hierarchical ids creating subforms on demand */
  public void testHierarchicalSubformCreation() throws Exception {
    FormWidget form = new FormWidget();
    FormElement<Boolean, Boolean> element = form.addElement("1.2", "dummyLabel", new CheckboxControl(), new BooleanData());

    assertTrue("Created element should be accessible", form.getElementByFullName("1.2").equals(element));
    assertTrue("Subform should have been created", form.getElement("1") instanceof FormWidget);

    // flat addElementAfter after nested form
    FormElement<String, String> afterElement = form.createElement("dumbLabel", new TextControl(), new StringData());
    form.addElementAfter("after", afterElement, "1");

    Iterator<GenericFormElement> iterator = form.getElements().values().iterator();

    assertTrue("First should be the first nested form", iterator.next() instanceof FormWidget);
    assertEquals("Second should be the 'afterElement'", iterator.next(), afterElement);
    assertTrue("Should be no more elements in that form", !iterator.hasNext());

    FormElement<?, ?> nestedAfterElement = form.createElement("dumbdumbLabel", new TextControl(), new StringData());

    // addElementAfter with nested afterId is supported
    form.addElementAfter("3", nestedAfterElement, "1.2");

    iterator = form.getSubFormByFullName("1").getElements().values().iterator();
    assertTrue("First should be the first 'element'", element.equals(iterator.next()));
    assertTrue("Second should be the 'nestedAfterElement'", nestedAfterElement.equals(iterator.next()));

    // addElementAfter with nested id is not supported
    try { // TODO check it
      FormElement<?, ?> nextElement = form.createElement("dumbLabel", new NumberControl(), new IntegerData(), false);
      form.addElementAfter("x.y", nextElement, "1");
    } catch (Exception e) {
      fail("addElementAfter() with nested added element id failed");
    }
  }

  /**
   * Tests helper functions that access form parts by their full name.
   */
  public void testFormTraversal() throws Exception {
    FormWidget form = makeUsualForm();

    assertTrue("'myCheckBox' must contain a CheckboxControl!",
        form.getControlByFullName("myCheckBox") instanceof CheckboxControl);
    assertTrue("'hierarchyTest.myTextarea' must contain a TextareaControl!", form
        .getControlByFullName("hierarchyTest.myTextarea") instanceof TextareaControl);

    Control<?> result = form.getControlByFullName("hierarchyTestm.yTextarea");

    assertTrue("An exception must be thrown if wrong element name is given!", result == null);
  }

  /**
   * Tests that uninitialized form conversion works (that should be a no-op)
   */
  public void testUnInitializedFormConversion() throws Exception {
    FormWidget form = new FormWidget();
    form.addElement("elementName", "labelId", new TextControl(), new StringData(), true);

    // should succeed
    form.convert();
  }

  private class TestOnClickEventHandler implements OnClickEventListener {

    public void onClick() {
      LOG.debug("Received onClick event!");
      FormTest.this.eventsWork = true;
    }
  }
}
