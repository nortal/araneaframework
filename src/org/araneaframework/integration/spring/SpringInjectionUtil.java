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

package org.araneaframework.integration.spring;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.araneaframework.Environment;
import org.araneaframework.core.Assert;
import org.araneaframework.core.util.ClassLoaderUtil;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.integration.spring.support.SpringBeanInvocationHandler;
import org.springframework.beans.factory.BeanFactory;

/**
 * Utility to enable dependency injection.
 */
public final class SpringInjectionUtil {

  // This class does not need to be created as object.
  private SpringInjectionUtil() {
  }

  /**
   * Injects dependencies from the <code>Environment</code> to the given
   * <code>object</code>. The method looks for methods of <code>object</code>
   * that have a form of
   * <code>inject&lt;DependecyInterfaceName&gt;(&lt;DependecyInterfaceName&gt;)</code>.
   * It is expected that <code>&lt;DependecyInterfaceName&gt;</code> is a
   * key to find the necessary object from the <code>Environment</code>.
   * <p>
   * There is a way to make all widgets have their dependencies injected before
   * their <code>init()</code> method is called. To enable it, they should
   * have a parent class that invokes this method right before the widget's
   * <code>init()</code> method is called.
   * <p>
   * For example, the parent widget class may have code like this:
   * <pre><code>
   *   public Component.Interface _getComponent() {
   *     return new ComponentImpl();
   *   }
   * 
   *   protected class ComponentImpl extends BaseApplicationWidget.ComponentImpl {
   * 
   *     private static final long serialVersionUID = 1L;
   * 
   *     public synchronized void init(Scope scope, Environment env) {
   *       setGlobalEventListener(new ProxyEventListener(MyBaseWidget.this));
   *       SpringInjectionUtil.injectBeans(env, MyBaseWidget.this);
   *       super.init(scope, env);
   *     }
   *   }
   * </code></pre> 
   * 
   * @param env The <code>Environment</code> that is expected to contain the
   *            dependency objects.
   * @param object The object that is expected to have some methods with
   *            "inject" prefix that should be invoked to set inject
   *            dependencies.
   */
  public static void injectBeans(Environment env, Object object) {

    Method[] methods = object.getClass().getMethods();

    BeanFactory bf = env.getEntry(BeanFactory.class);

    for (int i = 0; i < methods.length; i++) {

      if (!methods[i].getName().startsWith("inject")) {
        continue;
      }

      String beanName = methods[i].getName().substring(6);
      beanName = beanName.substring(0, 1).toLowerCase()
          + beanName.substring(1);

      Assert.isTrue(methods[i].getParameterTypes().length == 1,
          "Injection method '" + methods[i].toString()
              + "' does not have exactly one parameter!");

      Assert.isTrue(methods[i].getParameterTypes()[0].isInterface(),
          "Injection method '" + methods[i].toString()
              + "()' parameter is not an interface!");

      Assert.isTrue(bf.containsBean(beanName), "Injection method '"
          + methods[i].toString() + "' describes a missing bean '" + beanName
          + "'!");

      try {
        Class<?>[] proxyArgs = { methods[i].getParameterTypes()[0] };

        Object[] args = { Proxy.newProxyInstance(
            ClassLoaderUtil.getDefaultClassLoader(), proxyArgs,
            new SpringBeanInvocationHandler(env, beanName)) };

        methods[i].invoke(object, args);
      } catch (Exception e) {
        ExceptionUtil.uncheckException(e);
      }
    }
  }
}