
package org.araneaframework.tests.integration;

import java.io.Serializable;
import javax.annotation.Resource;
import junit.framework.TestCase;
import org.araneaframework.integration.spring.SpringInjectionUtil;
import org.araneaframework.tests.mock.MockEnvironment;
import org.springframework.beans.factory.BeanFactory;

public class SpringInjectionUtilTest extends TestCase {

  public void testInjectBeans() {
    B object = new B();
    SpringInjectionUtil.injectBeans(new Env(), object);
    assertNotNull(object.s2);
    assertNotNull(object.s3);
    assertNull(object.s4);
    assertNotNull(object.s5);
    assertNotNull(object.s1);
  }

  public class A {

    @Resource
    Serializable s5;

    Serializable s1;

    public void injectSerializable(Serializable s1) {
      this.s1 = s1;
    }
  }

  public class B extends A {

    @Resource
    private Serializable s2;

    private Serializable s3;

    private Serializable s4;

    @Resource
    public void setS3(Serializable s3) {
      this.s3 = s3;
    }

    public void setS4(Serializable s4) {
      this.s4 = s4;
    }

  }

  private class Env extends MockEnvironment {

    Env() {
      super();
      MockBeanFactory bf = new MockBeanFactory();
      bf.setBean(Integer.valueOf(1));
      this.contexts.put(BeanFactory.class, bf);
    }
  }

  private class MockBeanFactory extends org.araneaframework.tests.framework.bean.MockBeanFactory {

    @Override
    public boolean containsBean(String arg0) {
      return true;
    }
  }
}
