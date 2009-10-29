package org.araneaframework.tests;

import java.math.BigDecimal;

import junit.framework.TestCase;
import org.araneaframework.http.core.StandardServletInputData;
import org.araneaframework.tests.mock.MockEnvironment;
import org.araneaframework.tests.util.RequestUtil;
import org.araneaframework.uilib.form.Data;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.BaseControl;
import org.araneaframework.uilib.form.control.BigDecimalControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.BigDecimalData;
import org.araneaframework.uilib.form.data.StringData;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * Regression test for changes made in change #153 ("Asynchronous form modifications"). 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class AsyncFormModificationTest extends TestCase {
	protected FormWidget makeForm() throws Exception {
		FormWidget testForm = new FormWidget();
		testForm.addElement("myLongText", "my long text", new TextControl(), new StringData(), true);
		testForm._getComponent().init(null, new MockEnvironment());
		return testForm;
	}
	
	/* Test setting the value of fully initialized FormElement. */
	public void testSetValue_1() throws Exception {
		String value = "newvalue";
		
		FormWidget form = makeForm();
		FormElement<?, String> el = form.getElementByFullName("myLongText");
		
		el.setValue(value);
		
		assertEquals("Element value incorrect: '" + value + "' != '" + el.getValue() + "'", "newvalue", el.getValue());
	}
	
	/* Test whether the createElement() with initialValue set produces 
	 * expected results when FormElement is left uninitiated. */
	public void testSetValue_2() throws Exception {
		String value = "newvalue";
		
		FormWidget form = makeForm();
		FormElement<String, String> el = form.createElement("labelId", new TextControl(), new StringData(), value, false);

		assertEquals("Element value incorrect", value, el.getValue());
	}
	
	/* Test that replacing initiated FormElement's Data changes FormElement's value to Data's value.
	 * After that, make sure that Control sees the same value (meaning that asynchronous form 
	 * modifications work). */
	@SuppressWarnings("unchecked")
  public void testSetValue_3() throws Exception {
		String value = "newvalue";
		Data<String> data = new StringData();
		data.setValue(value);
		
		FormWidget form = makeForm();
		FormElement<?, String> element = form.getElementByFullName("myLongText");

		element.setData(data);
		assertEquals("Element value incorrect", value, element.getValue());
		
		TextControl.ViewModel viewModel = (TextControl.ViewModel) ((BaseControl<String>)(element.getControl())).getViewModel();
		
		assertEquals("Inited formelement's Control value differs from Data value", value, viewModel.getSimpleValue());
	}
	
	/* Test that replacing uninitiated FormElement's Data changes FormElement's value to Data's value.
	 * After that, demonstrate that Control inside uninitiated FormElement is still unaware of its value. */
	public void testSetValue_4() throws Exception {
		String value = "newvalue";
		Data<String> data = new StringData();
		data.setValue(value);
		
		FormWidget form = makeForm();
		FormElement<String, String> element = form.createElement("labelId", new TextControl(), new StringData(), "initial", false);

		element.setData(data);
		assertEquals("Element value incorrect", value, element.getValue());
		
		TextControl.ViewModel viewModel = (TextControl.ViewModel) ((BaseControl<String>)(element.getControl())).getViewModel();

		// As Control's converters are not yet in place, this is expected to be false.
		if (value.equals(viewModel.getSimpleValue())) {
			fail("Unexpected semantic change (might be real useful :D): See test comments for details.");
		}
	}
	
	/* Test that convert() retains the Control contents when request with invalid form field content comes in. */
  @SuppressWarnings("unchecked")
  public void testConvert_1() throws Exception {
		String notNumber = "qwe";
		String someText = "someText";
		
		FormWidget testForm = new FormWidget();
		testForm.addElement("number", "#Number", new BigDecimalControl(), new BigDecimalData(), true);
		testForm.addElement("text", "#Text", new TextControl(), new StringData(), false);
		testForm._getComponent().init(null, new MockEnvironment());
		
		// construct the request
		MockHttpServletRequest validRequest = 
	    	RequestUtil.markSubmitted(new MockHttpServletRequest());

	    validRequest.addParameter("number", notNumber);
	    ((FormElement) testForm.getElement("number")).rendered();

	    validRequest.addParameter("text", someText);
	    ((FormElement) testForm.getElement("text")).rendered();

	    // process the request
	    StandardServletInputData input = new StandardServletInputData(validRequest);
	    testForm._getWidget().update(input);
	    
	    // convert
	    testForm.convert();

	    assertEquals(someText, testForm.getControlByFullName("text").getRawValue());
	    String simpleValue = getSimpleValue(testForm, "number");
	    assertEquals(notNumber, simpleValue);
	    
	    // this is not true (because of legacy code in StringArrayRequestControl.process())
	    assertEquals(someText, testForm.getControlByFullName("text").getRawValue());

        simpleValue = getSimpleValue(testForm, "number");
	    assertEquals(notNumber, simpleValue);
	}

	@SuppressWarnings("unchecked")
  private String getSimpleValue(FormWidget form, String elementName) throws Exception {
      BaseControl<BigDecimal> baseControl = (BaseControl<BigDecimal>) form.getControlByFullName(elementName);
      BigDecimalControl.ViewModel viewModel = (BigDecimalControl.ViewModel) baseControl.getViewModel();
      return viewModel.getSimpleValue();
	}
}