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

/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.framework.FilterService;
import org.araneaframework.framework.core.BaseFilterService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

/**
 * This is a utility service to enable custom filter service chains in XML configuration. More specifically, once it is
 * defined as a (Spring) bean in the <code>default-aranea-conf.xml</code> and its <code>beanId</code> property is set,
 * upon initialization it will try to find the bean <code>beanId</code> and add it as a child service (therefore, the
 * bean should indeed be a <i>service</i>!).
 * <p>
 * To see it in use, look at the <code>default-aranea-conf.xml</code> and then the <code>aranea-conf.xml</code> file.
 */
public class SpringOptionalFilterService extends BaseFilterService {

  private static final Log LOG = LogFactory.getLog(SpringOptionalFilterService.class);

  private String beanId;

  /**
   * A bean property to define the <code>beanId</code> that will be used to find the bean that will be incorporated as a
   * child service.
   * 
   * @param beanName The name of the bean to incorporate.
   */
  public void setBeanId(String beanName) {
    this.beanId = beanName;
  }

  /**
   * During initialization it will try to locate and add the bean with Id of <code>beanId</code> as its child service.
   * Note that the bean must be an instance of {@link FilterService}.
   */
  @Override
  protected void init() throws Exception {
    try {
      BeanFactory bf = getEnvironment().getEntry(BeanFactory.class);
      FilterService filter = (FilterService) bf.getBean(this.beanId);

      filter.setChildService(getChildService());
      setChildService(filter);

      if (LOG.isDebugEnabled()) {
        LOG.debug("Found optional bean '" + this.beanId + "'");
      }
    } catch (NoSuchBeanDefinitionException e) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Could not find optional bean '" + this.beanId + "'");
      }
    }

    super.init();
  }

}
