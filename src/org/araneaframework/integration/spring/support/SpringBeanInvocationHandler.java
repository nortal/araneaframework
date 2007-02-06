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

package org.araneaframework.integration.spring.support;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.commons.lang.exception.ExceptionUtils;
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
    
    try {
      return method.invoke(bean, args);
    } catch (InvocationTargetException ex){
      throw ExceptionUtils.getCause(ex);
    }
  }
}