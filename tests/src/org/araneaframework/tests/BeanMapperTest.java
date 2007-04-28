package org.araneaframework.tests;

import java.util.List;
import junit.framework.TestCase;
import org.araneaframework.backend.util.BeanMapper;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class BeanMapperTest extends TestCase {
	public static final Integer IA = new Integer(6);
	public static final Integer IB = new Integer(8);

	public static class A {
		private Integer a = IA;

		public Integer getA() {
			return a;
		}

		public void setA(Integer a) {
			this.a = a;
		}
	}

	public static class B extends A {
		private Integer b = IB;
		private boolean t = true;

		public Integer getB() {
			return b;
		}

		public void setB(Integer b) {
			this.b = b;
		}

		public boolean isT() {
			return t;
		}

		public void setT(boolean t) {
			this.t = t;
		}
	}
	
	public void testClassField() {
		BeanMapper beanMapper = new BeanMapper(A.class);
		A a = new A();
		Object o = beanMapper.getFieldValue(a, "a");

		assertEquals(o, IA);
	}
	
	public void testClassAndSuperClassFields() {
		BeanMapper beanMapper = new BeanMapper(B.class);
		B b = new B();

		assertEquals(beanMapper.getFieldValue(b, "a"), IA);
		assertEquals(beanMapper.getFieldValue(b, "b"), IB);
		assertEquals(beanMapper.getFieldValue(b, "t"), Boolean.TRUE);
	}
	
	public void testGetFields() {
		BeanMapper beanMapper = new BeanMapper(B.class);

		List beanFields = beanMapper.getFields();
		assertTrue(beanFields.contains("a"));
		assertTrue(beanFields.contains("b"));
		assertTrue(beanFields.contains("t"));
	}
	
	public void testGetFieldType() {
		BeanMapper beanMapper = new BeanMapper(B.class);

		assertTrue(Integer.class.equals(beanMapper.getFieldType("a")));
		assertTrue(Integer.class.equals(beanMapper.getFieldType("b")));
		assertTrue(boolean.class.equals(beanMapper.getFieldType("t")));
	}
}
