package org.araneaframework.tests;

import java.util.List;
import junit.framework.TestCase;
import org.araneaframework.backend.util.BeanMapper;

/**
 * @author Nikita Salnikov-Tarnovski (<a href="mailto:nikem@webmedia.ee">nikem@webmedia.ee</a>)
 */
public class BeanMapperFieldTest extends TestCase {
	public static final Integer IA = new Integer(6);
	public static final Integer IB = new Integer(8);

	public static class A {
		private Integer a = IA;
	}

	public static class B extends A {
		private Integer b = IB;
		private boolean t = true;
	}
	
	public void testClassField() {
		BeanMapper beanMapper = new BeanMapper(A.class);
		A a = new A();
		Object o = beanMapper.getFieldValue(a, "a");

		assertEquals(IA, o);
	}
	
	public void testClassAndSuperClassFields() {
		BeanMapper beanMapper = new BeanMapper(B.class);
		B b = new B();

		assertEquals(null, beanMapper.getFieldValue(b, "a"));
		assertEquals(IB, beanMapper.getFieldValue(b, "b"));
		assertEquals(Boolean.TRUE, beanMapper.getFieldValue(b, "t"));
	}
	
	public void testGetFields() {
		BeanMapper beanMapper = new BeanMapper(B.class);

		List beanFields = beanMapper.getFields();
		assertFalse(beanFields.contains("a"));
		assertTrue(beanFields.contains("b"));
		assertTrue(beanFields.contains("t"));
	}
	
	public void testGetFieldType() {
		BeanMapper beanMapper = new BeanMapper(B.class);

		assertTrue(Integer.class.equals(beanMapper.getFieldType("b")));
		assertTrue(boolean.class.equals(beanMapper.getFieldType("t")));
	}
}
