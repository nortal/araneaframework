package org.araneaframework.tests.uilib.form;

import junit.framework.TestCase;
import org.araneaframework.core.StandardScope;
import org.araneaframework.tests.mock.MockEnvironment;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.control.NumberControl;
import org.araneaframework.uilib.form.control.TextControl;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class BeanFormWidgetTest extends TestCase {
	protected BeanFormWidget form;
	
	public static class FlatBean {
		private int a;
		private String s;

		public int getA() {
			return a;
		}
		public void setA(int a) {
			this.a = a;
		}

		public String getS() {
			return s;
		}
		public void setS(String s) {
			this.s = s;
		}		
	}
	
	public static class HierarhicalBean extends FlatBean {
		private FlatBean subFlatBean;

		public FlatBean getSubFlatBean() {
			return subFlatBean;
		}

		public void setSubFlatBean(FlatBean subFlatBean) {
			this.subFlatBean = subFlatBean;
		}
	}
	
	protected FlatBean makeFlatBean(int integer, String string) {
		FlatBean bean = new FlatBean();
		bean.setA(integer);
		bean.setS(string);
		return bean;
	}
	
	protected HierarhicalBean makeHierarchicalBean(int integer, String string, int subInteger, String subString) {
		HierarhicalBean bean = new HierarhicalBean();
		bean.setA(integer);
		bean.setS(string);

		FlatBean flatBean = makeFlatBean(subInteger, subString);
		bean.setSubFlatBean(flatBean);

		return bean;
	}
	
	protected BeanFormWidget makeFlatBeanForm() throws Exception {
		BeanFormWidget result = new BeanFormWidget(FlatBean.class);
		result.addBeanElement("a", "#dummyLabel1", new NumberControl(), true);
		result.addBeanElement("s", "#dummyLabel2", new TextControl(), false);
		return result;
	}
	
	protected BeanFormWidget makeHierarchicalBeanForm() throws Exception {
		BeanFormWidget result = new BeanFormWidget(HierarhicalBean.class);
		result.addBeanElement("a", "#dummyLabelX", new NumberControl(), true);
		result.addBeanElement("s", "#dummyLabelY", new TextControl(), false);
		result.addElement("subFlatBean", new BeanFormWidget(FlatBean.class));
		((BeanFormWidget)result.getSubFormByFullName("subFlatBean")).addBeanElement("a", "#dummyLabel1", new NumberControl(), true);

		return result;
	}
	
	public void testFlatBeanWrite() throws Exception {
		form = makeFlatBeanForm();
		form._getComponent().init(new StandardScope(null, null), new MockEnvironment());
		
		form.setValueByFullName("a", new Integer(100));
		form.setValueByFullName("s", "newString");

		FlatBean bean = (FlatBean) form.writeToBean(new FlatBean());
		
		assertEquals(new Integer(100), new Integer(bean.getA()));
		assertEquals(new String("newString"), bean.getS());
	}
	
	public void testFlatBeanRead() throws Exception {
		form = makeFlatBeanForm();
		form._getComponent().init(new StandardScope(null, null), new MockEnvironment());

		FlatBean bean = makeFlatBean(234, "aaac");
		form.readFromBean(bean);
		
		assertEquals(new Integer(234), form.getValueByFullName("a"));
		assertEquals(new String("aaac"), form.getValueByFullName("s"));
	}

	// tests that bean fields that are not tied to BeanFormWidget elements
	// are not modified when writing hierarchical form into hierarchical bean
	public void testHierarchicalBeanWrite() throws Exception {
		form = makeHierarchicalBeanForm();
		form._getComponent().init(new StandardScope(null, null), new MockEnvironment());
		
		form.setValueByFullName("a", new Integer(100));
		form.setValueByFullName("s", "newString");
		form.setValueByFullName("subFlatBean.a", new Integer(200));
		
		HierarhicalBean bean = new HierarhicalBean();
		bean.setSubFlatBean(new FlatBean());
		bean.getSubFlatBean().setS("value");
		
		bean = (HierarhicalBean) form.writeToBean(bean);
		
		assertEquals(new Integer(100), new Integer(bean.getA()));
		assertEquals(new String("newString"), bean.getS());
		assertEquals(new Integer(200), new Integer(bean.getSubFlatBean().getA()));
		assertEquals(new String("value"), bean.getSubFlatBean().getS());
	}
}
