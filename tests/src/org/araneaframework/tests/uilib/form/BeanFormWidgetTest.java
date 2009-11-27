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
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.control.NumberControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.StringData;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class BeanFormWidgetTest extends TestCase {

  public static class FlatBean {

    protected int a;

    protected String s;

    public FlatBean() {}

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

  }

  public static class HierarhicalBean extends FlatBean {

    private FlatBean subFlatBean;

    public HierarhicalBean() {}

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

  }

  protected FlatBean makeFlatBean(int integer, String string) {
    return new FlatBean(integer, string);
  }

  protected HierarhicalBean makeHierarchicalBean(int integer, String string, int subInteger, String subString) {
    return new HierarhicalBean(integer, string, makeFlatBean(subInteger, subString));
  }

  protected BeanFormWidget<FlatBean> makeFlatBeanForm(FlatBean bean) throws Exception {
    BeanFormWidget<FlatBean> result = new BeanFormWidget<FlatBean>(FlatBean.class, bean);
    result.addBeanElement("a", "#dummyLabel1", new NumberControl(), true);
    result.addBeanElement("s", "#dummyLabel2", new TextControl());
    result.addElement("b", "b", new TextControl(), new StringData());
    result.addElement("button", "button", new ButtonControl());
    return result;
  }

  protected BeanFormWidget<HierarhicalBean> makeHierarchicalBeanForm(HierarhicalBean bean) throws Exception {
    BeanFormWidget<HierarhicalBean> result = new BeanFormWidget<HierarhicalBean>(HierarhicalBean.class, bean);
    result.addBeanElement("a", "#dummyLabelX", new NumberControl(), true);
    result.addBeanElement("s", "#dummyLabelY", new TextControl(), false);
    result.addBeanElement("subFlatBean.a", "#dummyLabel1", new NumberControl(), true);
    return result;
  }

  public void testFlatBeanWrite() throws Exception {
    BeanFormWidget<FlatBean> form = makeFlatBeanForm(makeFlatBean(100, "newString"));
    form._getComponent().init(new StandardScope(null, null), new MockEnvironment());

    FlatBean bean = form.writeToBean();

    assertEquals(100, bean.a);
    assertEquals("newString", bean.s);
  }

  public void testFlatBeanRead() throws Exception {
    FlatBean bean = makeFlatBean(234, "aaac");
    BeanFormWidget<FlatBean> form = makeFlatBeanForm(bean);
    form._getComponent().init(new StandardScope(null, null), new MockEnvironment());
    assertEquals(new Integer(234), form.getValueByFullName("a"));
    assertEquals(new String("aaac"), form.getValueByFullName("s"));

    form.readFromBean(makeFlatBean(2, "xyz"));

    assertEquals(2, form.getValueByFullName("a"));
    assertEquals(new String("xyz"), form.getValueByFullName("s"));
  }

  // tests that bean fields that are not tied to BeanFormWidget elements
  // are not modified when writing hierarchical form into hierarchical bean
  public void testHierarchicalBeanWrite() throws Exception {
    BeanFormWidget<HierarhicalBean> form = makeHierarchicalBeanForm(new HierarhicalBean(100, "newString", new FlatBean(
        200, "value")));
    form._getComponent().init(new StandardScope(null, null), new MockEnvironment());

    HierarhicalBean bean = new HierarhicalBean();
    bean.setSubFlatBean(new FlatBean(100, "value"));

    bean = form.writeToBean();

    assertEquals(new Integer(100), new Integer(bean.a));
    assertEquals(new String("newString"), bean.s);
    assertEquals(new Integer(200), new Integer(bean.subFlatBean.a));
    assertEquals(new String("value"), bean.subFlatBean.s);
  }
}
