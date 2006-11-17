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

package org.araneaframework.integration.spring;

/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
import org.apache.log4j.Logger;
import org.araneaframework.framework.FilterWidget;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public class SpringOptionalFilterWidget extends BaseFilterWidget {
  private static final Logger log = Logger.getLogger(SpringOptionalFilterWidget.class);
  
  private String beanId;
  
  public void setBeanId(String beanName) {
    this.beanId = beanName;
  }
  
  protected void init() throws Exception {       
    BeanFactory bf = (BeanFactory) getEnvironment().getEntry(BeanFactory.class);
    try {
      FilterWidget filter = (FilterWidget) bf.getBean(beanId);
      filter.setChildWidget(childWidget);
      childWidget = filter;
      
      log.debug("Found optional bean '" + beanId + "'");
    }
    catch (NoSuchBeanDefinitionException e) {
      log.debug("Could not find optional bean '" + beanId + "'");
    }        
    
    super.init();       
  }
}
