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
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class BeanMapperTest extends TestCase {

  public static final Integer IA = 6;

  public static final Integer IB = 8;

  public static class A {

    private Integer a = IA;

    public Integer getA() {
      return this.a;
    }

    public void setA(Integer a) {
      this.a = a;
    }
  }

  public static class B extends A {

    private Integer b = IB;

    private boolean t = true;

    public Integer getB() {
      return this.b;
    }

    public void setB(Integer b) {
      this.b = b;
    }

    public boolean isT() {
      return this.t;
    }

    public void setT(boolean t) {
      this.t = t;
    }
  }

  public void testClassField() {
    BeanMapper<A> beanMapper = new BeanMapper<A>(A.class);
    A a = new A();
    Object o = beanMapper.getProperty(a, "a");

    assertEquals(o, IA);
  }

  public void testClassAndSuperClassFields() {
    BeanMapper<B> beanMapper = new BeanMapper<B>(B.class);
    B b = new B();

    assertEquals(beanMapper.getProperty(b, "a"), IA);
    assertEquals(beanMapper.getProperty(b, "b"), IB);
    assertEquals(beanMapper.getProperty(b, "t"), Boolean.TRUE);
  }

  public void testGetFields() {
    BeanMapper<B> beanMapper = new BeanMapper<B>(B.class);

    List<String> beanFields = beanMapper.getPropertyNames();
    assertTrue(beanFields.contains("a"));
    assertTrue(beanFields.contains("b"));
    assertTrue(beanFields.contains("t"));
  }

  public void testGetFieldType() {
    BeanMapper<B> beanMapper = new BeanMapper<B>(B.class);

    assertTrue(Integer.class.equals(beanMapper.getPropertyType("a")));
    assertTrue(Integer.class.equals(beanMapper.getPropertyType("b")));
    assertTrue(boolean.class.equals(beanMapper.getPropertyType("t")));
  }
}
