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

package org.araneaframework.ioc.spring;

import org.araneaframework.Service;
import org.araneaframework.core.ServiceFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * A simple factory to be used in the xml configuration files for creating services with
 * factories. By setting a bean factory via <code>setBeanFactory(BeanFactory)</code> and
 * a bean id via <code>setBeanId(String)</code> it is possible to construct a bean, 
 * represented by the bean id the config file, using the bean factory specified.   
 * 
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class SpringServiceFactory implements ServiceFactory, BeanFactoryAware {
  private BeanFactory factory = null;
  private String beanId;
  
  public void setBeanFactory(BeanFactory factory) throws BeansException {
    this.factory = factory;
  }

  /**
   * The id of the bean in the configuration.
   */
  public void setBeanId(String beanId) {
    this.beanId = beanId;
  }
  
  public Service buildService() {
    return (Service)factory.getBean(beanId);
  }
}
