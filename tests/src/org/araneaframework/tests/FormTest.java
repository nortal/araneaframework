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

package org.araneaframework.tests;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import junit.framework.TestCase;
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
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.constraint.ConstraintGroupHelper;
import org.araneaframework.uilib.form.constraint.GroupedConstraint;
import org.araneaframework.uilib.form.constraint.NotEmptyConstraint;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.control.CheckboxControl;
import org.araneaframework.uilib.form.control.DateTimeControl;
import org.araneaframework.uilib.form.control.NumberControl;
import org.araneaframework.uilib.form.control.SelectControl;
import org.araneaframework.uilib.form.control.StringArrayRequestControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.control.TextareaControl;
import org.araneaframework.uilib.form.converter.IdenticalConverter;
import org.araneaframework.uilib.form.converter.StringToLongConverter;
import org.araneaframework.uilib.form.converter.TimestampToDateConverter;
import org.araneaframework.uilib.form.data.BooleanData;
import org.araneaframework.uilib.form.data.DateData;
import org.araneaframework.uilib.form.data.IntegerData;
import org.araneaframework.uilib.form.data.LongData;
import org.araneaframework.uilib.form.data.StringData;
import org.araneaframework.uilib.support.DisplayItem;
import org.springframework.mock.web.MockHttpServletRequest;


