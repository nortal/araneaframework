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

package org.araneaframework.example.main;

import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.Scope;
import org.araneaframework.example.common.framework.ViewSelectorAware;
import org.araneaframework.example.main.business.data.IGeneralDAO;
import org.araneaframework.http.PopupWindowContext;
import org.araneaframework.integration.spring.SpringInjectionUtil;
import org.araneaframework.uilib.core.BaseUIWidget;
import org.springframework.beans.factory.BeanFactory;

/**
 * This is a base class for all widgets in this application.
 * 
 * @author Rein Raudjärv (rein@araneaframework.org)
 */
public abstract class TemplateBaseWidget extends BaseUIWidget implements ViewSelectorAware {

  @Override
  protected BeanFactory getBeanFactory() {
    return getEnvironment().getEntry(BeanFactory.class);
  }

  protected SecurityContext getSecCtx() {
    return getEnvironment().getEntry(SecurityContext.class);
  }

  @Override
  protected PopupWindowContext getPopupCtx() {
    return getEnvironment().requireEntry(PopupWindowContext.class);
  }

  /**
   * Retrieves the general DAO for  use with given model. The parameter is not actually used
   * @param <T>
   * @param modelClass
   * @return
   */
  public IGeneralDAO getGeneralDAO() {
    return (IGeneralDAO) getBeanFactory().getBean("generalDAO");
  }

  public String getViewSelector() {
    return this.viewSelector;
  }

  @Override
  public Component.Interface _getComponent() {
    return new ComponentImpl();
  }

  /**
   * Note that this class is referenced in <code>_getComponent()</code>. The purpose of this class is to introduce
   * Spring dependency integration in Aranea. The <code>ComponentImpl</code> takes care of invoking the right methods.
   * As can be seen from the <code>init()</code> method below, first we inject dependencies, and then we call
   * <code>init()</code> of the super class that also calls widget's <code>init()</code> method.
   * <p>
   * Every project that wishes to use Spring injection in Aranea, must create similar solution in their base widget.
   */
  protected class ComponentImpl extends BaseUIWidget.ComponentImpl {

    @Override
    public void init(Scope scope, Environment env) {
      SpringInjectionUtil.injectBeans(env, TemplateBaseWidget.this);
      super.init(scope, env);
    }
  }
}
