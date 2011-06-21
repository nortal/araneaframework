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

import java.util.List;
import junit.framework.TestCase;
import org.araneaframework.backend.util.BeanMapper;

/**
 * @author Nikita Salnikov-Tarnovski (<a href="mailto:nikem@webmedia.ee">nikem@webmedia.ee</a>)
 */
public class BeanMapperFieldTest extends TestCase {

  public static final Integer IA = 6;

  public static final Integer IB = 8;

  public static class A {

    @SuppressWarnings("unused")
    private Integer a = IA;
  }

  public static class B extends A {

    @SuppressWarnings("unused")
    private Integer b = IB;

    @SuppressWarnings("unused")
    private boolean t = true;
  }

  public void testClassField() {
    BeanMapper<A> beanMapper = new BeanMapper<A>(A.class);
    A a = new A();
    Object o = beanMapper.getProperty(a, "a");

    assertEquals(IA, o);
  }

  public void testClassAndSuperClassFields() {
    BeanMapper<B> beanMapper = new BeanMapper<B>(B.class);
    B b = new B();

    assertEquals(null, beanMapper.getProperty(b, "a"));
    assertEquals(IB, beanMapper.getProperty(b, "b"));
    assertEquals(Boolean.TRUE, beanMapper.getProperty(b, "t"));
  }

  public void testGetFields() {
    BeanMapper<B> beanMapper = new BeanMapper<B>(B.class);

    List<String> beanFields = beanMapper.getPropertyNames();
    assertFalse(beanFields.contains("a"));
    assertTrue(beanFields.contains("b"));
    assertTrue(beanFields.contains("t"));
  }

  public void testGetFieldType() {
    BeanMapper<B> beanMapper = new BeanMapper<B>(B.class);

    assertTrue(Integer.class.equals(beanMapper.getPropertyType("b")));
    assertTrue(boolean.class.equals(beanMapper.getPropertyType("t")));
  }
}
