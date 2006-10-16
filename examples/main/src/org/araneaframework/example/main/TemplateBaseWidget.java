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

import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.example.common.framework.ViewSelectorAware;
import org.araneaframework.example.main.business.data.GeneralDAO;
import org.araneaframework.http.PopupWindowContext;
import org.araneaframework.integration.spring.SpringInjectionUtil;
import org.araneaframework.uilib.core.BaseUIWidget;
import org.springframework.beans.factory.BeanFactory;

/**
 * This is a base class for all widgets in this application.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public abstract class TemplateBaseWidget extends BaseUIWidget implements ViewSelectorAware {
  protected BeanFactory getBeanFactory() {
    return (BeanFactory) getEnvironment().getEntry(BeanFactory.class);
  }
  
  protected SecurityContext getSecCtx() {
    return (SecurityContext) getEnvironment().getEntry(SecurityContext.class);
  }
  
  protected PopupWindowContext getPopupCtx() {
    return (PopupWindowContext) getEnvironment().requireEntry(PopupWindowContext.class);
  }
  
  public GeneralDAO getGeneralDAO() {
    return (GeneralDAO) getBeanFactory().getBean("generalDAO");
  }

  public String getViewSelector() {
    return viewSelector;
  }
  
  public Component.Interface _getComponent() {
    return new ComponentImpl();
  }
  
  protected class ComponentImpl extends BaseUIWidget.ComponentImpl {
    public void init(Environment env) {
      SpringInjectionUtil.injectBeans(env, TemplateBaseWidget.this);
  
      super.init(env);
    }
  }
}
