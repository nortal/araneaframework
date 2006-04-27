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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.servlet.ServletException;
import org.araneaframework.servlet.ServletServiceAdapterComponent;
import org.araneaframework.servlet.core.BaseAraneaDispatcherServlet;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SimpleAraneaSpringDispatcherServlet extends BaseAraneaDispatcherServlet {
  public static final String ARANEA_ROOT = "araneaApplicationRoot";
  
  protected WebApplicationContext webAppCtx;
  
  public void init() throws ServletException {        
    WebApplicationContext beanFactory = WebApplicationContextUtils.getWebApplicationContext(getServletContext());           
       
    XmlBeanFactory rootConf = new XmlBeanFactory(new ClassPathResource("conf/default-aranea-conf.xml"), beanFactory);

    PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
    cfg.setLocation(new ClassPathResource("conf/default-aranea-conf.properties"));
    
    Properties localConf = new Properties();
    try {
      if (getServletContext().getResource("/WEB-INF/aranea-conf.properties") != null)
        localConf.load(getServletContext().getResourceAsStream( "/WEB-INF/aranea-conf.properties"));
    }
    catch (IOException e) {
      throw new ServletException(e);
    }
    cfg.setProperties(localConf);
    cfg.setLocalOverride(true);
    
    cfg.postProcessBeanFactory(rootConf);
    
    XmlBeanDefinitionReader localConfReader = new XmlBeanDefinitionReader(rootConf);
    localConfReader.loadBeanDefinitions(new ServletContextResource(getServletContext(), "/WEB-INF/aranea-conf.xml"));
    
    webAppCtx = new GenericWebApplicationContext(rootConf);    
    
    super.init();        
  }  
  
  protected ServletServiceAdapterComponent buildRootComponent() {
    ServletServiceAdapterComponent adapter = 
      (ServletServiceAdapterComponent) webAppCtx.getBean(ARANEA_ROOT);
    return adapter;
  }
  
  protected Map getEnvironmentEntries() { 
    Map result = new HashMap();
    result.put(BeanFactory.class, webAppCtx);   
    result.put(ApplicationContext.class, webAppCtx);
    result.put(WebApplicationContext.class, webAppCtx);
    return result;
  }
}
