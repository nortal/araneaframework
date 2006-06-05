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
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.servlet.ServletException;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.core.util.ResourceResolverUtil;
import org.araneaframework.servlet.ServletServiceAdapterComponent;
import org.araneaframework.servlet.core.BaseAraneaDispatcherServlet;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class AraneaSpringDispatcherServlet extends BaseAraneaDispatcherServlet {   
  public static final String ARANEA_START = "araneaApplicationStart";  
  
  public static final String ARANEA_DEFAULT_CONF_XML = "conf/default-aranea-conf.xml";
  public static final String ARANEA_DEFAULT_CONF_PROPERTIES = "conf/default-aranea-conf.properties";
  
  public static final String DEFAULT_ARANEA_ROOT = "araneaApplicationRoot";    
  public static final String DEFAULT_ARANEA_CUSTOM_CONF_XML = "/WEB-INF/aranea-conf.xml";
  public static final String DEFAULT_ARANEA_CUSTOM_CONF_PROPERTIES = "/WEB-INF/aranea-conf.properties";
  
  public static final String ARANEA_CUSTOM_CONF_XML_INIT_PARAMETER = "araneaCustomConfXML";
  public static final String ARANEA_CUSTOM_CONF_PROPERTIES_INIT_PARAMETER = "araneaCustomConfProperties";
  public static final String ARANEA_START_CLASS_INIT_PARAMETER = "araneaApplicationStart";
  public static final String ARANEA_ROOT_INIT_PARAMETER = "araneaApplicationRoot";
  
  protected GenericWebApplicationContext webAppCtx;
  
  public void init() throws ServletException {    
    //Reading init-param's
    String araneaCustomConfXml = DEFAULT_ARANEA_CUSTOM_CONF_XML;    
    if (getServletConfig().getInitParameter(ARANEA_CUSTOM_CONF_XML_INIT_PARAMETER) != null)
      araneaCustomConfXml = getServletConfig().getInitParameter(ARANEA_CUSTOM_CONF_XML_INIT_PARAMETER);    
    
    String araneaCustomConfProperties = DEFAULT_ARANEA_CUSTOM_CONF_PROPERTIES;    
    if (getServletConfig().getInitParameter(ARANEA_CUSTOM_CONF_PROPERTIES_INIT_PARAMETER) != null)
      araneaCustomConfXml = getServletConfig().getInitParameter(ARANEA_CUSTOM_CONF_PROPERTIES_INIT_PARAMETER);    

    //Getting the Spring loaded main web application context
    WebApplicationContext beanFactory = WebApplicationContextUtils.getWebApplicationContext(getServletContext());                     
    
    //Loading default Aranea configuration
    XmlBeanFactory rootConf = new XmlBeanFactory(new ClassPathResource(ARANEA_DEFAULT_CONF_XML), beanFactory);

    
    //Loading default properties
    PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
    cfg.setLocation(new ClassPathResource(ARANEA_DEFAULT_CONF_PROPERTIES));
        
    //Loading custom properties
    Properties localConf = new Properties();
    try {
      if (getServletContext().getResource(araneaCustomConfProperties) != null)
        localConf.load(getServletContext().getResourceAsStream(araneaCustomConfProperties));
    }
    catch (IOException e) {
      throw new ServletException(e);
    }
    cfg.setProperties(localConf);
    cfg.setLocalOverride(true);
    
    //Applying properties to default configuration
    cfg.postProcessBeanFactory(rootConf);
    
    
    //Loading custom configuration 
    try {
      if (getServletContext().getResource(araneaCustomConfXml) != null) {    
        XmlBeanDefinitionReader localConfReader = new XmlBeanDefinitionReader(rootConf);
        localConfReader.loadBeanDefinitions(new ServletContextResource(getServletContext(), araneaCustomConfXml));
      }
    }
    catch (BeansException e) {
      throw new AraneaRuntimeException(e);
    }
    catch (MalformedURLException e) {
      throw new AraneaRuntimeException(e);
    }
    
    
    //Reading the starting widget from an init parameter
    if (getServletConfig().getInitParameter(ARANEA_START_CLASS_INIT_PARAMETER) != null) {
      Class startClass;
      try {
        startClass = ResourceResolverUtil.getDefaultClassLoader().loadClass(
            getServletConfig().getInitParameter(ARANEA_START_CLASS_INIT_PARAMETER));
      }
      catch (ClassNotFoundException e) {
        throw new AraneaRuntimeException(e);
      }
      
      rootConf.registerBeanDefinition(ARANEA_START, new RootBeanDefinition(startClass));
    }
    
    //Making a resulting web application context    
    webAppCtx = new GenericWebApplicationContext(rootConf);
    webAppCtx.setParent(beanFactory);
    webAppCtx.refresh();
    
    super.init();        
  }  
  
  protected ServletServiceAdapterComponent buildRootComponent() {
    //Getting the Aranea root component name
    String araneaRoot = DEFAULT_ARANEA_ROOT;    
    if (getServletConfig().getInitParameter(ARANEA_ROOT_INIT_PARAMETER) != null)
      araneaRoot = getServletConfig().getInitParameter(ARANEA_ROOT_INIT_PARAMETER);    
    
    //Creating the root bean
    ServletServiceAdapterComponent adapter = 
      (ServletServiceAdapterComponent) webAppCtx.getBean(araneaRoot);
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