/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class FormTest extends TestCase {

  private static final Log log = LogFactory.getLog(FormTest.class);

  boolean eventsWork = false;
  
  public FormTest(String name) {
    super(name);
  }
  
  private FormWidget makeUsualForm() throws Exception {

    //Creating form :-)
    FormWidget testForm = new FormWidget();

    //Adding elements to form
    testForm.addElement("myCheckBox", "my checkbox", new CheckboxControl(), new BooleanData(), true);
    testForm.addElement("myLongText", "my long text", new TextControl(), new LongData(), true);
    testForm.addElement("myDateTime", "my date and time", new DateTimeControl(), new DateData(), false);
    testForm.addElement("myButton", "my button", new ButtonControl(), null, false);

    //Adding a composite element
    FormWidget hierarchyTest = testForm.addSubForm("hierarchyTest");
    hierarchyTest.addElement("myTextarea", "my text area", new TextareaControl(), new StringData(), true);

    //Filling in select control (which is under a composite element)
    FormElement mySelectElement = hierarchyTest.addElement("mySelect", "my drop down", new SelectControl(), new LongData(), true);
    SelectControl mySelect = (SelectControl) mySelectElement.getControl();
    mySelect.addItem(new DisplayItem("1", "one"));
    mySelect.addItem(new DisplayItem("2", "two"));
    mySelect.addItem(new DisplayItem("3", "three"));
    mySelect.addItem(new DisplayItem("4", "four"));
    
    testForm._getComponent().init(null, new MockEnvironment());

    return testForm;
  }

  /**
   * Tests converter picking
   */
  public void testConverters() throws Exception {

    FormWidget testForm = makeUsualForm();

    FormWidget hierarchyTest = (FormWidget) testForm.getElement("hierarchyTest");

    //Checking that converter is picked right
    assertTrue(((FormElement) testForm.getElement("myCheckBox")).getConverter() instanceof IdenticalConverter);
    assertTrue(((FormElement) testForm.getElement("myLongText")).getConverter() instanceof StringToLongConverter);
    assertTrue(((FormElement) testForm.getElement("myDateTime")).getConverter() instanceof TimestampToDateConverter);
    assertTrue(((FormElement) hierarchyTest.getElement("mySelect")).getConverter() instanceof StringToLongConverter);
    assertTrue(((FormElement) hierarchyTest.getElement("myTextarea")).getConverter() instanceof IdenticalConverter);
  }

  /**
   * Tests value assigning.
   */
  public void testValueAssigning() throws Exception {

    FormWidget testForm = makeUsualForm();

    FormWidget hierarchyTest = (FormWidget) testForm.getElement("hierarchyTest");

    //Setting initial form data
     ((FormElement) testForm.getElement("myCheckBox")).getData().setValue(Boolean.TRUE);
    ((FormElement) testForm.getElement("myLongText")).getData().setValue(new Long(16));

    Date now = new Date(System.currentTimeMillis());

    ((FormElement) testForm.getElement("myDateTime")).getData().setValue(now);
    ((FormElement) hierarchyTest.getElement("mySelect")).getData().setValue(new Long(12637));
    ((FormElement) hierarchyTest.getElement("myTextarea")).getData().setValue("MIB");

    //Checking that the data assigning works
    assertTrue(((FormElement) testForm.getElement("myCheckBox")).getData().getValue().equals(Boolean.TRUE));
    assertTrue(((FormElement) testForm.getElement("myLongText")).getData().getValue().equals(new Long(16)));
    assertTrue(((FormElement) testForm.getElement("myDateTime")).getData().getValue().equals(now));
    assertTrue(((FormElement) hierarchyTest.getElement("mySelect")).getData().getValue().equals(new Long(12637)));
    assertTrue(((FormElement) hierarchyTest.getElement("myTextarea")).getData().getValue().equals("MIB"));
  }

  /**
   * Testing reading from valid request.
   */
  public void testFormValidRequestReading() throws Exception {

    FormWidget testForm = makeUsualForm();

    FormWidget hierarchyTest = (FormWidget) testForm.getElement("hierarchyTest");

    MockHttpServletRequest validRequest = 
    	RequestUtil.markSubmitted(new MockHttpServletRequest());

    validRequest.addParameter("myCheckBox", (String) null);
    ((FormElement) testForm.getElement("myCheckBox")).rendered();

    validRequest.addParameter("myLongText", "108");
    ((FormElement) testForm.getElement("myLongText")).rendered();
    
    ((FormElement) testForm.getElement("myDateTime")).rendered();
    validRequest.addParameter("myDateTime.date", "11.10.2015");
    validRequest.addParameter("myDateTime.time", "01:01");

    (((FormElement) ((FormWidget)testForm.getElement("hierarchyTest")).getElement("myTextarea"))).rendered();
    validRequest.addParameter("hierarchyTest.myTextarea", "blah");

    (((FormElement) ((FormWidget)testForm.getElement("hierarchyTest")).getElement("mySelect"))).rendered();
    validRequest.addParameter("hierarchyTest.mySelect", "2");

    //Trying to read from a valid request
    StandardServletInputData input = new StandardServletInputData(validRequest);
    
    // Test in lifecycle order, without calling event and render.
    testForm._getWidget().update(input);
    assertTrue("Test form must be valid after reading from request", testForm.convertAndValidate());
    testForm._getWidget().render(new MockOutputData());
    
    Date reqDate = (new SimpleDateFormat("dd.MM.yyyy hh:mm")).parse("11.10.2015 01:01");

    //Checking that reading from request works
    
    assertTrue(testForm.getValueByFullName("myCheckBox").equals(Boolean.FALSE));
    assertTrue(testForm.getValueByFullName("myLongText").equals(new Long(108)));
    assertTrue(testForm.getValueByFullName("myDateTime").equals(reqDate));
    assertTrue(hierarchyTest.getValueByFullName("mySelect").equals(new Long(2)));
    assertTrue(hierarchyTest.getValueByFullName("myTextarea").equals("blah"));
    
    StringArrayRequestControl.ViewModel vm1 = 
    	((CheckboxControl)testForm.getControlByFullName("myCheckBox")).getViewModel();
    
    assertTrue((Boolean.valueOf(vm1.getSimpleValue()).equals(Boolean.FALSE)));
    
    TextControl.ViewModel vm2 = 
    	((TextControl)testForm.getControlByFullName("myLongText")).getViewModel();
    assertTrue(Long.valueOf(vm2.getSimpleValue()).equals(new Long(108)));
    
    assertTrue(((FormElement) testForm.getElement("myCheckBox")).getData().getValue().equals(Boolean.FALSE));
    assertTrue(((FormElement) testForm.getElement("myLongText")).getData().getValue().equals(new Long(108)));
    assertTrue(((FormElement) testForm.getElement("myDateTime")).getData().getValue().equals(reqDate));
    assertTrue(((FormElement) hierarchyTest.getElement("mySelect")).getData().getValue().equals(new Long(2)));
    assertTrue(((FormElement) hierarchyTest.getElement("myTextarea")).getData().getValue().equals("blah"));
  }

  /**
   * Testing reading from invalid request.
   */
  public void testFormInvalidRequestReading() throws Exception {
    FormWidget testForm = makeUsualForm();

    MockHttpServletRequest invalidRequest =
    	RequestUtil.markSubmitted(new MockHttpServletRequest());

    invalidRequest.addParameter("myCheckBox", "ksjf");
    ((FormElement) testForm.getElement("myCheckBox")).rendered();

    ((FormElement) testForm.getElement("myDateTime")).rendered();
    invalidRequest.addParameter("myDateTime.date", "HA-HA");
    invalidRequest.addParameter("myDateTime.time", "BLAH");

    (((FormElement)((FormWidget)testForm.getElement("hierarchyTest")).getElement("myTextarea"))).rendered();
    invalidRequest.addParameter("hierarchyTest.myTextarea", "");    

    //Testing that invalid requests are read right
    StandardServletInputData input = new StandardServletInputData(invalidRequest);
    testForm._getWidget().update(input);
    
    assertTrue("Test form must not be valid after reading from request", !testForm.convertAndValidate());
  }

  /**
   * Testing reading from request with a mandatory element missing.
   */
  public void testFormMandatoryMissingRequestReading() throws Exception {

    FormWidget testForm = makeUsualForm();

    MockHttpServletRequest mandatoryMissingRequest =
    	RequestUtil.markSubmitted(new MockHttpServletRequest());

    mandatoryMissingRequest.addParameter("myCheckBox", "true");
    mandatoryMissingRequest.addParameter("myLongText", "108");
    mandatoryMissingRequest.addParameter("myDateTime.date", "11.10.2015");
    mandatoryMissingRequest.addParameter("myDateTime.time", "01:01");
    mandatoryMissingRequest.addParameter("hierarchyTest.mySelect", "2");

    //Testing that mandatory items are processed right
    StandardServletInputData input = new StandardServletInputData(mandatoryMissingRequest);
    testForm._getWidget().update(input);
    
    assertTrue("Test form must not be valid after reading from request", !testForm.convertAndValidate());
  }

  /**
   * Testing reading from request with a not mandatory element missing.
   */
  public void testFormNotMandatoryMissingRequestReading() throws Exception {

    FormWidget testForm = makeUsualForm();

    MockHttpServletRequest notMandatoryMissingRequest =
    	RequestUtil.markSubmitted(new MockHttpServletRequest());

    notMandatoryMissingRequest.addParameter("myCheckBox", (String) null);
    ((FormElement)testForm.getElement("myCheckBox")).rendered();

    notMandatoryMissingRequest.addParameter("myLongText", "108");
    ((FormElement)testForm.getElement("myLongText")).rendered();

    notMandatoryMissingRequest.addParameter("hierarchyTest.myTextarea", "blah");
    (((FormElement)((FormWidget)testForm.getElement("hierarchyTest")).getElement("myTextarea"))).rendered();

    notMandatoryMissingRequest.addParameter("hierarchyTest.mySelect", "3");
    (((FormElement)((FormWidget)testForm.getElement("hierarchyTest")).getElement("mySelect"))).rendered();

    StandardServletInputData input = new StandardServletInputData(notMandatoryMissingRequest);
    testForm._getWidget().update(input);
    
    assertTrue("Test form must be valid after reading from request", testForm.convertAndValidate());
  }
  
/**
   * Testing reading from request with a grouped constraint set.
   */
  public void testFormActiveGroupedConstraintInvalidates() throws Exception {
    FormWidget testForm = makeUsualForm();

    MockHttpServletRequest notMandatoryMissingRequest =
    	RequestUtil.markSubmitted(new MockHttpServletRequest());

    notMandatoryMissingRequest.addParameter("myCheckBox", "true");
    ((FormElement)testForm.getElement("myCheckBox")).rendered();

    notMandatoryMissingRequest.addParameter("myLongText", "108");
    ((FormElement)testForm.getElement("myLongText")).rendered();
    
    ((FormElement)testForm.getElement("myDateTime")).rendered();
    notMandatoryMissingRequest.addParameter("myDateTime", (String) null);

    notMandatoryMissingRequest.addParameter("hierarchyTest.myTextarea", "blah");
    (((FormElement)((FormWidget)testForm.getElement("hierarchyTest")).getElement("myTextarea"))).rendered();
    notMandatoryMissingRequest.addParameter("hierarchyTest.mySelect", "2");
    (((FormElement)((FormWidget)testForm.getElement("hierarchyTest")).getElement("mySelect"))).rendered();

    // create helper
    ConstraintGroupHelper groupHelper = new ConstraintGroupHelper();
    testForm.getElement("myDateTime").setConstraint(
        groupHelper.createGroupedConstraint(new NotEmptyConstraint(), "active"));

    StandardServletInputData input = new StandardServletInputData(notMandatoryMissingRequest);
    testForm._getWidget().update(input);

    groupHelper.setActiveGroup("active");

    assertTrue("Test form must not be valid after reading from request", !testForm.convertAndValidate());
  }
  
  /**
   * Testing reading from request with a grouped constraint set.
   */
  public void testFormActiveGroupedConstraintValidates() throws Exception {

    FormWidget testForm = makeUsualForm();

    MockHttpServletRequest notMandatoryMissingRequest = 
    	RequestUtil.markSubmitted(new MockHttpServletRequest());

    notMandatoryMissingRequest.addParameter("myCheckBox", "true");
    ((FormElement)testForm.getElement("myCheckBox")).rendered();
    
    notMandatoryMissingRequest.addParameter("myLongText", "108");
    ((FormElement)testForm.getElement("myLongText")).rendered();
    
    ((FormElement)testForm.getElement("myDateTime")).rendered();
    notMandatoryMissingRequest.addParameter("myDateTime.date", "11.10.2015");
    notMandatoryMissingRequest.addParameter("myDateTime.time", "01:01");
    
    notMandatoryMissingRequest.addParameter("hierarchyTest.myTextarea", "blah");
    (((FormElement)((FormWidget)testForm.getElement("hierarchyTest")).getElement("myTextarea"))).rendered();
    notMandatoryMissingRequest.addParameter("hierarchyTest.mySelect", "2");
    (((FormElement)((FormWidget)testForm.getElement("hierarchyTest")).getElement("mySelect"))).rendered();

    // create helper
    ConstraintGroupHelper groupHelper = new ConstraintGroupHelper();
    GroupedConstraint groupedConstraint = (GroupedConstraint) groupHelper.createGroupedConstraint(new NotEmptyConstraint(), "active");
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

    MockHttpServletRequest notMandatoryMissingRequest = 
    	RequestUtil.markSubmitted(new MockHttpServletRequest());

    notMandatoryMissingRequest.addParameter("myCheckBox", "true");
    ((FormElement)testForm.getElement("myCheckBox")).rendered();
    
    notMandatoryMissingRequest.addParameter("myLongText", "108");
    ((FormElement)testForm.getElement("myLongText")).rendered();
    
    ((FormElement)testForm.getElement("myDateTime")).rendered();
    notMandatoryMissingRequest.addParameter("myDateTime.date", "11.10.2015");
    notMandatoryMissingRequest.addParameter("myDateTime.time", "01:01");
    
    notMandatoryMissingRequest.addParameter("hierarchyTest.myTextarea", "blah");
    (((FormElement)((FormWidget)testForm.getElement("hierarchyTest")).getElement("myTextarea"))).rendered();
    notMandatoryMissingRequest.addParameter("hierarchyTest.mySelect", "2");
    (((FormElement)((FormWidget)testForm.getElement("hierarchyTest")).getElement("mySelect"))).rendered();

    // create helper
    ConstraintGroupHelper groupHelper = new ConstraintGroupHelper();
    testForm.getElement("myDateTime").setConstraint(groupHelper.createGroupedConstraint(new NotEmptyConstraint(), "active"));
    
    StandardServletInputData input = new StandardServletInputData(notMandatoryMissingRequest);
    testForm._getWidget().update(input);
    
    assertTrue("Test form must be valid after reading from request", testForm.convertAndValidate());       
  }
	
  /**
   * Testing events.
   */
  public void testFormEventProcessing() throws Exception {
    FormWidget testForm = makeUsualForm();

    //Simple event
    ((FormElement) testForm.getElement("myButton")).rendered();
    ((ButtonControl) ((FormElement) testForm.getElement("myButton")).getControl()).addOnClickEventListener(new TestOnClickEventHandler());
     
    Map data = new HashMap();
    data.put(ApplicationWidget.EVENT_HANDLER_ID_KEY, "onClicked");
    MockHttpInputData input = new MockHttpInputData(data);
    
    ((FormElement) testForm.getElement("myButton"))._getWidget().update(input);
    testForm._getWidget().event(new StandardPath("myButton"), input);
    
    assertTrue("Event succeeded", eventsWork);
  }
  
  /**
   * Testcase: http://changelogic.araneaframework.org/index.php?event=Show_task&task_id=316&project_id=0
   * Disabled &lt;select&gt; is created, initial value set. Request does not contain this select (disabled
   * HTML elements are not submitted). FormElement must be considered valid.
   */
  public void testDisabledNonNullMandatorySelectValidates() throws Exception {
    FormWidget testForm = new FormWidget();
    
    SelectControl selectControl = new SelectControl();
	selectControl.addItem(new DisplayItem(null, "- choose -"));
	selectControl.addItem(new DisplayItem("1", "one"));
	selectControl.addItem(new DisplayItem("2", "two"));

	testForm.addElement("select", "#Select", selectControl, new StringData(), true);
	FormElement selectElement = (FormElement) testForm.getElement("select");
	selectElement.setValue("1");
	selectElement.setDisabled(true);
	selectElement.rendered();
	
	testForm._getComponent().init(null, new MockEnvironment());
	
    MockHttpServletRequest almostEmptyRequest = 
    	RequestUtil.markSubmitted(new MockHttpServletRequest());

    almostEmptyRequest.addParameter("dummyParam", "true");
    
    StandardServletInputData input = new StandardServletInputData(almostEmptyRequest);
    
    testForm._getWidget().update(input);

    testForm.getValueByFullName("select");
    assertTrue(
    		"Test form disabled select element with assigned value must be valid after reading from request.", 
    		testForm.convertAndValidate());
  }
  
  /** Test that form addElement methods accept hierarchical ids creating subforms on demand */
  public void testHierarchicalSubformCreation() throws Exception {
	  FormWidget form = new FormWidget();
	  FormElement element = form.addElement("1.2", "dummyLabel", new CheckboxControl(),  new BooleanData(), false);
	  
	  assertTrue("Created element should be accessible", form.getElementByFullName("1.2").equals(element));
	  assertTrue("Subform should have been created", form.getElement("1") instanceof FormWidget);
	  
	  // flat addElementAfter after nested form
	  FormElement afterElement = form.createElement("dumbLabel", new TextControl(), new StringData(), false);
	  form.addElementAfter("after", afterElement, "1");
	  
	  Iterator iterator = form.getElements().values().iterator();
	  assertTrue("First should be the first nested form", iterator.next() instanceof FormWidget);
	  assertTrue("Second should be the 'afterElement'", iterator.next().equals(afterElement));
	  assertTrue("Should be no more elements in that form", !iterator.hasNext());
	  
	  // addElementAfter with nested afterid is supported
	  FormElement nestedAfterElement = form.createElement("dumbdumbLabel", new TextControl(), new StringData(), false);
	  form.addElementAfter("3", nestedAfterElement, "1.2");
	  
	  iterator = form.getSubFormByFullName("1").getElements().values().iterator();
	  assertTrue("First should be the first 'element'", element.equals(iterator.next()));
	  assertTrue("Second should be the 'nestedAfterElement'", nestedAfterElement.equals(iterator.next()));
	  
	  // addElementAfter with nested id is not supported
	  boolean failure = false;
	  try {
	  	FormElement nextElement = form.createElement("dumbLabel", new NumberControl(), new IntegerData(), false);
	  	form.addElementAfter("x.y", nextElement, "1");
	  } catch (Exception e) {
		failure = true;
      }

	  assertTrue("addElementAfter() with nested added element id is not supported", failure);
  }
  
  /**
   * Tests helper functions that access form parts by their full name.
   */
  public void testFormTraversal() throws Exception {
    FormWidget form = makeUsualForm();

    assertTrue("'myCheckBox' must contain a CheckboxControl!", form.getControlByFullName("myCheckBox") instanceof CheckboxControl);
    assertTrue("'hierarchyTest.myTextarea' must contain a TextareaControl!", form.getControlByFullName("hierarchyTest.myTextarea") instanceof TextareaControl);
    
    Control result = form.getControlByFullName("hierarchyTestm.yTextarea");
    
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
      log.debug("Received onClick event!");
      eventsWork = true;
    }
  }
}
