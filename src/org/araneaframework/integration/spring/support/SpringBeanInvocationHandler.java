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

package org.araneaframework.integration.spring.support;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.araneaframework.Environment;
import org.springframework.beans.factory.BeanFactory;

public class SpringBeanInvocationHandler implements InvocationHandler, Serializable {

  private Environment env;

  private String id;

  public SpringBeanInvocationHandler(Environment env, String id) {
    this.env = env;
    this.id = id;
    env.requireEntry(BeanFactory.class);
  }

  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    BeanFactory bf = this.env.getEntry(BeanFactory.class);

    // XXX: This is a bit evil, but otherwise Serialization debug fails
    // The check is done in the constructor to ensure that generally
    // BeanFactory is available
    if (bf == null)
      return null;

    Object bean = bf.getBean(this.id);

    try {
      return method.invoke(bean, args);
    } catch (InvocationTargetException ex) {
      throw ex.getTargetException();
    }
  }
}
