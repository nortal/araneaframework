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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.araneaframework.servlet.ServletServiceAdapterComponent;
import org.araneaframework.servlet.core.BaseAraneaDispatcherServlet;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class AraneaSpringDispatcherServlet extends BaseAraneaDispatcherServlet {
  
  public static final String DEFAULT_ARANEA_ROOT = "applicationRoot";
  public static final String ARANEA_ROOT_INIT_PARAMETER_NAME = "aranea-application-root";
  
	protected WebApplicationContext beanFactory;

	public void init() throws ServletException {
    beanFactory = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());   
    
		super.init();
	}
  
  protected ServletServiceAdapterComponent buildRootComponent() {
    String araneaRoot = DEFAULT_ARANEA_ROOT;
    
    if (getServletConfig().getInitParameter(ARANEA_ROOT_INIT_PARAMETER_NAME) != null)
      araneaRoot = getServletConfig().getInitParameter(ARANEA_ROOT_INIT_PARAMETER_NAME);
    
    ServletServiceAdapterComponent adapter = 
      (ServletServiceAdapterComponent) beanFactory.getBean(araneaRoot);
    return adapter;
  }
  
  protected Map getEnvironmentEntries() { 
  	Map result = new HashMap();
  	result.put(BeanFactory.class, beanFactory);
    result.put(ApplicationContext.class, beanFactory);
    result.put(WebApplicationContext.class, beanFactory);    
  	return result;
  }
}
