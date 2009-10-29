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

package org.araneaframework.integration.spring;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import javax.annotation.Resource;
import org.araneaframework.Environment;
import org.araneaframework.core.Assert;
import org.araneaframework.core.util.ClassLoaderUtil;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.integration.spring.support.SpringBeanInvocationHandler;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Utility to enable dependency injection.
 */
public final class SpringInjectionUtil {

  // This class does not need to be created as object.
  private SpringInjectionUtil() {}

  /**
   * Injects dependencies from the {@link BeanFactory} to the given <code>object</code>. {@link BeanFactory} is taken
   * from the provided {@link Environment}.
   * <p>
   * The method looks for the following members of the <code>object</code>:
   * <ul>
   * <li>methods that have a form of <code>inject&lt;DependencyInterfaceName&gt;(&lt;DependencyInterfaceName&gt;)</code>.
   * <li>methods that have a form of
   * <code>@Resource set&lt;DependencyInterfaceName&gt;(&lt;DependencyInterfaceName&gt;)</code>
   * <li>fields that annotated with {@link Resource} annotation, have name corresponding to the Spring bean being
   * injected and of type of one of the interfaces that bean implements.
   * </ul>
   * <p>
   * There is a way to make all widgets have their dependencies injected before their <code>init()</code> method is
   * called. To enable it, they should have a parent class that invokes this method right before the widget's
   * <code>init()</code> method is called.
   * <p>
   * For example, the parent widget class may have code like this:
   * 
   * <pre>
   * <code>
   *   public Component.Interface _getComponent() {
   *     return new ComponentImpl();
   *   }
   * 
   *   protected class ComponentImpl extends BaseApplicationWidget.ComponentImpl {
   * 
   *     public synchronized void init(Scope scope, Environment env) {
   *       setGlobalEventListener(new ProxyEventListener(MyBaseWidget.this));
   *       SpringInjectionUtil.injectBeans(env, MyBaseWidget.this);
   *       super.init(scope, env);
   *     }
   *   }
   * </code>
   * </pre>
   * 
   * @param env The <code>Environment</code> that is expected to contain the Spring's BeanFactory.
   * @param object The object that is expected to have some methods or fields eligible for dependency injection.
   */
  public static void injectBeans(Environment env, Object object) {
    BeanFactory bf = env.getEntry(BeanFactory.class);
    injectMethods(env, object, bf);
    injectFields(env, object, bf);
  }

  private static void injectFields(Environment env, Object object, BeanFactory bf) {
    Class<? extends Object> objectClass = object.getClass();
    do {
      injectFieldsForClass(env, object, bf, objectClass);
      objectClass = objectClass.getSuperclass();
    } while (objectClass != null);
  }

  private static void injectFieldsForClass(Environment env, Object object, BeanFactory factory,
      Class<? extends Object> objectClass) {
    for (Field field : objectClass.getDeclaredFields()) {
      if (field.isAnnotationPresent(Resource.class) || field.isAnnotationPresent(Autowired.class)) {
        String beanName = field.getName();

        if (field.isAnnotationPresent(Qualifier.class)) {
          beanName = field.getAnnotation(Qualifier.class).value();
        }

        Object proxy = createProxy(beanName, factory, field.getType(), env);

        try {
          field.setAccessible(true);
          field.set(object, proxy);
        } catch (Exception e) {
          ExceptionUtil.uncheckException(e);
        }
      }
    }
  }

  private static void injectMethods(Environment env, Object object, BeanFactory bf) {
    for (Method method : object.getClass().getMethods()) {

      if (!method.getName().startsWith("inject") && !method.isAnnotationPresent(Resource.class)) {
        continue;
      }

      Assert.isTrue(method.getParameterTypes().length == 1, "Injection method '" + method.toString()
          + "' does not have exactly one parameter!");

      Class<?> interfaceInjected = method.getParameterTypes()[0];

      String beanName;
      if (method.getName().startsWith("inject")) {
        beanName = method.getName().substring(6);
      } else {
        beanName = method.getName().substring(3);
      }
      beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);

      Object proxy = createProxy(beanName, bf, interfaceInjected, env);
      try {
        method.invoke(object, proxy);
      } catch (Exception e) {
        ExceptionUtil.uncheckException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static Object createProxy(String beanName, BeanFactory bf, Class interfaceInjected, Environment env) {
    Assert.isTrue(interfaceInjected.isInterface(), "Resouce being injected is not an interface!");

    Assert.isTrue(bf.containsBean(beanName), "Resouce being injected describes a missing bean '" + beanName + "'!");
    Class[] proxyArgs = { interfaceInjected };

    return Proxy.newProxyInstance(ClassLoaderUtil.getDefaultClassLoader(), proxyArgs, new SpringBeanInvocationHandler(
        env, beanName));

  }
}
