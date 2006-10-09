package org.araneaframework.integration.spring;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.araneaframework.Environment;
import org.springframework.beans.factory.BeanFactory;

public class SpringBeanInvocationHandler implements InvocationHandler, Serializable {
  private Environment env;
  private String id;
  
  public SpringBeanInvocationHandler(Environment env, String id) {
    this.env = env;
    this.id = id;
  }
  
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    BeanFactory bf = 
      (BeanFactory) env.getEntry(BeanFactory.class);
    
    Object bean = bf.getBean(id);
        
    return method.invoke(bean, args);
  }
}