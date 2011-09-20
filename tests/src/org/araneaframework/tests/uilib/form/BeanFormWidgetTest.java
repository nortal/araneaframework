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

package org.araneaframework.tests.uilib.form;

import junit.framework.TestCase;
import org.araneaframework.core.StandardScope;
import org.araneaframework.tests.mock.MockEnvironment;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.control.NumberControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.StringData;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class BeanFormWidgetTest extends TestCase {

  public static class FlatBean implements Cloneable {

    protected int a;

    protected String s;

    public FlatBean() {
    }

    public FlatBean(int a, String s) {
      this.a = a;
      this.s = s;
    }

    public int getA() {
      return this.a;
    }

    public String getS() {
      return this.s;
    }

    public void setA(int a) {
      this.a = a;
    }

    public void setS(String s) {
      this.s = s;
    }

    @Override
    protected FlatBean clone() {
      return new FlatBean(this.a, this.s);
    }
  }

  public static class HierarhicalBean extends FlatBean {

    private FlatBean subFlatBean;

    public HierarhicalBean() {
      super(0, null);
    }

    public HierarhicalBean(int a, String s, FlatBean subFlatBean) {
      super(a, s);
      this.subFlatBean = subFlatBean;
    }

    public FlatBean getSubFlatBean() {
      return this.subFlatBean;
    }

    public void setSubFlatBean(FlatBean subFlatBean) {
      this.subFlatBean = subFlatBean;
    }

    @Override
    protected HierarhicalBean clone() {
      FlatBean subBean = this.subFlatBean == null ? null : this.subFlatBean.clone();
      return new HierarhicalBean(this.a, this.s, subBean);
    }
  }

  private static BeanFormWidget<HierarhicalBean> makeBeanForm(HierarhicalBean bean) throws Exception {
    BeanFormWidget<HierarhicalBean> result = null;

    if (bean != null) {
      result = new BeanFormWidget<HierarhicalBean>(HierarhicalBean.class, bean);
    } else {
      result = new BeanFormWidget<HierarhicalBean>(HierarhicalBean.class);
    }

    result.addBeanElement("a", "#dummyLabelX", new NumberControl(), true);
    result.addBeanElement("s", "#dummyLabelY", new TextControl());
    result.addBeanElement("subFlatBean.a", "#dummyLabel1", new NumberControl(), true);
    result.addBeanElement("subFlatBean.s", "#dummyLabel2", new TextControl());
    result.addElement("b", "b", new TextControl(), new StringData());
    result.addElement("button", "button", new ButtonControl());
    return result;
  }

  private static void assertFormValuesEqualTo(BeanFormWidget<HierarhicalBean> form, HierarhicalBean expected) {
    assertNotNull("The expected bean must not be null.", expected);
    assertNotNull("The form to test must not be null.", form);
    assertEquals(expected.a, form.getValueByFullName("a"));
    assertEquals(expected.s, form.getValueByFullName("s"));

    if (expected.subFlatBean != null) {
      assertEquals(expected.subFlatBean.a, form.getValueByFullName("subFlatBean.a"));
      assertEquals(expected.subFlatBean.s, form.getValueByFullName("subFlatBean.s"));
    }

    // Read form data into separate bean and test values one-by-one:

    HierarhicalBean testBean = new HierarhicalBean(0, "", null);
    assertSame("BeanFormWidget.writeToBean() must return the same object as provided", testBean,
        form.writeToBean(testBean));

    assertEquals(expected.a, testBean.a);
    assertEquals(expected.s, testBean.s);

    if (expected.subFlatBean != null) {
      assertEquals(expected.subFlatBean.a, testBean.subFlatBean.a);
      assertEquals(expected.subFlatBean.s, testBean.subFlatBean.s);
    }
  }

  private static void assertFormElement(FormWidget form, String id, String label, Class<?> control, Class<?> data,
      Object value, boolean mandatory) {
    FormElement<?, ?> element = (FormElement<?, ?>) form.getElement(id);
    assertNotNull("The form should contain element '" + id + "'.", element);
    assertEquals("The form element label does not match.", label, element.getLabel());
    assertNotNull("The form element control must not be null.", element.getControl());
    assertEquals("The form element control does not match.", control, element.getControl().getClass());

    if (data == null) {
      assertNull("The form element data must be null.", element.getData());
    } else {
      assertNotNull("The form element data must not be null.", element.getData());
      assertFalse("The form element data type must not be list.", element.getData().getValueType().isList());
      assertEquals("The form element data does not match.", data, element.getData().getValueType().getType());
    }

    if (value == null) {
      assertNull("The form element value must be null.", element.getValue());
    } else {
      assertEquals("The form element value does not match.", value, element.getValue());
    }

    assertEquals("The form element mandatory property does not match.", mandatory, element.isMandatory());
  }

  private static void testForm(HierarhicalBean testBean, HierarhicalBean readBean, boolean emptyConstruct)
      throws Exception {
    HierarhicalBean valuesBean = testBean.clone();

    // 1. Test with bean as a constructor parameter.
    BeanFormWidget<HierarhicalBean> form = makeBeanForm(emptyConstruct ? null : testBean);

    if (emptyConstruct) {
      form.readFromBean(testBean);
      testBean = form.writeToBean();
    }

    form.readFromBean(readBean);
    assertNotSame("BeanFormWidget.getBean() must return the original bean.", readBean, form.getBean());
    assertFormValuesEqualTo(form, readBean); // Form data must be equal to the *read* bean.

    form._getComponent().init(new StandardScope(null, null), new MockEnvironment());

    assertFormValuesEqualTo(form, readBean); // Form data must be equal to the *read* bean.

    if (emptyConstruct) {
      form.readFromBean(valuesBean);
    } else {
      form.readFromBean();
    }
    assertFormValuesEqualTo(form, testBean); // Form data must be equal to the *original* bean.

    assertEquals(valuesBean.a, testBean.a);
    assertEquals(valuesBean.s, testBean.s);

    assertSame("BeanFormWidget.getBean() must return the original bean.", testBean, form.getBean());
    assertSame("BeanFormWidget.writeToBean() must return the original bean.", testBean, form.writeToBean());
    assertFormValuesEqualTo(form, valuesBean);

    // Changing form values using another bean:
    form.readFromBean(readBean);
    assertSame("BeanFormWidget.getBean() must return the original bean.", testBean, form.getBean());
    assertFormValuesEqualTo(form, readBean);

    // Reverting form values using original bean:
    form.readFromBean();
    assertSame("BeanFormWidget.getBean() must return the original bean.", testBean, form.getBean());
    assertFormValuesEqualTo(form, valuesBean);
  }

  public void testBeanReadWrite() throws Exception {
    FlatBean subBean1 = new FlatBean(123, "Hello");
    FlatBean subBean2 = new FlatBean(987, "World");
    HierarhicalBean valuesBean = new HierarhicalBean(100, "newString", subBean1); // For testing bean properties and
                                                                                  // their values.
    HierarhicalBean readBean = new HierarhicalBean(678, "newStringValue", subBean2); // For testing form data changes.

    // 1. Test with bean as a constructor parameter.
    testForm(valuesBean, readBean, false);

    // =================================================

    // 2. Test with bean NOT as a constructor parameter.
    subBean1 = new FlatBean(123, "Hello");
    subBean2 = new FlatBean(987, "World");
    valuesBean = new HierarhicalBean(100, "newString", subBean1); // For testing bean properties and their values.
    readBean = new HierarhicalBean(678, "newStringValue", subBean2); // For testing form data changes.

    testForm(valuesBean, readBean, true);
  }

  public void testBeanFormElements() throws Exception {
    // Beans for testing their properties and their values.
    FlatBean subBean1 = new FlatBean(123, "Hello");
    HierarhicalBean valuesBean = new HierarhicalBean(100, "newString", subBean1);

    BeanFormWidget<HierarhicalBean> form = makeBeanForm(valuesBean);
    form.readFromBean();

    assertEquals("The form should have 5 elements", 5, form.getElements().size());
    assertFormElement(form, "a", "#dummyLabelX", NumberControl.class, int.class, 100, true);
    assertFormElement(form, "s", "#dummyLabelY", TextControl.class, String.class, "newString", false);
    assertFormElement(form, "subFlatBean.a", "#dummyLabel1", NumberControl.class, int.class, 123, true);
    assertFormElement(form, "subFlatBean.s", "#dummyLabel2", TextControl.class, String.class, "Hello", false);
    assertFormElement(form, "b", "b", TextControl.class, String.class, null, false);
    assertFormElement(form, "button", "button", ButtonControl.class, null, null, false);
  }
}
