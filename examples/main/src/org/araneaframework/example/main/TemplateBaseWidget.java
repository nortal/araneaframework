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

package org.araneaframework.example.main;

import org.apache.log4j.Logger;
import org.araneaframework.example.main.business.data.GeneralDAO;
import org.araneaframework.uilib.core.StandardPresentationWidget;
import org.springframework.beans.factory.BeanFactory;

/**
 * This is a base class for all widgets in this application.
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public abstract class TemplateBaseWidget extends StandardPresentationWidget {
  private static final Logger log = Logger.getLogger(TemplateBaseWidget.class);
		
  protected BeanFactory getBeanFactory() {
    return (BeanFactory) getEnvironment().getEntry(BeanFactory.class);
  }
  
  protected SecurityContext getSecCtx() {
    return (SecurityContext) getEnvironment().getEntry(SecurityContext.class);
  }
  
	public GeneralDAO getGeneralDAO() {
		return (GeneralDAO) getBeanFactory().getBean("generalDAO");
	}
}
