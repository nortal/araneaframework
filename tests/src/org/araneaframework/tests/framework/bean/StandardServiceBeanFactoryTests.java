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

import junit.framework.TestCase;
import org.araneaframework.Service;
import org.araneaframework.integration.spring.SpringServiceFactory;
import org.araneaframework.mock.core.MockEventfulStandardService;
import org.araneaframework.tests.mock.MockEnvironment;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class StandardServiceBeanFactoryTests extends TestCase {
  private SpringServiceFactory factory;
  private MockBeanFactory beanFactory;
  
  public void setUp() throws Exception {
    factory = new SpringServiceFactory();
    beanFactory = new MockBeanFactory();
    
    factory.setBeanFactoryClass(MockBeanFactory.class);
  }
  
  public void testBuildService() {
    Service service = new MockEventfulStandardService();
    beanFactory.setBean(service);
    
    factory.setBeanId("beanId");
    assertEquals(service, factory.buildService(new MockEnvironment()));
    assertEquals("beanId",beanFactory.getBeanId());
  }
}
