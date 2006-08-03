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

package org.araneaframework.mock.servlet;

import org.araneaframework.http.ServletServiceAdapterComponent;
import org.araneaframework.http.core.BaseAraneaDispatcherServlet;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class MockServlet extends BaseAraneaDispatcherServlet {
  private String beansFile = "smokeTest.xml";
  private ServletServiceAdapterComponent builtComponent;
  private BeanFactory factory;
  
  protected ServletServiceAdapterComponent buildRootComponent() {
    ClassPathResource resource = new ClassPathResource(beansFile);
    factory = new XmlBeanFactory(resource);  
    ServletServiceAdapterComponent adapter = 
      (ServletServiceAdapterComponent)factory.getBean("servletServiceAdapterComponent");
    builtComponent = adapter;
    return adapter;
  }
  
  public BeanFactory getFactory() {
    return factory;
  }

  public void setConfFile(String file) {
    this.beansFile = file;
  }

  public ServletServiceAdapterComponent getBuiltComponent() {
    return builtComponent;
  } 
}
