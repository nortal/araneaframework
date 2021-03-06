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

import org.araneaframework.Environment;
import org.araneaframework.Service;
import org.araneaframework.core.ServiceFactory;
import org.springframework.beans.factory.BeanFactory;

/**
 * A simple factory to be used in the XML configuration files for creating services with factories. By setting a bean id
 * via <code>setBeanId(String)</code> it is possible to construct a bean with factory found in Environment under key
 * <code>beanFactoryClass</code>.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class SpringServiceFactory implements ServiceFactory {

  protected Class<? extends BeanFactory> beanFactoryClass = BeanFactory.class;

  protected String beanId;

  /**
   * Set the class under which BeanFactory resides in the Environment.
   * 
   * @param beanFactoryClass the class under which BeanFactory resides in the Environment.
   */
  public void setBeanFactoryClass(Class<? extends BeanFactory> beanFactoryClass) {
    this.beanFactoryClass = beanFactoryClass;
  }

  /**
   * The id of the bean in the configuration.
   */
  public void setBeanId(String beanId) {
    this.beanId = beanId;
  }

  public Service buildService(Environment env) {
    BeanFactory factory = env.requireEntry(this.beanFactoryClass);
    return (Service) factory.getBean(this.beanId);
  }
}
