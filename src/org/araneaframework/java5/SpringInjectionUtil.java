package org.araneaframework.java5;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.araneaframework.Environment;
import org.araneaframework.core.util.ExceptionUtil;


public class SpringInjectionUtil {
  public static void injectBeans(Environment env, Object object) {
    for (Method m : object.getClass().getDeclaredMethods()) {
      if (m.isAnnotationPresent(Inject.class)) {
        Inject inject = m.getAnnotation(Inject.class);
        try {
          m.invoke(object, new Object[] {
              Proxy.newProxyInstance(
                  SpringInjectionUtil.class.getClassLoader(), 
                  new Class[] {m.getParameterTypes()[0]}, 
                  new SpringBeanInvocationHandler(env, inject.id()))              
          });
        }
        catch (Exception e) {
          ExceptionUtil.uncheckException(e);
        }
      }
    }
  }
}
