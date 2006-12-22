package org.araneaframework.integration.spring;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.araneaframework.Environment;
import org.araneaframework.core.Assert;
import org.araneaframework.core.util.ClassLoaderUtil;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.integration.spring.support.SpringBeanInvocationHandler;
import org.springframework.beans.factory.BeanFactory;


public class SpringInjectionUtil {
  public static void injectBeans(Environment env, Object object) {
    Method[] methods = object.getClass().getMethods();
    for (int i = 0; i < methods.length; i++) {         
      if (methods[i].getName().startsWith("inject")) {
        String beanName = methods[i].getName().substring(6);
        beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1); 
        
        Assert.isTrue(
            methods[i].getParameterTypes().length == 1, 
            "Injection method '" + methods[i].toString() + "' has more than one parameter!");        
        Assert.isTrue(
            methods[i].getParameterTypes()[0].isInterface(), 
            "Injection method '" + methods[i].toString() + "()' parameter is not an interface!");
        
        BeanFactory bf = 
          (BeanFactory) env.getEntry(BeanFactory.class);
        
        Assert.isTrue(
            bf.containsBean(beanName), 
            "Injection method '" + methods[i].toString() + "' describes missing bean '" + beanName +"'!");                                    
        
        try {
          methods[i].invoke(object, new Object[] {
              Proxy.newProxyInstance(
                  ClassLoaderUtil.getDefaultClassLoader(), 
                  new Class[] {methods[i].getParameterTypes()[0]}, 
                  new SpringBeanInvocationHandler(env, beanName))              
          });
        }
        catch (Exception e) {
          ExceptionUtil.uncheckException(e);
        }
      }
    }
  }
}