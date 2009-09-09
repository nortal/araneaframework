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

package org.araneaframework.tests.framework.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class MockBeanFactory implements BeanFactory {
  private Object bean = null;
  private String beanId = null;
  
  public void setBean(Object bean) {
    this.bean = bean;
  }

  public Object getBean(String arg0) throws BeansException {
    this.beanId = arg0;
    return bean;
  }

  @SuppressWarnings("unchecked")
  public Object getBean(String arg0, Class arg1) throws BeansException {
    return null;
  }

  public boolean containsBean(String arg0) {
    return false;
  }

  public boolean isSingleton(String arg0) throws NoSuchBeanDefinitionException {
    return false;
  }

  @SuppressWarnings("unchecked")
  public Class getType(String arg0) throws NoSuchBeanDefinitionException {
    return null;
  }

  public String[] getAliases(String arg0) throws NoSuchBeanDefinitionException {
    return null;
  }

  public String getBeanId() {
    return beanId;
  }

  public Object getBean(String arg0, Object[] arg1) throws BeansException {
    return null;
  }

  public boolean isPrototype(String arg0) throws NoSuchBeanDefinitionException {
    return false;
  }

  @SuppressWarnings("unchecked")
  public boolean isTypeMatch(String arg0, Class arg1) throws NoSuchBeanDefinitionException {
    return false;
  }
}
